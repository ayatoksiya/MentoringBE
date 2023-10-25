import helpers.URLResolver;
import helpers.UserData;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class CRUDTest {
    ValidatableResponse response;

    @Test
    public void getAllUsers() {

        RequestSpecification requestSpecification = getBaseRequestSpecification();

        response = given().spec(requestSpecification).log().all()
                .when().get("/users")
                .then().statusCode(200)
                .log().all();

        JsonPath jsonPath = response.extract().jsonPath();

        List<Integer> usersId = jsonPath.getList("data.id");
        System.out.println(usersId);
    }

    @Test
    public void getUserWithId3() {

        RequestSpecification requestSpecification = getBaseRequestSpecification();

        given().spec(requestSpecification).log().all()
                .when().get("/users/3")
                .then().statusCode(200)
                .body("data.email", equalTo("emma.wong@reqres.in"))
                .log().all();
    }

    @Test
    public void postNewDataForUserWithID3() {

        RequestSpecification requestSpecification = getBaseRequestSpecification();

        given().spec(requestSpecification).log().all()
                .when().body(UserData.getNewUserData()).post("/users/3")
                .then().statusCode(201)
                .log().all();
    }

    @Test
    public void putNewDataForUserWithID3() {

        RequestSpecification requestSpecification = getBaseRequestSpecification();

        given().spec(requestSpecification).log().all()
                .when().body(UserData.getNewUserPasswordData()).put("/users/3")
                .then().statusCode(200)
                .log().all();
    }

    @Test
    public void deleteUserWithID3() {

        RequestSpecification requestSpecification = getBaseRequestSpecification();

        given().spec(requestSpecification).log().all()
                .when().delete("/users/3")
                .then().statusCode(204)
                .log().all();
    }

    private static RequestSpecification getBaseRequestSpecification() {
        return new RequestSpecBuilder()
                .setBaseUri(URLResolver.baseURI)
                .setAccept(ContentType.JSON)
                .build();
    }
}
