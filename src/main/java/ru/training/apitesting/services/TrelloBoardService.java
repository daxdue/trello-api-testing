package ru.training.apitesting.services;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;

import java.util.HashMap;
import java.util.Map;

public class TrelloBoardService {

    private Map<String, Object> parameters;
    private Method requestMethod;
    private static final String BASE_TRELLO_URI = "https://api.trello.com/1";

    public static RequestSpecification requestSpec() {
        return new RequestSpecBuilder()
                .setAccept(ContentType.JSON)
                .setBaseUri(BASE_TRELLO_URI)
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

    public Response sendRequest() {
        return RestAssured
                .given(requestSpec()).log().all()
                .queryParams(parameters)
                .request(requestMethod, BASE_TRELLO_URI)
                .prettyPeek();

    }

    public static class ApiRequestBuilder {
        private Map<String, String> params = new HashMap<>();
        private Method requestMethod = Method.GET;

        public ApiRequestBuilder setRequestMethod(Method method) {
            this.requestMethod = method;
            return this;
        }
    }
}
