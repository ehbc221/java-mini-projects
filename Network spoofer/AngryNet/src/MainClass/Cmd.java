package MainClass;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class Cmd {
public StringBuilder exec_Cmd(String Command) throws InterruptedException, IOException{
	Process p = Runtime.getRuntime().exec(Command);
	p.waitFor();
	BufferedReader reader = 
	         new BufferedReader(new InputStreamReader(p.getInputStream()));
		StringBuilder sb = new StringBuilder();
	    String line = "";			
	    while ((line = reader.readLine())!= null) {
		sb.append(line + "\n");
	    }
		return sb;
	
}
public String Create_tmpFile() throws IOException, InterruptedException{
	
	File temp = File.createTempFile("Capture", ".cap"); 
	try{
	temp.deleteOnExit();
	
	}catch(Exception e){
		
	}
	 BufferedWriter bw = new BufferedWriter(new FileWriter(temp));
	    bw.write(new Cmd().exec_Cmd("arp -a").toString());
	    bw.close();
   return  temp.getCanonicalPath();
}

}
