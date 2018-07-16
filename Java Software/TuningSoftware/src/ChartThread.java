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
public class ChartThread extends Thread {
    
   
    
    JPanel chartPnlArea;
    
    public ChartThread(JPanel chartPnlArea){
        this.chartPnlArea = chartPnlArea;
    }
    
    public void run(){
        
        final XYChart graph;
        double phase = 0;
        double[][] initdata = getSineData(phase);
        
        graph = QuickChart.getChart("Simple XChart Real-time Demo", "Radians", "Sine", "sine", initdata[0], initdata[1]);
        
        JPanel pnlChart = new XChartPanel(graph); 
        
        this.chartPnlArea.add(pnlChart);
        this.chartPnlArea.validate();
        
        
        //graph = QuickChart.getChart("Simple XChart Real-time Demo", "Radians", "Sine", "sine", initdata[0], initdata[1]);

        while (true) {
            
            //System.out.println("update");
            
            phase += 2 * Math.PI * 2 / 20.0;

            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(ChartThread.class.getName()).log(Level.SEVERE, null, ex);
            }

            final double[][] data = getSineData(phase);

            graph.updateXYSeries("sine", data[0], data[1], null);
            pnlChart.updateUI();
            chartPnlArea.validate();
        }
    }
    
    private static double[][] getSineData(double phase) {

        double[] xData = new double[100];
        double[] yData = new double[100];
        for (int i = 0; i < xData.length; i++) {
            double radians = phase + (2 * Math.PI / xData.length * i);
            xData[i] = radians;
            yData[i] = Math.sin(radians);
        }
        return new double[][] { xData, yData };
    }
    
}
