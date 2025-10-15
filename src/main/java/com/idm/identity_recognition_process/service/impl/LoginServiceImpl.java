package com.idm.identity_recognition_process.service.impl;

import com.idm.identity_recognition_process.document.SessionDocument;
import com.idm.identity_recognition_process.document.UserDocument;
import com.idm.identity_recognition_process.dto.LoginRequest;
import com.idm.identity_recognition_process.dto.LoginResponse;
import com.idm.identity_recognition_process.repository.SessionRepository;
import com.idm.identity_recognition_process.repository.UserRepository;
import com.idm.identity_recognition_process.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginServiceImpl implements LoginService {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;

    @Override
    public Mono<LoginResponse> login(LoginRequest loginRequest) {
        String documentNumber = loginRequest.getDocumentNumber();
        return userRepository.findByDocumentNumber(documentNumber)
                .switchIfEmpty(registerUserByDocumentNumber(documentNumber))
                .flatMap(user -> {
                    user.validateAccessStatus();
                    return persistNewSession(user.getUserId());
                })
                .map(LoginResponse::new);

    }

    private Mono<UserDocument> registerUserByDocumentNumber(String documentNumber) {
        UserDocument userDocument = UserDocument.create(documentNumber);
        return userRepository.save(userDocument)
                .doOnNext(user -> log.info("User created with id: {}", user.getUserId()));
    }

    private Mono<String> persistNewSession(String userId) {
        SessionDocument sessionDocument = SessionDocument.create(userId);
        return sessionRepository.save(sessionDocument)
                .doOnNext(session -> log.info("Session created with id: {}", session.getSessionId()))
                .map(SessionDocument::getSessionId);
    }
}

