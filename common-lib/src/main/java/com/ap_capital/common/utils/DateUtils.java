package com.ap_capital.common.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

public class DateUtils {

    public static Date getStartOfDay() {
        // 當天 00:00:00，GMT+8 時區
        LocalDateTime startOfDay = LocalDateTime.now(ZoneId.of("Asia/Taipei")).withHour(0).withMinute(0).withSecond(0).withNano(0);
        return Date.from(startOfDay.toInstant(ZoneOffset.ofHours(8)));
    }

    public static Date getEndOfDay() {
        // 當天 23:59:59，GMT+8 時區
        LocalDateTime endOfDay = LocalDateTime.now(ZoneId.of("Asia/Taipei")).withHour(23).withMinute(59).withSecond(59).withNano(999999999);
        return Date.from(endOfDay.toInstant(ZoneOffset.ofHours(8)));
    }
}
