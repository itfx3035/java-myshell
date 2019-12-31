/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myshell_helper;

import com.sun.javafx.scene.control.skin.Utils;
import com.sun.webkit.graphics.WCImage;
import java.awt.Toolkit;
import java.awt.AWTException;

import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.imageio.ImageIO;



/**
 *
 * @author nemo
 */
public class myshell_helper_JFrame extends javax.swing.JFrame {

    int ThreadStarted=0;
    ExecutorService exec;
    int StopExec=0;
    TrayIcon trayIcon;
    BufferedImage ic;
    SystemTray tray;
    
    /**
     * Creates new form myshell_helper_JFrame
     */
    public myshell_helper_JFrame() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setTitle("MyShell Helper process");
        setLocationByPlatform(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jLabel1.setText("Last event: nothing");

        jButton1.setText("OK");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Close");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(41, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap())
        );

        jLabel1.getAccessibleContext().setAccessibleName("jlStatus");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        setVisible(false);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        StopExec=1;
        tray.remove(trayIcon);
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        if (ThreadStarted==0)
        {
            ThreadStarted=1;
            
            tray = SystemTray.getSystemTray();
            URL iconUrl = this.getClass().getResource("/images/icon.png");
            
            try
            {
                ic = ImageIO.read(iconUrl); 
                trayIcon = new TrayIcon(ic);
                try {
                    tray.add(trayIcon);
                } catch (AWTException e) {}                
            } catch (IOException e) {}
            
            
            ActionListener al = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setVisible(true);
                }
            };
            trayIcon.addActionListener(al);
            
            setVisible(false);
            exec = Executors.newSingleThreadExecutor();
            exec.execute(new Runnable() {
                @Override
                public synchronized void run() 
                {
                    VmWareCheckerThread();
                }                   
            });
            exec.shutdownNow();
        }
    }//GEN-LAST:event_formWindowOpened

    public void VmWareCheckerThread(){
        String ExchDir="C:\\myshell\\exchange\\";
        String MessagesFolder="C:\\myshell\\messages\\";

        while (true)
        {
            try
            {
                if (StopExec==1)
                {
                    break;
                }
                Thread.sleep(1000);
            } catch (InterruptedException ioe) {}

            
            BufferedReader breader;
            FileReader freader;
            //File folder = new File("/data0/myshell");
            File folder = new File(ExchDir);
            File[] folderEntries = folder.listFiles();
            for (File entry : folderEntries)
            {
                if ((entry.getName().toLowerCase().endsWith(".txt")) && (entry.getName().toLowerCase().startsWith("vmware_")))
                {
                    try
                    {             
                        freader = new FileReader(entry.getAbsolutePath());
                        breader = new BufferedReader(freader); 
                        String cmdline = breader.readLine();
                        try
                        {                            
                            Process p = Runtime.getRuntime().exec(cmdline);
                            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));

                            Calendar cal = Calendar.getInstance();
                            SimpleDateFormat sdf = new SimpleDateFormat("YYYY_MM_dd_HH_mm_ss_SSS");
                            String filename= MessagesFolder+"\\vmware_"+sdf.format(cal.getTime())+".txt";
                            FileWriter fw = new FileWriter(filename,true);

                            String line = null;
                            while ( (line = br.readLine()) != null ){                                
                                fw.write("0\n");
                                fw.write("0\n");
                                fw.write("255\n");
                                fw.write("0\n");
                                fw.write("0\n");
                                fw.write("0\n");
                                fw.write(line+"\n");                                
                                
                                jLabel1.setText("Last event: "+sdf.format(cal.getTime())+" "+line);
                            }
                            fw.close();
                            br.close();

                            
                        } 
                        catch (IOException ioe)
                        {                    
                            Calendar cal = Calendar.getInstance();
                            SimpleDateFormat sdf = new SimpleDateFormat("YYYY_MM_dd_HH_mm_ss_SSS");
                            String filename= MessagesFolder+"\\vmware_"+sdf.format(cal.getTime())+".txt";
                            FileWriter fw = new FileWriter(filename,true);
                            fw.write("255\n");
                            fw.write("0\n");
                            fw.write("0\n");
                            fw.write("0\n");
                            fw.write("0\n");
                            fw.write("0\n");
                            fw.write("Error running VM\n");    
                            fw.close();
                            
                            jLabel1.setText("Last event: "+sdf.format(cal.getTime())+" Error running VM");
                        }                        
                        breader.close();
                        freader.close();

                        entry.delete();
                    }
                    catch (IOException ioe) {}
                }
            }        
        } 
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(myshell_helper_JFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(myshell_helper_JFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(myshell_helper_JFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(myshell_helper_JFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

         
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new myshell_helper_JFrame().setVisible(true);                          
            }
        });
    }
    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
