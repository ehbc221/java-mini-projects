package team.conso.system.ui;

import javax.swing.JTabbedPane;

/**
 * team.conso.system.ui
 * 
 */
public class TabbedViewPane extends JTabbedPane
{
    
    public synchronized void addViewPane( ViewPanel vp )
    {
        try
        {
            add( vp );
            setTitleAt( getTabCount()-1, vp.getViewName() );
        } catch (Exception e)
        {
            System.out.println(" erreor : " + e );
        }
    }
    
    public synchronized ViewPanel getViewPanelAt( int idx )
    {
        return  (ViewPanel)getComponentAt( idx );
    }
    
    public synchronized void removeViewPane( ViewPanel vp )
    {
        try
        {
	        vp.close();
	        remove( vp );
        } catch ( Exception ex )
        {
            System.out.println( "team.conso.system.ui.TabbedViewPane.removeViewPane() : " + ex );
        }
    }
    
    /**
     * si la vu est deja presente on l enleve et on return false
     * sinon on laffiche et on return true
     * @param vp
     * @return
     */
    public synchronized boolean addOrRemove( ViewPanel vp )
    {
        try
        {
            if ( isViewAdded( vp ) ) // vp est deja a laffichage
            {
                removeViewPane( vp );
                return false;
            }
            addViewPane( vp );
            return true;
            
        } catch (Exception e)
        {
            System.out.println( "error : " + e );
        }
        return false;
    }
    
    public boolean isViewAdded( ViewPanel vp )
    {
        return indexOfComponent( vp ) >= 0; // vp est deja a laffichage
    }
}
