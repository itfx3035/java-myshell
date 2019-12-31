/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myshell_net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * @author nemo
 */
public class Myshell_net {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args[0].equals("ping"))
        {
            try
            {    
                Process p = Runtime.getRuntime().exec(args[0]+" -c 4 "+args[1]);
                BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line = null;
                while ( (line = br.readLine()) != null ){            
//                    if (line.contains("icmp_seq"))
//                    {
//                        System.out.println("150");
//                        System.out.println("150");
//                        System.out.println("150");
//                        System.out.println("0");
//                        System.out.println("0");
//                        System.out.println("0");
//                        System.out.println(line);
//                    }
                    if (line.contains("transmitted") && line.contains("received"))
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
            } 
            catch (IOException ioe) 
            {
                System.out.println("255");
                System.out.println("0");
                System.out.println("0");
                System.out.println("0");
                System.out.println("0");
                System.out.println("0");
                System.out.println(ioe.getMessage());
            }           
        }
        
        if (args[0].equals("resolve"))
        {
            try
            {    
                InetAddress address = InetAddress.getByName(args[1]); 
                System.out.println("0");
                System.out.println("0");
                System.out.println("255");
                System.out.println("0");
                System.out.println("0");
                System.out.println("0");
                System.out.println(address.getHostAddress()); 
            }
            catch (UnknownHostException uhe) 
            {
                System.out.println("255");
                System.out.println("0");
                System.out.println("0");
                System.out.println("0");
                System.out.println("0");
                System.out.println("0");
                System.out.println("Unknown host: "+args[1]); 
            }          
        }
    }
    
}
