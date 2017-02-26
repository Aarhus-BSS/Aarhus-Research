/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Common.Logging;

/**
 *
 * @author d3vil401
 */

/*

    Interface used for customizable implementations of the log handling
    System.

*/
public interface ILogManager 
{
    public enum _LOG_TYPE
    {
        TYPE_NORMAL,
        TYPE_INFORMATION,
        TYPE_WARNING,
        TYPE_ERROR,
        TYPE_CRITICAL,
        TYPE_DEBUG
    };
    
    public void initializeSession(String _fileName);
    public void print(_LOG_TYPE _type, String _msg);
    public void destroySession();
    public String getSessionName();
    public void setDebugMode(boolean _status);
}
