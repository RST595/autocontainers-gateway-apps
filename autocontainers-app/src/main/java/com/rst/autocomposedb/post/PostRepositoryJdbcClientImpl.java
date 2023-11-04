package com.rst.autocomposedb.post;

import lombok.RequiredArgsConstructor;
import org.springframework.data.relational.repository.Lock;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
class PostRepositoryJdbcClientImpl implements PostRepository {
    private final JdbcClient jdbcClient;

    @Override
    @Transactional
    public Optional<Post> findById(long id) {
        return jdbcClient.sql("SELECT id, userId, title, content FROM post WHERE id=:id")
                .param("id", id)
                .query(Post.class)
                .optional();
    }

    @Override
    @Transactional
    public List<Post> findAll() {
        return jdbcClient.sql("SELECT id, userId, title, content FROM post")
                .query(Post.class)
                .list();
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        jdbcClient.sql("DELETE FROM post WHERE id=:id")
                .param("id", id)
                .update();
    }

    @Override
    @Transactional
    public Post update(Post post) {
        jdbcClient.sql("UPDATE post SET userId = ?, title = ?, content = ?  WHERE id = ?")
                .params(List.of(post.userId, post.title, post.content, post.id))
                .update();

        return jdbcClient.sql("SELECT id, userId, title, content FROM post WHERE id=:id")
                .param("id", post.id)
                .query(Post.class)
                .optional()
                .orElse(null);
    }

    @Override
    @Transactional
    public Post save(Post post) {
        long id = jdbcClient.sql("INSERT INTO post(userId, title, content) VALUES(?, ?, ?) RETURNING id")
                .params(List.of(post.userId, post.title, post.content))
                .query(Long.class)
                .single();

        return jdbcClient.sql("SELECT id, userId, title, content FROM post WHERE id=:id")
                .param("id", id)
                .query(Post.class)
                .optional()
                .orElse(null);
    }

    @Override
    @Transactional
    public void saveAll(List<Post> post) {
        post.forEach(this::save);
    }
}
