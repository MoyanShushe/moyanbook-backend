package com.moyanshushe.service;

import com.moyanshushe.model.dto.address.AddressForDelete;
import com.moyanshushe.model.dto.address.AddressForQuery;
import com.moyanshushe.model.dto.address.AddressSubstance;
import org.babyfish.jimmer.Page;

/*
 * Author: Hacoj
 * Version: 1.0
 */
public interface AddressService {
    Boolean add(AddressSubstance addressSubstance);

    Boolean update(AddressSubstance addressSubstance);

    Page<AddressSubstance> get(AddressForQuery addressForQuery);

    Boolean delete(AddressForDelete addressForDelete);
}
