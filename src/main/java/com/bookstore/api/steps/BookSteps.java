package com.bookstore.api.steps;

import com.bookstore.api.common.annotation.Steps;
import com.bookstore.api.dto.BookDTO;
import com.bookstore.api.rest.controller.BookController;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.apache.http.HttpStatus.SC_OK;

@Steps
@Log4j2
public class BookSteps {

    @Autowired
    private BookController bookController;

    @Step("Get books list")
    public List<BookDTO> getBooksList() {
        log.info("Retrieve a list of all books");

        return bookController
                .getBooksList(SC_OK)
                .getBodyAsList(BookDTO.class);
    }

    @Step("Get specific book by ID")
    public BookDTO getBookDetailsById(int bookId) {
        log.info("Retrieve details of a specific book by ID = {}", bookId);

        return bookController
                .getBookById(bookId, SC_OK)
                .getBodyAs(BookDTO.class);
    }

    @Step("Add a new book to the system")
    public BookDTO createNewBook(BookDTO bookDTO) {
        log.info("Add a new book to the system");

        return bookController
                .createNewBook(bookDTO, SC_OK)
                .getBodyAs(BookDTO.class);
    }

    @Step("Update an existing book")
    public BookDTO updateBook(int bookId, BookDTO bookDTO) {
        log.info("Update an existing book by ID = {}", bookId);

        return bookController
                .updateBook(bookId, bookDTO, SC_OK)
                .getBodyAs(BookDTO.class);
    }

    @Step("Delete a book")
    public void deleteBook(int bookId) {
        log.info("Delete a book by ID = {}", bookId);

        bookController.deleteBook(bookId, SC_OK);
    }
}
