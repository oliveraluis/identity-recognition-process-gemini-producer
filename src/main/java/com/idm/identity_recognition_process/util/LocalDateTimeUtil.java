package com.idm.identity_recognition_process.util;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class LocalDateTimeUtil {

    public static final String LIMA_ZONE = "America/Lima";

    public static LocalDateTime getLocalDateTimeByZoneId(String zoneId){
        return LocalDateTime.now(ZoneId.of(zoneId));
    }
}
