package servlets;

import java.rmi.*;

public class RegistryLister {

  public static void main(String[] args) {
    int port = 1111;
    String host = "localhost";
    String url = "rmi://" + host + ":" + port + "/";
    
    try {
      String[] remoteObjects = Naming.list(url);
      for (String remoteObject : remoteObjects) {
        System.out.println(remoteObject);
      }
    }
    catch (RemoteException ex) {
      System.err.println(ex);
    }
    catch (java.net.MalformedURLException ex) {
      System.err.println(ex);
    }
  }
}
