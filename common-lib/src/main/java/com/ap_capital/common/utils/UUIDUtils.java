package com.ap_capital.common.utils;

import java.util.UUID;

public class UUIDUtils {
    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "").toUpperCase();
    }
}
