package fr.ftp.view;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.SimpleDateFormat;

import javax.swing.SwingUtilities;

import fr.ftp.control.FTPExplorerMouseListener;
import fr.ftp.model.Explorer;
import fr.ftp.model.LocalFileLister;


public class LocalExplorer extends Explorer {

	private static final long serialVersionUID = 576293507229138417L;
	private LocalFileLister fileLister;
	private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	
	public void addFolder(String name) {
		this.addFolder(new Object[]{"", name,"Folder","", ""});
	}
	
	public void addFile(String name, int size, long update) {
		this.addFile(new Object[]{"", name, "File", String.valueOf(size), sdf.format(update)});
	}
	
	public void removeAll() {
		this.getModel().removeAll();
	}
	
	public LocalExplorer() {
		fileLister = new LocalFileLister();
		for(String g : fileLister.getDirectorys()) {
			addFolder(g);
		}
		this.getModel().fireTableDataChanged();
		this.addMouseListener(new FTPExplorerMouseListener() {
			public void mouseClicked(MouseEvent e) {
				if(SwingUtilities.isRightMouseButton(e)) {
					int row = ((LocalExplorer)e.getSource()).rowAtPoint(e.getPoint());
					String fileName = (String)LocalExplorer.this.getModel().getFileAt(row);
					new FTPLocalPopupMenu(fileName, e, LocalFileLister.currentDir+"\\"+fileName, LocalFileLister.currentDir);
				} else if(e.getClickCount() == 1) {
					int row = ((LocalExplorer)e.getSource()).rowAtPoint(e.getPoint());
					String directory = (String)LocalExplorer.this.getModel().getFileAt(row);
					fileLister.changeDirectory(directory);
					removeAll();
					for(String g : fileLister.getDirectorys()) {
						addFolder(g);
					}
					for(File g : fileLister.getFiles()) {
						addFile(g.getName(),(int)g.length(),g.lastModified());
					}
					LocalExplorer.this.getModel().fireTableDataChanged();
				}
			}
		});
	}
}
