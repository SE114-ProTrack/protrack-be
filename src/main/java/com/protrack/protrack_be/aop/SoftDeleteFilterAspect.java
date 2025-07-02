package com.protrack.protrack_be.aop;

import com.protrack.protrack_be.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class SoftDeleteFilterAspect {

    @PersistenceContext
    private EntityManager entityManager;

    @Pointcut("@annotation(com.protrack.protrack_be.annotation.EnableSoftDeleteFilter)")
    public void methodsWithSoftDeleteFilter() {}

    @Before("methodsWithSoftDeleteFilter()")
    public void applyFilter() {
        HibernateUtil.enableDeletedFilter(entityManager);
        log.debug("✅ Hibernate filter 'deletedFilter' applied via @EnableSoftDeleteFilter");
    }
}
