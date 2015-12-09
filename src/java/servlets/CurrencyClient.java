package servlets;

import java.rmi.*;
import java.net.*;

public class CurrencyClient {

  public static void main(String args[]) {
    String sRMI = "rmi://localhost:1099/currencyexchange";        
    String sNum="500";
    String currencyType = "SGD";
    try {      
      Currency calculator = (Currency) Naming.lookup(sRMI);
      Integer value = new Integer(sNum);
      double exchangeRate = calculator.getExchangeRate(currencyType);
      System.out.println("The price in " + currencyType + ": " + value*exchangeRate);
    }
    catch (MalformedURLException ex) {
      System.err.println(sRMI + " is not a valid RMI URL");
    }
    catch (RemoteException ex) {
      System.err.println("Remote object threw exception " + ex);
    }
    catch (NotBoundException ex) {
      System.err.println("Could not find the requested remote object on the server");
    } 
  }
}
