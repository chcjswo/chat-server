// package me.mocadev.chatserver.chat.config;
//
// import java.util.Set;
// import java.util.concurrent.ConcurrentHashMap;
// import org.springframework.stereotype.Component;
// import org.springframework.web.socket.CloseStatus;
// import org.springframework.web.socket.TextMessage;
// import org.springframework.web.socket.WebSocketSession;
// import org.springframework.web.socket.handler.TextWebSocketHandler;
// import lombok.extern.slf4j.Slf4j;
//
// /**
//  * @author mc.jeon
//  * @version 1.0.0
//  * @since 2025-06-22
//  **/
// @Slf4j
// @Component
// public class SimpleWebSocketHandler extends TextWebSocketHandler {
//
//    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();
//
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) {
//        sessions.add(session);
//        log.info("Connected: {}", session.getId());
//    }
//
//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        String payload = message.getPayload();
// 	   log.info("received message: {}", payload);
//        for(WebSocketSession s : sessions){
//            if(s.isOpen()){
//                s.sendMessage(new TextMessage(payload));
//            }
//        }
//    }
//
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
//        sessions.remove(session);
// 	   log.info("disconnected!!");
//    }
// }
