
package team.conso.system.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import team.conso.system.core.Helpable;

/**
 *
 */
public class APropo extends AbstractAction
{
    public APropo()
    {
        super();
    }
 
    public void actionPerformed( ActionEvent e )
    {
        Helpable appli = (Helpable)getValue( "appli" );
        appli.showAPropo();
    }
}