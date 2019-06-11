package fr.ftp.view;

import java.awt.GridLayout;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class FTPTaskWindow extends JFrame {

	private static final long serialVersionUID = 6018209677648052040L;

	public FTPTaskWindow(LinkedList<String> taskDone) {
		// TODO Auto-generated constructor stub
		JPanel jp = new JPanel(new GridLayout(taskDone.size(),1));
		Iterator<String> it = taskDone.iterator();
		while(it.hasNext()) {
			jp.add(new JLabel(it.next()));
		}
		this.setSize(200,200);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.add(new JScrollPane(jp));
		this.setVisible(true);
	}

}
