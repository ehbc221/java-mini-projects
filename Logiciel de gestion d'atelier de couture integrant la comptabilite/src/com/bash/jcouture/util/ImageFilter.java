/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bash.jcouture.util;

import java.io.File;
import javax.swing.filechooser.FileFilter;



/**
 *
 * @author bash
 */
public class ImageFilter extends FileFilter{

      public boolean accept(File f) {
            return f.isDirectory() || f.getName().endsWith(".png")||f.getName().endsWith(".jpg")||f.getName().endsWith(".gif");
        }
        
        public String getDescription() {
            return "Fichiers image";
        }
    }


