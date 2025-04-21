package com.bookstore.api.common.client;

import com.bookstore.api.common.restclient.RestClientWrapper;

public interface ApiClient {

    RestClientWrapper getClient();
}