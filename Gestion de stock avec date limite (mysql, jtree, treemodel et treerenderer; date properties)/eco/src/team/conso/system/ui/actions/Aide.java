
package team.conso.system.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import team.conso.system.ui.FrmConso;

/**
 */
public class Aide extends AbstractAction
{
    public Aide()
    {
        super();
    }
 
    public void actionPerformed( ActionEvent e )
    {
        FrmConso appli = (FrmConso)getValue( "appli" );
        appli.browseAide();
    }
}
