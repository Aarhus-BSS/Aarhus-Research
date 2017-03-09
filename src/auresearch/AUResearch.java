/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auresearch;

import Common.Configuration.ConfigManager;
import Common.Logging.ILogManager;
import Common.Logging.cLogConsole;
import Graphics.CEditor;
import java.io.IOException;

/**
 *
 * @author d3vil401
 */
public class AUResearch 
{   
    public static void main(String[] args) throws IOException 
    {
        // First of all it is needed to register the logging system.
        FactoryHolder._logManager = new cLogConsole();
        FactoryHolder._logManager.initializeSession("session");
        
        if (args.length > 0) {
            if (args[0].compareToIgnoreCase("-editor") == 0)
            {
                FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_INFORMATION, "Configuration editor has been requested, firing up!");
                java.awt.EventQueue.invokeLater(new Runnable() 
                {
                    public void run() 
                    {
                        CEditor dialog = new CEditor();
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
                        //new CEditor().setVisible(true);
                    }
                });
            }
        } else {
            // We import the configuration stuff, it is fundamental to have this.
            FactoryHolder._configManager = new ConfigManager();
            if (FactoryHolder._configManager.hasError())
                return;

            //Mint.initAndStartSession(null, FactoryHolder._configManager.getStringValue("MINT_APIKEY"));
            // Import the grade table.
            //GradeTableConverter.setTable(FactoryHolder._configManager.getArrayValue("GRADE_TABLE"));

            // Override simulator initialization and function to the ModeSwitcher.
            ModeSwitcher.start(FactoryHolder._configManager.getStringValue("MODE"));
        }
    }
}
