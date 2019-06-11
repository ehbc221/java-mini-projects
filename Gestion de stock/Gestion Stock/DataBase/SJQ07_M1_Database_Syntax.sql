create  database SJQ07_M1

create table Article_Stock
(
ID_Article int primary key,
Nom_Article varchar(30) not null,
Categorie varchar(30) not null,
Quantite int not null
)
--insertion des donnees dans la table Article_Stock pour test
insert into Commande values(1,'Ordinateur','Electromenager',5)
insert into Commande values(2,'Pelle','Construction',2)
insert into Commande values(3,'Tapis','Decoration',4)
insert into Commande values(4,'Carte Mere P4','Maintenance',20)
insert into Commande values(5,'Cable Frein','Piece Rechange',9)
insert into Commande values(6,'Frigo','Electromenager',8)
insert into Commande values(7,'Echafaud','Construction',15)
insert into Commande values(8,'Peinture Huilée','Decoration',9)
insert into Commande values(9,'Gasiniére','Electromenager',1)
insert into Commande values(10,'Bourouette','Construction',12)
insert into Commande values(11,'Salon Fer Forgé','Decoration',7)
insert into Commande values(12,'Disque Dur','Maintenance',2)
insert into Commande values(13,'Tembour Frein','Piece Rechange',4)
insert into Commande values(14,'Ecran 21"','Electromenager',15)
insert into Commande values(15,'Brique Rouge','Construction',10)
insert into Commande values(16,'Creaux','Decoration',20)
insert into Commande values(17,'Barettes Ram','Maintenance',2)
insert into Commande values(18,'Bougie','Piece Rechange',3)
insert into Commande values(19,'Micro-Onde','Electromenager',1)
insert into Commande values(20,'Tuiles','Construction',14)
insert into Commande values(21,'Brosse-Peinture','Decoration',14)
insert into Commande values(22,'Cable-UTP','Maintenance',7)
insert into Commande values(23,'Phares R-19','Piece Rechange',2)
insert into Commande values(24,'Televiseur LED','Electromenager',9)
insert into Commande values(25,'Tuyaux','Construction',20)
insert into Commande values(26,'Porte a Rideaux','Decoration',2)
insert into Commande values(27,'Carte Graphique','Maintenance',11)
insert into Commande values(28,'Pare-Brise','Piece Rechange',1)
insert into Commande values(29,'Lecteur DVD','Electromenager',2)
insert into Commande values(30,'Soufleur','Construction',10)
insert into Commande values(31,'Tapis','Decoration',8)
insert into Commande values(32,'Carte Mere P4','Maintenance',2)
insert into Commande values(33,'Cable Frein','Piece Rechange',4)
insert into Commande values(34,'Ordinateur','Electromenager',5)
insert into Commande values(35,'Pelle','Construction',2)
insert into Commande values(36,'Tapis','Decoration',4)
insert into Commande values(37,'Carte Mere P4','Maintenance',1)
insert into Commande values(38,'Cable Frein','Piece Rechange',2)
insert into Commande values(39,'Ordinateur','Electromenager',5)
insert into Commande values(40,'Pelle','Construction',2)
insert into Commande values(41,'Tapis','Decoration',4)
insert into Commande values(42,'Carte Graphique','Maintenance',3)
insert into Commande values(43,'Cable Frein','Piece Rechange',1)
insert into Commande values(44,'Ordinateur','Electromenager',9)
insert into Commande values(45,'Pelle','Construction',4)
insert into Commande values(46,'Tapis','Decoration',1)
insert into Commande values(47,'Carte Mere P4','Maintenance',2)
insert into Commande values(48,'Cable Frein','Piece Rechange',5)
insert into Commande values(49,'Frigo','Electromenager',4)
insert into Commande values(50,'Echafaud','Construction',1)
insert into Commande values(51,'Peinture Huilée','Decoration',2)
insert into Commande values(52,'Gasiniére','Electromenager',5)
insert into Commande values(53,'Bourouette','Construction',3)
insert into Commande values(54,'Salon Fer Forgé','Decoration',3)
insert into Commande values(55,'Disque Dur','Maintenance',6)
insert into Commande values(56,'Tembour Frein','Piece Rechange',1)
insert into Commande values(57,'Ecran 21"','Electromenager',4)
insert into Commande values(58,'Brique Rouge','Construction',3)
insert into Commande values(59,'Creaux','Decoration',1)
insert into Commande values(60,'Barettes Ram','Maintenance',7)
insert into Commande values(61,'Bougie','Piece Rechange',8)
insert into Commande values(62,'Micro-Onde','Electromenager',15)
insert into Commande values(63,'Tuiles','Construction',4)
insert into Commande values(64,'Brosse-Peinture','Decoration',6)
insert into Commande values(65,'Cable-UTP','Maintenance',17)
insert into Commande values(66,'Phares R-19','Piece Rechange',8)
insert into Commande values(67,'Televiseur LED','Electromenager',5)
insert into Commande values(68,'Tuyaux','Construction',2)
insert into Commande values(69,'Porte a Rideaux','Decoration',12)
insert into Commande values(70,'Carte Graphique','Maintenance',2)
insert into Commande values(71,'Pare-Brise','Piece Rechange',10)
insert into Commande values(72,'Lecteur DVD','Electromenager',21)
insert into Commande values(73,'Soufleur','Construction',4)
insert into Commande values(74,'Tapis','Decoration',12)
insert into Commande values(75,'Carte Mere P4','Maintenance',15)
insert into Commande values(76,'Cable Frein','Piece Rechange',10)
insert into Commande values(77,'Ordinateur','Electromenager',15)
insert into Commande values(78,'Pelle','Construction',12)
insert into Commande values(79,'Tapis','Decoration',14)
insert into Commande values(80,'Carte Mere P4','Maintenance',11)
insert into Commande values(81,'Cable Frein','Piece Rechange',12)
insert into Commande values(82,'Ordinateur','Electromenager',15)
insert into Commande values(83,'Pelle','Construction',2)
insert into Commande values(84,'Tapis','Decoration',14)
insert into Commande values(85,'Carte Graphique','Maintenance',9)
insert into Commande values(86,'Cable Frein','Piece Rechange',10)
go

create table Commande
(
ID_Com int primary key,
Nom_Article varchar(30) not null,
Categorie varchar(30) not null,
Quantite int not null
)

create table Client
(
ID_Client int ,
ID_Article int ,
Nom_Article varchar(30),
Categorie varchar(30),
Quantite int
)

Create table Fournisseur
(
ID_Fournisseur int,
Nom_Fournisseur varchar(20),
Categorie_Fournie varchar(30),
Adresse varchar(30)
)

insert into Fournisseur values(1,'CCBM','Electromenager','Dakar/Rte de Rufisque')
insert into Fournisseur values(2,'Senegal Construction','Construction','2 Rue Galandou Diof')
insert into Fournisseur values(3,'Design_Soft','Decoration','Dakar/ Rue Fleurus')
insert into Fournisseur values(4,'Info_Solutions','Maintenance','Dakar-Ville/Centre Comercial 4C')
insert into Fournisseur values(5,'TADI','Piece Rechange','Thies/ Rte de Touba')
insert into Fournisseur values(6,'Carre Cons','Revetemen','Petersen')
insert into Fournisseur values(7,'ETSP','Informatique','Rue Carneaux')
insert into Fournisseur values(8,'SODIDA','Alimentation','Castor')
insert into Fournisseur values(9,'SOCOCIM','Electrivite','Rte de Rufisque')
insert into Fournisseur values(10,'So-Boutik','Habillement','Liberté 6')
insert into Fournisseur values(11,'ENMA','Alimentation Betaille','Dakar/Rte de Rufisque')
insert into Fournisseur values(12,'Sen_Soft','Bureautique','Dakar/Parcelles Assainies')
insert into Fournisseur values(13,'Design+','Design','Dakar/ Patte Doie')
insert into Fournisseur values(14,'Silhouette','Couture','Centre Comercial 4C')
insert into Fournisseur values(155,'AMA-ARCHI','Architecture','Almadies')
go

													--PROCEDURES--

--Procedure utilisée pour recuperer des données pour generer le diagramme des categorie en stock
create procedure Donnees_Diagramme_Categorie @Categorie varchar(30),@Quantite int output
as
begin
select @Quantite=sum(Quantite) from Article_Stock where Categorie=@Categorie
end

--Procedure utilisée pour recuperer des données pour generer le diagramme des qrticle en stock
create procedure Donnees_Diagramme_Article @Nom_Article varchar(30),@Quantite int output
as
begin
select @Quantite=sum(Quantite) from Article_Stock where Nom_Article=@Nom_Article
end