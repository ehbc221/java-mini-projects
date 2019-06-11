import java.sql.*;
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.AbstractButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import java.awt.Font;
import java.awt.Color;

import javax.swing.JTable;
import javax.swing.JFormattedTextField;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JScrollBar;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTextField;
import javax.swing.JRadioButton;


public class Coord extends JFrame {
	
	ManipDB manip=new ManipDB();
	private JPanel contentPane;
	private JLabel numeroCaissier;
	public JLabel numvalue;
	private JLabel nomCaissier;
	private JLabel nomvalue;
	private JButton achatButton;
	private JLabel achatLabel;
	private JScrollPane jScrollPane1 ;
	private JTable table;
	private JScrollPane scrollPane;
	private JTextField valueCode;
	private JScrollPane scrollPane_1;
	private JTable table_1;
	private JLabel nomArticle;
	private JLabel nbArticle;
	private JTextField valueNom;
	private JTextField valueNb;
	private JButton validerButton;
	private JLabel codeLabel;
	private JButton OkButton ;
	private JRadioButton lotCheck ;
	private JLabel choixLabel;
	private JRadioButton articleCheck;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Coord frame = new Coord();
					frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}

	/**
	 * Create the frame.
	 */
	public Coord() {
		setTitle("Caisse");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 662, 460);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		numeroCaissier = new JLabel("Numero de caissier:");
		numeroCaissier.setForeground(Color.BLUE);
		numeroCaissier.setFont(new Font("Tahoma", Font.BOLD, 12));
		numeroCaissier.setBounds(0, 0, 124, 15);
		contentPane.add(numeroCaissier);
		
		numvalue = new JLabel("");
		numvalue.setFont(new Font("Tahoma", Font.PLAIN, 12));
		numvalue.setBounds(116, 1, 116, 14);
		contentPane.add(numvalue);
		
		nomCaissier = new JLabel("Nom de caissier:");
		nomCaissier.setForeground(Color.BLUE);
		nomCaissier.setFont(new Font("Tahoma", Font.BOLD, 12));
		nomCaissier.setBounds(319, 0, 102, 14);
		contentPane.add(nomCaissier);
		
		nomvalue = new JLabel("");
		nomvalue.setFont(new Font("Tahoma", Font.PLAIN, 12));
		nomvalue.setBounds(431, 0, 116, 14);
		contentPane.add(nomvalue);
		
		achatButton = new JButton("Achat Produit");
		achatButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				Object source=ev.getSource();
				if(source==achatButton){
					choixLabel.setVisible(true);
					articleCheck.setVisible(true);
					lotCheck.setVisible(true);
				}
			}
		});
		achatButton.setFont(new Font("Tahoma", Font.BOLD, 11));
		achatButton.setBounds(265, 103, 115, 23);
		contentPane.add(achatButton);
		
		achatLabel = new JLabel(" Si vous voulez acheter un produit ");
		achatLabel.setForeground(Color.BLUE);
		achatLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		achatLabel.setBounds(0, 106, 232, 14);
		contentPane.add(achatLabel);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 26, 646, 56);
		contentPane.add(scrollPane);
		
		table = new JTable();
		table.setRowHeight(30);
		scrollPane.setViewportView(table);
		
		codeLabel = new JLabel(" Saisir le code de Produit");
		codeLabel.setForeground(Color.BLUE);
		codeLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		codeLabel.setBounds(0, 289, 184, 14);
		contentPane.add(codeLabel);
		
		valueCode = new JTextField();
		valueCode.setBounds(194, 287, 116, 20);
		contentPane.add(valueCode);
		valueCode.setColumns(10);
		
		OkButton = new JButton("OK");
		OkButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String prix=valueCode.getText();
				int code=Integer.parseInt(prix);
				table_1.setModel(manip.afficherProduit(code));
			}
		});
		OkButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		OkButton.setBounds(319, 285, 68, 23);
		contentPane.add(OkButton);
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(0, 335, 646, 56);
		contentPane.add(scrollPane_1);
		
		table_1 = new JTable();
		table_1.setRowHeight(30);
		scrollPane_1.setViewportView(table_1);
		
		choixLabel = new JLabel(" Veuillez choisir");
		choixLabel.setForeground(Color.BLUE);
		choixLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		choixLabel.setBounds(10, 137, 104, 14);
		contentPane.add(choixLabel);
		
		articleCheck = new JRadioButton("Article");
		articleCheck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				AbstractButton abstractButton = (AbstractButton)evt.getSource();
				boolean selected=abstractButton.getModel().isSelected();
				if(selected){
					nomArticle.setVisible(true);
					nbArticle.setVisible(true);
					valueNom.setVisible(true);
					valueNb.setVisible(true);
					validerButton.setVisible(true);
				}
			}
		});
		articleCheck.setBounds(120, 134, 64, 23);
		contentPane.add(articleCheck);
		
		lotCheck = new JRadioButton("Lot");
		lotCheck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AbstractButton abstractButton = (AbstractButton)e.getSource();
				boolean selected=abstractButton.getModel().isSelected();
				if(selected){
					nomArticle.setVisible(true);
					nbArticle.setVisible(true);
					valueNom.setVisible(true);
					valueNb.setVisible(true);
					validerButton.setVisible(true);
				}
					
			}
		});
		lotCheck.setBounds(205, 134, 57, 23);
		contentPane.add(lotCheck);
		
		nomArticle = new JLabel("Nom");
		nomArticle.setBounds(20, 162, 46, 14);
		contentPane.add(nomArticle);
		
		nbArticle = new JLabel("Nombre");
		nbArticle.setBounds(20, 199, 46, 14);
		contentPane.add(nbArticle);
		
		valueNom = new JTextField();
		valueNom.setBounds(99, 162, 86, 20);
		contentPane.add(valueNom);
		valueNom.setColumns(10);
		
		valueNb = new JTextField();
		valueNb.setBounds(98, 196, 86, 20);
		contentPane.add(valueNb);
		valueNb.setColumns(10);
		
		validerButton = new JButton("Valider");
		validerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object source=e.getSource();
				if(source==articleCheck){
					String nom=valueNom.getText();
					String nbst=valueNb.getText();
					int nb=Integer.parseInt(nbst);
					manip.acheterArticle(nb,nom);
					
				}
				if(source==lotCheck)
				{
					String nom=valueNom.getText();
					String nbst=valueNb.getText();
					int nb=Integer.parseInt(nbst);
					manip.acheterLot(nb,nom);
				}
			}
		});
		validerButton.setBounds(99, 227, 85, 23);
		contentPane.add(validerButton);
		choixLabel.setVisible(false);
		articleCheck.setVisible(false);
		lotCheck.setVisible(false);
		nomArticle.setVisible(false);
		nbArticle.setVisible(false);
		valueNom.setVisible(false);
		valueNb.setVisible(false);
		validerButton.setVisible(false);
	}
	public void affiche(int id,String pass){
			
			try{
				ResultSet result=manip.afficherCaissier(id);
				while(result.next()){
					int idcaiss=result.getInt("idCaissier");
					System.out.println(idcaiss);
					String idstr=String.valueOf(idcaiss);
					String nomCai=result.getString("nomCaissier");
					String password=result.getString("passwordCaissier");
					if((idcaiss==id)&&(pass.equals(password)))
					{
						numvalue.setText(idstr);
						nomvalue.setText(nomCai);
					}
					else{
						JOptionPane.showMessageDialog(null,"login or password incorrect","Message d’avertissement",JOptionPane.ERROR_MESSAGE);
					}
				}
			
			}
			catch(Exception e){
			e.printStackTrace();
			
		}
	}
	public void affiche(){
		try{
			table.setModel(manip.afficherStock());
		}
		catch(Exception e){
			e.printStackTrace();
		
		}
	}
}

