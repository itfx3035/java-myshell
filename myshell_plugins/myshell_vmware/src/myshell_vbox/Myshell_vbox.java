/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myshell_vbox;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author nemo
 */
public class Myshell_vbox {

    public static void main(String[] args) {
        Boolean isValid=false;
        String DataDir="C:\\myshell\\plugins_data\\";
        String ExchDir="C:\\myshell\\exchange\\";
            
        if (args[1].equals("run"))
        {
            isValid=true;
            
            String[] sAliases = new String[100];
            String[] sNames = new String[100];
            int vm_count=0;
            
            try
            {
                BufferedReader breader;
                FileReader freader;
                freader = new FileReader(DataDir+"vms.txt");
                breader = new BufferedReader(freader); 
                String line = breader.readLine();
                int isAlias=1;
                while (line != null) {
                    if (isAlias==1)
                    {
                        vm_count++;
                        sAliases[vm_count-1]=line;                        
                        isAlias=0;
                    }
                    else
                    {
                        sNames[vm_count-1]=line;
                        isAlias=1;
                    }                     
                    line = breader.readLine();            
                }
                breader.close();
                freader.close();
            } catch (IOException ioe) {}
           
            String vmname = "";
            for (int i=1; i<=vm_count; i++)
            {
                if (args[2].equals(sAliases[i-1]))
                {
                    vmname=sNames[i-1];
                }
            }
            
            if (vmname.equals(""))
            {
                System.out.println("255");
                System.out.println("0");
                System.out.println("0");
                System.out.println("0");
                System.out.println("0");
                System.out.println("0");
                System.out.println("VM not found: "+args[2]);
            }
            else
            {
                try
                {                            
                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("YYYY_MM_dd_HH_mm_ss_SSS");
                    String filename= ExchDir+"\\vmware_"+sdf.format(cal.getTime())+".txt";
                    FileWriter fw = new FileWriter(filename,true);
                    fw.write("C:\\Program Files (x86)\\VMware\\VMware VIX\\vmrun.exe start "+vmname);
                    fw.close();
                } 
                catch (IOException ioe)
                {                    
                    System.out.println("255");
                    System.out.println("0");
                    System.out.println("0");
                    System.out.println("0");
                    System.out.println("0");
                    System.out.println("0");
                    System.out.println("Error running VM: "+args[2]);
                }
            }
        }
        
        if (args[1].equals("list"))
        {
            isValid=true;
            try
            {                            
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("YYYY_MM_dd_HH_mm_ss_SSS");
                String filename= ExchDir+"\\vmware_"+sdf.format(cal.getTime())+".txt";
                FileWriter fw = new FileWriter(filename,true);
                fw.write("C:\\Program Files (x86)\\VMware\\VMware VIX\\vmrun.exe list");
                fw.close();                
            } 
            catch (IOException ioe)
            {                    
                System.out.println("255");
                System.out.println("0");
                System.out.println("0");
                System.out.println("0");
                System.out.println("0");
                System.out.println("0");
                System.out.println("Can't get data about running VMs.");
            }
        }
    }    
}
