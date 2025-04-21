package com.bookstore.api.tests;

import com.bookstore.api.BaseTest;
import com.bookstore.api.common.restclient.HttpResponseWrapper;
import com.bookstore.api.dto.BookDTO;
import com.bookstore.api.utils.DateTimeUtils;
import com.bookstore.api.utils.RandomUtils;
import com.bookstore.api.utils.UniqueIdGenerator;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

public class BookApiTest extends BaseTest {

    @Test
    public void shouldRetrieveBooksList() {
        List<BookDTO> booksList = bookSteps.getBooksList();

        assertThat(booksList)
                .as("Books list should not be null — expected a list of books, but got null")
                .isNotNull();
        assertThat(booksList)
                .as("Books list should contain at least one book — actual size: %s", booksList.size())
                .hasSizeGreaterThan(0);
    }

    @Test
    public void shouldRetrieveSpecificBookByID() {
        int booksListSize = bookSteps.getBooksList().size();
        int bookIdExpected = RandomUtils.getRandomNumber(booksListSize);

        BookDTO bookResponse = bookSteps.getBookDetailsById(bookIdExpected);

        assertThat(bookResponse.getId())
                .as("Book ID should not be null for ID %s", bookIdExpected)
                .isNotNull();

        assertThat(bookResponse.getId())
                .as("Expected book ID to be %s but got %s", bookIdExpected, bookResponse.getId())
                .isEqualTo(bookIdExpected);

        assertThat(bookResponse.getPageCount())
                .as("Page count should not be null for book ID %s", bookIdExpected)
                .isNotNull();

        assertThat(bookResponse.getPublishDate())
                .as("Publish date should not be null for book ID %s", bookIdExpected)
                .isNotNull();

        assertThatCode(() -> OffsetDateTime.parse(bookResponse.getPublishDate()))
                .as("Publish date '%s' should be in a valid ISO date-time format", bookResponse.getPublishDate())
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, Integer.MAX_VALUE})
    public void getBookWithEdgeCaseInvalidIds(int invalidBookId) {
        HttpResponseWrapper responseWrapper = bookClient.sendGetBookByIdRequest(invalidBookId);

        assertThat(responseWrapper.getResponseCode())
                .as("Expected 404 Not Found when requesting book with invalid ID %s, but got %s",
                        invalidBookId, responseWrapper.getResponseCode())
                .isEqualTo(HttpStatus.SC_NOT_FOUND);
        assertThat(responseWrapper.getBodyAsString("title"))
                .as("Expected error title 'Not Found' for invalid ID %s, but got '%s'",
                        invalidBookId, responseWrapper.getBodyAsString("title"))
                .isEqualTo("Not Found");
    }

    @Test
    public void createBookAndRetrieveItSuccessfully() {
        List<BookDTO> booksList = bookSteps.getBooksList();
        int bookId = UniqueIdGenerator.createUniqueId(booksList, BookDTO::getId);

        BookDTO bookExpected = BookDTO.builder()
                .id(bookId)
                .title("Test Book " + bookId)
                .description("Some description")
                .pageCount(500)
                .excerpt("Excerpt here")
                .publishDate(DateTimeUtils.getDateTimeNow())
                .build();

        bookSteps.createNewBook(bookExpected);

        HttpResponseWrapper responseWrapper = bookClient.sendGetBookByIdRequest(bookId);

        assertThat(responseWrapper.getResponseCode())
                .as("Expected HTTP 200 OK when retrieving book with ID %s, but got %s",
                        bookId, responseWrapper.getResponseCode())
                .isEqualTo(HttpStatus.SC_OK);

        BookDTO bookActual = responseWrapper.getBodyAs(BookDTO.class);

        assertThat(bookActual)
                .as("Book with ID %s was not created or retrieved correctly", bookId)
                .isEqualTo(bookExpected);
    }

    @Test
    public void createBookWhenIdAlreadyExisted() {
        int booksListSize = bookSteps.getBooksList().size();
        int bookId = RandomUtils.getRandomNumber(booksListSize);

        BookDTO book = BookDTO.builder()
                .id(bookId)
                .pageCount(500)
                .publishDate(DateTimeUtils.getDateTimeNow())
                .build();

        HttpResponseWrapper responseWrapper = bookClient.sendPostBookRequest(book);

        assertThat(responseWrapper.getResponseCode())
                .as("Expected HTTP 409 Conflict when creating a book with existing ID %s, but got %s instead",
                        bookId, responseWrapper.getResponseCode())
                .isEqualTo(HttpStatus.SC_CONFLICT);
    }

    @Test
    public void createBookWithMissingFields() {
        List<BookDTO> booksList = bookSteps.getBooksList();
        int bookId = UniqueIdGenerator.createUniqueId(booksList, BookDTO::getId);

        Map<String, Object> book = new HashMap<>();
        book.put("id", bookId);

        HttpResponseWrapper responseWrapper = bookClient.sendPostBookRequest(book);
        assertThat(responseWrapper.getResponseCode())
                .as("Expecting 400 or 500 due to missing fields")
                .isIn(400, 500);
    }

    @Test
    public void updateExistingBookSuccessfully() {
        int booksListSize = bookSteps.getBooksList().size();
        int bookId = RandomUtils.getRandomNumber(booksListSize);

        BookDTO bookExpected = BookDTO.builder()
                .id(bookId)
                .title("Updated book title")
                .description("Updated description")
                .pageCount(250)
                .excerpt("Updated excerpt here")
                .publishDate(DateTimeUtils.getDateTimeNow())
                .build();

        bookSteps.updateBook(bookId, bookExpected);

        BookDTO bookActual = bookSteps.getBookDetailsById(bookId);

        assertThat(bookActual)
                .as("Book with ID %s was not updated correctly", bookId)
                .isEqualTo(bookExpected);
    }

    @Test
    public void deleteExistingBook() {
        int booksListSize = bookSteps.getBooksList().size();
        int bookId = RandomUtils.getRandomNumber(booksListSize);

        bookSteps.deleteBook(bookId);

        HttpResponseWrapper responseWrapper = bookClient.sendGetBookByIdRequest(bookId);
        assertThat(responseWrapper.getResponseCode())
                .as("Expected 404 Not Found when getting book with ID %s, but got %s",
                        bookId, responseWrapper.getResponseCode())
                .isEqualTo(HttpStatus.SC_NOT_FOUND);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    public void deleteBookWithInvalidId(int invalidBookId) {
        HttpResponseWrapper responseWrapper = bookClient.sendDeleteBookRequest(invalidBookId);

        assertThat(responseWrapper.getResponseCode())
                .as("Expected 404 Not Found when deleting book with invalid ID %s, but got %s",
                        invalidBookId, responseWrapper.getResponseCode())
                .isEqualTo(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void deleteNonExistingBook() {
        List<BookDTO> booksList = bookSteps.getBooksList();
        int bookId = UniqueIdGenerator.createUniqueId(booksList, BookDTO::getId);

        HttpResponseWrapper responseWrapper = bookClient.sendDeleteBookRequest(bookId);

        assertThat(responseWrapper.getResponseCode())
                .as("Expected 404 Not Found when deleting book with non-existing ID %s, but got %s",
                        bookId, responseWrapper.getResponseCode())
                .isEqualTo(HttpStatus.SC_NOT_FOUND);
    }
}
