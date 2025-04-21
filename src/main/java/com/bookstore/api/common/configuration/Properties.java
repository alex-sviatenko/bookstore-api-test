package com.bookstore.api.common.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Properties {

    @Value("${config.application.url}")
    public String appUrl;
}
