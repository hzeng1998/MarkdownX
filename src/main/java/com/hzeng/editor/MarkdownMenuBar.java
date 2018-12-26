package com.hzeng.editor;

import javax.swing.*;

public class MarkdownMenuBar {

    static JMenuBar getMarkdownMenuBar() {

        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File", true);
        JMenu collaborateMenu = new JMenu("Collaborate", true);

        menuBar.add(fileMenu);
        menuBar.add(collaborateMenu);

        fileMenu.add(new JMenuItem("new", new ImageIcon("src/main/resources/icons/baseline_create/1x/file-add.png")));
        fileMenu.add(new JMenuItem("open", new ImageIcon("src/main/resources/icons/baseline_folder/1x/baseline_folder_black_18dp.png")));
        fileMenu.add(new JMenuItem("save", new ImageIcon("src/main/resources/icons/baseline_save/1x/baseline_save_black_18dp.png")));

        collaborateMenu.add(new JMenuItem("connect", new ImageIcon("src/main/resources/icons/friend/friends.png")));
        collaborateMenu.add(new JMenuItem("close", new ImageIcon("src/main/resources/icons/close/close.png")));

        return menuBar;
    }
}
