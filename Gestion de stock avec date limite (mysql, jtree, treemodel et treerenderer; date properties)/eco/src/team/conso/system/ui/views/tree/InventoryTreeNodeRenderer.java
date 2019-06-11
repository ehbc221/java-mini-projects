package team.conso.system.ui.views.tree;

import java.awt.Color;
import java.awt.Component;
import java.awt.SystemColor;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;

import team.conso.data.BDDEco;
import team.conso.data.bdd.DataElement;


/**
 * class de rendu pour les cellule du treee d inventaire de l onglet inventaire .. ;o)
 * 
 */
public class InventoryTreeNodeRenderer implements TreeCellRenderer
{
    public static final Color COLOR_ALERTE_DEFAULT   = Color.orange;
    public static final Color COLOR_TOO_LATE_DEFAULT = Color.red;
    
    private Color colorAlerte = COLOR_ALERTE_DEFAULT;
    private Color colorTooLate = COLOR_TOO_LATE_DEFAULT;
    
    /**
     * Constructor for .
     */
    public InventoryTreeNodeRenderer()
    {
        super();	}
    
    public Component getTreeCellRendererComponent( JTree tree, Object value, boolean selected, boolean expanded,
            boolean leaf, int row, boolean hasFocus )
    {
        JLabel lbl = new JLabel();
        lbl.setOpaque( true );
        lbl.setForeground( selected ? SystemColor.textHighlightText : tree.getForeground() );
        Color col = tree.getBackground(); // init la couleur du fond
        // nous savon que notre tree ne contien que des DefaultMutableTree
        Object obj = ((DefaultMutableTreeNode)value).getUserObject();
        if ( obj instanceof DataElement )
        {
            DataElement val = (DataElement)obj;
            int status = val.getStatus();
            if ( status == BDDEco.STATUS_ALERTE ) col = colorAlerte;
            else if ( status == BDDEco.STATUS_TOO_LATE ) col = colorTooLate;

            lbl.setBackground( selected?SystemColor.textHighlight:col );
            lbl.setText( val.getLibelle() );
            if ( val.getIcon() != null )
                lbl.setIcon( val.getIcon() );
            // ----------- code permettan de fixer la taille en hauteur ds une cellule du tree -------
            //	        Dimension dl = lbl.getPreferredSize();
            //	        dl.height = 20;
            //	        lbl.setPreferredSize(dl);
            // ---------------------------------------------------------------------------------------
        }
        else if ( obj instanceof String ) // sinon si c une string c dc une leaf ou le root
        {
            lbl.setText( obj.toString() );
            lbl.setBackground( selected ? SystemColor.textHighlight : tree.getBackground() );
        }
        return lbl;
    }

}