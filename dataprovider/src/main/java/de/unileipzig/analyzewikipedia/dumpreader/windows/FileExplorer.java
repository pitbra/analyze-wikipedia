package de.unileipzig.analyzewikipedia.dumpreader.windows;

import de.unileipzig.analyzewikipedia.dumpreader.constants.Components;
import de.unileipzig.analyzewikipedia.dumpreader.controller.ThreadController;
import de.unileipzig.analyzewikipedia.dumpreader.dataobjects.TreePanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

/**
 * @author Danilo Morgado
 * 
 * class window for file explorer
 */
public class FileExplorer extends JFrame {
    
    private final String[] MAIN_BUTTON_STRINGS = {"Ok", "Abbrechen"};
    private final String[] TEXTAREA_BUTTON_STRINGS = {"Hinzufügen", "Entfernen"};
    private final String[] TEXTAREA_STRING = {"Dateisystem", "Ausgewählte Dateien oder Ordner"};
    private final String[] TEXT = {"Sie können für die Verarbeitung Dateien oder Ordner hinzufügen, die auf dem Computer verfügbar sind.",
                                    "Es wird keine Suche in Unterordner der getroffenen Auswahl durchgeführt."};
    private final String DESCRIPTION = "Beschreibung";
    
    private final Font FONT = new Font("Tahoma", Font.PLAIN, 18);
    
    private final JPanel CONTENTPANE;
    private final TreePanel TREEPANEL = new TreePanel();
    private final JList<String> TEXTLIST;
    private final JButton[] MAIN_BUTTONS = new JButton[MAIN_BUTTON_STRINGS.length];
    private final JButton[] TEXTAREA_BUTTONS = new JButton[TEXTAREA_BUTTON_STRINGS.length];
    private final JCheckBox[] CHECKBOX_LINKS = new JCheckBox[Components.getTrickerNames().length];
    
    private static final DefaultListModel<String> MODEL = new DefaultListModel<>();
    
    /**
     * KONSTRUCTOR: default
     * 
     */
    public FileExplorer() {
        
        setTitle(Components.getApplicationName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setWindow(this);
        
        CONTENTPANE = new JPanel();
        CONTENTPANE.setBorder(new EmptyBorder(5, 5, 5, 5));
        CONTENTPANE.setLayout(new BorderLayout(0, 0));
        setContentPane(CONTENTPANE);
                
        JLabel toplabel = new JLabel(TEXT[0]);
        toplabel.setBorder(new EmptyBorder(5, 5, 5, 5));
        toplabel.setFont(FONT);
        CONTENTPANE.add(toplabel, BorderLayout.NORTH);
        
        JPanel centerpane = new JPanel();
        centerpane.setLayout(new FlowLayout(FlowLayout.CENTER));
        CONTENTPANE.add(centerpane, BorderLayout.CENTER);
        
        int axis_x = (int)(getWidth()*0.3);
        int axis_y = (int)(getHeight()*0.65);
        JPanel[] pan = new JPanel[4];
        for (int i = 0; i < 2; i++){
            pan[i*2] = new JPanel();
            pan[i*2].setBorder(new EmptyBorder(5, 5, 5, 5));
            pan[i*2].setLayout(new BorderLayout(0, 0));
            pan[i*2].setPreferredSize(new Dimension(axis_x, axis_y));
            centerpane.add(pan[i*2]);
            
            JLabel label1 = new JLabel(TEXTAREA_STRING[i] + ":");
            label1.setFont(FONT);
            pan[i*2].add(label1, BorderLayout.NORTH);
            
            pan[i*2+1] = new JPanel();
            pan[i*2+1].setBorder(new EmptyBorder(5, 5, 5, 5));
            pan[i*2+1].setLayout(new BorderLayout(0, 0));
            pan[i*2+1].setPreferredSize(new Dimension(axis_x/2, axis_y));
            centerpane.add(pan[i*2+1]);
            
            JLabel label2 = new JLabel(" ");
            label2.setFont(FONT);
            pan[i*2+1].add(label2, BorderLayout.NORTH);
        }
        
        pan[0].add(TREEPANEL, BorderLayout.CENTER);
        
        JPanel middle_buttons = new JPanel();
        middle_buttons.setLayout(new GridLayout(2, 1, 1, 1));
        pan[1].add(middle_buttons, BorderLayout.CENTER);
        
        JPanel btt_top = new JPanel();
        middle_buttons.add(btt_top);
        JPanel btt_bot = new JPanel();
        middle_buttons.add(btt_bot);
        
        JPanel[] barray = new JPanel[TEXTAREA_BUTTONS.length];
        
        for (int i = 0; i < TEXTAREA_BUTTONS.length; i++){
            barray[i] = new JPanel();
            barray[i].setLayout(new FlowLayout(FlowLayout.CENTER));
            btt_top.add(barray[i]);
            
            TEXTAREA_BUTTONS[i] = new JButton(TEXTAREA_BUTTON_STRINGS[i]);
            TEXTAREA_BUTTONS[i].setFont(FONT);
            barray[i].add(TEXTAREA_BUTTONS[i]);
            TEXTAREA_BUTTONS[i].setMnemonic(TEXTAREA_BUTTON_STRINGS[i].charAt(0));
            TEXTAREA_BUTTONS[i].setPreferredSize(new Dimension(130, 30));
        }
        
        //MODEL = new DefaultListModel<>();
        TEXTLIST = new JList<>(MODEL);
        TEXTLIST.setFont(FONT);
        pan[2].add(new JScrollPane(TEXTLIST), BorderLayout.CENTER);
        
        JPanel checkbox_panel = new JPanel();
        checkbox_panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        checkbox_panel.setLayout(new GridLayout(CHECKBOX_LINKS.length, 1, 1, 1));
        pan[3].add(checkbox_panel, BorderLayout.CENTER);
        for (int i = 0; i < CHECKBOX_LINKS.length; i++){
            CHECKBOX_LINKS[i] = new JCheckBox(Components.getTrickerNames()[i]);
            CHECKBOX_LINKS[i].setFont(FONT);
            checkbox_panel.add(CHECKBOX_LINKS[i]);
            CHECKBOX_LINKS[i].setSelected(true);
            Components.setTricker(i+1, true);
        }
        
        JPanel bottom = new JPanel();
        bottom.setLayout(new BorderLayout(0, 0));
        bottom.setBorder(new EmptyBorder(5, 5, 5, 5));
        CONTENTPANE.add(bottom, BorderLayout.SOUTH);
        
        JPanel description = new JPanel();
        description.setLayout(new BorderLayout(0, 0));
        bottom.add(description, BorderLayout.CENTER);
        
        JLabel description_label = new JLabel(DESCRIPTION + ":");
        description_label.setFont(FONT);
        description.add(description_label, BorderLayout.NORTH);
        
        JPanel description_text = new JPanel();
        description_text.setLayout(new FlowLayout(FlowLayout.LEFT));
        description_text.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        description.add(description_text, BorderLayout.CENTER);
        
        JLabel text = new JLabel();
        text.setText(TEXT[1]);
        text.setBorder(new EmptyBorder(5, 5, 5, 5));
        text.setFont(FONT);
        description_text.add(text, BorderLayout.CENTER);
        
        JPanel main_buttons = new JPanel();
        main_buttons.setBorder(new EmptyBorder(5, 5, 5, 5));
        main_buttons.setLayout(new GridLayout(1, 2, 1, 1));
        bottom.add(main_buttons, BorderLayout.SOUTH);
        
        JPanel empty_panel = new JPanel();
        empty_panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        main_buttons.add(empty_panel);
                
        JPanel windows_control = new JPanel();
        windows_control.setLayout(new FlowLayout(FlowLayout.RIGHT));
        main_buttons.add(windows_control);
        
        for (int i = 0; i < MAIN_BUTTON_STRINGS.length; i++){
            MAIN_BUTTONS[i] = new JButton(MAIN_BUTTON_STRINGS[i]);
            MAIN_BUTTONS[i].setFont(FONT);
            windows_control.add(MAIN_BUTTONS[i]);
            MAIN_BUTTONS[i].setMnemonic(MAIN_BUTTON_STRINGS[i].charAt(0));
            MAIN_BUTTONS[i].setPreferredSize(new Dimension(130, 30));
        }
        
        // add listeners
        // -------------------------------
        
        TEXTLIST.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(e.getClickCount() == 2) {
                    MODEL.remove(TEXTLIST.getSelectedIndex());
                }
            }
        });
        
        TEXTAREA_BUTTONS[0].addActionListener((ActionEvent arg0) -> {
            TREEPANEL.getSelectedPathes().stream().filter((path) -> (!MODEL.contains(path))).forEachOrdered((path) -> {
                MODEL.addElement(path);
            });
        });
        
        TEXTAREA_BUTTONS[1].addActionListener((ActionEvent arg0) -> {
            int[] it = TEXTLIST.getSelectedIndices();
            for (int i = it.length-1; i >= 0; i--){
                MODEL.remove(it[i]);
            }
        });
        
        for (int i = 0; i < CHECKBOX_LINKS.length; i++){
            final int j = i;
            CHECKBOX_LINKS[i].addActionListener((ActionEvent arg0) -> {
                Components.setTricker(j+1, CHECKBOX_LINKS[j].isSelected());
            });
        }
                
        MAIN_BUTTONS[0].addActionListener((ActionEvent arg0) -> {
            try {
                Object[] obj = MODEL.toArray();
                String[] str = new String[obj.length];
                for (int i = 0; i < obj.length; i++){
                    str[i] = obj[i].toString();
                }
                setVisible(false);
                ThreadController.initApplication(str);
            } catch (Exception ex) {}
        });
        
        MAIN_BUTTONS[1].addActionListener((ActionEvent e) -> {
            System.exit(0);
        });
        
    }
    
    /**
     * METHOD: set window in the middle of screen, half of the screen dimansion
     * 
     * @param window as object
     */
    public static void setWindow(Window window) {
        
        final GraphicsConfiguration config = window.getGraphicsConfiguration();

        final int left = Toolkit.getDefaultToolkit().getScreenInsets(config).left;
        final int right = Toolkit.getDefaultToolkit().getScreenInsets(config).right;
        final int top = Toolkit.getDefaultToolkit().getScreenInsets(config).top;
        final int bottom = Toolkit.getDefaultToolkit().getScreenInsets(config).bottom;
        
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        final int width = screenSize.width - left - right;
        final int height = screenSize.height - top - bottom;
        
        window.setBounds(width/3/2, height/3/2, width/3*2, height/3*2);
        
    }
    
    /**
     * METHOD: add path to model
     * 
     * @param path as string
     */
    public static void addModel(String path){
        
        MODEL.addElement(path);
        
    }
    
}
