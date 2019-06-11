package fr.ftp.model;

import java.io.IOException;

public class UploadShedullerThread extends ShedullerThread {
	
	private String in,out;
	private FTPConnection conn;

	public UploadShedullerThread(String in, String out, FTPConnection conn) {
		// TODO Auto-generated constructor stub
		this.in = in;
		this.out = out;
		this.conn = conn;
	}

	public void run() {
		try {
			conn.uploadFile(out, in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
