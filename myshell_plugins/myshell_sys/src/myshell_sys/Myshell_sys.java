/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myshell_sys;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author nemo
 */
public class Myshell_sys {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Boolean isValid=false;
        
        if (args[0].equals("sys"))
        {
            
            if (args[1].equals("ram"))
            {
                isValid=true;
                                
                try
                {    
                    Process p = Runtime.getRuntime().exec("systeminfo");
                    BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    String line = null;
                    while ( (line = br.readLine()) != null ){                                    
                        if (line.contains("Memory"))                        
                        {
                            System.out.println("0");
                            System.out.println("0");
                            System.out.println("255");
                            System.out.println("0");
                            System.out.println("0");
                            System.out.println("0");
                            System.out.println(line);
                        }
                    }
                    br.close();                    
                } catch (IOException ioe){}
            }
            
            if (args[1].equals("uptime"))
            {
                isValid=true;
                             
                try
                {    
                    Process p = Runtime.getRuntime().exec("systeminfo");
                    BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    String line = null;
                    while ( (line = br.readLine()) != null ){                                    
                        if (line.contains("Boot Time"))                        
                        {
                            System.out.println("0");
                            System.out.println("0");
                            System.out.println("255");
                            System.out.println("0");
                            System.out.println("0");
                            System.out.println("0");
                            System.out.println(line);
                        }
                    }
                    br.close();                    
                } catch (IOException ioe){}
            }
            
            
            
            
//            if (args[1].equals("ram"))
//            {
//                isValid=true;
//                BufferedReader breader;
//                FileReader freader;
//                String str;
//                int cnt=0;
//                
//                try
//                {    
//                    freader = new FileReader("/proc/meminfo");
//                    breader = new BufferedReader(freader);
//                    while ((str = breader.readLine()) != null) {
//                        cnt++;
//                        if (cnt>6) {break;}
//                        if (cnt==1 || cnt==2 || cnt==3 || cnt==6)
//                        {
//                            System.out.println("0");
//                            System.out.println("0");
//                            System.out.println("255");
//                            System.out.println("0");
//                            System.out.println("0");
//                            System.out.println("0");
//                            System.out.println(str);
//                        }
//                    }		                    
//                    breader.close();
//                    freader.close();                    
//                } catch (IOException ioe){}
//            }            
            
            
            
//            if (args[1].equals("uptime"))
//            {
//                isValid=true;
//                BufferedReader breader;
//                FileReader freader;
//                String str;
//                int cnt=0;
//                
//                try
//                {    
//                    Process p = Runtime.getRuntime().exec("uptime");
//                    BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
//                    String line = null;
//                    String resln = "";
//                    while ( (line = br.readLine()) != null ){                                    
//                        System.out.println("0");
//                        System.out.println("0");
//                        System.out.println("255");
//                        System.out.println("0");
//                        System.out.println("0");
//                        System.out.println("0");
//                        System.out.println(line);
//                    }
//                    br.close();                    
//                } catch (IOException ioe){}
//            }
//            if (args[1].equals("temp"))
//            {
//                isValid=true;
//                               
//                try
//                {    
//                    Process p = Runtime.getRuntime().exec("sensors");
//                    BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
//                    String line = null;
//                    String resln = "Cores: ";
//                    while ( (line = br.readLine()) != null ){            
//                        if (line.contains("Core "))
//                        {
//                            resln=resln+line.substring(16, 22)+"; ";
//                        }                        
//                    }
//                    System.out.println("0");
//                    System.out.println("0");
//                    System.out.println("255");
//                    System.out.println("0");
//                    System.out.println("0");
//                    System.out.println("0");
//                    System.out.println(resln);
//                    br.close();                    
//                } catch (IOException ioe){}
//            }
//            
//            
//            if (args[1].equals("power"))
//            {
//                isValid=true;
//                         
//                try
//                {    
//                    Process p = Runtime.getRuntime().exec("upsc eaton");
//                    BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
//                    String line = null;
//                    while ( (line = br.readLine()) != null ){                                    
//                        if ((line.contains("battery.charge")) |
//                           (line.contains("input.voltage:")) |
//                           (line.contains("output.voltage:")) |
//                           (line.contains("ups.load")) |
//                           (line.contains("ups.status"))) //OL CHRG  OL DISCHRG
//                        {
//                            System.out.println("0");
//                            System.out.println("0");
//                            System.out.println("255");
//                            System.out.println("0");
//                            System.out.println("0");
//                            System.out.println("0");
//                            System.out.println(line);
//                        }
//                    }
//                    br.close();                    
//                } catch (IOException ioe){}
//            }
        }
        
        if (isValid==false)
        {
            System.out.println("255");
            System.out.println("0");
            System.out.println("0");
            System.out.println("0");
            System.out.println("0");
            System.out.println("0");
            System.out.println("Invalid command!");
        }
    }
    
}
