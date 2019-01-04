/*
 *  Copyright (c) 2019. All Rights Reserved.
 */

package com.hzeng.file;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hzeng.config.Global;
import com.hzeng.editor.MarkdownTextArea;
import com.hzeng.net.WebsocketClientEndpoint;
import com.hzeng.util.FindComponent;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * @author hzeng
 * @email hzeng1998@gmail.com
 * @date 2018/12/29 18:38
 */
public class DocSync {

    private static int fileVersion;

    static {
        fileVersion = 0;
    }

    public static boolean sendChange() throws IOException {

        if (Global.getWebsocketClientEndpoint() == null)
            return false;

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("event", "change");
        jsonObject.put("message", JSON.toJSONString(Global.getChangeSet().getOperations()));
        System.out.println(jsonObject.get("message"));
        jsonObject.put("fileVersion", fileVersion);
        jsonObject.put("ID", Global.getID());
        Global.getWebsocketClientEndpoint().sendMessage(jsonObject.toJSONString());

        return true;
    }

    public static WebsocketClientEndpoint connectServer() throws URISyntaxException {

        final WebsocketClientEndpoint clientEndpoint = new WebsocketClientEndpoint(new URI("ws://localhost:8080/changeHandler/?ID=" + Global.getID() + "&" + "code=" + Global.getInviteCode().getCode()));

        clientEndpoint.addMessageHandler(message -> {
            System.out.println(Thread.currentThread().getName());

            HashMap map;
            try {
                map = JSONObject.parseObject(message, HashMap.class);
            }catch (Exception e) {
                e.printStackTrace();
                return;
            }
//change file
            if (map.containsKey("event") && map.get("event").equals("change")) {

                System.out.println("receive change");
                fileVersion++;

                System.out.println(message);

                String changeStream = (String) map.get("message");
                try {

                    JSONArray jsonArray = JSON.parseArray(changeStream);
                    ArrayList<Operation> operations = new ArrayList<>();
                    for (Object o : jsonArray) {
                        operations.add(JSON.parseObject(o.toString(), Operation.class));
                    }

                    Change change = new Change(operations, true);

                    if (map.containsKey("ID") && map.get("ID").equals(Global.getID())) {

                        synchronized (Global.getChangeSet()) {
                            synchronized (Global.getChangeBuffer()) {
                                Global.setChangeSet(Global.getChangeBuffer());
                                Global.setChangeBuffer(new Change());

                                Global.getChangeSet().setSend(true);
                                if (Global.getChangeSet().mergeOperations() != 0) {
                                    if (! sendChange())
                                        Global.getChangeSet().setSend(false);
                                } else {
                                    Global.getChangeSet().setSend(false);
                                }
                            }
                        }
                    } else {
                        System.out.println("synchronize file");
                        Global.setSyncDoc(new AtomicBoolean(false));
                        SyncFile(change);
                        Global.setSyncDoc(new AtomicBoolean(true));
                        SyncOperations(change);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
//invite
            if (map.containsKey("event") && map.get("event").equals("invite")) {

                synchronized (Global.getInviteCode()) {
                    Global.getInviteCode().setCode((String) map.get("code"));
                    Global.getInviteCode().notify();
                }
            }

            if (map.containsKey("event") && map.get("event").equals("pull")) {

                fileVersion++;

                JSONArray jsonArray = JSON.parseArray((String) map.get("message"));
                ArrayList<Operation> operations = new ArrayList<>();
                for (Object o : jsonArray) {
                    operations.add(JSON.parseObject(o.toString(), Operation.class));
                }

                Change change = new Change(operations, true);

                Global.setSyncDoc(new AtomicBoolean(false));
                SyncFile(change);
                Global.setSyncDoc(new AtomicBoolean(true));
                SyncOperations(change);
            }
        });

        return clientEndpoint;
    }

    public static void SyncFile(Change change) {

        MarkdownTextArea textArea = (MarkdownTextArea) FindComponent.searchComponentByName(Global.getFrame(), "textArea");
        StringBuffer stringBuffer = new StringBuffer(textArea.getText());
    //    System.out.println(stringBuilder.toString());
        int pos = 0;
        for (Operation operation : change.getOperations()) {
            if (operation.getMethod() == Method.RETAIN) {
                pos += Integer.valueOf(operation.getValue());
            }
            if (operation.getMethod() == Method.INSERT) {
                stringBuffer.insert(pos, operation.getValue());
                pos += operation.getValue().length();
            }
            if (operation.getMethod() == Method.DELETE) {
                stringBuffer.delete(pos, pos + operation.getValue().length());
            }
        }
     //   System.out.println(stringBuilder.toString());
        textArea.setText(stringBuffer.toString());
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
