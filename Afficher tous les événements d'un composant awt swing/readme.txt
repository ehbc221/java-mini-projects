Afficher tous les événements d'un composant AWT/Swing-----------------------------------------------------
Url     : http://codes-sources.commentcamarche.net/source/100424-afficher-tous-les-evenements-d-un-composant-awt-swingAuteur  : KXDate    : 22/02/2014
Licence :
=========

Ce document intitulé « Afficher tous les événements d'un composant AWT/Swing » issu de CommentCaMarche
(codes-sources.commentcamarche.net) est mis à disposition sous les termes de
la licence Creative Commons. Vous pouvez copier, modifier des copies de cette
source, dans les conditions fixées par la licence, tant que cette note
apparaît clairement.

Description :
=============

Peut-&ecirc;tre avez vous d&eacute;j&agrave; perdu de pr&eacute;cieuses minutes 
lors de la conception de votre interface graphique Swing &agrave; rechercher que
l Listener permettrait de r&eacute;cup&eacute;rer l'&eacute;v&eacute;nement exac
t que vous souhaitez intercepter.
<br />
<br />Le code propos&eacute; ici perm
et d'ajouter automatiquement tous les Listener disponibles pour le composant, de
 mani&egrave;re &agrave; avoir une trace en console de tous les &eacute;v&eacute
;nements.
<br />
<br />Voici un exemple d'utilisation :
<br />
<br /><pre cl
ass="code" data-mode="java">import java.io.File;

import javax.swing.JFrame;

im
port ccm.kx.swingTools.ListenerLogger;

public class Test
{
    public static vo
id main(String[] args)
    {
        // Définit où générer les sources
        L
istenerLogger.setPath(new File("src"));
        
        JFrame frame = new JFra
me();
        
        // Ajoute tous les Listener à 'frame'
        ListenerLog
ger.logListeners(frame);
        
        frame.setSize(200, 200);
        frame
.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }
}</pre>
<br />
<br />L'ex&eacute;cution se passe en deux temps. Au prem
ier d&eacute;marrage le programme va essayer de charger une impl&eacute;mentatio
n de chaque Listener. Comme celles-ci n'existent pas encore le programme va g&ea
cute;n&eacute;rer le code source manquant dans le r&eacute;pertoire source sp&ea
cute;cifi&eacute;. Le message suivant sera alors indiqu&eacute; :
<br />
<br /
><pre class="code" data-mode="java">Une ou plusieurs implémentations de listener
s sont manquantes : ...
Leur code source a été généré dans le répertoire ...\ccm
\kx\swingTools
Pour les prendre en charge, redémarrer le programme après avoir c
ompilé ces nouvelles sources.</pre>
<br />
<br />Comme indiqu&eacute;, les sou
rces ont &eacute;t&eacute; g&eacute;n&eacute;r&eacute;es &agrave; l'endroit indi
qu&eacute;s par le param&egrave;tre ListenerLogger.setPath(String), ce qui corre
spond normalement au dossier contenant le code ListenerLogger.java 
<br />
<br
 />Voici par exemple &agrave; quoi ressemble le code source g&eacute;n&eacute;r&
eacute; pour un FocusListener :
<br />
<br /><pre class="code" data-mode="java
">package ccm.kx.swingTools;

/**
 * FocusListener's implementation, automatical
ly generated to be invoked by {@link ListenerLogger#logListeners(java.awt.Compon
ent...)}.
 * @author KX
 * @see java.awt.event.FocusListener
 */
public class Li
stenerLogger$java_awt_event_FocusListener implements java.awt.event.FocusListene
r
{
	@Override
	public void focusGained(java.awt.event.FocusEvent event)
	{
		Sy
stem.out.println("FocusListener#focusGained\t"+event);
	}

	@Override
	public vo
id focusLost(java.awt.event.FocusEvent event)
	{
		System.out.println("FocusList
ener#focusLost\t"+event);
	}
}
</pre>
<br />
<br />Il faudra alors compiler ce
s sources et relancer le programme. Au deuxi&egrave;me d&eacute;marrage, puisque
 d&eacute;sormais les classes existent, elles seront automatiquement ajout&eacut
e;es et chaque &eacute;v&eacute;nement sera affich&eacute;.
<br />
<br />Il su
ffit donc de regarder la console au moment o&ugrave; l'on effectue l'&eacute;v&e
acute;nement que l'on souhaite intercepter pour le voir s'afficher et l'identifi
er. Reste alors &agrave; l'impl&eacute;menter comme il faut.
