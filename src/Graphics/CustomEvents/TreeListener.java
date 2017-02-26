/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics.CustomEvents;

import Graphics.AgentInfoView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.util.logging.Logger;
import javax.swing.JTree;
import javax.swing.Timer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

/**
 *
 * @author anymol
 * http://stackoverflow.com/questions/12847904/double-click-a-jtree-node-and-get-its-name
 */
public class TreeListener extends MouseAdapter
{
    private JTree _Tree;
    private boolean singleClick  = true;
    private int doubleClickDelay = 300;
    private Timer timer;    

    public TreeListener(JTree tree)
    {
        this._Tree = tree;
        ActionListener actionListener = new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {                
                timer.stop();
                if (singleClick)
                    singleClickHandler(e);
                else
                    try 
                    {
                        doubleClickHandler(e);
                    } catch (ParseException ex) {
                        Logger.getLogger(ex.getMessage());
                    }
            }
        }; 
        timer = new javax.swing.Timer(doubleClickDelay, actionListener);
        timer.setRepeats(false);
    }

    public void mouseClicked(MouseEvent e) 
    { 
        if (e.getClickCount() == 1) 
        {
            singleClick = true;
            timer.start();
        } else
            singleClick = false;
    }

    private void singleClickHandler(ActionEvent e) 
    {
       //System.out.println("-- single click --");
    }

     private void doubleClickHandler(ActionEvent e) throws ParseException 
     {         
        
        TreePath _tPath = this._Tree.getSelectionPath();
        if (_tPath != null)
        {
            DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) (_tPath.getLastPathComponent());
            MutableTreeNode parent = (MutableTreeNode) (currentNode.getParent());
            //if (parent != null)
            //{
            //    this._Tree.remove(_tPath);
            //}
            
            
        }
        
        java.awt.EventQueue.invokeLater(new Runnable() 
        {
            public void run() 
            {
                AgentInfoView dialog = new AgentInfoView(new javax.swing.JFrame(), true);
                dialog.setLocationRelativeTo(null);
                dialog.setAlwaysOnTop(true);
                dialog.setAutoRequestFocus(true);
                dialog.toFront();

                dialog.addWindowListener(new java.awt.event.WindowAdapter() 
                {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) 
                    {
                        dialog.dispose();
                    }
                });
                dialog.setVisible(true);
            }
        });
    }     
}