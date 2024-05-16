package com.moyanshushe.mapper;

/*
 * Author: Hacoj
 * Version: 1.0
 */

import com.moyanshushe.model.dto.book.*;
import com.moyanshushe.model.entity.Book;
import com.moyanshushe.model.entity.BookFetcher;
import com.moyanshushe.model.entity.BookTable;
import lombok.extern.slf4j.Slf4j;
import org.babyfish.jimmer.Page;
import org.babyfish.jimmer.sql.JSqlClient;
import org.babyfish.jimmer.sql.ast.mutation.SimpleSaveResult;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BookMapper {

    private final BookTable table;
    private final JSqlClient jsqlClient;

    public BookMapper(JSqlClient jsqlClient) {
        this.table = BookTable.$;
        this.jsqlClient = jsqlClient;
    }

    public SimpleSaveResult<Book> add(BookForAdd bookForAdd) {
        return jsqlClient.getEntities()
                .saveCommand(bookForAdd)
                .execute();
    }

    public Integer delete(BookForDelete bookForDelete) {
        return jsqlClient.createDelete(table)
                .whereIf(
                        !bookForDelete.getIds().isEmpty(),
                        () -> table.id().in(bookForDelete.getIds())
                ).execute();
    }

    public SimpleSaveResult<Book> update(BookForUpdate bookForUpdate) {
        return jsqlClient.save(bookForUpdate.toEntity());
    }

    public @NotNull Page<Book> fetch(BookSpecification specification, BookFetcher fetcher) {
        return jsqlClient.createQuery(table)
                .where(specification)
                .select(
                        table.fetch(
                                fetcher
                        )
                )
                .fetchPage(
                        specification.getPage() != null ? specification.getPage() : 0,
                        specification.getPageSize() != null ? specification.getPageSize() : 10
                );
    }
}
