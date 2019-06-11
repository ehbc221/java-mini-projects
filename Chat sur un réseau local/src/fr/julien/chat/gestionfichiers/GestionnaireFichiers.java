package fr.julien.chat.gestionfichiers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class GestionnaireFichiers {

	private static final GestionnaireFichiers instance = new GestionnaireFichiers();

	private GestionnaireFichiers(){
		super();
	}

	public void append(String filename, String text) {
		FileWriter writer = null;
		try{
			writer = new FileWriter(filename, true);
			writer.write(text, 0, text.length());
		}
		catch(IOException ex){
			ex.printStackTrace();
		}
		finally{
			if(writer != null){
				try {
					writer.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public String recupererContenu(String fichier){
		StringBuilder sb = new StringBuilder();
		try{
			BufferedReader buff = new BufferedReader(new FileReader(fichier));
			try {
				String line;
				while ((line = buff.readLine()) != null) {
					sb.append(line);
				}
			} 
			finally {
				buff.close();
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();

	}

	public static GestionnaireFichiers getInstance() {
		return instance;
	}


}
