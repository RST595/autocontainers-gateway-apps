package com.rst.autocomposedb.post;

import java.util.List;
import java.util.Optional;

interface PostRepository {
    Optional<Post> findById(long id);

    List<Post> findAll();

    void deleteById(long id);

    Post update(Post post);

    Post save(Post post);

    void saveAll(List<Post> post);
}
