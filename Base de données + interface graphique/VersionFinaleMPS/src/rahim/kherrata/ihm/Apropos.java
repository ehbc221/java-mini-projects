package rahim.kherrata.ihm;

import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class Apropos extends JDialog{
	private JOptionPane jop = new JOptionPane();
	private String str;
	ImageIcon img = new ImageIcon("images/univbe.jpg");
	JOptionPane jop1 = new JOptionPane(), jop2 = new JOptionPane();
	
	
	
	public Apropos(){
		
		str = "Mini-Projet réaliser par : \n";
		str += "\t \t \t \t \t \t \t \t \t \t \t \t \t \t"+"Boudi Abderrahim \n"; 
		str += "\t \t \t \t \t \t \t \t \t \t \t \t \t \t"+"Ounas Rida\n";
		str += "\t \t \t \t \t \t \t \t \t \t \t \t \t \t"+"M'sili Fateh\n";
		str += "\t \t \t \t \t \t \t \t \t \t \t \t \t \t"+"Zidoune Halim\n";
		str += "\t \t \t \t \t \t \t \t \t \t \t \t \t \t"+"Amghar Abdessalam\n";
		str += "\t \t \t \t \t \t \t \t \t \t \t \t \t \t"+"Zitouni Bilal\n";
		str += "\t \t \t \t \t \t \t \t \t \t \t \t \t \t"+"Rida\n\n\n";
		str += "Maître de Mini-Projet : \n";
		str += "\t \t \t \t \t \t \t"+"Ounas Rida\n";
		jop.showMessageDialog( null, str,"A propos...", JOptionPane. INFORMATION_MESSAGE, img); 
		
		
	}
	
}