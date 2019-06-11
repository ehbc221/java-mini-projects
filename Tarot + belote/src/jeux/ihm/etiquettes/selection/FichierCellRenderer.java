package jeux.ihm.etiquettes.selection;
import java.awt.*;
import javax.swing.*;
/**
 *
 */
public class FichierCellRenderer extends CellRenderer {
	private static final long serialVersionUID = 1L;
	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		setText((String)value);
		setOpaque(true);
		if(isSelected)
		{
			setBackground(Color.BLUE);
			setForeground(Color.RED);
		}
		else
		{
			setBackground(Color.WHITE);
			setForeground(Color.BLACK);
		}
		return this;
	}
}