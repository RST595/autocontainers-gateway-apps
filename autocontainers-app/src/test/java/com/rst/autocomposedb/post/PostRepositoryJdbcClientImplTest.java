package com.rst.autocomposedb.post;

import liquibase.integration.spring.SpringLiquibase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJdbcTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //don't replace our DB with an in-memory one
@Import(PostRepositoryJdbcClientImplTest.Configuration.class)
class PostRepositoryJdbcClientImplTest {
    @Container
    //@ServiceConnection // ServiceConnection replace DynamicPropertySource method since spring boot 3.1
    // if static - container creates only at first run, if nonstatic - new container for each run
    private static final PostgreSQLContainer<?> CONTAINER = new PostgreSQLContainer<>("postgres:15.0");

    @Autowired
    private SpringLiquibase liquibase;
    @Autowired
    private JdbcClient jdbcClient;
    private PostRepository repository;

    @BeforeEach
    void init() {
        repository = new PostRepositoryJdbcClientImpl(jdbcClient);
    }

    @Test
    void save() {
        // GIVEN
        Post post = new Post();
        post.setTitle("Title");
        post.setUserId(10L);
        post.setContent("Content");

        // WHEN
        Post savedPost = repository.save(post);

        // THEN
        assertNotEquals(0L, savedPost.id);
        assertEquals(0L, post.id);
    }

    @Test
    void findAll() {
        // GIVEN
        Post post = new Post();
        post.setTitle("Title");
        post.setUserId(10L);
        post.setContent("Content");
        repository.save(post);

        // WHEN
        List<Post> actualPosts = repository.findAll();

        // THEN
        assertEquals(1, actualPosts.size());
    }

    @Test
    void findById() {
        // GIVEN
        Post post = new Post();
        post.setTitle("Title2");
        post.setUserId(10L);
        post.setContent("Content");
        repository.save(post);

        // WHEN
        Optional<Post> actualPost = repository.findById(2);

        // THEN
        assertFalse(actualPost.isEmpty());
        assertEquals("Title2", actualPost.get().title);
    }

    @DynamicPropertySource
    static void dataSourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", CONTAINER::getUsername);
        registry.add("spring.datasource.password", CONTAINER::getPassword);
    }

    @Test
    void connection() {
        assertTrue(CONTAINER.isCreated());
        assertTrue(CONTAINER.isRunning());
    }

    @TestConfiguration
    static class Configuration {
        @Bean
        public DataSource dataSource() {
            DriverManagerDataSource ds = new DriverManagerDataSource();
            ds.setDriverClassName("org.postgresql.Driver");
            ds.setUrl(CONTAINER.getJdbcUrl());
            ds.setUsername(CONTAINER.getUsername());
            ds.setPassword(CONTAINER.getPassword());
            return ds;
        }

        @Bean
        public SpringLiquibase springLiquibase(DataSource dataSource) {
            SpringLiquibase liquibase = new SpringLiquibase();
            liquibase.setDropFirst(true);
            liquibase.setDataSource(dataSource);
            liquibase.setChangeLog("classpath:/db/changelog/changelog-master.xml");
            return liquibase;
        }
    }
}