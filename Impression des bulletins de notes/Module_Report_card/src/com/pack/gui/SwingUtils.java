package com.pack.gui;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JWindow;

/**
 * Provide utility methods for Swing Components. 
 * 
 * @author Donald
 */
public class SwingUtils {
 private static DisplayMode mode;
 private static GraphicsDevice device;
 
 /**
  * Private constructor, this class isn't instanciable. 
  *
  */
 private SwingUtils(){
  super();
 }
 
 /**
  * Load the display informations for this computer. 
  *
  */
 private static void loadDisplayInfos(){
  GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
  device = gEnv.getDefaultScreenDevice();
  mode = device.getDisplayMode();
 }
 
 /**
  * Return the insets of the screen. 
  * 
  * @return The insets
  */
 private static Insets getInsets(){
  return Toolkit.getDefaultToolkit().getScreenInsets(device.getDefaultConfiguration());
 }
 
 /**
  * Center a frame on the screen. 
  * 
  * @param frame The frame to be centered
  */
 public static void centerFrame(JFrame frame){
  if(mode == null){
   loadDisplayInfos();
  }
  
  
  frame.setLocation( (getWidth() - frame.getWidth()) / 2, 
        (getHeight() - frame.getHeight()) / 2);
 }
 
 public static void centerFrame(JWindow frame){
	  if(mode == null){
	   loadDisplayInfos();
	  }
	  
	  
	  frame.setLocation( (getWidth() -350- frame.getWidth()) / 2, 
	        (getHeight() -200- frame.getHeight()) / 2);
	 }

 
 public static void centerFrame(JDialog frame){
	  if(mode == null){
	   loadDisplayInfos();
	  }
	  frame.setLocation( (getWidth() - frame.getWidth()) / 10, 
		        (getHeight() - frame.getHeight()) / 16);
}
 /**
  * Return the height of the screen. 
  * 
  * @return The height
  */
 public static int getHeight(){
  if(mode == null){
   loadDisplayInfos();
  }
  
  return mode.getHeight() - getInsets().bottom - getInsets().top;
 }
 
 /**
  * Return the widht of the screen. 
  * 
  * @return The width
  */
 public static int getWidth(){
  if(mode == null){
   loadDisplayInfos();
  }
  
  return mode.getWidth() - getInsets().left - getInsets().right;
 }
}

