package com.rayhong.spring.demo.services;

import com.rayhong.spring.demo.Application;
import com.rayhong.spring.demo.exception.EcareBusyException;
import com.rayhong.spring.demo.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Application.class,
        initializers = ConfigFileApplicationContextInitializer.class)
public class GitHubLookupServiceTest {

    @Autowired
    private GitHubLookupService gitHubLookupService;

    @Test
    public void singleUserShouldReturnCorrectUserInfo() throws InterruptedException, ExecutionException {
        CompletableFuture<User> result = gitHubLookupService.findUser("PivotalSoftware");
        User user = result.get();
        assertThat(user.getName(), is(equalTo("Pivotal Software, Inc.")));
    }

    @Test(expected = EcareBusyException.class)
    public void multipleUsersShouldReceiveError() throws Throwable {
        CompletableFuture<User> result1 = gitHubLookupService.findUser("PivotalSoftware");
        CompletableFuture<User> result2 = gitHubLookupService.findUser("PivotalSoftware");
        User user1 = result1.get();
        try {
            User user2 = result2.get();
        } catch (ExecutionException ex) {
            ex.printStackTrace();
            await().atMost(10, SECONDS).until(() -> user1.getName().equals("Pivotal Software, Inc."));
            throw ex.getCause();
        }
    }
}