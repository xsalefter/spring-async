package com.xsalefter.springasync;

import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;

import com.xsalefter.springasync.github.GenericWebRepository;

@SpringBootApplication
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String...args) throws InterruptedException, ExecutionException {
        final ConfigurableApplicationContext context = SpringApplication.run(Application.class);
        final GenericWebRepository repository = context.getBean(GenericWebRepository.class);

        final ListenableFuture<ResponseEntity<String>> xsalefterGithub = repository.getUserProfile("https://api.github.com/users/xsalefter");
        log.info(">>> Invoking xsalefter @ github.");

        final ListenableFuture<ResponseEntity<String>> detikDotCom = repository.getUserProfile("https://detik.com");
        log.info(">>> Invoking detik.com");

        final ListenableFuture<ResponseEntity<String>> jbossDotOrg = repository.getUserProfile("https://jboss.org");
        log.info(">>> Invoking jboss.org");

        final ListenableFuture<ResponseEntity<String>> stackoverflowDotCom = repository.getUserProfile("https://stackoverflow.com");
        log.info(">>> Invoking stackoverflow.com");

        stackoverflowDotCom.addCallback(
            ok -> {
                ok.getBody();
                log.info(">>> stackoverflow done. Response is: {}", ok.getStatusCode());
            }, 
            error -> {
                log.error(">>> stackoverflow error. Message is: {}", error.getMessage());
            }
        );

        detikDotCom.addCallback(
            ok -> {
                ok.getBody();
            	log.info(">>> detik done. Response is: {}", ok.getStatusCode());
            },
            error -> {
            	log.error(">>> detik error. Message is: {}", error.getMessage());
            }
        );

        jbossDotOrg.addCallback(
            ok -> {
            	ok.getBody();
            	log.info(">>> jboss done. Response is: {}", ok.getStatusCode());
            },
            error -> {
            	log.error(">>> jboss error. Message is: {}", error.getMessage());
            }
        );

        xsalefterGithub.addCallback(
            ok -> {
                ok.getBody();
                log.info(">>> github done. Response is: {}", ok.getStatusCode());
        	},
            error -> {
            	log.error(">>> github error. Message is: {}", error.getMessage());
            }
        );
    }

    @Bean
    public AsyncRestTemplate asyncRestTemplate() {
        return new AsyncRestTemplate();
    }
}
