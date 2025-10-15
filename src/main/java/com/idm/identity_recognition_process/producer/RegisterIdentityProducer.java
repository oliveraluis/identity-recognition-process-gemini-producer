package com.idm.identity_recognition_process.producer;

import com.idm.identity_recognition_process.dto.IdentityRecognition;

public interface RegisterIdentityProducer{
    void registerIdentity(IdentityRecognition recognition);
}
