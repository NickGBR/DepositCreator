package ru.interns.deposit.config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import ru.interns.deposit.security.JwtTokenProvider;
import ru.interns.deposit.util.Api;

/**
 * Класс для настройки параметров WebSocket
 */
@Configuration
@EnableWebSocketMessageBroker
@EnableScheduling
@Slf4j
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    private final JwtTokenProvider tokenProvider;

    @Autowired
    public WebSocketConfiguration(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(Api.SOCKET_ENDPOINT.getUrl())
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/deposit");
    }

    /**
     * Устанавливаем перехватчик сообщений, чтобы аутентифицировать
     * пользователя при подключение через STOMP заголовок, согласно:
     * https://github.com/spring-projects/spring-framework/blob/master/src/docs/asciidoc/web/websocket.adoc#token-authentication
     *
     * @param registration - конфигурации канала, включающая в частности перехватчики сообщений
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                System.out.println(message);
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if ((accessor != null) && StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String token = accessor.getFirstNativeHeader("x-auth-token");
                    log.debug("WebSocket Connection attempt. Token: " + token);
                    Authentication user = tokenProvider.getAuthentication(token);
                    accessor.setUser(user);
                }
                return message;
            }
        });
    }

}