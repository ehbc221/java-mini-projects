-- phpMyAdmin SQL Dump
-- version 4.0.4
-- http://www.phpmyadmin.net
--
-- Client: localhost
-- Généré le: Jeu 04 Septembre 2014 à 16:39
-- Version du serveur: 5.6.12-log
-- Version de PHP: 5.4.16

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de données: `etude`
--
CREATE DATABASE IF NOT EXISTS `etude` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `etude`;

-- --------------------------------------------------------

--
-- Structure de la table `etudient`
--

CREATE TABLE IF NOT EXISTS `etudient` (
  `id` int(11) DEFAULT NULL,
  `Nom` varchar(50) DEFAULT NULL,
  `Prenom` varchar(50) DEFAULT NULL,
  `branche` varchar(50) DEFAULT NULL,
  `note` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Contenu de la table `etudient`
--

INSERT INTO `etudient` (`id`, `Nom`, `Prenom`, `branche`, `note`) VALUES
(1, 'hafil', 'liamsi', 'IGE', 13),
(2, 'hanafi', 'martin', 'SM', 14),
(3, 'ahmadi', 'ahmadi', 'SVT', 12),
(4, 'hamid', 'mamouni alaoui', 'SH', 17);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
