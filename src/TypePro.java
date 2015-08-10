import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import javax.swing.filechooser.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.util.*;
import java.text.*;
import javax.swing.undo.*;
import java.net.*;
import java.nio.channels.*;

public class TypePro extends JFrame implements KeyListener {
  
  /* define fields */
  private String version = "1.3.1";
  private String programName = "Type Pro - Version " + version; 
  
  /* visual fields */
  private JFrame frame;
  private JTextArea typeArea;
  private ImageIcon img;
  private JPanel statusPanel;
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
  
  /* function fields */
  private UndoManager undoManager;
  private String fileName;
  private String prevText;
  private Boolean statusBarToggle;
  private String fileURI = "none";
  
  
  public TypePro(String[] args) {
    //set look and feel to that of native os
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) { 
      JOptionPane.showMessageDialog(frame, "Setting look and feel failed. Error: \n" + e, "Error", JOptionPane.ERROR_MESSAGE); //present caught errors
    }
    /*build the frame*/
    initFrame();
    //open told to open file in args, open it
    if (args.length > 0) {
      open(args[0]);
    }
    //check for updates
    try {
      checkForUpdate();
    } catch (Exception e) { 
      JOptionPane.showMessageDialog(frame, "Checking for update failed. Error: \n" + e, "Error", JOptionPane.ERROR_MESSAGE); //present caught errors
    }
  }
  
  public void initFrame() {
    //make a new JFrame
    frame = new JFrame();
    //get logo url and set logo
    URL url = this.getClass().getResource("logo.png");
    frame.setIconImage(new ImageIcon(url).getImage());
    //set the frame title and size
    frame.setTitle(getTitle());
    frame.setSize(900, 500);
    frame.setExtendedState(Frame.MAXIMIZED_BOTH);
    //prevent closing without action
    frame.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    
    //initialize statusbar
    statusBarToggle = false;
    statusPanel = new JPanel();
    statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
    frame.add(statusPanel, BorderLayout.SOUTH);
    statusPanel.setPreferredSize(new Dimension(frame.getWidth(), 16));
    statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
    JLabel statusLabel = new JLabel();
    statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
    statusPanel.add(statusLabel);
    statusLabel.setText("Line: 1, Column: 0");
    
    //initialize type area
    typeArea = new JTextArea();
    typeArea.setCaretPosition(typeArea.getDocument().getLength());
    typeArea.setDragEnabled(true);
    typeArea.addKeyListener(this);
    
    //initialize and setup undoManager
    undoManager = new UndoManager();
    typeArea.getDocument().addUndoableEditListener(new UndoableEditListener() {
      @Override
      public void undoableEditHappened(UndoableEditEvent e) {
        
        undoManager.addEdit(e.getEdit());
        
      }
    });
    
    //initialize menubar components
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
    
    //add actions for each menu item
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
          JOptionPane.showMessageDialog(frame, "Printing failed. Error: \n" + e, "Error", JOptionPane.ERROR_MESSAGE);
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
    help.setToolTipText("Get Help");
    help.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent event) {
        try {
          openWebpage(new URL("https://github.com/PandemicMoon/TypePro/issues"));
        } catch (Exception e) { 
          JOptionPane.showMessageDialog(frame, "Opening help page failed. Error: \n" + e, "Error", JOptionPane.ERROR_MESSAGE);
        }
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
    
    //add each menu item to the menu, then add that menu to the bar
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
    
    //add the menu bar to the frame
    frame.setJMenuBar(menu);
    
    //find and pass the position of the caret to the statusbar
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
    
    //initialize scrollpane
    JScrollPane scrollPane = new JScrollPane(typeArea,
    JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
    JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    
    //add scrollpane
    frame.add(scrollPane);
    //add closing event
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
    
    //show the frame now that's it's been built
    frame.setVisible(true);
  }
  
  public void checkForUpdate() throws Exception {
    //get latest version
    URL file = new URL("https://raw.githubusercontent.com/PandemicMoon/TypePro/master/src/TypePro.java");
    BufferedReader in = new BufferedReader(new InputStreamReader(file.openStream()));
    
    String inputLine;
    String versionLine = null;
    while ((inputLine = in.readLine()) != null && versionLine == null) {
      if (inputLine.contains("private String version ="))
        versionLine = inputLine;
    }
    in.close();
    //check if latest update is newer
    if (versionLine != null) {
      versionLine = versionLine.substring(versionLine.indexOf("\"")+1);
      String ver = versionLine.substring(0, versionLine.indexOf("\""));
      if (isNewerVersion(ver))
        update(); //it is, update
    } else {
      JOptionPane.showMessageDialog(frame, "Checking for update failed. Could not check latest version number.", "Error", JOptionPane.ERROR_MESSAGE); //present caught errors
    }
  }
  
  //save file
  public void save(Boolean override) {
    String name = fileURI;
    Boolean changed = false;
    //ask user where to save if no location found
    if (name.equals("none") || override == true) {
      String save = saveAs();
      if (save != null) {
        name = save;
        changed = true;
      }
      //update title
      frame.setTitle(getTitle());
    } 
    //once again check if location is found
    if (!name.equals("none")) {
      try {
        File f = new File(name);
        if(!f.exists() && !f.isDirectory()) { //if file doesn't exist, make it
          f.createNewFile();
        } else if (changed == true && !sameText()) { //warn user file exists
          Object[] options = { "Yes", "No" };
          int selected = JOptionPane.showOptionDialog(frame, "This file already exists.\nDo you wish to override it?", 
                                                      "Save", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, 
                                                      null, options, options[0]);
          if (selected == 1) { //if no selected, exit method
            return;
          }
        }
        //actual saving
        BufferedWriter out = new BufferedWriter(new FileWriter(name));
        typeArea.write(out);
        out.close();
      } catch (Exception e) { 
        JOptionPane.showMessageDialog(frame, "Saving failed. Error: \n" + e, "Error", JOptionPane.ERROR_MESSAGE); //present caught errors
      }
      //update fileURI and prevtext
      fileURI = name;
      prevText = typeArea.getText();
    }
  }
  
  //open file
  public void open() {
    //save if needed
    if (!typeArea.getText().equals("") && typeArea.getText() != null)
      save(false);
    //show file chooser
    JFileChooser chooser = new JFileChooser();
    FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
    chooser.setFileFilter(filter);
    int choosen = chooser.showOpenDialog(frame);
    if (chooser.getSelectedFile() != null && choosen == 0) { //make sure file was chosen
      open(chooser.getSelectedFile().getAbsolutePath());
    }
  }
  
  public void open(String fileloc) {
    //clear type area
    typeArea.setText("");
    File f = new File(fileloc);
    fileURI = fileloc;
    fileName = f.getName();
    try {
      //actual file opening
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
      JOptionPane.showMessageDialog(frame, "Opening failed. Error: \n" + e, "Error", JOptionPane.ERROR_MESSAGE); //present caught errors
    }
    //update title
    frame.setTitle(getTitle());
    prevText = typeArea.getText();
  }
  
  //save as
  public String saveAs() {
    //show chooser
    JFileChooser chooser = new JFileChooser();
    FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
    chooser.setFileFilter(filter);
    int returnVal = chooser.showSaveDialog(frame);
    if(returnVal == JFileChooser.APPROVE_OPTION) { //make sure file was chosen
      fileName = chooser.getSelectedFile().getName();
      return chooser.getSelectedFile().getAbsolutePath(); //return file
    } else
      return null; //return that file wasn't chosen
  }
  
  public static void main(String[] args) {
    TypePro tPro = new TypePro(args); //launch TypePro
  }
  
  public String getTitle() {
    //update title
    if (fileURI == null || fileURI.equals("none")) //not editing file
      return programName;
    else //editing file
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
  
  //undo
  public void undo() {
    try {
      if (undoManager.canUndo()) {
        undoManager.undo();
      }
    } catch (CannotUndoException exp) {
      JOptionPane.showMessageDialog(frame, "Undo failed. Error: \n" + exp, "Error", JOptionPane.ERROR_MESSAGE); //present caught errors
    }
  }
  
  //redo
  public void redo() {
    try {
      if (undoManager.canRedo()) {
        undoManager.redo();
      }
    } catch (CannotUndoException exp) {
      JOptionPane.showMessageDialog(frame, "Redo failed. Error: \n" + exp, "Error", JOptionPane.ERROR_MESSAGE); //present caught errors
    }
  }
  
  //close file
  public void close() {
    if (typeArea.getText() != null && !typeArea.getText().equals("")) { //ask to save
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
    //get date
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    Date date = new Date();
    //insert date
    typeArea.insert(dateFormat.format(date), typeArea.getCaret().getMark());
  }
  
  public void keyReleased(KeyEvent e) {
    //ignore
  }
  
  public void keyPressed(KeyEvent e) {
    //for debugging
    //System.out.println("KeyPressed("+e.getKeyCode()+") called! Control/Meta Down == " + (e.isControlDown() || e.isMetaDown()));
    //add keyboard shortcuts
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
        JOptionPane.showMessageDialog(frame, "Printing failed. Error: /n" + e, "Error", JOptionPane.ERROR_MESSAGE); //present caught errors
      }
    } else if (e.getKeyCode() == KeyEvent.VK_F5) {
      insertDateAndTime();
    }
  } 
  
  public void keyTyped(KeyEvent e) {
    //ignore
  }
  
  //update
  public void update() {
    try {
      //get file location to update
      final File currentFile = new File(TypePro.class.getProtectionDomain().getCodeSource().getLocation().toURI());
      URL website = null;
      //replace (update) that file
      if (currentFile.getName().endsWith(".exe")) {
        website = new URL("https://github.com/PandemicMoon/TypePro/blob/master/typepro.exe?raw=true");
      } else if (currentFile.getName().endsWith(".jar")) {
        website = new URL("https://github.com/PandemicMoon/TypePro/blob/master/typepro.jar?raw=true");
      }
      ReadableByteChannel rbc = Channels.newChannel(website.openStream());
      FileOutputStream fos = new FileOutputStream(TypePro.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
      fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
    } catch (Exception e) {
      JOptionPane.showMessageDialog(frame, "Update failed. Error: \n" + e, "Error", JOptionPane.ERROR_MESSAGE); //present caught errors
    }
    try {
      restart();
    } catch (Exception e) {
      JOptionPane.showMessageDialog(frame, "Failed to restart. \nError: \n" + e, "Error", JOptionPane.ERROR_MESSAGE); //present caught errors
    }
  }
  
  public void restart() throws Exception
  {
    //get javaBin and location of file to restart
    final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
    final File currentFile = new File(TypePro.class.getProtectionDomain().getCodeSource().getLocation().toURI());
    
    //build command: java -jar application.jar [file] OR java -cp application.exe TypePro [file]
    final ArrayList<String> command = new ArrayList<String>();
    
    //add specific file type command
    if (currentFile.getName().endsWith(".jar")) {
      command.add(javaBin);
      command.add("-jar");
      command.add(currentFile.getPath());
      if (fileURI != null && fileURI != "")
        command.add(fileURI);
    } else {
      command.add(javaBin);
      command.add("-cp");
      command.add(currentFile.getPath());
      command.add("TypePro");
      if (fileURI != null && fileURI != "")
        command.add(fileURI);
    }
    //build commands and exit
    final ProcessBuilder builder = new ProcessBuilder(command);
    builder.start();
    System.exit(0);
  }
  
  public static void openWebpage(URI uri) {
    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
      try {
        desktop.browse(uri);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
  
  public static void openWebpage(URL url) {
    try {
      openWebpage(url.toURI());
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
  }
  
  public boolean isNewerVersion(String ver) {
    //remove dots (1.0.0 --> 100)
    for (int i = 0; i < version.length(); i++) {
      if (version.substring(i, i+1).equals("."))
        version = version.substring(0, i) + version.substring(i+1);
    }
    for (int i = 0; i < ver.length(); i++) {
      if (ver.substring(i, i+1).equals("."))
        ver = ver.substring(0, i) + ver.substring(i+1);
    }
    //convert strings to ints
    int versionint = Integer.parseInt(version);
    int verint = Integer.parseInt(ver);
    //compare versions
    return verint > versionint;
  }
  
}