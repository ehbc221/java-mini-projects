package fr.ftp.model;

import java.awt.Component;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

@SuppressWarnings("serial")
public class ExplorerTableRenderer extends DefaultTableCellRenderer {
		 
		  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		    //Si la valeur de la cellule est un JButton, on transtype cette valeur
		    if (value instanceof Component)
		      return (Component) value;
		    else if (value instanceof String) {
		    	return new JLabel((String)value);
		    }
		    else if(value instanceof Image) {
		    	JLabel jp = new JLabel();
		    	jp.setIcon(new ImageIcon((Image)value));
		    	return jp;
		    }
		    
		    else {
		    	return this;
		    }
		  }
		
}
