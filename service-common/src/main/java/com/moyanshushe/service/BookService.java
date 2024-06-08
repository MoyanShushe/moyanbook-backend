package com.moyanshushe.service;

import com.moyanshushe.model.dto.book.BookForAdd;
import com.moyanshushe.model.dto.book.BookForDelete;
import com.moyanshushe.model.dto.book.BookForUpdate;
import com.moyanshushe.model.dto.book.BookSpecification;
import com.moyanshushe.model.entity.Book;
import org.babyfish.jimmer.Page;

/*
 * Author: Hacoj
 * Version: 1.0
 */
public interface BookService {
    Boolean add(BookForAdd bookForAdd);

    Boolean delete(BookForDelete bookForDelete);

    Boolean update(BookForUpdate bookForUpdate);

    Page<Book> fetch(BookSpecification specification);
}
