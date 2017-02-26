/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agents.Properties;

import auresearch.FactoryHolder;

/**
 *
 * @author d3vil401
 */

/*

    Every skill has different name but the same experience & grade logics.
    For now the grade & experience logic is implemented but NOT used.

*/
public class cSkill implements ISkill
{
    private String  _skillName = "";
    private int     _skillExp = 0;
    public boolean _initialized = false;
    
    @Override
    public String getName() 
    {
        return this._skillName;
    }

    @Override
    public void setName(String _name) 
    {
        this._skillName = _name;
    }

    @Override
    public void addExperience(int _expAmount) 
    {
        this._skillExp += _expAmount;
    }

    @Override
    public int getExperience() 
    {
        return this._skillExp;
    }

    @Override
    public void _tick() 
    {
        // TODO:
        // _tick is a function that performs all the automated tasks: checking if experience is enough for a new grade, etc...
        // it is perfect for a thread, but Skill is not an entity but a property.
        //int _currentGradeLimitExp = (int)FactoryHolder._configManager.getArrayValue("GRADE_TABLE").get(this._skillGrade);
        
        //if (this._skillExp > _currentGradeLimitExp)
        //{
        //    this._skillGrade++;
        //    this._skillExp = _currentGradeLimitExp - this._skillExp;
        //}
    }
    
    public cSkill(String _name)
    {
        this._skillName = _name;
    }
    
    public cSkill(String _name, int _exp)
    {
        this._skillName = _name;
        this._skillExp = _exp;
        this._initialized = true;
    }

    @Override
    public void setExperience(int _expAmount) 
    {
        this._skillExp = _expAmount;
    }
    
    public cSkill clone()
    {
        return new cSkill(this._skillName, this._skillExp);
    }
}
