/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Agents.ProposerAgent;
import Agents.SolverAgent;
import Challenge.Challenge;
import Common.Configuration.ConfigManager;
import Common.Logging.cLogConsole;
import Environment.cRound;
import auresearch.FactoryHolder;
import java.io.IOException;
import java.util.ArrayList;
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
public class roundTest {
    
    public roundTest() {
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
    public void roundRun() throws IOException
    {
        FactoryHolder._logManager = new cLogConsole();
        FactoryHolder._configManager = new ConfigManager();
        FactoryHolder._logManager.initializeSession("session");
        FactoryHolder._logManager.setDebugMode(false);
        
        ArrayList<SolverAgent> _solvers = new ArrayList<>();
        ArrayList<ProposerAgent> _proposers = new ArrayList<>();
        ArrayList<Challenge> _challenges = new ArrayList<>();
        ArrayList<cRound> _rounds = new ArrayList<>();
        int _roundCount = 0;
        int _roundLimit = 5;
        
        long _successingAgents = 0;
        long _totalAmountOfChallenges = 0;
        long _failingAgents = 0;
        long _average = 0;
        long _averageFailAgents = 0;
        long _totalChaExported = 0;
        
        System.out.println("[+] Initializing solvers...");
        for (int i = 0; i < 500; i++)
            _solvers.add(new SolverAgent());
        
        System.out.println("[+] Initializing proposers...");
        for (int i = 0; i < 100; i++)
            _proposers.add(new ProposerAgent());
        
        System.out.println("[+] Initializing Round...");
        
        long _start = System.nanoTime();
        long _perRound[] = new long[_roundLimit];
        long _averageFailingAgents[] = new long[_roundLimit];
        
        int _cIndex = 0;
        while (_roundCount < _roundLimit)
        {
            if (_roundCount == 0)
            {
                _rounds.add(new cRound(_roundCount, _solvers, _proposers));
            } else {
                _cIndex = 1;
                _challenges = _rounds.get(_roundCount - _cIndex).exportChallenges();
                _rounds.add(new cRound(_roundCount, _solvers, _proposers, _challenges));
            }
            
            _rounds.get(_roundCount).run();
            
            _totalAmountOfChallenges += _rounds.get(_roundCount).getChallanges().size();
            _successingAgents += _rounds.get(_roundCount).getSolvedAgents().size();
            _failingAgents += _rounds.get(_roundCount).getUnsolvedSAgents(_solvers).size();
            _totalChaExported += _rounds.get(_roundCount - _cIndex).exportChallenges().size();
            
            _perRound[_roundCount] = System.nanoTime() - _start;
            _averageFailingAgents[_roundCount] = _rounds.get(_roundCount).getUnsolvedSAgents(_solvers).size();
            
            System.out.println("[+] Currently on round " + ++_roundCount + "/" + _roundLimit + ".");
        }
        
        long _end = System.nanoTime();
        for (long i: _perRound) 
            _average += i;
        for (long i: _averageFailingAgents)
            _averageFailAgents += i;
        
        _average /= _perRound.length;
        _averageFailAgents /= _averageFailingAgents.length;
        
        System.out.println("[+] Elapsed time: " + (_end - _start) / 1000000 + " ms (" + (double)((_end - _start) / 1000000) / 1000 + " s).");
        System.out.println("[+] Total challenges generated: " + _totalAmountOfChallenges 
                         + "\n[+] Total Successing Agents: " + _successingAgents
                         + "\n[+] Total Failed Agents: " + _failingAgents 
                        // + "\n[+] Exported Challenges (eq. Generated): " + _totalChaExported
                         + "\n[+] Average Failing Agents: " + _averageFailAgents + "pR");
                         
        
    }
}
