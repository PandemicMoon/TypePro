import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import javax.swing.filechooser.*;
import javax.swing.border.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TypePro extends JFrame {
  
  String programName = "Type Pro - Version 1.10"; 
  String fileName;
  JFrame frame;
  JTextArea typeArea;
  String fileURI = "none";
  ImageIcon img;
  String prevText;
  JPanel statusPanel;
  Boolean statusBarToggle;
  
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
    statusBarToggle = false;
    
    statusPanel = new JPanel();
    statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
    frame.add(statusPanel, BorderLayout.SOUTH);
    statusPanel.setPreferredSize(new Dimension(frame.getWidth(), 16));
    statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
    JLabel statusLabel = new JLabel();
    statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
    statusPanel.add(statusLabel);
    frame.add(statusPanel, BorderLayout.SOUTH);
    
    frame.remove(statusPanel);
    statusLabel.setText("Line: 1, Column: 0");
    
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
    JMenuItem dateAndTime = new JMenuItem("Date/Time");
    JMenu format = new JMenu("Format");
    JMenuItem font = new JMenuItem("Font");
    JMenuItem wordWrapping = new JMenuItem("Word Wrapping");
    JMenu view = new JMenu("View");
    JMenuItem statusBar = new JMenuItem("Status Bar");
    JMenu info = new JMenu("Info");
    JMenuItem help = new JMenuItem("Help");
    JMenuItem about = new JMenuItem("About");
    
    exit.setMnemonic(KeyEvent.VK_E);
    exit.setToolTipText("Exit Type Pro");
    exit.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent event) {
        if (typeArea.getText() != null && !typeArea.getText().equals("") && !sameText()) {
          Object[] options = { "Yes", "No" };
          int selected = JOptionPane.showOptionDialog(frame, "Do you wish to save?", "Save", 
                                                      JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, 
                                                      null, options, options[0]);
          if (selected == 0) {
            save();
          }
        }
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
        if (typeArea.getText() != null && !typeArea.getText().equals("")) {
          Object[] options = { "Yes", "No" };
          int selected = JOptionPane.showOptionDialog(frame, "Do you wish to save?", "Save", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
          if (selected == 0) {
            save();
          }
        }
        fileURI = "none";
        typeArea.setText("");
        prevText = null;
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
    
    dateAndTime.setMnemonic(KeyEvent.VK_S);
    dateAndTime.setToolTipText("Insert Date And Time");
    dateAndTime.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent event) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        typeArea.insert(dateFormat.format(date), typeArea.getCaret().getMark());
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
        } else {
          typeArea.setWrapStyleWord(true);
        }
      }
    });
    
    statusBar.setMnemonic(KeyEvent.VK_S);
    statusBar.setToolTipText("Toggle Status Bar");
    statusBar.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent event) {
        if (statusBarToggle) {
          frame.remove(statusPanel);
          statusBarToggle = false;
          frame.setVisible(true);
        } else {
          frame.add(statusPanel, BorderLayout.SOUTH);
          statusBarToggle = true;
          frame.setVisible(true);
        }
      }
    });
    
    help.setMnemonic(KeyEvent.VK_H);
    help.setToolTipText("Read Help");
    help.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent event) {
        JOptionPane.showMessageDialog(frame, "Help Center coming soon.", "Type Pro Help", 
                                      JOptionPane.QUESTION_MESSAGE/*, img*/);
      }
    });
    
    about.setMnemonic(KeyEvent.VK_A);
    about.setToolTipText("About Type Pro");
    about.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent event) {
        JOptionPane.showMessageDialog(frame, "A simple text file editor." + 
                                      "\nHandcoded By Pandemic Moon.", "About Type Pro", 
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
    edit.add(dateAndTime);
    menu.add(edit);
    
    format.add(wordWrapping);
    format.add(font);
    menu.add(format);
    
    view.add(statusBar);
    menu.add(view);
    
    info.add(help);
    info.add(about);
    menu.add(info);
    
    frame.setJMenuBar(menu);
    
    typeArea = new JTextArea();
    typeArea.setCaretPosition(typeArea.getDocument().getLength());
    typeArea.setDragEnabled(true);
    
    // Add a caretListener to q7+40 typeArea.
    typeArea.addCaretListener(new CaretListener() {
      // Each time the caret is moved, it will trigger the listener and its method caretUpdate.
      // It will then pass the event to the update method including the source of the event (which is our textarea control)
      public void caretUpdate(CaretEvent e) {
        JTextArea editArea = (JTextArea)e.getSource();
        
        // Lets start with some default values for the line and column.
        int linenum = 1;
        int columnnum = 1;
        
        // We create a try catch to catch any exceptions. We will simply ignore such an error for our demonstration.
        try {
          // First we find the position of the caret. This is the number of where the caret is in relation to the start of the JTextArea
          // in the upper left corner. We use this position to find offset values (eg what line we are on for the given position as well as
          // what position that line starts on.
          int caretpos = editArea.getCaretPosition();
          linenum = editArea.getLineOfOffset(caretpos);
          
          // We subtract the offset of where our line starts from the overall caret position.
          // So lets say that we are on line 5 and that line starts at caret position 100, if our caret position is currently 106
          // we know that we must be on column 6 of line 5.
          columnnum = caretpos - editArea.getLineStartOffset(linenum);
          
          // We have to add one here because line numbers start at 0 for getLineOfOffset and we want it to start at 1 for display.
          linenum += 1;
        }
        catch(Exception ex) { }
        
        // Once we know the position of the line and the column, pass it to a helper function for updating the status bar.
        statusLabel.setText("Line: " + linenum + ", Column: " + columnnum);
      }
    });


    JScrollPane scrollPane = new JScrollPane(typeArea,
    JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
    JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    
    frame.add(scrollPane);
    frame.addWindowListener(new WindowAdapter() {
      
      public void windowClosing(WindowEvent e) {
        if (typeArea.getText() != null && !typeArea.getText().equals("") && !sameText()) {
          Object[] options = { "Yes", "No" };
          int selected = JOptionPane.showOptionDialog(frame, "Do you wish to save?", "Save", 
                                                    JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, 
                                                    null, options, options[0]);
          if (selected == 0) {
            save();
          }
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
      prevText = typeArea.getText();
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
    prevText = typeArea.getText();
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
    if (fileURI == null || fileURI.equals("none"))
      return programName;
    else
      return fileName + " - " + programName;
  }
  
  public Boolean sameText() {
    if (prevText == null)
      return false;
    else if (prevText.equals(typeArea.getText()))
      return true;
    else
      return false;
  }
}