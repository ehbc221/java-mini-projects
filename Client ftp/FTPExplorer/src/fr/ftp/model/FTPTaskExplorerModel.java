package fr.ftp.model;

import java.util.HashMap;
import java.util.LinkedList;

import fr.ftp.FTP;
import fr.ftp.view.FTPTaskExplorer;

public class FTPTaskExplorerModel {
	
	private FTPTaskExplorer explorer;
	private LinkedList<TaskThread> threadList = new LinkedList<TaskThread>();
	private HashMap<String, Integer> tasks = new HashMap<String, Integer>();
	private int taskCount = 0;
	
	public FTPTaskExplorerModel(FTPTaskExplorer exp) {
		explorer = exp;
	}
	
	public void addTask(String taskName) {
		tasks.put(taskName, taskCount);
		taskCount++;
		threadList.add(new TaskThread(explorer, taskName));
	}
	
	public void removeTask(String taskName, int index) {
		threadList.get(index).stopThread();
		tasks.remove(taskName);
		explorer.remove(threadList.get(index).getPanel());
		explorer.revalidate();
		explorer.repaint();
		FTP.taskDone.add(taskName);
	}
	
	public int getTaskIndex(String taskName) {
		return tasks.get(taskName);
	}
	
	public void setTaskProgress(String taskName, int progress) {
		threadList.get(getTaskIndex(taskName)).setProgress(progress);
	}
}
