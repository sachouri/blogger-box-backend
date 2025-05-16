package com.dauphine.blogger.services;

import com.dauphine.blogger.models.Post;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public interface PostService {

    List<Post> getAllByCategoryId(UUID categoryID);

    List<Post> getAll();

    Post getById(UUID id);

    Post create(String title, String content, UUID categoryID);

    Post update(UUID id, String title, String content);

    boolean deleteById(UUID id);

    List<Post> getByCreatedDateBetween(Timestamp start, Timestamp end);

    List<Post> getAllByTitleOrContent(String value);

}