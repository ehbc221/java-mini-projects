import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JCheckBox;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.Color;
import java.util.prefs.Preferences;


public class Authentication extends JFrame {
	
	private JPanel contentPane;
	private JLabel authentication; 
	private JLabel idCaissier;
	private JTextField loginField;
	private JLabel passwordCaissier;
	private JPasswordField passwordField;
	private JCheckBox rememberme;
	private JButton loginCaissier;
	private Preferences prefs;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Authentication frame = new Authentication();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public void close(){
		this.dispose();
	}
	
	/**
	 * Create the frame.
	 */
	public Authentication() {
		setTitle("Authentication");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 361, 261);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		authentication = new JLabel("Authentication");
		authentication.setForeground(Color.BLUE);
		authentication.setFont(new Font("Tahoma", Font.BOLD, 24));
		authentication.setBounds(88, 11, 183, 23);
		contentPane.add(authentication);
		
		idCaissier = new JLabel("Login");
		idCaissier.setFont(new Font("Tahoma", Font.BOLD, 14));
		idCaissier.setBounds(27, 78, 52, 17);
		contentPane.add(idCaissier);
		
		passwordCaissier = new JLabel("Password");
		passwordCaissier.setFont(new Font("Tahoma", Font.BOLD, 14));
		passwordCaissier.setBounds(27, 118, 75, 14);
		contentPane.add(passwordCaissier);
		
		loginField = new JTextField();
		loginField.setBounds(116, 75, 123, 20);
		contentPane.add(loginField);
		loginField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(116, 112, 123, 20);
		contentPane.add(passwordField);
		
		rememberme = new JCheckBox("Remember me");
		rememberme.setBounds(116, 141, 123, 23);
		contentPane.add(rememberme);
		
		prefs=Preferences.userNodeForPackage(this.getClass());
		final String PREF_NAME = "remember";
		loginCaissier = new JButton("Login");
		
		loginCaissier.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				ManipDB manip=new ManipDB();
				Object source= evt.getSource();
				String valuelogin = null;
				int id=0;
				if(source==loginCaissier){
					
					valuelogin=loginField.getText();
					id=Integer.parseInt(valuelogin);
					char[] passChar=passwordField.getPassword();
					String pass=new String(passChar);
					if((!valuelogin.equals(""))&&(!pass.equals(""))){
						if((manip.verifier(id,pass))==true){
							close();
							Coord cord=new Coord();
							cord.affiche(id,pass);
							cord.affiche();
							cord.setVisible(true);
						}
						else
							JOptionPane.showMessageDialog(null,"login or password inncorrect","Alert Message",JOptionPane.ERROR_MESSAGE);
				}	
				else
					JOptionPane.showMessageDialog(null,"login or password empty","Alert Message",JOptionPane.ERROR_MESSAGE);		
			
				
				if (true==rememberme.isSelected())
					prefs.put(PREF_NAME, valuelogin);
				
				else
					prefs.remove(PREF_NAME);
				
				
			}
		}
			
		});
		loginCaissier.setFont(new Font("Tahoma", Font.BOLD, 14));
		loginCaissier.setBounds(116, 171, 89, 23);
		contentPane.add(loginCaissier);
	}
	
}
