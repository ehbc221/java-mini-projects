package jeux;
import java.io.*;
import java.util.*;
import jeux.cartes.*;
import jeux.mains.*;
import jeux.enumerations.*;
/**Classe permettant de stocker les parametres de la partie associee pour economiser la memoire
 * Une donne possede un donneur pour savoir a qui distribuer en premier
 * Une donne est constitue de mains pour repartir les cartes
 * Le mode sert lorsque la partie n'est pas solitaire a determiner le nombre de joueurs
 * et lorsque la partie est solitaire a determiner la facon de repartitir les cartes*/
public class Donne implements Serializable,Iterable<Main>{
	private static final long serialVersionUID = 1L;
	/**Ensemble des mains des joueurs*/
	private Vector<Main> donne=new Vector<Main>();
	/**donneur est un entier allant de 0 a nombre de joueurs-1*/
	private byte donneur;
	/**Represente le nombre de joueurs ou le mode de jeu*/
	private String mode;
	/**Parametres de la partie stockes dans un objet de type Donne*/
	private Vector<String> infos;
	/**nombre de parties jouees depuis le lancement*/
	private long nbDeParties;
	/**Pile de distribution pour initialiser la donne*/
	private transient Main pile;
	public Donne(String mode1,Vector<String> info,long nombreDeParties,Main ppile)
	{//info est le vecteur d'informations pour le jeu, nombreDeParties est necessaire pour savoir si c'est
		//la premiere fois qu'une partie est joue, pile est necessaire pour savoir si on ne distribue jamais
		mode=mode1;
		infos=info;
		nbDeParties=nombreDeParties;//jouees depuis le lancement
		pile=ppile;
	}
	public Donne(String mode1,Vector<String> info,long nombreDeParties)
	{//j est le jeu a jouer, info est le vecteur d'informations pour le jeu, nombreDeParties est necessaire pour savoir si c'est
		//la premiere fois qu'une partie est joue, pile est necessaire pour savoir si on ne distribue jamais
		mode=mode1;
		infos=info;
		nbDeParties=nombreDeParties;//jouees depuis le lancement
	}
	/**Utilise pour editer une partie*/
	public Donne(String mode1,Vector<String> info,Vector<Main> pdonne)
	{
		mode=mode1;
		infos=info;
		donne=pdonne;
	}
	/**Utilise pour editer une partie non solitaire*/
	public Donne(String mode1,Vector<String> info,Vector<Main> pdonne,byte pdonneur)
	{
		mode=mode1;
		infos=info;
		donne=pdonne;
		donneur=pdonneur;
	}
	public String getMode()
	{
		return mode;
	}
	public Vector<String> getInfos()
	{
		return infos;
	}
	/**Initialise de maniere aleatoire le premier donneur*/
	public void initDonneur()
	{
		//On recupere le nombre de joueurs dans le cas d'un jeu non solitaire
		byte nombreDeJoueurs;
		if(!(pile instanceof MainBelote))
			nombreDeJoueurs=Byte.parseByte(mode.split(" ")[0]);
		else
			nombreDeJoueurs=4;
		donneur=(byte)(nombreDeJoueurs*Math.random());
	}
	/**Initialise le donneur pour editer une partie*/
	public void initDonneur(byte b)
	{
		//On initialise le donneur par l'editeur
		donneur=b;
	}
	/**Apres une partie la joueur apres le donneur actuel devient le nouveau donneur*/
	public void donneurSuivant(byte nouveau_donneur)
	{
		donneur=nouveau_donneur;
		//On recupere le nombre de joueurs dans le cas d'un jeu non solitaire
		byte nombreDeJoueurs;
		if(!(pile instanceof MainBelote))
			nombreDeJoueurs=Byte.parseByte(mode.split(" ")[0]);
		else
			nombreDeJoueurs=4;
		donneur++;
		donneur%=nombreDeJoueurs;
	}
	public long getNombreDeParties()
	{
		return nbDeParties;
	}
	public byte getDonneur()
	{
		return donneur;
	}
	/**Distribue les cartes de maniere aleatoire ou non selon les parametres de distribution, on ne tient pas compte du sens de distribution*/
	public void initDonne()
	{
		if(infos.get(1).contains(ChoixBattreCartes.a_chaque_partie.toString()))
		{
			donnerEnBattant();
		}
		else if(infos.get(1).contains(ChoixBattreCartes.a_chaque_lancement.toString())||infos.get(1).contains(ChoixBattreCartes.une_seule_fois.toString()))
		{
			if(nbDeParties==0)
			{
				donnerEnBattant();
			}
			else
			{
				donnerSansBattre();
			}
		}
		else
		{
			donnerSansBattre();
		}
	}
	/**Pour l&#39;entra&icirc;nement surtout au tarot*/
	public void initDonne(ChoixTarot choix,byte nombre_cartes,byte nombre_joueurs)
	{
		/*Les deux nombres donnent le nombre d atouts avec Excuse*/
		byte min_atout=0;
		byte max_atout=0;
		byte atouts_tires=0;
		byte autres_cartes_tirer=0;
		Entier[] fonction_repartition;
		Entier alea;
		if(choix==ChoixTarot.Petit_a_chasser)
		{
			if(nombre_cartes==24)
			{
				min_atout=15;
				max_atout=21;
			}
			else if(nombre_cartes==18)
			{
				min_atout=13;
				max_atout=18;
			}
			else if(nombre_cartes==15)
			{
				min_atout=10;
				max_atout=15;
			}
			else
			{
				min_atout=10;
				max_atout=14;
			}
			fonction_repartition=new Entier[max_atout-min_atout+1];
			jeux.parties.Partie.chargement_simulation+=90/(max_atout-min_atout+1);
			fonction_repartition[0]=Entier.combinaison(min_atout,21).multiplierPositif(Entier.combinaison(nombre_cartes-min_atout,56));
			for(byte evenement=(byte)(min_atout+1);evenement<=max_atout;evenement++)
			{
				jeux.parties.Partie.chargement_simulation+=90/(max_atout-min_atout+1);
				fonction_repartition[evenement-min_atout]=fonction_repartition[evenement-min_atout-1].ajouterPositif(Entier.combinaison(evenement,21).multiplierPositif(Entier.combinaison(nombre_cartes-evenement,56)));
			}
			alea=fonction_repartition[fonction_repartition.length-1].fois(Math.random());
			jeux.parties.Partie.chargement_simulation=95;
			for(byte evenement=min_atout;evenement<=max_atout;evenement++)
			{
				if(alea.plusPetitQue(fonction_repartition[evenement-min_atout]))
				{
					atouts_tires=evenement;
					break;
				}
			}
			autres_cartes_tirer=(byte)(nombre_cartes-atouts_tires);
			for (int i = 0; i < nombre_joueurs+1; i++)
			{
				donne.addElement(new MainTarot());
			}
			MainTarot atouts=new MainTarot();
			MainTarot autres_cartes=new MainTarot();
			for(byte i=0;i<21;i++)
			{
				atouts.ajouter(new CarteTarot(i));
			}
			for(byte i=22;i<78;i++)
			{
				autres_cartes.ajouter(new CarteTarot(i));
			}
			for(byte tirage=0;tirage<atouts_tires;tirage++)
			{
				donne.get(0).ajouter(atouts.tirerUneCarteAleatoire());
			}
			for(byte tirage=0;tirage<autres_cartes_tirer;tirage++)
			{
				donne.get(0).ajouter(autres_cartes.tirerUneCarteAleatoire());
			}
			pile=new MainTarot();
			pile.ajouterCartes(atouts);
			pile.ajouterCartes(autres_cartes);
			byte reste=(byte) (78-nombre_cartes*nombre_joueurs);
			for(byte joueur=1;joueur<nombre_joueurs;joueur++)
			{
				for(byte indice_carte=0;indice_carte<nombre_cartes;indice_carte++)
				{
					donne.get(joueur).ajouter(pile.tirerUneCarteAleatoire());
				}
			}
			for(int i=0;i<reste;i++)
			{
				donne.lastElement().ajouter(pile.jouer(0));
			}
			for (int i = 0; i < nombre_joueurs+1; i++) {
				((MainTarot)donne.get(i)).trier(infos.get(3), infos.get(3));
			}
			initDonneur();
		}
		else
		{
			if(choix==ChoixTarot.Petit_a_sauver)
			{
				min_atout=1;
				if(nombre_cartes==24)
				{
					max_atout=5;
				}
				else if(nombre_cartes==18)
				{
					max_atout=4;
				}
				else if(nombre_cartes==15)
				{
					max_atout=3;
				}
				else
				{
					max_atout=2;
				}
			}
			else if(choix==ChoixTarot.Petit_a_emmener_au_bout)
			{
				if(nombre_cartes==24)
				{
					min_atout=14;
					max_atout=21;
				}
				else if(nombre_cartes==18)
				{
					min_atout=12;
					max_atout=17;
				}
				else if(nombre_cartes==15)
				{
					min_atout=9;
					max_atout=14;
				}
				else
				{
					min_atout=9;
					max_atout=13;
				}
			}
			fonction_repartition=new Entier[max_atout-min_atout+1];
			jeux.parties.Partie.chargement_simulation+=90/(max_atout-min_atout+1);
			fonction_repartition[0]=Entier.combinaison(min_atout,21).multiplierPositif(Entier.combinaison(nombre_cartes-min_atout-1,56));
			for(byte evenement=(byte)(min_atout+1);evenement<=max_atout;evenement++)
			{
				jeux.parties.Partie.chargement_simulation+=90/(max_atout-min_atout+1);
				fonction_repartition[evenement-min_atout]=fonction_repartition[evenement-min_atout-1].ajouterPositif(Entier.combinaison(evenement,21).multiplierPositif(Entier.combinaison(nombre_cartes-evenement-1,56)));
			}
			alea=fonction_repartition[fonction_repartition.length-1].fois(Math.random());
			jeux.parties.Partie.chargement_simulation=95;
			for(byte evenement=min_atout;evenement<=max_atout;evenement++)
			{
				if(alea.plusPetitQue(fonction_repartition[evenement-min_atout]))
				{
					atouts_tires=evenement;
					break;
				}
			}
			autres_cartes_tirer=(byte)(nombre_cartes-atouts_tires-1);
			for (int i = 0; i < nombre_joueurs+1; i++)
			{
				donne.addElement(new MainTarot());
			}
			MainTarot atouts=new MainTarot();
			MainTarot autres_cartes=new MainTarot();
			for(byte i=0;i<21;i++)
			{
				atouts.ajouter(new CarteTarot(i));
			}
			for(byte i=22;i<78;i++)
			{
				autres_cartes.ajouter(new CarteTarot(i));
			}
			for(byte tirage=0;tirage<atouts_tires;tirage++)
			{
				donne.get(0).ajouter(atouts.tirerUneCarteAleatoire());
			}
			donne.get(0).ajouter(new CarteTarot((byte)1,(byte)1));
			for(byte tirage=0;tirage<autres_cartes_tirer;tirage++)
			{
				donne.get(0).ajouter(autres_cartes.tirerUneCarteAleatoire());
			}
			pile=new MainTarot();
			pile.ajouterCartes(atouts);
			pile.ajouterCartes(autres_cartes);
			byte reste=(byte) (78-nombre_cartes*nombre_joueurs);
			for(byte joueur=1;joueur<nombre_joueurs;joueur++)
			{
				for(byte indice_carte=0;indice_carte<nombre_cartes;indice_carte++)
				{
					donne.get(joueur).ajouter(pile.tirerUneCarteAleatoire());
				}
			}
			for(int i=0;i<reste;i++)
			{
				donne.lastElement().ajouter(pile.jouer(0));
			}
			for (int i = 0; i < nombre_joueurs+1; i++) {
				((MainTarot)donne.get(i)).trier(infos.get(3), infos.get(3));
			}
			initDonneur();
		}
	}
	/**On change de joueur en ne changeant pas de sens<br/>
	 * exemple:<br/>
	 * pour nbJ=4, on a:<br/>
	 * <ol>
	 * <li>si i vaut 1 alors la valeur retournee est 2</li>
	 * <li>si i vaut 2 alors la valeur retournee est 3</li>
	 * <li>si i vaut 3 alors la valeur retournee est 0</li>
	 * <li>si i vaut 0 alors la valeur retournee est 1</li>
	 * </ol>
	 * @return le numero du joueur suivant*/
	private int miseAjour(int i,int nbJ)
	{
		i++;//Joueur suivant
		i%=nbJ;//Si necessaire on remet i a 0 pour entamer une nouvelle boucle
		return i;
	}
	/**On distribue les cartes sans les cartes ce qui ressemble plus a la realite
	 * On ne tient pas compte du sens de distribution*/
	private void donnerSansBattre()
	{
		pile.couper();
		if(pile instanceof MainBelote)
		{
			String ordre=infos.get(4).split(":")[1];
			/*On cree les mains des joueurs puis le talon qui sera distribue
			 * apres les encheres*/
			for (int i = 0; i < 5; i++) {
				donne.addElement(new MainBelote(Ordre.valueOf(ordre)));
			}
			/*On donne les cartes aux autres joueurs que le donneur.
			 *Le nombre de cartes donnes par joueur est de 3*/
			for (int i = (donneur+1)%4; i !=donneur; i=miseAjour(i,4)) {
				for (int j = 0; j < 3; j++) {
					donne.get(i).ajouter(pile.jouer(0));
				}
			}
			/*On donne les cartes au donneur.
			 *Le nombre de cartes donnes par joueur est de 3*/
			for (int j = 0; j < 3; j++) {
				donne.get(donneur).ajouter(pile.jouer(0));
			}
			/*On donne les cartes aux autres joueurs que le donneur.
			 *Le nombre de cartes donnes par joueur est de 2*/
			for (int i = (donneur+1)%4; i !=donneur; i=miseAjour(i,4)) {
				for (int j = 0; j < 2; j++) {
					donne.get(i).ajouter(pile.jouer(0));
				}
			}
			/*On donne les cartes au donneur.
			 *Le nombre de cartes donnes par joueur est de 2*/
			for (int j = 0; j < 2; j++) {
				donne.get(donneur).ajouter(pile.jouer(0));
			}
			/*On ajoute le reste des cartes dans le talon*/
			for (int i = 0; i < 12; i++) {
				donne.lastElement().ajouter(pile.jouer(0));
			}
		}
		else
		{
			/*On recupere le nombre de joueurs jouant au tarot*/
			byte nbJrs=Byte.parseByte(mode.split(" ")[0]);
			/*On prepare les mains des joueurs*/
			for (int i = 0; i < nbJrs+1; i++) {
				donne.addElement(new MainTarot());
			}
			if(nbJrs==3)
			{
				/*On distribue les cartes de facon a ce qu'il y ait six cartes au chien*/
				for(int i=0;i<6;i++)
				{
					/*On donne trois cartes a chaque joueur autre que le donneur*/
					for(int j = (donneur+1)%nbJrs; j !=donneur; j=miseAjour(j,nbJrs))
					{
						for(int k=0;k<3;k++)
						{
							donne.get(j).ajouter(pile.jouer(0));
						}
					}
					/*Le donneur se met trois cartes*/
					for(int k=0;k<3;k++)
					{
						donne.get(donneur).ajouter(pile.jouer(0));
					}
					/*On met une carte dans le chien*/
					donne.get(3).ajouter(pile.jouer(0));
				}
				/*Maintenant six cartes sont au chien*/
				/*On distribue les dernieres cartes*/
				for(int i=0;i<2;i++)
				{
					/*On donne trois cartes a chaque joueur autre que le donneur*/
					for(int j = (donneur+1)%nbJrs; j !=donneur; j=miseAjour(j,nbJrs))
					{
						for(int k=0;k<3;k++)
						{
							donne.get(j).ajouter(pile.jouer(0));
						}
					}
					/*Le donneur se met trois cartes*/
					for(int k=0;k<3;k++)
					{
						donne.get(donneur).ajouter(pile.jouer(0));
					}
				}
			}
			else if(nbJrs==4)
			{
				/*On distribue les cartes de facon a ce qu'il y ait six cartes au chien*/
				for(int i=0;i<3;i++)
				{
					int j;
					/*On donne trois cartes aux deux joueurs suivant le donneur*/
					for(j = (donneur+1)%nbJrs; j !=(donneur+3)%nbJrs; j=miseAjour(j,nbJrs))
					{
						for(int k=0;k<3;k++)
						{
							donne.get(j).ajouter(pile.jouer(0));
						}
					}
					/*On met une carte dans le chien*/
					donne.get(4).ajouter(pile.jouer(0));
					/*On donne trois cartes aux autres joueurs*/
					for(j = (donneur+3)%nbJrs; j !=(donneur+1)%nbJrs; j=miseAjour(j,nbJrs))
					{
						for(int k=0;k<3;k++)
						{
							donne.get(j).ajouter(pile.jouer(0));
						}
					}
					/*On met une carte dans le chien*/
					donne.get(4).ajouter(pile.jouer(0));
				}
				/*Maintenant six cartes sont au chien*/
				/*On distribue les dernieres cartes*/
				for(int i=0;i<3;i++)
				{
					/*On donne trois cartes a chaque joueur autre que le donneur*/
					for(int j = (donneur+1)%nbJrs; j !=donneur; j=miseAjour(j,nbJrs))
					{
						for(int k=0;k<3;k++)
						{
							donne.get(j).ajouter(pile.jouer(0));
						}
					}
					/*Le donneur se met trois cartes*/
					for(int k=0;k<3;k++)
					{
						donne.get(donneur).ajouter(pile.jouer(0));
					}
				}
			}
			else
			{
				/*On recupere l'information concernant la repartition des equipes*/
				if(infos.lastElement().endsWith(Repartition5Joueurs.c1_vs_4.toString()))
				{
					//On distribue les cartes deux par deux pour chaque joueur et le chien est constitue de 8 cartes
					//On ne met pas les cartes du chien a la fin
					for(int i=0;i<3;i++)
					{
						/*On donne deux cartes a chaque joueur autre que le donneur*/
						for(int j = (donneur+1)%nbJrs; j !=donneur; j=miseAjour(j,nbJrs))
						{
							for(int k=0;k<2;k++)
							{
								donne.get(j).ajouter(pile.jouer(0));
							}
						}
						/*On met une carte dans le chien*/
						donne.get(5).ajouter(pile.jouer(0));
						/*Le donneur se met deux cartes*/
						for(int k=0;k<2;k++)
						{
							donne.get(donneur).ajouter(pile.jouer(0));
						}
						/*On met une carte dans le chien*/
						donne.get(5).ajouter(pile.jouer(0));
					}
					/*Maintenant six cartes sont au chien*/
					for(int i=0;i<2;i++)
					{
						/*On donne deux cartes a chaque joueur autre que le donneur*/
						for(int j = (donneur+1)%nbJrs; j !=donneur; j=miseAjour(j,nbJrs))
						{
							for(int k=0;k<2;k++)
							{
								donne.get(j).ajouter(pile.jouer(0));
							}
						}
						/*On met une carte dans le chien*/
						donne.get(5).ajouter(pile.jouer(0));
						/*Le donneur se met deux cartes*/
						for(int k=0;k<2;k++)
						{
							donne.get(donneur).ajouter(pile.jouer(0));
						}
					}
					/*Maintenant huit cartes sont au chien*/
					for(int i=0;i<2;i++)
					{
						/*On donne deux cartes a chaque joueur autre que le donneur*/
						for(int j = (donneur+1)%nbJrs; j !=donneur; j=miseAjour(j,nbJrs))
						{
							for(int k=0;k<2;k++)
							{
								donne.get(j).ajouter(pile.jouer(0));
							}
						}
						/*Le donneur se met deux cartes*/
						for(int k=0;k<2;k++)
						{
							donne.get(donneur).ajouter(pile.jouer(0));
						}
					}
				}
				else
				{/*Si un preneur peut faire equipe avec un autre joueur*/
					/*On distribue les cartes de facon a ce qu'il y ait trois cartes au chien*/
					for(int i=0;i<3;i++)
					{
						/*On donne trois cartes a chaque joueur autre que le donneur*/
						for(int j = (donneur+1)%nbJrs; j !=donneur; j=miseAjour(j,nbJrs))
						{
							for(int k=0;k<3;k++)
							{
								donne.get(j).ajouter(pile.jouer(0));
							}
						}
						/*Le donneur se met trois cartes*/
						for(int k=0;k<3;k++)
						{
							donne.get(donneur).ajouter(pile.jouer(0));
						}
						/*On met une carte dans le chien*/
						donne.get(5).ajouter(pile.jouer(0));
					}
					/*Maintenant trois cartes sont au chien*/
					/*On distribue les dernieres cartes*/
					for(int i=0;i<2;i++)
					{
						/*On donne trois cartes a chaque joueur autre que le donneur*/
						for(int j = (donneur+1)%nbJrs; j !=donneur; j=miseAjour(j,nbJrs))
						{
							for(int k=0;k<3;k++)
							{
								donne.get(j).ajouter(pile.jouer(0));
							}
						}
						/*Le donneur se met trois cartes*/
						for(int k=0;k<3;k++)
						{
							donne.get(donneur).ajouter(pile.jouer(0));
						}
					}
				}
			}
		}
	}
	/**On distribue les cartes en les battant
	 * ceci est essentiel pour le solitaire car la fin de partie de solitaire
	 * ne depend pas a priori de la distribution au debut*/
	private void donnerEnBattant()
	{
		if(pile instanceof MainBelote)
		{
			for (int i = 0; i < 5; i++)
				donne.addElement(new MainBelote());
			MainBelote m=new MainBelote();
			for (byte i = 0; i < 32; i++)
				m.ajouter(new CarteBelote(i));
			for (int i = 0; i < 20; i++) {
				//On distribue les 1eres cartes des joueurs aleatoirement
				donne.get(i/5).ajouter(m.tirerUneCarteAleatoire());
			}
			for (int i = 20; i < 31; i++) {
				donne.lastElement().ajouter(m.tirerUneCarteAleatoire());
			}
			donne.lastElement().ajouter(m.carte(0));
			for (int i = 0; i < 4; i++) {
				((MainBelote)donne.get(i)).trier(infos.get(3), infos.get(3));
			}
		}
		else
		{
			byte nbJrs=Byte.parseByte(mode.split(" ")[0]);
			for (int i = 0; i < nbJrs+1; i++) {
				donne.addElement(new MainTarot());
			}
			MainTarot m=new MainTarot();
			for(byte i=0;i<78;i++)
				m.ajouter(new CarteTarot(i));
			if(nbJrs==3||nbJrs==4)
			{
				for (int i = 0; i < 72; i++)
					donne.get(i%nbJrs).ajouter(m.tirerUneCarteAleatoire());
				for(int i=0;i<6;i++)
					donne.lastElement().ajouter(m.jouer(0));
			}
			else
			{
				if(infos.lastElement().endsWith(Repartition5Joueurs.c1_vs_4.toString()))
				{
					for (int i = 0; i < 70; i++)
						donne.get(i%nbJrs).ajouter(m.tirerUneCarteAleatoire());
					for(int i=0;i<8;i++)
						donne.lastElement().ajouter(m.jouer(0));
				}
				else
				{
					for (int i = 0; i < 75; i++)
						donne.get(i%nbJrs).ajouter(m.tirerUneCarteAleatoire());
					for(int i=0;i<3;i++)
						donne.lastElement().ajouter(m.jouer(0));
				}
			}
			for (int i = 0; i < nbJrs+1; i++) {
				((MainTarot)donne.get(i)).trier(infos.get(3), infos.get(3));
			}
		}
	}
	/**Renvoie la main de l'utilisateur*/
	public Main main()
	{
		return donne.get(0);
	}
	public Main derniereMain()
	{
		return donne.lastElement();
	}
	public Main main(byte i)
	{
		return donne.get(i);
	}
	public byte nombreDeMains()
	{
		return (byte)donne.size();
	}
	public String toString()
	{
		return mode+"\n"+donne+"\n"+donneur+"\n"+nbDeParties+"\n"+infos;
	}
	public Iterator<Main> iterator() {
		return donne.iterator();
	}
}