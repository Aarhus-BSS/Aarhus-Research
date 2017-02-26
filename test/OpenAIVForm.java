/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Graphics.AgentInfoView;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author d3vil401
 */
public class OpenAIVForm {
    
    
    public OpenAIVForm() 
    {
        
    }
    
    @Test
    public void openForm()
    {
        java.awt.EventQueue.invokeLater(new Runnable() 
        {
            public void run() 
            {
                AgentInfoView dialog2 = new AgentInfoView(new javax.swing.JFrame(), true);
                dialog2.setLocationRelativeTo(null);

                dialog2.addWindowListener(new java.awt.event.WindowAdapter() 
                {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) 
                    {
                        System.exit(0);
                    }
                });
                dialog2.setVisible(true);
            }
        });
    }
}
