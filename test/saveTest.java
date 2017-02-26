/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import SessionManager.SaveManager.SaveManager;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author d3vil401
 */
public class saveTest 
{
    
    public saveTest() 
    {
        
    }
    
    @Test
    public void save()
    {
        SaveManager.createFile("testSave");
        SaveManager.compile();
    }
}
