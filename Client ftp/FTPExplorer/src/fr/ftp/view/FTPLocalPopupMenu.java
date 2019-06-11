package fr.ftp.view;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import fr.ftp.FTP;
import fr.ftp.control.FTPPopupMenuActionListener;
import fr.ftp.model.FTPConnection;

public class FTPLocalPopupMenu extends JPopupMenu {

	private static final long serialVersionUID = -3364644670839112096L;
	
	public FTPLocalPopupMenu(final String fileName, MouseEvent e, final String filePath, final String currentPath) {
		System.out.println(filePath);
		JMenuItem jmi = new JMenuItem("Envoyer "+fileName);
		jmi.addActionListener(new FTPPopupMenuActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					public void run() {
						FTP.uploader.addThingToDo(filePath, FTPConnection.currentPath+"/"+fileName);
					}
				}).start();
			}
		});
		JMenuItem jmi1 = new JMenuItem("Créer un dossier");
		jmi1.addActionListener(new FTPPopupMenuActionListener() {
			public void actionPerformed(ActionEvent e) {
				String dir = JOptionPane.showInputDialog("Directory name ?");
				new File(currentPath+"\\"+dir).mkdir();
			}
		});
		JMenuItem jmi2 = new JMenuItem("Supprimer");
		jmi2.addActionListener(new FTPPopupMenuActionListener() {
			public void actionPerformed(ActionEvent e) {
					new File(filePath).delete();
			}
		});
		this.add(jmi);
		this.add(jmi1);
		this.add(jmi2);
		this.show(e.getComponent(), e.getX(), e.getY());
	}
}
