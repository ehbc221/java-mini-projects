import java.io.File;

import javax.swing.JFrame;

import ccm.kx.swingTools.ListenerLogger;

public class Test
{
    public static void main(String[] args)
    {
        // Définit où générer les sources
        ListenerLogger.setPath(new File("src"));
        
        JFrame frame = new JFrame();
        
        // Ajoute tous les Listener à 'frame'
        ListenerLogger.logListeners(frame);
        
        frame.setSize(200, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
