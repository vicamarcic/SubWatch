package vica.SubWatch.components;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import vica.SubWatch.service.JwtService;

@Component
public class StompAuthChannelInterceptor implements ChannelInterceptor {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public StompAuthChannelInterceptor(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (accessor.getCommand() == null) {
            return message;
        }

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = firstNativeHeader(accessor, HttpHeaders.AUTHORIZATION);

            if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
                throw new IllegalArgumentException("Missing Authorization header in STOMP CONNECT");
            }

            String token = authHeader.substring(7);

            String username = jwtService.extractUsername(token);
            if (!StringUtils.hasText(username)) {
                throw new IllegalArgumentException("Invalid JWT token (no subject)");
            }

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (!jwtService.isTokenValid(token, userDetails)) {
                throw new IllegalArgumentException("Invalid or expired JWT token");
            }

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

            accessor.setUser(authentication);
        }

        return message;
    }

    private String firstNativeHeader(StompHeaderAccessor accessor, String name) {
        List<String> values = accessor.getNativeHeader(name);
        return (values == null || values.isEmpty()) ? null : values.get(0);
    }
}