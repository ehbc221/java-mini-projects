package com.pack.gui;

import java.awt.Color;
import java.awt.Font;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;

/**
 *
 * @author Absolute
 */
public class LabelGUI extends JLabel{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	LabelGUI localLabel;
    public LabelGUI() {}
    
    public LabelGUI(String msg) {
        super(msg);
    }
    
    public LabelGUI(String msg, int pos) {
        super(msg,pos);
    }
    
    public LabelGUI setFont(int police){
        this.setFont(new Font(Font.MONOSPACED, Font.BOLD, police));
        return this;
    }
    
    public LabelGUI setColor(String color){
        if(color.equalsIgnoreCase("red"))
            this.setForeground(Color.red);
        else if(color.equalsIgnoreCase("blue"))
            this.setForeground(Color.blue);
        return this;
    }
    
    public LabelGUI setFontColor(String color){
        if(color.equalsIgnoreCase("gray"))
            this.setBackground(Color.gray);
        else if(color.equalsIgnoreCase("blue"))
            this.setBackground(Color.blue);
        else if(color.equalsIgnoreCase("red"))
            this.setBackground(Color.red);
        if(color.equalsIgnoreCase("black"))
            this.setBackground(Color.black);
        if(color.equalsIgnoreCase("white"))
            this.setBackground(Color.white);
        this.repaint();
        return this;
    }
    public LabelGUI setOpacity(boolean bool){
        this.setOpaque(bool);
        return this;
    }
    
    public LabelGUI setTexte(String text){
        this.setText(text);
        return this;
    }
    
    public LabelGUI animate(){
        localLabel = this;
        new Thread(){
            @Override
            public void run(){
                while(true){
                    try {
                        localLabel.setOpacity(false).setFontColor("gray").repaint();
                        Thread.sleep(1000);
                        localLabel.setOpacity(true).repaint();
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(LabelGUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }.start();
        return this;
    }
}

