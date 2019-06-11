package fr.ftp.model;

import java.io.File;
import java.util.ArrayList;

public class LocalFileLister {

	private File file;
    private ArrayList<File> directorys = new ArrayList<File>();
    private ArrayList<File> files = new ArrayList<File>();
    private ArrayList<File> toParse = new ArrayList<File>();
    public static String currentDir = "";
	
	public LocalFileLister() {
		for(File i : File.listRoots()) {
			toParse.add(i);
		}
		parse();
	}

	private void parse() {
		// TODO Auto-generated method stub
		directorys.removeAll(directorys);
		files.removeAll(files);
		for(File i : toParse) {
			if(i.isDirectory()) {
				directorys.add(i);
			} else {
				files.add(i);
			}
		}
		toParse.removeAll(toParse);
	}
	
	public ArrayList<File> getFiles() {
		return files;
	}
	
	public String[] getDirectorys() {
		String k = "Parent Directory¤";
		for(File i : directorys) {
			k+=i.toString()+"¤";
		}
		return k.split("¤");
	}
	
	public void changeDirectory(String newDir) {
		System.out.println(currentDir);
		while (true) {
			if (newDir.equals("Parent Directory")) { // Répertoire parent
				// demandé
				if (currentDir.matches("^.:\\\\$")) { // Si on est dans la
					// racine d'un lecteur
					// exemple : E:\ //
					// Fichier vide (pour
					// plus tard)
					for (File i : File.listRoots()) { // On liste les lecteurs
						toParse.add(i);
					}
					break;
				} else {
					file = file.getParentFile();
					currentDir = file.getPath();
					// Sinon on va dans le répertoire parent
					for (File i : file.listFiles()) { // Sinon on liste les fichiers
						toParse.add(i);
					}
					break;
				}
			} else { // Le répertoire parent n'est pas demandé
				file = new File(newDir); // Donc on va dans le répertoire
				// demandé
				currentDir = newDir;
				for (File i : file.listFiles()) { // Sinon on liste les fichiers
					toParse.add(i);
				}
				break;
			}
		}
		parse(); // Et on parse le tout
	}
}