package com.dauphine.blogger.exceptions;

public class PostNameAlreadyExistsException extends RuntimeException {
    public PostNameAlreadyExistsException(String title) {
        super("Post title already exists: \"" + title + "\"");
    }
}
