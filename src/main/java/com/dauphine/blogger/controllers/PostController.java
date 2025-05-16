package com.dauphine.blogger.controllers;

import com.dauphine.blogger.dto.CreationPostRequest;
import com.dauphine.blogger.dto.UpdatePostRequest;
import com.dauphine.blogger.exceptions.PostNotFoundByIdException;
import com.dauphine.blogger.models.Post;
import com.dauphine.blogger.services.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("v1/posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping()
    @Operation(
            summary = "Create post",
            description = "Create new post"
    )
    public ResponseEntity<Post> create(
            @Parameter(
                    description = "Post creation request"
            )
            @RequestBody CreationPostRequest creationPostRequest) {
        Post post = postService.create(
                creationPostRequest.getTitle(),
                creationPostRequest.getContent(),
                creationPostRequest.getCategoryId()
        );
        return ResponseEntity
                .created(URI.create("/v1/posts/" + post.getId()))
                .body(post);
    }

    @PutMapping("{id}")
    @Operation(
            summary = "Update post",
            description = "Update post by id")
    public ResponseEntity<Post> update(
            @Parameter(description = "id of post", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Post update request")
            @RequestBody UpdatePostRequest updatePostRequest) {
        Post updatedPost = postService.update(
                id,
                updatePostRequest.getTitle(),
                updatePostRequest.getContent()
        );
        return ResponseEntity.ok(updatedPost);
    }

    @GetMapping("{id}")
    @Operation(
            summary = "Get post by Id",
            description = "Retrieve a post by its id"
    )
    public ResponseEntity<Post> getPostById(
            @Parameter(description = "id of post")
            @PathVariable UUID id) {
        Post post = postService.getById(id);
        if (post == null) {
            throw new PostNotFoundByIdException(id);
        }
        return ResponseEntity.ok(post);
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "Delete post",
            description = "Delete a post by its id"
    )
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID of the post to delete", required = true)
            @PathVariable UUID id) {
        postService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /*
    @GetMapping("v1/posts")
    @Operation(
            summary = "Get all posts (optional : filter by date, title or content)",
            description = "Returns all posts or filters by optional creation date (dd-MM-yyyy), title, or content"
    )
    public ResponseEntity<List<Post>> getAll(
            @Parameter(description = "Optional filter by creation date (format: dd-MM-yyyy)")
            @RequestParam(name = "date", required = false)
            @DateTimeFormat(pattern = "dd-MM-yyyy") Optional<LocalDate> date,

            @Parameter(description = "Optional search value to match in title or content")
            @RequestParam(name = "value", required = false)
            Optional<String> value) {

        if (date.isPresent()) {
            Timestamp startOfDay = Timestamp.valueOf(date.get().atStartOfDay());
            Timestamp endOfDay = Timestamp.valueOf(date.get().atTime(23, 59, 59, 999_999_999));
            List<Post> posts = postService.getByCreatedDateBetween(startOfDay, endOfDay);
            return ResponseEntity.ok(posts);
        }

        if (value.isPresent()) {
            List<Post> posts = postService.getByTitleOrContent(value.get());
            return ResponseEntity.ok(posts);
        }

        List<Post> posts = postService.getAll();
        return ResponseEntity.ok(posts);
    }
    */

    @GetMapping("category/{categoryId}")
    @Operation(
            summary = "Get posts by category id",
            description = "Retrieve all posts in a category"
    )
    public ResponseEntity<List<Post>> getAllByCategory(
            @Parameter(description = "category id", required = true)
            @PathVariable UUID categoryId) {
        List<Post> posts = postService.getAllByCategoryId(categoryId);
        return ResponseEntity.ok(posts);
    }
}
