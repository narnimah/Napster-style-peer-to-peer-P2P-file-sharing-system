
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;    

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Nikitha Mahesh
 */
public class ThreadOfHelloServer implements Runnable   
{   
   public void run()    
   {    
      try   
      {
         LocateRegistry.createRegistry(1099);    
         HelloInterface hello = new Hello();    
         Naming.rebind("Hello", hello);    
            
         System.out.println("Hello Server is ready.");    
      }    
      catch (Exception e)    
      {    
         System.out.println("Hello Server failed: " + e);    
      }    
   }    
   
   public static void main(String [] args)    
   {    
	 new ThreadOfHelloServer().run();
         

   } 
}   



