/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bash.jcouture.util.swing;

import com.bash.jcouture.util.ComptesBuilder;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

/**
 *
 * @author bashizip
 */
public class ComptesListModel implements ListModel{

    String[] elts=ComptesBuilder.comptes;
    public int getSize() {
       return elts.length ;
    }

    public Object getElementAt(int index) {
      return elts[index];
    }

    public void addListDataListener(ListDataListener l) {
       
    }

    public void removeListDataListener(ListDataListener l) {
      
    }

}
