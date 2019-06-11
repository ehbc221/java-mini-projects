package fr.ftp.view;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import fr.ftp.FTP;
import fr.ftp.control.FTPPopupMenuActionListener;
import fr.ftp.model.FTPConnection;
import fr.ftp.model.LocalFileLister;

public class FTPRemotePopupMenu extends JPopupMenu {

	private static final long serialVersionUID = -7339864565430166313L;

	public FTPRemotePopupMenu(final String fileName, MouseEvent e, String filePath, final String currentDir) {
		// TODO Auto-generated constructor stub
		filePath = filePath.replaceAll("[\r\n]+", "");
		filePath = filePath.replaceAll("[\\\\]+", "/");
		final String path = filePath;
		System.out.println(filePath);
		JMenuItem jmi = new JMenuItem("Télécharger "+fileName);
		jmi.addActionListener(new FTPPopupMenuActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					public void run() {
						System.out.println("Download : "+FTPConnection.currentPath+"/"+path+"->"+LocalFileLister.currentDir);
						FTP.downloader.addThingToDo(FTPConnection.currentPath+"/"+path, LocalFileLister.currentDir, fileName);
					}
				}).start();
			}
		});
		JMenuItem jmi1 = new JMenuItem("Créer un dossier");
		jmi1.addActionListener(new FTPPopupMenuActionListener() {
			public void actionPerformed(ActionEvent e) {
				String dir = JOptionPane.showInputDialog("Directory name ?");
				try {
					FTP.conn.makeDirectory(dir);
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		JMenuItem jmi2 = new JMenuItem("Supprimer");
		jmi2.addActionListener(new FTPPopupMenuActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					public void run() {
						try {
							if(!FTP.conn.deleteFile(FTPConnection.currentPath+"/"+path)) {
								FTP.conn.removeDirectory(FTPConnection.currentPath+"/"+path);
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}	
					}
				}).start();
			}
		});
		this.add(jmi);
		this.add(jmi1);
		this.add(jmi2);
		this.show(e.getComponent(), e.getX(), e.getY());
	}

}
