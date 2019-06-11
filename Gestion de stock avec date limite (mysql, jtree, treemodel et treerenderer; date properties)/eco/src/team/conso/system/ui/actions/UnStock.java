package team.conso.system.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import team.conso.data.BDDEco;

/**
 */
public class UnStock extends AbstractAction
{
    public UnStock()
    {
        super();
    }
 
    public void actionPerformed( ActionEvent e )
    {
        try
        {
            BDDEco bdd = (BDDEco)getValue( "bdd" );
            bdd.desStock( 
                    (String)getValue( "cleStock" ),
                    (String)getValue( "quantite" ),
                    (String)getValue( "rest" ),
                    (String)getValue( "dest" )
                    ) ;
        } catch (Exception ex)
        {
            System.out.println( "Destoquage echoué." );
        }
    }

}
