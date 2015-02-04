import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import javax.swing.filechooser.*;

public class TypePro extends JFrame {
  
  String programName = "Type Pro - Version 1.7"; 
  String fileName;
  JFrame frame;
  JTextArea typeArea;
  String fileURI = "none";
  ImageIcon img;
  
  public TypePro() {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) { 
      JOptionPane.showMessageDialog(frame, "Error: /n" + e, "Error", JOptionPane.ERROR_MESSAGE);
    }
    initFile();
  }
  
  public void initFile() {
    frame = new JFrame();
    img = new ImageIcon("logo.png");
    frame.setIconImage(img.getImage());
    frame.setTitle(getTitle());
    frame.setExtendedState(Frame.MAXIMIZED_BOTH);
    frame.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    
    JMenuBar menu = new JMenuBar();
    JMenu file = new JMenu("File");
    JMenuItem open = new JMenuItem("Open");
    JMenuItem save = new JMenuItem("Save");
    JMenuItem saveAs = new JMenuItem("Save As");
    JMenuItem print = new JMenuItem("Print");
    JMenuItem close = new JMenuItem("Close");
    JMenuItem exit = new JMenuItem("Exit");
    JMenu edit = new JMenu("Edit");
    JMenuItem cut = new JMenuItem("Cut");
    JMenuItem copy = new JMenuItem("Copy");
    JMenuItem paste = new JMenuItem("Paste");
    JMenuItem selectAll = new JMenuItem("Select All");
    JMenu format = new JMenu("Format");
    JMenuItem font = new JMenuItem("Font");
    JMenuItem wordWrapping = new JMenuItem("Word Wrapping");
    JMenu info = new JMenu("Info");
    JMenuItem help = new JMenuItem("Help");
    JMenuItem about = new JMenuItem("About");
    
    exit.setMnemonic(KeyEvent.VK_E);
    exit.setToolTipText("Exit Type Pro");
    exit.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent event) {
        System.exit(0);
      }
    });
    
    open.setMnemonic(KeyEvent.VK_O);
    open.setToolTipText("Open a File");
    open.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent event) {
        open();
      }
    });
    
    save.setMnemonic(KeyEvent.VK_S);
    save.setToolTipText("Save File");
    save.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent event) {
        save();
      }
    });
    
    saveAs.setMnemonic(KeyEvent.VK_S);
    saveAs.setToolTipText("Save File As");
    saveAs.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent event) {
        save();
      }
    });
    
    print.setMnemonic(KeyEvent.VK_P);
    print.setToolTipText("Print File");
    print.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent event) {
        try {
          typeArea.print();
        } catch (Exception e) { 
          JOptionPane.showMessageDialog(frame, "Error: /n" + e, "Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    });
    
    close.setMnemonic(KeyEvent.VK_C);
    close.setToolTipText("Close File");
    close.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent event) {
        if (!typeArea.getText().equals("") && typeArea.getText() != null) {
          Object[] options = { "Yes", "No" };
          int selected = JOptionPane.showOptionDialog(frame, "Do you wish to save?", "Save", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
          if (selected == 0) {
            save();
          }
        }
        fileURI = "none";
        typeArea.setText("");
        frame.setTitle(getTitle());
      }
    });
    
    cut.setMnemonic(KeyEvent.VK_C);
    cut.setToolTipText("Cut Selected Test");
    cut.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent event) {
        typeArea.cut();
      }
    });
    
    copy.setMnemonic(KeyEvent.VK_C);
    copy.setToolTipText("Copy Selected Text");
    copy.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent event) {
        typeArea.copy();
      }
    });
    
    paste.setMnemonic(KeyEvent.VK_P);
    paste.setToolTipText("Paste Text");
    paste.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent event) {
        typeArea.paste();
      }
    });
    
    selectAll.setMnemonic(KeyEvent.VK_S);
    selectAll.setToolTipText("Select All");
    selectAll.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent event) {
        typeArea.selectAll();
      }
    });
    
    font.setMnemonic(KeyEvent.VK_F);
    font.setToolTipText("Select Font");
    font.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent event) {
        JFontChooser fontChooser = new JFontChooser();
        int result = fontChooser.showDialog(frame);
        if (result == JFontChooser.OK_OPTION)
        {
          Font font = fontChooser.getSelectedFont(); 
          typeArea.setFont(font);
        }
      }
    });
    
    wordWrapping.setMnemonic(KeyEvent.VK_W);
    wordWrapping.setToolTipText("Enable/Disable Word Wrapping");
    wordWrapping.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent event) {
        if (typeArea.getWrapStyleWord() == true) {
          typeArea.setWrapStyleWord(false);
          System.out.println("Set Word Wrapping to " + typeArea.getWrapStyleWord() + "(supposed to be false).");
        } else {
          typeArea.setWrapStyleWord(true);
          System.out.println("Set Word Wrapping to " + typeArea.getWrapStyleWord() + "(supposed to be true).");
        }
      }
    });
    
    help.setMnemonic(KeyEvent.VK_H);
    help.setToolTipText("Read Help");
    help.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent event) {
        
      }
    });
    
    about.setMnemonic(KeyEvent.VK_A);
    about.setToolTipText("About Type Pro");
    about.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent event) {
        JOptionPane.showMessageDialog(frame, "Your text file editing just got easier." + 
                                      "\nMade By Pandemic Moon." + 
                                      "\nHandcoded to perfection.", "About Type Pro", 
                                      JOptionPane.INFORMATION_MESSAGE, img);
      }
    });
    
    file.add(open);
    file.add(saveAs);
    file.add(save);
    file.add(print);
    file.add(close);
    file.add(exit);
    menu.add(file);
    
    edit.add(cut);
    edit.add(copy);
    edit.add(paste);
    edit.add(selectAll);
    menu.add(edit);
    
    format.add(wordWrapping);
    format.add(font);
    menu.add(format);
    
    info.add(help);
    info.add(about);
    menu.add(info);
    
    frame.setJMenuBar(menu);
    
    typeArea = new JTextArea();
    typeArea.setCaretPosition(typeArea.getDocument().getLength());
    typeArea.setDragEnabled(true);
    
    JScrollPane scrollPane = new JScrollPane(typeArea,
    JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
    JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    
    frame.add(scrollPane);
    frame.addWindowListener(new WindowAdapter() {
      
      public void windowClosing(WindowEvent e) {
        Object[] options = { "Yes", "No" };
        int selected = JOptionPane.showOptionDialog(frame, "Do you wish to save?", "Save", 
                                                    JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, 
                                                    null, options, options[0]);
        if (selected == 0) {
          save();
        }
        System.exit(0);
      }
    });
    frame.setVisible(true);
  }
  
  public void save() {
    String name = fileURI;
    if (fileURI.equals("none")) {
      String save = saveAs();
      if (save != null)
        name = save;
      /*name = JOptionPane.showInputDialog(frame, "Save File As (.txt will be added to end)", "Save", 
                                         JOptionPane.INFORMATION_MESSAGE);
      name = name + ".txt";*/
      frame.setTitle(getTitle());
    }
    if (!fileURI.equals("none")) {
      try {
        BufferedWriter out = new BufferedWriter(new FileWriter(name));
        typeArea.write(out);
        out.close();
      } catch (Exception e) { 
        JOptionPane.showMessageDialog(frame, "Error: /n" + e, "Error", JOptionPane.ERROR_MESSAGE);
      }
      fileURI = name;
    }
  }
  
  public void open() {
    JFileChooser chooser = new JFileChooser();
    if (!typeArea.getText().equals("") && typeArea.getText() != null)
      save();
    typeArea.setText("");
    FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
    chooser.setFileFilter(filter);
    int returnVal = chooser.showOpenDialog(frame);
    if(returnVal == JFileChooser.APPROVE_OPTION) {
       System.out.println("You chose to open this file: " +
            chooser.getSelectedFile().getName());
    }
    /*String name = JOptionPane.showInputDialog(frame, "Open File (.txt will be added to end)", "Open", 
                                       JOptionPane.INFORMATION_MESSAGE);*/
    if (chooser.getSelectedFile() != null) {
      fileURI = chooser.getSelectedFile().getAbsolutePath();
      fileName = chooser.getSelectedFile().getName();
      try {
        BufferedReader in = new BufferedReader(new FileReader(fileURI));
        String line = in.readLine();
        while (line != null)
        {
          typeArea.append(line + "\n");
          line = in.readLine();
        }
        in.close();
      } catch (Exception e) { 
        JOptionPane.showMessageDialog(frame, "Error: \n" + e, "Error", JOptionPane.ERROR_MESSAGE);
      }
      frame.setTitle(getTitle());
    }
  }
  
  public String saveAs() {
    JFileChooser chooser = new JFileChooser();
    FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
    chooser.setFileFilter(filter);
    int returnVal = chooser.showSaveDialog(frame);
    if(returnVal == JFileChooser.APPROVE_OPTION) {
      System.out.println("You chose to save this file as: " + chooser.getSelectedFile().getName());
      if (chooser.getSelectedFile().getAbsolutePath().contains(".txt")) {
        fileName = chooser.getSelectedFile().getName();
        return chooser.getSelectedFile().getAbsolutePath();
      } else {
        fileName = chooser.getSelectedFile().getName() + ".txt";
        return chooser.getSelectedFile().getAbsolutePath() + ".txt";
      }
    } else
      return null;
  }
  
  public static void main(String[] args) {
    TypePro tPro = new TypePro();
  }
  
  public String getTitle() {
    if (fileURI.equals("none") || fileURI == null)
      return programName;
    else
      return fileName + " - " + programName;
  }
}