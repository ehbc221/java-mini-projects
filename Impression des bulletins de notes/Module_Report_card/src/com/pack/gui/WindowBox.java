package com.pack.gui;

import java.awt.Container;
import java.awt.Cursor;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;

/**
 *
 * @author Absolute
 */
public class WindowBox extends JWindow{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Object parent;
    JProgressBar progress = new JProgressBar();
    JButton wait = new JButton(new ImageIcon("."+File.separatorChar+"images"+File.separatorChar+"wait"+File.separatorChar+"0.png"));
    LabelGUI msg = new LabelGUI();
    boolean waitting = true;
    public WindowBox() {}
    
    public WindowBox(JFrame f) {
        super(f);
        parent = f;
    }
    
    public WindowBox(JDialog f) {
        super(f);
        parent = f;
    }
    
    public WindowBox setPanel(){
        Container c = this.getContentPane();
        c.setLayout(null);
        JPanel pane = new JPanel();
            wait.setFocusPainted(false);			
            wait.setBorderPainted(false);
            wait.setContentAreaFilled(false);
            pane.add(msg);
            pane.add(wait);
        pane.setBounds(50, 40, 200, 60);
        c.add(pane);
        this.setContentPane(c);
        this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        return this;
    }
    
    public WindowBox addProgressBar(){
        progress.setBounds(30, 110, 240, 20);
        this.getContentPane().add(progress);
        return this;
    }
    
    public WindowBox removeProgressBar(){
        this.getContentPane().remove(progress);
        return this;
    }
    
    public WindowBox setProgress(int value){
        progress.setValue(value);
        return this;
    }
    
    public int getProgress(){
        return progress.getValue();
    }
    
    public WindowBox setMsg(String message){
        msg.setTexte(message);
        return this;
    }
    
    public WindowBox display(){
        SwingUtils.centerFrame(this);
        this.setSize(300, 150);
        this.setVisible(true);
        waitting = true;
        return this.animate();
    }
    
    public WindowBox close(){
        this.setVisible(false);
        waitting = false;
        return this;
    }
    
    public WindowBox animate(){
        new Thread(){
            @Override
            public void run(){
                int img = 0;
                while(waitting){
                    try {
                        if(img<0||8<img) img = 0;
                        wait.setIcon(new ImageIcon("."+File.separatorChar+"images"+File.separatorChar+"wait"+File.separatorChar+""+img+".png"));
                        img++;
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(WindowBox.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }.start();
        return this;
    }
}

