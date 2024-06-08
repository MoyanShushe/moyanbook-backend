package com.moyanshushe.service.impl;

/*
 * Author: Hacoj
 * Version: 1.0
 */

import com.moyanshushe.mapper.AddressMapper;
import com.moyanshushe.model.dto.address.AddressForDelete;
import com.moyanshushe.model.dto.address.AddressForQuery;
import com.moyanshushe.model.dto.address.AddressSubstance;
import com.moyanshushe.model.entity.Address;
import com.moyanshushe.service.AddressService;
import org.babyfish.jimmer.Page;
import org.babyfish.jimmer.sql.ast.mutation.DeleteResult;
import org.babyfish.jimmer.sql.ast.mutation.SimpleSaveResult;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressMapper mapper;

    public AddressServiceImpl(AddressMapper mapper) {
        this.mapper = mapper;
    }


    @Override
    public Boolean add(AddressSubstance addressSubstance) {

        SimpleSaveResult<Address> add = mapper.add(addressSubstance.toEntity());

        return add.getAffectedRowCount(Address.class) == 1;
    }

    @Override
    public Boolean update(AddressSubstance addressSubstance) {

        SimpleSaveResult<Address> update = mapper.update(addressSubstance.toEntity());

        return 1 == update.getAffectedRowCount(Address.class);
    }

    @Override
    public Page<AddressSubstance> get(AddressForQuery addressForQuery) {
        return mapper.get(addressForQuery);
    }

    @Override
    public Boolean delete(AddressForDelete addressForDelete) {

        DeleteResult deleteResult = mapper.delete(addressForDelete);

        return deleteResult.getAffectedRowCount(Address.class) == addressForDelete.getIds().size();
    }
}
