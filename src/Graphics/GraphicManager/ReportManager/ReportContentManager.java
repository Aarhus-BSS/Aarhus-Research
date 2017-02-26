/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics.GraphicManager.ReportManager;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author d3vil401
 */
public class ReportContentManager 
{
    private Map<String, Object[]> _dataArchive = new TreeMap<>();
    
    public ReportContentManager(Object[] _sheetIndex)
    {
        // [ Index 1 ] [ Index 2] [ Index 3]
        // [ VALUE   ] [ VALUE  ] [ VALUE  ]
        // [ VALUE   ] [ VALUE  ] [ VALUE  ]
        this.add("1", _sheetIndex);
    }
    
    public void add(String _index, Object[] _content)
    {
        this._dataArchive.put(_index, _content);
    }
    
    public Map<String, Object[]> getMap()
    {
        return this._dataArchive;
    }
    
    private void _rescale(String _from)
    {
        for (Map.Entry<String, Object[]> entry : this._dataArchive.entrySet())
            if (Integer.parseInt(_from) < Integer.parseInt(entry.getKey()))
            {
                this._dataArchive.put(String.valueOf(Integer.parseInt(entry.getKey()) - 1), entry.getValue());
                this._dataArchive.remove(entry.getKey());
            }
    }
    
    public void remove(String _index)
    {
        this._dataArchive.remove(_index);
        // Re-scale the sheet to avoid empty rows.
        this._rescale(_index);
    }
}
