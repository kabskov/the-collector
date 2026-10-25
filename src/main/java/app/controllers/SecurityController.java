package app.controllers;

import app.dtos.LoginDTO;
import app.dtos.TokenDTO;
import app.dtos.UserDTO;
import app.entities.Role;
import app.exceptions.ApiException;
import app.services.UserService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.util.Date;
import java.util.Map;

public class SecurityController {

    private static final String ISSUER = "the-collector";
    private static final long EXPIRATION_MS = 3_600_000; // 1 time

    private final UserService userService;
    private final String secret = System.getenv("SECRET_KEY");

    public SecurityController(EntityManagerFactory emf) {
        if (secret == null) {
            throw new IllegalStateException("SECRET_KEY is not set");
        }
        this.userService = new UserService(emf);
    }

    public void login(Context ctx) {
        LoginDTO loginDTO = ctx.bodyAsClass(LoginDTO.class);
        UserDTO verifiedUser = userService.login(loginDTO);
        ctx.json(new TokenDTO(createToken(verifiedUser), verifiedUser.getId()));
    }

    public static Long getUserId(Context ctx) {
        return ctx.attribute("userId");
    }

    public static void requireOwner(Context ctx, Long pathUserId) {
        if (!pathUserId.equals(getUserId(ctx))) {
            throw new ApiException(403, "Forbidden");
        }
    }

    private String createToken(UserDTO userDTO) {
        String token = JWT.create()
                .withIssuer(ISSUER)
                .withSubject(String.valueOf(userDTO.getId()))
                .withClaim("role", userDTO.getRole().name())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .sign(Algorithm.HMAC256(secret));

        return token;
    }

    public void authenticate(Context ctx) {
        if (ctx.routeRoles().contains(Role.ANYONE)) return; // Åbne routes.
        String header = ctx.header("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new ApiException(401, "Missing token");
        }

        try {
            DecodedJWT jwt = JWT.require(Algorithm.HMAC256(secret)).withIssuer(ISSUER).build()
                    .verify(header.substring(7));
            ctx.attribute("userId", Long.valueOf(jwt.getSubject()));
            ctx.attribute("role", Role.valueOf(jwt.getClaim("role").asString()));
        } catch (JWTVerificationException e) {
            throw new ApiException(401, "Invalid or expired token");
        }
    }

    public void authorize(Context ctx) {
        if (ctx.routeRoles().contains(Role.ANYONE)) return; // Åbne routes.
        Role role = ctx.attribute("role");
        if (!ctx.routeRoles().contains(role)) {
            throw new ApiException(403, "Forbidden");
        }
    }

    public void healthCheck(Context ctx) {
        ctx.status(200).json(Map.of("msg", "API is up and running"));
    }
}
