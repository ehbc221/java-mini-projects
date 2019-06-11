package jeux;
import java.io.*;
import java.util.*;
import jeux.enumerations.*;
import jeux.ihm.dialogues.*;
/**Parametres du logiciel et des jeux a utiliser a la fin*/
public class Parametres implements Serializable{
	private static final long serialVersionUID = 1L;
	/**Ce sont les informations utilisees pendant le fonctionnement du logiciel*/
	private Vector<Vector<String>> infos=new Vector<Vector<String>>();
	/**Ce sont les parametres installes par defaut, en cas de suppression du fichier parametres.txt, ils servent a le
	 * reinstaller*/
	public static final String fichier=Fichier.parametres;
	/**Contient les informations pour l'ensemble des parametres*/
	public static final Vector<Vector<String>> par=new Vector<Vector<String>>();
	public static final String separateur_tiret;
	public static final String separateur_tiret_slash;
	static{
		int nombre_jeux=Jeu.values().length;
		for(byte jeu=0;jeu<2+nombre_jeux;jeu++)
		{
			par.addElement(new Vector<String>());
		}
		int belote=Jeu.Belote.ordinal()+1;
		String ch_belote=Jeu.Belote.toString().toLowerCase();
		int tarot=Jeu.Tarot.ordinal()+1;
		String ch_tarot=Jeu.Tarot.toString().toLowerCase();
		char p2=Lettre.p2;
		char tb=Lettre.tb;
		char pv=Lettre.pv;
		char es=Lettre.es;
		char tt=Lettre.tt;
		char quote2=Lettre.quote2;
		char pt=Lettre.pt;
		char pi=Lettre.pi;
		char et=Lettre.et;
		char cd=Lettre.cd;
		char cg=Lettre.cg;
		char in=Lettre.in;
		char su=Lettre.su;
		char sl=Lettre.sl;
		char as=Lettre.as;
		char ch=Lettre.ch;
		char pp=Lettre.pp;
		char c0=48;
		char c5=53;
		char A=65;
		char a=97;
		char B=66;
		char b=98;
		char c=99;
		char D=68;
		char d=100;
		char E=69;
		char e=101;
		char ea=233;
		char eg=232;
		char g=103;
		char i=105;
		char j=106;
		char L=76;
		char l=108;
		char m=109;
		char n=110;
		char o=111;
		char p=112;
		char r=114;
		char S=83;
		char s=115;
		char T=84;
		char t=116;
		char u=117;
		char x=120;
		String ch_v=Lettre.ch_v;
		separateur_tiret=ch_v+es+tt+es;
		separateur_tiret_slash=ch_v+es+as+tt+es;
		String attente=ch_v+A+t+t+e+n+t+e+es+e+n+t+r+e+es;
		String annonces_contrat=ch_v+a+n+n+o+n+c+e+s+es+d+e+es+c+o+n+t+r+a+t+es;
		String cartes=ch_v+c+a+r+t+e+s+es;
		String plis=ch_v+p+l+i+s+es;
		String c500=ch_v+p2+c5+c0+c0;
		String delai_pli_pour=ch_v+D+e+l+a+i+es+p+l+i+es+p+o+u+r+es;
		String par_clic=ch_v+es+p+a+r+es+c+l+i+c;
		String tri=ch_v+T+r+i;
		String info;
		String battre_cartes=ch_v+B+a+t+t+r+e+es+l+e+s+es+c+a+r+t+e+s;
		String sens_distribution=ch_v+S+e+n+s+es+d+e+es+d+i+s+t+r+i+b+u+t+i+o+n;
		String joueur=ch_v+j+o+u+e+u+r;
		par.get(0).addElement(ch_v+L+a+n+c+e+m+e+n+t+p2+Lancements.Bienvenue_dans_les_jeux_de_cartes);
		par.get(0).addElement(attente+annonces_contrat+ch_belote+c500);
		par.get(0).addElement(attente+cartes+ch_belote+c500);
		par.get(0).addElement(attente+plis+ch_belote+c500);
		par.get(0).addElement(attente+annonces_contrat+ch_tarot+c500);
		par.get(0).addElement(attente+cartes+ch_tarot+c500);
		par.get(0).addElement(attente+plis+ch_tarot+c500);
		par.get(0).addElement(delai_pli_pour+l+a+es+ch_belote+par_clic+p2+Reponse.oui+pv+delai_pli_pour+l+e+es+ch_tarot+par_clic+p2+Reponse.oui);
		par.get(0).addElement("Belote,cliquer pour jouer:"+Reponse.oui+";"+"Tarot,cliquer pour jouer:"+Reponse.oui);
		par.get(0).addElement(ch_v+E+x+p+r+e+s+s+i+o+n+es+r+ea+g+u+l+i+eg+r+e+pv+cg+ch+es+p2+as+as+as+pi+sl+quote2+as+pt+as+pp+as+et+in+su+as+t+cd);
		par.get(belote).addElement(Jeu.Belote.toString()+p2);
		par.get(belote).addElement(battre_cartes+p2+ChoixBattreCartes.a_chaque_partie);
		par.get(belote).addElement(sens_distribution+p2+Sens.Anti_horaire);
		info=ch_v;
		int indice=0;
		for(Couleur couleur:Couleur.values())
		{
			if(couleur!=Couleur.Atout)
			{
				if(indice>0)
				{
					info+=separateur_tiret;
				}
				info+=couleur;
				indice++;
			}
		}
		par.get(belote).addElement(tri+es+a+u+es+d+ea+b+u+t+p2+info+pv+Monotonie.Decroissant);
		par.get(belote).addElement("Ordre avant enchere:"+Ordre.Atout);
		par.get(belote).addElement("Obligation de sous-couper adversaire:"+Reponse.non);
		par.get(belote).addElement("Obligation avec le partenaire:"+BeloteCoupePartenaire.Ni_sur_coupe_ni_sous_coupe_obligatoire);
		par.get(belote).addElement("Calcul des scores a la fin:classique");
		par.get(tarot).addElement(Jeu.Tarot.toString()+p2);
		par.get(tarot).addElement(battre_cartes+p2+ChoixBattreCartes.a_chaque_partie);
		par.get(tarot).addElement(sens_distribution+p2+Sens.Anti_horaire);
		info=ch_v;
		for(Couleur couleur:Couleur.values())
		{
			if(couleur.ordinal()>0)
			{
				info+=separateur_tiret;
			}
			info+=couleur;
		}
		par.get(tarot).addElement(tri+p2+info+pv+Monotonie.Decroissant);
		info="Poignees:";
		for(Poignees poignee:Poignees.values())
		{
			if(poignee.ordinal()>0)
			{
				info+=separateur_tiret;
			}
			info+=poignee;
		}
		info+=";Miseres:";
		par.get(tarot).addElement(info);
		par.get(tarot).addElement("Chelem contrat:"+Reponse.non);
		par.get(tarot).addElement("Regle du demi-point:classique");
		par.get(tarot).addElement("Mode:Normal");
		par.get(tarot).addElement("4 joueurs:"+Repartition4Joueurs.c1_vs_3);
		par.get(tarot).addElement("5 joueurs:"+Repartition5Joueurs.c2_vs_3_appel_au_roi);
		par.lastElement().addElement("Joueurs:");
		par.lastElement().addElement("Pseudo:Votre_pseudo");
		par.lastElement().addElement("Autres joueurs:");
		par.lastElement().addElement(Jeu.Belote.toString()+p2);
		info=ch_v;
		for(indice=1;indice<4;indice++)
		{
			if(indice>1)
			{
				info+=ch_v+tb;
			}
			info+=joueur+es+ch_v+indice;
		}
		par.lastElement().addElement(info);
		par.lastElement().addElement(Jeu.Tarot.toString()+p2);
		info=ch_v;
		for(indice=1;indice<5;indice++)
		{
			if(indice>1)
			{
				info+=ch_v+tb;
			}
			info+=joueur+es+ch_v+indice;
		}
		par.lastElement().addElement(info);
	}
	/**Cree les parametres de fonctionnement pour le logiciel*/
	public Parametres(){
		infos=par;
	}
	/**Sauvegarde les informations dans le fichier*/
	public void sauvegarder()
	{
		try {
			ObjectOutputStream oos=new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(new File(fichier))));
			oos.writeObject(this);
			oos.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	/**Modifie une partie des informations*/
	public void changerGroupe(int indice,Vector<String> info)
	{
		infos.setElementAt(info, indice);
	}
	/**Retourne les informations*/
	public Vector<Vector<String>> getInfos()
	{
		return infos;
	}
}