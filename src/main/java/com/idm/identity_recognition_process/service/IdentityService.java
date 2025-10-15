package com.idm.identity_recognition_process.service;

import com.idm.identity_recognition_process.dto.IdentityResponse;
import org.springframework.http.codec.multipart.Part;
import reactor.core.publisher.Mono;

public interface IdentityService {
    Mono<Void> processAsync(String sessionId, Part frontal, Part dorsal);
    Mono<IdentityResponse> process(String sessionId, Part frontal, Part dorsal);
}

