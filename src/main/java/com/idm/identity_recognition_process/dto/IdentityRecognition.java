package com.idm.identity_recognition_process.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class IdentityRecognition {
    private String documentNumber;
    private String issueDate;
    private String fullName;
    private String address;
    private Boolean success;
}
