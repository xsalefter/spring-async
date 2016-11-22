package com.xsalefter.springasync.github;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;

@Component
public class GenericWebRepository {

    private static final Logger log = LoggerFactory.getLogger(GenericWebRepository.class);
    private final AsyncRestTemplate asyncRestTemplate;

    @Inject
    public GenericWebRepository(final AsyncRestTemplate asyncRestTemplate) {
        log.info("Creating instance of GenericWebRepository.");
        this.asyncRestTemplate = asyncRestTemplate;
	}

    public ListenableFuture<ResponseEntity<String>> getUserProfile(final String url) {
        final ListenableFuture<ResponseEntity<String>> result = this.asyncRestTemplate.getForEntity(url, String.class);
        return result;
    }

}
