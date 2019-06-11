Tarot et belote---------------
Url     : http://codes-sources.commentcamarche.net/source/51128-tarot-et-beloteAuteur  : mercierdesDate    : 11/08/2013
Licence :
=========

Ce document intitulé « Tarot et belote » issu de CommentCaMarche
(codes-sources.commentcamarche.net) est mis à disposition sous les termes de
la licence Creative Commons. Vous pouvez copier, modifier des copies de cette
source, dans les conditions fixées par la licence, tant que cette note
apparaît clairement.

Description :
=============

Pour que le jeu fonctionne sous Windows ou Linux, il faut le jdk et le jre.
<br
 />J'ai enlev&eacute; le solitaire, car il y avait un probl&egrave;me d'unicit&e
acute; de la carte pour le spider solitaire.
<br />J'ai enlev&eacute; le pr&eac
ute;sident, car il y avait un probl&egrave;me &agrave; r&eacute;soudre pour la f
in de chaque partie.
<br />Il ne reste que le tarot et la belote
<br />Je peux
 les rajouter d&egrave;s que j'aurai une solution &agrave; ces probl&egrave;mes.

<br />Nouveau nom pour le code source: &quot;tarot et belote&quot;.
<br />Le 
zip contient des batchs pour:
<br />	1. cr&eacute;er un jar ex&eacute;cutable.

<br />	2. cr&eacute;er la documentation java.
<br />	3. supprimer le jar ex&ea
cute;cutable et les fichiers ou dossiers install&eacute;s.
<br />Attention, si 
les noms des paquetages sont chang&eacute;s ou si certains paquetages sont suppr
im&eacute;s ou ajout&eacute;s, la compilation ne marche plus
<br />J'ai commenc
&eacute; &agrave; encoder les cha&icirc;nes de caract&egrave;res.
<br />J'ai su
pprim&eacute; l'ancienne intelligence artificielle trop lourde pour l'&eacute;va
luation du contrat au tarot et je l'ai remplac&eacute;.
<br />J'ai surtout &eac
ute;dit&eacute; une intelligence artificielle pour le tarot et la belote.
<br /
>Si cette intelligence artificielle ne me pla&icirc;t pas,
<br />j'essaierai de
 l'am&eacute;liorer.
<br />Le style graphique du logiciel est celui de Windows.

<br />Les cartes sont dessin&eacute;es par Java, donc il n'y a pas besoin de l
es stocker dans des fichiers images.
<br />Vous pourrez utiliser l'aide g&eacut
e;n&eacute;rale.
<br />Pour le jeux des cartes et les annonces des contrats, j'
ai &eacute;dit&eacute; des classes h&eacute;ritant de Thread.
<br />Voici une p
remi&egrave;re version du logiciel avec la belote et le tarot.
<br />Les classe
s sont hi&eacute;rarchis&eacute;es par paquetage.
<br />Pour les &eacute;couteu
rs de bouton, des classes internes aux classes de fen&ecirc;tre ou de bo&icirc;t
e de dialogue ou des classes anonymes sont utilis&eacute;es.
<br />Il est possi
ble de temporiser le jeu par les d&eacute;lais entre deux cartes cons&eacute;cut
ives, deux plis cons&eacute;cutifs et deux annonces cons&eacute;cutives,
<br />
ou par le clic sur un bouton pour passer au pli suivant.
<br />La classe princi
pale (contenant le &quot;main&quot;) s'appelle ClassePrincipale.
<br />	1. Au p
remier lancement du logiciel, une premi&egrave;re bo&icirc;te de dialogue appara
&icirc;t,
<br />		elle demande une information &agrave; propos du syst&egrave;m
e d'exploitation utilis&eacute;.
<br />		Puis, un fichier de param&egrave;tres 
contenant un objet Java est cr&eacute;&eacute;.
<br />		Puis, les dossiers de s
auvegarde et de paquets contenant des fichiers permettant de stocker les paquets
 de cartes sont cr&eacute;&eacute;s.
<br />	2. Au lancement suivant, si des fic
hiers ou dossiers importants sont supprim&eacute;s, ils seront r&eacute;install&
eacute;s.
<br />	3. Apr&egrave;s les &eacute;ventuelles cr&eacute;ations de fic
hiers ou de dossiers, la fen&ecirc;tre principale s'ouvre.
<br />	4. A la ferme
ture du logiciel, les coordonn&eacute;es du logiciel sont enregistr&eacute;es da
ns un fichier pour objet
<br />		et le fichier est supprim&eacute; apr&egrave;s
 que la fen&ecirc;tre principale soit visible.
<br />
<br />A la fin de chaque
 partie de belote ou de tarot, un bilan r&eacute;capitulatif des scores, des mai
ns et plis est faits.
<br />Des courbes temporelles de scores centr&eacute;es s
eront visibles pour une partie al&eacute;atoire.
<br />Il est possible de voir 
une simulation d'une partie de belote ou de tarot.
<br />Pr&eacute;c&eacute;dan
t la simulation, la barre de chargement indique l'avancement de la partie.
