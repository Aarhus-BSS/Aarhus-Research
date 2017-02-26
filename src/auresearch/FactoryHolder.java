package auresearch;

import Common.Configuration.ConfigManager;
import Common.Logging.ILogManager;
import Common.Lua.LuaManager;
import Graphics.GraphicManager.GraphicManager;
import org.jfree.chart.ChartPanel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author d3vil401
 */
public class FactoryHolder 
{
    /*
    
        This class is supposed to hold global variables and classes.
    
    */
    // Logs
    public static ILogManager       _logManager = null;
    // Scripting Engine
    public static LuaManager        _luaManager = null;
    // Graphics generator engine
    public static GraphicManager    _graphicManager = new GraphicManager();
    // Configuration engine
    public static ConfigManager     _configManager = null;
    
    // Documentation (XLS & Graphics) are going to follow these names, pay attention to the flags {}
    public static String[] _docNames = {"ProblemsReport.{DATETIME}-{OS}", 
        "AgentReport.{DATETIME}-{OS}", "AVGReport.{DATETIME}-{OS}", 
        "DetailReport.{DATETIME}-{OS}", "CompositeReport.{DATETIME}-{OS}",
        "GroupReport.{DATETIME}-{OS}"};
    
    public static String[] _graphNames = {"AVGAgentsPerRound.{DATETIME}-{OS}.png", 
        "AgentsPerRound.{DATETIME}-{OS}.png", "SAgentsSolvedPerRound.{DATETIME}-{OS}.png", 
        "CompositeEXP.{DATETIME}-{OS}.png", "CompositeProblems.{DATETIME}-{OS}.png" };
    
    public static ChartPanel[] _graphsRender = null;
    
    public static Globals _GLOBALS;
    
    public static void destroy()
    {
        _logManager.destroySession();
    }
}
