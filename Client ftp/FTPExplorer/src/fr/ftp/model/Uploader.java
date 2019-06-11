package fr.ftp.model;

public class Uploader extends Sheduller {
	
	public Uploader(FTPConnection conn) {
		super(conn);
	}
	
	public void addThingToDo(String in, String out) {
		this.isDone.add(false);
		this.threads.add(new UploadShedullerThread(in,out,conn));
		processQueue();
	}

}
