package com.dauphine.blogger.services.impl;

import com.dauphine.blogger.models.Category;
import com.dauphine.blogger.models.Post;
import com.dauphine.blogger.repositories.CategoryRepository;
import com.dauphine.blogger.repositories.PostRepository;
import com.dauphine.blogger.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository repository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public PostServiceImpl(PostRepository repository, CategoryRepository categoryRepository) {
        this.repository = repository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Post> getAllByCategoryId(UUID categoryID) {
        return repository.findAll().stream()
                .filter(post -> categoryID.equals(post.getCategoryId()))
                .sorted((post1, post2) -> post2.getCreatedDate().compareTo(post1.getCreatedDate()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Post> getAll() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "createdDate"));
    }

    @Override
    public Post getById(UUID id) {
        return repository.findById(id)
                .orElse(null);
    }

    @Override
    public Post create(String title, String content, UUID categoryID) {
        Category category = categoryRepository.findById(categoryID).orElse(null);
        if (category == null) {
            category = new Category(categoryID, "Default Category");
            categoryRepository.save(category);
        }
        Post post = new Post(UUID.randomUUID(), title, content, category);
        return repository.save(post);
    }

    @Override
    public Post update(UUID id, String title, String content) {
        Post post = getById(id);
        if (post == null) {
            return null;
        }
        post.setTitle(title);
        post.setContent(content);
        return repository.save(post);
    }

    @Override
    public boolean deleteById(UUID id) {
        repository.deleteById(id);
        return true;
    }

    @Override
    public List<Post> getByCreatedDateBetween(Timestamp start, Timestamp end) {
        return repository.findByCreatedDateBetween(start, end);
    }

    @Override
    public List<Post> getAllByTitleOrContent(String value) {
        return repository.findAllByTitleOrContent(value);
    }
}