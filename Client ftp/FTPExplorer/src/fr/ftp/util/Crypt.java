package fr.ftp.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;

import fr.ftp.view.ConnectionManager;

public class Crypt {
 private String algo = "Blowfish";

 public void crypter(String password, String entree, String sortie) throws IOException {
   
     byte[] passwordInBytes;
	try {
		passwordInBytes = password.getBytes("ISO-8859-2");
		Key clef = new SecretKeySpec(passwordInBytes, algo); 
		Cipher cipher = Cipher.getInstance(algo);
		cipher.init(Cipher.ENCRYPT_MODE, clef);
		
		byte[] texteClaire = ouvrirFichier(entree);
		byte[] texteCrypte = cipher.doFinal(texteClaire);
		sauverFichier(sortie, texteCrypte);
	} catch (InvalidKeyException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IllegalBlockSizeException e) {
		// TODO Auto-generated catch block
		System.out.println("decrypt");
		ConnectionManager.deleteAndCreateNewFile();
		JOptionPane.showMessageDialog(null, "Error : the save file has been modified !", "Error !", JOptionPane.ERROR_MESSAGE);
		System.exit(0);
	} catch (BadPaddingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (NoSuchAlgorithmException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (NoSuchPaddingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   
 }

 public void decrypter(String password, String entree, String sortie) throws IOException {

     byte[] passwordInBytes = password.getBytes("ISO-8859-2"); 
     Key clef = new SecretKeySpec(passwordInBytes, algo); 
     try {
    	 Cipher cipher = Cipher.getInstance(algo);
		cipher.init(Cipher.DECRYPT_MODE, clef);
		byte[] texteCrypte = ouvrirFichier(entree);
		byte[] texteClaire = cipher.doFinal(texteCrypte);
		sauverFichier(sortie, texteClaire);
	} catch (InvalidKeyException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IllegalBlockSizeException e) {
		// TODO Auto-generated catch block
		System.out.println("decrypt");
		ConnectionManager.deleteAndCreateNewFile();
		e.printStackTrace();
		JOptionPane.showMessageDialog(null, "Error : the save file has been modified !", "Error !", JOptionPane.ERROR_MESSAGE);
		System.exit(0);
	} catch (BadPaddingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (NoSuchAlgorithmException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (NoSuchPaddingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
 }

 private byte[] ouvrirFichier(String filename) {
   try {
     File fichier = new File(filename);
     byte[] result = new byte[(int) fichier.length()];
     FileInputStream in = new FileInputStream(filename);
     in.read(result);
     in.close();
     return result;
   }
   catch (Exception e) {
     System.out.println("Probleme lors de la lecture du fichier: " + e.getMessage());
     return null;
   }
 }

 private void sauverFichier(String filename, byte[] data) {
   try {
     FileOutputStream out = new FileOutputStream(filename);
     out.write(data);
     out.close();
   }
   catch (Exception e) {
     System.out.println("Probleme lors de la sauvegarde du fichier: " + e.getMessage());
   }
 }
}