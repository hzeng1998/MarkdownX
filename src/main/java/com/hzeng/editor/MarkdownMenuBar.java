package com.hzeng.editor;

import com.hzeng.util.FindComponent;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class MarkdownMenuBar extends JMenuBar {

    MarkdownMenuBar(Container root) {

        JMenu fileMenu = new JMenu("File", true);
        JMenu collaborateMenu = new JMenu("Collaborate", true);

        add(fileMenu);
        add(collaborateMenu);

        JMenuItem jMenuItem;

        fileMenu.add(jMenuItem = new JMenuItem("new", new ImageIcon("src/main/resources/icons/baseline_create/1x/file-add.png")));
        jMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getModifiers() == MouseEvent.BUTTON1_MASK) {

                    MarkdownTextArea textArea;

                    if (!(textArea = (MarkdownTextArea) FindComponent.searchComponentByName(root, "textArea")).isSaved()) {
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
            }
        });

        fileMenu.add(jMenuItem = new JMenuItem("open", new ImageIcon("src/main/resources/icons/baseline_folder/1x/baseline_folder_black_18dp.png")));
        jMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getModifiers() == MouseEvent.BUTTON1_MASK) {
                    MarkdownTextArea textArea;
                    int confirm = 0;
                    if (!(textArea = (MarkdownTextArea) FindComponent.searchComponentByName(root, "textArea")).isSaved()) {
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
            }
        });

        fileMenu.add(jMenuItem = new JMenuItem("save", new ImageIcon("src/main/resources/icons/baseline_save/1x/baseline_save_black_18dp.png")));
        jMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getModifiers() == MouseEvent.BUTTON1_MASK) {
                    MarkdownTextArea textArea;

                    if (!(textArea = (MarkdownTextArea) FindComponent.searchComponentByName(root, "textArea")).isSaved()) {
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
            }
        });

        collaborateMenu.add(new JMenuItem("connect", new ImageIcon("src/main/resources/icons/friend/friends.png")));
        collaborateMenu.add(new JMenuItem("close", new ImageIcon("src/main/resources/icons/close/close.png")));
    }
}
