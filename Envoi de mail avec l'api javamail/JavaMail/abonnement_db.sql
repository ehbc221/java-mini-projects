-- phpMyAdmin SQL Dump
-- version 3.5.8.1
-- http://www.phpmyadmin.net
--
-- Client: 127.0.0.1
-- Généré le: Jeu 13 Mars 2014 à 20:06
-- Version du serveur: 5.6.11-log
-- Version de PHP: 5.4.14

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de données: `abonnement_db`
--

-- --------------------------------------------------------

--
-- Structure de la table `abonnement`
--

CREATE TABLE IF NOT EXISTS `abonnement` (
  `id_abonnement` int(11) NOT NULL AUTO_INCREMENT,
  `id_client` int(11) NOT NULL DEFAULT '0',
  `date_debut` date NOT NULL DEFAULT '0000-00-00',
  `date_fin` date NOT NULL DEFAULT '0000-00-00',
  `etat_abonnement` tinyint(4) NOT NULL DEFAULT '0',
  `couper_client` tinyint(4) NOT NULL DEFAULT '0',
  `send_mail_client` tinyint(4) NOT NULL DEFAULT '0',
  `send_mail_charge_compte` tinyint(4) NOT NULL DEFAULT '0',
  `facture_regle` tinyint(4) NOT NULL DEFAULT '0',
  `observation` text NOT NULL,
  `id_connexion` int(11) NOT NULL DEFAULT '0',
  `id_charge_compte` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id_abonnement`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=45 ;

--
-- Contenu de la table `abonnement`
--

INSERT INTO `abonnement` (`id_abonnement`, `id_client`, `date_debut`, `date_fin`, `etat_abonnement`, `couper_client`, `send_mail_client`, `send_mail_charge_compte`, `facture_regle`, `observation`, `id_connexion`, `id_charge_compte`) VALUES
(1, 1, '2014-03-13', '2014-05-15', 1, 0, 1, 1, 0, '', 0, 2),
(2, 2, '2014-03-13', '2014-04-24', 1, 0, 1, 1, 0, '', 0, 1);

-- --------------------------------------------------------

--
-- Structure de la table `abonnement_contact`
--

CREATE TABLE IF NOT EXISTS `abonnement_contact` (
  `id_abonnement` int(11) NOT NULL DEFAULT '0',
  `id_contatct` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id_abonnement`,`id_contatct`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Contenu de la table `abonnement_contact`
--

INSERT INTO `abonnement_contact` (`id_abonnement`, `id_contatct`) VALUES
(1, 1),
(1, 2),
(2, 1),
(2, 2);

-- --------------------------------------------------------

--
-- Structure de la table `charge_compte`
--

CREATE TABLE IF NOT EXISTS `charge_compte` (
  `id` int(30) NOT NULL AUTO_INCREMENT,
  `Nom` varchar(100) DEFAULT 'Nom revendeur-X',
  `Prenom` varchar(100) DEFAULT 'Prenom revendeur-X',
  `Adresse` varchar(100) DEFAULT 'Adresse  X',
  `Telephone` varchar(50) DEFAULT '021 00 00 00',
  `Portable` varchar(50) DEFAULT '070 00 00 00',
  `Email` varchar(40) DEFAULT '@',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=4 ;

--
-- Contenu de la table `charge_compte`
--

INSERT INTO `charge_compte` (`id`, `Nom`, `Prenom`, `Adresse`, `Telephone`, `Portable`, `Email`) VALUES
(1, 'Nom revendeur-1', 'Prenom revendeur-1', 'Adresse  1', '021 00 00 00', '070 00 00 00', 'rv1@comp.com'),
(2, 'Nom revendeur-2', 'Prenom revendeur-2', 'Adresse  2', '021 00 00 00', '070 00 00 00', 'rv2@comp.com');

-- --------------------------------------------------------

--
-- Structure de la table `clients`
--

CREATE TABLE IF NOT EXISTS `clients` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `Nom` varchar(100) DEFAULT 'Client  X',
  `Adresse` varchar(255) DEFAULT 'Adresse  X',
  `Telephone` varchar(16) DEFAULT '021 00 00 00',
  `Portable` varchar(16) DEFAULT '070 00 00 00',
  `Willaya_id` int(10) DEFAULT NULL,
  `Fax` varchar(20) DEFAULT '021000000',
  `Email` varchar(30) DEFAULT '@',
  `Contact` varchar(100) DEFAULT 'contact',
  `Fonction` varchar(120) DEFAULT 'fonction contact',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 DELAY_KEY_WRITE=1 COMMENT='Tables des Clients' AUTO_INCREMENT=3 ;

--
-- Contenu de la table `clients`
--

INSERT INTO `clients` (`id`, `Nom`, `Adresse`, `Telephone`, `Portable`, `Willaya_id`, `Fax`, `Email`, `Contact`, `Fonction`) VALUES
(1, 'Client  1', 'Adresse  X1', '021 00 00 00', '070 00 00 00', 16, '021000000', 'client.1@client.com', 'contact', 'fonction contact'),
(2, 'Client  2', 'Adresse  2', '021 00 00 00', '070 00 00 00', 16, '021000000', 'client.2@client2.com', 'contact', 'fonction contact');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
