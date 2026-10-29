package app.routes;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

//Rest Assured tests for user story 1
public class UserRoutesTest extends RouteTestBase {

    //User story 1:
    //As a rock collector, I want to create an account, so I can register my finds.

    //Given I do not have an account, when I create a user with name, email and password, then I get 201 and my details without the password.
    @Test
    void createUserWithCorrectCredentials() {
        given()
                .contentType(ContentType.JSON)
                .body(Map.of("name", "Berit", "email", "berit@mail.dk", "password", PASSWORD))
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .body("name", equalTo("Berit"))
                .body("email", equalTo("berit@mail.dk"))
                .body("$", not(hasKey("password")));
    }

    //Given I do not have an account, when I create a user without name, email or password, then I get 400.
    @Test
    void createUserWithNullName() {
        Map<String, Object> body = new HashMap<>();
        body.put("name", null);
        body.put("email", "berit@mail.dk");
        body.put("password", PASSWORD);

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/users")
                .then()
                .statusCode(400)
                .body("status", equalTo(400))
                .body("msg", equalTo("Name is required"));
    }

    //Given I do not have an account, when I create a user without name, email or password, then I get 400.
    @Test
    void createUserWithBlankEmail() {
        given()
                .contentType(ContentType.JSON)
                .body(Map.of("name", "Berit", "email", "", "password", PASSWORD))
                .when()
                .post("/users")
                .then()
                .statusCode(400)
                .body("status", equalTo(400))
                .body("msg", equalTo("Email is required"));
    }

    //Given I do not have an account, when I create a user without name, email or password, then I get 400.
    @Test
    void createUserWithMissingPassword() {
        given()
                .contentType(ContentType.JSON)
                .body(Map.of("name", "Berit", "email", "berit@mail.dk"))
                .when()
                .post("/users")
                .then()
                .statusCode(400)
                .body("status", equalTo(400))
                .body("msg", equalTo("Password is required"));
    }

    //Given the email is already in use, when I create a user with that email, then I get 409.
    @Test
    void createUserWithEmailAlreadyInUse() {
        given()
                .contentType(ContentType.JSON)
                .body(Map.of("name", "Berit", "email", "katarina@mail.dk", "password", PASSWORD))
                .when()
                .post("/users")
                .then()
                .statusCode(409)
                .body("status", equalTo(409))
                .body("msg", equalTo("Email already in use"));
    }
}
