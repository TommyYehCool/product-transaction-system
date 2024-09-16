package com.ap_capital.common.model.user_module;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
public class User {
    private Long userId;
    private String name;
    private BigDecimal prepaidAccountBalance;
    private Integer status;
    private Date createdAt;
    private Date updatedAt;
}
