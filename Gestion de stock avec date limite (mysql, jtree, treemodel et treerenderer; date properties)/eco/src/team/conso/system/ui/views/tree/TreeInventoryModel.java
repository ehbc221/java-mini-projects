package team.conso.system.ui.views.tree;

import java.util.Vector;

import team.conso.data.BDDEco;
import team.conso.data.DataEvent;
import team.conso.data.DataListener;
import team.conso.data.bdd.DataFilter;
import team.conso.system.core.TreeVectorModel;

/**
 * team.conso.system.ui.views.tree
 * 
 */
public class TreeInventoryModel extends TreeVectorModel
{
    BDDEco bdd = null;
    DataFilter filtre = null;
    public TreeInventoryModel( BDDEco bdd )
    {
        super();
        this.bdd = bdd;
        bdd.addDataListener( new DataListener() {
            public void changeUpdate( DataEvent dataEvent )
            {
                majTree();
            }

        });
        majTree();
        setRootName( bdd.getName() );
    }
    
    /**
     * met a jour le tree en cas de modification des donnee de la base 
     * pour plus de facilite et parce que ce n est pas trop chere en CPU 
     * on recalcul entierement le tree a chac modif
     * a terme il faut prevoir dobtimiser cette methode
     */
    public void majTree()
    {
        if ( bdd == null ) return;
        Vector treeData = bdd.getStock( filtre );
        setData( treeData );
    }

    public BDDEco getBdd()
    {
        return bdd;
    }
    public DataFilter getFiltre()
    {
        return filtre;
    }
    public void setFiltre(DataFilter filtre)
    {
        this.filtre = filtre;
    }
}
