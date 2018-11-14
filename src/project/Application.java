/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import java.io.IOException;
import java.util.logging.Logger;
import project.logic.ServerSocketListener;
import project.model.Model;
import project.model.ModelFactory;

/**
 *
 * @author Jon Gonzalez and Ion Mikel Lopez
 */
public class Application{
      private final static Logger LOG = Logger.getLogger("mainLogger");
    /**
      * The initialization of the server application 
      * @param args
      * @throws IOException
      * @throws ClassNotFoundException 
      */
    public static void main(String[] args) throws IOException, 
            ClassNotFoundException {
        LOG.info("Starting application.");
        Model model= ModelFactory.getModel();
        ServerSocketListener server=new ServerSocketListener();
        server.listenerThread(model);
    }
    
}
