package MainClass;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetAllDevices {
private File Capture;

/**
 * @param capture
 */
public GetAllDevices(File capture) {
	super();
	Capture = capture;
}
@SuppressWarnings("rawtypes")
public List ReadFromFile() throws IOException{
	List <ConnectedDevice> devices_List = new ArrayList<ConnectedDevice>() ;
		BufferedReader br = new BufferedReader(new FileReader(Capture.getAbsolutePath()));
		String line;
		 FileClass f = new FileClass(Capture);
		 long lines=f.getLines();
		 int device_number=0;
		 long CurrentLine=1;
		 while((line=br.readLine())!=null){
			 try{
				if(CurrentLine>3 && CurrentLine<=lines-5){
				 device_number+=1;
				 String [] devices = line.trim().split(" ",2);
				 devices[1]=devices[1].trim().split(" ",2)[0];
				 
				ConnectedDevice device = new ConnectedDevice();
				device.setNumber(device_number);
				device.setIP_Address(devices[0]);
				device.setMAC_Address(devices[1]);
			devices_List.add(device);
				}
				CurrentLine+=1;
			 }catch(Exception e){
				 e.printStackTrace();
			 }
		 }
		   br.close();
return devices_List;
}

}
