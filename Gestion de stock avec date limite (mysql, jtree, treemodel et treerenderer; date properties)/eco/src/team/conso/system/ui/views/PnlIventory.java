package team.conso.system.ui.views;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.MouseInputAdapter;
import javax.swing.tree.DefaultMutableTreeNode;

import team.conso.data.BDDEco;
import team.conso.data.DataEvent;
import team.conso.data.DataListener;
import team.conso.data.bdd.DataFilter;
import team.conso.data.bdd.Stock;
import team.conso.system.ui.FrmConso;
import team.conso.system.ui.PnlFilter;
import team.conso.system.ui.ViewPanel;
import team.conso.system.ui.actions.UnStock;
import team.conso.system.ui.views.tree.InventoryTreeNodeRenderer;
import team.conso.system.ui.views.tree.TreeInventoryModel;

/**
 */
public class PnlIventory extends ViewPanel
{
    private static final String VIEW_NAME = "Inventaire";

    private JTree tree;
    private PnlFilter pnlFiltre = new PnlFilter();
    private JSplitPane split = new JSplitPane();

    // je passe par une action extern ;o) juste comme ca au cas ou 
    // on pourrai metre le contenu de UnStock a la place de son appelle ds cette class
    private UnStock unstockAction = new UnStock();
    
    public PnlIventory()
    {
        super();
        setLayout( new BorderLayout() );
        add( split, BorderLayout.CENTER );
        JScrollPane scrll = new JScrollPane();
        scrll.setAutoscrolls( true );
        split.setLeftComponent( scrll );
	    tree = new JTree();
        scrll.getViewport().add( tree );
        split.setRightComponent( pnlFiltre );
        // regle le comportement sur un resize de la fenetre
        split.setResizeWeight( 1 );
    	
    	    
        tree.setEditable( false );
        // on a pas besoin du root mais on pourra toujour le metre en option pour 
        // le nom d un user par exemple ou d un domaine
        tree.setRootVisible( false ); 
        tree.setExpandsSelectedPaths( true );
        // tant que pas de data ben y a rien a montrer
        tree.setModel( null );
        // met en place le renderer qui apêllera le rendu de chac cellule
        tree.setCellRenderer( new InventoryTreeNodeRenderer() );
        tree.addMouseListener( new Mouse() );
        pnlFiltre.addOkActionListener( new ActionListener() {   
            public void actionPerformed(ActionEvent e)
            {
                updateValues( pnlFiltre.getFiltre() );
            }
        });
        pnlFiltre.addCancelActionListener( new ActionListener() {   
            public void actionPerformed(ActionEvent e)
            {
                updateValues( null );
            }
        });
    }
    
    public void setBDDStock( BDDEco bdd )
    {
        tree.setModel( new TreeInventoryModel( bdd ) );
        pnlFiltre.setBDDStock( bdd );
//        ((TreeInventoryModel)tree.getModel()).setRootName( bdd.getName() ); now c fai ds le cosntructeur
        tree.setRootVisible( true ); 
        tree.repaint();
        bdd.addDataListener( new UpdateListener() );
    }
    
    public String getViewName()
    {
        return VIEW_NAME;
    }

    public void refresh()
    {
        repaint();
    }
    public void close()
    {
    }
    public String getMnemo()
    {
        return "I";
    }
    
    // chaque ViewPanel qui doi reagir a une modification de la BDD 
    // doi implementer un listener sur celle ci
    class UpdateListener implements DataListener
    {
        public void changeUpdate(DataEvent dataEvent)
        {
            updateValues( pnlFiltre.getFiltre() );
        }
    }
    
    public void updateValues( DataFilter filtre )
    {
        try
        {
            // pour l instan le type d event ne ns interresse pas 
            TreeInventoryModel mdl = (TreeInventoryModel)tree.getModel();
            mdl.setFiltre( filtre );
            mdl.majTree(); // repositionne les donnees
            mdl.reload();  // pour recharger le model de l arbre
            refresh();     // repaint le pnl
            
        } catch (Exception e)
        {
            System.out.println( "change update  : " + e);
        }
    }
    class Mouse extends MouseInputAdapter
    {
        
        public void mouseClicked( MouseEvent e )
        {
            if ( e.getButton() == MouseEvent.BUTTON3 )
            {
                // pour l instant clik droit c que destoker
                unStock();
            }
        }
    }
    
    /**
     * action de desstoker un produit
     *
     */
    public void unStock()
    {
        try
        {
            // traitement executer que sur selection d un produit
            if ( tree.getSelectionCount() > 0 )
            {
	            Object userObject =  ((DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent()).getUserObject();
	            if ( userObject instanceof Stock )
	            {
	                Stock stk = (Stock)userObject;
	                // interrogation de la quantitee
	                String quantite = JOptionPane.showInputDialog( 
	                        FrmConso.getInstance(), 
	                        "Combien en voulez-vous? ",
	                        "Destoquage de " + stk.getLibProd(), 
	                        JOptionPane.QUESTION_MESSAGE
	                        );
	                if ( quantite != null )
	                {
	                    int current = Integer.parseInt( stk.getNombre() );
	                    int q = Integer.parseInt( quantite );
	                    if ( current - q < 0 )
	                    {
	                        JOptionPane.showConfirmDialog( FrmConso.getInstance(), "Nombre dépassant la capacité de ce stoque.", "Control de saisie.", JOptionPane.WARNING_MESSAGE );
	                    }
	                    else
	                    {
			    			unstockAction.putValue( "bdd", ((TreeInventoryModel)tree.getModel()).getBdd() );
			    			unstockAction.putValue( "cleStock", stk.getCodeRef() );
			    			unstockAction.putValue( "quantite", quantite );
			    			unstockAction.putValue( "rest", stk.getNombre() );
			    			unstockAction.putValue( "dest", System.getProperty( "user.name" ) );
			    			unstockAction.actionPerformed( null ); 
	                    }
	                }
	            }
            }
        } catch (Exception ex)
        {
            System.out.println( "exception unstok  : " + ex );
        }
    }
    
}
