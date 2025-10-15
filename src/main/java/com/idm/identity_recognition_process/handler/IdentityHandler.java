package com.idm.identity_recognition_process.handler;

import com.idm.identity_recognition_process.service.IdentityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class IdentityHandler {

    private final IdentityService identityService;

    public Mono<ServerResponse> process(ServerRequest request) {
        Mono<String> sessionIdMono = Mono.justOrEmpty(request.headers().firstHeader("session-id"));

        Mono<Part> frontalMono = request.multipartData()
                .map(parts -> parts.getFirst("frontalFile"));

        Mono<Part> dorsalMono = request.multipartData()
                .map(parts -> parts.getFirst("dorsalFile"));

        return Mono.zip(sessionIdMono, frontalMono, dorsalMono)
                .flatMap(tuple -> identityService.process(tuple.getT1(), tuple.getT2(), tuple.getT3()))
                .then(ServerResponse.accepted().build());
    }
}

