/*
 *  Copyright (c) 2018. All Rights Reserved.
 */

package com.hzeng.net;

import com.alibaba.fastjson.JSONObject;
import com.hzeng.config.Global;
import com.hzeng.editor.MarkdownTextArea;
import com.hzeng.file.Change;
import com.hzeng.file.Method;
import com.hzeng.file.Operation;
import com.hzeng.render.RenderHTML;
import com.hzeng.util.FindComponent;
import com.hzeng.util.SerializeUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;


/**
 * @author hzeng
 * @email hzeng1998@gmail.com
 * @date 2018/12/29 18:38
 */
public class DocSync implements Runnable {

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */

    static int fileVersion;

    private static void sendChange() throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("event", "change");
        jsonObject.put("message", SerializeUtils.serialize(Global.getChangeSet().getOperations()));
        jsonObject.put("fileVersion", fileVersion);
        Global.getWebsocketClientEndpoint().sendMessage(jsonObject.toJSONString());
    }

    @Override
    public void run() {

        MarkdownTextArea preArea =  (MarkdownTextArea) FindComponent.searchComponentByName(Global.getFrame(), "preArea");

        while (true) {

            if (Global.getMessage() != null) {
                String message = Global.getMessage();
                Global.setMessage(null);
                Global.getWebsocketClientEndpoint().sendMessage(message);
            }

            if (Global.getParseText() != null) {

                String textArea = Global.getParseText();
                Global.setParseText(null);
                String img_pattern = "(?!(\\bsrc\\b\\s*=\\s*['\"]?)(https|http|ftp:file):)(\\bsrc\\b\\s*=\\s*['\"]?)([^'\"]*)(['\"]?)";
                String html = RenderHTML.convertToHTML(textArea).toString().replaceAll(img_pattern, "$3file:$4$5");
                preArea.setText("<html>" + html + "</html>");
            }
            //first send
            if (Global.getChangeSet().isSend()) {
                synchronized (Global.getChangeSet()) {

                    Global.getChangeSet().mergeOperations();

                    if (!Global.getChangeSet().getOperations().isEmpty()) {
                        Global.getChangeSet().setSend(true);
                        try {
                            sendChange();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public static WebsocketClientEndpoint connectServer() throws URISyntaxException {

        final WebsocketClientEndpoint clientEndpoint = new WebsocketClientEndpoint(new URI("ws://localhost:8080/changeHandler/ID=" + Global.getID()));

        clientEndpoint.addMessageHandler(new WebsocketClientEndpoint.MessageHandler() {
            @Override
            public void handleMessage(String message) {

                HashMap map = JSONObject.parseObject(message, HashMap.class);
//change file
                if (map.containsKey("events") && map.get("events").equals("change")) {

                    fileVersion++;

                    String changeStream = (String) map.get("message");
                    try {
                        Change change = (Change) SerializeUtils.serializeToObject(changeStream);

                        if (map.containsKey("ID") && map.get("ID").equals(Global.getID())) {

                            synchronized (Global.getChangeSet()) {
                                synchronized (Global.getChangeBuffer()) {
                                    Global.setChangeSet(Global.getChangeBuffer());
                                    Global.setChangeSet(new Change());
                                }
                                Global.getChangeSet().setSend(true);
                            }
                        } else {
                            SyncOperations(change);
                            SyncFile(change);
                        }

                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    //send change
                    try {
                        sendChange();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
//invite
                if (map.containsKey("events") && map.get("events").equals("invite")) {

                    Global.setInviteCode((String) map.get("message"));
                    Global.getInviteCode().notify();
                }
            }
        });

        return clientEndpoint;
    }



    public static void SyncFile(Change change) {

        MarkdownTextArea textArea = (MarkdownTextArea) FindComponent.searchComponentByName(Global.getFrame(), "textArea");
        StringBuilder stringBuilder = new StringBuilder(textArea.getText());

        int pos = 0;
        for (Operation operation : change.getOperations()) {
            if (operation.getMethod() == Method.RETAIN) {
                pos += Integer.valueOf(operation.getValue());
            }
            if (operation.getMethod() == Method.INSERT) {
                stringBuilder.insert(pos, operation.getValue());
                pos += operation.getValue().length();
            }
            if (operation.getMethod() == Method.DELETE) {
                stringBuilder.delete(pos, pos + operation.getValue().length());
            }
        }

        textArea.setText(stringBuilder.toString());
    }

    public static void SyncOperations(Change change) {

        synchronized (Global.getChangeSet()) {
            Global.getChangeSet().transformation(change);
        }
        synchronized (Global.getChangeBuffer()) {
            Global.getChangeBuffer().transformation(change);
        }
    }
}
