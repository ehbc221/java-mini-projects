package team.conso.system.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import team.conso.system.ui.FrmConso;

/**
 * permet dafficher ou de fermer un onglet 
 */
public class OpenCloseView extends AbstractAction
{
    public OpenCloseView()
    {
        super();
    }
 
    /**
     * pour lexecution de cette action evidement l appli doit connaitre le
     * mnemonic utilise pour la methode addorremove... 
     */
    public void actionPerformed( ActionEvent e )
    {
        FrmConso appli = (FrmConso)getValue( "appli" );
        appli.addOrRemoveView( e.getActionCommand().substring( 0, 1 ) );
    }
}