package servlets;

import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;

public class CurrencyImpl extends UnicastRemoteObject implements Currency {

  private double MYR = 4.19;
  private double AUD = 1.36;
  private double CNY = 6.40;
  private double HKD = 7.75;
  private double GBP = 0.66;
  private double CAD = 1.34;
  private double PHP = 47.03;
  private double BDT = 78.27;
  private double IDR = 13844.50;
  private double SGD = 1.40;
  
  public CurrencyImpl() throws RemoteException {
    super();
  }
    
  public double getExchangeRate(String currencyType) throws RemoteException {
    double exchangeRate;
    
    switch(currencyType){
          case "MYR":
              exchangeRate = MYR;
              break;
          case "AUD":
              exchangeRate = AUD;
              break;
          case "CNY":
              exchangeRate = CNY;
              break;
          case "HKD":
              exchangeRate = HKD;
              break;
          case "GBP":
              exchangeRate = GBP;
              break;
          case "CAD":
              exchangeRate = CAD;
              break;
          case "PHP":
              exchangeRate = PHP;
              break;
          case "BDT":
              exchangeRate = BDT;
              break;
          case "IDR":
              exchangeRate = IDR;
              break;
          case "SGD":
              exchangeRate = SGD;
              break;
          default:
              exchangeRate = 1;
              break;
      }
    return exchangeRate;
  }
}
