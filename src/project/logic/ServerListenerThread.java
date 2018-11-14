/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.logic;

import exceptions.EmailNotUniqueException;
import exceptions.LoginExistingException;
import exceptions.LoginNotExistingException;
import exceptions.WrongPasswordException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.Message;
import message.User;
import project.model.Model;

/**
 * This is the runnable class for the thread
 * @author Jon Gonzalez
 * @version 1.0
 */
public class ServerListenerThread implements Runnable{
    
    private final static Logger LOG = Logger.getLogger("ServerLogger");
    
    private final Socket client;
    
    private ServerSocketListener ssl;
    
    private Model model;
     /**
      * The constructor method with the client and the server socket
      * @param client
      * @param ssl 
      */
    public ServerListenerThread(Socket client, ServerSocketListener ssl, Model model) {
        this.client = client;
        this.ssl = ssl;
        this.model=model;
    }
    
    /**
     * This method open the streams
     */
    @Override
    public void run() {
        ssl.addThread();
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        Message message = new Message();
        try {
            oos=new ObjectOutputStream(client.getOutputStream());
            ois= new ObjectInputStream(client.getInputStream());
            message = (Message) ois.readObject();
            switch(message.getMessage()){
                case 1:
                    message.setData(model.loginUser((User) message.getData()));
                    message.setMessage(10);
                    break;
                case 2:
                    model.signUpUser((User) message.getData());
                    message.setMessage(20);
                    break;
            }
        } catch (LoginNotExistingException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
            message.setMessage(11);
        } catch (WrongPasswordException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
            message.setMessage(12);
        } catch (LoginExistingException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
            message.setMessage(21);
        }catch (EmailNotUniqueException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
            message.setMessage(22);
        }catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
            message.setMessage(0);
        }finally{
            try{
                oos.writeObject(message);
                if (ois != null){
                    ois.close();
                }
                if (oos != null){
                    oos.close();
                }
                if (client != null){
                    client.close();
                }
            }catch (IOException ex) {
                LOG.log(Level.SEVERE, ex.getMessage(), ex);
            }
            ssl.substractThread();
        }
    }
}
