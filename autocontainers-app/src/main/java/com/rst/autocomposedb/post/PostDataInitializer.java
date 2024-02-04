package com.rst.autocomposedb.post;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
class PostDataInitializer implements CommandLineRunner {
    public static final String FILE_PATH = "autocontainers-app/src/main/resources/post/input.json";
    @Value("${init.fromJson}")
    private boolean initFromJson;

    private final PostRepository repository;
    private final ObjectMapper objectMapper;

    @Override
    public void run(String... args) throws Exception {
        if (!initFromJson) {
            return;
        }

        List<Post> posts = getTestPosts();
        repository.saveAll(posts);
    }

    public String manualInit() throws IOException {
        List<Post> posts = getTestPosts();
        repository.saveAll(posts);
        return "success";
    }

    private List<Post> getTestPosts() throws IOException {
        return objectMapper.readValue(new File(FILE_PATH), new TypeReference<>() {
        });
    }
}
