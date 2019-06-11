/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Diagramme;

import java.awt.Color;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.Plot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.DefaultPieDataset.*;

/**
 *
 * @author Ibrahima
 */
public class Diagramme {

    public void Diagramme(String str, String s1, String s2, String s3, String s4, String s5, float i1, float i2, float i3, float i4, float i5) {

        DefaultPieDataset union = new DefaultPieDataset();
        union.setValue(s1, i1);
        union.setValue(s2, i2);
        union.setValue(s3, i3);
        union.setValue(s4, i4);
        union.setValue(s5, i5);
        /*cree un diagramme circulaire 3D*/
        JFreeChart repart = ChartFactory.createPieChart3D(str, union, true, true, false);
        /*Ajout de l'effet transparent au diagramme*/
        PiePlot3D plot3 = (PiePlot3D) repart.getPlot();
        plot3.setForegroundAlpha(0.6f);
        plot3.setCircular(true);
        Plot plot = repart.getPlot();
        /*Determination des couleur des parts*/
        ((PiePlot) plot).setSectionPaint(s1, new Color(255, 0, 0));
        ((PiePlot) plot).setSectionPaint(s2, new Color(255, 255, 0));
        ((PiePlot) plot).setSectionPaint(s3, new Color(255, 0, 255));
        ((PiePlot) plot).setSectionPaint(s4, new Color(0, 0, 255));
        ((PiePlot) plot).setSectionPaint(s5, new Color(0, 255, 255));

        // cree et affiche le frame...
        ChartFrame frame = new ChartFrame("Diagramme Circulaire", repart);
        frame.pack();
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);
        frame.setTitle("Diagramme Circulaire ");
    }
}
