/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.hzeng.editor;

import com.hzeng.util.FindComponent;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

class Directory extends JScrollPane {

    static final ImageIcon ICON_HOME = new ImageIcon("src/main/resources/icons/baseline_home/1x/baseline_home_black_18dp.png");
    static final ImageIcon ICON_FOLDER = new ImageIcon("src/main/resources/icons/directory/folder.png");
    static final ImageIcon ICON_EXPANDEDFOLDER = new ImageIcon("src/main/resources/icons/directory/expandfolder.png");
    static final ImageIcon ICON_FILE = new ImageIcon("src/main/resources/icons/directory/file.png");

    private JTree file_tree;
    private DefaultTreeModel file_model;

    Directory(Container root) {

        DefaultMutableTreeNode top = new DefaultMutableTreeNode(new IconData(ICON_HOME, null, System.getProperty("user.name")));
        DefaultMutableTreeNode node;
        File[] roots = File.listRoots();

        for (File k : roots) {
            node = new DefaultMutableTreeNode(new IconData(ICON_FOLDER, ICON_EXPANDEDFOLDER, new FileNode(k)));
            top.add(node);
            node.add(new DefaultMutableTreeNode(true));
        }

        file_model = new DefaultTreeModel(top);
        file_tree = new JTree(file_model);
        file_tree.putClientProperty("JTree.lineStyle", "Angled");

        TreeCellRenderer renderer = new IconCellRenderer();
        file_tree.setCellRenderer(renderer);

        file_tree.addTreeExpansionListener(new TreeExpansionListener() {
            @Override
            public void treeExpanded(TreeExpansionEvent event) {

                final DefaultMutableTreeNode node = (DefaultMutableTreeNode) (event.getPath().getLastPathComponent());
                final FileNode fileNode = getFileNode(node);

                Thread runner = new Thread(() -> {
                    if (fileNode != null && fileNode.expand(node)) {
                        Runnable runnable = () -> file_model.reload(node);
                        SwingUtilities.invokeLater(runnable);
                    }
                });

                runner.start();
            }

            @Override
            public void treeCollapsed(TreeExpansionEvent event) {
            }
        });

        file_tree.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getSource() == file_tree && e.getClickCount() == 2) {
                    TreePath select_path = file_tree.getPathForLocation(e.getX(), e.getY());
                    if (select_path != null) {

                        String[] file_path = select_path.toString().split(",");
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 1; i < file_path.length; i++) {
                            if (i == file_path.length - 1) {
                                file_path[i] = file_path[i].substring(0, file_path[i].length() - 1);
                            }
                            stringBuilder.append(file_path[i].trim());
                            if (i != 1) {
                                stringBuilder.append('\\');
                            }
                        }
                        File file = new File(stringBuilder.toString());
                        System.out.println(file.getPath());
                        if (file.isFile()) {
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
                                textArea.setCurrentFile(file);
                                try {
                                    textArea.loadFile();
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                            }

                        }

                    }
                }
            }
        });

        file_tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        file_tree.setEditable(false);

        file_tree.setShowsRootHandles(false);

        //   file_tree.setRootVisible(false);

        getViewport().add(file_tree);

    }

    private FileNode getFileNode(DefaultMutableTreeNode node) {

        if (node == null)
            return null;
        Object object = node.getUserObject();
        if (object instanceof IconData)
            object = ((IconData) object).getObject();
        if (object instanceof FileNode)
            return (FileNode) object;
        else
            return null;
    }

    @Override
    public void paintComponents(Graphics g) {
        super.paintComponents(g);
    }
}

class IconCellRenderer extends JLabel implements TreeCellRenderer {

    private Color m_textSelectionColor;
    private Color m_textNonSelectionColor;
    private Color m_bkSelectionColor;
    private Color m_bkNonSelectionColor;
    private Color m_borderSelectionColor;
    private boolean m_selected;

    IconCellRenderer() {
        super();
        m_textSelectionColor = UIManager.getColor("Tree.selectionForeground");
        m_textNonSelectionColor = UIManager.getColor("Tree.textForeground");
        m_bkSelectionColor = UIManager.getColor("Tree.selectionBackground");
        m_bkNonSelectionColor = UIManager.getColor("Tree.textBackground");
        m_borderSelectionColor = UIManager.getColor("Tree.selectionBorderColor");
        setOpaque(false);
    }

    public Component getTreeCellRendererComponent(JTree tree,
                                                  Object value, boolean sel, boolean expanded, boolean leaf,
                                                  int row, boolean hasFocus) {

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        Object obj = node.getUserObject();
        setText(obj.toString());
        if (obj instanceof Boolean)
            setText("Retrieving data...");
        if (obj instanceof IconData) {
            IconData i_data = (IconData) obj;
            if (expanded)
                setIcon(i_data.getExpandedIcon());
            else
                setIcon(i_data.getIcon());
        } else
            setIcon(null);

        setFont(tree.getFont());
        setForeground(sel ? m_textSelectionColor : m_textNonSelectionColor);
        setBackground(sel ? m_bkSelectionColor : m_bkNonSelectionColor);
        m_selected = sel;
        return this;
    }

    public void paintComponent(Graphics g) {

        Color background = getBackground();
        Icon icon = getIcon();
        g.setColor(background);
        int offset = 0;

        if (icon != null && getText() != null)
            offset = (icon.getIconWidth() + getIconTextGap());
        g.fillRect(offset, 0, getWidth() - 1 - offset, getHeight() - 1);

        if (m_selected) {
            g.setColor(m_borderSelectionColor);
            g.drawRect(offset, 0, getWidth() - 1 - offset, getHeight() - 1);
        }

        super.paintComponent(g);
    }
}

class FileNode {

    private File m_file;

    FileNode(File file) {
        m_file = file;
    }

    File getFile() {
        return m_file;
    }

    public String toString() {
        return m_file.getName().length() > 0 ? m_file.getName() : m_file.getPath();
    }

    boolean expand(DefaultMutableTreeNode parent) {
        DefaultMutableTreeNode flag = (DefaultMutableTreeNode) parent.getFirstChild();

        if (flag == null)
            return false;

        Object obj = flag.getUserObject();

        if (!(obj instanceof Boolean))
            return false;

        parent.removeAllChildren();
        File[] files = listFiles();

        if (files == null)
            return true;

        ArrayList<FileNode> fileNodeArrayList = new ArrayList<>();

        for (File file : files) {
            fileNodeArrayList.add(new FileNode(file));
        }

        fileNodeArrayList.sort((o1, o2) -> o1.m_file.getName().compareToIgnoreCase(o2.m_file.getName()));

        for (FileNode fileNode : fileNodeArrayList) {

            IconData i_data;

            if (fileNode.m_file.isFile())
                i_data = new IconData(Directory.ICON_FILE, fileNode);
            else
                i_data = new IconData(Directory.ICON_FOLDER, Directory.ICON_EXPANDEDFOLDER, fileNode);

            DefaultMutableTreeNode node = new DefaultMutableTreeNode(i_data);

            parent.add(node);

            if (fileNode.listFiles() != null)

                node.add(new DefaultMutableTreeNode(true));
        }
        return true;
    }


    private File[] listFiles() {
        if (!m_file.isDirectory())
            return null;

        try {
            return m_file.listFiles();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    "Error reading directory " + m_file.getAbsolutePath(),
                    "Warning", JOptionPane.WARNING_MESSAGE);
            return null;
        }
    }
}

class IconData {

    private Icon m_icon;
    private Icon m_expandedIcon;
    private Object m_data;

    public IconData(Icon icon, Object data) {
        m_icon = icon;
        m_expandedIcon = null;
        m_data = data;
    }

    IconData(Icon icon, Icon expandedIcon, Object data) {
        m_icon = icon;
        m_expandedIcon = expandedIcon;
        m_data = data;
    }

    Icon getIcon() {
        return m_icon;
    }

    Icon getExpandedIcon() {
        return m_expandedIcon != null ? m_expandedIcon : m_icon;
    }

    Object getObject() {
        return m_data;
    }

    public String toString() {
        return m_data.toString();
    }
}