package com.idm.identity_recognition_process.producer;

import com.idm.identity_recognition_process.dto.IdentityRecognition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.idm.identity_recognition_process.constant.Constants.PRODUCER_REGISTER_IDENTITY;

@Component
@RequiredArgsConstructor
@Slf4j
public class RegisterIdentityProducerImpl implements RegisterIdentityProducer {
    private final StreamBridge streamBridge;

    public void registerIdentity(IdentityRecognition recognition) {
        Optional.of(streamBridge.send(PRODUCER_REGISTER_IDENTITY, recognition))
                .ifPresentOrElse(
                        success -> log.info("Producer event successfully with id: {}", recognition.getEvent().getEventId()),
                        () -> log.warn("Producer event with error by id: {}", recognition.getEvent().getEventId())
                );

    }

}
