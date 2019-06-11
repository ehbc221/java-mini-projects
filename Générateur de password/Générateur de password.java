//::democrite41@laposte.net 
import java.io.*;
import java.util.Random;
import java.lang.*;
import java.lang.String;
	
 public class diCoTe
//°.°declaration de variable par type et caractere avec une variable
// ~d'aléatoire, suivant le modèle de la casse et caractère spéciaux ,il est possible de modifier le générateur de mot de passe en conservant uniquement les chaine de caractere alphanumerique exception faite des caractère spéciaux
  {
   private static final Random rand = new Random();
   private static final   String Xsi ="abcdefghijklmnopqrstuvwxyz";
   private static final String Gamm ="ABCDEFGHIJKLMNOPQRSTUVWXYZ";  
   private static final String Iot = "1234567890";
   private static final String Zeta="&~#|`-_)('/?,;:.";
   private static String demo =""; 
   private static double i =0;
   public static void main(String[] args) {
//création d'un fichier dico.txt à l'emplacement de la compilation ,taille illimité max essayé 8 Go;)
        FileWriter writer;
    try {
      writer = new FileWriter("dico01.txt");

        {
//nombre d 'iteration à executer afin de générer du code et un nombre de mots de
// passe __________;))))))))
	for (i=0;i<31415926L;i++){
	demo="";
//randomisation des caractères selon leur nombre par type définis ,entre six et dix caratères
        while ((demo.length() != 6)&& (demo.length() != 7)&& (demo.length() != 8)&& (demo.length() != 9)&& (demo.length() != 10)) {
//selection aleatoire du type de caractère puis selection parmis les differents modèles de caractères              
              int rPick=rand.nextInt(4);
           if (rPick ==0) {
	      int erzat=rand.nextInt(25);
              demo+=Xsi.charAt(erzat);
         } else if (rPick == 1) {
	      int erzat=rand.nextInt(9);
	      demo+=Iot.charAt(erzat);
         } else if (rPick==2) {
              int erzat=rand.nextInt(25);
              demo+=Gamm.charAt(erzat);
         }else if (rPick==3) {
              int erzat=rand.nextInt(15);
              demo+=Zeta.charAt(erzat);
	}
	}
//Ecrire le caractères selectionnée pour être mis en place comme caractère transcris 
	writer.write(demo+"\n");
}
}
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
}
}
}