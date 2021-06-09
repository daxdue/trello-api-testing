package ru.training.apitesting.services;

import com.google.gson.Gson;
import ru.training.apitesting.constant.PathParams;
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

    private String pathPattern;
    private Method requestMethod;
    private Map<String, String> queryParams;
    private Map<String, String> pathParams;

    public TrelloBoardService(Map<String, String> parameters, Map<String, String> pathParams,
                              Method requestMethod, String pathPattern) {
        this.queryParams = parameters;
        this.requestMethod = requestMethod;
        this.pathParams = pathParams;
        this.pathPattern = pathPattern;
    }

    public static RequestSpecification requestSpecBase() {
        return new RequestSpecBuilder()
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .setBaseUri(PropertyManager.getApiBaseUrl())
                .addQueryParam(QueryParams.API_KEY, PropertyManager.getApiKey())
                .addQueryParam(QueryParams.API_TOKEN, PropertyManager.getApiToken())
                .build();
    }

    public static RequestSpecification requestSpecWithQueryParams(Map<String, String> queryParams) {
        return new RequestSpecBuilder()
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .setBaseUri(PropertyManager.getApiBaseUrl())
                .addQueryParam(QueryParams.API_KEY, PropertyManager.getApiKey())
                .addQueryParam(QueryParams.API_TOKEN, PropertyManager.getApiToken())
                .addQueryParams(queryParams)
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

    public static RequestSpecification requestSpecWithQueryAndPathParams(
            Map<String, String> queryParams,
            Map<String, String> pathParams) {

        return new RequestSpecBuilder()
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .setBaseUri(PropertyManager.getApiBaseUrl())
                .addQueryParam(QueryParams.API_KEY, PropertyManager.getApiKey())
                .addQueryParam(QueryParams.API_TOKEN, PropertyManager.getApiToken())
                .addQueryParams(queryParams)
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

    public static RequestSpecification requestSpecification(
            RequestSpecType requestSpecType,
            Map<String, String> queryParams,
            Map<String, String> pathParams) {

        RequestSpecification requestSpecification = null;

        switch (requestSpecType) {
            case PATH_PARAMS:
                requestSpecification =  requestSpecWithPathParams(pathParams);
                break;

            case QUERY_PARAMS:
                requestSpecification = requestSpecWithQueryParams(queryParams);
                break;

            case QUERY_AND_PATH_PARAMS:
                requestSpecification = requestSpecWithQueryAndPathParams(queryParams, pathParams);
                break;

            default:
                requestSpecification = requestSpecBase();
                break;
        }

        return requestSpecification;
    }

    public ValidatableResponse sendRequest(RequestSpecType requestSpecType,
            ResponseSpecification responseSpecification) {

        return RestAssured
                .given(requestSpecification(requestSpecType, queryParams, pathParams))
                .when()
                    .request(requestMethod,PropertyManager.getApiBaseUrl() + pathPattern)
                .then().log().all()
                    .spec(responseSpecification);
    }

    public static TrelloBoard getBoardFromResponse(Response response) {
        TrelloBoard trelloBoard = new Gson().fromJson(response.asString().trim(), TrelloBoard.class);
        return trelloBoard;
    }

    public static SpecificResponse getSpecificResponse(Response response) {
        SpecificResponse specificResponse = new Gson().fromJson(
                response.asString().trim(), SpecificResponse.class);
        return specificResponse;
    }

    public static String getStringFromResponse(Response response) {
        return response.asString().trim();
    }

    public static ApiRequestBuilder requestBuilder() {
        return new ApiRequestBuilder();
    }

    //Request builder class
    public static class ApiRequestBuilder {

        private String pathPattern = "";
        private Method requestMethod = Method.GET;
        private Map<String, String> params = new HashMap<>();
        private Map<String, String> pathParams = new HashMap<>();

        public ApiRequestBuilder setRequestMethod(Method method) {
            this.requestMethod = method;
            return this;
        }

        public ApiRequestBuilder setBoardName(String boardName) {
            params.put(QueryParams.BOARD_NAME, boardName);
            return this;
        }

        public ApiRequestBuilder addPathParam(String key, String value) {
            this.pathParams.put(key, value);
            this.pathPattern += String.format(PathParams.PATH_PATTERN, key);
            return this;
        }

        public TrelloBoardService build() {
            return new TrelloBoardService(params, pathParams, requestMethod, pathPattern);
        }
    }
}
