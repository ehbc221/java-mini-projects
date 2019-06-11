package ccm.kx.swingTools;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.TreeSet;

/**
 * Classe utilitaire pour logger tous les �venements d'un composant Swing.
 * @author KX
 */
public class ListenerLogger
{
    private static final String PACKAGE = "ccm.kx.swingTools", PATH = PACKAGE.replace('.', '/'), PREFIX = "ListenerLogger$", SUFFIX = "";
    
    private static final TreeSet<String> errorSet = new TreeSet<String>();
    
    private static File path = null;
    
    /**
     * Initialises le chemin d'acc�s o� doivent �tre g�n�r�s les sources des listeners de test.
     * Ce chemin doit correspondre � la source du projet import� avec cette classe.
     */
    public static void setPath(File src)
    {
        File pack = new File(src, PATH);
        
        if (!pack.isDirectory() || !pack.canWrite())
            throw new IllegalArgumentException(pack.getAbsolutePath()+" is not valid as writing path");
        
        path=pack;
    }
    
    /**
     * Ajoutes � tous les composants pass�s en param�tre une impl�mentation de chacun des Listener qu'ils supportent.<br>
     * Si ces impl�mentations n'existent pas elles sont g�n�r�es pour pouvoir �tre utilis�es � la prochaine compilation.<br>
     * @param components tous les composants � ajouter
     */
    public static void logListeners(Component...components)
    {        
        if (components==null)
            return;
        
        int nbError = errorSet.size();
        
        // pour chaque composant � ajouter
        for (Component component : components)
        {
            // pour chaque m�thode
            for (Method method : component.getClass().getMethods())
            {
                Class<?>[] parameters = method.getParameterTypes();
                
                // si la m�thode a un seule param�tre
                if (parameters.length == 1)
                {
                    for (Class<?> type : parameters[0].getInterfaces())
                    {
                        // si l'unique param�tre de la m�thode impl�mente EventListener
                        if (type.equals(java.util.EventListener.class))
                        {
                            String name = PREFIX + parameters[0].getCanonicalName().replaceAll("\\.", "_") + SUFFIX;
                            
                            try // on ajoute notre instance du listener via cette m�thode
                            {
                                Class<?> test = Class.forName(PACKAGE + "." + name);
                                
                                try
                                {
                                    method.invoke(component, test.newInstance());
                                }
                                catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e)
                                {
                                    System.err.println(e);
                                }
                            }
                            catch (ClassNotFoundException e) // si notre instance n'existe pas on en g�n�re le code source
                            {
                                if (errorSet.add(parameters[0].getCanonicalName()))
                                {
                                    generateSource(parameters[0], name);
                                }
                            }
                            
                            break;
                        }
                    }
                }
            }
        }
        
        // Si on a cr�� de nouvelles sources
        if (errorSet.size() > nbError)
        {
            String strSet = errorSet.toString();
            System.err.println("Une ou plusieurs impl�mentations de listeners sont manquantes : "+strSet.substring(1, strSet.length() - 1));
            System.err.println("Leur code source a �t� g�n�r� dans le r�pertoire " + path.getAbsolutePath());
            System.err.println("Pour les prendre en charge, red�marrer le programme apr�s avoir compil� ces nouvelles sources.");
        }
    }
    
    private static void generateSource(Class<?> clazz, String name)
    {
        if (path==null)
            throw new IllegalStateException("Path must be set with 'setPath(String)' before invoking 'logListeners' for generate sources.");
        
        try
        {
            PrintWriter writer = new PrintWriter(new File(path, name + ".java"));
            
            writer.printf("package %s;\n\n",PACKAGE);
            
            // javadoc
            writer.printf("/**\n * %s's implementation, automatically generated to be invoked by {@link ListenerLogger#logListeners(java.awt.Component...)}.\n",clazz.getSimpleName());
            writer.printf(" * @author KX\n * @see %s\n */\n",clazz.getCanonicalName());
            
            // classe
            writer.printf("public class %s implements %s\n{", name, clazz.getName());
            
            for (Method method : clazz.getMethods())
            {
                // m�thode
                writer.printf("\n\t@Override\n\tpublic void %s(%s event)\n", method.getName(), method.getParameterTypes()[0].getCanonicalName());
                writer.printf("\t{\n\t\tSystem.out.println(\"%s#%s\\t\"+event);\n\t}\n", clazz.getSimpleName(), method.getName());
            }
            
            writer.print("}\n");
            writer.close();
        }
        catch (IOException e)
        {
            System.err.println(e);
        }
    }
}
