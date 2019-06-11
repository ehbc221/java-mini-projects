package rahim.kherrata.connexion;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.html.HTMLEditorKit;

public class Htmlf extends JFrame{
	private JEditorPane editorPane, apercu;
	private JTabbedPane onglet = new JTabbedPane();
	public Htmlf(){
		this.setSize(600, 400);
		this.setTitle("Conteneur �ditable");
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		editorPane = new JEditorPane();
		//editorPane.setText(" <HTML><HEAD></HEAD><BODY></BODY></HTML> ");
		apercu = new JEditorPane();
		apercu.setEditable(false);
		onglet.addTab("Editeur HTML", new JScrollPane(editorPane));
		onglet.addTab("Aper�u", new JScrollPane(apercu));
		onglet.addChangeListener(new ChangeListener(){
		public void stateChanged(ChangeEvent e) {
		FileWriter fw = null;
		try {
		fw = new FileWriter(new File("fichhtml/fichierHT.html"));
		//fw.write(editorPane.getText());
		fw.close();
		} catch (FileNotFoundException e1) {
		e1.printStackTrace();
		} catch (IOException e1) {
		e1.printStackTrace();
		}
		try {
		File file = new File("fichhtml/fichierHT.html");
		apercu.setEditorKit(new HTMLEditorKit());
		apercu.setPage(file.toURL());
		} catch (IOException e1) {
		e1.printStackTrace();
		}
		}
		});
		this.getContentPane().add(onglet, BorderLayout.CENTER);
		this.setVisible(true);
		}
}
