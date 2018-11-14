package project.model;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * 
 * @author Ion Mikel
 * 
 */
public class DBConnection {
    private final Logger LOG;
    public  String db_driver=null;
    public String db_url=null;
    public String db_username=null;
    public String db_password=null;

    public DBConnection() throws IOException{
        ResourceBundle urlFich= ResourceBundle.getBundle("project.model.parameters");
        db_driver=urlFich.getString("db.driver");
        db_url=urlFich.getString("db.url");
        db_username=urlFich.getString("db.username");
        db_password=urlFich.getString("db.password");
        this.LOG = Logger.getLogger(Connection.class.getName());
        /*
        Properties prop= new Properties();
        try{
        FileInputStream fis= new FileInputStream("src/parameters.properties");
        prop.load(fis);
        }catch(FileNotFoundException e){
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        
        db_driver=prop.getProperty("db.driver");
        db_url=prop.getProperty("db.url");
        db_username=prop.getProperty("db.username");
        db_password=prop.getProperty("db.password");*/
    }
    
    public Connection getConnection() {
        
        Connection con = null;
        /**
         * Load the Driver Class
         */
        try{
            Class.forName(db_driver);
            con = DriverManager.getConnection(db_url, db_username, db_password);
            
        }catch(SQLException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }catch(Exception e){
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
	return con;
    }

}
