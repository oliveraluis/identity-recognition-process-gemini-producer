package com.idm.identity_recognition_process.repository;

import com.idm.identity_recognition_process.document.SessionDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface SessionRepository extends ReactiveMongoRepository<SessionDocument, String> {
}
