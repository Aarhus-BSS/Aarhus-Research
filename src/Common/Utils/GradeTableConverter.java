/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Common.Utils;

import java.util.ArrayList;

/**
 *
 * @author d3vil401
 */

/*

    Every agent has a set of skills which increase with challenge resolution (or lost with reduced income)

*/
public class GradeTableConverter 
{
    private static int[] _rawTable = null;
    
    public GradeTableConverter()
    {
        
    }
    
    // Transforms the table into a raw int array and sets the lower and upper bounds to avoid null pointer exceptions and
    // in case of an agent reached maximum level can still be traceable in the grade table.
    public static void setTable(ArrayList<String> _table)
    {
        _rawTable = new int[_table.size() + 2];
        _rawTable[0] = 0;
        
        for (int i = 1; i < _table.size(); i++)
            _rawTable[i] = Integer.parseInt(_table.get(i));
        
        _rawTable[_table.size() + 1] = 0xFFFF;
    }
    
    public static int getMaximumGrade()
    {
        return _rawTable.length;
    }
    
    private static boolean check(int lowBound, int value, int highBound) 
    {
        return lowBound <= value && value <= highBound;
    }
    
    // Converts total experience into grade, referring to the table.
    public static int expToGrade(int _exp)
    {
        for (int i = 1; i < _rawTable.length; i++)
            if (check(_rawTable[i - 1], _exp, _rawTable[i + 1]))
                return i;
        
        return -0xFFFF;
    }
    
    // Inverse of what described above.
    public static int gradeToExp(int _grade)
    {
        if (_grade <= _rawTable.length - 1)
            return _rawTable[_grade];
        else
            return _rawTable[_rawTable.length - 1];
    }
}
