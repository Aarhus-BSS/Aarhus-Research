/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Common.Logging;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import Common.MsgBoxer.MBox;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.util.Date;

/**
 *
 * @author d3vil401
 */

// This one goes into a file, very usefull in case we have a console mode.
public class cLogFile extends cLogConsole implements ILogManager
{
    private File                _file           = null;
    private PrintStream         _printStream    = null;
    private boolean             _isFileStream   = false;
    
    public cLogFile()
    {
        
    }
    
    @Override
    public void initializeSession(String _fileName) 
    {
        super.initializeSession(_fileName);
        
        DateFormat _dateFormat = new SimpleDateFormat("dd.MM.yyyy-HH.mm.ss");
        String _fName = _fileName + "_" + _dateFormat.format(new Date()) + ".txt";
        this._file = new File(_fName);
        try {
            // Required auto flush, so we don't have to update the buffer continuosly.
            this._printStream = new PrintStream(new BufferedOutputStream(new FileOutputStream(_file, true)));
            System.setOut(this._printStream);
            this._isFileStream = true;
        } catch (FileNotFoundException ex) {
            MBox.showBox("Unable to write " + _fName + ": " + ex.getMessage(), "Error trying to write log file", MBox._MSGBOX_TYPE.TYPE_ERROR);
        }
    }

    @Override
    public void print(_LOG_TYPE _type, String _msg) 
    {
        super.print(_type, _msg);
    }

    @Override
    public void destroySession() 
    {
        super.destroySession();
        this._printStream.flush();
        this._printStream.close();
        this._switch(); // Just to be sure.
    }
    
    // Whenever it's needed to get back to console || file
    public void _switch()
    {
        if (this._isFileStream)
        {
            System.setOut(System.out);
            this._isFileStream = false;
        } else {
            System.setOut(this._printStream);
            this._isFileStream = true;
        }
    }

    @Override
    public String getSessionName() 
    {
        return this._file.getName();
    }
    
    @Override
    public void setDebugMode(boolean _status)
    {
        
    }
}
