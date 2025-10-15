package com.idm.identity_recognition_process.repository;

import com.idm.identity_recognition_process.document.UserDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<UserDocument, String> {
    Mono<UserDocument> findByDocumentNumber(String documentNumber);
}
