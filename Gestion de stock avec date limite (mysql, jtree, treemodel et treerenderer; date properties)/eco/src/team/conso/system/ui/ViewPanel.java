package team.conso.system.ui;

import javax.swing.JPanel;

/**
 * team.conso.system.ui
 * 
 */
public abstract class ViewPanel extends JPanel
{
    public abstract String getViewName();
    public abstract void refresh();
    public abstract String getMnemo();
    public abstract void close();
}
