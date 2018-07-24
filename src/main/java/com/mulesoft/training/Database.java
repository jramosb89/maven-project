package com.mulesoft.training;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

public class Database implements InitializingBean {
    private static Logger logger = LoggerFactory.getLogger(Database.class);
    
    public void afterPropertiesSet() throws Exception {
    	Locale.setDefault(new Locale("es", "CO"));
        String dbURL = "jdbc:derby:memory:muleEmbeddedDB;create=true";
        Connection conn = null;
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            conn = DriverManager.getConnection(dbURL);
            DatabaseMetaData md = conn.getMetaData();
            ResultSet rs = md.getTables(null, null, "%", null);
            logger.debug("&&&& - DB Init - &&&&");
            int i=0;
            while (rs.next()){
                logger.debug("there is next " + rs.getString(3));
                if(rs.getString(3).equalsIgnoreCase("FLIGHTS")) i=1; //set a marker that this table already exists
            }
            logger.debug("&&&& - DB Init - &&&&");
            Statement stmt = conn.createStatement();
            if(i!=1){
            stmt.executeUpdate("CREATE TABLE FLIGHTS(ID INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 0)  NOT NULL PRIMARY KEY," +
                    "PRICE INT," +
                    "DESTINATION VARCHAR(255)," +
                    "ORIGIN VARCHAR(255))");
            stmt.executeUpdate("INSERT INTO FLIGHTS(PRICE, DESTINATION, ORIGIN) VALUES (555, 'SFO','YYZ')");
            stmt.executeUpdate("INSERT INTO FLIGHTS(PRICE, DESTINATION, ORIGIN) VALUES (450, 'LAX','YYZ')");
            stmt.executeUpdate("INSERT INTO FLIGHTS(PRICE, DESTINATION, ORIGIN) VALUES (777, 'SEA','SQL')");
            stmt.executeUpdate("INSERT INTO FLIGHTS(PRICE, DESTINATION, ORIGIN) VALUES (999, 'SFO','SQL')");
            }
            
        } 
        catch (java.sql.SQLException sqle) {
            sqle.printStackTrace();
            throw sqle;
        }
    }
}