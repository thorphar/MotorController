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
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author vincent strong
 */
public class Serial {
    private static SerialPort comPort;
    private static String readData = "";
    private static ArrayList<String> dataIn = new ArrayList<String>();
    
    public Serial(){
                
        for(int i = 0; i<SerialPort.getCommPorts().length;i++)
            System.out.println(SerialPort.getCommPorts()[i].toString());
        
        comPort = SerialPort.getCommPorts()[0];
        comPort.setBaudRate(9600);
        
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
                          readData = "";
                  }
                  else {
                          readData += doc2;
                  }
                System.out.println(doc2);
            }
        });
        
    }
    
    public String getCommsPort(){
        return comPort.toString();
    }
    
    public void openConnection(){
        comPort.openPort();
    }
    
    public void closeConnection(){
        comPort.closePort();
    }
    
    public void sendData(String data){
        OutputStream out = comPort.getOutputStream();
        
        if(!data.endsWith("\n"))data +="\n";
        
        try {	
            out.write(data.getBytes());
        } catch (IOException ex) {
            Logger.getLogger(Serial.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Unable to send data");
        }
        
        try {
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(Serial.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Unable to close serial stream");
        }
    }
}
