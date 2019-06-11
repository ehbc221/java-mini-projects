import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.text.*;

class FileClientImpl extends FileInterfaceClientPOA implements FileInterfaceClientOperations
{
	Frame f=null;
	int partMaxSize=10*1024*1024;
	
	FileClientImpl(Frame f)
	{
		this.f=f;	
	}
	public boolean askReceiveFile(String hostName, String fileName, int fileSize)
	{
		 int ret=JOptionPane.showConfirmDialog(f, hostName+" send you the file :\n"+fileName+"\nSize : "+getSize(fileSize)+"\nWould you like to accept it ??");
		 if(ret==JOptionPane.YES_OPTION)
		 {
			File file = new File("ReceiveBox/" + fileName);
			if(file.exists())
			{
				ret=JOptionPane.showConfirmDialog(f, "The file\n"+file.getName()+"already exists !!\nDo you want to overwrite it ??"); 
				if(ret!=JOptionPane.YES_OPTION)
					return false;
				return true;
			}
			return true;
		 }
		 return false;
	}

	public void receiveFile(String hostName, String fileName, byte[] fileData)
	{
		 try
		 {
			 // save the file
			 File file = new File("ReceiveBox/" + fileName);

			 if(file.exists())
			 {
				 int ret=JOptionPane.showConfirmDialog(f, "The file\n"+file.getName()+"\n\nalready exists !!\nDo you want to overwrite it ??");
				 if(ret!=JOptionPane.YES_OPTION)
					 return; 
			 }
			
			 BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(file));
			 output.write(fileData, 0, fileData.length);
			 output.flush();
			 output.close();
		 }
		 catch(IOException ex)
		 {
			 System.out.println("Error when receiving the file "+fileName+" !!");
		 }
	 }

	public void receiveFilePart(String hostname, String fileName, boolean notFirstPart, byte[] fileData)
	{
		 try
		 {
			 // save the file
			 File file = new File("ReceiveBox/" + fileName);
			
			 /*BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(file));
			 output.write(fileData, 0, fileData.length);
			 output.flush();
			 output.close();*/
			 FileOutputStream fos=new FileOutputStream(file, notFirstPart);
			 fos.write(fileData);
			 fos.close();
		 }
		 catch(IOException ex)
		 {
			 System.out.println("Error when receiving the file "+fileName+" !!");
		 }
	 }

	String getSize(int sizeInByte)
	{
		String size;
		DecimalFormat df = new DecimalFormat("#######0.00");
	
		if(sizeInByte<1024)
			size=Integer.toString(sizeInByte)+" B";
		else if(sizeInByte<(1024*1024))
		{
			float sizeTemp=(float)sizeInByte/1024;
			size=/*Float.toString(sizeTemp)*/df.format(sizeTemp)+" KB";
		}
		else if(sizeInByte<(1024*1024*1024))
		{
			float sizeTemp=(float)sizeInByte/(1024*1024);
			size=/*Float.toString(sizeTemp)*/df.format(sizeTemp)+" MB";
		}
		else
		{
			float sizeTemp=(float)sizeInByte/(1024*1024*1024);
			size=/*Float.toString(sizeTemp)*/df.format(sizeTemp)+" GB";
		}
		return size;
	}
}