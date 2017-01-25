
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Nikitha Mahesh
 */

    public class FileImpl extends UnicastRemoteObject
  implements HelloClient {

   private String directoryName;

   public FileImpl(String s) throws RemoteException{
      super();
      directoryName = s;
   }

   public byte[] downloadFile(String fileName){
      try {
         File file = new File(directoryName+"/"+fileName);
         byte buffer[] = new byte[(int)file.length()];
         BufferedInputStream input = new
      BufferedInputStream(new FileInputStream(directoryName+"//"+fileName));
         input.read(buffer,0,buffer.length);
         input.close();
         return(buffer);
      } catch(Exception e){
         System.out.println("FileImpl: "+e.getMessage());
         e.printStackTrace();
         return(null);
      }
   }
}

 

