package com.dauphine.blogger.services.impl;

import com.dauphine.blogger.models.Category;
import com.dauphine.blogger.models.Post;
import com.dauphine.blogger.services.PostService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PostServiceImpl implements PostService {

    private final List<Post> temporaryPosts;

    public PostServiceImpl() {
        temporaryPosts = new ArrayList<>();
        temporaryPosts.add(new Post(UUID.randomUUID(), "title", "content",
                new Category(UUID.randomUUID(),"category")));
        temporaryPosts.add(new Post(UUID.randomUUID(), "title", "content",
                new Category(UUID.randomUUID(),"category")));
    }

    @Override
    public List<Post> getAllByCategoryID(UUID categoryID) {
        return List.of();
    }

    @Override
    public List<Post> getAll() {
        return List.of();
    }

    @Override
    public Post getById(UUID id) {
        return null;
    }

    @Override
    public Post create(String title, String content, UUID categoryID) {
        return null;
    }

    @Override
    public Post update(UUID categoryID, String title, String content) {
        return null;
    }

    @Override
    public boolean deleteById(UUID id) {
        return false;
    }
}