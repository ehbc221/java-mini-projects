package team.conso.system.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import team.conso.system.core.Exitable;

/**
 * team.conso.system.ui.actions
 * 
 */
public class Exit extends AbstractAction
{
    public Exit()
    {
        super();
    }
 
    public void actionPerformed( ActionEvent e )
    {
        Exitable appli = (Exitable)getValue( "appli" );
        appli.exit();
    }
}
