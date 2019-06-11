/*
 * Created on 7 oct. 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package team.conso.system.ui.views;

import java.awt.BorderLayout;

import javax.swing.JTextArea;
import javax.swing.JTextField;

import team.conso.system.ui.ViewPanel;


/**
 * @author Ludovic
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class PnlTest extends ViewPanel
{
    private static final String VIEW_NAME = "Test";


    /**
     * 
     */
    public PnlTest()
    {
        super();
        setLayout( new BorderLayout() );
        JTextArea ta = new JTextArea();
        JTextField pnl = new JTextField();
//        JTextComponent pnl = new JTextComponent();
        
        add( ta, BorderLayout.CENTER );
        add( pnl, BorderLayout.SOUTH );
    }

    public String getViewName()
    {
        return VIEW_NAME;
    }
    
    public void refresh()
    {
        repaint();
    }
    public String getMnemo()
    {
        return "T";
    }
    public void close()
    {
        
    }
}
