package Repositories;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Entities.CDR;
import Gateways.PersistenciaCDR;
 

public class CDRSqlRepository implements PersistenciaCDR{
	public void createTable() {
		Connection c = null;
	      Statement stmt = null;
	      
	      try {
	         Class.forName("org.sqlite.JDBC");
	         c = DriverManager.getConnection("jdbc:sqlite:CLARO.db");
	         System.out.println("Opened database successfully");

	         stmt = c.createStatement();
	         String sql = "CREATE TABLE IF NOT EXISTS CDR" +
	                        "(ID INTEGER PRIMARY KEY	AUTOINCREMENT	NOT NULL," +
	                        " TELF_ORIGEN	TEXT  		NOT NULL, " + 
	                        " TELF_DESTINO		TEXT  		NOT NULL, " + 
	                        " FECHALLAMADA		TEXT     	NOT NULL, " +
	                        " HORALLAMADA		TEXT     	NOT NULL, " + 
	                        " DURACIONLLAMADA	TEXT     	NOT NULL, " +
	                        " TARIFA			DOUBLE     	NOT NULL, " +
	                        " ID_TARIFICACION	INT     	NOT NULL )"; 
	         stmt.executeUpdate(sql);
	         stmt.close();
	         c.close();
	      } catch ( Exception e ) {
	         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	         System.exit(0);
	      }
	      System.out.println("Table created successfully");
	}
	public void guardarCDR(CDR cdr,int id_tarificacion) {
		this.createTable();
		Connection c = null;
	    Statement stmt = null;
	   
	    try {
	       Class.forName("org.sqlite.JDBC");
	       c = DriverManager.getConnection("jdbc:sqlite:CLARO.db");
	       c.setAutoCommit(false);
	       System.out.println("Opened database successfully");
	       stmt = c.createStatement();
	       String sql = "INSERT INTO CDR (TELF_ORIGEN,TELF_DESTINO,FECHALLAMADA,HORALLAMADA,DURACIONLLAMADA,TARIFA,ID_TARIFICACION) " +
	                      "VALUES ("+cdr.getTelfOrigen()+","+cdr.getTelfDestino()+",'"
	                      +(cdr.getFecha().replace('-', 'a')) +"','"+(cdr.getHoraLlamada().replace(':', 'a')) 
	                      +"','"+ (cdr.getDuracionLlamada().replace(':', 'a'))+"',"+cdr.getTarifa()+","+id_tarificacion+");";
	       
	       stmt.executeUpdate(sql);
	       stmt.close();
	       c.commit();
	       c.close();
	    } catch ( Exception e ) {
	       System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	       System.exit(0);
	    }
	    cdr.setId(getLastId());
	}
	public CDR getCDR(int id) {
		this.createTable();
		Connection c = null;
	    Statement stmt = null;
	    CDR cdr=null;
	    try {
		      Class.forName("org.sqlite.JDBC");
		      c = DriverManager.getConnection("jdbc:sqlite:CLARO.db");
		      c.setAutoCommit(false);
		      System.out.println("Opened database successfully");

		      stmt = c.createStatement();
		      ResultSet rs = stmt.executeQuery( "SELECT * FROM CDR WHERE ID ="+id+ ";" );
		      
		      while ( rs.next() ) {
		    	  cdr=new CDR();
			      cdr.setTelfOrigen(rs.getString("TELF_ORIGEN"));
			      cdr.setTelfDestino(rs.getString("TELF_DESTINO"));
			      cdr.setFecha(rs.getString("FECHALLAMADA").replace('a', '-'));
			      cdr.setHoraLlamada(rs.getString("HORALLAMADA").replace('a', ':'));
			      cdr.setDuracionLlamada(rs.getString("DURACIONLLAMADA").replace('a', ':'));
			      cdr.setId(rs.getInt("id"));
			      cdr.setTarifa(rs.getDouble("TARIFA"));
		      }
		      rs.close();
		      stmt.close();
		      c.close();
		   } catch ( Exception e ) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      System.exit(0);
		   }
	    return cdr;
	}
	public int getLastId(){
		this.createTable();
		int id=1;
		Connection c = null;
	    Statement stmt1 = null;
	    try {
		      Class.forName("org.sqlite.JDBC");
		      c = DriverManager.getConnection("jdbc:sqlite:CLARO.db");
		      c.setAutoCommit(false);
		      System.out.println("Opened database successfully");

		      stmt1 = c.createStatement();
		      ResultSet rs = stmt1.executeQuery( "SELECT * FROM CDR WHERE ID = (SELECT MAX(ID) FROM CDR);" );
		      id=rs.getInt("ID");
		      rs.close();
		      stmt1.close();
		      c.close();
		   } catch ( Exception e ) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      System.exit(0);
		   }
		return id;
	}
	
	public List<CDR> getCDRSbyTarificationId(int id) {
		this.createTable();
		List<CDR> listaCDRs=new ArrayList<CDR>();
		Connection c = null;
	    Statement stmt = null;
	    CDR cdr=null;
	    try {
		      Class.forName("org.sqlite.JDBC");
		      c = DriverManager.getConnection("jdbc:sqlite:CLARO.db");
		      c.setAutoCommit(false);
		      System.out.println("Opened database successfully");

		      stmt = c.createStatement();
		      ResultSet rs = stmt.executeQuery( "SELECT * FROM CDR WHERE ID_TARIFICACION ="+id+ ";" );
		      
		      while ( rs.next() ) {
		    	  cdr=new CDR();
			      cdr.setTelfOrigen(rs.getString("TELF_ORIGEN"));
			      cdr.setTelfDestino(rs.getString("TELF_DESTINO"));
			      cdr.setFecha(rs.getString("FECHALLAMADA").replace('a', '-'));
			      cdr.setHoraLlamada(rs.getString("HORALLAMADA").replace('a', ':'));
			      cdr.setDuracionLlamada(rs.getString("DURACIONLLAMADA").replace('a', ':'));
			      cdr.setId(rs.getInt("ID"));
			      cdr.setTarifa(rs.getDouble("TARIFA"));
			      listaCDRs.add(cdr);
			      
		      }
		      rs.close();
		      stmt.close();
		      c.close();
		   } catch ( Exception e ) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      System.exit(0);
		   }
	    return listaCDRs;
	}
	
	public List<CDR> getCDRSbyTelfOrigen(String telfOrigen) {
		this.createTable();
		List<CDR> listaCDRs=new ArrayList<CDR>();
		Connection c = null;
	    Statement stmt = null;
	    CDR cdr=null;
	    try {
		      Class.forName("org.sqlite.JDBC");
		      c = DriverManager.getConnection("jdbc:sqlite:CLARO.db");
		      c.setAutoCommit(false);
		      System.out.println("Opened database successfully");

		      stmt = c.createStatement();
		      ResultSet rs = stmt.executeQuery( "SELECT * FROM CDR WHERE TELF_ORIGEN ="+telfOrigen+ ";" );
		      
		      while ( rs.next() ) {
		    	  cdr=new CDR();
			      cdr.setTelfOrigen(rs.getString("TELF_ORIGEN"));
			      cdr.setTelfDestino(rs.getString("TELF_DESTINO"));
			      cdr.setFecha(rs.getString("FECHALLAMADA").replace('a', '-'));
			      cdr.setHoraLlamada(rs.getString("HORALLAMADA").replace('a', ':'));
			      cdr.setDuracionLlamada(rs.getString("DURACIONLLAMADA").replace('a', ':'));
			      cdr.setId(rs.getInt("ID"));
			      cdr.setTarifa(rs.getDouble("TARIFA"));
			      listaCDRs.add(cdr);
		      }
		      rs.close();
		      stmt.close();
		      c.close();
		   } catch ( Exception e ) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      System.exit(0);
		   }
	    return listaCDRs;
	}
}
