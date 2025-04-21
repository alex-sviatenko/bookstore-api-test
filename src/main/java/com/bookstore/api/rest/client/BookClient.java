package com.bookstore.api.rest.client;

import com.bookstore.api.common.annotation.RestClient;
import com.bookstore.api.common.client.ApiClient;
import com.bookstore.api.common.configuration.Properties;
import com.bookstore.api.common.factory.RestClientFactory;
import com.bookstore.api.common.restclient.HttpResponseWrapper;
import com.bookstore.api.common.restclient.RestClientWrapper;
import org.springframework.beans.factory.annotation.Autowired;

@RestClient
public class BookClient implements ApiClient {

    public static final String API_V1_BOOKS = "/api/v1/Books";
    public static final String API_V1_BOOKS_ID = "/api/v1/Books/{id}";

    @Autowired
    private Properties properties;

    @Override
    public RestClientWrapper getClient() {
        return RestClientFactory.getClient(properties.appUrl);
    }

    public HttpResponseWrapper sendGetBooksRequest() {
        return getClient()
                .get(API_V1_BOOKS);
    }

    public HttpResponseWrapper sendGetBookByIdRequest(int bookId) {
        return getClient()
                .pathParam("id", bookId)
                .get(API_V1_BOOKS_ID);
    }

    public HttpResponseWrapper sendPostBookRequest(Object body) {
        return getClient()
                .body(body)
                .post(API_V1_BOOKS);
    }

    public HttpResponseWrapper sendPutBookRequest(int bookId, Object body) {
        return getClient()
                .pathParam("id", bookId)
                .body(body)
                .put(API_V1_BOOKS_ID);
    }

    public HttpResponseWrapper sendDeleteBookRequest(Integer bookId) {
        return getClient()
                .pathParam("id", bookId)
                .delete(API_V1_BOOKS_ID);
    }
}
