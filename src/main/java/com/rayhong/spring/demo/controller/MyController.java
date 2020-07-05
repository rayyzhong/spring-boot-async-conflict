package com.rayhong.spring.demo.controller;

import com.rayhong.spring.demo.exception.EcareException;
import com.rayhong.spring.demo.model.User;
import com.rayhong.spring.demo.services.GitHubLookupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

@RestController
public class MyController {
    private static final Logger logger = LoggerFactory.getLogger(MyController.class);

    @Autowired
    GitHubLookupService gitHubLookupService;


    @GetMapping("/user")
    public Callable<User> getUser() throws EcareException {
        logger.info("Enter controller");
        return () -> {
            CompletableFuture<User> page1 = gitHubLookupService.findUser("PivotalSoftware");
            return page1.get();
        };
    }


}
