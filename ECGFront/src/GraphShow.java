import com.intellij.uiDesigner.core.GridConstraints;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.io.IOException;
import java.time.Instant;

public class GraphShow {
    private JButton doneButton;
    public JPanel panel1;
    private JPanel graphPanel;

    public GraphShow() {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
        XYSeries  xySeries = new XYSeries("mV");
        XYSeriesCollection xyDataset = new XYSeriesCollection( );
        xyDataset.addSeries( xySeries );

        JFreeChart lineChart = ChartFactory.createXYLineChart(
                "ECG",
                "Time","mV",
                xyDataset,
                PlotOrientation.VERTICAL,
                true,true,false);

        ChartPanel chartPanel = new ChartPanel( lineChart );
        chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
        graphPanel.removeAll();



        graphPanel.add(chartPanel, new GridConstraints(), 0);
        graphPanel.revalidate();

        new Thread(() -> {

            long start = Instant.now().toEpochMilli();

            for (int i=0; true; i++){
                try {

                    String message = Global.awesomeClient.readMessageLine();
                    if(message.equals("done")){
                        doneButton.setEnabled(true);
                        break;
                    }
                    float data = Float.parseFloat(Global.awesomeClient.readMessageLine());

                    xySeries.add(Instant.now().toEpochMilli()- start, data);

                    if(xySeries.getItemCount()> 50){
                        xySeries.remove(0);
                    }

                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        doneButton.addActionListener(e -> Global.ChangePanel(new Options().panel1));
    }
}
