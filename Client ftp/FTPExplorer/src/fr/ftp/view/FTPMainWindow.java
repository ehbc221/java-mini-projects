package fr.ftp.view;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import fr.ftp.FTP;
import fr.ftp.control.FTPMainWindowMenuActionListener;
import fr.ftp.util.FTPInfoSaver;


public class FTPMainWindow extends JFrame {
	

	private static final long serialVersionUID = -5898736707134296611L;
	private FTPClientServerDialog serverDialog = new FTPClientServerDialog();
	private JPanel filesExplorer = new JPanel();
	private JPanel FTPInfos = new JPanel(new GridLayout(1,2));
	private FTPTaskExplorer taskExplo = new FTPTaskExplorer();
	
	public FTPMainWindow() throws IOException {
		initComponents();
	}
	
	private JMenuBar createMenu() {
		JMenuBar jmb = new JMenuBar();
		JMenu jm = new JMenu("Fichier");
		JMenu jm1 = new JMenu("Help");
		JMenuItem jmi1 = new JMenuItem("Save");
		jmi1.addActionListener(new FTPMainWindowMenuActionListener() {
			public void actionPerformed(ActionEvent e) {
				new FTPInfoSaver("save", serverDialog);
			}
		});
		JMenuItem jmi2 = new JMenuItem("Quit");
		JMenuItem jmi4 = new JMenuItem("Task done");
		jmi4.addActionListener(new FTPMainWindowMenuActionListener() {
			public void actionPerformed(ActionEvent e) {
				new FTPTaskWindow(FTP.taskDone);
			}
		});
		jmi2.addActionListener(new FTPMainWindowMenuActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					FTP.conn.logout();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.exit(0);
			}
		});
		JMenuItem jmi3 = new JMenuItem("Open help");
		jm1.add(jmi3);
		jm.add(jmi1);
		jm.add(jmi4);
		jm.add(jmi2);
		jmb.add(jm);
		jmb.add(jm1);
		return  jmb;
	}
	
	private void initComponents() throws IOException {
		this.setLayout(new GridLayout(2,1));
		this.setJMenuBar(createMenu());
		serverDialog.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		taskExplo.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		FTPInfos.setSize(1000,300);
		FTPInfos.add(new JScrollPane(serverDialog));
		FTPInfos.add(new JScrollPane(taskExplo));
		filesExplorer.setSize(1000,300);
		filesExplorer.add(new JLabel("Please wait for connect"));
		this.getContentPane().add(FTPInfos);
		this.getContentPane().add(filesExplorer);
		this.setSize(1000,600);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("FTPExplorer - "+FTP.host);
		this.setResizable(false);
		this.setIconImage(ImageIO.read(ClassLoader.getSystemResourceAsStream("fr/ftp/rsc/icon_win32.jpg")));
		this.setVisible(true);
		
		
	}
	
	public FTPClientServerDialog getServerDialog() {
		return serverDialog;
	}
	
	public FTPTaskExplorer getTaskExplorer() {
		return taskExplo;
	}
	
	public void initExplorers() throws IOException {
		filesExplorer.removeAll();
		filesExplorer.setLayout(new GridLayout(1,2));
		filesExplorer.add(new JScrollPane(new LocalExplorer()));
		filesExplorer.add(new JScrollPane(new RemoteExplorer()));
		filesExplorer.revalidate();
		this.revalidate();
	}
}
