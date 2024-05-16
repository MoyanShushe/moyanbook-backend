package com.moyanshushe.controller;

/*
 * Author: Hacoj
 * Version: 1.0
 */

import com.moyanshushe.model.Result;
import com.moyanshushe.model.dto.book.BookForAdd;
import com.moyanshushe.model.dto.book.BookForDelete;
import com.moyanshushe.model.dto.book.BookForUpdate;
import com.moyanshushe.model.dto.book.BookSpecification;
import com.moyanshushe.model.entity.Book;
import com.moyanshushe.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.babyfish.jimmer.Page;
import org.babyfish.jimmer.client.meta.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api
@Slf4j
@RestController
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @Api
    @PostMapping("/fetch")
    public ResponseEntity<Result> fetch(
            @RequestBody BookSpecification specification
    ) {

        Page<Book> page = bookService.fetch(specification);
        return ResponseEntity.ok(Result.success(page));
    }

    @Api
    @PostMapping("/add")
    public ResponseEntity<Result> add(
            @RequestBody BookForAdd bookForAdd
    ) {
        bookService.add(bookForAdd);

        return ResponseEntity.ok(Result.success());
    }

    @Api
    @PostMapping("/update")
    public ResponseEntity<Result> update(
            @RequestBody BookForUpdate bookForUpdate
    ) {
        bookService.update(bookForUpdate);

        return ResponseEntity.ok(Result.success());
    }

    @Api
    @PostMapping("/delete")
    public ResponseEntity<Result> delete(
            @RequestBody BookForDelete bookForDelete
    ) {
        bookService.delete(bookForDelete);

        return ResponseEntity.ok(Result.success());
    }
}
