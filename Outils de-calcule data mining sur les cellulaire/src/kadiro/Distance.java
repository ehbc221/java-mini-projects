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
import java.util.Vector ;
public class Distance implements CommandListener{
    public Distance(Display d){
         confirm = new Command("confirmer", Command.OK, 1);
        backCommand = new Command("revenir", Command.BACK, 1);
        sw = new Command("choix_nb", Command.BACK, 1);
        tf1 = new TextField(new String(),new String(),20,0) ;
        tf2 = new TextField(new String(),new String(),20,0) ;
        display = d;
        frame = new Form("kadiro dis");
        frame.addCommand(confirm);
        frame.addCommand(backCommand);
        frame.append("Par OUALI Abdelkader\n") ;
        frame.append("la methode utiliser\n") ;
        frame.append(tf1);
        frame.append("Le nombre des attributs\n");
        frame.append(tf2);
        frame.setCommandListener(this);
    }
    
     public void commandAction(Command c, Displayable d) {
         if(c == clcCommand1 ){
                  try{
                      if(!tf.isEmpty()&&!af.isEmpty()){ 
                      methode = tf1.getString() ;
                      if(methode.equals("e")){
                          TextField temp,temp2 ;
                           
                            if(res!=null) res = null ;
                            res = new Vector() ;  
                           for(int i=0;i<tf.size();i++){
                           temp =  (TextField) tf.elementAt(i) ;
                           temp2 =  (TextField) af.elementAt(i) ;
                           try{ 
 res.addElement(new Double((Double.parsedouble(temp.getString())-Double.parsedouble(temp2.getString()))*
         (Double.parsedouble(temp.getString())-Double.parsedouble(temp2.getString()))));
                          }catch(Exception ee){
                          System.out.println("erreurs" +ee);
                          show();frame.append("erreurs parametre"); return;
                      }
                          } result =0 ;
                           for(int i=0;i<res.size();i++){
                               result += ((Double)res.elementAt(i)).getvalue() ;
                           }
                           tres.setString(Math.sqrt(result)+"");
                      }
                      else if (methode.equals("m")){
                          TextField temp,temp2 ;
                           
                            if(res!=null) res = null ;
                            res = new Vector() ;  
                           for(int i=0;i<tf.size();i++){
                           temp =  (TextField) tf.elementAt(i) ;
                           temp2 =  (TextField) af.elementAt(i) ;
                           try{
 res.addElement(new Double(Math.abs(Double.parsedouble(temp.getString())-Double.parsedouble(temp2.getString()))));
                          }catch(Exception ee){
                          System.out.println("erreurs" +ee);
                          show();frame.append("erreurs parametre"); return;
                      }
                          } result =0 ;
                           for(int i=0;i<res.size();i++){
                               result += ((Double)res.elementAt(i)).getvalue() ;
                           }
                           tres.setString(result+"");
                      }
                      else{
                          show();
                          frame.append("methode inconnue");
                         frame2=null ;
                      }}

        }catch(Exception e){frame2.append("erreurs ");
        System.out.println("erreurs "+e);
        }}
       if(c ==confirm){
             try{
                 try{
                 att = Integer.parseInt(tf2.getString());
                 }
                 catch(Exception ee){frame.append("erreur parametre");return;
                 }
                 clcCommand1 = new Command("Calcule", Command.OK, 2);
                 frame2 = new Form("Distance");
                 frame2.addCommand(clcCommand1);
                 frame2.addCommand(sw);
                  tres = new TextField(new String(),new String(),20,0) ;
                 frame2.append("Par OUALI Abdelkader\n") ;
                 frame2.append("RQ apres la normalisation \n") ;  
                 if(tf!=null) tf = null ;
                 tf = new Vector() ;
                 frame2.append("le nouveau exemple\n");
                 for(int i=0;i<att;i++){
                     tf.addElement(new TextField(new String(),new String(),10,0));
                     frame2.append("att"+i+" ");
                     frame2.append((TextField)tf.elementAt(i));
                 }
                 frame2.append("-----------\n");
                 frame2.append("l'ancien exemple\n");
                 af = new Vector() ;
                 for(int i=0;i<att;i++){
                     af.addElement(new TextField(new String(),new String(),10,0));
                     
                     frame2.append("att"+i+" ");
                     frame2.append((TextField)af.elementAt(i));
                 }
                 frame2.append("-----------\n");
                 frame2.append("resultat est ");
                 frame2.append(tres);
                 frame2.setCommandListener(this);
                 display.setCurrent(frame2);
             }
             catch(Exception e){
                 System.out.println("erreurs "+e) ;
             }
         }
         if(c ==backCommand){
             try{
                 display.setCurrent(Midlet.getframe());
             }
             catch(Exception e){
                 System.out.println("erreurs "+e) ;
             }
         }
         if(c ==sw){
             try{
                 show();
                 frame2=null ;
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
    private Form frame2;
    private Command clcCommand1;
    private Command confirm;
    private Command sw;
    static protected Command backCommand;
    
    private Vector tf,af,res ;
    private TextField tf1,tf2,tres ;
    private int att ;
    private double result ;
    String methode ;
}
