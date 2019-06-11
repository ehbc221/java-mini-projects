/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kadiro;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;
/**
 *
 * @author ABDELKADER
 */
public class Support implements CommandListener {
    public Support(Display d){
        clcCommand = new Command("Calcule", Command.OK, 2);
        backCommand = new Command("revenir", Command.BACK, 1);
        try{
        tf1 = new TextField(new String(),new String(),20,0) ;
        tf2 = new TextField(new String(),new String(),20,0) ;
        tf3 = new TextField(new String(),new String(),20,0) ;
        tf4 = new TextField(new String(),new String(),20,0) ;}
        catch(Exception e){System.err.println("erreur "+e);}
        display = d;
        frame = new Form("kadiro Sup");
        frame.addCommand(clcCommand);
        frame.addCommand(backCommand);
        frame.append("Par OUALI Abdelkader\n") ;
        frame.append("La valeur minimale\n");
        frame.append(tf1);
        frame.append("La valeur maximale\n");
        frame.append(tf2);
        frame.append("la valeur actuelle");
        frame.append(tf3);
        frame.append("ca normalisation donne");
        frame.append(tf4);
        frame.setCommandListener(this);
    }
    public void commandAction(Command c, Displayable d) {
        if(c == clcCommand ){
             try{
                 min =Double.parsedouble(tf1.getString()) ;
                 max =Double.parsedouble(tf2.getString()) ;
                 act =Double.parsedouble(tf3.getString()) ;
                 double rt =(double) (act-min)/(max-min) ;
                 tf4.setString(rt+"");
        }catch(Exception e){tf4.setString("erreurs ");}}
         if(c ==backCommand){
             try{
                 display.setCurrent(Midlet.getframe());
             }
             catch(Exception e){
                 System.out.println("erreurs "+e) ;
             }
         }
    }
    public void show(){
        display.setCurrent(frame) ;
    }
    	     
    private Display display;
    private Form frame;
    private Command clcCommand;
    static protected Command backCommand;
   
    private TextField tf1,tf2,tf3,tf4 ;
    double min,max,act ;
}
