/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics.GraphicManager;

import Common.Logging.ILogManager;
import auresearch.FactoryHolder;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author d3vil401
 */
public class GraphTypeHandler 
{
    public enum _GRAPH_DATASET_TYPE
    {
        TYPE_DEFAULT,
        TYPE_XYCOLLECTION
    }
    
    public enum _GRAPH_TYPE
    {
        TYPE_PIECHART, 
        TYPE_LINECHART, 
        TYPE_HISTOGRAM, 
        TYPE_XYLINE
    }
    
    public enum _GRAPH_ORIENTATION
    {
        ORIENT_VERTICAL,
        ORIENT_HORIZONTAL
    }
    
    private _GRAPH_TYPE _type = null;
    private _GRAPH_ORIENTATION _orientation = _GRAPH_ORIENTATION.ORIENT_HORIZONTAL;
    private _GRAPH_DATASET_TYPE _dataSetType = _GRAPH_DATASET_TYPE.TYPE_DEFAULT;
    
    private DefaultCategoryDataset _defaultDelegated = new DefaultCategoryDataset();
    private XYSeriesCollection     _xyDelegated = new XYSeriesCollection();
    private PieDataset             _pieDelegated = null;
    
    private String                 _name = "Unnamed Graphic";
    private String                 _CatAxisName = "Unnamed Y";
    private String                 _ValAxisName = "Unnamed X";
    
    public GraphTypeHandler(_GRAPH_TYPE _type, _GRAPH_ORIENTATION _orientation)
    {
        this._type = _type;
        
        switch (this._type)
        {
            case TYPE_PIECHART:
            case TYPE_LINECHART:
            case TYPE_HISTOGRAM:
                this._dataSetType = _GRAPH_DATASET_TYPE.TYPE_DEFAULT;
                break;
            case TYPE_XYLINE:
                this._dataSetType = _GRAPH_DATASET_TYPE.TYPE_XYCOLLECTION;
                break;
            default:
                FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_ERROR, "GraphTypeHandler::__construct() case happened to be a default, absolutely outrageous.");
                break;
        }
        this._orientation = _orientation;
        this._name = _name;
    }
    
    public GraphTypeHandler(_GRAPH_TYPE _type, _GRAPH_ORIENTATION _orientation, String _name)
    {
        this._type = _type;
        
        switch (this._type)
        {
            case TYPE_PIECHART:
            case TYPE_LINECHART:
            case TYPE_HISTOGRAM:
                this._dataSetType = _GRAPH_DATASET_TYPE.TYPE_DEFAULT;
                break;
            case TYPE_XYLINE:
                this._dataSetType = _GRAPH_DATASET_TYPE.TYPE_XYCOLLECTION;
                break;
            default:
                FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_ERROR, "GraphTypeHandler::__construct() case happened to be a default, absolutely outrageous.");
                break;
        }
        this._orientation = _orientation;
        this._name = _name;
    }
    
    public void setName(String _name)
    {
        this._name = _name;
    }
    
    public _GRAPH_DATASET_TYPE getDataType()
    {
        return this._dataSetType;
    }
    
    public void setAxisName(String _category, String _value)
    {
        this._CatAxisName = _category;
        this._ValAxisName = _value;
    }
    
    public boolean allegateDataset(Object _dataSet)
    {
        if (_dataSet instanceof DefaultCategoryDataset)
        {
            this._defaultDelegated = (DefaultCategoryDataset)_dataSet;
            return true;
        } else if (_dataSet instanceof XYSeries) {
            this._xyDelegated.addSeries((XYSeries)_dataSet);
            return true;
        } else if (_dataSet instanceof PieDataset) {
            
            return false;
        }
        
        return false;
    }
    
    // return ChartFactory.createLineChart("Time spent on this project", "What", "Time", this._dataSet, PlotOrientation.HORIZONTAL, false, false, false);
    public JFreeChart renderGraphic()
    {
        JFreeChart _chart = null;
        PlotOrientation _currentPlot = null;
        
        switch (this._orientation)
        {
            case ORIENT_VERTICAL:
                _currentPlot = PlotOrientation.VERTICAL;
                break;
            case ORIENT_HORIZONTAL:
                _currentPlot = PlotOrientation.HORIZONTAL;
                break;
        }
        
        switch (this._type)
        {
            case TYPE_PIECHART:
                _chart = ChartFactory.createPieChart(this._name, this._pieDelegated);
                break;
            case TYPE_LINECHART:
                _chart = ChartFactory.createLineChart(this._name, this._CatAxisName, this._ValAxisName, this._defaultDelegated);
                break;
            case TYPE_HISTOGRAM:
                //_chart = ChartFactory.createHistogram(_name, _CatAxisName, _CatAxisName, _xyDelegated, _currentPlot, true, true, true)
                FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_ERROR, "GraphTypeHandler::renderGraphic() case happened to be a histogram, not allowed, sry.");
                break;
            case TYPE_XYLINE:
                _chart = ChartFactory.createXYLineChart(this._name, this._ValAxisName, this._CatAxisName, this._xyDelegated, _currentPlot, true, true, false);
                break;
            default:
                FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_ERROR, "GraphTypeHandler::renderGraphic() case happened to be a default, absolutely barbaric.");
                break;
        }
        return _chart;
    }
}
