-- phpMyAdmin SQL Dump
-- version 3.5.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jan 06, 2015 at 05:01 PM
-- Server version: 5.5.24-log
-- PHP Version: 5.4.3

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `caisse`
--

-- --------------------------------------------------------

--
-- Table structure for table `article`
--

CREATE TABLE IF NOT EXISTS `article` (
  `codeArticle` mediumint(9) NOT NULL,
  `referenceArticle` varchar(20) NOT NULL,
  `designationArticle` varchar(20) NOT NULL,
  `nomArticle` varchar(30) NOT NULL,
  `prixArticle` double NOT NULL,
  PRIMARY KEY (`codeArticle`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `article`
--

INSERT INTO `article` (`codeArticle`, `referenceArticle`, `designationArticle`, `nomArticle`, `prixArticle`) VALUES
(1234567, 'article1234567', 'papierRAM', 'papierA4', 7000),
(1234568, 'article1234568', 'portemine05', 'portemine', 1250);

-- --------------------------------------------------------

--
-- Table structure for table `caissier`
--

CREATE TABLE IF NOT EXISTS `caissier` (
  `idCaissier` mediumint(9) NOT NULL,
  `passwordCaissier` varchar(20) NOT NULL,
  `nomCaissier` varchar(30) NOT NULL,
  PRIMARY KEY (`idCaissier`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `caissier`
--

INSERT INTO `caissier` (`idCaissier`, `passwordCaissier`, `nomCaissier`) VALUES
(111111, 'caissier111111', 'Ahmed'),
(111112, 'caissier111112', 'Bachir'),
(111113, 'caissier111113', 'Salah'),
(111114, 'caissier111114', 'Khaled'),
(111115, 'caissier111115', 'Ali');

-- --------------------------------------------------------

--
-- Table structure for table `lot`
--

CREATE TABLE IF NOT EXISTS `lot` (
  `codeLot` mediumint(9) NOT NULL,
  `referenceLot` varchar(20) NOT NULL,
  `designationLot` varchar(20) NOT NULL,
  `nomLot` varchar(30) NOT NULL,
  `prixLot` double NOT NULL,
  PRIMARY KEY (`codeLot`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `lot`
--

INSERT INTO `lot` (`codeLot`, `referenceLot`, `designationLot`, `nomLot`, `prixLot`) VALUES
(123456, 'lot1234567', 'stylotbic', 'stylot', 5000),
(123457, 'lot1234568', 'stylotpointfine', 'stylot', 6000);

-- --------------------------------------------------------

--
-- Table structure for table `stock`
--

CREATE TABLE IF NOT EXISTS `stock` (
  `idStock` int(11) NOT NULL,
  `stockU` int(11) NOT NULL,
  `stockLot` int(11) NOT NULL,
  `chiffreArticle` double NOT NULL,
  `chiffreLot` double NOT NULL,
  `chiffreAffaire` double NOT NULL,
  PRIMARY KEY (`idStock`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `stock`
--

INSERT INTO `stock` (`idStock`, `stockU`, `stockLot`, `chiffreArticle`, `chiffreLot`, `chiffreAffaire`) VALUES
(123456, 1000, 50, 0, 0, 0);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
