package com.twohoseon.app.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : QueryDslConfig
 * @date : 10/19/23 10:36 PM
 * @modifyed : $
 **/
@Configuration
public class QueryDslConfig {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}
