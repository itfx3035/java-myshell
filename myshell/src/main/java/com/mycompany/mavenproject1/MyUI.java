package com.mycompany.mavenproject1;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.UIEvents;
import com.vaadin.server.FileResource;
import com.vaadin.server.Resource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Audio;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import java.util.concurrent.Executors;



@Theme("mytheme")
public class MyUI extends UI {
    
    String PluginsFolder="C:\\myshell\\plugins";
    String MessagesFolder="C:\\myshell\\messages";
    String SettingsFolder="C:\\myshell\\settings";
    String EventSoundFile="C:\\myshell\\media\\myshell_notify.wav";
    String RootFolder="C:\\myshell";
    String MyShellVersion="MyShell v0.6w";

    
    String[] cmdHistory = new String[1000];
    int CurrHistElementID=0;
    int SelectedHistElementID=0;
    String[] sMessages = new String[1000];
    Integer[] sMessagesColorR = new Integer[1000];
    Integer[] sMessagesColorG = new Integer[1000];
    Integer[] sMessagesColorB = new Integer[1000];
    Integer[] sMessagesShowWindow = new Integer[1000];
    Integer[] sMessagesPlaySound = new Integer[1000];
    String[] sMessagesChangeCaption = new String[1000];
    int CurrMessagesID=0;
    
    String[] cfgPluginCommands = new String[1000];
    String[] cfgPluginSettings = new String[1000];
    String[] cfgPluginFiles = new String[1000];
    int PluginCount=0;
    
    String[] cfgCronMessages = new String[1000];
    String[] cfgCronHH = new String[1000];
    String[] cfgCronMM = new String[1000];
    String[] cfgCronSS = new String[1000];
    int CronRecordsCount=0;
    
    String sLogin;
    String sPassword;
    int ExitApp=0;
    long LastPollDateMillis = 0L;
    
    private void SaveCronTab()
    {
        try
        {    
            String filename= SettingsFolder+"\\crontab.txt";
            File fl= new File(filename);
            fl.delete();
            
            FileWriter fw = new FileWriter(filename,true);
            for (int i=1; i<=CronRecordsCount; i++)
            {
                fw.write(cfgCronHH[i]+" "+cfgCronMM[i]+" "+cfgCronSS[i]+" "+cfgCronMessages[i]+"\n");
            }    
            fw.close();
        } catch (IOException ioe){};
    }    
    
    private void LoadCronTab()
    {
        CronRecordsCount=0;
        try
        {                
            BufferedReader breader;
            FileReader freader;
            String filename= SettingsFolder+"\\crontab.txt";
        
            freader = new FileReader(filename);
            breader = new BufferedReader(freader); 
            String line = breader.readLine();
            while (line != null) {
                CronRecordsCount++;
                cfgCronHH[CronRecordsCount]=ParseParam(line," ",1);
                cfgCronMM[CronRecordsCount]=ParseParam(line," ",2);
                cfgCronSS[CronRecordsCount]=ParseParam(line," ",3);
                cfgCronMessages[CronRecordsCount]=line.substring(GetParamAddr(line, " ", 4)-1);
                line = breader.readLine();
            }
            breader.close();
            freader.close();
        } catch (IOException ioe){};
    }
    
    private String ParseParam(String input_str, String delimit, int ind)
    {
        int ln;
        int curr_section=1;
        String ret="";
        ln=input_str.length();
        for (int i=1; i<=ln; i++)
        {
            if (input_str.substring(i-1, i).equals(delimit))
            {
                curr_section++;
                continue;
            }
            if (curr_section==ind)
            {
                ret=ret+input_str.substring(i-1, i);
            }
        }
        return ret;
    };
    
    private int GetParamAddr(String input_str, String delimit, int ind)
    {
        int ln;
        int curr_section=1;
        ln=input_str.length();
        for (int i=1; i<=ln; i++)
        {
            if (input_str.substring(i-1, i).equals(delimit))
            {
                curr_section++;
                continue;
            }
            if (curr_section==ind)
            {
                return i;
            }
        }
        return 0;
    };
    
    private void GetPlugins()
    {
        BufferedReader breader;
        FileReader freader;
        File folder = new File(PluginsFolder);
        File[] folderEntries = folder.listFiles();
        for (File entry : folderEntries)
        {
            if (entry.getName().toLowerCase().endsWith(".txt"))
            {
                try
                {              
                    PluginCount++;
                    freader = new FileReader(entry.getAbsolutePath());
                    breader = new BufferedReader(freader); 
                    String line = breader.readLine();
                    int pos=0;
                    while (line != null) {
                        pos++;
                        if (pos==1) {cfgPluginCommands[PluginCount]=line;}
                        if (pos==2) {cfgPluginSettings[PluginCount]=line;}
                        if (pos==3) {cfgPluginFiles[PluginCount]=line;} 
                        line = breader.readLine();
                    }
                    breader.close();
                    freader.close();
                }
                catch (IOException ioe) {}
            }
        }
    };
    
    private void InitPlugins(VerticalLayout layoutOutput, VerticalLayout layoutOutputSupp, Audio aEvent)
    {

// ===== not needed anymore, but may be useful for further implementations ===================
//        Executors.newSingleThreadExecutor().execute(new Runnable() {
//        @Override
//        public synchronized void run() {                
//            try
//            {    
//                while (ExitApp==0)
//                {   
//                    FileWriter fw = new FileWriter(HeartBeatFolder+"/heartbeat",false);
//                    fw.write("!");
//                    fw.close();
//                    try
//                    {
//                        Thread.sleep(10000);
//                    } catch (InterruptedException intex){}
//                }
//            } catch (IOException ioe) {}
//        }});
// ==============================================================================
        
        
        for (int i=1; i<=PluginCount; i++)
        {
            if (cfgPluginSettings[i].contains("initonstart"))
            {
                RunCommand(layoutOutput, layoutOutputSupp, cfgPluginFiles[i], cfgPluginSettings[i], "init", aEvent);
            }
        }        
    } 
    
    private void RunWatchDog()
    {       
        Executors.newSingleThreadExecutor().execute(new Runnable() {
        @Override
        public synchronized void run() {  
            while (ExitApp==0)
            {   
                try
                {
                    Calendar cln = Calendar.getInstance();
                    long ll=cln.getTimeInMillis();
                    if ((LastPollDateMillis!=0L) && (ll-LastPollDateMillis>=30000L))
                    {
                        ExitApp=1;                        
                        
                    }  
                    else
                    {
                        Thread.sleep(5000);
                    }
                } catch (InterruptedException intex){}                
            }
            getSession().close();
        }});      
    } 
    
    private void ReadMessages()
    {
        BufferedReader breader;
        FileReader freader;
        File folder = new File(MessagesFolder);
        File[] folderEntries = folder.listFiles();
        for (File entry : folderEntries)
        {
            if (entry.getName().toLowerCase().endsWith(".txt"))
            {
                try
                {              
                    
                    freader = new FileReader(entry.getAbsolutePath());
                    breader = new BufferedReader(freader); 
                    String line = breader.readLine();
                    int pos=0;
                    while (line != null) {
                        pos++;
                        if (pos==1) 
                        {
                            CurrMessagesID++;
                            sMessagesColorR[CurrMessagesID]=Integer.parseInt(line);
                        }
                        if (pos==2) {sMessagesColorG[CurrMessagesID]=Integer.parseInt(line);}
                        if (pos==3) {sMessagesColorB[CurrMessagesID]=Integer.parseInt(line);} 
                        if (pos==4) {sMessagesShowWindow[CurrMessagesID]=Integer.parseInt(line);}
                        if (pos==5) {sMessagesPlaySound[CurrMessagesID]=Integer.parseInt(line);}
                        if (pos==6) 
                        {
                            if (line.startsWith("1"))
                            {
                                sMessagesChangeCaption[CurrMessagesID]=line.substring(1, line.length());
                            }
                            else
                            {
                                sMessagesChangeCaption[CurrMessagesID]="";
                            }
                        };                        
                        if (pos==7) {
                            sMessages[CurrMessagesID]=line;
                            pos=0;                        
                        };                        
                        
                        line = breader.readLine();
                    }
                    breader.close();
                    freader.close();
                    
                    entry.delete();
                }
                catch (IOException ioe) {}
            }
        }
    };
    
    
    private void WriteLabel(VerticalLayout HO, String ms, Integer color_r, Integer color_g, Integer color_b)
    {    
        Label l = new Label("<span style=\"color:rgb("+Integer.toString(color_r)+","+Integer.toString(color_g)+","+Integer.toString(color_b)+")\">"+ms+"</span>",ContentMode.HTML);
        l.addStyleName("mono");
        HO.addComponentAsFirst(l);        
    };
    
    
    private void RunCommand(VerticalLayout layoutOutput, VerticalLayout layoutOutputSupp, String PluginFile, String PluginSettings, String cmd, Audio aEvent)
    {
        if ((PluginSettings.contains("forkoninit") && cmd.equals("init")) ||
            (PluginSettings.contains("forkonrun") && cmd.equals("init")==false))
        {
           WriteLabel(layoutOutputSupp, "Fork process: "+PluginFile+" "+cmd, 150, 150, 150);
           Executors.newSingleThreadExecutor().execute(new Runnable() {
           @Override
           public synchronized void run() {                
                try
                {    
                    Process p = Runtime.getRuntime().exec("java -jar "+PluginsFolder+"\\"+PluginFile+" "+cmd);
                    BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    String line = null;
                    Integer cnt=0;
                    while ( (line = br.readLine()) != null ){            
                        cnt++;
                        if (cnt==1)
                        {
                            CurrMessagesID++;
                            sMessagesColorR[CurrMessagesID]=Integer.parseInt(line);
                        }
                        if (cnt==2)
                        {
                            sMessagesColorG[CurrMessagesID]=Integer.parseInt(line);
                        }
                        if (cnt==3)
                        {
                            sMessagesColorB[CurrMessagesID]=Integer.parseInt(line);
                        }
                        if (cnt==4)
                        {
                            sMessagesShowWindow[CurrMessagesID]=Integer.parseInt(line);
                        }
                        if (cnt==5)
                        {
                            sMessagesPlaySound[CurrMessagesID]=Integer.parseInt(line);
                        }
                        if (cnt==6)
                        {
                            if (line.startsWith("1"))
                            {
                                sMessagesChangeCaption[CurrMessagesID]=line.substring(1, line.length());
                            }
                            else
                            {
                                sMessagesChangeCaption[CurrMessagesID]="";
                            }
                        }
                        if (cnt==7)
                        {
                            sMessages[CurrMessagesID]=line;
                            cnt=0;
                        }
                    }
                    br.close();
                } 
                catch (IOException ioe) 
                {
                    CurrMessagesID++;
                    sMessages[CurrMessagesID]=ioe.getMessage();                                    
                }
                WriteLabel(layoutOutputSupp, "Finish fork process: "+PluginFile+" "+cmd, 150, 150, 150);
           }});
           
           
       }
       else
       {
           try
           {
               Process p = Runtime.getRuntime().exec("java -jar "+PluginsFolder+"\\"+PluginFile+" "+cmd);
               BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
               String line = null;
               Integer cnt=0;
               Integer ColorR=0;
               Integer ColorG=0;
               Integer ColorB=0;
               Integer ShowWindow=0;
               Integer PlaySound=0;
               String ChangeCaption = "";
               while ( (line = br.readLine()) != null ){            
                   //WriteLabel(layoutOutput, line, 0,0,255);
                    cnt++;
                    if (cnt==1)
                    {
                        ColorR=Integer.parseInt(line);
                    }
                    if (cnt==2)
                    {
                        ColorG=Integer.parseInt(line);
                    }
                    if (cnt==3)
                    {
                        ColorB=Integer.parseInt(line);
                    }
                    if (cnt==4)
                    {
                        ShowWindow=Integer.parseInt(line);
                    }
                    if (cnt==5)
                    {
                        PlaySound=Integer.parseInt(line);
                    }
                    if (cnt==6)
                    {
                        if (line.startsWith("1"))
                        {
                            ChangeCaption=line.substring(1, line.length());
                        }
                        else
                        {
                            ChangeCaption="";
                        }
                    }
                    if (cnt==7)
                    {
                        ProcessMessage( layoutOutput, 
                                        aEvent, 
                                        line, 
                                        ColorR, 
                                        ColorG, 
                                        ColorB, 
                                        ShowWindow, 
                                        PlaySound, 
                                        ChangeCaption);
                        cnt=0;
                    }
               }
               br.close();
           }catch (IOException ioe) 
           {
               WriteLabel(layoutOutput, ioe.getMessage(), 255,0,0);
           }
       }
    }
    
    private void ProcessMessage(VerticalLayout HO, Audio aEvent, String ms, Integer color_r, Integer color_g, Integer color_b, Integer ShowWindow, Integer PlaySound, String ChangeCaption)
    {    
        WriteLabel(HO, ms, color_r, color_g, color_b);
        if (!ChangeCaption.equals(""))
        {
            getPage().setTitle(ChangeCaption);
        }
        if (PlaySound==1)
        {
            aEvent.play();
        } 
        if (ShowWindow==1)
        {
            JavaScript.getCurrent().execute("var AlarmWindow; "
                                          + "AlarmWindow = window.open(\"\", \"_blank\", \"left=200, top=200, width=400, height=200 \");\n "
                                          + "AlarmWindow.document.title = \""+ms+"\"; "
                                          + "var t = AlarmWindow.document.createTextNode(\""+ms+"\"); "
                                          + "AlarmWindow.document.body.appendChild(t); "
                                          + "AlarmWindow.blur(); "
                                          + "AlarmWindow.focus();");
             
        }
    };
    
    
    private void ProcessMessages(VerticalLayout HO, VerticalLayout HOSupp, Audio aEvent)
    {
        while (CurrMessagesID>0)
        {
            if (sMessagesColorR[1]==150 && sMessagesColorG[1]==150 && sMessagesColorB[1]==150)
            {
                ProcessMessage(HOSupp,
                           aEvent, 
                           sMessages[1], 
                           sMessagesColorR[1], 
                           sMessagesColorG[1], 
                           sMessagesColorB[1], 
                           sMessagesShowWindow[1], 
                           sMessagesPlaySound[1], 
                           sMessagesChangeCaption[1]);
            }
            else
            {
                ProcessMessage(HO,
                           aEvent, 
                           sMessages[1], 
                           sMessagesColorR[1], 
                           sMessagesColorG[1], 
                           sMessagesColorB[1], 
                           sMessagesShowWindow[1], 
                           sMessagesPlaySound[1], 
                           sMessagesChangeCaption[1]);
            }
            for (int x=1; x<CurrMessagesID; x++)
            {
                sMessages[x]=sMessages[x+1];
                sMessagesColorR[x]=sMessagesColorR[x+1];
                sMessagesColorG[x]=sMessagesColorG[x+1];
                sMessagesColorB[x]=sMessagesColorB[x+1];
                sMessagesShowWindow[x]=sMessagesShowWindow[x+1];
                sMessagesPlaySound[x]=sMessagesPlaySound[x+1];
                sMessagesChangeCaption[x]=sMessagesChangeCaption[x+1];
            }
            CurrMessagesID--;                    
        }  
    };
    
    private void ProcessCommand(VerticalLayout layoutOutput, VerticalLayout layoutOutputSupp, TextField tfMain, Audio aEvent)
    {
        String cmd;
        boolean isProcessed;

        cmd=tfMain.getValue().trim().toLowerCase();
        isProcessed=false;
        tfMain.setValue("");


        if (cmd.equals(""))
        {
            return;
        }                
        if (isProcessed==false)
        {    
            WriteLabel(layoutOutput, ">> "+cmd, 0, 0, 0);
        }

        // commands -----------------------------------------------
        if ((isProcessed==false) && (cmd.equals("about")))
        {
            WriteLabel(layoutOutput, MyShellVersion, 0, 0, 255);
            isProcessed=true;
        }
        
        if ((isProcessed==false) && (cmd.equals("cls")))
        {
            layoutOutput.removeAllComponents();
            layoutOutputSupp.removeAllComponents();
            isProcessed=true;
        }

        if ((isProcessed==false) && (cmd.equals("plugins")))
        {
            isProcessed=true;
            String list="";
            for (int i=1; i<=PluginCount; i++)
            {
                list=list+cfgPluginCommands[i]+"; ";            
            }
             WriteLabel(layoutOutput, list, 0, 0, 255);            
        }


        if ((isProcessed==false) && (cmd.startsWith("eval ")))
        {                    
            ScriptEngineManager mgr = new ScriptEngineManager();
            ScriptEngine engine = mgr.getEngineByName("JavaScript");
            String exprs = cmd.substring(5);
            try
            {
                WriteLabel(layoutOutput, engine.eval(exprs).toString(), 0, 0, 255);
            }
            catch (ScriptException se)
            {
                WriteLabel(layoutOutput, se.getMessage(), 255, 0, 0);
            }
            isProcessed=true;
        } 
        
        if ((isProcessed==false) && (cmd.startsWith("cron ")))
        {
            
            if (cmd.startsWith("cron list"))
            {   
                isProcessed=true;
                for (int i=CronRecordsCount; i>=1; i--)
                {
                    WriteLabel(layoutOutput, Integer.toString(i)+") "+cfgCronHH[i]+":"+cfgCronMM[i]+":"+cfgCronSS[i]+" - "+cfgCronMessages[i], 0, 0, 255);
                }
            }
            if (cmd.startsWith("cron add"))
            {
                isProcessed=true;
                String hh_param="";
                String mm_param="";
                String ss_param="";
                String msg_param="";
                hh_param=ParseParam(cmd, " ", 3);
                mm_param=ParseParam(cmd, " ", 4);
                ss_param=ParseParam(cmd, " ", 5);                
                msg_param=cmd.substring(GetParamAddr(cmd, " ", 6)-1);
                
                CronRecordsCount++;
                cfgCronMessages[CronRecordsCount]=msg_param;
                cfgCronHH[CronRecordsCount]=hh_param;
                cfgCronMM[CronRecordsCount]=mm_param;
                cfgCronSS[CronRecordsCount]=ss_param;
                SaveCronTab();
                WriteLabel(layoutOutput, "Added", 0, 0, 255);
            }
            if (cmd.startsWith("cron del"))
            {
                isProcessed=true;
                int ind=0;
                ind=Integer.parseInt(ParseParam(cmd, " ", 3));                
                if (ind<CronRecordsCount)
                {
                    for (int i=ind; i<CronRecordsCount; i++)
                    {
                        cfgCronMessages[i]=cfgCronMessages[i+1];
                        cfgCronHH[i]=cfgCronHH[i+1];
                        cfgCronMM[i]=cfgCronMM[i+1];
                        cfgCronSS[i]=cfgCronSS[i+1];
                    }                
                }
                CronRecordsCount--;
                SaveCronTab();
                WriteLabel(layoutOutput, "Deleted", 0, 0, 255);
                
            }
            
        }

// ------------------------------------------  

        if (isProcessed==false)
        {    
            String CurrPluginFile="";
            String CurrPluginSettings="";
            for (int pl_cmd_id=1; pl_cmd_id<=PluginCount; pl_cmd_id++)
            {
                if (cmd.startsWith(cfgPluginCommands[pl_cmd_id]))
                {
                    CurrPluginFile=cfgPluginFiles[pl_cmd_id];
                    CurrPluginSettings=cfgPluginSettings[pl_cmd_id];
                }
            }
            if (CurrPluginFile.equals("")==false)
            {
                isProcessed=true;
                RunCommand(layoutOutput, layoutOutputSupp, CurrPluginFile, CurrPluginSettings, cmd, aEvent);
            }
        }



        //-------------------------------------------------

        if ((isProcessed==false) && (cmd.startsWith("--")))
        {
            isProcessed=true;    
            try
            {
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("YYYY_MM_dd");
                String filename= RootFolder+"/note_"+sdf.format(cal.getTime())+".txt";
                FileWriter fw = new FileWriter(filename,true);
                fw.write(cmd.substring(2,cmd.length())+"\n");
                fw.close();
            }
            catch(IOException ioe)
            {
                WriteLabel(layoutOutput, ioe.getMessage(), 255,0,0);
            }
        }

        if (isProcessed==false)
        {  
            WriteLabel(layoutOutput, "Invalid command: "+cmd, 255,0,0);                                        
        }

        CurrHistElementID++;
        cmdHistory[CurrHistElementID]=cmd;         
        SelectedHistElementID=CurrHistElementID+1;    
    };
            
    private void StartCron()
    {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
        @Override
        public synchronized void run() {                
            int cHH;
            int cMM;
            int cSS; 
            int cTS;
            int currHH;
            int currMM;
            int currSS;
            int currTS;
            
            while (ExitApp==0)
            {
                try
                {
                    Thread.sleep(5000);
                    
                    Calendar cln = Calendar.getInstance();
                    currHH=cln.get(Calendar.HOUR_OF_DAY);
                    currMM=cln.get(Calendar.MINUTE);
                    currSS=cln.get(Calendar.SECOND);
                    currTS=currHH*3600+currMM*60+currSS;
                    for (int i=1;i<=CronRecordsCount;i++)
                    {
                        cHH=Integer.parseInt(cfgCronHH[i]);
                        cMM=Integer.parseInt(cfgCronMM[i]);
                        cSS=Integer.parseInt(cfgCronSS[i]);
                        cTS=cHH*3600+cMM*60+cSS;
                        
                        if (currTS >= cTS+5)
                        {
                            // overdue
                        }
                        else
                        {
                            if (currTS >= cTS)
                            {
                                // bingo!
                                CurrMessagesID++;
                                sMessages[CurrMessagesID]=cfgCronMessages[i];
                                sMessagesChangeCaption[CurrMessagesID]="";
                                sMessagesColorR[CurrMessagesID]=0;
                                sMessagesColorG[CurrMessagesID]=0;
                                sMessagesColorB[CurrMessagesID]=255;
                                sMessagesPlaySound[CurrMessagesID]=1;
                                sMessagesShowWindow[CurrMessagesID]=1;
                            }
                        }                               
                    }  
                    
                }
                catch (InterruptedException intex){};            
            }
        }});
    }
    
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        
        setPollInterval(5000);        
        //DeploymentConfiguration conf =
        //getSession().getConfiguration();
        // Heartbeat interval in seconds
        //int heartbeatInterval = conf.getHeartbeatInterval();
                
        try
        {                
            BufferedReader breader;
            FileReader freader;
            String filename= SettingsFolder+"\\password.txt";
        
            freader = new FileReader(filename);
            breader = new BufferedReader(freader); 
            sLogin = breader.readLine();
            sPassword = breader.readLine();            
            breader.close();
            freader.close();
        } catch (IOException ioe){};
        
        
        final VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(false);
        
        
        VerticalLayout lTab1 = new VerticalLayout();
        lTab1.setSpacing(false);
        VerticalLayout lTab2 = new VerticalLayout();
        lTab2.setSpacing(false);
        
        TabSheet tsMain = new TabSheet();
        tsMain.setVisible(false);
        tsMain.addTab(lTab1,"Main");
        tsMain.addTab(lTab2,"Supplemental");
        
        // login page
        final HorizontalLayout layoutLogin = new HorizontalLayout();
        final TextField tfLogin = new TextField();
        final PasswordField pfPwd = new PasswordField();
        layoutLogin.addComponents(tfLogin, pfPwd);
        layout.addComponent(layoutLogin);  
        tfLogin.focus();
        
        // work page - main
        final HorizontalLayout layoutHeader = new HorizontalLayout();
        final VerticalLayout layoutOutput = new VerticalLayout();
        final VerticalLayout layoutOutputSupp = new VerticalLayout();
        layoutHeader.setVisible(false);

        layoutOutput.setSpacing(false);
        layoutOutputSupp.setSpacing(false);
        layoutHeader.setSpacing(false);
        layoutOutput.setMargin(false);
        layoutOutputSupp.setMargin(false);
        layoutHeader.setMargin(false);
        layout.addComponent(layoutHeader);
        lTab1.addComponent(layoutOutput);
        lTab2.addComponent(layoutOutputSupp);
        
        TextField tfMain = new TextField();
        tfMain.addStyleName("mono");
        layoutHeader.addComponentsAndExpand(tfMain);
        
        
        // audio component
        Audio aEvent = new Audio();
        final Resource audioResource = new FileResource(
                new File(EventSoundFile));
        aEvent.setSource(audioResource);
        aEvent.setHtmlContentAllowed(true);
        aEvent.setAltText("Can't play audio");
        aEvent.setShowControls(false); 
        aEvent.setSizeUndefined();

        // login page - logic        
        Button bSubmit = new Button("Submit");
        bSubmit.addClickListener( e -> {
            if ((tfLogin.getValue().equals(sLogin)) && (pfPwd.getValue().equals(sPassword)))
            {   
                layoutLogin.setVisible(false);
                tsMain.setVisible(true);
                layoutHeader.setVisible(true);
                bSubmit.removeClickShortcut();
                tfMain.focus();
                GetPlugins();
                WriteLabel(layoutOutput, MyShellVersion, 0, 0, 255);
                WriteLabel(layoutOutputSupp, "Plugins loaded: "+Integer.toString(PluginCount), 150, 150, 150);
                getPage().setTitle("MyShell");
                InitPlugins(layoutOutput, layoutOutputSupp, aEvent);   
                LoadCronTab();
                StartCron();
                RunWatchDog();
            }
            else
            {
                Notification.show("Invalid login or password!");
            }            
        });
        layoutLogin.addComponent(bSubmit);
        bSubmit.setClickShortcut(ShortcutAction.KeyCode.ENTER);        
        
        layout.addComponent(aEvent);
        layout.setExpandRatio(aEvent, 0);
        
        // execute button logic
        tfMain.addShortcutListener(new ShortcutListener("UP_Shortcut", ShortcutAction.KeyCode.ARROW_UP, null) {
            @Override
            public void handleAction(Object sender, Object target) {                    
                    if (CurrHistElementID>0)
                    {
                        SelectedHistElementID--;
                        if (SelectedHistElementID<1) {SelectedHistElementID=CurrHistElementID;};
                        tfMain.setValue(cmdHistory[SelectedHistElementID]);
                    }
                    
                }
            });
        tfMain.addShortcutListener(new ShortcutListener("DOWN_Shortcut", ShortcutAction.KeyCode.ARROW_DOWN, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                    if (CurrHistElementID>0)
                    {
                        SelectedHistElementID++;
                        if (SelectedHistElementID>CurrHistElementID) {SelectedHistElementID=1;};
                        tfMain.setValue(cmdHistory[SelectedHistElementID]);
                    }                    
                }
            });
        
                
        // heartbeat logic        
        addPollListener(new UIEvents.PollListener(){
            @Override
            public void poll(UIEvents.PollEvent event) {
                if (layoutOutput.isVisible())
                {
                    ReadMessages();
                    ProcessMessages(layoutOutput, layoutOutputSupp, aEvent);            
                    
                    Calendar cln = Calendar.getInstance();
                    LastPollDateMillis=cln.getTimeInMillis();                       
                };
            }
        } );
        
        addDetachListener(new DetachListener() {
            @Override
            public void detach(DetachEvent event) {
                ExitApp=1;                
            }
        });
        
        // work - final adjustments        
        Label lSpacer = new Label("");
        lSpacer.setWidth( "8px" );
        lSpacer.setHeight ( null );
        layoutHeader.addComponent (lSpacer);
        
        Button bExecute = new Button("Execute");
        bExecute.addClickListener( e -> {
            ProcessCommand(layoutOutput, layoutOutputSupp, tfMain, aEvent); 
        });
        bExecute.setClickShortcut(ShortcutAction.KeyCode.ENTER); 
        layoutHeader.addComponent(bExecute);
        
        layout.addComponent(tsMain);
        setContent(layout);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}



