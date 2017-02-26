/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auresearch;

import Common.Logging.ILogManager;
import Graphics.AgentInfoView;
import Graphics.MainMenu;

/**
 *
 * @author d3vil401
 */
public class ModeSwitcher 
{
    // There are 3 different modes, as we already discussed during the meeting.
    public enum Mode
    {
        MODE_LIBRARY("library"), MODE_SERVER("server"), MODE_CONSOLE("console");
        
        private String _mode;
        
        private Mode(final String _mode) 
        {
            this._mode = _mode;
        }
        
        @Override
        public String toString() 
        {
            return this._mode;
        }
    }
    
    // Checks which mode has been specified inside the configuration file, loweing the cases for a better general comparation.
    public static void start(String _mode)
    {
        if (_mode.toLowerCase().equals(Mode.MODE_LIBRARY.toString()))
            _startAsLibrary();
        else if (_mode.toLowerCase().equals(Mode.MODE_SERVER.toString()))
            _startAsServer();
        else if (_mode.toLowerCase().equals(Mode.MODE_CONSOLE.toString()))
            _startAsConsole();
        else
            FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_CRITICAL, "Unknown mode " + _mode);
    }
    
    // Start the simulator as "library" (GUI Application)
    private static void _startAsLibrary()
    {
        FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_NORMAL, "Opening main form...");
        java.awt.EventQueue.invokeLater(new Runnable() 
        {
            public void run() 
            {
                MainMenu dialog = new MainMenu(new javax.swing.JFrame(), true);
                dialog.setLocationRelativeTo(null);

                dialog.addWindowListener(new java.awt.event.WindowAdapter() 
                {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) 
                    {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
        /*
        java.awt.EventQueue.invokeLater(new Runnable() 
        {
            public void run() 
            {
                AgentInfoView dialog = new AgentInfoView(new javax.swing.JFrame(), true);
                dialog.setLocationRelativeTo(null);

                dialog.addWindowListener(new java.awt.event.WindowAdapter() 
                {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) 
                    {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
*/
    }
    
    // As console, the one we said we just start and outputs the graphics via the bash script.
    private static void _startAsConsole()
    {
        FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_CRITICAL, "Mode console has not been implemented yet.");
    }
    
    // As HTTP server, not going to be developed for a while.
    private static void _startAsServer()
    {
        FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_CRITICAL, "Mode server has not been implemented yet.");
    }
}
