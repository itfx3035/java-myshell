/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myshell_linkchecker;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

/**
 *
 * @author nemo
 */
public class Myshell_linkchecker {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String MessagesFolder="C:\\myshell\\messages";
        String LinkFile="C:\\myshell\\plugins_data\\linkchecker_links.txt";
        String TriggerFile="C:\\myshell\\plugins_data\\linkchecker_triggers.txt";
        String[] sLinkList = new String[1000];
        String[] sTriggerList = new String[1000];
        int ListCount=0;
        
        try
        {
            FileReader freader1 = new FileReader(LinkFile);
            BufferedReader breader1 = new BufferedReader(freader1); 
            FileReader freader2 = new FileReader(TriggerFile);
            BufferedReader breader2 = new BufferedReader(freader2);
            String line1 = breader1.readLine();
            String line2 = breader2.readLine();
            while (line1 != null) 
            {
                ListCount++;
                sLinkList[ListCount]=line1;                
                line1 = breader1.readLine();
                sTriggerList[ListCount]=line2;                
                line2 = breader2.readLine();
            }
            breader1.close();
            freader1.close(); 
            breader2.close();
            freader2.close();               
        }catch (IOException ioe) {}
        
        
        if (args[0].equals("init") || (args[0].equals("linkcheck") && args[1].equals("check")))
        {
            for (int i=ListCount;i>=1;i--)
            {
                if (check_url(sLinkList[i],sTriggerList[i])==1)
                {
                    try
                    {
                        Calendar cal = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("YYYY_MM_dd_HH_mm_ss_SSS");
                        String filename= MessagesFolder+"\\linkchecker_"+sdf.format(cal.getTime())+".txt";
                        FileWriter fw = new FileWriter(filename,true);
                        fw.write("0\n");
                        fw.write("0\n");
                        fw.write("255\n");
                        fw.write("0\n");
                        fw.write("0\n");
                        fw.write("0\n");
                        fw.write("Linkchecker: trigger ["+sTriggerList[i]+"] found on page ["+sLinkList[i]+"]\n");
                        fw.close();
                    }
                    catch(IOException ioe) 
                    {
                        try
                        {
                            Calendar cal = Calendar.getInstance();                        
                            SimpleDateFormat sdf = new SimpleDateFormat("YYYY_MM_dd_HH_mm_ss_SSS");
                            String filename= MessagesFolder+"\\linkchecker_"+sdf.format(cal.getTime())+".txt";
                            FileWriter fw2 = new FileWriter(filename,true);
                            fw2.write("0\n");
                            fw2.write("0\n");
                            fw2.write("255\n");
                            fw2.write("0\n");
                            fw2.write("0\n");
                            fw2.write("0\n");
                            fw2.write("Linkchecker: URL could not be reached: ["+sLinkList[i]+"]\n");
                            fw2.close();
                        }catch(IOException ioe2) {}
                    }
                }                
            }
        }
        
        if (args[0].equals("linkcheck"))
        {
            if (args[1].equals("add"))
            {
                String res="";
                for (int i=4;i<=args.length;i++)
                {
                    res=res.concat(args[i-1].concat(" "));
                }
                res=res.trim();
                ListCount++;
                sTriggerList[ListCount]=res;
                sLinkList[ListCount]=args[2];

                try
                {
                    FileWriter fw = new FileWriter(LinkFile,false);
                    for (int i=1;i<=ListCount;i++)
                    {
                        fw.write(sLinkList[i]+"\n");
                    }
                    fw.close();

                    FileWriter fw2 = new FileWriter(TriggerFile,false);
                    for (int i=1;i<=ListCount;i++)
                    {
                        fw2.write(sTriggerList[i]+"\n");
                    }
                    fw2.close();
                }catch(IOException ioe){};

                System.out.println("0");
                System.out.println("0");
                System.out.println("255");
                System.out.println("0");
                System.out.println("0");
                System.out.println("0");
                System.out.println("Added.");
            }

            if (args[1].equals("list"))
            {
                for (int i=ListCount;i>=1;i--)
                {
                    System.out.println("0");
                    System.out.println("0");
                    System.out.println("255");
                    System.out.println("0");
                    System.out.println("0");
                    System.out.println("0");
                    System.out.println("ToDo #"+Integer.toString(i)+": ["+sTriggerList[i]+"] from ["+sLinkList[i]+"]");
                }
            }

            if (args[1].equals("del"))
            {
                int DelIndex=0;
                DelIndex=Integer.parseInt(args[2]);
                for (int i=DelIndex;i<=ListCount-1;i++)
                {
                    sLinkList[i]=sLinkList[i+1];                    
                }                 
                DelIndex=0;
                DelIndex=Integer.parseInt(args[2]);
                for (int i=DelIndex;i<=ListCount-1;i++)
                {
                    sTriggerList[i]=sTriggerList[i+1];                    
                } 
                ListCount--;

                try
                {
                    FileWriter fw = new FileWriter(LinkFile,false);
                    for (int i=1;i<=ListCount;i++)
                    {
                        fw.write(sLinkList[i]+"\n");
                    }
                    fw.close();

                    FileWriter fw2 = new FileWriter(TriggerFile,false);
                    for (int i=1;i<=ListCount;i++)
                    {
                        fw2.write(sTriggerList[i]+"\n");
                    }
                    fw2.close();
                }catch(IOException ioe){};

                System.out.println("0");
                System.out.println("0");
                System.out.println("255");
                System.out.println("0");
                System.out.println("0");
                System.out.println("0");
                System.out.println("Removed.");
            }
        }    
    }
    
    
    
    static int check_url(String str_url, String trigger)
    {
        int isExists=0;
        try
        {
            URL pageURL = new URL(str_url);       
            try
            {
                Scanner scanner = new Scanner(pageURL.openStream(), "utf-8");
                try {
                    while (scanner.hasNextLine()){
                        String str=scanner.nextLine();
                        if (str.toLowerCase().contains(trigger.toLowerCase()))
                        {
                            isExists=1;
                        }
                    }
                }
                finally{
                    scanner.close();
                }
            }catch(IOException ioex){}
        }  
        catch(MalformedURLException ex){}
        return isExists;
    }
    
}
