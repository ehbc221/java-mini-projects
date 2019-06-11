package fr.ftp.model;

import java.io.IOException;
import java.util.ArrayList;

import fr.ftp.FTP;

public class RemoteFileLister {

    private String[] directorys;
    private String[] files;
    public String currentDir = "";
    private FTPConnection conn;
    
    public RemoteFileLister() throws IOException {
    	conn = FTP.conn;
    	directorys = conn.listSubdirectories().split("\n");
    	files = conn.listFiles().split("\n");
    }
    
    public String[] getDirectorys() {
    	return directorys;
    }
    
    public ArrayList<FTPFile> getFiles() throws IOException {
    	ArrayList<FTPFile> m = new ArrayList<FTPFile>();
    	for(String g : files) {
    		FTPFile p = new FTPFile(g , conn.getFileSize(g), conn.getModificationTime(g));
    		m.add(p);
    	}
    	return m;
    }
    
    public void changeDirectory(String newDir) throws IOException {
    	conn.changeDirectory(newDir);
    	directorys = conn.listSubdirectories().split("\n");
    	files = conn.listFiles().split("\n");
    }
    
    public void parentDirectory() throws IOException {
    	conn.parentDirectory();
    	directorys = conn.listSubdirectories().split("\n");
    	files = conn.listFiles().split("\n");
    }
}
