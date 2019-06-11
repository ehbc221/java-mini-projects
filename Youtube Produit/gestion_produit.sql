-- phpMyAdmin SQL Dump
-- version 4.0.4
-- http://www.phpmyadmin.net
--
-- Client: localhost
-- Généré le: Sam 08 Novembre 2014 à 18:50
-- Version du serveur: 5.6.12-log
-- Version de PHP: 5.4.16

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de données: `gestion_produit`
--
CREATE DATABASE IF NOT EXISTS `gestion_produit` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `gestion_produit`;

-- --------------------------------------------------------

--
-- Structure de la table `produit`
--

CREATE TABLE IF NOT EXISTS `produit` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code_produit` varchar(20) NOT NULL,
  `reference` varchar(50) NOT NULL,
  `deseignation` varchar(52) NOT NULL,
  `rangement` varchar(50) DEFAULT NULL,
  `fournisseur` varchar(56) DEFAULT NULL,
  `remise` int(11) NOT NULL,
  `prix` int(11) NOT NULL,
  `stock` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=7 ;

--
-- Contenu de la table `produit`
--

INSERT INTO `produit` (`id`, `code_produit`, `reference`, `deseignation`, `rangement`, `fournisseur`, `remise`, `prix`, `stock`) VALUES
(1, '1', 'REF1', 'DES1', 'R11', 'F1', 10, 1200, 80),
(2, '568', 'REF2', 'DES2', 'R118', 'F12', 7, 700, 144),
(3, '5685', 'REF256', 'DES25', 'R112', 'F12', 7, 700, 174),
(4, '2', 'REF2', 'DES2', 'R118', 'F12', 7, 700, 100),
(6, '56856', 'REF2', 'DES2', 'R112', 'F12', 7, 700, 199);

-- --------------------------------------------------------

--
-- Structure de la table `utilisateur`
--

CREATE TABLE IF NOT EXISTS `utilisateur` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_user` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `type` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5 ;

--
-- Contenu de la table `utilisateur`
--

INSERT INTO `utilisateur` (`id`, `id_user`, `username`, `password`, `type`) VALUES
(2, 25, 'hafil', 'ismail', 'directeur'),
(3, 23, 'youssef', 'mamouni_alaoui', 'Cashier'),
(4, 22, 'admin', 'admin', 'Cashier');

-- --------------------------------------------------------

--
-- Structure de la table `vente`
--

CREATE TABLE IF NOT EXISTS `vente` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `num_facture` int(11) NOT NULL,
  `code_produit` int(11) NOT NULL,
  `reference` varchar(55) NOT NULL,
  `prix_vente` int(11) NOT NULL,
  `stock_sortie` int(11) NOT NULL,
  `subtotal` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=12 ;

--
-- Contenu de la table `vente`
--

INSERT INTO `vente` (`id`, `num_facture`, `code_produit`, `reference`, `prix_vente`, `stock_sortie`, `subtotal`) VALUES
(3, 1, 568, 'REF2', 651, 1, 1080),
(4, 1, 568, 'REF2', 651, 2, 1302),
(5, 1, 568, 'REF2', 651, 2, 1302),
(6, 1, 5685, 'REF256', 651, 3, 1953),
(7, 2, 568, 'REF2', 651, 6, 3906),
(8, 2, 568, 'REF2', 651, 12, 7812),
(9, 1, 5685, 'REF256', 651, 10, 6510),
(10, 2, 2, 'REF2', 651, 100, 65100),
(11, 2, 2, 'REF2', 651, 100, 65100);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
