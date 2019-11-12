import com.intellij.uiDesigner.core.GridConstraints;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.time.Instant;

public class GraphShow {
    private JButton doneButton;
    public JPanel panel1;
    private JPanel graphPanel;
    JFreeChart lineChart;

    public GraphShow() {

        //intitialize dataset
        XYSeries xySeries = new XYSeries("mV");
        XYSeriesCollection xyDataset = new XYSeriesCollection();
        xyDataset.addSeries(xySeries);

        //initilize line chart
        lineChart = ChartFactory.createXYLineChart(
                "ECG",
                "Time", "mV",
                xyDataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        //initialize chart panel and add it to graphPanel
        ChartPanel chartPanel = createDemoPanel();
        chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));
        graphPanel.removeAll();
        graphPanel.add(chartPanel, new GridConstraints(), 0);
        graphPanel.revalidate();

        new Thread(() -> {

            long start = Instant.now().toEpochMilli();

            for (int i = 0; true; i++) {
                try {
                    Global.awesomeClient.sendMessageLine("meo");
                    String message = Global.awesomeClient.readMessageLine();

                    if (message.equals("done")) {
                        doneButton.setEnabled(true);
                        break;
                    }
                    float data = Float.parseFloat(message);

                    xySeries.add(Instant.now().toEpochMilli() - start, data);

                    if (xySeries.getItemCount() > 50) {
                        xySeries.remove(0);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        doneButton.addActionListener(e -> Global.ChangePanel(new Options().panel1));
    }

    private ChartPanel createDemoPanel() {
        XYPlot xyPlot = (XYPlot) lineChart.getPlot();
        xyPlot.setDomainCrosshairVisible(true);
        xyPlot.setRangeCrosshairVisible(true);
        XYItemRenderer renderer = xyPlot.getRenderer();
        renderer.setSeriesPaint(0, Color.blue);

        NumberAxis range = (NumberAxis) xyPlot.getRangeAxis();
        range.setRange(-1.0, 1.0);
        range.setTickUnit(new NumberTickUnit(0.1));
        return new ChartPanel(lineChart);
    }
}
