package com.hzeng.editor;

import org.commonmark.node.Node;

import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class MarkdownTextArea extends JEditorPane {

    private boolean saved;

    private Node ASTnode;

    private File currentFile;

    public File getCurrentFile() {
        return currentFile;
    }

    public void setSaved() {
        saved = false;
    }

    public void setCurrentFile(File file) {
        currentFile = file;
    }

    MarkdownTextArea() {

        saved = true;
        ASTnode = null;
        currentFile = null;
    }

    public boolean isSaved() {
        return saved;
    }

    public void saveFile() throws IOException {

        FileOutputStream fileOutputStream = new FileOutputStream(currentFile);
        fileOutputStream.write(getText().getBytes());
        fileOutputStream.close();
        saved = true;
    }

    public void loadFile() throws IOException {

        FileInputStream fileInputStream = new FileInputStream(currentFile);

        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

        byte[] bytes = new byte[4096];

        int len;
        StringBuilder stringBuilder = new StringBuilder();
        while ((len = bufferedInputStream.read(bytes, 0, bytes.length)) != -1) {
            stringBuilder.append(new String(bytes, 0, len, StandardCharsets.UTF_8));
        }

        setText(stringBuilder.toString());
        saved = true;
        bufferedInputStream.close();
    }

    public void clearArea() {
        currentFile = null;
        setText("");
        ASTnode = null;
        saved = true;
    }
}
