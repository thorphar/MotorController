package uk.co.tuning;

import javax.swing.*;

import java.awt.GridLayout;
import java.awt.event.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import com.fazecast.jSerialComm.*;

public class main {
		static SerialPort comPort;
		static String readData = "";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame f = new JFrame();
		f.setSize(400, 400);
		f.setLayout(new GridLayout(6,1));
		System.out.println(SerialPort.getCommPorts().toString());
		JButton b = new JButton("Click");
		JTextField tx = new JTextField(6);
		JLabel lb = new JLabel("Serial Read...");
		JButton bConnect = new JButton("Connect");
		JButton bClose = new JButton("Close");
		JButton bSpeed = new JButton("Get Speed");
		
		
		comPort = SerialPort.getCommPorts()[0];
		comPort.setBaudRate(9600);
		
		f.addWindowListener(new WindowAdapter() {
	         public void windowClosing(WindowEvent windowEvent){
	            System.exit(0);
	         }        
	      }); 
		
		comPort.addDataListener(new SerialPortDataListener() {
			   @Override
			   public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_AVAILABLE; }
			   @Override
			   public void serialEvent(SerialPortEvent event)
			   {
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
					readData+= doc2;
					lb.setText("");
					lb.setText(readData);
					readData = "";
				}
				else {
					readData+= doc2;
				}
			      System.out.println(doc2);
			   }
			});
		
		
		
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent  e) {				
				OutputStream out = comPort.getOutputStream();
				try {
					System.out.println(tx.getText());
					out.write((tx.getText()+"\n").getBytes());	
					out.close();

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		
		
		
		
		bConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent  e) {
				comPort.openPort();	
			}
		});
		bClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent  e) {
				comPort.closePort();	
			}
		});
		
		bSpeed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent  e) {
				OutputStream out = comPort.getOutputStream();
				try {
					
					out.write(("1vs_speed"+"\n").getBytes());	
					out.close();

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		
		
		f.add(b);
		f.add(bConnect);
		f.add(bClose);
		f.add(bSpeed);
		f.add(tx);
		f.add(lb);
		
		f.setVisible(true);
	}

}
