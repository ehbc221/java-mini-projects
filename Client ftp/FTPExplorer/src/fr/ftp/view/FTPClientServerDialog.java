package fr.ftp.view;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class FTPClientServerDialog extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1260701759353765564L;
	
	
	public FTPClientServerDialog() {
		this.setLayout(new GridLayout(0,1));
	}
	

	public void addCommand(String command) {
		JLabel l = new JLabel(command);
		l.setForeground(Color.GREEN);
		this.add(l,0);
		this.revalidate();
	}
	public void addResponse(String response) {
		JLabel l = new JLabel(response);
		l.setForeground(Color.RED);
		this.add(l,0);
		this.revalidate();
	}

}
