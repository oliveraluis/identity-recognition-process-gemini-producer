package com.idm.identity_recognition_process.document;

import com.idm.identity_recognition_process.type.SessionStatusType;
import com.idm.identity_recognition_process.util.LocalDateTimeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "sessions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SessionDocument {
    @Id
    private String sessionId;
    private String userId;
    private SessionStatus status;
    private LocalDateTime createdAt;

    public static SessionDocument create(String userId){
        SessionStatus sessionStatus = SessionStatus.from(SessionStatusType.ACTIVE);
        return new SessionDocument(null, userId, sessionStatus, LocalDateTimeUtil.getLocalDateTimeByZoneId(LocalDateTimeUtil.LIMA_ZONE));
    }

    @AllArgsConstructor
    @Getter
    public static class SessionStatus{
        private String code;
        private String description;

        public static SessionStatus from(SessionStatusType statusType){
           return new SessionStatus(statusType.name(), statusType.getDescription());
        }
    }
}
