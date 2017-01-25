
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
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
public class AverageResponseTime implements Runnable{
 String portno=null;
    String directoryName=null;
    String fileTobeSearched=null;
           BufferedReader inp = new BufferedReader(new InputStreamReader(System.in));
     AverageResponseTime(String portno,String directoryName)
     {
         this.portno=portno;
         this.directoryName=directoryName;
         
     }
 @Override
	public void run(){        
            String peerID=null;
            String srcPeerID="2";
            String srcPortNo="4002";
            String srcfileName="TestingFile.txt";
            String srcDir="C:\\Users\\Nikitha Mahesh\\Desktop\\HOM_WORK";
            long responseTime = 0;
            long endTime=0;
		   
		  try {
                      HelloInterface helloObj=null;    
                try {
                    helloObj = (HelloInterface) Naming.lookup("Hello");
                } catch (NotBoundException ex) {
                    Logger.getLogger(AverageResponseTime.class.getName()).log(Level.SEVERE, null, ex);
                } catch (MalformedURLException ex) {
                    Logger.getLogger(AverageResponseTime.class.getName()).log(Level.SEVERE, null, ex);
                }
                      
                      // to fetch all the files from the current directory(client)
                      File directoryList = new File(directoryName);
                       String[] store = directoryList.list();
                  int counter=0;
                 
                   while(counter<store.length)
                   {
                   File currentFile = new File(store[counter]);
                           try {
                               helloObj.registerFiles(peerID, currentFile.getName(),portno,directoryName);
                           } catch (RemoteException ex) {
                               Logger.getLogger(ThreadOfHelloClient.class.getName()).log(Level.SEVERE, null, ex);
                           }
                           counter++;
                 } 
                   //int trackingCount=0;
                    // method to search for the file (1000 times)
                     for(int i=0;i<1000;i++)
                               {
                                   
                              long start = System.currentTimeMillis();
             downloadFromPeer(srcPeerID,srcPortNo,srcfileName,srcDir);
              endTime= System.currentTimeMillis()-start;
               responseTime=responseTime+ endTime;
               
               System.out.println("trackingCount"+i);
                               }
                     // print the avg response time 
                     
                     System.out.println("responseTime is"+responseTime+"ms");
        long avgResponseTime=responseTime/1000;
        System.out.println("avgResponseTime is"+avgResponseTime+"ms");
                     
                     
                   } catch (RemoteException ex) {
                               Logger.getLogger(ThreadOfHelloClient.class.getName()).log(Level.SEVERE, null, ex);
                           } catch (NotBoundException ex) {
         Logger.getLogger(AverageResponseTime.class.getName()).log(Level.SEVERE, null, ex);
     } catch (IOException ex) {
         Logger.getLogger(AverageResponseTime.class.getName()).log(Level.SEVERE, null, ex);
     }
                           
                              
        }
		
public void downloadFromPeer(String peerid,String portNo,String fileName,String srcDir) throws NotBoundException, RemoteException, MalformedURLException, IOException{
  //get port

  HelloClient peerServer = (HelloClient) Naming.lookup("rmi://localhost:"+portNo+"/FileServer");
  //String filetoDownload=peerServer.

  String source = srcDir+"\\"+fileName;
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
public static void main(String [] args) throws IOException, NotBoundException{
    
     String portno="5000";
     String directoryName = "C:\\Users\\Nikitha Mahesh\\Desktop\\testDirectory";
    
          
     try{
         LocateRegistry.createRegistry(Integer.parseInt(portno)); 
       HelloClient fi = new FileImpl(directoryName);
       System.out.println("Directory name"+directoryName);
         Naming.rebind("rmi://localhost:"+portno+"/FileServer", fi);
 } catch(Exception e) {
         System.err.println("FileServer exception: "+ e.getMessage());
         e.printStackTrace();
      }
	new AverageResponseTime(portno,directoryName).run();
       
     
	}
    
}
