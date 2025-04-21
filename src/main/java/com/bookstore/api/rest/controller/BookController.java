package com.bookstore.api.rest.controller;

import com.bookstore.api.common.restclient.HttpResponseWrapper;
import com.bookstore.api.dto.BookDTO;
import com.bookstore.api.rest.client.BookClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class BookController {

    @Autowired
    private BookClient bookClient;

    public HttpResponseWrapper getBooksList(int expectedCode) {
        return bookClient
                .sendGetBooksRequest()
                .expectStatusCode("Get books list request returned wrong status code ", expectedCode);
    }

    public HttpResponseWrapper getBookById(int bookId, int expectedCode) {
        return bookClient
                .sendGetBookByIdRequest(bookId)
                .expectStatusCode("Get book by ID request returned wrong status code ", expectedCode);
    }

    public HttpResponseWrapper createNewBook(BookDTO bookDTO, int expectedCode) {
        return bookClient
                .sendPostBookRequest(bookDTO)
                .expectStatusCode("Create new book request returned wrong status code ", expectedCode);
    }

    public HttpResponseWrapper updateBook(int bookId, BookDTO bookDTO, int expectedCode) {
        return bookClient
                .sendPutBookRequest(bookId, bookDTO)
                .expectStatusCode("Update book request returned wrong status code ", expectedCode);
    }

    public HttpResponseWrapper deleteBook(int bookId, int expectedCode) {
        return bookClient
                .sendDeleteBookRequest(bookId)
                .expectStatusCode("Delete book request returned wrong status code ", expectedCode);
    }
}
