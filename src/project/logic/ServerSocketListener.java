/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.logic;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import project.model.Model;

/**
 * It's the socket of the server. It catchs the clients requests and throw they
 * to the corresponding thread.
 * @author Jon Gonzalez
 * @version 1.0
 */
public class ServerSocketListener {
    
    private final static Logger LOG = Logger.getLogger("serverLogger");
    
    private int port;
    
    private int quantityThreads=0;
    
    /**
     * Add one to the quantity of threads
     */
    public synchronized void addThread(){
        quantityThreads++;
        LOG.log(Level.INFO, "There are {0} thread(s)", quantityThreads);
    }
    
    /**
     * Substract one to the quantity of threads
     */
    public synchronized void substractThread(){
        quantityThreads--;
        LOG.log(Level.INFO, "There are {0} thread(s)", quantityThreads);
    }
    
    /**
     * Create the threads when the server socket listen any client
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public void listenerThread(Model model) throws IOException, ClassNotFoundException {
        properties();
        ServerSocket server=null;
        Socket client=null;
        try{
            LOG.info("Creating the socket.");
            //Give a free port to the server socket
            server=new ServerSocket(port);
            while(true){
                //The maximum connections are twenty five 
                if(quantityThreads<25){
                    LOG.info("Accepting.");
                    //Listen for a client request
                    client = server.accept();
                    LOG.info("Creating the thread.");
                    //Create a runnable class and sending the client requesting
                    //and this object for add or substract in the threads count
                    ServerListenerThread slt= 
                            new ServerListenerThread(client, this, model);
                    //Create the thread and start it
                    Thread t = new Thread(slt);
                    t.start();
                }
            }
        }catch(IOException e1){
            LOG.log(Level.SEVERE, "IO exception closing the socket", e1);
        }finally{
            if(server!=null){
                server.close();
            }
            if(client!=null){
                client.close();
            }
        }
    }

    private void properties() {
         ResourceBundle urlFich= ResourceBundle.getBundle("project.model.parameters");
         port = Integer.parseInt(urlFich.getString("port"));
    }
}
