package com.idm.identity_recognition_process.document;

import com.idm.identity_recognition_process.exception.DomainException;
import com.idm.identity_recognition_process.type.UserStatusType;
import com.idm.identity_recognition_process.util.LocalDateTimeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Optional;

@Document(collection = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserDocument {
    @Id
    private String userId;
    private String documentNumber;
    private String issueDate;
    private String fullName;
    private String address;
    private UserStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
    public static UserDocument create(String documentNumber){
        UserStatus userStatus = UserStatus.from(UserStatusType.PENDING);
        return new UserDocument(null, documentNumber, null, null, null, userStatus, LocalDateTimeUtil.getLocalDateTimeByZoneId(LocalDateTimeUtil.LIMA_ZONE), null);
    }

    public void validateAccessStatus() {
        Optional.of(this)
                .filter(this::isBlocked)
                .ifPresent(user -> {
                    throw new DomainException("Contáctate con el administrador para desbloquear tu usuario.");
                });
    }

    public void validateNotVerified() {
        Optional.ofNullable(this.status.code)
                .filter(value -> UserStatusType.VERIFIED.name().equals(value))
                .ifPresent(value -> {
                    throw new IllegalStateException("El usuario ya está verificado");
                });
    }


    private boolean isBlocked(UserDocument user) {
        return UserStatusType.BLOCKED.name().equals(user.getStatus().getCode());
    }

    @AllArgsConstructor
    @Getter
    public static class UserStatus {
        private String code;
        private String description;

        public static UserStatus from(UserStatusType statusType) {
            return new UserStatus(statusType.name(), statusType.getDescription());
        }

    }
}
