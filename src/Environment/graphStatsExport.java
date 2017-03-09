/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Environment;

import Challenge.Challenge;
import Common.Utils.OutputNameFormatter;
import Graphics.GraphicManager.GraphicManager;
import auresearch.FactoryHolder;
import java.awt.BasicStroke;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.DeviationRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.data.xy.YIntervalSeries;
import org.jfree.data.xy.YIntervalSeriesCollection;
import org.jfree.ui.RectangleInsets;

/**
 *
 * @author d3vil401
 */
public class graphStatsExport 
{
    public static final int _GRAPH_AVG_CHALLENGES_PER_ROUND = 0;
    public static final int _GRAPH_SAGENTS_PER_ROUND = 1;
    public static final int _GRAPH_PAGENTS_PER_ROUND = 2;
    public static final int _GRAPH_COMPOSITEXP = 3;
    public static final int _GRAPH_COMPOSITECHALLENGES = 4;
    
    private static double _getStdDevianceMinChallenges(ArrayList<cRound> _rounds)
    {
        return 0;
    }
    
    private static double _getStdDevianceMaxChallenges(ArrayList<cRound> _rounds)
    {
        return 0;
    }
    
    public static ChartPanel[] renderGraphs(ArrayList<cRound> _rounds, roundStatsHolder _globalStats)
    {
        ChartPanel[] _outGraphs = new ChartPanel[5];
        
        YIntervalSeries compositeExpSeries = new YIntervalSeries("Solvers");
	YIntervalSeries compositeProblemSeries = new YIntervalSeries("Proposers"); // TODO
	XYSeries avgProbsRoundsSeries = new XYSeries("Fraction of problems solved"); // TODO
	XYSeries agentsPerRoundSeries = new XYSeries("Solvers");
	XYSeries proposersPerRoundSeries = new XYSeries("Proposers");
        
        for (int i = 0; i < _rounds.size(); i++)
            agentsPerRoundSeries.add(i, _rounds.get(i)._stats._SAgentsCountPerRound);
        
        for (int i = 0; i < _rounds.size(); i++)
            compositeExpSeries.add(i, _rounds.get(i)._stats._avgExpPerRound, _globalStats.g_stdMinDevianceAVG[i], _globalStats.g_stdPlusDevianceAVG[i]);
        
        for (int i = 0; i < _rounds.size(); i++)
            compositeProblemSeries.add(i, _globalStats.g_avgProblemsPerRound[i], _globalStats.g_stdMinDevianceAVGProblems[i], _globalStats.g_stdPlusDevianceAVGProblems[i]);
        
        for (int i = 0; i < _rounds.size(); i++)
            proposersPerRoundSeries.add(i, _rounds.get(i)._stats._PAgentsCountPerRound);
        
        XYSeriesCollection dataset = new XYSeriesCollection();
	dataset.addSeries(avgProbsRoundsSeries);
        
        YIntervalSeriesCollection datasetDP = new YIntervalSeriesCollection();
        datasetDP.addSeries(compositeExpSeries);
        datasetDP.addSeries(compositeProblemSeries);
        
        XYSeriesCollection dataset2 = new XYSeriesCollection();
	XYSeriesCollection dataset3 = new XYSeriesCollection();
	dataset2.addSeries(agentsPerRoundSeries);
	dataset3.addSeries(proposersPerRoundSeries);
        
        JFreeChart avgProbRoundsChart = ChartFactory.createXYLineChart(
				"Fraction of problems solved ", "Rounds",
				"Fraction of total problems", dataset);

	JFreeChart agentsPerRoundChart = ChartFactory.createXYLineChart(
				"Number of agents per round", "Rounds", "Number of agents",
				dataset2);
        
        XYPlot plot = agentsPerRoundChart.getXYPlot();
	plot.setDataset(0, dataset2);
	plot.setDataset(1, dataset3);
	XYLineAndShapeRenderer renderer0 = new XYLineAndShapeRenderer();
	XYLineAndShapeRenderer renderer1 = new XYLineAndShapeRenderer();
	plot.setRenderer(0, renderer0);
	plot.setRenderer(1, renderer1);
	plot.getRendererForDataset(plot.getDataset(0)).setSeriesPaint(0, Color.red);
	plot.getRendererForDataset(plot.getDataset(1)).setSeriesPaint(0, Color.blue);
        
        JFreeChart compositeChart = ChartFactory.createXYLineChart(
				"Average skill/difficulty", "Rounds", "Exp/Difficulty",
				datasetDP, PlotOrientation.VERTICAL, true, true, false);
	XYPlot xyplot = (XYPlot)compositeChart.getPlot();
	xyplot.setDomainPannable(true);
	xyplot.setRangePannable(false);
	xyplot.setInsets(new RectangleInsets(5D, 5D, 5D, 20D));
	DeviationRenderer deviationrenderer = new DeviationRenderer(true, false);
	deviationrenderer.setSeriesStroke(0, new BasicStroke(3F, 1, 1));
	deviationrenderer.setSeriesStroke(0, new BasicStroke(3F, 1, 1));
	deviationrenderer.setSeriesStroke(1, new BasicStroke(3F, 1, 1));
	deviationrenderer.setSeriesFillPaint(0, new Color(255, 200, 200));
	deviationrenderer.setSeriesFillPaint(1, new Color(200, 200, 255));
	xyplot.setRenderer(deviationrenderer);
	NumberAxis numberaxis = (NumberAxis)xyplot.getRangeAxis();
	numberaxis.setAutoRangeIncludesZero(false);
	numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
	XYPlot xyPlot2 = agentsPerRoundChart.getXYPlot();
	NumberAxis domainAxis2 = (NumberAxis) xyPlot2.getDomainAxis();
	NumberAxis domainAxis21 = (NumberAxis) xyPlot2.getRangeAxis();
	domainAxis21.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
	domainAxis2.setRange(1, _rounds.size());
	domainAxis2.setTickUnit(new NumberTickUnit(5));
        
        try {
            // create the image file on the desktop // 1400, 700
            ChartUtilities.saveChartAsPNG(new File(FactoryHolder._configManager.getStringValue("GRAPH_OUTPUT_FOLDER") + File.separator
                                                    + OutputNameFormatter.parseName(FactoryHolder._graphNames[1])), agentsPerRoundChart, 3000, 1000);

            ChartUtilities.saveChartAsPNG(new File(FactoryHolder._configManager.getStringValue("GRAPH_OUTPUT_FOLDER") + File.separator
                                                    + OutputNameFormatter.parseName(FactoryHolder._graphNames[0])), avgProbRoundsChart, 3000, 1000);
			
            ChartUtilities.saveChartAsPNG(new File(FactoryHolder._configManager.getStringValue("GRAPH_OUTPUT_FOLDER") + File.separator
                                                    + OutputNameFormatter.parseName(FactoryHolder._graphNames[3])), compositeChart, 3000, 1000);
        } catch (IOException e1) {
            e1.printStackTrace();
	}
        
        XYSeries avgAgentsRoundsSeries = new XYSeries("Fraction of successful agents ");

	for (int i = 0; i < _rounds.size(); i++)
            avgAgentsRoundsSeries.add(i + 1, _rounds.get(i)._stats.getSolvedAgents());
        
        XYSeriesCollection dataset1 = new XYSeriesCollection();
	dataset1.addSeries(avgAgentsRoundsSeries);

	JFreeChart avgAgentsRoundsChart = ChartFactory.createXYLineChart(
				"Fraction of agents having solved ", "Rounds",
				"Fraction of successful agents", dataset1);

	// setting the axises of the graph
	XYPlot xyPlot1 = avgAgentsRoundsChart.getXYPlot();
	NumberAxis domainAxis1 = (NumberAxis) xyPlot1.getDomainAxis();
	domainAxis1.setRange(1, _rounds.size());
	domainAxis1.setTickUnit(new NumberTickUnit(5));
        
        try {
            // save graph as image on desktop
            ChartUtilities.saveChartAsPNG(new File(FactoryHolder._configManager.getStringValue("GRAPH_OUTPUT_FOLDER") + File.separator
                                                    + OutputNameFormatter.parseName(FactoryHolder._graphNames[2])), avgAgentsRoundsChart, 3000, 1000);
	} catch (IOException e1) {
            e1.printStackTrace();
	}
        
        _outGraphs[_GRAPH_AVG_CHALLENGES_PER_ROUND] = new ChartPanel(avgProbRoundsChart);
        _outGraphs[_GRAPH_COMPOSITEXP] = new ChartPanel(compositeChart);
        _outGraphs[_GRAPH_SAGENTS_PER_ROUND] = new ChartPanel(agentsPerRoundChart);
        _outGraphs[_GRAPH_PAGENTS_PER_ROUND] = new ChartPanel(avgAgentsRoundsChart);
        
        return _outGraphs;
    }
}
