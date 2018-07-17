/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import javax.swing.JPanel;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Harry Thorpe
 */
public class TuingUI extends javax.swing.JFrame {

    static ArrayList<String> CommandHistoryList = new ArrayList<String>();
    static int listcount = 0;
    static String CommandHistory = "";
    
    ChartObject charter;
    Serial serial;
    
    /**
     * Creates new form TuingUI
     */
    public TuingUI() {
        initComponents();
        
        lb_console.setText("");
        
        //create chart
        charter = new ChartObject(chartPnlArea);
        
        //create serial connection
        serial = new Serial(ta_reviced,charter);
        Comms_Label.setText(serial.getCommsPort());
    }
    
          
    public void submit(){
        CommandHistory += "["+ CommandHistoryList.size() + "]" + tx_command.getText() +"\n";
        CommandHistoryList.add(tx_command.getText());
        listcount = CommandHistoryList.size();
        ta_history.setText(CommandHistory);
        lb_console.setText(tx_command.getText());
        
        serial.sendData(tx_command.getText());
        
        tx_command.setText("");
        //charter.addDataPoint(new Random().nextInt(50));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu3 = new javax.swing.JMenu();
        Connect_Button = new javax.swing.JButton();
        tx_command = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        ta_history = new javax.swing.JTextArea();
        Send_Button = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        Disconnect_Button = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        kpSlider = new javax.swing.JSlider();
        kdSlider = new javax.swing.JSlider();
        kiSlider = new javax.swing.JSlider();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        currentP = new javax.swing.JLabel();
        currentI = new javax.swing.JLabel();
        currentD = new javax.swing.JLabel();
        lb_console = new javax.swing.JLabel();
        chartPnlArea = new javax.swing.JPanel();
        Comms_Titel_Label = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        ta_reviced = new javax.swing.JTextArea();
        Command_Titel_Label = new javax.swing.JLabel();
        Recived_Titel_Label = new javax.swing.JLabel();
        Comms_Label = new javax.swing.JLabel();

        jMenu3.setText("jMenu3");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        Connect_Button.setText("Connect");
        Connect_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Connect_ButtonActionPerformed(evt);
            }
        });

        tx_command.setToolTipText("Enter Command");
        tx_command.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tx_commandKeyPressed(evt);
            }
        });

        ta_history.setEditable(false);
        ta_history.setColumns(20);
        ta_history.setFont(new java.awt.Font("Courier New", 0, 10)); // NOI18N
        ta_history.setRows(5);
        ta_history.setEnabled(false);
        ta_history.setFocusable(false);
        jScrollPane1.setViewportView(ta_history);

        Send_Button.setText("Send !");
        Send_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Send_ButtonActionPerformed(evt);
            }
        });

        jButton3.setText("Step Inpulse");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        Disconnect_Button.setText("Disconnect");
        Disconnect_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Disconnect_ButtonActionPerformed(evt);
            }
        });

        jButton5.setText("Send Tunings");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        kpSlider.setMajorTickSpacing(1);
        kpSlider.setMinorTickSpacing(1);
        kpSlider.setValue(0);
        kpSlider.setCursor(new java.awt.Cursor(java.awt.Cursor.W_RESIZE_CURSOR));
        kpSlider.setDoubleBuffered(true);
        kpSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                kpSliderStateChanged(evt);
            }
        });

        kdSlider.setValue(0);
        kdSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                kdSliderStateChanged(evt);
            }
        });

        kiSlider.setValue(0);
        kiSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                kiSliderStateChanged(evt);
            }
        });

        jLabel1.setText("kP");

        jLabel2.setText("kI");

        jLabel3.setText("kD");

        currentP.setText("0.0");

        currentI.setText("0.0");

        currentD.setText("0.0");

        lb_console.setText("console");

        chartPnlArea.setLayout(new javax.swing.BoxLayout(chartPnlArea, javax.swing.BoxLayout.LINE_AXIS));

        Comms_Titel_Label.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        Comms_Titel_Label.setText("Comms Port:");

        ta_reviced.setColumns(20);
        ta_reviced.setFont(new java.awt.Font("Courier New", 0, 10)); // NOI18N
        ta_reviced.setRows(5);
        jScrollPane2.setViewportView(ta_reviced);

        Command_Titel_Label.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        Command_Titel_Label.setText("Sent Commands");

        Recived_Titel_Label.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        Recived_Titel_Label.setText("Recived Commands");

        Comms_Label.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        Comms_Label.setText("port number");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(Connect_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Disconnect_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(48, 48, 48)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(jLabel1))
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(kiSlider, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
                            .addComponent(kpSlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(kdSlider, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(currentP)
                            .addComponent(currentI, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(currentD)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(tx_command, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(Send_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Comms_Label)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(Command_Titel_Label, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Recived_Titel_Label, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lb_console, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(Comms_Titel_Label)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 640, Short.MAX_VALUE)
                .addComponent(chartPnlArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(chartPnlArea, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Connect_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Disconnect_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(kpSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(currentP))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(kiSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(currentI))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(kdSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(currentD)))
                            .addComponent(jLabel2))
                        .addGap(4, 4, 4)
                        .addComponent(Comms_Titel_Label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Comms_Label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tx_command, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Send_Button))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Command_Titel_Label)
                            .addComponent(Recived_Titel_Label))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jScrollPane2)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lb_console))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void Disconnect_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Disconnect_ButtonActionPerformed
        // TODO add your handling code here:
        //Disconnect Serial
        serial.closeConnection();
    }//GEN-LAST:event_Disconnect_ButtonActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        //Send Tuning values
    }//GEN-LAST:event_jButton5ActionPerformed

    private void Send_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Send_ButtonActionPerformed
        //Serial send command 
        submit();
        // TODO add your handling code here:
    }//GEN-LAST:event_Send_ButtonActionPerformed

    private void kpSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_kpSliderStateChanged
        currentP.setText(Double.toString((double)kpSlider.getValue()/100));
    }//GEN-LAST:event_kpSliderStateChanged

    private void kiSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_kiSliderStateChanged
        // TODO add your handling code here:
        currentI.setText(Double.toString((double)kiSlider.getValue()/100));

    }//GEN-LAST:event_kiSliderStateChanged

    private void kdSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_kdSliderStateChanged
        // TODO add your handling code here:
        currentD.setText(Double.toString((double)kdSlider.getValue()/100));

    }//GEN-LAST:event_kdSliderStateChanged

    private void Connect_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Connect_ButtonActionPerformed
        // TODO add your handling code here:
        //Connect serial
        serial.openConnection();
        
    }//GEN-LAST:event_Connect_ButtonActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        // Send step Inpulse
    }//GEN-LAST:event_jButton3ActionPerformed

    private void tx_commandKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tx_commandKeyPressed
        // TODO add your handling code here:
        //System.out.print(evt.getKeyCode());
        switch (evt.getKeyCode()){
            case 10://enter
                submit();
                break;
            case 38://up
                if(listcount > 0){
                    listcount--;
                    tx_command.setText(CommandHistoryList.get(listcount));
                }
                break;
            case 40://down
                if(listcount < CommandHistoryList.size()-1){
                    listcount++;
                    tx_command.setText(CommandHistoryList.get(listcount));
                }
                break;
        }
    }//GEN-LAST:event_tx_commandKeyPressed

    
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
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TuingUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TuingUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TuingUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TuingUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TuingUI().setVisible(true);
                
            }
        });
        
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Command_Titel_Label;
    private javax.swing.JLabel Comms_Label;
    private javax.swing.JLabel Comms_Titel_Label;
    private javax.swing.JButton Connect_Button;
    private javax.swing.JButton Disconnect_Button;
    private javax.swing.JLabel Recived_Titel_Label;
    private javax.swing.JButton Send_Button;
    private static javax.swing.JPanel chartPnlArea;
    private javax.swing.JLabel currentD;
    private javax.swing.JLabel currentI;
    private javax.swing.JLabel currentP;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSlider kdSlider;
    private javax.swing.JSlider kiSlider;
    private javax.swing.JSlider kpSlider;
    private javax.swing.JLabel lb_console;
    private javax.swing.JTextArea ta_history;
    private javax.swing.JTextArea ta_reviced;
    private javax.swing.JTextField tx_command;
    // End of variables declaration//GEN-END:variables
}
