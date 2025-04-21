package com.bookstore.api.tests;

import com.bookstore.api.BaseTest;
import com.bookstore.api.common.restclient.HttpResponseWrapper;
import com.bookstore.api.dto.AuthorDTO;
import com.bookstore.api.utils.UniqueIdGenerator;
import com.bookstore.api.utils.RandomUtils;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthorApiTest extends BaseTest {

    @Test
    public void shouldRetrieveAuthorsList() {
        List<AuthorDTO> authorList = authorSteps.getAuthorsList();

        assertThat(authorList)
                .as("Expected a non-null list of authors, but got null")
                .isNotNull();
        assertThat(authorList)
                .as("Expected authors list to contain at least one item, but got size: %s", authorList.size())
                .hasSizeGreaterThan(0);
    }

    @Test
    public void shouldRetrieveAuthorDetailsByID() {
        int authorsListSize = authorSteps.getAuthorsList().size();
        int authorIdExpected = RandomUtils.getRandomNumber(authorsListSize);

        AuthorDTO authorDTO = authorSteps.getAuthorDetailsById(authorIdExpected);

        assertThat(authorDTO.getId())
                .as("Author ID should not be null for ID %s", authorIdExpected)
                .isNotNull();

        assertThat(authorDTO.getId())
                .as("Expected author ID to be %s but got %s", authorIdExpected, authorDTO.getId())
                .isEqualTo(authorIdExpected);

        assertThat(authorDTO.getIdBook())
                .as("Book ID should not be null for author ID %s", authorIdExpected)
                .isNotNull();

        assertThat(authorDTO.getFirstName())
                .as("First name should not be null for author ID %s", authorIdExpected)
                .isNotNull();

        assertThat(authorDTO.getLastName())
                .as("Last name should not be null for author ID %s", authorIdExpected)
                .isNotNull();
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, Integer.MAX_VALUE})
    public void getAuthorDetailsWithEdgeCaseInvalidIds(int invalidAuthorId) {
        HttpResponseWrapper responseWrapper = authorClient.sendGetAuthorByIdRequest(invalidAuthorId);

        assertThat(responseWrapper.getResponseCode())
                .as("Expected 404 Not Found when requesting author details with invalid ID %s, but got %s",
                        invalidAuthorId, responseWrapper.getResponseCode())
                .isEqualTo(HttpStatus.SC_NOT_FOUND);
        assertThat(responseWrapper.getBodyAsString("title"))
                .as("Expected error title 'Not Found' for invalid ID %s, but got '%s'",
                        invalidAuthorId, responseWrapper.getBodyAsString("title"))
                .isEqualTo("Not Found");
    }

    @Test
    public void createAuthorAndRetrieveItSuccessfully() {
        List<AuthorDTO> authorList = authorSteps.getAuthorsList();
        int authorId = UniqueIdGenerator.createUniqueId(authorList, AuthorDTO::getId);

        AuthorDTO authorExpected = AuthorDTO.builder()
                .id(authorId)
                .idBook(RandomUtils.getRandomNumber(100))
                .firstName(RandomUtils.getRandomString(10))
                .lastName(RandomUtils.getRandomString(15))
                .build();

        authorSteps.createNewAuthor(authorExpected);

        HttpResponseWrapper responseWrapper = authorClient.sendGetAuthorByIdRequest(authorId);

        assertThat(responseWrapper.getResponseCode())
                .as("Expected HTTP 200 OK when retrieving author details with ID %s, but got %s",
                        authorId, responseWrapper.getResponseCode())
                .isEqualTo(HttpStatus.SC_OK);

        AuthorDTO authorActual = responseWrapper.getBodyAs(AuthorDTO.class);

        assertThat(authorActual)
                .as("Author details with ID %s was not created or retrieved correctly", authorId)
                .isEqualTo(authorExpected);
    }

    @Test
    public void createAuthorWhenIdAlreadyExisted() {
        int authorListSize = authorSteps.getAuthorsList().size();
        int authorId = RandomUtils.getRandomNumber(authorListSize);

        AuthorDTO authorDTO = AuthorDTO.builder()
                .id(authorId)
                .idBook(RandomUtils.getRandomNumber(100))
                .build();

        HttpResponseWrapper responseWrapper = authorClient.sendPostAuthorRequest(authorDTO);

        assertThat(responseWrapper.getResponseCode())
                .as("Expected HTTP 409 Conflict when creating the author with existing ID %s, but got %s instead",
                        authorId, responseWrapper.getResponseCode())
                .isEqualTo(HttpStatus.SC_CONFLICT);
    }

    @Test
    public void createAuthorWhenFieldsAreAbsent() {
        List<AuthorDTO> authorList = authorSteps.getAuthorsList();
        int authorId = UniqueIdGenerator.createUniqueId(authorList, AuthorDTO::getId);

        Map<String, Object> author = new HashMap<>();
        author.put("firstName", RandomUtils.getRandomString(5));

        HttpResponseWrapper responseWrapper = authorClient.sendPostAuthorRequest(author);
        assertThat(responseWrapper.getResponseCode())
                .as("Expecting 400 or 500 due to missing fields")
                .isIn(400, 500);
    }

    @Test
    public void updateAuthorDetailsSuccessfully() {
        int authorListSize = authorSteps.getAuthorsList().size();
        int authorId = RandomUtils.getRandomNumber(authorListSize);

        AuthorDTO authorExpected = AuthorDTO.builder()
                .id(authorId)
                .idBook(RandomUtils.getRandomNumber(100))
                .build();

        authorSteps.updateAuthor(authorId, authorExpected);

        AuthorDTO authorActual = authorSteps.getAuthorDetailsById(authorId);

        assertThat(authorActual)
                .as("Author details with ID %s was not updated correctly", authorId)
                .isEqualTo(authorExpected);
    }

    @Test
    public void deleteExistingAuthor() {
        int authorListSize = authorSteps.getAuthorsList().size();
        int authorId = RandomUtils.getRandomNumber(authorListSize);

        authorSteps.deleteAuthor(authorId);

        HttpResponseWrapper responseWrapper = authorClient.sendGetAuthorByIdRequest(authorId);
        assertThat(responseWrapper.getResponseCode())
                .as("Expected 404 Not Found when getting the author with ID %s, but got %s",
                        authorId, responseWrapper.getResponseCode())
                .isEqualTo(HttpStatus.SC_NOT_FOUND);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    public void deleteAuthorWithInvalidId(int invalidAuthorId) {
        HttpResponseWrapper responseWrapper = authorClient.sendDeleteAuthorRequest(invalidAuthorId);

        assertThat(responseWrapper.getResponseCode())
                .as("Expected 404 Not Found when deleting the author with invalid ID %s, but got %s",
                        invalidAuthorId, responseWrapper.getResponseCode())
                .isEqualTo(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void deleteNonExistingAuthor() {
        List<AuthorDTO> authorList = authorSteps.getAuthorsList();
        int authorId = UniqueIdGenerator.createUniqueId(authorList, AuthorDTO::getId);

        HttpResponseWrapper responseWrapper = authorClient.sendDeleteAuthorRequest(authorId);

        assertThat(responseWrapper.getResponseCode())
                .as("Expected 404 Not Found when deleting author with non-existing ID %s, but got %s",
                        authorId, responseWrapper.getResponseCode())
                .isEqualTo(HttpStatus.SC_NOT_FOUND);
    }
}
