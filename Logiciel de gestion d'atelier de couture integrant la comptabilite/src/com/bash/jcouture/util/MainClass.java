/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bash.jcouture.util;

/**
 *
 * @author bashizip
 */
//
import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

public class MainClass extends JFrame {

    MainClass(String s) {
        super(s);
        ListModel lm = new StaticListModel();
        JList list = new JList();
        list.setModel(lm);
        list.setCellRenderer(new MyCellRenderer());
        getContentPane().add(new JScrollPane(list));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static void main(String[] s) {
        MainClass l = new MainClass("ListModel");
        l.pack();
        l.setVisible(true);
    }

    class MyCellRenderer extends JLabel implements ListCellRenderer {

        public Component getListCellRendererComponent(JList list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            Component c = null;
            if (value == null) {
                c = new JLabel("(null)");
            } else if (value instanceof Component) {
                c = (Component) value;
            } else {
                c = new JLabel(value.toString());
            }

            if (isSelected) {
                c.setBackground(list.getSelectionBackground());
                c.setForeground(list.getSelectionForeground());
            } else {
                c.setBackground(list.getBackground());
                c.setForeground(list.getForeground());
            }

            if (c instanceof JComponent) {
                ((JComponent) c).setOpaque(true);
            }

            return c;
        }
    }

    class StaticListModel implements ListModel {

        private final Object[] data = {"Hello", new Object(), new java.util.Date(),
            new JLabel("Hello world!"), this,};

        public Object getElementAt(int index) {
            return data[index];
        }

        public int getSize() {
            return data.length;
        }

        public void addListDataListener(ListDataListener ldl) {
        }

        public void removeListDataListener(ListDataListener l) {
        }
    }
}
