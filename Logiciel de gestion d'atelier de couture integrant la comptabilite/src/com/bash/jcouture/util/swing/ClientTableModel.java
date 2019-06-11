/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bash.jcouture.util.swing;

import com.bash.jcouture.entities.model.Client;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author bashizip
 */
public class ClientTableModel extends AbstractTableModel {

    private List<Client> data;
    private String[] colones;

    public ClientTableModel(List<Client> data, String[] colones) {
        this.data = data;
        this.colones = colones;
    }

    public int getRowCount() {
        return data.size();
    }

    public int getColumnCount() {
        return colones.length;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {

        Client c = data.get(rowIndex);

        Object toReturn = null;
        switch (columnIndex) {
            case 0:
                toReturn = c.getIdclient();
                break;
            case 1:
                toReturn = c.getNom();
                break;
            case 2:
                toReturn = c.getPostnom();
                break;
            case 3:
                toReturn = c.getPrenom();
                break;
            case 4:
                toReturn = c.getAdresse();
                break;
            case 5:
                toReturn = c.getTelephone();
                break;
            case 6:
                toReturn = c.getPhoto();
                break;
            default:
                toReturn = c.getIdclient();

        }
        return toReturn;

    }

    @Override
    public String getColumnName(int column) {
        return colones[column];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}
