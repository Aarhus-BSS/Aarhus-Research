/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Common.Configuration.ConfigManager;
import Common.Logging.cLogConsole;
import Graphics.GraphicManager.ReportManager.ReportCompiler;
import auresearch.FactoryHolder;
import java.io.File;
import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author d3vil401
 */
public class reporTest {
    
    public reporTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    @Test
    public void writeDummyReport() throws IOException
    {
        FactoryHolder._logManager = new cLogConsole();
        FactoryHolder._configManager = new ConfigManager();
        FactoryHolder._logManager.initializeSession("session");
        
        String _reportName = "testReport";
        
        ReportCompiler _report = new ReportCompiler(_reportName);
        
        _report.createReport(new Object[] {"Index", "Name", "Surname"});
        _report.addContent(new Object[] {1, "Luca", "Francioni"});
        _report.addContent(new Object[] {2, "Lars", "Bach"});
        _report.addContent(new Object[] {3, "Dorthe", "Danish Surname"});
        _report.addContent(new Object[] {1337, "1337", "ub3r"});
        
        _report.end();
        
        File f = new File(FactoryHolder._configManager.getStringValue("REPORT_OUTPUT_FOLDER") + _reportName + ".xls");
        if(!f.exists() && f.isDirectory()) 
            fail();
    }
}
