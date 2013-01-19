package client;

/*
 * Copyright (c) 2000 David Flanagan. All rights reserved. This code is from the
 * book Java Examples in a Nutshell, 2nd Edition. It is provided AS-IS, WITHOUT
 * ANY WARRANTY either expressed or implied. You may study, use, and modify it
 * for any non-commercial purpose. You may distribute it non-commercially as
 * long as you retain this notice. For a commercial use license, or to purchase
 * the book (recommended), visit http://www.davidflanagan.com/javaexamples2.
 */

import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.StyledDocument;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import lib.entities.SearchRequest;
import lib.entities.SearchResponse;


import ac.at.tuwien.infosys.swa.audio.Fingerprint;


public class SwazamGUI extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private client.ClientAgent clientAgent;

	
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
	
	private final class BrowseButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
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

				resultDisplay.setText("Successfully opened file: "
						+ selectedFile.getAbsolutePath());

				resultDisplay.append("\n Calculating fingerprint.....");
				browseButton.setEnabled(false);

				Runnable fpcalc = new Runnable() {
					@Override
					public void run() {
						SwazamGUI.this.setCursor(new Cursor(Cursor.HAND_CURSOR));
						fp = FingerPrintCreator
								.createFingerPrint(selectedFile);
						request.setFingerprint(fp);
						browseButton.setEnabled(true);
						sendButton.setEnabled(true);
						resultDisplay
								.append("\n Fingerprint was calculated. You may now send it to the server.");
						SwazamGUI.this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
						
					}
				};

				Thread thread = new Thread(fpcalc);
				thread.start();

				sendButton.setVisible(true);

			}
			if (rVal == JFileChooser.CANCEL_OPTION) {
				resultDisplay
						.setText("A valid music file must be selected.");

			}
		}
	}

	private final class AccessButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			JFrame login = new JFrame();
			String result = JOptionPane.showInputDialog(login,
					"Enter AcessToken:");

			if (result == null)
				return;

			request.setAcessToken(result);
			AcessTokenIsSet = (request.isAcessTokenSet());
			if (AcessTokenIsSet) {
				accessButton.setEnabled(false);
				sendButton.setEnabled(false);
				browseButton.setEnabled(true);
				resultDisplay.setText("You may now select a file. You can select only mp3 files.");

			} else {
				resultDisplay.setText("You have to enter valid acessToken");
			}
		}
	}

	private JButton accessButton, browseButton, sendButton;
	JTextArea resultDisplay;
	private boolean AcessTokenIsSet = false;
	private Fingerprint fp;
	SearchRequest request = new SearchRequest();
	SwazamController controller = new SwazamController(); 
	public JPanel create() {
		// Create and specify a layout manager
		this.setLayout(new GridBagLayout());

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
		this.add(resultDisplay, c);

		c.gridx = 4;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = c.weighty = 0.0;
		accessButton = new JButton("Access Token");
		this.add(accessButton, c);

		c.gridx = 4;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		browseButton = new JButton("Browse File");
		this.add(browseButton, c);

		c.gridx = 4;
		c.gridy = 2;
		c.gridwidth = 1;
		c.gridheight = 1;
		sendButton = new JButton("Send File");
		this.add(sendButton, c);

		resultDisplay.append("Please enter your access token to use swazam.");
		browseButton.setEnabled(false);
		sendButton.setEnabled(false);

		accessButton.addActionListener(new AccessButtonListener());

		browseButton.addActionListener(new BrowseButtonListener());
		sendButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				clientAgent.reqst = request;
			}
		});
		return this;
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
}
