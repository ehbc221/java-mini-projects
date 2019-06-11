package fr.ftp.model;

import java.io.File;
import java.io.IOException;

public class DownloadShedullerThread extends ShedullerThread {
	
	private String in,out, fileName;
	private FTPConnection conn;

	public DownloadShedullerThread(String in, String out,String fileName, FTPConnection conn) {
		// TODO Auto-generated constructor stub
		this.in = in;
		this.out = out;
		System.out.println("m "+out+File.separator+fileName);
		this.fileName = fileName;
		this.conn = conn;
	}
	
	public void run() {
		try {
			conn.downloadFile(in, out, fileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
