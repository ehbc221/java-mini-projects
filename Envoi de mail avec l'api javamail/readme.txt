Envoi de mail avec l'api javamail---------------------------------
Url     : http://codes-sources.commentcamarche.net/source/55205-envoi-de-mail-avec-l-api-javamailAuteur  : medkhdDate    : 14/03/2014
Licence :
=========

Ce document intitulé « Envoi de mail avec l'api javamail » issu de CommentCaMarche
(codes-sources.commentcamarche.net) est mis à disposition sous les termes de
la licence Creative Commons. Vous pouvez copier, modifier des copies de cette
source, dans les conditions fixées par la licence, tant que cette note
apparaît clairement.

Description :
=============

1 ) Description de besoin: 
<br />Pour la suite du projet &quot;UTILISATION DE 
SSH ET MYSQL EN JAVA&quot; que vous trouverez ici :<a href='http://www.javafr.co
m/codes/UTILISATION-SSH-MYSQL-JAVA_55140.aspx' rel='nofollow' target='_blank'>ht
tp://www.javafr.com/codes/UTILISATION-SSH-MYSQL-JAVA_55140.aspx</a> 
<br />Aujo
urd'hui, je vais aborder une nouvelle &eacute;tape de mon projet et qui consiste
 &agrave; envoyer des mails de notifications. Pour rappel le but de ce projet es
t la gestion des abonnements. Donc on va mettre en place une procedure d'envoi d
e mail aux clients est charg&eacute;s de comptes. 
<br />
<br />2 ) L'environn
ement et mise en place 
<br />une machine Linux (Debian), Mysql, JVM 
<br />mo
n application tourne en cron, et s'execute chaque 24 heurs 
<br />
<br />3) L'
application 
<br />Avant de commencer, il faut avoir la librairie JavaMail que 
vous trouvrer dans ce lient <a href='http://www.oracle.com/technetwork/java/java
sebusiness/downloads/java-archive-downloads-eeplat-419426.html#javamail-1.4.7-ot
h-JPR.' rel='nofollow' target='_blank'>http://www.oracle.com/technetwork/java/ja
vasebusiness/downloads/java-archive-downloads-eeplat-419426.html#javamail-1.4.7-
oth-JPR.</a> ainsi que le connecteur Mysql et que vous pouvez le trouver ici <a 
href='http://cdn.mysql.com/Downloads/Connector-J/mysql-c' rel='nofollow' target=
'_blank'>http://cdn.mysql.com/Downloads/Connector-J/mysql-c</a> onnector-java-5.
1.25.zip. 
<br />cette application des constituer de 5 classes regroup&eacute;s
 dans 3 pacquages. 
<br /><a name='source-exemple'></a><h2> Source / Exemple : 
</h2>
<br /><pre class="code" data-mode="basic"> package javamail; import enti
ty.Abonnement; import entity.ChargeCompte; import java.text.SimpleDateFormat; im
port java.util.Date; import java.util.List; import java.util.Properties; import 
javax.mail.Message; import javax.mail.MessagingException; import javax.mail.Sess
ion; import javax.mail.Transport; import javax.mail.internet.InternetAddress; im
port javax.mail.internet.MimeMessage; import session.AbonnementFacade; public cl
ass JavaMail {     public JavaMail() {     }     public void sendMail(String to,
 String subject, String messsage) {         Properties props = new Properties();
         props.put("mail.smtp.host", "mail.dom.cc");         Session session = S
ession.getInstance(props, null);         try {             MimeMessage msg = new
 MimeMessage(session);             msg.setFrom(new InternetAddress("service.clie
nt@slc.dz"));             msg.setRecipients(Message.RecipientType.TO, to);      
       //msg.setRecipients(Message.RecipientType.CC, CC_LIST);             //msg
.setRecipients(Message.RecipientType.BCC, BCC_LIST);             msg.setSubject(
subject);             msg.setSentDate(new Date());             msg.setText(messs
age, "utf-8", "html");             Transport.send(msg);         } catch (Messagi
ngException mex) {             System.out.println("send failed, exception: " + m
ex);         }     }     public void mailRappelFinAbonnement() {           Abonn
ementFacade abonnementFacade = new AbonnementFacade();         List&lt;ChargeCom
pte&gt; listMailChargeClientPourRappelEtablierFacture = abonnementFacade.getList
MailChargeClient();         for (ChargeCompte chargeCompte : listMailChargeClien
tPourRappelEtablierFacture) {             if (!chargeCompte.getAbonnementsList()
.isEmpty()) {                 String msg = "Bonjour " + chargeCompte.getPrenom()
 + "&lt;br/&gt;";                 String j = "";                 for (Abonnement
 abonnement : chargeCompte.getAbonnementsList()) {                     j += "&lt
;tr&gt;"                             + "&lt;td bgcolor=\"#FFFFFF\" valign=\"top\
" align=\"left\"&gt;" + abonnement.getNomClient() + "&lt;/td&gt;"               
              + "&lt;td bgcolor=\"#FFFFFF\" valign=\"top\" align=\"left\"&gt;" +
 abonnement.getContact() + "&lt;/td&gt;"                             + "&lt;td b
gcolor=\"#FFFFFF\" valign=\"top\" align=\"left\"&gt;" + abonnement.getEmailClien
t() + "&lt;/td&gt;"                             + "&lt;td bgcolor=\"#FFFFFF\" va
lign=\"top\" align=\"left\"&gt;" + new SimpleDateFormat("dd/MM/YYYY").format(abo
nnement.getDateDebut()) + "&lt;/td&gt;"                             + "&lt;td bg
color=\"#FFFFFF\" valign=\"top\" align=\"left\"&gt;" + new SimpleDateFormat("dd/
MM/YYYY").format(abonnement.getDateFin()) + "&lt;/td&gt;"                       
      + "&lt;td bgcolor=\"#FFFFFF\" valign=\"top\" align=\"left\"&gt;" + abonnem
ent.getReste() + "&lt;/td&gt;"                             + "&lt;/tr&gt;";     
            }                 msg += "Les abonnenements des clients suivants tou
cheront a leurs fin dans quelques jours. Veuillez leurs préparer les factures po
ur les prochains abonnements" + "&lt;br/&gt;";                 msg += "Merci" + 
"&lt;br/&gt;&lt;br/&gt;&lt;br/&gt;";                 msg += "&lt;table width=\"1
00%\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\" bgcolor=\"#333333\"&gt;";
                 msg += "&lt;tr bgcolor=\"#0acff1\"&gt;";                 msg +=
 "&lt;td width=\"30%\"&gt;&lt;strong&gt;&lt;font color=\"#FFFFFF\"&gt;Client&lt;
/font&gt;&lt;/strong&gt;&lt;/td&gt;";                 msg += "&lt;td width=\"30%
\"&gt;&lt;strong&gt;&lt;font color=\"#FFFFFF\"&gt;Contact&lt;/font&gt;&lt;/stron
g&gt;&lt;/td&gt;";                 msg += "&lt;td width=\"30%\"&gt;&lt;strong&gt
;&lt;font color=\"#FFFFFF\"&gt;E-mail&lt;/font&gt;&lt;/strong&gt;&lt;/td&gt;";  
               msg += "&lt;td width=\"30%\"&gt;&lt;strong&gt;&lt;font color=\"#F
FFFFF\"&gt;Date debut&lt;/font&gt;&lt;/strong&gt;&lt;/td&gt;";                 m
sg += "&lt;td width=\"30%\"&gt;&lt;strong&gt;&lt;font color=\"#FFFFFF\"&gt;Date 
fin&lt;/font&gt;&lt;/strong&gt;&lt;/td&gt;";                 msg += "&lt;td widt
h=\"30%\"&gt;&lt;strong&gt;&lt;font color=\"#FFFFFF\"&gt;Jours restant avant fin
 d'abonnement&lt;/font&gt;&lt;/strong&gt;&lt;/td&gt;";                 msg += "&
lt;/tr&gt;";                 msg += j;                 msg += "&lt;/table&gt;"; 
                sendMail(chargeCompte.getEmail(), "", msg);                 Syst
em.out.println(msg);             }         }     }     /** 

* @param args the c
ommand line arguments 

*/
     public static void main(String[] args) {        
 // TODO code application logic here         new JavaMail().mailRappelFinAbonnem
ent();     } } package entity; import java.util.Date; public class Abonnement { 
    private int reste;     private int idAbonnement;     private int idClient;  
   private Date dateDebut;     private Date dateFin;     private int etatAbonnem
ent;     private String nomClient;     private String emailClient;     private S
tring contact;     public String getContact() {         return contact;     }   
  public void setContact(String contact) {         this.contact = contact;     }
     public Date getDateDebut() {         return dateDebut;     }     public voi
d setDateDebut(Date dateDebut) {         this.dateDebut = dateDebut;     }     p
ublic Date getDateFin() {         return dateFin;     }     public void setDateF
in(Date dateFin) {         this.dateFin = dateFin;     }     public String getEm
ailClient() {         return emailClient;     }     public void setEmailClient(S
tring emailClient) {         this.emailClient = emailClient;     }     public in
t getEtatAbonnement() {         return etatAbonnement;     }     public void set
EtatAbonnement(int etatAbonnement) {         this.etatAbonnement = etatAbonnemen
t;     }     public int getIdAbonnement() {         return idAbonnement;     }  
   public void setIdAbonnement(int idAbonnement) {         this.idAbonnement = i
dAbonnement;     }     public int getIdClient() {         return idClient;     }
     public void setIdClient(int idClient) {         this.idClient = idClient;  
   }     public String getNomClient() {         return nomClient;     }     publ
ic void setNomClient(String nomClient) {         this.nomClient = nomClient;    
 }     public int getReste() {         return reste;     }     public void setRe
ste(int reste) {         this.reste = reste;     }     public Abonnement(int res
te, int idAbonnement, int idClient, Date dateDebut, Date dateFin, int etatAbonne
ment, String nomClient, String emailClient, String contact) {         this.reste
 = reste;         this.idAbonnement = idAbonnement;         this.idClient = idCl
ient;         this.dateDebut = dateDebut;         this.dateFin = dateFin;       
  this.etatAbonnement = etatAbonnement;         this.nomClient = nomClient;     
    this.emailClient = emailClient;         this.contact = contact;     } } pack
age entity; import java.util.List; public class ChargeCompte {     private Strin
g nom;     private String prenom;     private String email;     private String t
el;     private List&lt;Abonnement&gt; abonnementsList;     public List&lt;Abonn
ement&gt; getAbonnementsList() {         return abonnementsList;     }     publi
c void setAbonnementsList(List&lt;Abonnement&gt; abonnementsList) {         this
.abonnementsList = abonnementsList;     }     public String getEmail() {        
 return email;     }     public void setEmail(String email) {         this.email
 = email;     }     public String getNom() {         return nom;     }     publi
c void setNom(String nom) {         this.nom = nom;     }     public String getP
renom() {         return prenom;     }     public void setPreom(String preom) { 
        this.prenom = preom;     }     public String getTel() {         return t
el;     }     public void setTel(String tel) {         this.tel = tel;     }    
 public ChargeCompte(String nom, String preom, String email, String tel) {      
   this.nom = nom;         this.prenom = preom;         this.email = email;     
    this.tel = tel;     }     public ChargeCompte(String nom, String preom, Stri
ng email, String tel, List&lt;Abonnement&gt; abonnementsList) {         this.nom
 = nom;         this.prenom = preom;         this.email = email;         this.te
l = tel;         this.abonnementsList = abonnementsList;     } } package session
; import entity.Abonnement; import entity.ChargeCompte; import java.sql.ResultSe
t; import java.sql.SQLException; import java.util.ArrayList; import java.util.Li
st; public class AbonnementFacade extends AbstractSQL {     public AbonnementFac
ade() {     }     public List&lt;Abonnement&gt; getListClient(int idContact) {  
       List&lt;Abonnement&gt; lcs = new ArrayList&lt;Abonnement&gt;();         S
tring query = "SELECT DATEDIFF(a.date_fin,CURDATE()) as reste, a.id_abonnement, 
a.id_client, a.date_debut, a.date_fin, a.etat_abonnement, c.id , c.Nom , c.Email
 , c.Contact ";         query += "FROM abonnement a JOIN clients c ON c.id = a.i
d_client JOIN abonnement_contact ac ON ac.id_abonnement = a.id_abonnement ";    
     query += "WHERE ac.id_contatct = " + idContact+ " AND (";         query += 
"DATE_ADD( CURDATE( ) , INTERVAL 15 DAY ) = date_fin ";         query += "OR DAT
E_ADD( CURDATE( ) , INTERVAL 10 DAY ) = date_fin ";         query += "OR DATE_AD
D( CURDATE( ) , INTERVAL 5 DAY ) = date_fin ";         query += "OR DATE_ADD( CU
RDATE( ) , INTERVAL 2 DAY ) = date_fin ";         query += "OR DATE_ADD( CURDATE
( ) , INTERVAL 1 DAY ) = date_fin ";         query += "OR DATE_ADD( CURDATE( ) ,
 INTERVAL 0 DAY ) = date_fin ";         query += ") ";         if (connect()) { 
            try {                 ResultSet rs = execResult(query);             
    if (rs != null) {                     while (rs.next()) {                   
      lcs.add(new Abonnement(rs.getInt("reste"), rs.getInt("id_abonnement"), rs.
getInt("id_client"), rs.getDate("date_debut"), rs.getDate("date_fin"), rs.getInt
("etat_abonnement"), rs.getString("Nom"), rs.getString("Email"), rs.getString("C
ontact")));                     }                 }             } catch (SQLExce
ption ex) {             }         } else {             System.out.println("Mysql
 connection failed !!!");         }         close();         return lcs;     }  
        public List&lt;ChargeCompte&gt; getListMailChargeClient(){         List&
lt;ChargeCompte&gt; lcs = new ArrayList&lt;ChargeCompte&gt;();         String qu
ery = "SELECT id_contatct , Nom , Prenom , Portable , Email ";         query += 
"FROM abonnement_contact a ";         query += "JOIN utilisateurs u ON a.id_cont
atct = u.id ";         query += "GROUP BY a.id_contatct ";         if (connect()
) {             try {                 ResultSet rs = execResult(query);         
        if (rs != null) {                     while (rs.next()) {               
          lcs.add(new ChargeCompte(rs.getString("Nom"), rs.getString("Prenom"), 
rs.getString("Email"), rs.getString("Portable"), getListClient(rs.getInt("id_con
tatct"))));                     }                 }             } catch (SQLExce
ption ex) {             }         } else {             System.out.println("Mysql
 connection failed !!!");         }         close();         return lcs;     } }
 package session; import java.sql.DriverManager; import java.sql.ResultSet; impo
rt java.sql.SQLException; import java.util.logging.Level; import java.util.loggi
ng.Logger; public class AbstractSQL {          private String dbURL = "jdbc:mysq
l://localhost:3306/abonnement_db";     private String driverClass = "com.mysql.j
dbc.Driver";     private String serverName = "localhost";     private String por
tNumber = "3306";     private String databaseName = "abonnement";     private St
ring user = "root";     private String password = "root";          private java.
sql.Connection dbConnect = null;     private java.sql.Statement dbStatement = nu
ll;          public Boolean connect() {         try {             Class.forName(
driverClass).newInstance();             this.dbConnect = DriverManager.getConnec
tion(dbURL, this.user, this.password);             this.dbStatement = this.dbCon
nect.createStatement();             return true;         } catch (SQLException e
x) {             Logger.getLogger(AbstractSQL.class.getName()).log(Level.SEVERE,
 null, ex);         } catch (ClassNotFoundException ex) {             Logger.get
Logger(AbstractSQL.class.getName()).log(Level.SEVERE, null, ex);         } catch
 (InstantiationException ex) {             Logger.getLogger(AbstractSQL.class.ge
tName()).log(Level.SEVERE, null, ex);         } catch (IllegalAccessException ex
) {             Logger.getLogger(AbstractSQL.class.getName()).log(Level.SEVERE, 
null, ex);         }         return false;     }     /** 

* Executer une requet
e SQL 

* @param sql 

* @return resultat de la requete 

*/
     public ResultS
et execResult(String sql) {         try {             ResultSet rs = this.dbStat
ement.executeQuery(sql);             return rs;         } catch (SQLException ex
) {             Logger.getLogger(AbstractSQL.class.getName()).log(Level.SEVERE, 
null, ex);         }         return null;     }          public void exec(String
 sql){         try {             this.dbStatement.executeUpdate(sql);         } 
catch (SQLException ex) {             Logger.getLogger(AbstractSQL.class.getName
()).log(Level.SEVERE, null, ex);         }     }     /** 

* Fermer la connexion
 au serveur de DB 

*/
     public void close() {         try {             this
.dbStatement.close();             this.dbConnect.close();             this.dbCon
nect.close();         } catch (SQLException ex) {             Logger.getLogger(A
bstractSQL.class.getName()).log(Level.SEVERE, null, ex);         }     } } </pre
>
<br />     
<br />     
<br />     
<br />     
<br />Voici le script de 
la db 
<br />
<br />----------------------------------
<br />-- CREATE DATABA
SE `abonnement_db`;
<br />-- USE `abonnement_db`;
<br />--
<br />
<br />-- -
-------------------------------------------------------
<br />
<br />--
<br /
>-- Structure de la table `abonnement`
<br />--
<br />
<br />CREATE TABLE IF 
NOT EXISTS `abonnement` (
<br />  `id_abonnement` int(11) NOT NULL AUTO_INCREME
NT,
<br />  `id_client` int(11) NOT NULL DEFAULT '0',
<br />  `date_debut` dat
e NOT NULL DEFAULT '0000-00-00',
<br />  `date_fin` date NOT NULL DEFAULT '0000
-00-00',
<br />  `etat_abonnement` tinyint(4) NOT NULL DEFAULT '0',
<br />  `c
ouper_client` tinyint(4) NOT NULL DEFAULT '0',
<br />  `send_mail_client` tinyi
nt(4) NOT NULL DEFAULT '0',
<br />  `send_mail_charge_compte` tinyint(4) NOT NU
LL DEFAULT '0',
<br />  `facture_regle` tinyint(4) NOT NULL DEFAULT '0',
<br /
>  `observation` text NOT NULL,
<br />  `id_connexion` int(11) NOT NULL DEFAULT
 '0',
<br />  `id_charge_compte` int(11) NOT NULL DEFAULT '0',
<br />  PRIMARY
 KEY (`id_abonnement`)
<br />) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCRE
MENT=45 ;
<br />
<br />--
<br />-- Contenu de la table `abonnement`
<br />--

<br />
<br />INSERT INTO `abonnement` (`id_abonnement`, `id_client`, `date_de
but`, `date_fin`, `etat_abonnement`, `couper_client`, `send_mail_client`, `send_
mail_charge_compte`, `facture_regle`, `observation`, `id_connexion`, `id_charge_
compte`) VALUES
<br />(1, 1, '2014-03-13', '2014-05-15', 1, 0, 1, 1, 0, '', 0, 
2),
<br />(2, 2, '2014-03-13', '2014-04-24', 1, 0, 1, 1, 0, '', 0, 1);
<br />

<br />-- --------------------------------------------------------
<br />
<br 
/>--
<br />-- Structure de la table `abonnement_contact`
<br />--
<br />
<br
 />CREATE TABLE IF NOT EXISTS `abonnement_contact` (
<br />  `id_abonnement` in
t(11) NOT NULL DEFAULT '0',
<br />  `id_contatct` int(11) NOT NULL DEFAULT '0',

<br />  PRIMARY KEY (`id_abonnement`,`id_contatct`)
<br />) ENGINE=MyISAM DEF
AULT CHARSET=utf8 COLLATE=utf8_bin;
<br />
<br />--
<br />-- Contenu de la ta
ble `abonnement_contact`
<br />--
<br />
<br />INSERT INTO `abonnement_contac
t` (`id_abonnement`, `id_contatct`) VALUES
<br />(1, 1),
<br />(1, 2),
<br />
(2, 1),
<br />(2, 2);
<br />
<br />-- ---------------------------------------
-----------------
<br />
<br />--
<br />-- Structure de la table `charge_comp
te`
<br />--
<br />
<br />CREATE TABLE IF NOT EXISTS `charge_compte` (
<br /
>  `id` int(30) NOT NULL AUTO_INCREMENT,
<br />  `Nom` varchar(100) DEFAULT 'No
m revendeur-X',
<br />  `Prenom` varchar(100) DEFAULT 'Prenom revendeur-X',
<b
r />  `Adresse` varchar(100) DEFAULT 'Adresse  X',
<br />  `Telephone` varchar(
50) DEFAULT '021 00 00 00',
<br />  `Portable` varchar(50) DEFAULT '070 00 00 0
0',
<br />  `Email` varchar(40) DEFAULT '@',
<br />  PRIMARY KEY (`id`)
<br /
>) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;
<br />
<br />--
<
br />-- Contenu de la table `charge_compte`
<br />--
<br />
<br />INSERT INTO
 `charge_compte` (`id`, `Nom`, `Prenom`, `Adresse`, `Telephone`, `Portable`, `Em
ail`) VALUES
<br />(1, 'Nom revendeur-1', 'Prenom revendeur-1', 'Adresse  1', '
021 00 00 00', '070 00 00 00', 'rv1@comp.com'),
<br />(2, 'Nom revendeur-2', 'P
renom revendeur-2', 'Adresse  2', '021 00 00 00', '070 00 00 00', 'rv2@comp.com'
);
<br />
<br />-- --------------------------------------------------------
<
br />
<br />--
<br />-- Structure de la table `clients`
<br />--
<br />
<br
 />CREATE TABLE IF NOT EXISTS `clients` (
<br />  `id` int(20) NOT NULL AUTO_IN
CREMENT,
<br />  `Nom` varchar(100) DEFAULT 'Client  X',
<br />  `Adresse` var
char(255) DEFAULT 'Adresse  X',
<br />  `Telephone` varchar(16) DEFAULT '021 00
 00 00',
<br />  `Portable` varchar(16) DEFAULT '070 00 00 00',
<br />  `Willa
ya_id` int(10) DEFAULT NULL,
<br />  `Fax` varchar(20) DEFAULT '021000000',
<b
r />  `Email` varchar(30) DEFAULT '@',
<br />  `Contact` varchar(100) DEFAULT '
contact',
<br />  `Fonction` varchar(120) DEFAULT 'fonction contact',
<br />  
PRIMARY KEY (`id`)
<br />) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT
=3 ;
<br />
<br />--
<br />-- Contenu de la table `clients`
<br />--
<br />

<br />INSERT INTO `clients` (`id`, `Nom`, `Adresse`, `Telephone`, `Portable`, 
`Willaya_id`, `Fax`, `Email`, `Contact`, `Fonction`) VALUES
<br />(1, 'Client  
1', 'Adresse  X1', '021 00 00 00', '070 00 00 00', 16, '021000000', 'client.1@cl
ient.com', 'contact', 'fonction contact'),
<br />(2, 'Client  2', 'Adresse  2',
 '021 00 00 00', '070 00 00 00', 16, '021000000', 'client.2@client2.com', 'conta
ct', 'fonction contact');
<br />
<br />----------------------------------
