package com.dauphine.blogger.controllers;

import com.dauphine.blogger.dto.CreationCategoryRequest;
import com.dauphine.blogger.dto.UpdateCategoryRequest;
import com.dauphine.blogger.exceptions.CategoryNameAlreadyExistsException;
import com.dauphine.blogger.exceptions.CategoryNotFoundByIdException;
import com.dauphine.blogger.models.Category;
import com.dauphine.blogger.models.Post;
import com.dauphine.blogger.services.CategoryService;
import com.dauphine.blogger.services.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/categories")
@Tag(
        name = "Category API",
        description = "Category endpoints"
)
public class CategoryController {

    private final CategoryService categoryService;
    private final PostService postService;

    @Autowired
    public CategoryController(CategoryService categoryService, PostService postService) {
        this.categoryService = categoryService;
        this.postService = postService;
    }

    @GetMapping("{id}")
    @Operation(
            summary = "Get category by id",
            description = "Retrieve category by id"
    )
    public ResponseEntity<Category> retrieveCategoryById(@PathVariable UUID id){
        try {
            Category category = categoryService.getById(id);
            return ResponseEntity.ok(category);
        } catch (CategoryNotFoundByIdException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(
            summary = "Create new category",
            description = "Create new category, only required field is the name of the category to create"
    )
    public ResponseEntity<Category> createCategory(
            @RequestBody CreationCategoryRequest creationCategoryRequest) throws CategoryNameAlreadyExistsException {
        Category category = categoryService.create(creationCategoryRequest.getName());
        return ResponseEntity
                .created(URI.create("/v1/categories/" + category.getId()))
                .body(category);
    }

    @PutMapping("{id}")
    @Operation(
            summary = "Update category",
            description = "Update category name by id"
    )
    public ResponseEntity<Category> updateCategory(
            @Parameter(description = "category id", required = true)
            @PathVariable UUID id,
            @Parameter(description = "category update request")
            @RequestBody UpdateCategoryRequest updateCategoryRequest) {
        Category updatedCategory = categoryService.update(
                id,
                updateCategoryRequest.getName());
        if (updatedCategory == null) {
            throw new CategoryNotFoundByIdException(id);
        }
        return ResponseEntity.ok(updatedCategory);
    }

    @PatchMapping("{id}")
    @Operation(
            summary = "Partially update category",
            description = "Update a sub part of a category name"
    )
    public ResponseEntity<Category> patch(@PathVariable UUID id,
                                          @RequestBody CreationCategoryRequest categoryRequest) {
        Category updated = categoryService.update(id, categoryRequest.getName());
        if (updated == null) {
            throw new CategoryNotFoundByIdException(id);
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "Delete category",
            description = "Delete category by id"
    )
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        boolean deleted = categoryService.deleteById(id);
        if (!deleted) {
            throw new CategoryNotFoundByIdException(id);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping()
    @Operation(
            summary = "Get all categories",
            description = "Retrieve all categories or filter like name"
    )
    public ResponseEntity<List<Category>> getAll(@RequestParam(required = false) String name){
        System.out.println("GET /v1/posts called");
        List<Category> categories = name == null || name.isBlank()
                ? categoryService.getAll()
                : categoryService.getAllLikeName(name);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("{id}/posts")
    @Operation(
            summary = "Get all post of a certain categories",
            description = "Retrieve all posts linked to category id"
    )
    public ResponseEntity<List<Post>> getPostsByCategory(@PathVariable UUID id) {
        Category category = categoryService.getById(id);
        if (category == null) {
            throw new CategoryNotFoundByIdException(id);
        }
        List<Post> posts = postService.getAllByCategoryId(id);
        return ResponseEntity.ok(posts);
    }

}
