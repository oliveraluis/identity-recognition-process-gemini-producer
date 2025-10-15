package com.idm.identity_recognition_process.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.ThinkingConfig;
import com.idm.identity_recognition_process.document.SessionDocument;
import com.idm.identity_recognition_process.document.UserDocument;
import com.idm.identity_recognition_process.dto.IdentityRecognition;
import com.idm.identity_recognition_process.producer.RegisterIdentityProducer;
import com.idm.identity_recognition_process.repository.SessionRepository;
import com.idm.identity_recognition_process.repository.UserRepository;
import com.idm.identity_recognition_process.service.IdentityService;
import com.idm.identity_recognition_process.util.GeminiUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class IdentityServiceImpl implements IdentityService {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final Client geminiClient;
    private final RegisterIdentityProducer registerIdentityProducer;

    @Value("${gemini.ia.model}")
    private String model;

    @Override
    public Mono<Void> process(String sessionId, Part frontal, Part dorsal) {
        return validateSession(sessionId)
                .flatMap(session -> validateUser(session.getUserId())
                        .then(invokeGemini(frontal, dorsal))
                )
                .map(recognition -> {
                    recognition.addSession(sessionId);
                    return recognition;
                })
                .flatMap(this::publishResult);
    }


    private Mono<SessionDocument> validateSession(String sessionId) {
        return sessionRepository.findById(sessionId)
                .switchIfEmpty(Mono.error(new RuntimeException("Sesión no encontrada")))
                .doOnNext(SessionDocument::validateActive);
    }

    private Mono<UserDocument> validateUser(String userId) {
        return userRepository.findById(userId)
                .switchIfEmpty(Mono.error(new RuntimeException("Usuario no encontrado")))
                .doOnNext(UserDocument::validateNotVerified);
    }

    private Mono<IdentityRecognition> invokeGemini(Part frontal, Part dorsal) {
        return GeminiUtil.toContent(frontal, dorsal)
                .map(content -> {
                    var config = GenerateContentConfig.builder()
                            .thinkingConfig(ThinkingConfig.builder().thinkingBudget(0).build())
                            .responseMimeType(MimeTypeUtils.APPLICATION_JSON_VALUE)
                            .build();

                    GenerateContentResponse response = geminiClient.models.generateContent(this.model, content, config);
                    log.info("Gemini raw response: {}", response.text());
                    try {
                        return new ObjectMapper().readValue(response.text(), IdentityRecognition.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    private Mono<Void> publishResult(IdentityRecognition recognition) {
        return Mono.fromRunnable(() -> registerIdentityProducer.registerIdentity(recognition));
    }
}
