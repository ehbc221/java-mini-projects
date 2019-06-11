/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bash.jcouture.util;


import java.awt.Component;
import java.io.File;
import javax.swing.JFileChooser;


/**
 *
 * @author bash
 */
public class ImageFileHandler {

    public static File chooseImageFile(Component parent) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new ImageFilter());

        int returnVal = chooser.showOpenDialog(parent);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        }

        return null;

    }
    
    public static File getFileToSaveIn(Component parent){
        JFileChooser chooser = new JFileChooser();
       // chooser.setFileFilter(new XLSFilter());
        File file = null;

        int returnVal = chooser.showSaveDialog(parent);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file= chooser.getSelectedFile();
        }

        return file;
    }

   
    
  
    
}
