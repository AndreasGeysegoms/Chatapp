package web4.deelopdracht1.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import web4.deelopdracht1.websockets.*;

@Configuration
public class WebSocketConfiguration implements WebSocketConfigurer {

    @Bean
    public AlpineSocket alpineSocket() {
        return new AlpineSocket();
    }

    @Bean
    public BrabusSocket brabusSocket() {
        return new BrabusSocket();
    }

    @Bean
    public CizetaSocket cizetaSocket() {
        return new CizetaSocket();
    }

    @Bean
    public DauerSocket dauerSocket() {
        return new DauerSocket();
    }

    @Bean
    public ElementalSocket elementalSocket() {
        return new ElementalSocket();
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new BrabusSocket(), "/b");
        registry.addHandler(new AlpineSocket(), "/a");
        registry.addHandler(new CizetaSocket(), "/c");
        registry.addHandler(new DauerSocket(), "/d");
        registry.addHandler(new ElementalSocket(), "/e");
    }
}