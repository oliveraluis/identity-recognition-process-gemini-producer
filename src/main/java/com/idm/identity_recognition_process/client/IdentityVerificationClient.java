package com.idm.identity_recognition_process.client;

import com.idm.identity_recognition_process.dto.IdentityRecognition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class IdentityVerificationClient {

    private final WebClient identityWebClient;

    public void sendIdentityRecognition(IdentityRecognition recognition) {
        identityWebClient.post()
                .uri("/identities")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(recognition)
                .retrieve()
                .toBodilessEntity()
                .doOnSuccess(response -> log.info("Identity recognition sent successfully. eventId={}", recognition.getEvent().getEventId()))
                .doOnError(error -> log.error("Failed to send identity recognition. eventId={}, reason={}",
                        recognition.getEvent().getEventId(), error.getMessage()))
                .then();
    }
}

