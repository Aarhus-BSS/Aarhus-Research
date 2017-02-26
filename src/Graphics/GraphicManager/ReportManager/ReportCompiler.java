/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics.GraphicManager.ReportManager;

import auresearch.FactoryHolder;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author d3vil401
 */
public class ReportCompiler 
{
    protected class _report
    {
        public ReportManager _rManager = null;
        public ReportContentManager _rcManager = null;
        public int _currentLineIndex = 2; // First line is the header.
        
        public _report()
        {
            
        }
        
        public _report(ReportManager _rM, ReportContentManager _rcM)
        {
            this._rManager = _rM;
            this._rcManager = _rcM;
        }
    }
    
    private Map<String, _report> _rEntity = new HashMap<>();
    private String _reportName = "";
    
    public ReportCompiler(String _name)
    {
        this._reportName = _name;
    }
    
    public void createReport(Object[] _header)
    {
        _report _instance = new _report();
        
        _instance._rManager = new ReportManager(this._reportName);
        _instance._rcManager = new ReportContentManager(_header);
        
        _rEntity.put(this._reportName, _instance);
    }
    
    public void addContent(Object[] _content)
    {
        _rEntity.get(this._reportName)._rcManager.add(String.valueOf(_rEntity.get(this._reportName)._currentLineIndex += 1), _content);
    }
    
    public void cleanConcent()
    {
        _rEntity.clear();
    }
    
    public void end()
    {
        _rEntity.get(this._reportName)._rManager.applyData(_rEntity.get(this._reportName)._rcManager);
        _rEntity.get(this._reportName)._rManager.write(FactoryHolder._configManager.getStringValue("REPORT_OUTPUT_FOLDER"));
        _rEntity.remove(this._reportName);
    }
}
