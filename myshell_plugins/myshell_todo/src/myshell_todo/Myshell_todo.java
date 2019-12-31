/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myshell_todo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author nemo
 */
public class Myshell_todo {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String ToDoFile="C:\\myshell\\plugins_data\\todo.txt";
        //String ToDoFile="C:\\test\\todo.txt";
        
        String[] sToDoList = new String[1000];
        int ListCount=0;
        
        try
        {
            FileReader freader = new FileReader(ToDoFile);
            BufferedReader breader = new BufferedReader(freader); 
            String line = breader.readLine();
            while (line != null) 
            {
                ListCount++;
                sToDoList[ListCount]=line;                
                line = breader.readLine();
            }
            breader.close();
            freader.close();            
        }catch (IOException ioe) {}
        
                    
        if (args[0].equals("todo"))
        {            
            if (args[1].equals("add"))
            {
                String res="";
                for (int i=3;i<=args.length;i++)
                {
                    res=res.concat(args[i-1].concat(" "));
                }
                ListCount++;
                sToDoList[ListCount]=res;
                
                try
                {
                    FileWriter fw = new FileWriter(ToDoFile,false);
                    for (int i=1;i<=ListCount;i++)
                    {
                        fw.write(sToDoList[i]+"\n");
                    }
                    fw.close();
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
                    System.out.println("ToDo #"+Integer.toString(i)+": "+sToDoList[i]);
                }
            }
            if (args[1].equals("del"))
            {
                int DelIndex=0;
                DelIndex=Integer.parseInt(args[2]);
                for (int i=DelIndex;i<=ListCount-1;i++)
                {
                    sToDoList[i]=sToDoList[i+1];                    
                } 
                ListCount--;
                
                try
                {
                    FileWriter fw = new FileWriter(ToDoFile,false);
                    for (int i=1;i<=ListCount;i++)
                    {
                        fw.write(sToDoList[i]+"\n");
                    }
                    fw.close();
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
        if (args[0].equals("init"))
        {    
            if (ListCount>0)
            {
                System.out.println("0");
                System.out.println("0");
                System.out.println("255");
                System.out.println("0");
                System.out.println("0");
                System.out.println("0");
                System.out.println("TODO: You have "+Integer.toString(ListCount)+" things to do!");
            }
        }
    }
    
}
