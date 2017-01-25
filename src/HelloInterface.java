
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Nikitha Mahesh
 */
public interface HelloInterface extends Remote{

 public void registerFiles(String peerId, String filename,String portno,String srcDir)throws RemoteException; 
  public ArrayList<FileDetails> search(String filename)throws RemoteException; 
 //public void calculateAvgResponseTime(String fileName, String peerId,String portNo, String srcDir) throws RemoteException;
}
