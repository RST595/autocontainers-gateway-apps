package com.rst.autocomposedb.post;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
interface PostMapper {
    PostDTO toDto(Post post);
    Post toEntity(PostDTO postDto);
    List<PostDTO> toDtos(List<Post> posts);
    List<Post> toEntities(List<PostDTO> postDtos);
}
