package app.routes;

import app.entities.Find;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

//Rest Assured tests for user story 3-10
public class FindRoutesTest extends RouteTestBase {

    //User story 3:
    //As a rock collector, I want to register a find, so I can keep track of what I have found.

    //Given I am logged in, when I register a find with date, photo URL and at least one category, then I get 201 and
    // the find is linked to me.
    @Test
    void registerFindWhenLoggedIn() {
        String token = login("katarina@mail.dk");

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(Map.of("date", "2026-10-10", "photoURL", "url", "categoryIds",
                        List.of(testData.categories.get(2).getId())))
                .when()
                .post("/finds")
                .then()
                .statusCode(201)
                .body("userId", equalTo(testData.users.getFirst().getId().intValue()));
    }

    //Given I am not logged in, when I register a find, then I get 401.
    @Test
    void registerFindWithoutLogin() {
        given()
                .contentType(ContentType.JSON)
                .body(Map.of("date", "2026-10-10", "photoURL", "url", "categoryIds",
                        List.of(testData.categories.get(2).getId())))
                .when()
                .post("/finds")
                .then()
                .statusCode(401);
    }


    //User story 4:
    //As a rock collector, I want to choose a category for my find, so my finds are easy to sort.

    //Given I am not logged in, when I fetch the categories, then I get 200 with the list.
    @Test
    void getCategoriesWithoutLogin() {
        List<Integer> categoryIds = testData.categories.stream()
                .map(category -> category.getId().intValue())
                .toList();

        given()
                .when()
                .get("/categories")
                .then()
                .statusCode(200)
                .body("id", containsInAnyOrder(categoryIds.toArray()));
    }

    //Given I am logged in, when I register a find without categories, then I get 400.
    @Test
    void registerFindWithoutCategory() {
        String token = login("katarina@mail.dk");

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(Map.of("date", "2026-10-10", "photoURL", "url"))
                .when()
                .post("/finds")
                .then()
                .statusCode(400)
                .body("status", equalTo(400))
                .body("msg", equalTo("A find must have at least one category"));
    }

    //Given I am logged in, when I register a find with a category that does not exist, then I get 404.
    @Test
    void registerFindWithCategoryThatDoesNotExist() {
        String token = login("katarina@mail.dk");

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(Map.of("date", "2026-10-10", "photoURL", "url", "categoryIds", List.of(99999L)))
                .when()
                .post("/finds")
                .then()
                .statusCode(404)
                .body("status", equalTo(404));
    }


    //User story 5:
    //As a rock collector, I want to see a list of all my finds, so I get an overview of my collection.

    //Given I am logged in and have finds, when I fetch my finds, then I get 200 with a list of my own finds.
    @Test
    void getOwnFinds() {
        String token = login("katarina@mail.dk");
        Long userId = testData.users.getFirst().getId();

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/users/{userId}/finds", userId)
                .then()
                .statusCode(200)
                .body("size()", is(4))
                .body("userId", everyItem(equalTo(userId.intValue())))
                .body("note", hasItem("En flot hulsten!"));
    }

    //Given I am logged in, when I fetch another user's finds, then I get 403.
    @Test
    void getAnotherUsersFinds() {
        String token = login("katarina@mail.dk");
        Long anotherUsersId = testData.users.getLast().getId();

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/users/{userId}/finds", anotherUsersId)
                .then()
                .statusCode(403)
                .body("status", equalTo(403));
    }


    //User story 6:
    //As a rock collector, I want to edit a find, so I can correct it if I made a mistake.

    //Given I own a find, when I send a change to for example the date, then I get 200 and only the date is changed.
    @Test
    void updateOwnedFind() {
        String token = login("katarina@mail.dk");
        Find find = testData.finds.getFirst();
        Long userId = testData.users.getFirst().getId();
        Long categoryId = testData.categories.get(3).getId();
        String newDate = "2026-10-20";

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(Map.of("date", newDate))
                .when()
                .patch("/users/{userId}/finds/{findId}", userId, find.getId())
                .then()
                .statusCode(200)
                .body("photoURL", equalTo(find.getPhotoURL()))
                .body("note", equalTo(find.getNote()))
                .body("categoryIds", contains(categoryId.intValue()))
                .body("date", equalTo(newDate));
    }

    //Given I own a find, when I send an empty list of categories, then I get 400.
    @Test
    void updateFindWithEmptyCategoryList() {
        String token = login("katarina@mail.dk");
        Long userId = testData.users.getFirst().getId();
        Long findId = testData.finds.getFirst().getId();

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(Map.of("categoryIds", List.of()))
                .when()
                .patch("/users/{userId}/finds/{findId}", userId, findId)
                .then()
                .statusCode(400)
                .body("status", equalTo(400))
                .body("msg", equalTo("A find must have at least one category"));
    }

    //Given the find does not exist or belongs to someone else, when I try to edit it, then I get 404.
    @Test
    void updateUnknownFind() {
        String token = login("katarina@mail.dk");
        Long userId = testData.users.getFirst().getId();
        Long findId = testData.finds.getLast().getId(); // Frederik's find

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(Map.of("date", "2026-05-10"))
                .when()
                .patch("/users/{userId}/finds/{findId}", userId, findId)
                .then()
                .statusCode(404);
    }


    //User story 7:
    //As a rock collector, I want to delete a find, so my collection only contains what I want to keep.

    //Given I own a find, when I delete it, then I get 204 with no body.
    @Test
    void deleteOwnedFind() {
        String token = login("katarina@mail.dk");
        Long userId = testData.users.getFirst().getId();
        Long findId = testData.finds.getFirst().getId();

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("/users/{userId}/finds/{findId}", userId, findId)
                .then()
                .statusCode(204)
                .body(emptyOrNullString());
    }

    //Given I have deleted a find, when I try to fetch it, then I get 404.
    @Test
    void getFindThatHasBeenDeleted() {
        String token = login("katarina@mail.dk");
        Long userId = testData.users.getFirst().getId();
        Long findId = testData.finds.getFirst().getId();

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("/users/{userId}/finds/{findId}", userId, findId)
                .then()
                .statusCode(204);

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/users/{userId}/finds/{findId}", userId, findId)
                .then()
                .statusCode(404)
                .body("msg", equalTo("Find with id " + findId + " not found"));
    }

    //Given the find does not exist or belongs to someone else, when I try to delete it, then I get 404.
    @Test
    void deleteUnknownFind() {
        String token = login("katarina@mail.dk");
        Long userId = testData.users.getFirst().getId();
        Long findId = testData.finds.getLast().getId();

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("/users/{userId}/finds/{findId}", userId, findId)
                .then()
                .statusCode(404);
    }

    //User story 8:
    //As a rock collector, I want to add a find spot to my find, so I can see where I made my finds.

    //Given I am logged in, when I register a find with a find spot keyword, then the name of the place is shown as
    // findSpotName in the response.
    //This test has call to nominatim and is excluded from automated build.
    @Tag("external")
    @Test
    void registerFindWithFindSpotKeywordAndSeeFindSpotNameInResponse() {
        String token = login("katarina@mail.dk");
        String findSpot = "Gilleleje";

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(Map.of("date", "2026-10-10", "photoURL", "url", "categoryIds",
                        List.of(testData.categories.getFirst().getId()), "findSpotKeyword", findSpot))
                .when()
                .post("/finds")
                .then()
                .statusCode(201)
                .body("findSpotName", containsString(findSpot));
    }

    //Given I am logged in, when I register a find without a find spot, then the find is created and findSpotName is null.
    @Test
    void registerFindWithoutFindSpotAndFindSpotNameIsNull() {
        String token = login("katarina@mail.dk");

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(Map.of("date", "2026-10-10", "photoURL", "url", "categoryIds",
                        List.of(testData.categories.getFirst().getId())))
                .when()
                .post("/finds")
                .then()
                .statusCode(201)
                .body("findSpotName", equalTo(null));
    }

    //Given I am logged in, when I enter a place that cannot be found, then I get 404.
    //This test has call to nominatim and is excluded from automated build.
    @Tag("external")
    @Test
    void registerFindWithFindSpotThatCannotBeFound() {
        String token = login("katarina@mail.dk");
        String findSpot = "kjlseøåldejfefe";

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(Map.of("date", "2026-10-10", "photoURL", "url", "categoryIds",
                        List.of(testData.categories.get(2).getId()), "findSpotKeyword", findSpot))
                .when()
                .post("/finds")
                .then()
                .statusCode(404);
    }


    //User story 9:
    //As a rock collector, I want to add a date to my find, so I can see when I found it.

    //Given I am logged in, when I register a find without a date, then I get 400.
    @Test
    void registerFindWithoutDate() {
        String token = login("katarina@mail.dk");

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(Map.of("photoURL", "url", "categoryIds", List.of(testData.categories.get(2).getId())))
                .when()
                .post("/finds")
                .then()
                .statusCode(400)
                .body("status", equalTo(400))
                .body("msg", equalTo("Date is required"));
    }

    //Given I have registered a find with a date, when I fetch it, then the date is shown as yyyy-MM-dd.
    @Test
    void getFindWithDateShownAsyyyyMMdd() {
        String token = login("katarina@mail.dk");
        Long userId = testData.users.getFirst().getId();
        Long findId = testData.finds.getFirst().getId();

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/users/{userId}/finds/{findId}", userId, findId)
                .then()
                .statusCode(200)
                .body("date", equalTo("2026-06-20"));
    }


    //User story 10:
    //As a rock collector, I want to add a note to my find, so I can connect a thought or an event to it.

    //Given I am logged in, when I register a find without a note, then the find is created with an empty note.
    @Test
    void registerFindWithoutNote() {
        String token = login("katarina@mail.dk");

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(Map.of("date", "2026-10-10", "photoURL", "url", "categoryIds",
                        List.of(testData.categories.get(2).getId())))
                .when()
                .post("/finds")
                .then()
                .statusCode(201)
                .body("note", equalTo(""));
    }

    //Given I own a find, when I change the note, then the new note is saved.
    @Test
    void updateFindWithNewNote() {
        String token = login("katarina@mail.dk");
        Long userId = testData.users.getFirst().getId();
        Long findId = testData.finds.getFirst().getId();
        String newNote = "En ny note";

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(Map.of("note", newNote))
                .when()
                .patch("/users/{userId}/finds/{findId}", userId, findId)
                .then()
                .statusCode(200)
                .body("note", equalTo(newNote));
    }
}
