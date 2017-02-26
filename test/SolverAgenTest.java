/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Agents.SolverAgent;
import Common.Configuration.ConfigManager;
import Common.Logging.cLogConsole;
import Common.Utils.GradeTableConverter;
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
public class SolverAgenTest 
{
    private SolverAgent _agent;
    
    public SolverAgenTest() throws IOException 
    {
        
    }
    
    @BeforeClass
    public static void setUpClass() 
    {
        
    }
    
    @AfterClass
    public static void tearDownClass() 
    {
        
    }
    
    @Before
    public void setUp() 
    {
        
    }
    
    @After
    public void tearDown() 
    {
        
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    @Test
    public void mutationTest() throws IOException
    {
        FactoryHolder._logManager = new cLogConsole();
        FactoryHolder._configManager = new ConfigManager();
        FactoryHolder._logManager.initializeSession("session");
        //GradeTableConverter.setTable(FactoryHolder._configManager.getArrayValue("GRADE_TABLE"));
        _agent = new SolverAgent();
        
        // BUG: Clone agent gives 0 experience despite it shouldn't.
        SolverAgent _newAgent = _agent.clone();
        for (int i = 0; i < _newAgent.getSkills().size(); i++)
        {
            System.out.println("["+ _newAgent.getSkills().get(i).getName() + 
                    "] Exp " + _newAgent.getSkills().get(i).getExperience() + ".");
        }
        
        System.out.println("Difference: " + _agent.compareTo(_newAgent));
    }
}
