/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics.GraphicManager;

import Common.Logging.ILogManager;
import auresearch.FactoryHolder;
import org.jfree.chart.JFreeChart;

/**
 *
 * @author d3vil401a
 */
public class GraphicManager 
{
    private static GraphicManager __instance = new GraphicManager();
    
    public static GraphicManager getInstance()
    {
        if (__instance != null)
            return __instance;
        else
            return __instance = new GraphicManager();
    }
    
    private GraphContentManager _gcM = null;
    private GraphTypeHandler _gtH = null;
    
    public void setGraphProperties(GraphTypeHandler._GRAPH_TYPE _type, GraphTypeHandler._GRAPH_ORIENTATION _orientation)
    {
        this._gtH = new GraphTypeHandler(_type, _orientation);
    }
    
    public void setSeriesName(String _name)
    {
        this._gcM.setSeriesName(_name);
    }
    
    public GraphicManager()
    {
        this._gcM = new GraphContentManager();
        this.setGraphProperties(GraphTypeHandler._GRAPH_TYPE.TYPE_XYLINE, GraphTypeHandler._GRAPH_ORIENTATION.ORIENT_HORIZONTAL);
    }
    
    public GraphicManager(GraphTypeHandler._GRAPH_TYPE _type, GraphTypeHandler._GRAPH_ORIENTATION _orientation)
    {
        this._gcM = new GraphContentManager();
        this.setGraphProperties(_type, _orientation);
    }
    
    public void setName(String _name)
    {
        if (this._gtH != null)
            this._gtH.setName(_name);
        else
            FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_ERROR, "Assigning chart name " + _name + " to an uninitialized class.");
    }
    
    public void addCategoryData(double _value, String _unit, String _what)
    {
        this._gcM.addCategory(_value, _unit, _what);
    }
    
    public void addCategoryData(Number _value, String _unit, String _what)
    {
        this._gcM.addCategory(_value, _unit, _what);
    }
    
    public void addXYData(Number x, Number y)
    {
        this._gcM.addXYData(x, y);
    }
    
    public boolean assignData()
    {
        if (this._gtH.getDataType().equals(GraphTypeHandler._GRAPH_DATASET_TYPE.TYPE_DEFAULT))
            return this._gtH.allegateDataset(this._gcM._dataSet);
        else if (this._gtH.getDataType().equals(GraphTypeHandler._GRAPH_DATASET_TYPE.TYPE_XYCOLLECTION))
            return this._gtH.allegateDataset(this._gcM._series);
        
        return false;
    }
    
    public boolean exportGraphicToFolder()
    {
        // TODO
        return false;
    }
    
    public JFreeChart render()
    {
        return this._gtH.renderGraphic();
    }
}
