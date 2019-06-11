package fr.ftp.model;

import java.awt.Dimension;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JTable;

abstract public class Explorer extends JTable {

	private Image folderIcon;
	private Image fileIcon;
	private static final long serialVersionUID = 5465470240051621641L;
	String[] title = {"","Name", "Type", "Size", "Last update"};
	Object[][] data = {  
	         {"","","","",""},
	      };
	ExplorerModel model = new ExplorerModel(data,title);
	
	public Explorer() {
		super();
		this.setMaximumSize(new Dimension(500,300));
		this.setModel(model);
		this.setDefaultRenderer(Object.class, new ExplorerTableRenderer());
		this.setShowHorizontalLines(false);
		this.setShowVerticalLines(false);
		try {
			folderIcon = ImageIO.read(ClassLoader.getSystemResourceAsStream("fr/ftp/rsc/folder_icon.jpg"));
			fileIcon = ImageIO.read(ClassLoader.getSystemResourceAsStream("fr/ftp/rsc/file_icon.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.getColumnModel().getColumn(0).setMaxWidth(30);
		
	}
	
	protected void addFile(Object [] data) {
		data[0] = fileIcon;
		getModel().addRow(data);
	}
	
	protected void addFolder(Object [] data) {
		data[0] = folderIcon;
		getModel().addRow(data);
	}
	
	public ExplorerModel getModel() {
		return model;
	}

}
