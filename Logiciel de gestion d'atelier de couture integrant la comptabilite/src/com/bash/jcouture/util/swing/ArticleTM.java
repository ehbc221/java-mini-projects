/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bash.jcouture.util.swing;

import com.bash.jcouture.entities.model.Article;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author bashizip
 */
public class ArticleTM extends AbstractTableModel {

    private List<Article> data;
    private String[] colones;

    public ArticleTM(List<Article> data, String[] colones) {
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

        Article c = data.get(rowIndex);

        Object toReturn = null;
        switch (columnIndex) {
            case 0:
                toReturn = c.getIdarticle();
                break;
            case 1:
                toReturn = c.getClient().getIdclient();
                break;
            case 2:
                toReturn = c.getTypeArticle().getNom();
                break;
            case 3:
                toReturn = c.getModele().getNom();
                break;
            case 4:
                toReturn = c.getFacture();
                break;
            case 5:
                toReturn = c.getDateEntree();
                break;
            case 6:
                toReturn = c.getDateSortie();
                break;
            case 7:
                toReturn = c.getEtat();
                break;
            case 8:
                toReturn = c.getCommentaire();
                break;
            case 9:
                toReturn = c.getPhoto();
                break;
            case 10:
                toReturn = c.getMesure().getIdmesure();
                break;
            default:
                toReturn =c.getIdarticle();

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
