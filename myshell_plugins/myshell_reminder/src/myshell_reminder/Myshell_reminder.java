/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myshell_reminder;

import java.util.Calendar;

/**
 *
 * @author nemo
 */
public class Myshell_reminder {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //System.out.println(args.length);
         
        int Seconds=0;
         
        if (args[0].equals("wait"))
        {             
            Seconds=Integer.parseInt(args[1]);           
        }      
        
        if (args[0].equals("waitfor"))
        {
            // we expecting something like 01:22:30             
            int hh=0;
            int mm=0;
            int ss=0;
                         
            hh=Integer.parseInt(args[1].substring(0, 2));
            mm=Integer.parseInt(args[1].substring(3, 5));  
            ss=Integer.parseInt(args[1].substring(6, 8));  
            
            int curr_hh=0;
            int curr_mm=0;
            int curr_ss=0;
            Calendar cal = Calendar.getInstance();
            curr_hh=cal.get(Calendar.HOUR_OF_DAY);
            curr_mm=cal.get(Calendar.MINUTE);
            curr_ss=cal.get(Calendar.SECOND);
            
            Seconds=(hh*3600+mm*60+ss)-(curr_hh*3600+curr_mm*60+curr_ss);
            if (Seconds<0)
            {
                Seconds=0;
                System.out.println("255");
                System.out.println("0");
                System.out.println("0");
                System.out.println("0");
                System.out.println("0");
                System.out.println("0");
                System.out.println("Invalid time for reminder: "+args[1]);
            }
        }
         
        if (Seconds!=0)
        {
           try
           {
               Thread.sleep(Seconds*1000);
           }
           catch (InterruptedException ie){}

           String res="";
           for (int i=3;i<=args.length;i++)
           {
               res=res.concat(args[i-1].concat(" "));
           }
           System.out.println("0");
           System.out.println("0");
           System.out.println("255");
           System.out.println("1");
           System.out.println("1");
           System.out.println("0");
           System.out.println(res);
        }
    }
    
}

