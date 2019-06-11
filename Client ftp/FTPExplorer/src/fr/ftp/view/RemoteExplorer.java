package fr.ftp.view;

import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.swing.SwingUtilities;

import fr.ftp.control.FTPExplorerMouseListener;
import fr.ftp.model.Explorer;
import fr.ftp.model.FTPConnection;
import fr.ftp.model.FTPFile;
import fr.ftp.model.RemoteFileLister;

public class RemoteExplorer extends Explorer {

	private static final long serialVersionUID = 8175867045107521722L;
	private RemoteFileLister fileLister;
	private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

	public void addFolder(String name) {
		this.addFolder(new Object[]{"", name,"Folder", "", ""});
	}
	
	public void addFile(String name, int size, long update) {
		this.addFile(new Object[]{"", name, "File", String.valueOf(size), sdf.format(update)});
	}
	
	public void removeAll() {
		this.getModel().removeAll();
	}
	
	public RemoteExplorer() throws IOException {
		fileLister = new RemoteFileLister();
		for(String g : fileLister.getDirectorys()) {
			addFolder(g);
		}
		for(FTPFile g : fileLister.getFiles()) {
			addFile(g.name,(int)g.size,g.lastModified);
		}
		this.getModel().fireTableDataChanged();
		this.addMouseListener(new FTPExplorerMouseListener() {
			public void mouseClicked(MouseEvent e) {
				if(SwingUtilities.isRightMouseButton(e)) {
					int row = ((RemoteExplorer)e.getSource()).rowAtPoint(e.getPoint());
					String fileName = (String)RemoteExplorer.this.getModel().getFileAt(row);
					new FTPRemotePopupMenu(fileName, e, fileLister.currentDir+File.separator+fileName, fileLister.currentDir);
				} else if(e.getClickCount() == 1) {
					int row = ((RemoteExplorer)e.getSource()).rowAtPoint(e.getPoint());
					final String directory = (String)RemoteExplorer.this.getModel().getFileAt(row);
					new Thread(new Runnable() {
						public void run() {
							try {
								if(directory.equals("..")) {
									fileLister.parentDirectory();
								} else {
									fileLister.changeDirectory(FTPConnection.currentPath+"/"+directory.trim());
								}
								removeAll();
								for(String g : fileLister.getDirectorys()) {
									addFolder(g);
								}
								for(FTPFile g : fileLister.getFiles()) {
									addFile(g.name,(int)g.size,g.lastModified);
								}
							} catch(IOException e) {
								e.printStackTrace();
							}
						}
					}).start();
					RemoteExplorer.this.getModel().fireTableDataChanged();
				}
			}
		});
	}
}

