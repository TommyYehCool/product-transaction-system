package com.ap_capital.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class IDGenerator {
    public static Long getOrderId() {
        return getCurrentTimeAsId();
    }

    public static Long getTransactionId() {
        return getCurrentTimeAsId();
    }

    public static Long getReconciliationId() {
        return getCurrentTimeAsId();
    }

    private static Long getCurrentTimeAsId() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return Long.parseLong(sdf.format(new Date()));
    }
}
