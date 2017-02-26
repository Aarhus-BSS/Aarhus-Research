/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Common.MsgBoxer;

import javax.swing.JOptionPane;

/**
 *
 * @author d3vil401
 */

// Used to deliver a message box error.
public class MBox 
{
    public enum _MSGBOX_TYPE
    {
        TYPE_INFORMATION(1), TYPE_WARNING(2), TYPE_ERROR(0);
        private final int value;
        
        private _MSGBOX_TYPE(int value) 
        {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
    
    public static void showBox(String _msg, String _title, _MSGBOX_TYPE _type)
    {
        JOptionPane.showMessageDialog(null, _msg, _title, _type.getValue());
    }
}
