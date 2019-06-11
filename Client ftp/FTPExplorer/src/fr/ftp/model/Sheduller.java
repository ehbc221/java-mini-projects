package fr.ftp.model;

import java.util.ArrayList;

public abstract class Sheduller {
	
	protected FTPConnection conn;
	protected ArrayList<Boolean> isDone = new ArrayList<Boolean>();
	protected ArrayList<ShedullerThread> threads = new ArrayList<ShedullerThread>();
	
	public Sheduller(FTPConnection conn) {
		this.conn = conn;
	}
	protected void processQueue() {
		for(int i = 0;i<threads.size();i++) {
			Boolean done = isDone.get(i);
			if(!done) {
				threads.get(i).start();
				isDone.set(i, true);
			}
		}
 	}

}
