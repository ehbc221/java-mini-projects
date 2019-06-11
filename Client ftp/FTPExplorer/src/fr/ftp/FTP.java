package fr.ftp;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import fr.ftp.model.Downloader;
import fr.ftp.model.FTPConnection;
import fr.ftp.model.Uploader;
import fr.ftp.util.JLoadWindow;
import fr.ftp.view.FTPLoginWindow;
import fr.ftp.view.FTPMainWindow;

public class FTP{

	public static FTPConnection conn = null;
	public static Uploader uploader;
	public static Downloader downloader;
	private FTPMainWindow window = null;
	private FTPLoginWindow logWindow;
	private String[] connInfos;
	private JLoadWindow jl;
	public static String host;
	public static LinkedList<String> taskDone = new LinkedList<String>();

	public FTP() throws UnknownHostException, IOException, InterruptedException {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		jl = new JLoadWindow("FTPExplorer", true, ImageIO.read(ClassLoader.getSystemResourceAsStream("fr/ftp/rsc/icon.jpg")));
		while(!jl.isLoaded()){}
		logWindow = new FTPLoginWindow();
		while(logWindow.connInfo[2].equals("")) {}
		connInfos = logWindow.connInfo;
		host = connInfos[0];
		instantiateWindow();
		window.getServerDialog().add(new JLabel("Connecting to "+host));
		createConnection();
		downloader = new Downloader(conn);
		uploader = new Uploader(conn);
	}

	private void instantiateWindow() throws IOException {
		// TODO Auto-generated method stub
		window = new FTPMainWindow();
	}

	private void createConnection() throws UnknownHostException, IOException {
		// TODO Auto-generated method stub
		conn = new FTPConnection(window);
		conn.connect(connInfos[0]);
		conn.login(connInfos[1], connInfos[2]);
		window.initExplorers();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			new FTP();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
