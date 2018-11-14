package project.model;

import exceptions.EmailNotUniqueException;
import exceptions.LoginExistingException;
import exceptions.LoginNotExistingException;
import java.sql.Connection;
import exceptions.WrongPasswordException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.Privilege;
import message.Status;
import message.User;


/**
 *
 * @author Ion Mikel, Lander Lluvia and Jon Gonzalez
 */
public class ModelImplementation  implements Model{
    private static final Logger LOG = 
            Logger.getLogger(ModelImplementation.class.getName());
    
    private DBConnection db;
    
    public ModelImplementation(){
        try {
            db =new DBConnection();
        } catch (IOException ex) {
            Logger.getLogger(ModelImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
    
    /**
     * 
     * @param user
     * @return
     * @throws LoginNotExistingException
     * @throws WrongPasswordException
     * @throws Exception 
     */
    @Override
    public User loginUser(User user)throws LoginNotExistingException,WrongPasswordException, Exception{
        Connection con = null;
        PreparedStatement  ps = null;
        ResultSet rs = null;
        try{
            con = db.getConnection();
            String query = "SELECT * FROM grupo4.users WHERE login=?;";
            ps = con.prepareStatement(query);
            ps.setString(1, user.getLogin());
            rs = ps.executeQuery();
            
            //compruebo que el rs no este vacio y si lo esta lanzo LoginNotExistingException.
            if(rs.next()){
                if(rs.getString("status").equals("enabled")){
                    //recojer el user y comprobar la pw.
                    String password = rs.getString("password");
                    if (!password.equals(user.getPassword())){
                        throw new WrongPasswordException();
                    }else{
                        user.setId(rs.getInt("id"));
                        user.setEmail(rs.getString("email"));
                        user.setFullName(rs.getString("fullname"));
                        user.setStatus(Status.enabled);
                        if(rs.getString("privilege").equals("user")){
                            user.setPrivilege(Privilege.user);
                        }else{
                            user.setPrivilege(Privilege.admin);
                        }
                        user.setLastAcces(Timestamp.valueOf(LocalDateTime.now()));
                        user.setLastPasswordChange(Timestamp.valueOf(LocalDateTime.now()));
                        query="Update users set lastAccess=? where id=?";
                        ps = con.prepareStatement(query);
                        ps.setTimestamp(1, user.getLastAcces());
                        ps.setInt(2, user.getId());
                        ps.executeUpdate();
                    }
                }else{
                    throw new Exception("Esta deshabilitado");
                }
            }else{
                throw new LoginNotExistingException();
            }
            
            rs.close();
            ps.close();
            con.close();
            return user;
        }catch(SQLException e){
            e.printStackTrace();
        }
    return null;
    }
        /**
         * 
         * @param user
         * @throws LoginExistingException
         * @throws EmailNotUniqueException
         * @throws Exception 
         */
        @Override
    public void signUpUser(User user)throws LoginExistingException,
            EmailNotUniqueException,Exception{
        Connection con=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try{
            con = db.getConnection();
            user.setLastAcces(Timestamp.valueOf(LocalDateTime.now()));
            user.setLastPasswordChange(Timestamp.valueOf(LocalDateTime.now()));

            String queryL = "SELECT * FROM grupo4.users WHERE login=?;";

            ps = con.prepareStatement(queryL);
            ps.setString(1, user.getLogin());
            rs = ps.executeQuery();

            if(rs.next()){
                throw new LoginExistingException();   
            }else{
                String queryE = "SELECT * FROM grupo4.users WHERE email =?;"; 
                PreparedStatement  psE = con.prepareStatement(queryE);
                psE.setString(1, user.getEmail());
                ResultSet rs1 = psE.executeQuery();
                
                if(rs1.next()){
                    throw new EmailNotUniqueException();
                }else{
                    String query = "INSERT INTO grupo4.users(login,email,fullName,"
                            + "password,lastAccess,lastPasswordChange,"
                            + "status,privilege)"
                            + " VALUES (?,?,?,?,?,?,?,?);";
                    ps = con.prepareStatement(query); 
                    ps.setString(3, user.getFullName());
                    ps.setString(2, user.getEmail());
                    ps.setString(1, user.getLogin());
                    ps.setString(4, user.getPassword());
                    ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
                    ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
                    ps.setString(7, user.getStatus().toString());
                    ps.setString(8, user.getPrivilege().toString());
                    ps.executeUpdate();

                    LOG.info("User have been inserted.");
                }   
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            rs.close();
            ps.close();
            con.close();
        }
    }
} 
            
           
        
           
        
        
      
 
    

