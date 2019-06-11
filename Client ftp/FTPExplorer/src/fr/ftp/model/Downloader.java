package fr.ftp.model;

public class Downloader extends Sheduller {
	
	public Downloader(FTPConnection conn) {
		super(conn);
	}
	
	public void addThingToDo(String in, String out, String fileName) {
		this.isDone.add(false);
		this.threads.add(new DownloadShedullerThread(in,out,fileName,conn));
		processQueue();
	}

}
