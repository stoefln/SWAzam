package client;

import jade.gui.GuiEvent;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileFilter;
import lib.entities.SearchResponse;

public class SwazamGUI extends JFrame implements ActionListener{
	
	private static final long serialVersionUID = 1L;

	private client.ClientAgent clientAgent;
	JPanel panel; 
	String mp3FileName; 
	public SwazamGUI(ClientAgent agent) {
		clientAgent = agent;

		JFrame f = new JFrame();
		f.setTitle("Swazam");
		f.setSize(500, 400);
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		f.setContentPane(create());

		f.setVisible(true);
	}
	


	private JButton accessButton, browseButton, sendButton;
	JTextArea resultDisplay;
	private boolean AcessTokenIsSet = false;
	
	 
	public JPanel create() {
		panel = new JPanel(); 
		// Create and specify a layout manager
		panel.setLayout(new GridBagLayout());

		// Create a constraints object, and specify some default values
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH; // components grow in both dimensions
		c.insets = new Insets(5, 5, 5, 5); // 5-pixel margins on all sides

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 4;
		c.gridheight = 4;
		c.weightx = c.weighty = 1.0;

		resultDisplay = new JTextArea();
		resultDisplay.setLineWrap(true);
		panel.add(resultDisplay, c);

		c.gridx = 4;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = c.weighty = 0.0;
		accessButton = new JButton("Access Token");
		panel.add(accessButton, c);

		c.gridx = 4;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		browseButton = new JButton("Browse File");
		panel.add(browseButton, c);

		c.gridx = 4;
		c.gridy = 2;
		c.gridwidth = 1;
		c.gridheight = 1;
		sendButton = new JButton("Send File");
		panel.add(sendButton, c);

		resultDisplay.append("Please enter your access token to use swazam.");
		browseButton.setEnabled(false);
		sendButton.setEnabled(false);

		accessButton.addActionListener(this);

		browseButton.addActionListener(this);
		sendButton.addActionListener(this); 
		return panel;
	}
	
	public void setResult(SearchResponse res) {

		if (res == null) {
			resultDisplay
					.setText("Could not receive a valid response from server.");
			return;
		}
		if (res.wasFound()) {
			resultDisplay.setText(res.toString());

		} else {
			resultDisplay.setText("Sorry, no match found! ");
		}
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()== accessButton){
			processTokenButtonClick(); 
			GuiEvent ge = new GuiEvent(this,EventTypes.SESSION_TOKEN  );	
			ge.addParameter(token); 
			clientAgent.postGuiEvent(ge);
			
		}else
		if(e.getSource()== browseButton){
			processBrowseButtonClick(); 
			GuiEvent ge = new GuiEvent(this,EventTypes.BROWSE  );
			ge.addParameter(mp3FileName); 
			clientAgent.postGuiEvent(ge);
			
		}else
		if(e.getSource()== sendButton){
			System.out.println("sendButton clicked");
			GuiEvent ge = new GuiEvent(this,EventTypes.SEND  );		
			ge.addParameter(mp3FileName); 
			clientAgent.postGuiEvent(ge);
			
		}
		
	}
	String token; 
	private void processTokenButtonClick() {
		JFrame login = new JFrame();
		String result = JOptionPane.showInputDialog(login,
				"Enter AcessToken:");

		if (result == null)
			return;

		token = (result);
		AcessTokenIsSet = true;
		if (AcessTokenIsSet) {
			accessButton.setEnabled(false);
			sendButton.setEnabled(false);
			browseButton.setEnabled(true);
			resultDisplay.setText("You may now select a file. You can select only mp3 files.");

		} else {
			resultDisplay.setText("You have to enter valid acessToken");
		}
	}

	private void processBrowseButtonClick() {
		JFileChooser c = new JFileChooser();
		c.setFileFilter(new FileFilter() {
			
			@Override
			public String getDescription() {
				
				return "*.mp3";
			}
			
			@Override
			public boolean accept(File f) {
				return f.getName().endsWith(".mp3"); 
			}
		}); 
		// Demonstrate "Open" dialog:
		int rVal = c.showOpenDialog(new JFrame());
		if (rVal == JFileChooser.APPROVE_OPTION) {
			final File selectedFile = c.getSelectedFile();
			mp3FileName = selectedFile.getAbsolutePath(); 
			resultDisplay.setText("Successfully opened file: "
					+ mp3FileName);			

			sendButton.setVisible(true);
			sendButton.setEnabled(true);

		}
		if (rVal == JFileChooser.CANCEL_OPTION) {
			resultDisplay
					.setText("A valid music file must be selected.");

		}
	}
}
