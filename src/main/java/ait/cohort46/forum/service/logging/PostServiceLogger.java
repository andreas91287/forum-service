package ait.cohort46.forum.service.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Service;

@Service
@Aspect
@Slf4j(topic = "Post service")
public class PostServiceLogger {

    @Pointcut("execution(* ait.cohort46.forum.service.PostServiceImpl.findPostById(String)) && args(id)")
    public void findById(String id) {}

    @Pointcut("execution(public Iterable<ait.cohort46.forum.dto.PostDto> ait.cohort46.forum.service.PostServiceImpl.findPosts*(..))")
    public void bulkFindPosts() {}

    @Pointcut("@annotation(PostLogger)")
    public void annotatedPostLogger() {}

    @Before("findById(id)")
    public void getPostLogging(String id) {
        log.info("post with id {}, requested", id);
    }

    @Around("bulkFindPosts()")
    public Object bulkFindPostsLogging(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        long start = System.currentTimeMillis();
        Object result = pjp.proceed(args);
        long end = System.currentTimeMillis();
        log.info("method {} took {} ms", pjp.getSignature().getName(), end - start);
        return result;
    }

    @AfterReturning("annotatedPostLogger()")
    public void annotatedPostLogger(JoinPoint jp) {
        log.info("annotated by PostLogger method {}, done", jp.getSignature().getName());
    }
}
