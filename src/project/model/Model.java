/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.model;

import exceptions.EmailNotUniqueException;
import exceptions.LoginExistingException;
import exceptions.LoginNotExistingException;
import exceptions.WrongPasswordException;
import message.User;


/**
 *
 * @author Gorka
 */
public interface Model {
    /**
     * Method that checks with the database that the user and the password
     * are correct
     * @param user identifys the user that is trying to login
     * @return User with all the attributes 'rellenos'
     * @throws LoginNotExistingException in case that the user doesn't exist
     * @throws WrongPasswordException in case that the password is wrong
     * @throws Exception in any other case
     */
    public User loginUser(User user) throws LoginNotExistingException,
            WrongPasswordException, Exception;
    /**
     * Method that registers a new user
     * @param user contains all the data required for the user to login
     * @throws LoginExistingException in case that the user does exist
     * @throws EmailNotUniqueException in case that the email is repeated
     * @throws Exception in any other case
     */
    public void signUpUser(User user) throws LoginExistingException,
            EmailNotUniqueException, Exception;
}
