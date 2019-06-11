package jeux;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import jeux.enumerations.*;
import jeux.ihm.dialogues.*;
import jeux.cartes.*;
import jeux.mains.*;
import jeux.ihm.*;
/**Classe interm&eacute;diaire qui permet
 * le lancement du logiciel*/
public class Jouer{
	/**Tous les modes de jouerie Nombre de joueurs pour le president, ou le tarot,
	 * avec ou sans surcontrat pour la belote et Type de distribution pour le solitaire*/
	public static final Vector<Vector<String>> modes1=new Vector<Vector<String>>();
	/**Deuxieme mode pour le tarot*/
	public static final Vector<String> modes2=new Vector<String>();
	static
	{
		modes1.addElement(new Vector<String>());
		modes1.lastElement().addElement(ModeBelote.Sans_surcontrat.name());
		modes1.lastElement().addElement(ModeBelote.Avec_surcontrat.name());
		modes1.addElement(new Vector<String>());
		for(byte b=3;b<6;b++)
		{
			modes1.lastElement().addElement(b+"_joueurs");
		}
		modes2.addElement("Normal");
		modes2.addElement("Normal_avec_misere");
		modes2.addElement("Normal_avec_chacun_pour_soi");
		modes2.addElement("Misere");
		modes2.addElement("Chacun_pour_soi");
	}
	/**Installe si necessaire puis lance le logiciel avec des parametres*/
	static void lancer()
	{
		Parametres par;
		int[] coordonnees=new int[2];
		try
		{
			par=chargerParametres();
		}
		catch(Exception e)
		{
			par=new Parametres();
			e.printStackTrace();
			if(!System.getProperty("os.name").contains("Windows"))
			{
				par.getInfos().get(0).setElementAt(par.getInfos().get(0).lastElement().split(";")[0]+";[a-zA-Z0-9_]",par.getInfos().get(0).size()-1);
			}
			par.sauvegarder();
			JOptionPane.showMessageDialog(null,"Fichier "+Parametres.fichier+" de parametres installe","Installation finie",JOptionPane.INFORMATION_MESSAGE);
		}
		installer();
		try
		{
			coordonnees=chargerLocation();
			new File(Fichier.coordonnees_fenetre).delete();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		new Fenetre().init(par,coordonnees);
	}
	private static Parametres chargerParametres()throws Exception
	{
		ObjectInputStream oos=new ObjectInputStream(new BufferedInputStream(new FileInputStream(new File(Parametres.fichier))));
		Parametres par=(Parametres)oos.readObject();
		oos.close();
		return par;
	}
	private static int[] chargerLocation()throws Exception
	{
		ObjectInputStream oos=new ObjectInputStream(new BufferedInputStream(new FileInputStream(new File(Fichier.coordonnees_fenetre))));
		int[] coordonnees=(int[])oos.readObject();
		oos.close();
		return coordonnees;
	}
	/**Permet d'installer les dossiers de sauvegardes, de statistiques, ainsi que des paquets de jeux de cartes et le nombre de parties
	 * jouees pendant le fonctionnement du logiciel*/
	private static void installer()
	{
		File f;
		Vector<String> dossiers_installes=new Vector<String>();
		Vector<String> fichiers_installes=new Vector<String>();
		String message_fichiers="";
		String message_dossiers="";
		String message2="Fichiers installes:\n";
		String message3="Dossiers installes:\n";
		for(Type type:Type.values())
		{
			for(Jeu jeu:Jeu.values())
			{
				for(String mode:modes1.get(jeu.ordinal()))
				{
					if(jeu!=Jeu.Tarot&&type!=Type.Entrainement)
					{
						f=new File(Fichier.dossier_sauvegarde+File.separator+jeu.toString().replace(Lettre.ea,Lettre.e)+File.separator+mode+File.separator+type);
						if(!f.exists()&&f.mkdirs())
						{
							dossiers_installes.addElement(Fichier.dossier_sauvegarde+File.separator+jeu.toString().replace(Lettre.ea,Lettre.e)+File.separator+mode+File.separator+type);
						}
					}
					else if(jeu==Jeu.Tarot)
					{
						for(String mode2:modes2)
						{
							f=new File(Fichier.dossier_sauvegarde+File.separator+jeu+File.separator+mode+File.separator+mode2+File.separator+type);
							if(!f.exists()&&f.mkdirs())
							{
								dossiers_installes.addElement(Fichier.dossier_sauvegarde+File.separator+jeu+File.separator+mode+File.separator+mode2+File.separator+type);
							}
						}
					}
				}
			}
		}
		f=new File(Fichier.dossier_paquets);
		if(!f.exists()&&f.mkdir())
		{
			dossiers_installes.addElement(Fichier.dossier_paquets);
		}
		f=new File(Fichier.dossier_paquets+File.separator+Jeu.Belote+Fichier.extension_paquet);
		Main main=new MainBelote();
		for (byte carte = 0; carte < 32; carte++)
			main.ajouter(new CarteBelote(carte));
		if(!f.exists())
		{
			main.sauvegarder(Fichier.dossier_paquets+File.separator+Jeu.Belote+Fichier.extension_paquet);
			fichiers_installes.addElement(Fichier.dossier_paquets+File.separator+Jeu.Belote+Fichier.extension_paquet);
		}
		f=new File(Fichier.dossier_paquets+File.separator+Jeu.Tarot+Fichier.extension_paquet);
		main=new MainTarot();
		for (byte carte = 0; carte < 78; carte++)
			main.ajouter(new CarteTarot(carte));
		if(!f.exists())
		{
			main.sauvegarder(Fichier.dossier_paquets+File.separator+Jeu.Tarot+Fichier.extension_paquet);
			fichiers_installes.addElement(Fichier.dossier_paquets+File.separator+Jeu.Tarot+Fichier.extension_paquet);
		}
		f=new File(Fichier.dossier_paquets+File.separator+Fichier.fichier_paquet);
		BufferedWriter bw;
		if(!f.exists())
		{
			try {
				bw=new BufferedWriter(new FileWriter(f));
				for(int i=0;i<2;i++)
				{
					bw.write(new Integer(0).toString());
					bw.newLine();
				}
				bw.close();
				fichiers_installes.addElement(Fichier.dossier_paquets+File.separator+Fichier.fichier_paquet);
			}
			catch(IOException e){}
		}
		for(String fichier:fichiers_installes)
		{
			message_fichiers+="\t"+fichier+"\n";
		}
		for(String dossier:dossiers_installes)
		{
			message_dossiers+="\t"+dossier+"\n";
		}
		JTextArea zone=new JTextArea("");
		if(!message_fichiers.isEmpty())
		{
			message2+=message_fichiers;
			zone.append(message2);
		}
		if(!message_dossiers.isEmpty())
		{
			message3+=message_dossiers;
			zone.append(message3);
		}
		if(!message_fichiers.isEmpty()||!message_dossiers.isEmpty())
		{
			final JDialog dialogue=new JDialog((JFrame)null,"Installation finie");
			dialogue.setModal(true);
			Container container=new Container();
			container.setLayout(new BorderLayout());
			JScrollPane barre_defilement=new JScrollPane(zone);
			barre_defilement.setPreferredSize(new Dimension(400,400));
			container.add(barre_defilement,BorderLayout.CENTER);
			JButton bouton_ok=new JButton("OK");
			bouton_ok.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					dialogue.dispose();
				}
			});
			container.add(bouton_ok,BorderLayout.SOUTH);
			dialogue.setContentPane(container);
			dialogue.pack();
			dialogue.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
			try {/*Permet d avoir une application graphique comme si c etait Windows*/
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				SwingUtilities.updateComponentTreeUI(dialogue);
			} catch (InstantiationException e) {
			} catch (ClassNotFoundException e) {
			} catch (UnsupportedLookAndFeelException e) {
			} catch (IllegalAccessException e) {}
			dialogue.setVisible(true);
		}
	}
}
