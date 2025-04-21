package com.bookstore.api.rest.controller;

import com.bookstore.api.common.restclient.HttpResponseWrapper;
import com.bookstore.api.dto.AuthorDTO;
import com.bookstore.api.rest.client.AuthorClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class AuthorController {

    @Autowired
    private AuthorClient authorClient;

    public HttpResponseWrapper getAuthorsList(int expectedCode) {
        return authorClient
                .sendGetAuthorsRequest()
                .expectStatusCode("Get authors list request returned wrong status code ", expectedCode);
    }

    public HttpResponseWrapper getAuthorById(int authorId, int expectedCode) {
        return authorClient
                .sendGetAuthorByIdRequest(authorId)
                .expectStatusCode("Get author by ID request returned wrong status code ", expectedCode);
    }

    public HttpResponseWrapper createNewAuthor(AuthorDTO authorDTO, int expectedCode) {
        return authorClient
                .sendPostAuthorRequest(authorDTO)
                .expectStatusCode("Create new author request returned wrong status code ", expectedCode);
    }

    public HttpResponseWrapper updateAuthor(int authorId, AuthorDTO authorDTO, int expectedCode) {
        return authorClient
                .sendPutAuthorRequest(authorId, authorDTO)
                .expectStatusCode("Update author request returned wrong status code ", expectedCode);
    }

    public HttpResponseWrapper deleteAuthor(int authorId, int expectedCode) {
        return authorClient
                .sendDeleteAuthorRequest(authorId)
                .expectStatusCode("Delete author request returned wrong status code ", expectedCode);
    }
}
