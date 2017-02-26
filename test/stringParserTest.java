/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Common.Utils.OutputNameFormatter;
import auresearch.FactoryHolder;
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
public class stringParserTest {
    
    public stringParserTest() {
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
    public void testNames()
    {
        long _start = System.nanoTime();
        for (String i: FactoryHolder._docNames)
            System.out.println(OutputNameFormatter.parseName(i));
        
        for (String i: FactoryHolder._graphNames)
            System.out.println(OutputNameFormatter.parseName(i));
        long _end = System.nanoTime();
        
        System.out.println("[+] Parsed, formatted & printed all strings in " + (_end - _start) / 1000000 + " ms.");
    }
}
