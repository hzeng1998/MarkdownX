/*
 *  Copyright (c) 2018. All Rights Reserved.
 */

package com.hzeng.net;

import javax.websocket.*;
import java.net.URI;

/**
 * @author hzeng
 * @email hzeng1998@gmail.com
 * @date 2018/12/29 16:51
 */

@ClientEndpoint
public class WebsocketClientEndpoint {

    Session userSession = null;

    private MessageHandler messageHandler;

    public WebsocketClientEndpoint(URI endpointURI) {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, endpointURI);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * Callback hook for Connection open events
     *
     * @param userSession the userSession which is opened
     */
    @OnOpen
    public void onOpen(Session userSession) {
        System.out.println("opening websocket");
        this.userSession = userSession;
    }

    /**
     *
     * Callback hook for Connection close events
     *
     * @param userSession the userSession which is opened
     * @param reason reason the reason for connection close
     */
    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        System.out.println("closing websocket");
        this.userSession = null;
    }

    /**
     *
     * Callback hook for message events. This method will be invoked when a client send a message
     *
     * @param message the text message
     */
    @OnMessage
    public void onMessage(String message){
        if (this.messageHandler != null) {
            this.messageHandler.handleMessage(message);
        }
    }


    /**
     *
     * register message handler
     *
     * @param messageHandler message handler
     */
    public void addMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    /**
     *
     * Send a message
     *
     * @param message the text message
     */
    public void sendMessage(String message) {
        this.userSession.getAsyncRemote().sendText(message);
    }

    public static interface MessageHandler {
        public void handleMessage(String message);
    }
}
