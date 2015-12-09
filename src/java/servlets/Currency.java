/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

/**
 *
 * @author Beng
 */

import java.rmi.*;

public interface Currency extends Remote{
    public double getExchangeRate(String currencyType) throws RemoteException;
}
