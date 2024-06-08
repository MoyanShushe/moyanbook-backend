package com.moyanshushe.service.impl;

import com.moyanshushe.model.dto.coupon.CouponForDelete;
import com.moyanshushe.model.dto.coupon.CouponSpecification;
import com.moyanshushe.model.dto.coupon.CouponSubstance;
import com.moyanshushe.model.entity.Coupon;
import com.moyanshushe.model.entity.CouponTable;
import com.moyanshushe.model.entity.Fetchers;
import com.moyanshushe.service.CouponService;
import org.babyfish.jimmer.Page;
import org.babyfish.jimmer.sql.JSqlClient;
import org.babyfish.jimmer.sql.ast.mutation.DeleteResult;
import org.babyfish.jimmer.sql.ast.mutation.SimpleSaveResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
 * Author: Hacoj
 * Version: 1.0
 */
@Service
public class CouponServiceImpl implements CouponService {
    private final JSqlClient jsqlClient;
    private final CouponTable table;
    public CouponServiceImpl(JSqlClient jsqlClient) {
        this.jsqlClient = jsqlClient;
        this.table = CouponTable.$;
    }

    @Override
    public Page<CouponSubstance> get(CouponSpecification couponSpecification) {
        return jsqlClient.createQuery(table)
                .where(couponSpecification)
                .select(
                        table.fetch(CouponSubstance.class)
                )
                .fetchPage(couponSpecification.getPage() == null ? 1 : couponSpecification.getPage(),
                        couponSpecification.getPageSize() == null ? 10 : couponSpecification.getPageSize());
    }

    @Override
    public Boolean add(CouponSubstance couponSubstance) {
        SimpleSaveResult<Coupon> result = jsqlClient.save(couponSubstance);
        return result.isModified();
    }

    @Override
    public Boolean update(CouponSubstance couponSubstance) {
        SimpleSaveResult<Coupon> result = jsqlClient.update(couponSubstance.toEntity());
        return result.isModified();
    }

    @Override
    public Boolean delete(CouponForDelete couponForDelete) {
        DeleteResult result = jsqlClient.deleteByIds(Coupon.class, couponForDelete.getIds());
        return result.getAffectedRowCount(Coupon.class) == couponForDelete.getIds().size();
    }
}
