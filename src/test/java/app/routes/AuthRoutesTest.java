package app.routes;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

//Rest Assured tests for user story 2
public class AuthRoutesTest extends RouteTestBase {

    //User story 2:
    //As a rock collector, I want to log in, so I can see my own finds.

    //Given I have an account, when I log in with the correct email and password, then I get 200 with a token and my user id.
    @Test
    void loginWithCorrectCredentials() {
        given()
                .contentType(ContentType.JSON)
                .body(Map.of("email", "katarina@mail.dk", "password", PASSWORD))
                .when()
                .post("auth/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .body("userId", equalTo(testData.users.getFirst().getId().intValue()));
    }

    //Given I have an account, when I log in with the wrong password, then I get 401 "Wrong email or password".
    @Test
    void loginWithWrongPassword() {
        given()
                .contentType(ContentType.JSON)
                .body(Map.of("email", "katarina@mail.dk", "password", "wrongPassword"))
                .when()
                .post("auth/login")
                .then()
                .statusCode(401)
                .body("status", equalTo(401))
                .body("msg", equalTo("Wrong email or password"));
    }

    //Given the email does not exist, when I log in, then I get the same 401 as for a wrong password.
    @Test
    void loginWithWrongEmail() {
        given()
                .contentType(ContentType.JSON)
                .body(Map.of("email", "wrong@mail.dk", "password", PASSWORD))
                .when()
                .post("/auth/login")
                .then()
                .statusCode(401)
                .body("status", equalTo(401))
                .body("msg", equalTo("Wrong email or password"));
    }

    //When I log in without email or password, then I get 400.
    @Test
    void loginWithMissingEmail() {
        given()
                .contentType(ContentType.JSON)
                .body(Map.of("email", "", "password", PASSWORD))
                .when()
                .post("/auth/login")
                .then()
                .statusCode(400)
                .body("status", equalTo(400))
                .body("msg", equalTo("Email is required"));
    }

    //When I log in without email or password, then I get 400.
    @Test
    void loginWithMissingPassword() {
        given()
                .contentType(ContentType.JSON)
                .body(Map.of("email", "katarina@mail.dk", "password", ""))
                .when()
                .post("/auth/login")
                .then()
                .statusCode(400)
                .body("status", equalTo(400))
                .body("msg", equalTo("Password is required"));
    }
}
