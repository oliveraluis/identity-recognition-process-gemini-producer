package com.idm.identity_recognition_process.router;

import com.idm.identity_recognition_process.handler.IdentityHandler;
import com.idm.identity_recognition_process.handler.LoginHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterConfig {

    @Bean
    public RouterFunction<ServerResponse> routes(LoginHandler loginHandler,
                                                 IdentityHandler identityHandler) {
        return RouterFunctions.route()
                .POST("/login", loginHandler::login)
                .POST("/identity/async", identityHandler::processAsync)
                .POST("/identity", identityHandler::process)
                .build();
    }
}
