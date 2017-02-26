/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agents.Properties;

/**
 *
 * @author d3vil401
 */
public interface ISkill 
{
    public String getName();
    public void   setName(String _name);
    public void   addExperience(int _expAmount);
    public int    getExperience();
    public void   _tick();
    public void   setExperience(int _expAmount);
}
