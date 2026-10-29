package app.routes;

import app.config.ApplicationConfig;
import app.config.HibernateTestConfig;
import app.testutils.TestData;
import app.testutils.TestPopulator;
import io.javalin.Javalin;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;

import java.util.Map;

import static io.restassured.RestAssured.given;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class RouteTestBase {

    protected static final int PORT = 7777;
    protected static final String PASSWORD = "test1234";

    protected final EntityManagerFactory emf = HibernateTestConfig.getEntityManagerFactory();
    protected Javalin app;
    protected TestData testData;

    @BeforeAll
    void startServer() {
        app = ApplicationConfig.startServer(PORT, emf);
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = PORT;
        RestAssured.basePath = "/api/v1";
    }

    @BeforeEach
    void resetData() {
        testData = TestPopulator.populate(emf);
    }

    @AfterAll
    void stopServer() {
        app.stop();
    }

    //Hjælpemetode, der logger ind og returnerer token.
    protected String login(String email) {
        return given()
                .contentType(ContentType.JSON)
                .body(Map.of("email", email, "password", PASSWORD))
                .when().post("/auth/login")
                .then().statusCode(200)
                .extract().path("token");
    }
}
