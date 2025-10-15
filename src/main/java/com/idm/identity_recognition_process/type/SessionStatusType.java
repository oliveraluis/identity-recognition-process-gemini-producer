package com.idm.identity_recognition_process.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SessionStatusType {
    ACTIVE("Activo"),
    INACTIVE("Inactivo");
    private final String description;
}
