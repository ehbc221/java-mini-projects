package fr.ftp.model;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import fr.ftp.view.FTPTaskExplorer;

public class TaskThread extends Thread implements Runnable{
	
	private boolean running = true;
	private FTPTaskExplorer explorer;
	private JProgressBar jpb;
	private JPanel ex = new JPanel(new GridLayout(1,2));
	
	public TaskThread(FTPTaskExplorer explore, String taskName) {
		explorer = explore;
		jpb = new JProgressBar(0,100);
		jpb.setStringPainted(true);
		JLabel jl = new JLabel(taskName);
		jl.setMaximumSize(jl.getMinimumSize());
		jpb.setMaximumSize(jpb.getMinimumSize());
		jpb.setIndeterminate(false);
		ex.add(jl);
		ex.add(jpb);
		ex.setMaximumSize(ex.getMinimumSize());
		ex.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		explorer.add(ex,0);
		explorer.revalidate();
	}

	public void stopThread() {
		// TODO Auto-generated method stub
		running = false;
	}
	
	public void run() {
		while(running) {
			
		}
	}
	
	public JPanel getPanel() {
		return ex;
	}
	
	public void setProgress(int progress) {
		if(progress <= 100) {
			jpb.setValue(progress);
		} else {
			jpb.setIndeterminate(true);
		}
	}

}
