import javax.swing.JPanel;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;


/**
 * Creates a simple real-time chart
 */
public class Chart {
    
    static XYChart graph;
    static double phase = 0;
    static double[][] initdata = getSineData(phase);
    
    JPanel chartPnlArea;
    
    public Chart(JPanel chartPnlArea){
        this.chartPnlArea = chartPnlArea;
        graph = QuickChart.getChart("Simple XChart Real-time Demo", "Radians", "Sine", "sine", initdata[0], initdata[1]);
        
        JPanel pnlChart = new XChartPanel(graph); 
        
        this.chartPnlArea.add(pnlChart);
        this.chartPnlArea.validate();
    }

  public static void main(String[] args) throws Exception {

    // Create Chart
    graph = QuickChart.getChart("Simple XChart Real-time Demo", "Radians", "Sine", "sine", initdata[0], initdata[1]);

    while (true) {

      phase += 2 * Math.PI * 2 / 20.0;

      Thread.sleep(100);

      final double[][] data = getSineData(phase);

      graph.updateXYSeries("sine", data[0], data[1], null);
      //chartPnlArea.validate();
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