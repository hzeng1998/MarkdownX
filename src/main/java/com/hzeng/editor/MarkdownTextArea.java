/*
 *  Copyright (c) 2018. All Rights Reserved.
 */

package com.hzeng.editor;

import com.hzeng.util.FileReadUtil;
import org.commonmark.node.Node;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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

        setText(FileReadUtil.readAll(currentFile));
        saved = true;
    }

    public void clearArea() {
        currentFile = null;
        setText("");
        ASTnode = null;
        saved = true;
    }
}
