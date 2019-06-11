/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kadiro;

/**
 *
 * @author ABDELKADER
 */
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;
public class Denpro implements CommandListener {
    public Denpro(Display d){
        b = new Command("Calcule", Command.OK, 2);
        a = new Command("revenir", Command.BACK, 1);
        try{
        tf1 = new TextField(new String(),new String(),20,0) ;
        tf2 = new TextField(new String(),new String(),20,0) ;
        tf3 = new TextField(new String(),new String(),20,0) ;
        tf4 = new TextField(new String(),new String(),20,0) ;}
        catch(Exception e){System.err.println("erreur "+e);}
        f = d;
        this.d = new Form("kadiro Sup");
        this.d.addCommand(b);
        this.d.addCommand(a);
        this.d.append("Par OUALI Abdelkader\n") ;
        this.d.append("La moyenne\n");
        this.d.append(tf1);
        this.d.append("l'ecartype\n");
        this.d.append(tf2);
        this.d.append("la valeur de att");
        this.d.append(tf3);
        this.d.append("resultat");
        this.d.append(tf4);
        this.d.setCommandListener(this);
    }
    public void commandAction(Command c, Displayable d) {
        if(c == b ){
             try{
              // Math.exp() ;
        }catch(Exception e){tf4.setString("erreurs ");}}
         if(c ==a){
             try{
                 f.setCurrent(Midlet.getframe());
             }
             catch(Exception e){
                 System.out.println("erreurs "+e) ;
             }
         }
    }
      public void show(){
        f.setCurrent(d) ;
    }
    	
     private Display f;
    private Form d;
    private Command b;
    static protected Command a;
   
    private TextField tf1,tf2,tf3,tf4 ;
}
