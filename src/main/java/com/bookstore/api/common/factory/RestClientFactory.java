package com.bookstore.api.common.factory;

import com.bookstore.api.common.restclient.RestClientWrapper;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class RestClientFactory {

    static {
        RestAssured.useRelaxedHTTPSValidation();
    }

    public static RestClientWrapper getClient(String baseUri) {

        RequestSpecification requestSpecification = RestAssured
                .given()
                .baseUri(baseUri)
                .filters(new RequestLoggingFilter(), new ResponseLoggingFilter())
                .contentType(ContentType.JSON);

        return new RestClientWrapper(requestSpecification);
    }
}
