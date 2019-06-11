package fr.ftp.view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.jdom2.JDOMException;

import fr.ftp.control.FTPLoginKeyListener;
import fr.ftp.control.FTPLoginSelectionListener;

public class FTPLoginWindow extends JFrame {

	private static final long serialVersionUID = 5718537493272319713L;
	public String[] connInfo = {"", "", "", ""};
	private JLabel jl = new JLabel("Host :");
	private JLabel jl1 = new JLabel("User :");
	private JLabel jl2 = new JLabel("Pass :");
	private JButton jb = new JButton("Login");
	private JButton jb1 = new JButton("Quit");
	private JTextField jtf = new JTextField("");
	private JTextField jtf1 = new JTextField("");
	private JPasswordField jpf = new JPasswordField("");
	
	public FTPLoginWindow() throws IOException {
		this.setTitle("Login");
		this.setIconImage(ImageIO.read(ClassLoader.getSystemResourceAsStream("fr/ftp/rsc/icon_win32.jpg")));
		this.setLayout(new GridLayout(4,2));
		this.getContentPane().add(jl);
		this.getContentPane().add(jtf);
		this.getContentPane().add(jl1);
		this.getContentPane().add(jtf1);
		this.getContentPane().add(jl2);
		this.getContentPane().add(jpf);
		this.getContentPane().add(jb);
		this.getContentPane().add(jb1);
		setListeners();
		setMenu();
		this.setSize(300,170);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	private void setListeners() {
		// TODO Auto-generated method stub
		jpf.addKeyListener(new FTPLoginKeyListener() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER && !(jtf.getText().equals("")) && 
						!(jtf1.getText().equals("")) && !(String.valueOf(jpf.getPassword()).equals(""))) {
					login();
				}
			}
		});
		jb.addActionListener(new FTPLoginSelectionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!(jtf.getText().equals("")) && !(jtf1.getText().equals("")) && !(String.valueOf(jpf.getPassword()).equals(""))) {
					login();
				}
			}
		});
		jb1.addActionListener(new FTPLoginSelectionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
	}

	protected void login() {
		// TODO Auto-generated method stub
		connInfo[0] = jtf.getText();
		connInfo[1] = jtf1.getText();
		connInfo[2] = String.valueOf(jpf.getPassword());
		try {
			ConnectionManager.connect(connInfo);
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.dispose();
	}
	
	private void setMenu() {
		JMenuBar jmb = new JMenuBar();
		JMenu jm = new JMenu("Connections");
		ArrayList<String[]> infos = null;
		try {
			infos = ConnectionManager.getConnectInfos();
		} catch (Exception e) {
			e.printStackTrace();
		}
		for(int i = 0;i<infos.size();i++) {
			JMenuItem p = new JMenuItem(infos.get(i)[0]+"@"+infos.get(i)[1]);
			final int j = i;
			final ArrayList<String[]> infoss = infos;
			p.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					FTPLoginWindow.this.jtf.setText(infoss.get(j)[0]);
					FTPLoginWindow.this.jtf1.setText(infoss.get(j)[1]);
					FTPLoginWindow.this.jpf.setText(infoss.get(j)[2]);
				}
			});
			jm.add(p);
		}
		jmb.add(jm);
		this.setJMenuBar(jmb);
	}
}
