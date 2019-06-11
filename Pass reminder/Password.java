package mytools.util;

public class Password 
{
    
    public static String PassGenerator(int longueur)
    {
        String Pass = "";
        String Chaine = "abcdefghijklmnopqrstuvwxyz1234567890@*#";
        
              for (int i = 0; i<longueur ; i++)
                 {
                     Pass = Pass + Chaine.charAt((int)(Math.random() * Chaine.length()));
                 }
        
        return Pass;
    }
    
    public static int NbreAl(int min , int max)
    {
        return (int)(Math.random()*(max-min+1));
    }
    
    public static int CompteurLettre(String str, char c)
    {
     int Nbre = 0;
     int posCar, i = 0 ;
     
     do
     {
        posCar = str.indexOf(c,i);
        if (posCar >= 0)
        {
            Nbre++;
            i = posCar+1;
        }
     }while(posCar >= 0);
     
     
     return Nbre;
    }
}
