package fr.ftp.util;

/*
Utilisation : 
JLoadWindow lw = new JLoadWindow();
while(!lw.isLoaded()) {}
// Après ces 2 lignes, vous pouvez mettre votre code

*/
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.io.IOException;

import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.SwingWorker;

@SuppressWarnings("serial")
public class JLoadWindow extends JWindow implements Runnable, 
													PropertyChangeListener {
	private String app;
	private JProgressBar jpb;
	private JLabel jl;
	public Task task;
	int totalProgress = 0;
	
	public JLoadWindow(String appName) {
		this.jl = new JLabel("Loading "+appName);
		app = appName;
		this.jpb = new JProgressBar(0, 100);
		jpb.setValue(0);
		jpb.setPreferredSize(new Dimension(350,15));
		jpb.setStringPainted(true);
		JPanel j = new JPanel(new FlowLayout());
		j.add(jl);
		this.add(j, BorderLayout.NORTH);
		this.add(jpb, BorderLayout.CENTER);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		task = new Task();
		task.addPropertyChangeListener(this);
		task.execute();
	}
	public JLoadWindow() {
		this.jl = new JLabel("Loading");
		app = "";
		this.jpb = new JProgressBar(0, 100);
		jpb.setValue(0);
		jpb.setPreferredSize(new Dimension(350,15));
		jpb.setStringPainted(true);
		JPanel j = new JPanel(new FlowLayout());
		j.add(jl);
		this.add(j, BorderLayout.NORTH);
		this.add(jpb, BorderLayout.CENTER);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		task = new Task();
		task.addPropertyChangeListener(this);
		task.execute();
	}
	public JLoadWindow(String appName, boolean determinated, Image back) throws IOException {
		this.jl = new JLabel("Loading "+appName);
		app = appName;
		this.jpb = new JProgressBar(0, 100);
		if(!determinated) {
			jpb.setIndeterminate(true);
		} else {
			jpb.setValue(0);
			jpb.setStringPainted(true);
		}
		this.setContentPane(new JBackground(back));
		JPanel j = new JPanel(new FlowLayout());
		j.add(jl);
		j.setOpaque(false);
		this.getContentPane().add(j, BorderLayout.NORTH);
		this.getContentPane().add(jpb, BorderLayout.CENTER);
		this.setSize(back.getWidth(this), back.getHeight(this));
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		task = new Task();
		task.addPropertyChangeListener(this);
		task.execute();
	}
	public JLoadWindow(String appName, boolean determinated, Color color) {
		this.jl = new JLabel("Loading "+appName);
		app = appName;
		this.jpb = new JProgressBar(0, 100);
		if(!determinated) {
			jpb.setIndeterminate(true);
		} else {
			jpb.setValue(0);
			jpb.setStringPainted(true);
		}
		this.getContentPane().setBackground(color);
		jpb.setBackground(color);
		JPanel j = new JPanel(new FlowLayout());
		j.add(jl);
		j.setBackground(color);
		this.getContentPane().add(j, BorderLayout.NORTH);
		this.getContentPane().add(jpb, BorderLayout.CENTER);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		task = new Task();
		task.addPropertyChangeListener(this);
		task.execute();
	}
	public JLoadWindow(String appName, boolean determinated) {
		this.jl = new JLabel("Loading "+appName);
		app = appName;
		this.jpb = new JProgressBar(0, 100);
		if(!determinated) {
			jpb.setIndeterminate(true);
		} else {
			jpb.setValue(0);
			jpb.setStringPainted(true);
		}
		JPanel j = new JPanel(new FlowLayout());
		j.add(jl);
		this.getContentPane().add(j, BorderLayout.NORTH);
		this.getContentPane().add(jpb, BorderLayout.CENTER);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		task = new Task();
		task.addPropertyChangeListener(this);
		task.execute();
	}
	public void setIndeterminated(boolean a) {
		jpb.setIndeterminate(a);
	}
	@Override
	public void run() {
		Random r = new Random();
		int progress = 0;
		
		while (progress < 100) {
			jpb.setValue(r.nextInt(10));
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	class Task extends SwingWorker<Void, Void> {
        /*
         * Main task. Executed in background thread.
         */
        @Override
        public Void doInBackground() throws InterruptedException {
            Random random = new Random();
            int progress = 0;
            //Initialize progress property.
            setProgress(0);
            while (progress < 100) {
                //Sleep for up to one second.
                try {
                    Thread.sleep(random.nextInt(250));
                } catch (InterruptedException ignore) {}
                //Make random progress.
                progress += random.nextInt(10);
                setProgress(Math.min(progress, 100));
            }
            return null;
        }

        /*
         * Executed in event dispatching thread
         */
        @Override
        public void done() {
            setCursor(null); //turn off the wait cursor
            if(!app.equals("")){
            	jpb.setString(app+" will start");
            } else {
            	jpb.setString("Your app will start !");
            }
        }
    }
	public void propertyChange(PropertyChangeEvent evt) {
        if ("progress" == evt.getPropertyName()) {
            int progress = (Integer) evt.getNewValue();
            totalProgress = progress;
            jpb.setValue(progress);
        } 
    }
	
	class JBackground extends JPanel {
		private Image backgroundImage;

		  // Some code to initialize the background image.
		  // Here, we use the constructor to load the image. This
		  // can vary depending on the use case of the panel.
		  public JBackground(Image fileName) throws IOException {
		    backgroundImage = fileName;
		  }

		  public void paintComponent(Graphics g) {
		    super.paintComponent(g);

		    // Draw the background image.
		    g.drawImage(backgroundImage, 0, 0, this);
		  }
	}
	public boolean isLoaded() throws InterruptedException {
		// TODO Auto-generated method stub
		Random r = new Random();
		if(this.totalProgress == 100) {
			setIndeterminated(true);
			Thread.sleep(r.nextInt(3000));
			dispose();
			return true;
		} else if (this.totalProgress < 100) {
			return false;
		}
		return false;
	}
}  