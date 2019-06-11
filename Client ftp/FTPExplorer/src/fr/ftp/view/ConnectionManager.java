package fr.ftp.view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import fr.ftp.util.Crypt;

public class ConnectionManager {
	
	private static File saveFile;
	private static Crypt crypt = new Crypt();
	private static String key = "aceb84a1664e293bab295a5b10e02671";
	public static void checkFile() throws IOException {
		saveFile = new File("ftp.exp");
		if(!saveFile.exists()) {
			try {
				createFile();
			} catch (IOException | JDOMException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private static void decodeFile() throws IOException {
		// TODO Auto-generated method stub
		crypt.decrypter(key, "ftp.exp", "ftp.exp");
	}
	
	private static void encodeFile() throws IOException {
		crypt.crypter(key, "ftp.exp", "ftp.exp");
	}
	
	public static void deleteAndCreateNewFile() throws IOException {
		saveFile.delete();
		saveFile.createNewFile();
		FileOutputStream f = new FileOutputStream(saveFile);
		PrintWriter fw = new PrintWriter(f);
		fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><FTPExplorer><LoginConnexions></LoginConnexions></FTPExplorer>");
		fw.close();
		encodeFile();
	}

	private static void createFile() throws IOException, JDOMException {
		// TODO Auto-generated method stub
		saveFile.createNewFile();
		FileOutputStream f = new FileOutputStream(saveFile);
		PrintWriter fw = new PrintWriter(f);
		fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><FTPExplorer><LoginConnexions></LoginConnexions></FTPExplorer>");
		fw.close();
		encodeFile();
	}

	public static ArrayList<String[]> getConnectInfos() throws Exception {
		checkFile();
		decodeFile();
		ArrayList<String[]> k = new ArrayList<String[]>();
		SAXBuilder sxb = new SAXBuilder();
		Document doc = sxb.build(saveFile);
		Element root = doc.getRootElement();
		List<Element> elements = root.getChild("LoginConnexions").getChildren();
		Iterator<Element> it = elements.iterator();
		while(it.hasNext()) {
			Element current = (Element)it.next();
			String[] a = new String[] {
					current.getAttributeValue("host"),
					current.getAttributeValue("user"),
					current.getAttributeValue("pass")
			};
			k.add(a);
		}
		encodeFile();
		return k;
	}

	public static void connect(String[] connInfo) throws JDOMException, IOException {
		// TODO Auto-generated method stub
		decodeFile();
		SAXBuilder sxb = new SAXBuilder();
		Document doc = sxb.build(saveFile);
		Element root = doc.getRootElement();
		Element toAdd = new Element("cnx");
		toAdd.setAttribute("host", connInfo[0]);
		toAdd.setAttribute("user", connInfo[1]);
		toAdd.setAttribute("pass", connInfo[2]);
		Iterator<Element> it = root.getChild("LoginConnexions").getChildren().iterator();
		int i = 0;
		while(it.hasNext()) {
			Element current = (Element)it.next();
			if(current.getAttributeValue("host").equals(connInfo[0]) && current.getAttributeValue("user").equals(connInfo[1])) {
				i++;
			}
		}
		if(i==0) {
			root.getChild("LoginConnexions").addContent(toAdd);
			XMLOutputter output = new XMLOutputter(Format.getPrettyFormat());
			output.output(doc, new FileOutputStream(new File("ftp.exp")));
		}
		encodeFile();
	}

}
