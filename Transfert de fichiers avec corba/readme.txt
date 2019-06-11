Application de transfert de fichiers avec corba-----------------------------------------------
Url     : http://codes-sources.commentcamarche.net/source/21616-application-de-transfert-de-fichiers-avec-corbaAuteur  : vinvayDate    : 09/08/2013
Licence :
=========

Ce document intitulé « Application de transfert de fichiers avec corba » issu de CommentCaMarche
(codes-sources.commentcamarche.net) est mis à disposition sous les termes de
la licence Creative Commons. Vous pouvez copier, modifier des copies de cette
source, dans les conditions fixées par la licence, tant que cette note
apparaît clairement.

Description :
=============

Le zip contient deux repertoires, un pour les sources du client, l'autre pour le
s sources du serveur.
<br />Pour l'utiliser, lancer d'abord 'NameServ.bat' puis
 taper la commande : java FileServer -ORBInitialPort 2500 -ORBInitialHost xxx.xx
x.xxx.xxx
<br />java FileClient -ORBInitialPort 2500 -ORBInitialHost xxx.xxx.xx
x.xxx.
<br />O&ugrave; xxx.xxx.xxx.xxx est l'adresse IP de la machine sur laque
lle tourne le service de nommage (NameServ.bat), elle n'est pas n&eacute;cessair
e si tout tourne sur la m&ecirc;me machine.
<br />
<br />En cliquant sur le bo
uton 'To', vous obtenez le nom des machines connecter au serveur et s&eacute;lec
tionnez le destinataire de votre fichier. Le bouton 'Browse' comme son nom l'ind
ique sert &agrave; chercher le fichier que vous voulez envoyez.
<br />Losque vo
us allez cliquer sur le bouton 'Send', un message va s'afficher sur la machine d
estinataire, lui demandant si elle veut recevoir ce fichier ou non.
<br />
<br
 />Ceci n'est qu'une deux&egrave;me version que je vais essayer d'am&eacute;lior
er, comme par exemple :
<br />- je vais ajouter la possibilit&eacute; de mettre
 plusieurs destinataires.
<br />- je vais faire aussi que si plusieurs fichiers
 ou un r&eacute;pertoire sont s&eacute;lectionn&eacute;s, ceux-ci soient zipp&ea
cute;s avant d'&ecirc;tre envoy&eacute;s.
<br />- je vais aussi arranger l'inte
rface graphique.
<br />
<br />En plus par rapport &agrave; la premi&egrave;re 
version :
<br />- la possibilit&eacute; d'envoyer de tr&egrave;s gros fichier :
 j'ai d&eacute;j&agrave; essay&eacute; avec un fichier de 1.69 Go, cela ne pose 
aucun probl&egrave;me.
<br />
<br />Si vous avez des suggestions ou des remarq
ues, n'h&eacute;sitez pas.
