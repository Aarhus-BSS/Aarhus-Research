/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Common.Configuration;

import Common.Logging.ILogManager;
import Common.MsgBoxer.MBox;
import auresearch.FactoryHolder;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author d3vil401
 */
public class ConfigManager 
{
    private File _file = null;
    private Map<String, Object> _params = new HashMap<>();
    private boolean _gotError = false;
    
    private void _setupDefaultSettings()
    {
        
    }
    
    private void _importConfiguration() throws IOException
    {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(this._file)));
        } catch (FileNotFoundException ex) {
            FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_WARNING, "Unable to read configuration file, default settings are being set.");
            this._setupDefaultSettings();
            return;
        }
        
        String _line;
        String[] _entrySplit;
        String[] _arraySplit;
        int _lineIndex = 1;
        ArrayList<String> _slaveList;
        
        // We read each line.
        while ((_line = br.readLine()) != null) 
        {
            // Check if it's a comment or an empty line
            if (!_line.equals(""))
            {
                if (!_line.startsWith(";"))
                {
                    // Split configuration key and value based on =
                    _entrySplit = _line.split("=");
                    // If 2 entries aren't there then the configuration line is incomplete.
                    if (_entrySplit.length < 2)
                    {
                        FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_ERROR, "Configuration line " + _lineIndex + " is wrong (\"" + _line + "\")");
                        MBox.showBox("Configuration line " + _lineIndex + " is wrong (\"" + _line + "\")", "Configuration reading error", MBox._MSGBOX_TYPE.TYPE_ERROR);
                        this._gotError = true;
                        return;
                    }

                    // If it has 2 entries AND contains ',' then we have multiple values (array).
                    if (_entrySplit[1].contains(","))
                    {
                        // It's an array configuration, we need to split some more.
                        _slaveList = new ArrayList<>();

                        // Split each entry based on , and insert them into an arraylist.
                        _arraySplit = _entrySplit[1].split(",");
                        for (int i = 0; i < _arraySplit.length; i++)
                            _slaveList.add(_arraySplit[i]);

                        this._params.put(_entrySplit[0], _slaveList);
                        FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_DEBUG, "Added parameter " + _entrySplit[0] + " as array values " + _entrySplit[1]);
                    } else {
                        this._params.put(_entrySplit[0], _entrySplit[1]);
                        FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_DEBUG, "Added parameter " + _entrySplit[0] + " as value " + _entrySplit[1]);
                    }
                }
            }
        }
        br.close();
    }
    
    public ConfigManager() throws IOException
    {
        FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_INFORMATION, "Loading configuration file " + System.getProperty("user.dir") + File.separator + "options.cfg");
        this._file = new File(System.getProperty("user.dir") + File.separator + "options.cfg");
        if (!this._file.exists() || !this._file.canRead())
        {
            FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_WARNING, "Can't read configuration file, setting up with default development settings.");
            this._setupDefaultSettings();
        } else {
            this._importConfiguration();
        }
    }
    
    public ArrayList getArrayValue(String _key)
    {
        for (Map.Entry<String, Object> entry : this._params.entrySet())
            if (entry.getKey().toLowerCase().equals(_key.toLowerCase()))
                return (ArrayList)entry.getValue();
        
        FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_ERROR, _key + " not found");
        return null;
    }
    
    public float getFloatValue(String _key)
    {
        for (Map.Entry<String, Object> entry : this._params.entrySet())
            if (entry.getKey().toLowerCase().equals(_key.toLowerCase()))
                return Float.parseFloat(entry.getValue().toString());
        
        FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_ERROR, _key + " not found");
        return -0xFFFF;
    }
    
    public int getNumberValue(String _key)
    {
        for (Map.Entry<String, Object> entry : this._params.entrySet())
            if (entry.getKey().toLowerCase().equals(_key.toLowerCase()))
                return Integer.parseInt(entry.getValue().toString());
        
        FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_ERROR, _key + " not found");
        return -0xFFFF;
    }
    
    public boolean hasError()
    {
        return this._gotError;
    }
    
    public String getStringValue(String _key)
    {
        for (Map.Entry<String, Object> entry : this._params.entrySet())
            if (entry.getKey().toLowerCase().equals(_key.toLowerCase()))
                return (String)entry.getValue().toString();
        
        FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_ERROR, _key + " not found");
        return null;
    }
}
