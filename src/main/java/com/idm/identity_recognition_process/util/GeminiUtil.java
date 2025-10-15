package com.idm.identity_recognition_process.util;

import com.google.genai.types.Content;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.Part;
import reactor.core.publisher.Mono;

import java.util.List;

public class GeminiUtil {

    private GeminiUtil() {}

    public static Mono<Content> toContent(Part frontal, Part dorsal) {
        Mono<byte[]> frontalBytes = DataBufferUtils.join(frontal.content())
                .map(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    DataBufferUtils.release(dataBuffer);
                    return bytes;
                });

        Mono<byte[]> dorsalBytes = DataBufferUtils.join(dorsal.content())
                .map(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    DataBufferUtils.release(dataBuffer);
                    return bytes;
                });

        return Mono.zip(frontalBytes, dorsalBytes)
                .map(tuple -> Content.builder().parts(List.of(
                        com.google.genai.types.Part.fromText(buildPrompt()),
                        com.google.genai.types.Part.fromBytes(tuple.getT1(), frontal.headers().getContentType().toString()),
                        com.google.genai.types.Part.fromBytes(tuple.getT2(), dorsal.headers().getContentType().toString())
                )).build());
    }

    private static String buildPrompt() {
        return """
            Extract the following fields from the Peruvian National ID (DNI) shown in the provided image(s).
            Return the result as a JSON object with the exact field names listed below:

            - documentNumber
            - issueDate (format: 'YYYY-MM-DD')
            - fullName
            - address

            If any of the fields cannot be confidently extracted, return an empty object with:
            "success": false

            If all fields are successfully extracted, return them along with:
            "success": true"
            """;
    }
}
