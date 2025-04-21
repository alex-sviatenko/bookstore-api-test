package com.bookstore.api.steps;

import com.bookstore.api.common.annotation.Steps;
import com.bookstore.api.dto.AuthorDTO;
import com.bookstore.api.rest.controller.AuthorController;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.apache.http.HttpStatus.SC_OK;

@Steps
@Log4j2
public class AuthorSteps {

    @Autowired
    private AuthorController authorController;

    @Step("Get authors list")
    public List<AuthorDTO> getAuthorsList() {
        log.info("Retrieve a list of all authors");

        return authorController
                .getAuthorsList(SC_OK)
                .getBodyAsList(AuthorDTO.class);
    }

    @Step("Get specific author by ID")
    public AuthorDTO getAuthorDetailsById(int authorId) {
        log.info("Retrieve details of a specific author by ID = {}", authorId);

        return authorController
                .getAuthorById(authorId, SC_OK)
                .getBodyAs(AuthorDTO.class);
    }

    @Step("Add a new author to the system")
    public AuthorDTO createNewAuthor(AuthorDTO authorDTO) {
        log.info("Add a new author to the system");

        return authorController
                .createNewAuthor(authorDTO, SC_OK)
                .getBodyAs(AuthorDTO.class);
    }

    @Step("Update author details")
    public AuthorDTO updateAuthor(int authorId, AuthorDTO authorDTO) {
        log.info("Update author details by ID = {}", authorId);

        return authorController
                .updateAuthor(authorId, authorDTO, SC_OK)
                .getBodyAs(AuthorDTO.class);
    }

    @Step("Delete an author")
    public void deleteAuthor(int authorId) {
        log.info("Delete an author by ID = {}", authorId);

        authorController.deleteAuthor(authorId, SC_OK);
    }
}
