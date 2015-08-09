import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import javax.swing.filechooser.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.swing.undo.*;
import java.net.URL;
import java.nio.channels.*;

public class TypePro extends JFrame implements KeyListener {
  
  private String programName = "Type Pro - Version 1.2.6"; 
  private String fileName;
  private JFrame frame;
  private JTextArea typeArea;
  private String fileURI = "none";
  private ImageIcon img;
  private String prevText;
  private JPanel statusPanel;
  private Boolean statusBarToggle;
  private JMenuBar menu;
  private JMenu file;
  private JMenuItem open;
  private JMenuItem save;
  private JMenuItem saveAs;
  private JMenuItem print;
  private JMenuItem close;
  private JMenuItem exit;
  private JMenu edit;
  private JMenuItem undo;
  private JMenuItem cut;
  private JMenuItem copy;
  private JMenuItem paste;
  private JMenuItem selectAll;
  private JMenuItem dateAndTime;
  private JMenu format;
  private JMenuItem font;
  private JMenuItem wordWrapping;
  private JMenu view;
  private JMenuItem statusBar;
  private JMenu info;
  private JMenuItem help;
  private JMenuItem update;
  private JMenuItem about;
  private UndoManager undoManager;
  
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
    URL url = this.getClass().getResource("logo.png");
    frame.setIconImage(new ImageIcon(url).getImage());
    frame.setTitle(getTitle());
    frame.setSize(900, 500);
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
    
    typeArea = new JTextArea();
    typeArea.setCaretPosition(typeArea.getDocument().getLength());
    typeArea.setDragEnabled(true);
    typeArea.addKeyListener(this);
    undoManager = new UndoManager();
    typeArea.getDocument().addUndoableEditListener(new UndoableEditListener() {
      @Override
      public void undoableEditHappened(UndoableEditEvent e) {
        
        undoManager.addEdit(e.getEdit());
        
      }
    });
    
    menu = new JMenuBar();
    file = new JMenu("File");
    open = new JMenuItem("Open             \tCtrl + O");
    save = new JMenuItem("Save              \tCtrl + S");
    saveAs = new JMenuItem("Save As         \tCtrl + Q");
    print = new JMenuItem("Print              \tCtrl + P");
    close = new JMenuItem("Close            \tCtrl + N");
    exit = new JMenuItem("Exit");
    edit = new JMenu("Edit");
    undo = new JMenuItem("Undo             \tCtrl + Z");
    cut = new JMenuItem("Cut                \tCtrl + X");
    copy = new JMenuItem("Copy             \tCtrl + C");
    paste = new JMenuItem("Paste             \tCtrl + V");
    selectAll = new JMenuItem("Select All      \tCtrl + A");
    dateAndTime = new JMenuItem("Date/Time    \tF5");
    format = new JMenu("Format");
    font = new JMenuItem("Font");
    wordWrapping = new JCheckBoxMenuItem("Word Wrapping");
    view = new JMenu("View");
    statusBar = new JCheckBoxMenuItem("Status Bar");
    info = new JMenu("Info");
    help = new JMenuItem("Help");
    update = new JMenuItem("Update");
    about = new JMenuItem("About");
    
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
            save(false);
          }
        }
        System.exit(0);
      }
    });
    
    undo.setMnemonic(KeyEvent.VK_O);
    undo.setToolTipText("Undo Last Edit");
    undo.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent event) {
        undo();
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
        save(false);
      }
    });
    
    saveAs.setMnemonic(KeyEvent.VK_S);
    saveAs.setToolTipText("Save File As");
    saveAs.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent event) {
        save(true);
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
        close();
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
        insertDateAndTime();
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
    wordWrapping.setToolTipText("Toggle Word Wrapping");
    wordWrapping.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent event) {
        if (typeArea.getWrapStyleWord() == true) {
          typeArea.setWrapStyleWord(false);
          typeArea.setLineWrap(false);
        } else {
          typeArea.setWrapStyleWord(true);
          typeArea.setLineWrap(true);
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
    
    update.setMnemonic(KeyEvent.VK_U);
    update.setToolTipText("Update TypePro");
    update.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent event) {
        update();
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
    file.add(new JSeparator());
    file.add(print);
    file.add(new JSeparator());
    file.add(close);
    file.add(exit);
    menu.add(file);
    
    edit.add(undo);
    edit.add(new JSeparator());
    edit.add(cut);
    edit.add(copy);
    edit.add(paste);
    edit.add(new JSeparator());
    edit.add(selectAll);
    edit.add(dateAndTime);
    menu.add(edit);
    
    format.add(wordWrapping);
    format.add(font);
    menu.add(format);
    
    view.add(statusBar);
    menu.add(view);
    
    info.add(help);
    info.add(update);
    info.add(new JSeparator());
    info.add(about);
    menu.add(info);
    
    frame.setJMenuBar(menu);
    
    typeArea.addCaretListener(new CaretListener() {
      public void caretUpdate(CaretEvent e) {
        JTextArea editArea = (JTextArea)e.getSource();
        
        int linenum = 1;
        int columnnum = 1;
        
        try {
          int caretpos = editArea.getCaretPosition();
          linenum = editArea.getLineOfOffset(caretpos);
          
          columnnum = caretpos - editArea.getLineStartOffset(linenum);
          
          linenum += 1;
        }
        catch(Exception ex) { }
        
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
            save(false);
          }
        }
        System.exit(0);
      }
    });
    frame.addWindowFocusListener(new WindowAdapter() {
      public void windowGainedFocus(WindowEvent e) {
        typeArea.requestFocusInWindow();
      }
    });


    frame.setVisible(true);
  }
  
  public void save(Boolean override) {
    String name = fileURI;
    Boolean changed = false;
    if (name.equals("none") || override == true) {
      String save = saveAs();
      //System.out.println(save);
      if (save != null) {
        name = save;
        changed = true;
      }
      /*name = JOptionPane.showInputDialog(frame, "Save File As (.txt will be added to end)", "Save", 
                                         JOptionPane.INFORMATION_MESSAGE);
      name = name + ".txt";*/
      frame.setTitle(getTitle());
    } 
    if (!name.equals("none")) {
      try {
        File f = new File(name);
        if(!f.exists() && !f.isDirectory()) { 
          f.createNewFile();
        } else if (changed == true && !sameText()) {
          Object[] options = { "Yes", "No" };
          int selected = JOptionPane.showOptionDialog(frame, "This file already exists.\nDo you wish to override it?", 
                                                      "Save", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, 
                                                      null, options, options[0]);
          if (selected == 1) {
            return;
          }
        }
        BufferedWriter out = new BufferedWriter(new FileWriter(name));
        typeArea.write(out);
        out.close();
      } catch (Exception e) { 
        JOptionPane.showMessageDialog(frame, "Error: \n" + e, "Error", JOptionPane.ERROR_MESSAGE);
      }
      fileURI = name;
      prevText = typeArea.getText();
    }
  }
  
  public void open() {
    JFileChooser chooser = new JFileChooser();
    if (!typeArea.getText().equals("") && typeArea.getText() != null)
      save(false);
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
          typeArea.append(line);
          line = in.readLine();
          if (line != null)
            typeArea.append("\n");
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
      fileName = chooser.getSelectedFile().getName();
      return chooser.getSelectedFile().getAbsolutePath();
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
  
  public void undo() {
    try {
      if (undoManager.canUndo()) {
        undoManager.undo();
      }
    } catch (CannotUndoException exp) {
      exp.printStackTrace();
    }
  }
  
  public void redo() {
    try {
      if (undoManager.canRedo()) {
        undoManager.redo();
      }
    } catch (CannotUndoException exp) {
      exp.printStackTrace();
    }
  }
  
  public void close() {
    if (typeArea.getText() != null && !typeArea.getText().equals("")) {
      Object[] options = { "Yes", "No" };
      int selected = JOptionPane.showOptionDialog(frame, "Do you wish to save?", "Save", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
      if (selected == 0) {
        save(false);
      }
    }
    fileURI = "none";
    typeArea.setText("");
    prevText = null;
    frame.setTitle(getTitle());
  }
  
  public void insertDateAndTime() {
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    Date date = new Date();
    typeArea.insert(dateFormat.format(date), typeArea.getCaret().getMark());
  }
  
  public void keyReleased(KeyEvent e) {
    //ignore
  }
  
  public void keyPressed(KeyEvent e) {
    System.out.println("KeyPressed("+e.getKeyCode()+") called! Control/Meta Down == " + (e.isControlDown() || e.isMetaDown()));
    if (e.getKeyCode() == KeyEvent.VK_Z && (e.isControlDown() || e.isMetaDown())) {
      undo();
    } else if (e.getKeyCode() == KeyEvent.VK_Y && (e.isControlDown() || e.isMetaDown())) {
      redo();
    } else if (e.getKeyCode() == KeyEvent.VK_N && (e.isControlDown() || e.isMetaDown())) {
      close();
    } else if (e.getKeyCode() == KeyEvent.VK_O && (e.isControlDown() || e.isMetaDown())) {
      open();
    } else if (e.getKeyCode() == KeyEvent.VK_S && (e.isControlDown() || e.isMetaDown())) {
      save(false);
    } else if (e.getKeyCode() == KeyEvent.VK_Q && (e.isControlDown() || e.isMetaDown())) {
      save(true);
    } else if (e.getKeyCode() == KeyEvent.VK_P && (e.isControlDown() || e.isMetaDown())) {
      try {
        typeArea.print();
      } catch (Exception ex) { 
        JOptionPane.showMessageDialog(frame, "Error: /n" + e, "Error", JOptionPane.ERROR_MESSAGE);
      }
    } else if (e.getKeyCode() == KeyEvent.VK_F5) {
      insertDateAndTime();
    }
  } 
  
  public void keyTyped(KeyEvent e) {
    //ignore
  }
  
  public void update() {
    try {
      final File currentFile = new File(TypePro.class.getProtectionDomain().getCodeSource().getLocation().toURI());
      URL website = null;
      if (currentFile.getName().endsWith(".exe")) {
        website = new URL("https://github.com/PandemicMoon/TypePro/blob/master/typepro.exe?raw=true");
      } else if (currentFile.getName().endsWith(".jar")) {
        website = new URL("https://github.com/PandemicMoon/TypePro/blob/master/typepro.jar?raw=true");
      }
      ReadableByteChannel rbc = Channels.newChannel(website.openStream());
      FileOutputStream fos = new FileOutputStream(TypePro.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
      fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
    } catch (Exception e) {
      JOptionPane.showMessageDialog(frame, "Error: \n" + e, "Error", JOptionPane.ERROR_MESSAGE);
    }
    try {
      restart();
    } catch (Exception e) {
      JOptionPane.showMessageDialog(frame, "Failed to restart. \nError: \n" + e, "Error", JOptionPane.ERROR_MESSAGE);
    }
  }
  
  public void restart() throws Exception
  {
    final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
    final File currentFile = new File(TypePro.class.getProtectionDomain().getCodeSource().getLocation().toURI());
    
    /* Build command: java -jar application.jar OR java -cp application.exe TypePro */
    final ArrayList<String> command = new ArrayList<String>();
    
    /* is it a jar OR exe file? */
    if (currentFile.getName().endsWith(".jar")) {
      command.add(javaBin);
      command.add("-jar");
      command.add(currentFile.getPath());
    } else {
      command.add(javaBin);
      command.add("-cp");
      command.add(currentFile.getPath());
      command.add("TypePro");
    }
    final ProcessBuilder builder = new ProcessBuilder(command);
    builder.start();
    System.exit(0);
  }
  
}