package de.unileipzig.analyzewikipedia.dumpreader.dataobjects;

import de.unileipzig.analyzewikipedia.dumpreader.constants.Components;
import de.unileipzig.analyzewikipedia.dumpreader.windows.FileExplorer;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

import java.util.List;
import java.util.LinkedList;

import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeWillExpandListener;

/**
 * @author Danilo Morgado
 * 
 * class represent the tree panel
 */
public final class TreePanel extends JPanel {
    
    private JTree tree;
    private DefaultMutableTreeNode root;
    private List<String> selectedPathes = new LinkedList();
    private String hostname;
    
    /**
     * KONSTRUCTOR: default
     * 
     */
    public TreePanel() {
        
        setLayout(new BorderLayout());
        setRoot();
        
        add(new JScrollPane((JTree)tree),"Center");     
        final Font currentFont = tree.getFont();
        final Font font = new Font(currentFont.getName(), currentFont.getStyle(), 18);
        tree.setFont(font);
        
        // add listener
        // -----------------------------------------------
        
        tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int selRow = tree.getRowForLocation(e.getX(), e.getY());
                TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
                if(selRow != -1 && e.getClickCount() == 2 && selPath != null) {
                    File file = new File(selectedPathes.get(0));
                    if (file.isFile()) {
                        FileExplorer.addModel(selectedPathes.get(0));
                    }
                }
            }
        });
        
        tree.addTreeSelectionListener((TreeSelectionEvent e) -> {
            TreePath[] tree_pathes = tree.getSelectionPaths();
            if (tree_pathes != null){
                selectedPathes.clear();
                for (TreePath tree_pathe : tree_pathes) {
                    Object[] object = tree_pathe.getPath();
                    String path = "";
                    for (int j = 1; j < object.length; j++){
                        path = path + object[j].toString().replace(File.separator, "") + File.separator;
                    }
                    if (path.length() >= 2){
                        if (!path.subSequence(path.length()-2, path.length()-1).equals(":")) path = path.substring(0, path.length()-1);
                        selectedPathes.add(path);
                    }
                }
            }
        });
        
        tree.addTreeWillExpandListener(new TreeWillExpandListener() {
            
            @Override
            public void treeWillCollapse(TreeExpansionEvent treeExpansionEvent) throws ExpandVetoException {

//                TreePath path = treeExpansionEvent.getPath();
//                DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
//                String data = node.getUserObject().toString();
                                
            }

            @Override
            public void treeWillExpand(TreeExpansionEvent treeExpansionEvent) throws ExpandVetoException {

                TreePath path = treeExpansionEvent.getPath();
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                node.removeAllChildren();

                TreeNode treenode = (TreeNode) node;
                String filepath = treenode.toString();
                treenode = treenode.getParent();

                if (treenode != null) {
                    while (!treenode.toString().equals(hostname)){
                        filepath = treenode.toString().replace(File.separator, "") + File.separator + filepath;
                        treenode = treenode.getParent();
                    }

                    File file = new File(filepath);
                    File[] files = file.listFiles();
                    for (File file1 : files) {
                        getList(node, file1);
                    }
                }
            }
        }); 
    }

    /**
     * METHOD: set root of the tree
     * 
     */
    private void setRoot(){
        
        hostname = "root";
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {}
        
        root = new DefaultMutableTreeNode(hostname);
        
        File [] files = File.listRoots();
        for (File file : files) {
            DefaultMutableTreeNode child = new DefaultMutableTreeNode(file.toString());
            root.add(child);
            child.add(new DefaultMutableTreeNode(null));
        }
        
        tree = new JTree(root);
        tree.setRootVisible(false);
        
    }
    
    /**
     * METHOD: set child of the given node
     * 
     * @param node as object
     * @param f as file
     */
    private void getList(DefaultMutableTreeNode node, File f) {

        if(!f.isDirectory()) {
            boolean isCorrect = false;
            for (String ext : Components.getFileExtension()) if (f.getName().endsWith(ext)) isCorrect = true;
            if (isCorrect) {
                DefaultMutableTreeNode child = new DefaultMutableTreeNode(f.getName());
                node.add(child);
            }
        }
        else {
            File[] fList = f.listFiles();
            if (fList != null) {
                DefaultMutableTreeNode child = new DefaultMutableTreeNode(f.getName());
                node.add(child);
                child.add(new DefaultMutableTreeNode(null));
            }
        }
    }
    
    /**
     * METHOD: get selected pathes of tree
     * 
     * @return pathes
     */
    public List<String> getSelectedPathes(){
        
        return selectedPathes;
        
    }

}
