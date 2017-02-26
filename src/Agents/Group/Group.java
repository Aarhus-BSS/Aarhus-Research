/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agents.Group;

import Agents.SolverAgent;
import Challenge.Challenge;
import auresearch.FactoryHolder;
import java.util.Random;

/**
 *
 * @author d3vil401
 */
public class Group 
{
    private SolverAgent[] _groupMembers = new SolverAgent[2];
    private Random _random = new Random();
    private Challenge _solvedChallenge = null;
    
    public Group(SolverAgent _a, SolverAgent _b)
    {
        this._groupMembers[0] = _a;
        this._groupMembers[1] = _b;
    }
    
    public boolean isInGroup(SolverAgent _agent)
    {
        for (SolverAgent _groupMember : this._groupMembers) 
            if (_groupMember.equals(_agent)) 
                return true;
            
        return false;
    }
    
    public boolean attemptSolve(Challenge _challenge)
    {
        for (int i = 0; i < _challenge.getDifficultyMap().length; i++)
        {
            int[] _expMap = new int[FactoryHolder._configManager.getArrayValue("AGENT_SKILLS").size()];
            
            for (int k = 0; k < FactoryHolder._configManager.getArrayValue("AGENT_SKILLS").size(); k++)
                _expMap[k] = this._groupMembers[0].getSkills().get(k).getExperience() + this._groupMembers[1].getSkills().get(k).getExperience();
            
            if ((double)(_challenge.getDifficultyMap()[i] / _expMap[i]) >= FactoryHolder._configManager.getFloatValue("DEADLINE")) 
            {
                this._solvedChallenge = _challenge;
                return true;
            }
        }
        
        return false;
    }
    
    public boolean hasSolvedAChallenge()
    {
        return this._solvedChallenge != null;
    }
    
    public Challenge getSolvedChallenge()
    {
        return this._solvedChallenge;
    }
    
    public SolverAgent[] getMembers()
    {
        return this._groupMembers;
    }
    
    public int getTotalExp()
    {
        int _total = 0;
        for (int k = 0; k < FactoryHolder._configManager.getArrayValue("AGENT_SKILLS").size(); k++)
                _total += this._groupMembers[0].getSkills().get(k).getExperience() + this._groupMembers[1].getSkills().get(k).getExperience();
        
        return _total;
    }
    
    // Give reward & percentage of participation.
}
