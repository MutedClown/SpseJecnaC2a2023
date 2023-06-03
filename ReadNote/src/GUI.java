import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;

import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;

import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Color;
import java.awt.Container;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class GUI {
//DOBAVIT KNOPKU LOAD IZ VIDOSA BROCODE TEXTEDITOR
	
	//declaration 
	private File currentFile;
	private JFrame frame;
	private JList<Note> noteList;
	private JTextField textFld;
	private JTextArea textArea;	
	private JButton newButton;
	private JButton deleteButton;
	private JButton saveButton;
	private JButton loadButton;
	private JButton cancelButton;
	private boolean updateFlag;
	
	//font
	private static final Font FONT_FOR_WIDGETS =
			new Font("SansSerif", Font.PLAIN, 20);
	private static final Font FONT_FOR_EDITOR =
			new Font("Comic Sans MS", Font.PLAIN, 20);

	//main frame
	public GUI() {
	
		frame = new JFrame("ReadNote"); // frame
		addComponentsToFrame(frame); //adding components to frame

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation(350, 50); // x, y
		frame.setResizable(false);
		frame.pack();	//frame size made on components
		frame.setVisible(true);
		
		initializeDataAndUpdateUI(); 
	}

	
	//adding components to container and their location
	private void addComponentsToFrame(JFrame frame) {
	
		Container pane = frame.getContentPane();
		pane.setLayout(new GridBagLayout());
	
		pane.add(getNoteListInScrollPane(), getConstraintsForNoteList());		
		pane.add(getNameTextField(), getConstraintsForNameTextField());		
		pane.add(getTextAreaInScrollPane(), getConstraintsForTextArea());	
		pane.add(getToolBarWithButtons(), getConstraintsForButtonToolBar());
	
	}
	
	//update UI
	private void initializeDataAndUpdateUI() {
	
		noteList.setModel(new NoteListModel(new ArrayList<>()));			
		deleteButton.setEnabled(false);	
	
	}
	
	//noteList of notes
	private JScrollPane getNoteListInScrollPane() {
	
		noteList = new JList<Note>(); //notes array
		noteList.setFixedCellHeight(35); //height of a single note
		noteList.setFont(FONT_FOR_WIDGETS);	
		noteList.setBorder(new EmptyBorder(5, 5, 5, 5));//same as setMargin()
		noteList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //only 1 note can be chosen
		noteList.addListSelectionListener(new ListSelectListener());
		JScrollPane scroller = new JScrollPane(noteList);
		scroller.setPreferredSize(new Dimension(250, 350)); // wxh
		scroller.setVerticalScrollBarPolicy(
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		return scroller;
	}
	
	//name for a note
	private JTextField getNameTextField() {
	
		textFld = new JTextField("", 25); //size
		textFld.setFont(FONT_FOR_WIDGETS);
		textFld.setToolTipText("Note Name");
		textFld.setMargin(new Insets(5, 5, 5, 5)); 
		textFld.setEditable(false); //cant be edited 
		return textFld;
	}
	
	//text area
	private JScrollPane getTextAreaInScrollPane() {
	
		textArea = new JTextArea("");
		textArea.setToolTipText("Note text");
		textArea.setFont(FONT_FOR_EDITOR);
		textArea.setEditable(false);//cant be edited
		textArea.setLineWrap(true); //автомат. перенос текста
		textArea.setWrapStyleWord(true);//перенос с окончанием слов
		textArea.setMargin(new Insets(5, 5, 5, 5)); 

		JScrollPane scroller = new JScrollPane(textArea);//adding textarea to scrollpane
		scroller.setPreferredSize(new Dimension(550,0));//scroller size
		
		return scroller;
	}
	
	
	//creating icons for buttons
	ImageIcon n3w = new ImageIcon("New24.gif");
	ImageIcon s4ve  = new ImageIcon("Save24.gif");
	ImageIcon d3lete = new ImageIcon("Delete24.gif");
	ImageIcon c4ncel = new ImageIcon("Undo24.gif");
	
	//creating & adding buttons to JToolBar & setting icons
	private JToolBar getToolBarWithButtons() {

		newButton = getButton("New");	
		newButton.setIcon(n3w);
		
		saveButton = getButton("Save");	
		saveButton.setIcon(s4ve);
		
		deleteButton = getButton("Delete");
		deleteButton.setIcon(d3lete);
		
		cancelButton = getButton("Cancel");
		cancelButton.setIcon(c4ncel);
		
		
		JToolBar toolBar = getToolBarForButtons(); 
		toolBar.add(newButton);
		toolBar.addSeparator(new Dimension(2, 0));
		toolBar.add(saveButton);
		toolBar.addSeparator(new Dimension(2, 0));
		toolBar.add(deleteButton);
		toolBar.addSeparator(new Dimension(2, 0));		
		toolBar.add(cancelButton);			
		
		return toolBar;
	}
	
	//setting toolbar without buttons
	private JToolBar getToolBarForButtons() {
	
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);//cant be moved by user
		return toolBar;
	}
	
	//button switch
	private JButton getButton(String label) {
	
		JButton button = new JButton();
		button.setBorderPainted(false);//no border for buttons
		
		switch (label) {//clicking on a button -> actionlistener for each button
			case "New":
				button.addActionListener(new NewActionListener());
				break;
			case "Delete":
				button.addActionListener(new DeleteActionListener());
				break;
			case "Save":
				button.setEnabled(false);
				button.addActionListener(new SaveActionListener());
				break;
			case "Cancel":
				button.setEnabled(false);// if its clicked->disabled button
				button.addActionListener(new CancelActionListener());
				break;	
		}
		
		button.setToolTipText(label);
		return button;
	}
	
	//NoteList position
	private GridBagConstraints getConstraintsForNoteList() {
	
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 2;
		c.insets = new Insets(12, 12, 11, 11); 
		return c;
	}
	
	//NameTextField position
	private GridBagConstraints getConstraintsForNameTextField() {
	
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;//NameTextField goes left <-
		c.insets = new Insets(12, 0, 11, 11);
		return c;
	}
	
	//TextArea position
	private GridBagConstraints getConstraintsForTextArea() {
	
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 1;
		c.fill = GridBagConstraints.BOTH;//fill both up, right
		c.insets = new Insets(0, 0, 11, 11);
		return c;
	}

	//ToolBar position
	private GridBagConstraints getConstraintsForButtonToolBar() {
	
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 2;
		c.insets = new Insets(0, 12, 11, 11);
		return c;
	}
	
	// X    0        1
 	// 0 NoteList   Name
	// 1 NoteList   Text
	// 2 ToolBar
	
	//NoteListModel object
	private NoteListModel getNoteListModel() {
	
		return (NoteListModel) noteList.getModel();
	}
	
	//List selection settings,+ enable/disable buttons
	private class ListSelectListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {//when user chooses a note
					
			if (getNoteListModel().isEmpty()) {//if note noteList is empty
			
				deleteButton.setEnabled(false);//delete button is disabled
				textFld.setText("");//clear textfield and textarea
				textArea.setText("");
			}
			else {//if its not empty,and note is chosen
				deleteButton.setEnabled(true);//delete button is enabled
				Note n = noteList.getSelectedValue(); //chosen note
				textFld.setText(n.getName()); //sets name of a chosen note
				textArea.setText(n.getText()); //sets text of a chosen note
				textArea.setCaretPosition(0); //view beginning of the note
			}
			
			//when choosing a note
			textFld.setEditable(false); //notes cant be edited
			textArea.setEditable(false); // notes cant be edited
			newButton.setEnabled(true); //new button is available
			saveButton.setEnabled(false);	//save button is disabled	
			cancelButton.setEnabled(false);//cancel button is disabled
			updateFlag = false;			
		}
	}
	
	//new button -action
	private class NewActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {//action when clicked on New
			
			newButton.setEnabled(false);//disable new button
			saveButton.setEnabled(true);//enable save button
			deleteButton.setEnabled(false);//disable delete button
			cancelButton.setEnabled(true);//enable cancel button
			textFld.setEditable(true);//editable note's name
			textArea.setEditable(true);//editable text area
			textFld.setText("");//clear name&textarea
			textArea.setText("");
			textFld.requestFocusInWindow();//focus on note's name	
		}
	}
	
	//save button -action
	private class SaveActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {//when clicked on save button

			String name = textFld.getText();
			String text = textArea.getText();
			
			//SAVE FILE AS TXT
			try {
			    // Выбор файла для сохранения с помощью диалогового окна
			    JFileChooser fileChooser = new JFileChooser();
			    int result = fileChooser.showSaveDialog(frame);

			    if (result == JFileChooser.APPROVE_OPTION) {
			        currentFile = fileChooser.getSelectedFile();
			        
			        // Запись заметки в выбранный файл
			        FileWriter writer = new FileWriter(currentFile);
			        writer.write(text);
			        writer.close();
			        
			        // Вывод сообщения об успешном сохранении
			        JOptionPane.showMessageDialog(frame, "Note saved");
			    }
			} catch (IOException ex) {
			    ex.printStackTrace();
			    // Вывод сообщения об ошибке сохранения
			    JOptionPane.showMessageDialog(frame, "Error");
			}
			//SAVE FILE AS TXT
			
			Note newN = new Note(name, text); //new object(Note)
			Note originalN = noteList.getSelectedValue();

			if (updateFlag) {
				
				getNoteListModel().update(originalN, newN);
			}
			else {				
				getNoteListModel().add(newN);
			}
			
			noteList.updateUI();

			int ix = getNoteListModel().indexOf(newN);
			noteList.setSelectedIndex(ix);
			noteList.ensureIndexIsVisible(ix);
			
			textFld.setEditable(false);
			textArea.setEditable(false);		
			newButton.setEnabled(true);
			saveButton.setEnabled(false);
			deleteButton.setEnabled(true);
			cancelButton.setEnabled(false);
			updateFlag = false;			
	
		}
	}
	
	
	//delete button -action
	private class DeleteActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			int ix = noteList.getSelectedIndex();
			
			if (ix < 0) {
			
				return;
			}
			
			Note n = noteList.getSelectedValue();
			
			int optionSelected = JOptionPane.showConfirmDialog(frame,
					"Delete the selected Note ?", "Delete",
					JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
									
			if (optionSelected != JOptionPane.YES_OPTION) {
			
				return;
			}

			getNoteListModel().delete(n);
			
			if (currentFile != null && currentFile.exists()) {
			    currentFile.delete();
			    currentFile = null;
			}
			
			noteList.updateUI();
												
			if (getNoteListModel().isEmpty()) {
			
				textFld.setText("");
				textArea.setText("");
				deleteButton.setEnabled(false);
	
				return;
			}
			
			if (ix > 0) {
			
				--ix;
			}

			noteList.setSelectedIndex(ix);						
	
						
			if (ix == 0) {
			
				Note nn = noteList.getSelectedValue();
				textFld.setText(nn.getName());
				textArea.setText(nn.getText());
			}
		}
	}
	
	
	//cancel button -action
	private class CancelActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
		

			if (getNoteListModel().isEmpty()) {//if noteList is empty
			
				textFld.setText("");//clearing text
				textArea.setText("");
				deleteButton.setEnabled(false);//disable delete button
			}
			else {//if its not empty
				Note n = noteList.getSelectedValue();	//selected note		
				textFld.setText(n.getName());
				textArea.setText(n.getText());
				deleteButton.setEnabled(true);//enable button
			}

			textFld.setEditable(false);
			textArea.setEditable(false);
			newButton.setEnabled(true);
			saveButton.setEnabled(false);
			cancelButton.setEnabled(false);
			updateFlag = false;
		}
	}
}
