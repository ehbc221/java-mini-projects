package MainClass;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileClass {
private File file;

/**
 * @param file
 */
public FileClass(File file) {
	super();
	this.file = file;
}

public long getLines() throws IOException{
	BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()));
	@SuppressWarnings("unused")
	String line;
	
	 int linenumber=0;
	 while((line=br.readLine())!=null){
		 try{
			 
			linenumber+=1;
			
		 }catch(Exception e){
			 e.printStackTrace();
		 }
	 }
	   br.close();

	return linenumber;
	
}
}
