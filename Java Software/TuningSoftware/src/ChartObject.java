import java.util.ArrayList;
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
public class ChartObject {
    
    private XYChart graph;
    private JPanel pnlChart;
    
    private JPanel chartPnlArea;
    
    private long startTime;
    
    private ArrayList<Double> dataX = new ArrayList<Double>();
    private ArrayList<Double> dataY = new ArrayList<Double>();
    
    public ChartObject(JPanel chartPnlArea){
        this.chartPnlArea = chartPnlArea;
        dataX.add(0.0);
        dataY.add(0.0);
        
        startTime = System.currentTimeMillis();
        double[][] initdata = dataListToArray(dataX,dataY);
        
        graph = QuickChart.getChart("Data", "Time", "Speed", "series1", initdata[0], initdata[1]);
        
        pnlChart = new XChartPanel(graph); 
        
        this.chartPnlArea.add(pnlChart);
        this.chartPnlArea.validate();
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
    
    
    public void addDataPoint(double y){
        dataY.add(y);
        dataX.add((double)((System.currentTimeMillis() - startTime) / 1000));
        
        final double[][] data = dataListToArray(dataX,dataY);
                
        graph.updateXYSeries("series1",data[0], data[1], null);
        pnlChart.updateUI();
    }
    
}
