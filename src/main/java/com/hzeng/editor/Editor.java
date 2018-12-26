package com.hzeng.editor;

import com.hzeng.util.Constrains;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.util.Enumeration;

public class Editor {

    private JTextArea preArea;
    private JTextArea textArea;

    private Editor() {

        JFrame frame = new JFrame("MarkdownX");

        Directory directory = new Directory();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new GridBagLayout());

        frame.setMinimumSize(new Dimension(1280, 1024));
        frame.setIconImage(new ImageIcon("src/main/resources/icons/title/social-markdown.png").getImage());

        JMenuBar menuBar = MarkdownMenuBar.getMarkdownMenuBar();

        frame.setJMenuBar(menuBar);

        frame.getContentPane().add(directory, new Constrains(0, 0, 1, 1)
                .setWeight(1, 1)
                .setFill(Constrains.BOTH));

        JScrollPane text_pane = new JScrollPane();
        JScrollPane preview_pane = new JScrollPane();
        textArea = new JTextArea();
        preArea = new JTextArea();

        preArea.setDocument(textArea.getDocument());

        textArea.setMargin(new Insets(5, 5, 5, 5));

        text_pane.getViewport().add(textArea);

        preArea.setMargin(new Insets(5, 5, 5, 5));
        preArea.setEditable(false);
        preview_pane.getViewport().add(preArea);

        frame.getContentPane().add(text_pane, new Constrains(1, 0, 3, 1)
                .setFill(Constrains.BOTH)
                .setWeight(4, 1));

        frame.getContentPane().add(preview_pane, new Constrains(4, 0, 7, 1)
                .setFill(Constrains.BOTH)
                .setWeight(4, 1));

        frame.setVisible(true);
    }

    private static void initGlobalFont() {

        FontUIResource fontUIResource = new FontUIResource(new Font("Consolas", Font.PLAIN, 16));

        for (Enumeration keys = UIManager.getDefaults().keys(); keys.hasMoreElements(); ) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, fontUIResource);
            }
        }

    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //initGlobalFont();
        new Editor();

    }
}
