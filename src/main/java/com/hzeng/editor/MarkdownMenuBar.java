/*
 *  Copyright (c) 2018 All Rights Reserved.
 */

package com.hzeng.editor;

import com.alibaba.fastjson.JSONObject;
import com.hzeng.config.Global;
import com.hzeng.net.DocSync;
import com.hzeng.util.FindComponent;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class MarkdownMenuBar extends JMenuBar {

    MarkdownMenuBar() {

        JMenu fileMenu = new JMenu("file", true);
        JMenu collaborateMenu = new JMenu("Collaborate", true);

        add(fileMenu);
        add(collaborateMenu);

        JMenuItem jMenuItem;

        fileMenu.add(jMenuItem = new JMenuItem("new", new ImageIcon("src/main/resources/icons/baseline_create/1x/file-add.png")));
        jMenuItem.addActionListener(e -> {
            if (e.getModifiers() == MouseEvent.BUTTON1_MASK) {

                MarkdownTextArea textArea;

                if (!(textArea = (MarkdownTextArea) FindComponent.searchComponentByName(Global.getFrame(), "textArea")).isSaved()) {
                    if (JOptionPane.showConfirmDialog(null, "Save current file?", "Save", JOptionPane.YES_NO_CANCEL_OPTION) == 0) {

                        if (textArea.getCurrentFile() == null) {
                            JFileChooser jFileChooser = new JFileChooser();
                            jFileChooser.setFileFilter(new FileNameExtensionFilter("markdown", "md"));
                            jFileChooser.setSelectedFile(new File(".md"));
                            int return_val = jFileChooser.showDialog(null, "确定");
                            if (return_val == JFileChooser.APPROVE_OPTION)
                                textArea.setCurrentFile(jFileChooser.getSelectedFile());
                        }
                        try {
                            textArea.saveFile();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                textArea.clearArea();
            }
        });

        fileMenu.add(jMenuItem = new JMenuItem("open", new ImageIcon("src/main/resources/icons/baseline_folder/1x/baseline_folder_black_18dp.png")));
        jMenuItem.addActionListener(e -> {
            if (e.getModifiers() == MouseEvent.BUTTON1_MASK) {
                MarkdownTextArea textArea;
                int confirm = 0;
                if (!(textArea = (MarkdownTextArea) FindComponent.searchComponentByName(Global.getFrame(), "textArea")).isSaved()) {
                    if ((confirm = JOptionPane.showConfirmDialog(null, "Save current file?", "Save", JOptionPane.YES_NO_CANCEL_OPTION)) == 0) {

                        if (null == textArea.getCurrentFile()) {
                            JFileChooser jFileChooser = new JFileChooser();
                            jFileChooser.setFileFilter(new FileNameExtensionFilter("markdown", "md"));
                            jFileChooser.setSelectedFile(new File(".md"));
                            int return_val = jFileChooser.showDialog(null, "确定");
                            if (return_val == JFileChooser.APPROVE_OPTION)
                                textArea.setCurrentFile(jFileChooser.getSelectedFile());
                        }
                        try {
                            textArea.saveFile();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                if (confirm != 2) {
                    JFileChooser jFileChooser = new JFileChooser();
                    jFileChooser.setFileFilter(new FileNameExtensionFilter("markdown", "md"));
                    jFileChooser.setSelectedFile(new File(".md"));
                    int return_val = jFileChooser.showDialog(null, "确定");
                    if (return_val == JFileChooser.APPROVE_OPTION)
                        textArea.setCurrentFile(jFileChooser.getSelectedFile());
                    try {
                        textArea.loadFile();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }

            }
        });

        fileMenu.add(jMenuItem = new JMenuItem("save", new ImageIcon("src/main/resources/icons/baseline_save/1x/baseline_save_black_18dp.png")));
        jMenuItem.addActionListener(e -> {
            if (e.getModifiers() == MouseEvent.BUTTON1_MASK) {
                MarkdownTextArea textArea;

                if (!(textArea = (MarkdownTextArea) FindComponent.searchComponentByName(Global.getFrame(), "textArea")).isSaved()) {
                    if (null == textArea.getCurrentFile()) {
                        JFileChooser jFileChooser = new JFileChooser();
                        jFileChooser.setFileFilter(new FileNameExtensionFilter("markdown", "md"));
                        jFileChooser.setSelectedFile(new File(".md"));
                        int return_val = jFileChooser.showDialog(null, "确定");
                        if (return_val == JFileChooser.APPROVE_OPTION) {
                            textArea.setCurrentFile(jFileChooser.getSelectedFile());
                            try {
                                textArea.saveFile();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                    else {
                        try {
                            textArea.saveFile();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        });

        collaborateMenu.add(jMenuItem = new JMenuItem("connect", new ImageIcon("src/main/resources/icons/friend/friends.png")));
        jMenuItem.addActionListener(e -> {
            if (e.getModifiers() == MouseEvent.BUTTON1_MASK) {
                String text = (String) JOptionPane.showInputDialog(null, "Input the invite code, if have one", "Collaborate", JOptionPane.OK_CANCEL_OPTION, new ImageIcon("src/main/resources/icons/invite/technology_connected.png"), null, "");
                if (text != null) {
                    if (text.equals("")) {
                        try {
                            Global.setWebsocketClientEndpoint(DocSync.connectServer());
                        } catch (URISyntaxException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        });

        collaborateMenu.add(new JMenuItem("close", new ImageIcon("src/main/resources/icons/close/close.png")));
        collaborateMenu.add(jMenuItem = new JMenuItem("invite", new ImageIcon("src/main/resources/icons/invite/btn_ invite.png")));
        jMenuItem.addActionListener(e -> {
            if (e.getModifiers() == MouseEvent.BUTTON1_MASK) {
                if (Global.getWebsocketClientEndpoint() == null) {
                    JOptionPane.showMessageDialog(null, "Cannot connect to Server, connect first", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("event", "invite");
                    Global.setMessage(jsonObject.toJSONString());
                    try {
                        Global.getInviteCode().wait();
                        JOptionPane.showInputDialog(null, "Paste to connect input", "Invite Code", JOptionPane.QUESTION_MESSAGE,null,null,Global.getInviteCode());
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }
}
