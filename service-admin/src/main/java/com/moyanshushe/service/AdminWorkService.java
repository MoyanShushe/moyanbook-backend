package com.moyanshushe.service;

import com.moyanshushe.model.dto.item.ItemForConfirm;
import com.moyanshushe.model.entity.AdminConfirm;

import java.util.List;

/*
 * Author: Hacoj
 * Version: 1.0
 */
public interface AdminWorkService {

    void remindToReceiveItem();

    List<AdminConfirm> queryItems(String queryId);

    void confirmToReceive(String confirmCode);

    void confirmToDeliver(ItemForConfirm itemForConfirm);

    void remindToDeliver();
}
