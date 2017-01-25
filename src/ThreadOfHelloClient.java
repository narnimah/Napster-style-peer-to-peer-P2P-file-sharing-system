
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Nikitha Mahesh
 */
public class ThreadOfHelloClient implements Runnable{
    String portno=null;
    String directoryName=null;
    String fileTobeSearched=null;
           BufferedReader inp = new BufferedReader(new InputStreamReader(System.in));
     ThreadOfHelloClient(String portno,String directoryName)
     {
         this.portno=portno;
         this.directoryName=directoryName;
     }
	public void run(){        
            String peerID=null;
             
		try   
		{    
		   HelloInterface hello = (HelloInterface) Naming.lookup("Hello");    
		   
		   BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		   String line;
                   
                   System.out.println("Enter the peer ID");
                       peerID = br.readLine();
                      
                       File directoryList = new File(directoryName);

                  String[] store = directoryList.list();
                  int counter=0;
                 
                   while(counter<store.length)
                   {
                   File currentFile = new File(store[counter]);
                           try {
                               hello.registerFiles(peerID, currentFile.getName(),portno,directoryName);
                           } catch (RemoteException ex) {
                               Logger.getLogger(ThreadOfHelloClient.class.getName()).log(Level.SEVERE, null, ex);
                           }
                           counter++;
                 } 
               // method to search for the file
              
               ArrayList<FileDetails> arr = new ArrayList<FileDetails>();
                  System.out.println("Enter the file name to be searched"); 
                  //String fileTobeSearched=null;
                   while((fileTobeSearched=br.readLine())!=null){  
                     arr=hello.search(fileTobeSearched);
                       //System.out.println("Peer ID's having the given file are"+arr.get(na));
                       for(int i = 0; i < arr.size(); i++) {
            System.out.println("Peer ID's having the given file are"+arr.get(i).peerId);
        }
                   System.out.println("Enter the peerID of the peer you want to connect?");
                  peerID= br.readLine();
                   downloadFromPeer(peerID,arr);
                       break;
		   }
		}    
		catch (Exception e)    
		{    
		   System.out.println("HelloClient exception: " + e);    
		}       
	}
public void downloadFromPeer(String peerid,ArrayList<FileDetails> arr) throws NotBoundException, RemoteException, MalformedURLException, IOException{
  //get port
  String portForAnotherClient=null;
  String sourceDir=null;
  for(int i=0;i<arr.size();i++){
      if(peerid.equals(arr.get(i).peerId)){
          portForAnotherClient=arr.get(i).portNumber;
          sourceDir=arr.get(i).SourceDirectoryName;
      }
  }
  HelloClient peerServer = (HelloClient) Naming.lookup("rmi://localhost:"+portForAnotherClient+"/FileServer");
  //String filetoDownload=peerServer.

  String source = sourceDir+"\\"+fileTobeSearched;
        //directory where file will be copied
       String target =directoryName;
      
        InputStream is = null;
    OutputStream os = null;
    try {
        File srcFile = new File(source);
        File destFile = new File(target);
        System.out.println("file "+destFile);
        if(!destFile.exists())
        {
            destFile.createNewFile();
        }
        is = new FileInputStream(srcFile);
        
        os = new FileOutputStream(target+"\\"+srcFile.getName());
        byte[] buffer = new byte[1024];
        int length;
        while ((length = is.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }
    } 
      catch(Exception e)
            {
            e.printStackTrace();
            }
    finally {
        is.close();
        os.close();
    }
  }
public static void main(String [] args) throws IOException{
    
     BufferedReader inp = new BufferedReader(new InputStreamReader(System.in));
     String portno=null;
     System.out.println("Enter the port number on which peer needs to be registered");
          
                portno=inp.readLine();
                 System.out.println("Enter the directory path");
                      String directoryName = inp.readLine();
          
     try{
         LocateRegistry.createRegistry(Integer.parseInt(portno)); 
       HelloClient fi = new FileImpl(directoryName);
       System.out.println("Directory name"+directoryName);
         Naming.rebind("rmi://localhost:"+portno+"/FileServer", fi);
 } catch(Exception e) {
         System.err.println("FileServer exception: "+ e.getMessage());
         e.printStackTrace();
      }
	new ThreadOfHelloClient(portno,directoryName).run();
       
     
	}
}
