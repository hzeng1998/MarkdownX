/*
 *  Copyright (c) 2018. All Rights Reserved.
 */

package com.hzeng.config;

import com.hzeng.file.Change;
import com.hzeng.net.WebsocketClientEndpoint;
import lombok.Data;

import javax.swing.*;

/**
 * @author hzeng
 * @email hzeng1998@gmail.com
 * @date 2018/12/31 0:30
 */

@Data
public class Global {

    private static volatile String message;

    private static volatile String parseText;

    public static String getParseText() {
        return parseText;
    }

    public static void setParseText(String parseText) {
        Global.parseText = parseText;
    }

    public static String getMessage() {
        return message;
    }

    public static void setMessage(String message) {
        Global.message = message;
    }

    public static WebsocketClientEndpoint getWebsocketClientEndpoint() {
        return websocketClientEndpoint;
    }

    public static void setWebsocketClientEndpoint(WebsocketClientEndpoint websocketClientEndpoint) {
        Global.websocketClientEndpoint = websocketClientEndpoint;
    }

    static {
        changeBuffer = new Change();
        changeSet = new Change();
        websocketClientEndpoint = null;
        message = null;
        parseText = null;
    }

    private static WebsocketClientEndpoint websocketClientEndpoint;

    public static JFrame getFrame() {
        return frame;
    }

    public static String getID() {
        return ID;
    }

    private volatile static JFrame frame;

    private volatile static String ID;

    public static void setFrame(JFrame frame) {
        Global.frame = frame;
    }

    public static void setID(String ID) {
        Global.ID = ID;
    }

    public static void setChangeBuffer(Change changeBuffer) {
        Global.changeBuffer = changeBuffer;
    }

    public static void setChangeSet(Change changeSet) {
        Global.changeSet = changeSet;
    }

    private volatile static Change changeBuffer;

    public static Change getChangeBuffer() {
        return changeBuffer;
    }

    public static Change getChangeSet() {
        return changeSet;
    }

    private volatile static Change changeSet;

    private volatile static String inviteCode;

    public static String getInviteCode() {
        return inviteCode;
    }

    public static void setInviteCode(String inviteCode) {
        Global.inviteCode = inviteCode;
    }
}
