package com.idm.identity_recognition_process.service;

import com.idm.identity_recognition_process.dto.LoginRequest;
import com.idm.identity_recognition_process.dto.LoginResponse;
import reactor.core.publisher.Mono;

public interface LoginService {
    Mono<LoginResponse> login(LoginRequest loginRequest);
}
