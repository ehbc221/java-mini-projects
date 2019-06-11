Intranet d'agences bancaires----------------------------
Url     : http://codes-sources.commentcamarche.net/source/54463-intranet-d-agences-bancairesAuteur  : lezjDate    : 07/08/2013
Licence :
=========

Ce document intitulé « Intranet d'agences bancaires » issu de CommentCaMarche
(codes-sources.commentcamarche.net) est mis à disposition sous les termes de
la licence Creative Commons. Vous pouvez copier, modifier des copies de cette
source, dans les conditions fixées par la licence, tant que cette note
apparaît clairement.

Description :
=============

Cette application est un mod&egrave;le d'application (client/serveur) de gestion
 d'agences bancaires en intranet. Le but de ce programme est de montrer comment 
mettre en oeuvre simplement une communication TCP/IP en java avec les sockets. I
l expose aussi l'utilisation des threads c&ocirc;t&eacute; serveur pour la gesti
on des connexions simultan&eacute;es et aussi la synchronisation des m&eacute;th
odes pour assurer la s&eacute;curisation des op&eacute;rations d'ajout. Les donn
&eacute;es sont stock&eacute;es dans une base MySQL. Et en bonus, j'ai aussi pro
gramm&eacute; un client andro&iuml;d mais par paresse je n'ai pas cr&eacute;&eac
ute; toutes les fen&ecirc;tres sur le client andro&iuml;d.
<br /><a name='sourc
e-exemple'></a><h2> Source / Exemple : </h2>
<br /><pre class='code' data-mode
='basic'>
Voici le code sql de la base de données:

-- phpMyAdmin SQL Dump
-
- version 3.2.0.1
-- <a href='http://www.phpmyadmin.net' target='_blank'>http:/
/www.phpmyadmin.net</a>
--
-- Serveur: localhost
-- Généré le : Dim 22 Juille
t 2012 à 06:26
-- Version du serveur: 5.1.36
-- Version de PHP: 5.3.0

SET S
QL_MODE=&quot;NO_AUTO_VALUE_ON_ZERO&quot;;

--
-- Base de données: `banque`

--

-- --------------------------------------------------------

--
-- Stru
cture de la table `agence`
--

CREATE TABLE IF NOT EXISTS `agence` (
  `num_
agence` int(11) NOT NULL AUTO_INCREMENT,
  `adresse` varchar(100) NOT NULL,
  
`libele` varchar(60) NOT NULL,
  PRIMARY KEY (`num_agence`)
) ENGINE=MyISAM  D
EFAULT CHARSET=latin1 AUTO_INCREMENT=61 ;

--
-- Contenu de la table `agence`

--

INSERT INTO `agence` (`num_agence`, `adresse`, `libele`) VALUES
(60, 'l
iberte 6', 'regis bank'),
(59, 'android city', 'android bank'),
(55, 'librevil
le', 'lez bank');

-- --------------------------------------------------------


--
-- Structure de la table `client`
--

CREATE TABLE IF NOT EXISTS `cli
ent` (
  `num_cli` int(11) NOT NULL AUTO_INCREMENT,
  `nom` varchar(60) NOT NU
LL,
  `prenom` varchar(60) NOT NULL,
  `adresse` varchar(100) NOT NULL,
  `nu
m_agence` int(11) NOT NULL,
  PRIMARY KEY (`num_cli`)
) ENGINE=MyISAM  DEFAULT
 CHARSET=latin1 AUTO_INCREMENT=36 ;

--
-- Contenu de la table `client`
--


INSERT INTO `client` (`num_cli`, `nom`, `prenom`, `adresse`, `num_agence`) VAL
UES
(35, 'shela', 'lekouga', 'wakam', 60),
(34, 'steakel', 'kely', 'liberte 6'
, 55),
(33, 'ebazogo', 'lionel', 'liberte 6', 59);

-- ----------------------
----------------------------------

--
-- Structure de la table `compte`
--


CREATE TABLE IF NOT EXISTS `compte` (
  `num_cpt` varchar(50) NOT NULL,
  `
solde` int(11) NOT NULL,
  `sens` varchar(10) NOT NULL,
  `num_cli` int(11) NO
T NULL,
  PRIMARY KEY (`num_cpt`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1;


--
-- Contenu de la table `compte`
--

INSERT INTO `compte` (`num_cpt`, `sol
de`, `sens`, `num_cli`) VALUES
('1', 10000, 'CR', 33);

-- ------------------
--------------------------------------

--
-- Structure de la table `operatio
n`
--

CREATE TABLE IF NOT EXISTS `operation` (
  `num_op` int(11) NOT NULL 
AUTO_INCREMENT,
  `libele` varchar(30) DEFAULT NULL,
  `montant` double NOT NU
LL,
  `sens` varchar(3) NOT NULL,
  `date_op` varchar(10) NOT NULL,
  `num_cp
t` varchar(50) NOT NULL,
  PRIMARY KEY (`num_op`)
) ENGINE=MyISAM  DEFAULT CHA
RSET=latin1 AUTO_INCREMENT=67 ;

--
-- Contenu de la table `operation`
--


INSERT INTO `operation` (`num_op`, `libele`, `montant`, `sens`, `date_op`, `num
_cpt`) VALUES
(66, NULL, 10000, 'CR', '2012-07-10', '1');
</pre>
<br /><a nam
e='conclusion'></a><h2> Conclusion : </h2>
<br />Peut-&ecirc;tre qu'un tutorie
l sortira dessus si les choses ne sont pas claires et que la demande se fat sent
ir.
<br />Sinon, amusez-vous bien!
<br />telephone : 00221 77 705 25 91
<br /
>e-mails: ebazogo@yahoo.fr / lezagome@gmail.com / lezj.lezagome@facebook.com
<b
r />facebook : <a href='http://www.facebook.com/lezj.lezagome' target='_blank'>h
ttp://www.facebook.com/lezj.lezagome</a>
<br />youtube : <a href='http://www.yo
utube.com/user/lezagome' target='_blank'>http://www.youtube.com/user/lezagome</a
>
<br />twitter: <a href='https://twitter.com/lezagome' target='_blank'>https:/
/twitter.com/lezagome</a>
<br /><a href='http://www.linkedin.com/profile/view?i
d=138761972' target='_blank'>http://www.linkedin.com/profile/view?id=138761972</
a>
