package ru.training.apitesting.services;

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
import ru.training.apitesting.constant.RequestSpecType;
import ru.training.apitesting.utils.PropertyManager;
import ru.training.apitesting.beans.*;

import java.util.HashMap;
import java.util.Map;

public class TrelloBoardService {

    private String pathParam;
    private Method requestMethod;
    private Map<String, String> queryParams;
    private Map<String, String> pathParams;

    public TrelloBoardService(Map<String, String> parameters, Map<String, String> pathParams,
                              Method requestMethod) {
        this.queryParams = parameters;
        this.requestMethod = requestMethod;
        this.pathParam = pathParam;
    }

    public static RequestSpecification requestSpec() {
        return new RequestSpecBuilder()
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .setBaseUri(PropertyManager.getApiBaseUrl())
                .addQueryParam(QueryParams.API_KEY, PropertyManager.getApiKey())
                .addQueryParam(QueryParams.API_TOKEN, PropertyManager.getApiToken())
                .build();
    }

    public static RequestSpecification requestSpecWithPathParams(Map<String, String> pathParams) {
        return new RequestSpecBuilder()
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .setBaseUri(PropertyManager.getApiBaseUrl())
                .addQueryParam(QueryParams.API_KEY, PropertyManager.getApiKey())
                .addQueryParam(QueryParams.API_TOKEN, PropertyManager.getApiToken())
                .addPathParams(pathParams)
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

    public static RequestSpecification requestSpecification(RequestSpecType requestSpecType,
                                                            Map<String, String> queryParams,
                                                            Map<String, String> pathParams) {
        if (requestSpecType == RequestSpecType.WITH_PATH_PARAMS) {
            return requestSpecWithPathParams(pathParams);
        }
        return requestSpec();
    }

    public ValidatableResponse sendRequest(RequestSpecType requestSpecType,
            ResponseSpecification responseSpecification) {

        return RestAssured
                .given(requestSpec()).log().all()
                    .spec(requestSpecification(requestSpecType, queryParams, pathParams))
                    /*.pathParam("id", pathParam)*/
                    /*.queryParams(queryParams)*/
                .when()
                    .request(requestMethod/*,PropertyManager.getApiBaseUrl() + BOARD_ID_PATH*/)
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
        private Map<String, String> pathParams = new HashMap<>();
        private Method requestMethod = Method.GET;

        public ApiRequestBuilder setRequestMethod(Method method) {
            this.requestMethod = method;
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

        public ApiRequestBuilder addPathParam(String key, String value) {
            this.pathParams.put(key, value);
            return this;
        }

        public TrelloBoardService build() {
            return new TrelloBoardService(params, pathParams, requestMethod);
        }
    }

    public static TrelloBoard getBoardFromResponse(Response response) {
        TrelloBoard trelloBoard = new Gson().fromJson(response.asString().trim(), TrelloBoard.class);
        return trelloBoard;
    }

    public static String getStringFromResponse(Response response) {
        return response.asString().trim();
    }

}
