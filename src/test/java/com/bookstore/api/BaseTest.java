package com.bookstore.api;

import com.bookstore.api.rest.client.AuthorClient;
import com.bookstore.api.rest.client.BookClient;
import com.bookstore.api.rest.controller.AuthorController;
import com.bookstore.api.rest.controller.BookController;
import com.bookstore.api.steps.AuthorSteps;
import com.bookstore.api.steps.BookSteps;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

@SpringBootTest
@SpringBootConfiguration
@Execution(ExecutionMode.CONCURRENT)
@ComponentScan(basePackages = {"com.*"})
public class BaseTest {

    @Autowired
    protected AuthorSteps authorSteps;
    @Autowired
    protected BookSteps bookSteps;

    @Autowired
    protected AuthorController authorController;
    @Autowired
    protected BookController bookController;

    @Autowired
    protected AuthorClient authorClient;
    @Autowired
    protected BookClient bookClient;
}
