
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
public class ConcurrentFileSearch implements Runnable{
    String portno=null;
    String directoryName=null;
    String fileTobeSearched=null;
    String peerId=null;
    long responseTime=0;
    long start =0;
    long endTime=0;
           BufferedReader inp = new BufferedReader(new InputStreamReader(System.in));
     ConcurrentFileSearch(String portno,String directoryName,String peerId)
     {
         this.portno=portno;
         this.directoryName=directoryName;
         this.peerId=peerId;
     }
	public void run(){ 
             System.out.println("This is currently running, " + Thread.currentThread().getId());  
             
		try   
		{    
		  HelloInterface hello = (HelloInterface) Naming.lookup("Hello");    
		   
		   
                  File directoryList = new File(directoryName);

                  String[] store = directoryList.list();
                  int counter=0;
                 
                   while(counter<store.length)
                   {
                   File currentFile = new File(store[counter]);
                           try {
                               hello.registerFiles(peerId, currentFile.getName(),portno,directoryName);
                           } catch (RemoteException ex) {
                               Logger.getLogger(ThreadOfHelloClient.class.getName()).log(Level.SEVERE, null, ex);
                           }
                           counter++;
                 } 
               // method to search for the file
               start = System.nanoTime();
                System.out.println("start is: " + start);  
              downloadFromPeer("C:\\Users\\Nikitha Mahesh\\Desktop\\testDirectory","4","TestingFile1.doc");
         endTime= System.nanoTime()-start;
           System.out.println("endTime is: " + endTime);  
               responseTime=responseTime+ endTime;
               System.out.println("responseTime is: " + responseTime);  
		}    
		catch (Exception e)    
		{    
		   System.out.println("HelloClient exception: " + e);    
		}       

	}
public void downloadFromPeer(String srcDir,String peerid,String fileName) throws NotBoundException, RemoteException, MalformedURLException, IOException{

  

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
public static void main(String [] args) throws IOException{
    
    String clientPortno1="6000";
    String clientPortno2="6001";
    String clientPortno3="6002";
    String directoryName1="C:\\Users\\Nikitha Mahesh\\Desktop\\SMA";
    String directoryName2="C:\\Users\\Nikitha Mahesh\\Desktop\\HOM_WORK";
    String directoryName3="C:\\Users\\Nikitha Mahesh\\Desktop\\spring_16_fees";
          
     try{
         LocateRegistry.createRegistry(Integer.parseInt(clientPortno1)); 
         LocateRegistry.createRegistry(Integer.parseInt(clientPortno2)); 
         LocateRegistry.createRegistry(Integer.parseInt(clientPortno3)); 
         
       HelloClient fi1 = new FileImpl(directoryName1);
       HelloClient fi2= new FileImpl(directoryName2);
       HelloClient fi3 = new FileImpl(directoryName3);
       
       Naming.rebind("rmi://localhost:"+clientPortno1+"/FileServer", fi1);
       Naming.rebind("rmi://localhost:"+clientPortno2+"/FileServer", fi2);
       Naming.rebind("rmi://localhost:"+clientPortno3+"/FileServer", fi3);
       
       
       
         
 } catch(Exception e) {
         System.err.println("FileServer exception: "+ e.getMessage());
         e.printStackTrace();
      }
    Thread thread1 = new Thread(new ConcurrentFileSearch(clientPortno1,directoryName1,"1"));  
     Thread thread2 = new Thread(new ConcurrentFileSearch(clientPortno2,directoryName2,"2"));  
     Thread thread3 = new Thread(new ConcurrentFileSearch(clientPortno3,directoryName3,"3"));  
     thread1.start();
     thread2.start();
     thread3.start();
        
        
       
     
	}
    
}
