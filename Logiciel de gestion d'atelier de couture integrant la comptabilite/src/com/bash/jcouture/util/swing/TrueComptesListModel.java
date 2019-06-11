/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bash.jcouture.util.swing;

import com.bash.jcouture.controler.CompteJpaController;
import com.bash.jcouture.entities.model.Compte;
import java.util.List;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

/**
 *
 * @author bashizip
 */
public class TrueComptesListModel  implements ListModel{

    List<Compte> elts=new CompteJpaController().findCompteEntities();
     public int getSize() {
       return elts.size();
    }

    public Object getElementAt(int index) {
      return elts.get(index).getIdcompte()+"."+elts.get(index).getLibele();
    }

    public void addListDataListener(ListDataListener l) {

    }

    public void removeListDataListener(ListDataListener l) {

    }
}
