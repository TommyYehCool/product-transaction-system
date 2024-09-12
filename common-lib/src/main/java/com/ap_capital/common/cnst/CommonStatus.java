package com.ap_capital.common.cnst;

import lombok.Getter;

@Getter
public enum CommonStatus {
    Inactive(0),
    Active(1);

    private final int value;

    CommonStatus(int value) {
        this.value = value;
    }

}
