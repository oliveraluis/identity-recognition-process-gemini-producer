package com.idm.identity_recognition_process.handler;

import com.idm.identity_recognition_process.dto.LoginRequest;
import com.idm.identity_recognition_process.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class LoginHandler {

    private final LoginService loginService;

    public Mono<ServerResponse> login(ServerRequest request) {
        return request.bodyToMono(LoginRequest.class)
                .flatMap(loginService::login)
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response));
    }
}

