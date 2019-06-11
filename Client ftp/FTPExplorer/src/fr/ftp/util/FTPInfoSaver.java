package fr.ftp.util;

import java.awt.Component;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import fr.ftp.view.FTPClientServerDialog;

public class FTPInfoSaver {
	private FTPClientServerDialog serverDialog;

	public FTPInfoSaver(String mode, FTPClientServerDialog serverDialog) {
		// TODO Auto-generated constructor stub
		this.serverDialog = serverDialog;
		switch(mode) {
		case "save":
			saveFTPDialog();
			break;
		}
	}
	
	private void saveFTPDialog() {
		String dialog = "";
		for(Component c : serverDialog.getComponents()) {
			dialog += ((JLabel)c).getText()+"\n";
		}
		String fileName = JOptionPane.showInputDialog("Nom du fichier ?");
		try {
			FileWriter f = new FileWriter(new File(fileName+".ftplog"));
			f.write(dialog);
			f.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
