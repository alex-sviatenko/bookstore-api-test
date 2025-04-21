package com.bookstore.api.rest.client;

import com.bookstore.api.common.annotation.RestClient;
import com.bookstore.api.common.client.ApiClient;
import com.bookstore.api.common.configuration.Properties;
import com.bookstore.api.common.factory.RestClientFactory;
import com.bookstore.api.common.restclient.HttpResponseWrapper;
import com.bookstore.api.common.restclient.RestClientWrapper;
import org.springframework.beans.factory.annotation.Autowired;

@RestClient
public class AuthorClient implements ApiClient {

    public static final String API_V1_AUTHORS = "/api/v1/Authors";
    public static final String API_V1_AUTHORS_ID = "/api/v1/Authors/{id}";

    @Autowired
    private Properties properties;

    @Override
    public RestClientWrapper getClient() {
        return RestClientFactory.getClient(properties.appUrl);
    }

    public HttpResponseWrapper sendGetAuthorsRequest() {
        return getClient()
                .get(API_V1_AUTHORS);
    }

    public HttpResponseWrapper sendGetAuthorByIdRequest(int authorId) {
        return getClient()
                .pathParam("id", authorId)
                .get(API_V1_AUTHORS_ID);
    }

    public HttpResponseWrapper sendPostAuthorRequest(Object body) {
        return getClient()
                .body(body)
                .post(API_V1_AUTHORS);
    }

    public HttpResponseWrapper sendPutAuthorRequest(int authorId, Object body) {
        return getClient()
                .pathParam("id", authorId)
                .body(body)
                .put(API_V1_AUTHORS_ID);
    }

    public HttpResponseWrapper sendDeleteAuthorRequest(Integer authorId) {
        return getClient()
                .pathParam("id", authorId)
                .delete(API_V1_AUTHORS_ID);
    }
}
