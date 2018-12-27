package com.hzeng.editor;

import com.hzeng.render.RenderHTML;
import com.hzeng.util.Constrains;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.util.Enumeration;

public class Editor {

    private JEditorPane textArea;
    private JEditorPane preArea;

    private Editor() {

        JFrame frame = new JFrame("MarkdownX");
        frame.setName("MarkdownX");

        Directory directory = new Directory(frame);
        directory.setName("directory");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new GridBagLayout());

        frame.setMinimumSize(new Dimension(1280, 1024));
        frame.setIconImage(new ImageIcon("src/main/resources/icons/title/social-markdown.png").getImage());

        JMenuBar menuBar = new MarkdownMenuBar(frame);
        menuBar.setName("menuBar");
        frame.setJMenuBar(menuBar);

        frame.getContentPane().add(directory, new Constrains(0, 0, 1, 1)
                .setWeight(1, 1)
                .setFill(Constrains.BOTH));

        JPanel editor_pane = new JPanel();
        editor_pane.setLayout(new GridLayout(1,2));

        JScrollPane text_pane = new JScrollPane();
        text_pane.setName("text_pane");

        JScrollPane preview_pane = new JScrollPane();
        preview_pane.setName("preview_pane");

        textArea = new MarkdownTextArea();
        textArea.setName("textArea");

        preArea = new MarkdownTextArea();
        preArea.setEditorKit(new HTMLEditorKit());
        preArea.setContentType("text/html;charset=UTF-8");
        preArea.setName("preArea");
/*
        ((PlainDocument)textArea.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
                ((MarkdownTextArea) textArea).setSaved();
               // System.out.println(fb.getDocument().getText(offset, length));
                super.remove(fb, offset, length);
                preArea.setText("<html><body>" + RenderHTML.convertToHTML(textArea.getText()).toString() + "</body></html>");
            }
        });
*/
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                ((MarkdownTextArea) textArea).setSaved();
                String img_pattern = "(?!(\\bsrc\\b\\s*=\\s*['\"]?)(https|http|ftp:file):)(\\bsrc\\b\\s*=\\s*['\"]?)([^'\"]*)(['\"]?)";
                String html = RenderHTML.convertToHTML(textArea.getText()).toString().replaceAll(img_pattern, "$3file:$4$5");
                preArea.setText("<html>" + html + "</html>");
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                ((MarkdownTextArea) textArea).setSaved();
                String img_pattern = "(?!(\\bsrc\\b\\s*=\\s*['\"]?)(https|http|ftp|file):)(\\bsrc\\b\\s*=\\s*['\"]?)([^'\"]*)(['\"]?)";
                String html = RenderHTML.convertToHTML(textArea.getText()).toString().replaceAll(img_pattern, "$3file:$4$5");
                preArea.setText("<html>" + html + "</html>");
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                String img_pattern = "(?!(\\bsrc\\b\\s*=\\s*['\"]?)(https|http|ftp|file):)(\\bsrc\\b\\s*=\\s*['\"]?)([^'\"]*)(['\"]?)";
                String html = RenderHTML.convertToHTML(textArea.getText()).toString().replaceAll(img_pattern, "$3file:$4$5");
                preArea.setText("<html>" + html + "</html>");
            }
        });

        textArea.setMargin(new Insets(5, 5, 5, 5));

        text_pane.getViewport().add(textArea);

        preArea.setMargin(new Insets(5, 5, 5, 5));
        preArea.setEditable(false);
        preview_pane.getViewport().add(preArea);

        editor_pane.add(text_pane);
        editor_pane.add(preview_pane);

        frame.getContentPane().add(editor_pane, new Constrains(1, 0, 1, 1)
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
