Gestion Caisse--------------
Url     : http://codes-sources.commentcamarche.net/source/100888-gestion-caisseAuteur  : tlili_soufDate    : 25/01/2015
Licence :
=========

Ce document intitulé « Gestion Caisse » issu de CommentCaMarche
(codes-sources.commentcamarche.net) est mis à disposition sous les termes de
la licence Creative Commons. Vous pouvez copier, modifier des copies de cette
source, dans les conditions fixées par la licence, tant que cette note
apparaît clairement.

Description :
=============

C'est ma première application,elle sert  
<br />à ajouter,chercher des articles
...
<br />
<br />Voici un exemple de code:
<br />
<br />import java.sql.*;

<br />
<br />import javax.swing.JOptionPane;
<br />import javax.swing.table.De
faultTableModel;
<br />public class ManipDB {
<br /> String query;
<br /> Sta
tement stx;
<br /> ResultSet rset;
<br /> Connection conn;
<br /> public Mani
pDB(){
<br />  ConnDisc.connect();
<br />  conn=ConnDisc.getConnexion();
<br 
/>  rset=null;
<br /> }
<br /> public ResultSet afficherCaissier(int id){
<br
 />  try{
<br />   stx=conn.createStatement();
<br />   query=&quot;SELECT idC
aissier,nomCaissier,passwordCaissier FROM Caissier&quot;+&quot; WHERE idCaissier
=&quot;+id+&quot;&quot;;
<br />   rset=stx.executeQuery(query);
<br />   
<br
 />  }
<br />  catch(SQLException ex){
<br />   ex.printStackTrace();
<br /> 
  
<br />   
<br />  }
<br />  return rset;
<br /> }
<br /> public DefaultT
ableModel  afficherStock(){
<br />  DefaultTableModel dt = new DefaultTableMode
l();
<br />  dt.addColumn(&quot;Stock Unitaire&quot;);
<br />  dt.addColumn(&q
uot;Stock Lot&quot;);
<br />  dt.addColumn(&quot;chiffre d'affaire Article&quot
;);
<br />  dt.addColumn(&quot;chiffre d'affaire Lot&quot;);
<br />  dt.addCol
umn(&quot;chiffre d'affaire global&quot;);
<br />  try{
<br />   
<br />   st
x=conn.createStatement();
<br />   query=&quot;SELECT * FROM Stock&quot;;
<br 
/>   rset=stx.executeQuery(query);
<br />   while(rset.next()){
<br />    Obje
ct []tableau={rset.getInt(&quot;stockU&quot;),rset.getInt(&quot;stockLot&quot;),
rset.getDouble(&quot;chiffreArticle&quot;),rset.getDouble(&quot;chiffreLot&quot;
),
<br />      rset.getDouble(&quot;chiffreAffaire&quot;)};
<br />    dt.addRo
w(tableau);
<br />   }
<br />  }
<br />  catch(SQLException ex){
<br />   ex
.printStackTrace();
<br />   JOptionPane.showMessageDialog(null,&quot;ERROR&quo
t;,&quot;Message d?avertissement&quot;,JOptionPane.ERROR_MESSAGE);
<br />   
<
br />  }
<br />  return dt;
<br /> }
<br /> public void acheterArticle(int nb
,String nom){
<br />  try{
<br />   double prixarticle=0.0;
<br />   double c
hiffreaffaire=0.0;
<br />   int stocku=0;
<br />   
<br />   stx=conn.createS
tatement();
<br />   query=&quot;SELECT prixArticle FROM Article WHERE nomArtic
le=&quot;+nom+&quot; &quot;;
<br />   rset=stx.executeQuery(query);
<br />   

<br />   while(rset.next()){
<br />    prixarticle=rset.getDouble(&quot;prixAr
ticle&quot;);
<br />   }
<br />   
<br />   query=&quot;SELECT stockU,chiffre
Affaire FROM Stock&quot;;
<br />   rset=stx.executeQuery(query);
<br />   whil
e(rset.next()){
<br />    chiffreaffaire=rset.getDouble(&quot;chiffreAffaire&qu
ot;);
<br />    stocku=rset.getInt(&quot;stockU&quot;);
<br />   }
<br />   

<br />   double prixArticle=prixarticle*nb;
<br />   chiffreaffaire+=prixArtic
le;
<br />   stocku-=nb;
<br />   
<br />   if(stocku&gt;0){
<br />    query
=&quot;UPDATE Stock SET chiffreAffaire=&quot;+chiffreaffaire+&quot;,stockU=&quot
;+stocku+&quot;,chiffreArticle=&quot;+prixArticle+&quot;&quot;;
<br />    stx.e
xecuteUpdate(query);
<br />   }
<br />   else{
<br />    JOptionPane.showMess
ageDialog(null,&quot;stock n'est plus disponible&quot;,&quot;Message d?avertisse
ment&quot;,JOptionPane.ERROR_MESSAGE);
<br />   }
<br />   
<br />  }
<br />
  catch(SQLException ex){
<br />   ex.printStackTrace();
<br />   JOptionPane.
showMessageDialog(null,&quot;ERROR&quot;,&quot;Message d?avertissement&quot;,JOp
tionPane.ERROR_MESSAGE); 
<br />  }
<br />   }
<br />  
<br /> 
<br /> publ
ic void acheterLot(int nb,String nom){
<br />  try{
<br />   double prixlot=0.
0;
<br />   double chiffreaffaire=0.0;
<br />   int stocku=0;
<br />   int st
ocklot = 0;
<br />   stx=conn.createStatement();
<br />   query=&quot;SELECT p
rixLot FROM Lot&quot;+&quot; WHERE nomLot=&quot;+nom+&quot;&quot;;
<br />   rse
t=stx.executeQuery(query);
<br />   while(rset.next()){
<br />    prixlot=rset
.getDouble(&quot;prixLot&quot;);
<br />   }
<br />   query=&quot;SELECT stockU
,stockLot,chiffreAffaire FROM Stock&quot;;
<br />   rset=stx.executeQuery(query
);
<br />   while(rset.next()){
<br />    chiffreaffaire=rset.getDouble(&quot;
chiffreAffaire&quot;);
<br />    stocku=rset.getInt(&quot;stockU&quot;);
<br /
>    stocklot=rset.getInt(&quot;stockLot&quot;);
<br />   }
<br />   double pr
ixLot=prixlot*nb;
<br />   chiffreaffaire+=prixLot;
<br />   System.out.printl
n(chiffreaffaire);
<br />   //supposons qu'on a un lot de 3 articles.
<br />  
 stocku-=(nb*3);
<br />   stocklot-=nb;
<br />   if(stocku&gt;0)
<br />   {

<br />    query=&quot;UPDATE Stock SET chiffreAffaire=&quot;+chiffreaffaire+&quo
t;,stockU=&quot;+stocku+&quot;,stockLot=&quot;+stocklot+&quot;,chiffreLot=&quot;
+prixLot+&quot;&quot;;
<br />    stx.executeUpdate(query);
<br />   }
<br /> 
  else{
<br />    JOptionPane.showMessageDialog(null,&quot;stock n'est plus dis
ponible&quot;,&quot;Message d?avertissement&quot;,JOptionPane.ERROR_MESSAGE);
<
br />   }
<br />  }
<br />  catch(SQLException ex){
<br />   ex.printStackTra
ce();
<br />   JOptionPane.showMessageDialog(null,&quot;stock n'est plus dispon
ible&quot;,&quot;Message d?avertissement&quot;,JOptionPane.ERROR_MESSAGE);
<br 
/>   
<br />  }
<br />  
<br /> }
<br /> public DefaultTableModel afficherPr
oduit(int code){
<br />  DefaultTableModel dt = new DefaultTableModel();
<br /
>  dt.addColumn(&quot;numero&quot;);
<br />  dt.addColumn(&quot;Reference&quot;
);
<br />  dt.addColumn(&quot;Designation&quot;);
<br />  dt.addColumn(&quot;N
om&quot;);
<br />  dt.addColumn(&quot;Prix&quot;);
<br />  try{
<br />   
<b
r />   stx=conn.createStatement();
<br />   query=&quot;SELECT * FROM Article W
HERE codeArticle=&quot;+code+&quot; UNION SELECT * FROM Lot WHERE codeLot=&quot;
+code+&quot;&quot;;;
<br />   rset=stx.executeQuery(query);
<br />   while(rse
t.next()){
<br />    Object []tableau={rset.getInt(1),rset.getString(2),rset.ge
tString(3),rset.getString(4),rset.getDouble(5)};
<br />    dt.addRow(tableau);

<br />   }
<br />  }
<br />  catch(SQLException ex){
<br />   ex.printStackT
race();
<br />   
<br />  }
<br />  return dt;
<br />  
<br /> }
<br /> 

<br /> public boolean verifier(int login,String password){
<br />  boolean test
=false;
<br />  int log =0;
<br />  String pass=null;
<br />  try{
<br />   
stx=conn.createStatement();
<br />   query=&quot;SELECT idCaissier,passwordCais
sier FROM caissier&quot;;
<br />   rset=stx.executeQuery(query);
<br />   whil
e(rset.next()|| (test==true)){
<br />    log=rset.getInt(&quot;idCaissier&quot;
);
<br />    pass=rset.getString(&quot;passwordCaissier&quot;);
<br />    if((
log==login) && (pass.equals(password)))
<br />     test=true;  
<br />   }
<b
r />  }
<br />  catch(SQLException ex){
<br />   ex.printStackTrace();
<br />
  }
<br />  System.out.println(test);
<br />  System.out.println(log);
<br />
  System.out.println(pass);
<br />  return test;
<br />
<br /> }
<br /> 
<b
r /> 
<br />}
