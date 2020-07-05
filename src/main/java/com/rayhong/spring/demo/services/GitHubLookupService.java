package com.rayhong.spring.demo.services;

import com.rayhong.spring.demo.model.User;
import com.rayhong.spring.demo.exception.EcareBusyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class GitHubLookupService {

    private static final Logger logger = LoggerFactory.getLogger(GitHubLookupService.class);

    private final RestTemplate restTemplate;

    public GitHubLookupService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    ReentrantLock lock = new ReentrantLock();

    @Async
    public CompletableFuture<User> findUser(String user) throws InterruptedException, EcareBusyException {
        boolean isLockAcquired = lock.tryLock(1, TimeUnit.SECONDS);
        logger.info("isLockAcquired=" + isLockAcquired);
        if(isLockAcquired) {
            try {
                logger.info("Looking up " + user);
                String url = String.format("https://api.github.com/users/%s", user);
                User results = restTemplate.getForObject(url, User.class);
                // Artificial delay of 1s for demonstration purposes
                Thread.sleep(3000L);
                return CompletableFuture.completedFuture(results);
            } finally {
                lock.unlock();
                logger.info("Unlock");
            }
        } else {
            logger.error("Ecare is busy");
            throw new EcareBusyException("Ecare is busy, try again later");
        }
    }

}