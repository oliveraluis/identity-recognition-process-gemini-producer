package com.idm.identity_recognition_process.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class IdentityRecognition {
    private String documentNumber;
    private String issueDate;
    private String fullName;
    private String address;
    private Boolean success;
    private AuditEvent event;

    public void addSession(String sessionId){
        this.event = AuditEvent.from(sessionId);
    }
}
