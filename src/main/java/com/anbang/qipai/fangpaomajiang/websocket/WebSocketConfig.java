package com.anbang.qipai.fangpaomajiang.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

public class WebSocketConfig implements WebSocketConfigurer {

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(gamePlayWsController(), "/gp").setAllowedOrigins("*");
	}

	@Bean
	public GamePlayWsController gamePlayWsController() {
		return new GamePlayWsController();
	}
}
