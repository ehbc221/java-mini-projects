package fr.ftp.model;


public class FTPFile{
	
	public String name;
	public long size;
	public long lastModified;

	public FTPFile(String name, long size, long lastModified) {
		this.name = name;
		this.size = size;
		this.lastModified = lastModified;
	}

}
