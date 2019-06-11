Téléchargement ftp et envoi email---------------------------------
Url     : http://codes-sources.commentcamarche.net/source/54925-telechargement-ftp-et-envoi-emailAuteur  : imothepe_33Date    : 05/08/2013
Licence :
=========

Ce document intitulé « Téléchargement ftp et envoi email » issu de CommentCaMarche
(codes-sources.commentcamarche.net) est mis à disposition sous les termes de
la licence Creative Commons. Vous pouvez copier, modifier des copies de cette
source, dans les conditions fixées par la licence, tant que cette note
apparaît clairement.

Description :
=============

Salut,
<br />
<br />Il s'agit d'une application qui permet de r&eacute;cup&eac
ute;rer un fichier sur un serveur FTP et d'en diffuser le contenu par mail.
<br
 />Avant toute utilisation, il faut :
<br />
<br />- saisir les param&egrave;t
res de connexion &agrave; un compte Google existant (dans le code source, pr&eac
ute;cis&eacute;ment dans la classe EmailReport.java &agrave; la ligne 20 et 21.

<br />- remplir les informations dans le fichier de configuration config.proper
ties &agrave; savoir : 
<br />#Login du compte FTP
<br />config.login=
<br />
#Password du compte FTP
<br />config.password=
<br />#Adresse IP du serveur FT
P
<br />config.server=
<br />#Chemin d'acc&egrave;s du fichier sur le serveur 
FTP
<br />config.remotepath=
<br />#Chemin d'acc&egrave;s du dossier local de 
t&eacute;l&eacute;chargement du fichier
<br />config.localpath=
<br />#Nom du 
fichier &agrave; t&eacute;l&eacute;charger
<br />config.file=
<br />#Liste des
 destinataires (s&eacute;par&eacute; par des ,)
<br />config.recipients=
<br /
>
<br />Une fois le fichier t&eacute;l&eacute;charg&eacute;, son contenu est en
voy&eacute; au destinataires.
<br /><a name='source-exemple'></a><h2> Source / 
Exemple : </h2>
<br /><pre class='code' data-mode='basic'>
Il s'agit d'une ap
plication console.
</pre>
<br /><a name='conclusion'></a><h2> Conclusion : </h
2>
<br />Cette application peut servir &agrave; monitorer des syst&egrave;mes 
en t&eacute;l&eacute;chargeant des fichiers log qui y sont g&eacute;n&eacute;r&e
acute;s.
<br />Merci de me contacter en cas de soucis et de me donner vos appr&
eacute;ciations.
<br />
<br />Happy Coding!
