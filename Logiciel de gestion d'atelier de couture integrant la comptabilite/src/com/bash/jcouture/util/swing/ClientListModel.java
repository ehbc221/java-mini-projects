/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bash.jcouture.util.swing;

import com.bash.jcouture.entities.model.Client;
import java.util.List;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

/**
 *
 * @author bashizip
 */
public class ClientListModel implements ListModel {

    private List<Client> clist;

    public ClientListModel(List<Client> clist) {

        this.clist = clist;
    }

    public int getSize() {
        return clist.size();
    }

    public Object getElementAt(int index) {
        return clist.get(index).getIdclient();
    }

    public void addListDataListener(ListDataListener l) {
    }

    public void removeListDataListener(ListDataListener l) {
    }
}
