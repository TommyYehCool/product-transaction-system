package com.ap_capital.common.model.user_module;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class User {
    private Long userId;
    private String name;
    private BigDecimal prepaidAccountBalance;
    private Integer status;
    private Date createdAt;
    private Date updatedAt;
}
