package com.ap_capital.merchant.service;

import com.ap_capital.common.cnst.CommonStatus;
import com.ap_capital.common.model.merchant_module.Merchant;
import com.ap_capital.common.req.merchant_module.AddMerchantReq;
import com.ap_capital.merchant.mapper.MerchantMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MerchantService {

    private final MerchantMapper merchantMapper;

    public MerchantService(MerchantMapper merchantMapper) {
        this.merchantMapper = merchantMapper;
    }

    public void addMerchant(AddMerchantReq req) {
        Merchant merchant = new Merchant();
        BeanUtils.copyProperties(req, merchant);
        merchant.setStatus(CommonStatus.Active.getValue());
        merchant.setCreatedAt(new Date());
        merchantMapper.insert(merchant);
    }

    public void addInventory() {
    }

    public void settlement() {
    }
}
