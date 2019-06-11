package jeux.ihm.panneaux;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
/***/
abstract class Panneau extends JPanel {
	private static final long serialVersionUID = 1L;
	Panneau()
	{
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
	}
	void setSelection(boolean selectionUnique,JList liste)
	{
		if(selectionUnique)
			liste.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		else
			liste.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	}
	public JList getListe()
	{
		if(this instanceof PanneauListeFichiers)
		{
			return (JList)((JScrollPane)getComponent(0)).getViewport().getComponent(0);
		}
		return (JList)((JScrollPane)getComponent(1)).getViewport().getComponent(0);
	}
}