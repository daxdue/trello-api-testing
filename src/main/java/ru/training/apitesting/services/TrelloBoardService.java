package ru.training.apitesting.services;

import ru.training.apitesting.beans.TrelloBoard;
import com.google.gson.Gson;
import ru.training.apitesting.constant.QueryParams;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import ru.training.apitesting.utils.PropertyManager;

import java.util.HashMap;
import java.util.Map;

import static ru.training.apitesting.constant.QueryParams.BOARD_ID_PATH;

public class TrelloBoardService {
    //TODO Refactor with property parameter
    //private static final String BASE_TRELLO_URI = "https://api.trello.com/1/boards";
    private String pathParam;
    private Method requestMethod;
    private Map<String, String> parameters;

    public TrelloBoardService(Map<String, String> parameters, Method requestMethod, String pathParam) {
        this.parameters = parameters;
        this.requestMethod = requestMethod;
        this.pathParam = pathParam;
    }

    public static RequestSpecification requestSpec() {
        return new RequestSpecBuilder()
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .setBaseUri(/*BASE_TRELLO_URI*/PropertyManager.getApiBaseUrl())
                .addQueryParam(QueryParams.API_KEY, PropertyManager.getApiKey())
                .addQueryParam(QueryParams.API_TOKEN, PropertyManager.getApiToken())
                .build();
    }

    public static ResponseSpecification goodResponseSpec() {
        return new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .expectStatusCode(HttpStatus.SC_OK)
                .build();
    }

    public static ResponseSpecification badResponseSpec() {
        return new ResponseSpecBuilder()
                .expectContentType(ContentType.TEXT)
                .expectStatusCode(Matchers.oneOf(
                        HttpStatus.SC_BAD_REQUEST,
                        HttpStatus.SC_FORBIDDEN,
                        HttpStatus.SC_NOT_FOUND))
                .build();
    }

    public static ResponseSpecification baseResponseSpec() {

        return new ResponseSpecBuilder()
                .expectResponseTime(Matchers.lessThan(10000L))
                .build();
    }

    public ValidatableResponse sendRequest(ResponseSpecification responseSpecification) {

        return RestAssured
                .given(requestSpec()).log().all()
                    .spec(requestSpec())
                    .pathParam("id", pathParam)
                    .queryParams(parameters)
                .when()
                    .request(requestMethod, /*BASE_TRELLO_URI*/ PropertyManager.getApiBaseUrl() + BOARD_ID_PATH)
                .then().log().all()
                    .spec(responseSpecification);
    }


    public static ApiRequestBuilder requestBuilder() {
        return new ApiRequestBuilder();
    }

    //Request builder class
    public static class ApiRequestBuilder {
        private String pathParam = "";
        private Map<String, String> params = new HashMap<>();
        private Method requestMethod = Method.GET;

        public ApiRequestBuilder setRequestMethod(Method method) {
            this.requestMethod = method;
            return this;
        }

        public ApiRequestBuilder setApiKey(String apiKey) {
            params.put(QueryParams.API_KEY, apiKey);
            return this;
        }

        public ApiRequestBuilder setApiToken(String apiToken) {
            params.put(QueryParams.API_TOKEN, apiToken);
            return this;
        }

        public ApiRequestBuilder setBoardName(String boardName) {
            params.put(QueryParams.BOARD_NAME, boardName);
            return this;
        }

        public ApiRequestBuilder setPathParam(String pathParam) {
            this.pathParam = new String(pathParam);
            return this;
        }

        public TrelloBoardService build() {
            return new TrelloBoardService(params, requestMethod, pathParam);
        }
    }

    public static TrelloBoard getBoardFromResponse(Response response) {
        TrelloBoard trelloBoard = new Gson().fromJson(response.asString().trim(), TrelloBoard.class);
        return trelloBoard;
    }

}
