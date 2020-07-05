# Spring boot async conflict

Demo how to handle the conflict in shared resources

## Background

Some internal shared resource can only be used once at a time, so in the API we are just allow one success request on the resource. Other concurrent request will be getting exception with 503 status code. 

The benefits are:

* Fail the other requests in an elegant way;
* Ensure dedicated access for the 1st user until it finishes its job; (e.g. for a system does not support multiple logon, to avoid a later user kick out the session of an earlier user. This will lead to a situation that no one can complete its job although it win the resource competition.)

## Solution

Bean, Component and Service are by default Singleton in Spring. In other words, they are stateless.TWhat we need to ensure is the resouce can be access by a single user untill it is released. 

In this example, we use concurrent lock to ensure this. 

```java
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class GitHubLookupService {

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
```


## Notice

We use ```lock.tryLock(1, TimeUnit.SECONDS);``` to allow 1 second to wait for a lock for every user. Under high TPS this might cause unecessary waiting and casue the system halt. 

So we may remove the waiting as a solution. However, a service like this may not be good enough to support such use case, which should be considered carefully.

