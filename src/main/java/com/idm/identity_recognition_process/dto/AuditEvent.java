package com.idm.identity_recognition_process.dto;

import com.idm.identity_recognition_process.util.LocalDateTimeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class AuditEvent {
    private String eventId;
    private String sessionId;
    private LocalDateTime createdAt;

    public static AuditEvent from(String sessionId){
        return new AuditEvent(UUID.randomUUID().toString(), sessionId, LocalDateTimeUtil.getLocalDateTimeByZoneId(LocalDateTimeUtil.LIMA_ZONE));
    }
}
