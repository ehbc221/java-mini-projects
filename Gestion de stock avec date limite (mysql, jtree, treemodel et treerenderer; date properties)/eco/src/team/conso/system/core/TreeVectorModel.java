package team.conso.system.core;

import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import team.conso.data.bdd.Categorie;
import team.conso.data.bdd.DataElement;
import team.conso.data.bdd.Produit;
import team.conso.data.bdd.Stock;

/**
 * 
 * class ki permet de rempliir un arbre a partir d un vector de donnee
 * team.conso.system.ui.views.tree
 * 
 */
public class TreeVectorModel extends DefaultTreeModel
{
    static DefaultMutableTreeNode root = new DefaultMutableTreeNode( "" );
    /**
     * contien un vector de vector representatn les donnee a afficher
     * c donnne sont deja organiser en arbre chac element n est poresent qu une foi
     */
    Vector data = null;

    /**
     * @param root
     */
    public TreeVectorModel( TreeNode root )
    {
        super( root );
    }

    public TreeVectorModel()
    {
        super( root );
    }
    
    
    public void setData( Vector data )
    {
        this.data = data;
        buildTree();
    }
    
    DefaultMutableTreeNode currentCat = null;
    DefaultMutableTreeNode currentPro = null;
    
    /**
     * construit l arbre en fonction des donnee contenu ds le vector 
     * toute les donne du vector son des class elementaire de bdd
     */
    private void buildTree()
    {
        root.removeAllChildren();
        for ( Iterator iter = data.iterator(); iter.hasNext(); )
        {
            DataElement elt = (DataElement)iter.next();
            if ( elt instanceof Categorie )
            {
                Categorie cat = (Categorie)elt;
                currentCat = new DefaultMutableTreeNode( cat );
                root.add( currentCat );
            }
            else if ( elt instanceof Produit )
            {
                Produit pro = (Produit)elt;
                currentPro = new DefaultMutableTreeNode( pro );
                currentCat.add( currentPro );
            }
            else if ( elt instanceof Stock )
            {
                Stock st = (Stock)elt;
                DefaultMutableTreeNode dmtn = new DefaultMutableTreeNode( st );
                currentPro.add( dmtn );
                dmtn.add( new DefaultMutableTreeNode( "quantité restante: " + st.getNombre() ));
                dmtn.add( new DefaultMutableTreeNode( "prix total : " + st.getPrix() ) );
                StringTokenizer sgt = new StringTokenizer( st.getDate(), "-" );
                String y = sgt.nextToken(); String m = sgt.nextToken(); String d = sgt.nextToken();
                dmtn.add( new DefaultMutableTreeNode( "date limite : " + st.getDateLimit() ) );
                dmtn.add( new DefaultMutableTreeNode( "date d'achat : " + m + "/" + m + "/" + y ) );
            }
        } // end for
    }
    
    public void setRootName( String name )
    {
        root.setUserObject( name );
    }
    
    
}
