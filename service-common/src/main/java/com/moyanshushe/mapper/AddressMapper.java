package com.moyanshushe.mapper;

import com.moyanshushe.model.dto.address.AddressForDelete;
import com.moyanshushe.model.dto.address.AddressForQuery;
import com.moyanshushe.model.dto.address.AddressSubstance;
import com.moyanshushe.model.entity.Address;
import com.moyanshushe.model.entity.AddressTable;
import org.babyfish.jimmer.Page;
import org.babyfish.jimmer.sql.JSqlClient;
import org.babyfish.jimmer.sql.ast.mutation.DeleteResult;
import org.babyfish.jimmer.sql.ast.mutation.SimpleSaveResult;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Optional;

/*
 * Author: Hacoj
 * Version: 1.0
 */

@Component
public class AddressMapper {

    private final JSqlClient jsqlClient;
    private final AddressTable table;

    public AddressMapper(JSqlClient jsqlClient) {
        this.jsqlClient = jsqlClient;
        this.table = AddressTable.$;
    }


    public SimpleSaveResult<Address> add(Address entity) {
        return jsqlClient.getEntities().save(entity);
    }

    public Optional<AddressSubstance> queryOneByAddress(String address) {
        return jsqlClient.createQuery(table).where(
                table.address().eq(address)
        ).select(
                table.fetch(AddressSubstance.class)
        ).execute().stream().findFirst();
    }

    public SimpleSaveResult<Address> update(Address entity) {
        return jsqlClient.save(entity);
    }

    public @NotNull Page<AddressSubstance> get(AddressForQuery addressForQuery) {
        return jsqlClient.createQuery(table)
                .whereIf(
                        addressForQuery.getId() != null,
                        () -> table.id().eq(addressForQuery.getId())
                ).whereIf(
                        addressForQuery.getAddress() != null,
                        table.address().likeIf(addressForQuery.getAddress())
                ).select(
                        table.fetch(AddressSubstance.class)
                ).fetchPage(
                        addressForQuery.getPage() == null ? 0 : addressForQuery.getPage(),
                        addressForQuery.getPageSize() == null ? 10 : addressForQuery.getPageSize()
                );
    }

    public DeleteResult delete(AddressForDelete addressForDelete) {
        return jsqlClient.deleteByIds(Address.class, addressForDelete.getIds());
    }
}
