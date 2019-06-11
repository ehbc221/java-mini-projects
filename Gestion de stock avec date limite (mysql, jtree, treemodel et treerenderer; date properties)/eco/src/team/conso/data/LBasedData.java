
package team.conso.data;

import javax.swing.event.EventListenerList;



/**
 * mecanisme mis en place pour permetre l ecoute des changement de la BDD 
 * 
  */
public abstract class LBasedData implements LData
{
    private transient boolean notifyingListeners;

    protected EventListenerList listenerList = new EventListenerList();

    /**
     * Ajoute un data listener pour notifier tous les changement de la donnee.
     * 
     * @param listener
     */
    public void addDataListener( DataListener listener )
    {
        listenerList.add( DataListener.class, listener );
    }

    protected void fireChangeUpdate( DataEvent e )
    {
        notifyingListeners = true;
        try
        {
            Object[] listeners = listenerList.getListenerList();
            for ( int i = listeners.length - 2; i >= 0; i -= 2 )
            {
                if ( listeners[ i ] == DataListener.class )
                    ((DataListener)listeners[ i + 1 ]).changeUpdate( e );
            }
        }catch (Exception ex)
        {
            System.out.println( "error " + ex);
        } 
        finally
        {
            notifyingListeners = false;
        }
    }

    public EventListenerList getListenerDataList()
    {
        return listenerList;
    }
}
