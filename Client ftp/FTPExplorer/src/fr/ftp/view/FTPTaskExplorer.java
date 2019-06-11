package fr.ftp.view;

import java.awt.GridLayout;

import javax.swing.JPanel;

import fr.ftp.model.FTPTaskExplorerModel;

public class FTPTaskExplorer extends JPanel {
	
	private static final long serialVersionUID = -6333198174898489001L;
	private FTPTaskExplorerModel model;
	
	public FTPTaskExplorer() {
		super(new GridLayout(0,1));
		model = new FTPTaskExplorerModel(this);
	}
	
	public void addTask(String taskName) {
		model.addTask(taskName);
	}
	
	public void removeTask(String taskName) {
		model.removeTask(taskName, model.getTaskIndex(taskName));
	}
	
	public void setTaskProgress(String taskName, int progress) {
		model.setTaskProgress(taskName, progress);
	}
	
}
