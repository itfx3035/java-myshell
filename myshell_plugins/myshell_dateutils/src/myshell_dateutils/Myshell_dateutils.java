/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myshell_dateutils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author nemo
 */
public class Myshell_dateutils {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Boolean isValid=false;
        
        if (args[0].equals("date"))
        {
        
            if (args[1].equals("add"))
            {   
                Calendar cal = Calendar.getInstance();
                DateFormat dformat = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);                    
                try
                {
                    Date date1 = dformat.parse(args[4]);   
                    cal.setTime(date1);
                } 
                catch (ParseException poe) {System.out.println(poe.getMessage());}

                if (args[2].equals("dd"))
                {
                    cal.add(Calendar.DATE, Integer.parseInt(args[3]));
                }
                if (args[2].equals("ww"))
                {
                    cal.add(Calendar.WEEK_OF_YEAR, Integer.parseInt(args[3]));
                }
                if (args[2].equals("mm"))
                {
                    cal.add(Calendar.MONTH, Integer.parseInt(args[3]));
                }
                if (args[2].equals("yyyy"))
                {
                    cal.add(Calendar.YEAR, Integer.parseInt(args[3]));
                }
                SimpleDateFormat sdf = new SimpleDateFormat("YYYY.MM.dd");
                System.out.println("0");
                System.out.println("0");
                System.out.println("255");
                System.out.println("0");
                System.out.println("0");
                System.out.println("0");
                System.out.println(sdf.format(cal.getTime()));
                isValid=true;
            }
            if (args[1].equals("diff"))
            {
                DateTimeFormatter dtformatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                LocalDate date1 = LocalDate.parse(args[3],dtformatter); 
                LocalDate date2 = LocalDate.parse(args[4],dtformatter);  
                long cnt=0;
                if (args[2].equals("dd"))
                {
                    cnt = ChronoUnit.DAYS.between(date2, date1);
                }
                if (args[2].equals("ww"))
                {
                    cnt = ChronoUnit.WEEKS.between(date2, date1);
                }
                if (args[2].equals("mm"))
                {
                    cnt = ChronoUnit.MONTHS.between(date2, date1);
                }
                if (args[2].equals("yyyy"))
                {
                    cnt = ChronoUnit.YEARS.between(date2, date1);
                }
                System.out.println("0");
                System.out.println("0");
                System.out.println("255");
                System.out.println("0");
                System.out.println("0");
                System.out.println("0");
                System.out.println(Long.toString(cnt));
                isValid=true;
            }
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
