/*
 *  Copyright (c) 2018. All Rights Reserved.
 */

package com.hzeng.config;

import com.hzeng.editor.InviteCode;
import com.hzeng.file.Change;
import com.hzeng.net.WebsocketClientEndpoint;
import lombok.Data;

import javax.swing.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author hzeng
 * @email hzeng1998@gmail.com
 * @date 2018/12/31 0:30
 */

@Data
public class Global {

    private static volatile String parseText;

    public static String getParseText() {
        return parseText;
    }

    public static void setParseText(String parseText) {
    Global.parseText = parseText;
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
        parseText = null;
        inviteCode = new InviteCode();
        syncDoc = new AtomicBoolean(true);
        ID = null;
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

    private volatile static  InviteCode inviteCode;

    public static InviteCode getInviteCode() {
        return inviteCode;
    }

    private volatile static AtomicBoolean syncDoc;

    public static AtomicBoolean getSyncDoc() {
        return syncDoc;
    }

    public static void setSyncDoc(AtomicBoolean syncDoc) {
        Global.syncDoc = syncDoc;
    }

    public static void setInviteCode(InviteCode inviteCode) {
        Global.inviteCode = inviteCode;
    }
}

