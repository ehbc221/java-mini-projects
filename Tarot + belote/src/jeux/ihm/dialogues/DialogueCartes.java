package jeux.ihm.dialogues;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import jeux.cartes.*;
import jeux.ihm.*;
import jeux.ihm.etiquettes.*;
import jeux.parties.*;
/**
 * Classe utilisee pour une boite de dialogue annoncant la belote rebelote
 */
public class DialogueCartes extends Dialogue {
	private static final long serialVersionUID = 1L;
	private CarteBelote carte;
	private static final String conseil="Conseil";
	private static final String annuler="Annuler";
	public DialogueCartes(String titre,Fenetre fenetre) {
		super(titre,fenetre,true);
		setLocationRelativeTo(fenetre);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
	}
	public void setDialogue()
	{
		PartieBelote belote=(PartieBelote)((Fenetre)getOwner()).getPartie();
		Container container=new Container();
		container.setLayout(new BorderLayout());
		JPanel panneau=new JPanel();
		panneau.setLayout(new GridLayout(0,1));
		JPanel sous_panneau=new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
		CarteGraphique cg=new CarteGraphique(new CarteBelote((byte)14,belote.getCarteAppelee().couleur()),JLabel.RIGHT,false);
		cg.setPreferredSize(new Dimension(100,150));
		cg.addMouseListener(new Ecouteur((CarteBelote) cg.getCarte()));
		sous_panneau.add(cg);
		cg=new CarteGraphique(new CarteBelote((byte)13,belote.getCarteAppelee().couleur()),JLabel.RIGHT,true);
		cg.setPreferredSize(new Dimension(25,150));
		cg.addMouseListener(new Ecouteur((CarteBelote) cg.getCarte()));
		sous_panneau.add(cg);
		panneau.add(sous_panneau);
		container.add(panneau,BorderLayout.CENTER);
		panneau=new JPanel();
		JButton bouton=new JButton(conseil);
		bouton.addActionListener(new EcouteurBouton());
		panneau.add(bouton);
		bouton=new JButton(annuler);
		bouton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dispose();
			}
		});
		panneau.add(bouton);
		container.add(panneau,BorderLayout.SOUTH);
		setContentPane(container);
		pack();
	}
	private void conseil(PartieBelote belote)
	{
		String[] raison=new String[1];
		JOptionPane.showMessageDialog(this,"Je vous conseille de jouer "+belote.strategieJeuCarteUnique(raison),"Conseil de jouerie",JOptionPane.INFORMATION_MESSAGE);
	}
	public CarteBelote getCarte()
	{
		setVisible(true);
		return carte;
	}
	private class Ecouteur extends MouseAdapter{
		private CarteBelote carteb;
		Ecouteur(CarteBelote pcarte) {
			carteb=pcarte;
		}
		/**Lorsque l'utilisateur clique sur une carte la carte est jouee puis la boite de dialogue est fermee*/
		public void mousePressed(MouseEvent e)
		{
			carte=carteb;
			dispose();
		}
	}
	private class EcouteurBouton implements ActionListener{
		/**Lorsque l'utilisateur clique sur conseil, une fenetre de conseil apparait*/
		public void actionPerformed(ActionEvent e) {
			conseil((PartieBelote)((Fenetre)getOwner()).getPartie());
		}
	}
}