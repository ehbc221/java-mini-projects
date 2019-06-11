package team.conso.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import team.conso.data.bdd.Categorie;
import team.conso.data.bdd.DataFilter;
import team.conso.data.bdd.Produit;
import team.conso.data.bdd.Stock;


/**
 * cette class fait office de document elle regroupe l ensemble des donnee a gerer
 * et dc fourni toute les methode d acce a ces donnees
 * 
 * si l on change de type de base de donne c est dc cette class qu il faudra refaire
 * en respectrant l implementation pour la compatibilitee avec le reste du code
 * et l extention pour conserver l interactivitee avec les donnees
 * 
 * 
 * team.conso.system.core
 * 
 */
public class BDDEco extends LBasedData //implements EcoBase
{
    public static final int STATUS_NOTHING = 0;
    public static final int STATUS_ALERTE = 1;
    public static final int STATUS_TOO_LATE = 2;
    public static final int STATUS_DEAD = 3;   // ;o)
    private static boolean test = false;
    // pour plus de modularite on devrai externaliser le prototypage des tables ;o)
    // mais la chui feneant...
    // (a metre sous forme de fichier de conf ainsi les nom de champ 
    // deviendrait des variables dans toute la class avec des noms donnes 
    // pour les var pertinantes ...)
    private static final String[] TABLE_CATE = 
			{ 
            "cle int(11) auto_increment primary key",
            "libelle varchar(255)",
            "image varchar(255)",
            };
    private static final String[] TABLE_PROD = 
			{ 
	        "cle int(11) auto_increment primary key",
	        "libelle varchar(255)",
	        "cate int(11)",
            "image varchar(255)",
	        };
    private static final String[] TABLE_STOK = 
    		{ 
	        "cle int(11) auto_increment primary key",
//	        "cate int(11)",
	        "prod int(11)",
	        "`date limit` date",
	        "`date` date",  // date de stokage
	        "nombre double", // quantité restante ---> pour facilitee les requettes
	        "`nombre de depard` double", // quantité au stokage
	        "prix double"  // /prix d achat
	        };
    private static final String[] TABLE_UNSTOK = 
			{ 
		    "cle int(11) auto_increment primary key",
		    "stok int(11)",   // cle ds la table des stok
		    "date date",
		    "nombre double",  // quantite retirer a la date
		    "destination varchar(255)", // ou on veu koi ;o)
		    };
    public static final String tableCateName = "cate";
    public static final String tableProdName = "prod";
    public static final String tableStokName = "stock";
    public static final String tableDeltName = "fini";
    public static final String tableUStkName = "unstock";
    public static final String[] tablesName = { tableCateName, tableProdName, tableStokName, tableUStkName, tableDeltName };
    
    
    /**
     * delai prevu pour prevenir avant que le produit ne soit perimer
     */
    public int alarmDelai = 3;   // en jours
    /**
     * reference sur la connectino de la base utilise
     */
    private Connection connection = null;
    private String name = "";
    
    public BDDEco( Connection connec, String name )
    {
        System.out.println( "BDDEco   test = " + isTest() );
        connection = connec;
        // metre la valeur false si plus en test
        createBase();
        if ( isTest() ) initTables(); 
        setName( name );
        System.out.println( "enclosing_method + fin" );
    }
    /**
     * creer les table de la base eco si elle n existe pas deja
     *
     */
    private void createBase()
    {
        try
        {
            if ( isTest() )
            {
	            destroyTable( connection, tableCateName );
	            destroyTable( connection, tableProdName );
	            destroyTable( connection, tableStokName );
	            destroyTable( connection, tableUStkName );
	            destroyTable( connection, tableDeltName );
            }
            createTable( connection, tableCateName, TABLE_CATE );
            createTable( connection, tableProdName, TABLE_PROD );
            createTable( connection, tableStokName, TABLE_STOK );
            createTable( connection, tableDeltName, TABLE_STOK );
            createTable( connection, tableUStkName, TABLE_UNSTOK );
        } catch ( Exception ex )
        {
            System.out.println( "team.conso.data.BDDEco.createBase(  )  : " + ex );
        }
    }
    /**
     * creation d une table mysql avec spécification
     * 
     * @param :
     *            nom = nom de la table
     * @param :
     *            champs = noms des champs
     */
    public static void createTable( Connection c, String nom, String[] champs )
    {
        String sql = "";
        try
        {
            String varAndTypes = "";
            for ( int i = 0; i < champs.length; i++ )
                varAndTypes += ", " + champs[ i ] + "";
            Statement st = c.createStatement();
            sql = "CREATE TABLE IF NOT EXISTS `" + nom + "` ";
            sql += "(" + varAndTypes.substring( 2 ) + ")";
            st.executeUpdate( sql ); // met la table a jour
            st.close();
        } catch ( Exception e )
        {
            System.out.println( "createTable exception : " + e );
            System.out.println( "createTable sql : " + sql );
        }
    }
    /**
     * c pour etre trankil ;o)
     * 
     * 
     * @param c
     * @param nom
     */
    public static void destroyTable( Connection c, String nom )
    {
        String sql = "";
        try
        {
            Statement st = c.createStatement();
            sql = "DROP TABLE IF EXISTS `" + nom + "`";
            st.executeUpdate( sql ); // met la table a jour
            st.close();
        } catch ( Exception e )
        {
            System.out.println( "destroyTable exception : " + e );
            System.out.println( "destroyTable sql : " + sql );
        }
    }
    
    /** 
     * utile pour avoir un premier jeu d essai ... ;o) 
     *
     */
    private void initTables()
    {
        System.out.println( "initTables" );
        String rep = System.getProperty( "user.dir" ) + "\\data\\";
        loadDataInFile( connection, tableCateName, rep, tableCateName + ".txt" );
        loadDataInFile( connection, tableStokName, rep, tableStokName + ".txt" );
        loadDataInFile( connection, tableProdName, rep, tableProdName + ".txt" );
        
//        getCategoriesEnStock();
    }

    /**
     * Pour faire un import d'un fichier texte vers une table précédement créée
     */
    public static void loadDataInFile( Connection con, String nomTable, String cheminFic, String nomFic )
    {
        loadDataInFile( con, nomTable, cheminFic, nomFic, 0 );
    }

    /** 
	 * Pour faire un import d'un fichier texte vers une table précédement créée
	 */
	public void loadDataInFile( String nomTable, String fic )
	{
		String sql = "";
		try
		{
			sql = "LOAD DATA INFILE \"" + fic + "\"\r\n "
				   + "INTO TABLE `"+ nomTable +"`\r\n"
				   + "FIELDS terminated by ';'\r\n"
		  		   + "LINES terminated by '\\r\\n' ";
  	  
			Statement stm = connection.createStatement();
			stm.executeUpdate( sql );
			stm.close();
			
		}
		catch (Exception e)
		{
		    System.out.println( " loadDataInFile : " + e + "\r\n" + sql );
		}
	}

    /**
     * Pour faire un import d'un fichier texte vers une table précédement créée
     */
    public static void loadDataInFile( Connection con, String nomTable, String cheminFic, String nomFic, int skeepLines )
    {
        String sql = "";
        try
        {
            String fic = (cheminFic + nomFic).replace( '\\', '/' );
            sql = "LOAD DATA INFILE \"" + fic + "\"\r\n " + "INTO TABLE `" + nomTable + "`\r\n"
                    + "FIELDS terminated by ';'\r\n" + "LINES terminated by '\\r\\n' ";
            if ( skeepLines > 0 )
                sql += " IGNORE " + skeepLines + " LINES";

            Statement stm = con.createStatement();
            stm.executeUpdate( sql );

        } catch ( Exception e )
        {
            System.out.println( " loadDataInFile : " + e + "\r\n" + sql );
        }
    }

    
    /**
     * pour faire un export d'une table vers un fichier texte   
     */
    public static void selectIntoOutFile( Connection con, String nomTable, String cheminFic, String nomFic )
    {
        String sql = "";
        try
        {
            String fic = (cheminFic + nomFic).replace('\\','/');
            sql = "SELECT * INTO OUTFILE \""+fic+"\""
                  + " FIELDS terminated by ';'"
                  + " LINES terminated by '\\r\\n'"
                  + " FROM `" + nomTable + "`"
                  + " ;";
                  
            Statement stm = con.createStatement();
            stm.executeUpdate( sql );
            
        }
        catch (Exception e)
        {
            System.out.println( " selectIntoOutFile : " + e + "\r\n" + sql );
        }
    }

    /**
     * pour faire un export d'une table vers un fichier texte   
     */
    public void selectIntoOutFile( String nomTable, String cheminFic, String nomFic )
    {
        selectIntoOutFile( connection, nomTable, cheminFic, nomFic );
    }

    /**
     * appelle simplifier par defaul a la methode
     * @return
     */
    public Vector getProdList()
    {
        return getProdList( true );
    }
    /**
     * il en resort un vector ki contien le libelle du produit en element n et sa cle en n+1
     * si withcle = false alors seule les libelle sont renvoyer
     * @return
     */
    public Vector getProdList( boolean withCle )
    {
        Vector<String> result = new Vector<String>();
        String sql = "";
        try
        {
            sql = "SELECT libelle, cle FROM " + tableProdName
        		+ " ORDER BY libelle";
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery( sql );
        while ( rs.next() )
        {
            // range le resultat ds le vector
            result.add( rs.getString( 1 ) );
            if ( withCle ) result.add( rs.getString( 2 ) );
        }
        rs.close();
        stm.close();
        } catch (Exception e)
        {
            System.out.println( "team.conso.data.BDDEco.getProd(  )  : " + e );
            System.out.println( "sql = " + sql );
        }
        return result;
    }
    
    /**
     * appelle simplifier par defaul a la methode
     * @return
     */
    public Vector getCategories()
    {
        return getCategories( false );
    }
    
    /**
     * 
     * @param string
     * @return
     */
    public Vector getCategories( boolean string )
    {
        String sql = "";
        Vector<Object> result = new Vector<Object>( 100 );
        try
        {
            sql = "SELECT " + tableCateName + ".libelle, " + tableCateName + ".image FROM " + tableCateName;
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery( sql );
            while ( rs.next() )
            {// range le resultat ds le vector
                if ( string )
                    result.add( rs.getString( 1 ) );
                else
                    result.add( new Categorie( rs.getString( 1 ), rs.getString( 2 ) ) );
            }
            rs.close();
            stm.close();
        } catch ( Exception ex )
        {
            System.out.println( "team.conso.data.BDDEco.getCategories(  )  : " + ex );
            System.out.println( "sql = " + sql );
        }
        return result;
    }
    public Vector getCategoriesEnStock()
    {
        String sql = "";
        Vector<Object> result = new Vector<Object>( 100 );
        try
        {
            sql = "SELECT " + tableCateName + ".libelle, " + tableCateName + ".image FROM " + tableStokName + ", " +  tableCateName + ", " +  tableProdName +
            	" WHERE " + tableStokName + ".prod = " + tableProdName + ".cle AND " + 
            	tableProdName + ".cate = " + tableCateName + ".cle GROUP BY libelle ORDER BY libelle";
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery( sql );
            while ( rs.next() )
            {// range le resultat ds le vector
                result.add( new Categorie( rs.getString( 1 ), rs.getString( 2 ) ) );
            }
            rs.close();
            stm.close();
        } catch ( Exception ex )
        {
            System.out.println( "team.conso.data.BDDEco.getCategories(  )  : " + ex );
            System.out.println( "sql = " + sql );
        }
        return result;
    }
    
    /**
     * enleve une quantite ds le stok 
     * et archive l action ds la table unstok
     *
     */
    public void desStock( String cleStock, String quantite, String rest, String dest )
    {
        String sql = "";
        try
        {
            int dif = Integer.parseInt( rest ) - Integer.parseInt( quantite );
            // si la dif est inferrieur a 0 on transfere l enreg pour simplifier le traitement
            // ( en fait egal car on devrai pas pouvoir suppriumer plus que contenu) 
            Statement stm = connection.createStatement();
            if ( dif < 0 )
            {
                sql = "INSERT INTO " + tableDeltName + " SELECT * FROM `"+ tableStokName + "` WHERE cle = " + cleStock;     
                stm.executeUpdate( sql );
                sql = "DELETE FROM `" + tableStokName + "` WHERE cle = " + cleStock;
            }
            else
            {
                sql = "UPDATE `" + tableStokName + "` SET nombre = " + dif +" WHERE cle = " + cleStock;
            }
            stm.executeUpdate( sql );
	        Calendar cld = Calendar.getInstance();
            String date = cld.get( Calendar.YEAR ) + "-" + cld.get( Calendar.MONTH ) + "-" + cld.get( Calendar.DAY_OF_MONTH );
            sql = "INSERT INTO " + tableUStkName + " ( stok, nombre, date, destination ) " + "VALUES( '"
            + cleStock + "', '" + quantite + "', '" + date + "', '" + dest + "' ) " ;
            stm.executeUpdate( sql );
            stm.close();
            fireChangeUpdate( new BddEcoEvent( tableStokName, cleStock, quantite ) );
        } catch ( Exception ex )
        {
            System.out.println( "team.conso.data.BDDEco.desStock() : " + ex );
            System.out.println( "sql = " + sql );
        }
    }
    
    /**
     * fournit le cout total d un produi sur une periode donne 
     * si les date son des String vide alors couut calculer sur l ensemble de la base
     * si le prod et vide alors cout calculer sur tou les prod
     */
    public String getPrixProd( String prod, String dateDeb, String dateFin )
    {
        String sql = "";
        String result = "0";
        try
        {
            sql = "SELECT SUM( prix ) " ;
            sql += " FROM " + tableStokName;
            String where = "";
            if ( prod.length() > 0 )
                where += "" + tableStokName + ".prod = " + prod;
            if ( dateDeb.length() > 0 )
            {
                if ( where.length() > 0 ) where += " AND ";
                where += " date BETWEEN '" + dateDeb + "' AND '" + dateFin+ "'";
            }
            if ( where.length() > 0 ) sql += " WHERE " + where;
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery( sql );
            if ( rs.next()  )
                result = rs.getString( 1 );
        } catch ( Exception ex )
        {
            System.out.println( "team.conso.data.BDDEco.getPrixProd() : " + ex );
            System.out.println( "sql = " + sql );
        }
        return result;
    }

    /**
     * fournit le cout total d un produi sur une periode donne 
     * si les date son des String vide alors couut calculer sur l ensemble de la base
     */
    public String getPrixCate( String cate, String dateDeb, String dateFin )
    {
        String sql = "";
        String result = "0";
        try
        {
            sql = "SELECT SUM( prix ) " ;
            sql += " FROM " + tableStokName + ", " +  tableCateName + ", " + tableProdName; 
            sql += " WHERE " + tableProdName + ".cate = " + tableCateName + ".cle AND " +
            tableStokName + ".prod = " + tableProdName + ".cle AND " + tableCateName + ".libelle = '" + cate + "'";
            if ( dateDeb.length() > 0 )
                sql += " AND date BETWEEN '" + dateDeb + "' AND '" + dateFin+ "'";
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery( sql );
            if ( rs.next()  )
                result = rs.getString( 1 );
        } catch ( Exception ex )
        {
            System.out.println( "team.conso.data.BDDEco.getPrixProd() : " + ex );
            System.out.println( "sql = " + sql );
        }
        return result;
    }
    
    /**
     * fourni a getstok la traduction du DataFiltre en condition where
     * sous forme de string
     * 
     * @param filtre
     * @return
     */
    private String getWhereFromDataFilter( DataFilter filtre )
    {
        if ( filtre == null ) return ""; // si pas de filtre ben on a rien a faire la ;o)
        String where = "";
        try 
        {
			// fabrik la close where a partir du filtre 
            if ( filtre.getCate() != null )
                where += " AND " + tableCateName + ".libelle NOT IN ( " + filtre.getCateValues() + " ) ";
            if ( filtre.getProd() != null )
                where += " AND " + tableProdName  + ".libelle NOT IN ( " + filtre.getProdValues() + " ) ";
            if ( filtre.getDateDeb() != null ) // si il a une date defini
            {
                where += " `date limit` BETWEEN '" + BDDEco.convertDateToBdd( filtre.getDateDeb() ) 
                        + "' AND '" + BDDEco.convertDateToBdd( filtre.getDateFin() ) + "'";
            }
		} catch (Exception e) 
		{
            System.out.println( "team.conso.data.BDDEco.getWhereFromDataFilter() : " + e );
            System.out.println( "sql = " + where );
		}
		return where;
    }
    
    /**
     * methode utilisé en l absence de filtre
     */
    public Vector getStock()
    {
        return getStock( null );
    }
    
    public Vector getStock( DataFilter filtre )
    {
        String sql = "";
        Vector<Object> result = new Vector<Object>( 100 );
        try
        {
            // construction de la commande SQL
            // eu ula je me suis pas fouller penser a use un jour les prepareStatement ;o)
            sql = "SELECT " + tableCateName +".libelle AS cat, " + tableCateName + ".image, " +
            tableProdName +".libelle AS pro, " + tableProdName + ".image, " + "`date limit`,  date, nombre, " + tableStokName + ".cle, prix, `nombre de depard` ";
            sql += " FROM " + tableStokName + ", " +  tableCateName + ", " + tableProdName; 
            sql += " WHERE " + tableProdName + ".cate = " + tableCateName + ".cle AND " +
            tableStokName + ".prod = " + tableProdName + ".cle" + getWhereFromDataFilter( filtre );
            sql += " ORDER BY cat, pro";
            // execution de cette commande et obtention du resultat
	        Statement stm = connection.createStatement();
	        ResultSet rs = stm.executeQuery( sql );
	        Categorie currentCate = null;
	        Produit currentProd = null;
	        Calendar currentDate = GregorianCalendar.getInstance();
	        currentDate.set( 
	                currentDate.get( Calendar.YEAR),
	                currentDate.get( Calendar.MONTH)+1,
	                currentDate.get( Calendar.DAY_OF_MONTH)
	                ); // ca corrige un prob avec les date java ;-(
            while ( rs.next() )
	        {
                // init de la cate
	            Categorie cat = new Categorie( rs.getString( 1 ), rs.getString( 2 ) );
	            // init du prod
	            Produit pro = new Produit( rs.getString( 3 ), rs.getString( 4 ) );
	            // extraction de la date
	            StringTokenizer datL = new StringTokenizer ( rs.getString( 5 ), "-" );
	            int yyyy = Integer.parseInt( datL.nextToken() );
	            int mm = Integer.parseInt( datL.nextToken() );
	            int dd = Integer.parseInt( datL.nextToken() );
	            Calendar dateLim = new GregorianCalendar( yyyy, mm, dd );
	            //calcul de l alarme en enelvant le delai a la date limite
	            Calendar alarmDate = new GregorianCalendar( yyyy, mm, dd );
	            alarmDate.add( Calendar.DATE, -alarmDelai );
	            // init du stok
	            Stock stk = new Stock( rs.getString( 6 ), ""+dd+"/"+mm+"/"+yyyy, rs.getString( 7 ), rs.getString( 8 ), rs.getString( 9 )  );
	            stk.setLibProd( pro.getLibelle() ); // pour plus de commoditéé
	            stk.setNbDep( rs.getString( 10 ) );
	            int status = STATUS_NOTHING;
	            // test si perimee
	            if ( dateLim.before( currentDate )  ) 
	            {	// condition pour ne pas afficher les stk perim
	            	if ( filtre != null && filtre.isPerime() ) continue;
	                status = STATUS_TOO_LATE;
	            }
	            else if ( alarmDate.before( currentDate )  ) // test si en alarme
	            { 	// condition pour ne pas afficher les stk en alarme
	            	if ( filtre != null && filtre.isAlarme() ) continue;
	                status = STATUS_ALERTE;
	            }
	            else if ( filtre != null && filtre.isValide() ) continue;
	            if ( pro.getStatus() <= status ) 
	            {
	                pro.setStatus( status ); // pas besoin de faire 2 foi le meme test
	            }
	                
	            if ( cat.getStatus() <= status ) 
	            {
	                cat.setStatus( status );
	            }
                stk.setStatus( status );
	            // on repercute le status sur les groupes
	            // calcul si perimer
	            // genere la structure en arbe ds le vector
	            // chac element n etan representee qu une foi...;o)
	            if ( !cat.equal( currentCate ) )
	            {
	                result.add( cat );
		            currentCate = cat;
		            currentProd = null;
	            } 
	            else
	            {
	                if ( currentCate.getStatus() < cat.getStatus() )
	                    currentCate.setStatus( cat.getStatus() ); // sinon on ce contente de metre le statu a jour
	            }
	            if ( !pro.equal( currentProd ) )
	            {
	                result.add( pro );
		            currentProd = pro;
	            }
	            else
	            {
	                if ( currentProd.getStatus() < pro.getStatus() )
	                    currentProd.setStatus( pro.getStatus() );
	            }
	            result.add( stk );
	        } 
            rs.close();
            stm.close();
        }
        catch ( Exception ex )
        {
            System.out.println( "team.conso.data.BDDEco.getStock(  )  : " + ex );
            System.out.println( "sql = " + sql );
        }
        return result;
    }
    
    /**
     * converti une date de la bdd en date lisible
     * yyyy-mm-dd  vers dd/mm/yyyy
     * 
     * @return
     */
    public static String convertDateFromBdd( String d )
    {
        StringTokenizer datL = new StringTokenizer ( d, "-" );
        String yyyy = datL.nextToken();
        String mm = datL.nextToken();
        String dd = datL.nextToken();
        return dd+"/"+mm+"/"+yyyy;
    }

    /**
     * converti une date de la bdd en date lisible
     * dd/mm/yyyy  vers  yyyy-mm-dd  
     * 
     * @return
     */
    public static String convertDateToBdd( String d )
    {
        StringTokenizer datL = new StringTokenizer ( d, "/" );
        String dd = datL.nextToken();
        String mm = datL.nextToken();
        String yyyy = datL.nextToken();
        return yyyy+"-"+mm+"-"+dd;
    }

    public static String convertDateToBdd( Calendar cld )
    {
        int mm = cld.get( Calendar.MONTH );
        int dd = cld.get( Calendar.DAY_OF_MONTH );
        return  cld.get( Calendar.YEAR ) + "-" + 
        (mm<10?"0"+mm:""+mm) + "-" + (dd<10?"0"+dd:""+dd); // permet de cadrer les date sur 2 digits
    }

    public void addCate( String nom, String image )
    {
        String[] lib = {"libelle", "image" };
        String[] val = { nom, image };
        addRecord( tableCateName,  lib, val );
    }
    
    /**
     * on satend a trouver un num ds cate
     * @param nom
     * @param image
     * @param cate
     */
    public void addProd( String nom, String image, String cate )
    {
        String[] lib = {"libelle", "image", "cate" };
        String[] val = { nom, image, cate };
        addRecord( tableProdName, lib, val );
    }

    public void addStok( String numProd, String quantite, String dateLim, String date, String prix )
    {
        String sql = "";
        try
        {
            sql = "INSERT INTO " + tableStokName + " ( prod, nombre, `nombre de depard`, `date limit`, `date`, prix ) VALUES( '"
            + numProd + "', '" + quantite + "', '" + quantite + "', '" + dateLim + "', '" + date + "', '"+prix+"' ) " ;
            Statement stm = connection.createStatement();
	        stm.executeUpdate( sql );
	        stm.close();
	        // les donne on etait modifier
	        fireChangeUpdate( new BddEcoEvent( tableStokName, "", "" ) );
        } catch ( Exception ex )
        {
            System.out.println( "team.conso.data.BDDEco.addstok(  )  : " + ex );
            System.out.println( "sql = " + sql );
        }
    }
    
    private void addRecord( String table, String[] lib, String[] val )
    {
        String sql = "";
        try
        {
            String libs = "";
            String vals = "";
            for (int i = 0; i < lib.length; i++) 
            {
                libs += ", " + lib[ i ];
                vals += ", '" + val[ i ] + "' ";
            }
            sql = "INSERT INTO " + table + " ( " + libs.substring( 1 ) + " ) VALUES ( " + vals.substring( 1 ) + " ) ";
            Statement stm = connection.createStatement();
	        stm.executeUpdate( sql );
	        stm.close();
	        // les donne on etati modifier
	        fireChangeUpdate( new BddEcoEvent( table, "addrecord", "" ) );
        } catch ( Exception ex )
        {
            System.out.println( "team.conso.data.BDDEco.addRecord(  )  : " + ex );
            System.out.println( "sql = " + sql );
        }
    }
    
    public static class BddEcoEvent implements DataEvent
    {
        String eventDesc = "";
        public BddEcoEvent( String table, String nom, String image )
        {
            eventDesc = "" + table + ";" + nom + ";" + image;
        }
        public Object getSource()
        {
            return this; // pour linstant on fait mais ca ser a rien 
        }
        public Object getEventType()
        {
            return eventDesc;
        }
    }

    public Connection getConnection()
    {
        return connection;
    }
    public void setConnection(Connection connection)
    {
        this.connection = connection;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    /**
     * @param categorie
     * @return
     */
    public Vector getAchat( String categorie )
    {
        Vector<Object> result = new Vector<Object>( 100 );
        String sql = "";
        try
        {
            sql = "SELECT date, `nombre de depard` FROM " + tableStokName + ", " + tableProdName + ", " + tableCateName + 
                  " WHERE " + tableProdName + ".cate = " + tableCateName + ".cle AND " +
                  tableStokName + ".prod = " + tableProdName + ".cle AND " + 
                  tableCateName + ".libelle = '" + categorie + "'" +
                  " ORDER BY date";
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery( sql );
            Hashtable<String,String> ht = new Hashtable<String,String>();
            Vector<String> v = new Vector<String>(); // juste pour etre sur de garder le rangement
            while ( rs.next() )
            {
                StringTokenizer dat = new StringTokenizer( rs.getString( 1 ), "-" );
	            int yyyy = Integer.parseInt( dat.nextToken() );
	            int mm = Integer.parseInt( dat.nextToken() );
	            int dd = Integer.parseInt( dat.nextToken() );
	            Calendar cld = new GregorianCalendar( yyyy, mm, dd );
                if ( ht.get( ""+cld.getTimeInMillis() ) != null )
                {
                    
    	            int cumul = Integer.parseInt( (String)ht.get( "" + cld.getTimeInMillis() ) );
    	            cumul += Integer.parseInt( rs.getString( 2 ) );
    	            ht.put( "" + cld.getTimeInMillis(), "" + cumul );
                }
                else
                {
                    ht.put( ""+cld.getTimeInMillis(), rs.getString( 2 ) );
                    v.add( ""+cld.getTimeInMillis() );
                }
            }
            rs.close();
            stm.close();
            for ( Iterator iter = v.iterator(); iter.hasNext(); )
            {
                String date = (String)iter.next();
                Vector<String> coord = new Vector<String>();
                coord.add( date );
                coord.add( ht.get( date ) );
                result.add( coord );
            }
            return result;
        } catch ( Exception ex )
        {
            System.out.println( "team.conso.data.BDDEco.getAchat() : " + ex );
            System.out.println( " sql = " + sql );
        }
        return null;
    }
    public static boolean isTest()
    {
        return test;
    }
    public static void setTest( boolean test )
    {
        System.out.println( "set test  = " + test);
        BDDEco.test = test;
    }
}
