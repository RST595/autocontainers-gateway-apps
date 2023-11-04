package com.rst.autocomposedb.post;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
class PostController {
    private final PostRepository repository;
    private final PostMapper mapper;
    private final PostDataInitializer initializer;

    @GetMapping("/{id}")
    public PostDTO findById(@PathVariable("id") long id) {
        return mapper.toDto(repository.findById(id).orElse(null));
    }

    @GetMapping
    public List<PostDTO> findAll() {
        return mapper.toDtos(repository.findAll());
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") long id) {
        repository.deleteById(id);
    }

    @PutMapping
    public PostDTO update(@RequestBody PostDTO postDTO) {
        return mapper.toDto(repository.update(mapper.toEntity(postDTO)));
    }

    @PostMapping
    public PostDTO save(@RequestBody PostDTO postDTO) {
        return mapper.toDto(repository.save(mapper.toEntity(postDTO)));
    }

    @PostMapping("/init")
    public String  initTestData() throws Exception {
        return initializer.manualInit();
    }
}
