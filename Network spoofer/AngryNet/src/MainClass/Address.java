package MainClass;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Address {
public static String getAttackerName(){
	String Name="";
	try{
		Name=InetAddress.getLocalHost().getHostName();
	}catch(UnknownHostException e){
		
	}
	return Name;
}
public static String getInternalAddress(){
	String Address="";
	try{
		Address=InetAddress.getLocalHost().getHostAddress();
	}catch(UnknownHostException e){
		
	}
	return Address;
}

public String getMacAddress() throws SocketException, UnknownHostException{
	NetworkInterface netint = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
	StringBuilder sb = new StringBuilder();
	
	byte [] mac=netint.getHardwareAddress();
	for (int i = 0; i < mac.length; i++) {
		sb.append(String.format("%02X%s", mac[i],""));		
	}
	return sb.toString();
}
public  StringBuilder IPtoHex(String IP){
String [] address  = IP.replace(".", ":").split(":");
StringBuilder str = new StringBuilder();
for(String s : address){
	s=Integer.toHexString(Integer.parseInt(s));
	s=(s.length()<2)?"0"+s:s;
	str.append(s);
}
return str;
}
}
