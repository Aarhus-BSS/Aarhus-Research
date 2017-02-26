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

    There're 2 kinds of logging systems, this one is on console.

*/
public class cLogConsole implements ILogManager
{
    private static boolean ENABLE_DEBUG = false;
    
    @Override
    public void initializeSession(String _fileName) 
    {
        // Console logs does not require any file name.
    }

    @Override
    public void print(_LOG_TYPE _type, String _msg) 
    {
        switch (_type)
        {
            case TYPE_NORMAL:
                System.out.printf("[+] " + _msg + "\n");
                break;
            case TYPE_INFORMATION:
                System.out.printf("[-] " + _msg + "\n");
                break;
            case TYPE_WARNING:
                System.out.printf("[*] " + _msg + "\n");
                break;
            case TYPE_ERROR:
                System.out.printf("[!] " + _msg + "\n");
                break;
            case TYPE_DEBUG:
                if (ENABLE_DEBUG)
                    System.out.printf("[ ] " + _msg + "\n");
                break;
            default:
                System.out.printf("[UNK] " + _msg + "\n");
                break;
        }
    }

    @Override
    public void destroySession() 
    {
        // Console one does not need a destroyed session.
    }

    @Override
    public String getSessionName() 
    {
        return "_console_";
    }
    
    @Override
    public void setDebugMode(boolean _status)
    {
        ENABLE_DEBUG = _status;
    }
    
}
