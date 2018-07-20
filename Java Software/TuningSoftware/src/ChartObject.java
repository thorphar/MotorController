import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author vincent strong
 */
public class ChartObject extends Thread{
    
    private XYChart graph;
    private JPanel pnlChart;
    
    private JPanel chartPnlArea;
    
    private long startTime;
    
    private ArrayList<Double> dataX = new ArrayList<Double>();
    private ArrayList<Double> dataY = new ArrayList<Double>();
    
    private ArrayList<Double> speed = new ArrayList<Double>();
    
    private double recentY = 0;
    private double pvSpeed = 0;
    
    private boolean connected = false;
    
    public ChartObject(JPanel chartPnlArea){
        this.chartPnlArea = chartPnlArea;
        dataX.add(0.0);
        dataY.add(0.0);
        speed.add(0.0);
        
        startTime = System.currentTimeMillis();
        double[][] initdata = dataListToArray(dataX,dataY);
        
        graph = QuickChart.getChart("Data", "Time (seconds)", "Speed (RPM)", "PV Speed", initdata[0], initdata[1]);
        graph.addSeries("Set Speed", initdata[1]);
        graph.getStyler().setMarkerSize(0);
        
        //for(int i = 0 ; i<graph.getStyler().getSeriesLines().length;i++)System.out.println(graph.getStyler().getSeriesLines()[i]);
        //for(int i = 0 ; i<graph.getStyler().getSeriesMarkers().length;i++)System.out.println(graph.getStyler().getSeriesMarkers()[i]);
        
        
        pnlChart = new XChartPanel(graph); 
        
        this.chartPnlArea.add(pnlChart);
        this.chartPnlArea.validate();
    }
    
    
    public void run(){
        while(true){
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(ChartObject.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(connected)this.addDataPoint(recentY, pvSpeed);
        }
    }
    
    
    private static double[][] dataListToArray(ArrayList<Double> dataX, ArrayList<Double> dataY){
        double[] targetX = new double[dataX.size()];
        for (int i = 0; i < targetX.length; i++) {
            //targetX[i] = dataX.get(i).doubleValue();  // java 1.4 style
            // or:
            targetX[i] = dataX.get(i);                // java 1.5+ style (outboxing)
        }
        
        double[] targetY = new double[dataY.size()];
        for (int i = 0; i < targetY.length; i++) {
            //targetY[i] = dataY.get(i).doubleValue();  // java 1.4 style
            // or:
            targetY[i] = dataY.get(i);                // java 1.5+ style (outboxing)
        }
        
        double[][] target = {targetX, targetY};
        return target;
    }
    
    int scale = 20;
    int position = 0;
    public void addDataPoint(double y, double pvspeed){
        dataY.add(y);
        speed.add(pvspeed);
        dataX.add((double)((System.currentTimeMillis() - startTime) / 1000));
        
        int length = dataY.size();

        final double[][] dataArray = Arrays.copyOfRange(dataListToArray(dataX,dataY), 0, length);
        final double[][] speedArray = Arrays.copyOfRange(dataListToArray(dataX,speed), 0, length);
                
        graph.updateXYSeries("PV Speed", dataArray[0], dataArray[1], null);
        graph.updateXYSeries("Set Speed", speedArray[0], speedArray[1], null);
        pnlChart.updateUI();
        
        double max =  dataX.get(dataX.size()-1);
        graph.getStyler().setXAxisMin( ((max-scale)<0) ? 0 : (max-scale) );
    }
    
    public void setScale(int scale){
        this.scale = scale;
    }
    
    public void setPosition(int position){
        this.position = position;
        
        //graph.getStyler().setXAxisMin((double)position);
        //graph.getStyler().setXAxisMax((double)(position+scale));
    }
    
    public void setPvSpeed(double pvSpeed){
        this.pvSpeed = pvSpeed;
    }
    public void setRecentY(double recentY){
        this.recentY = recentY;
    }
    public void setConnected(boolean connected){
        this.connected = connected;
    }
    
}
