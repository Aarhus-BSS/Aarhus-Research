/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Common.Utils;

import auresearch.FactoryHolder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 *
 * @author d3vil401
 */
public class OutputNameFormatter 
{
    private static final String[] _knownFlags = {"{DATETIME}", "{DAY}", "{HOUR}", "{SESSIONID}", "{USERNAME}", "{OS}" };
    
    private static String _getParameterValue(String _flag)
    {
        DateFormat _format = new SimpleDateFormat("dd-MM-yyyy-HH.mm");
        Date _date = new Date();
        
        if (_flag.equals(_knownFlags[0])) // DATETIME
            return _format.format(_date);
        else if (_flag.equals(_knownFlags[1])) // DAY
            return _format.format(_date).substring(0, 2);
        else if (_flag.equals(_knownFlags[2])) // HOUR
            return _format.format(_date).substring(10, 12);
        else if (_flag.equals(_knownFlags[3])) // SESSIONID
            return FactoryHolder._logManager.getSessionName();
        else if (_flag.equals(_knownFlags[4])) // USERNAME
            return System.getProperty("user.name");
        else if (_flag.equals(_knownFlags[5])) // OPERATIVE SYSTEM
            return System.getProperty("os.name");
        
        return String.valueOf(new Random().nextInt());
    }
    
    public static String parseName(String _name)
    {
        String _newName = _name;
        
        for (String _knownFlag : _knownFlags)
            if (_name.contains(_knownFlag))
                _newName = _newName.replace(_knownFlag, _getParameterValue(_knownFlag));
        
        return _newName;
    }
    
    
}
