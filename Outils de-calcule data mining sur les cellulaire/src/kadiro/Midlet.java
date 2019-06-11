/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kadiro;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.Display;

/**
 * @author ABDELKADER
 */
public class Midlet extends MIDlet implements CommandListener {
    public Midlet(){
         d= Display.getDisplay(this);
        exit= new Command("Exit", 7, 99);
        choisir = new Command("Choisir", Command.OK, 1);
        f = new List(null,1) ;
        f.addCommand(exit);
        f.addCommand(choisir);
        f.setTitle("Outils Kader Data mining");
        f.append("KNN", null);
        f.append("Dynamic_normalisation", null);
        f.append("tri", null);
        f.append("distance", null);
        f.append("moy/ecart",null) ;
        f.append("densité proba",null) ;
        f.setCommandListener(this);
    }

    public void startApp() {
         d.setCurrent(f);
    }
    
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
    }
    public void commandAction(Command c, Displayable d) {
         if(c == exit)
            try
            {   destroyApp(false);
                notifyDestroyed();
            }
            catch(Exception _ex) { System.out.println("erreurs "+_ex);}
         else if(c == choisir){
             try{
              if(f.getSelectedIndex()==0){
                if(supp!=null){
                supp=null; }
                supp = new Support(this.d) ;
                supp.show();
                }
               if(f.getSelectedIndex()==1){
                if(dsupp!=null){
                dsupp=null; }dsupp = new DSupport(this.d) ;
                dsupp.show();
                }
               if(f.getSelectedIndex()==2){
                if(tri!=null){
                tri=null; }tri = new Tri(this.d) ;
                tri.show();
                }
               if(f.getSelectedIndex()==3){
                if(dis!=null){
                dis=null; }dis = new Distance(this.d) ;
                dis.show();
                }
                if(f.getSelectedIndex()==4){
                if(mec!=null){
                mec=null; }mec = new Moy_Ecar(this.d) ;
                mec.show();
                }
                if(f.getSelectedIndex()==5){
                if(denpro!=null){
                denpro=null; }denpro = new Denpro(this.d) ;
                denpro.show();
                }
             }catch(Exception ee){
                 System.out.println("erreur"+ee) ;
             }
         }
    }
    public static List getframe(){
        return f ;
    }
    private static List f ;
    private Display d ;
    private Command exit ;
    private Command choisir ;
    private Support supp ;
    private DSupport dsupp ;
    private Tri tri ;
    private Distance dis ;
    private Moy_Ecar mec ;
    private Denpro denpro ;
}
