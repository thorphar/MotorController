/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
/**
 *
 * @author vincent strong
 */
public class Serial{
    private static SerialPort comPort = null;
    private static String readData = "";
    private static ArrayList<String> dataIn = new ArrayList<String>();
    
    private ChartObject chart;
    private JTextArea ta_console;

    
    public Serial(JTextArea ta_console, ChartObject chart){
        
        this.chart = chart;
        this.ta_console = ta_console;
        
        if(SerialPort.getCommPorts().length <= 0 ){
            System.out.println("No device connected, connect one then restart the programe");
            final JPanel panel = new JPanel();
            JOptionPane.showMessageDialog(panel, "No device connected, connect one then restart the programe", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
            return;
        }
        
        for(int i = 0; i<SerialPort.getCommPorts().length;i++)
            System.out.println(SerialPort.getCommPorts()[i].toString());
        
        comPort = SerialPort.getCommPorts()[0];
        comPort.setBaudRate(19200);
        
        comPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_AVAILABLE; }
            @Override
            public void serialEvent(SerialPortEvent event){
                if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                   return;
                byte[] newData = new byte[comPort.bytesAvailable()];
                int numRead = comPort.readBytes(newData, newData.length);
                String doc2 = "";
                  try {
                          doc2 = new String(newData, "UTF-8");
                  } catch (UnsupportedEncodingException e) {
                          // TODO Auto-generated catch block
                          e.printStackTrace();
                  }
                  if(doc2.contains("\n")) {
                            readData += doc2;
                            dataIn.add(readData);
                            ta_console.setText(ta_console.getText()+readData);
                            double speed = 0.0;
                            if(readData.contains("pvspeed")){
                                //speed = 
                                int start = readData.lastIndexOf("pvspeed")+"pvspeed".length();
                                int end = readData.indexOf(",");
                                if(start >= readData.length() || end >= readData.length()){
                                  
                            }
                            else{
                                try{
                                    speed = Double.parseDouble(readData.substring(start, end));
                                    chart.setRecentY(speed);
                                    //System.out.println(speed);
                                    
                                }
                                catch(StringIndexOutOfBoundsException ex){
                                    
                                }
                            }
                              
                              //pvspeed0.00,
                          }
                          //chart.addDataPoint(new Random().nextInt(50));
                          readData = "";
                  }
                  else {
                          readData += doc2;
                  }
                //System.out.println(doc2);
            }
        });
        
    }
    
    public String getCommsPort(){
        if(comPort == null)return "No device";
        else return comPort.toString();
    }
    
    public void openConnection(){
        comPort.openPort();
        chart.setConnected(true);
    }
    
    public void closeConnection(){
        comPort.closePort();
        chart.setConnected(false);
    }
    
    public void sendData(String data){
        OutputStream out = comPort.getOutputStream();
        
        if(!data.endsWith("\n"))data +="\n";
        
        try {	
            out.write(data.getBytes());
        } catch (IOException ex) {
            //Logger.getLogger(Serial.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Unable to send data");
            final JPanel panel = new JPanel();
            JOptionPane.showMessageDialog(panel, "Unable to send data", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NullPointerException ex) {
            //Logger.getLogger(Serial.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Not connected yet");
            final JPanel panel = new JPanel();
            JOptionPane.showMessageDialog(panel, "Not connected yet", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            out.close();
        } catch (IOException ex) {
            //Logger.getLogger(Serial.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Unable to close serial stream");
            final JPanel panel = new JPanel();
            JOptionPane.showMessageDialog(panel, "Unable to close serial stream", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NullPointerException ex) {
            //Logger.getLogger(Serial.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Not connected yet");
            final JPanel panel = new JPanel();
            JOptionPane.showMessageDialog(panel, "Not connected yet", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }
}
