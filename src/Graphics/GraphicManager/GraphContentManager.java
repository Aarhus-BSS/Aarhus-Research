/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics.GraphicManager;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.YIntervalSeries;

/**
 *
 * @author d3vil401
 */
public class GraphContentManager 
{
    DefaultCategoryDataset _dataSet = new DefaultCategoryDataset();
    XYSeries _series;
    String _SeriesName = "Dummy Name";
    YIntervalSeries[] _intervals = new YIntervalSeries[2];
    int _intervalIndex = 0;
    
    public void setSeriesName(String _name)
    {
        this._SeriesName = _name;
    }
    
    public GraphContentManager()
    {
        this._series = new XYSeries(this._SeriesName);
    }
    
    public void addCategory(double _value, String _unit, String _what)
    {
        this._dataSet.addValue(_value, _what, _unit);
    }
    
    public void addCategory(Number _value, String _unit, String _what)
    {
        this._dataSet.addValue(_value, _what, _unit);
    }
    
    public void addXYData(Number x, Number y)
    {
        this._series.add(x, y);
    }
    
    public void addYInterval(String _name)
    {
        if (_intervalIndex < 2)
            this._intervals[_intervalIndex++] = new YIntervalSeries(_name);
    }
}
