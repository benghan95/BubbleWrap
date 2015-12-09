package servlets;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CurrencyServer {

  public static void main(String[] args) {
    
    try {
      CurrencyImpl currency = new CurrencyImpl();
      LocateRegistry.createRegistry(1099);
      Registry registry = LocateRegistry.getRegistry();
      
      registry.rebind("currencyexchange", currency);
      
      //Naming.rebind("fibonacci", f);
      System.out.println("Currency Exchange Rate Server ready.");
    }
     catch (RemoteException rex) {
      System.out.println("Exception in CurrencyImpl.main: " + rex);
    }
//    catch (MalformedURLException ex) {
//      System.out.println("MalformedURLException " + ex);
//    }
  }

}
