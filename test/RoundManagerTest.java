/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Common.Configuration.ConfigManager;
import Common.Logging.cLogConsole;
import Environment.RoundManager;
import auresearch.FactoryHolder;
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
public class RoundManagerTest {
    
    public RoundManagerTest() {
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
    public void testIt() throws IOException
    {
        FactoryHolder._logManager = new cLogConsole();
        FactoryHolder._configManager = new ConfigManager();
        FactoryHolder._logManager.initializeSession("session");
        FactoryHolder._logManager.setDebugMode(false);
        
        RoundManager _rm = new RoundManager(100, 100, 85);
        _rm.runLoop();
        _rm.end();
    }
}
