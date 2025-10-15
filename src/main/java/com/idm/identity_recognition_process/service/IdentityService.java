package com.idm.identity_recognition_process.service;

import org.springframework.http.codec.multipart.Part;
import reactor.core.publisher.Mono;

public interface IdentityService {
    Mono<Void> process(String sessionId, Part frontal, Part dorsal);
}

