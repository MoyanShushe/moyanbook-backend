package com.moyanshushe.service.impl;

/*
 * Author: Hacoj
 * Version: 1.0
 */

import com.moyanshushe.mapper.BookMapper;
import com.moyanshushe.model.dto.book.BookForAdd;
import com.moyanshushe.model.dto.book.BookForDelete;
import com.moyanshushe.model.dto.book.BookForUpdate;
import com.moyanshushe.model.dto.book.BookSpecification;
import com.moyanshushe.model.entity.Book;
import com.moyanshushe.model.entity.Fetchers;
import com.moyanshushe.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.babyfish.jimmer.Page;
import org.babyfish.jimmer.sql.ast.mutation.SimpleSaveResult;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class BookServiceImpl implements BookService {

    private final BookMapper mapper;

    public BookServiceImpl(BookMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    @NotNull
    @Transactional(rollbackFor = Exception.class)
    public Boolean add(BookForAdd bookForAdd) {
        log.info("添加书籍: {}", bookForAdd);

        SimpleSaveResult<Book> result = mapper.add(bookForAdd);

        boolean addSuccess =
                result.getTotalAffectedRowCount() ==
                        (bookForAdd.getLabels().size() + bookForAdd.getImages().size() + 1);

        if (addSuccess) {
            log.info("添加书籍成功");
            return true;
        } else {
            log.info("添加书籍失败");
            return false;
        }
    }

    @Override
    @NotNull
    public Boolean delete(BookForDelete bookForDelete) {
        log.info("删除书籍: {}", bookForDelete.getIds());

        Integer delete = mapper.delete(bookForDelete);

        log.info("删除书籍成功: {}", delete);
        return true;
    }

    @Override
    @NotNull
    public Boolean update(BookForUpdate bookForUpdate) {
        log.info("更新书籍: {}", bookForUpdate.getId());

        SimpleSaveResult<Book> result = mapper.update(bookForUpdate);

        int totalAffectedRowCount = result.getTotalAffectedRowCount();
        if (totalAffectedRowCount >= 1) {
            log.info("更新书籍成功");

            log.info("from {} \n to \n {}", result.getOriginalEntity(), result.getModifiedEntity());
            return true;
        }

        return false;
    }

    @Override
    public Page<Book> fetch(BookSpecification specification) {
        log.info("查询书籍: {}", specification);
        return mapper.fetch(specification, Fetchers.BOOK_FETCHER.allScalarFields());
    }
}
