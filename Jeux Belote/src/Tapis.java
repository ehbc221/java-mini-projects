/*
 * partie graphique du déroulement du jeu
 * 
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Tapis extends JPanel implements ActionListener {

	private static ImageIcon atout;

	private static ImageIcon[] carteJouee = new ImageIcon[4];

	private static Carte carter;

	private static ImageIcon[] carteSud = new ImageIcon[8];

	static String gagnant = new String("");

	static String joueurpris = new String("");

	private ImageIcon carteV, carteH, tapiscarte;

	JLabel essai;

	private Joueur[] lesjoueurs;

	JLabel nombre[] = new JLabel[8];

	int reponse = 0;

	int totalEstOuest = 0;

	int totalNordSud = 0;

	Tapis(Joueur[] joueurs) {
		
		lesjoueurs = joueurs;
		carteV = new ImageIcon("image/back-90.gif");
		carteH = new ImageIcon("image/back.gif");
		tapiscarte = new ImageIcon("image/Tapis Belotte.gif");
		essai=new JLabel(carteV);
		setLayout(null);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int x =0;
				int y=0;
				int nb = lesjoueurs[0].main.size();
				x=e.getX()-125;
				y=e.getY()-475;
				if((y>0)&&(y<97)){
					if((x>0)&&(x<(((nb-1)*40+73)))){
						reponse =(x/40);
						if(x>((nb-1)*40))reponse=nb-1;
					}
				}
			}
		});	
	}

	public void actionPerformed(ActionEvent ev) {
		String number = ev.getActionCommand();
		reponse = Integer.parseInt(number);
	}

	// ==============================================
	// Initialise le plateau
	public void affiche() {
		for (int v = 0; v < 8; v++) {
			carteSud[v] = new ImageIcon("");
		}
		for (int v = 0; v < 4; v++) {
			carteJouee[v] = new ImageIcon("");
		}
		atout = new ImageIcon("");
	}

	// ================================================
	// affiche la carte retourne
	public void affichecarter(Carte valpq, String imatout) {
		carter = valpq;
		atout = new ImageIcon("image/" + imatout + ".gif");
		repaint();
	}

	// ==========================================
	// affiche carte jouee
	public void afficheJoue(Carte y, int u) {
		carteJouee[u] = new ImageIcon("image/" + y.figure.nom + y.couleur.nom
				+ ".gif");
		repaint();
	}

	public void efface() {
		for (int v = 0; v < 4; v++) {
			carteJouee[v] = null;
		}
		repaint();
	}

	int getReponse() {
		return reponse;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// jeux joueur sud
		int nb = 0;
		int posx = 125;
		int posy = 475;
		g.drawImage(tapiscarte.getImage(), 0, 0, null);
		nb = lesjoueurs[0].main.size();
		for (int v = 0; v < nb; v++) { 
		 Carte lacarte =lesjoueurs[0].main.get(v); 
		 g.drawImage(lacarte.getImage(), posx,posy, null); posx = posx + 40; 
		}
		// jeux joueur est
		posx = 480;
		posy = 125;
		nb = lesjoueurs[1].main.size();
		for (int v = 0; v < nb; v++) {
			g.drawImage(carteV.getImage(), posx, posy, null);
			posy = posy + 40;
		}
		// jeux joueur nord
		posx = 130;
		posy = 25;
		nb = lesjoueurs[2].main.size();
		for (int v = 0; v < nb; v++) {
			g.drawImage(carteH.getImage(), posx, posy, null);
			posx = posx + 40;
		}
		// jeux joueur ouest
		posx = 5;
		posy = 125;
		nb = lesjoueurs[3].main.size();
		for (int v = 0; v < nb; v++) {
			g.drawImage(carteV.getImage(), posx, posy, null);
			posy = posy + 40;
		}
		// affiche tableau resultat
		String affresult = new String("");
		Font f2 = new Font("Dialog", Font.BOLD, 12);
		g.setFont(f2);
		g.setColor(Color.cyan);
		affresult = ("" + totalEstOuest);
		g.drawString(affresult, 40, 600);
		affresult = ("" + totalNordSud);
		g.drawString(affresult, 560, 600);
		// affiche le joueur
		String affresult2 = new String("");
		affresult2 = ("" + joueurpris);
		g.drawString(affresult2, 20, 35);
		// affiche le gagnant
		g.setColor(Color.cyan);
		Font f3 = new Font("Dialog", Font.BOLD, 40);
		g.setFont(f3);
		g.drawString(gagnant, 50, 300);
		// paquet de carte
		if (carter != null)
			g.drawImage(carter.getImage(),240, 240, null);
		// jeux
		if (carteJouee[2] != null)
			g.drawImage(carteJouee[2].getImage(), 260, 130, null);
		if (carteJouee[3] != null)
			g.drawImage(carteJouee[3].getImage(), 146, 240, null);
		if (carteJouee[0] != null)
			g.drawImage(carteJouee[0].getImage(), 260, 340, null);
		if (carteJouee[1] != null)
			g.drawImage(carteJouee[1].getImage(), 380, 240, null);
		if (atout != null)
			g.drawImage(atout.getImage(), 500, 5, null);
	}


	void setReponse(int lareponse) {
		reponse = lareponse;
	}

}
