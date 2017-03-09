/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agents;

import Agents.Properties.cSkill;
import Challenge.Challenge;
import Common.Logging.ILogManager;
import auresearch.FactoryHolder;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author d3vil401
 */
public class ProposerAgent 
{
    private Random _random = new Random();
    private Challenge _challengeProposed = null;
    private ArrayList<cSkill> _skills = new ArrayList<>();
    private boolean _lastSolved = false;
    public int _problemsSolvedAmount = 0;
    
    public boolean getLastSolved()
    {
        return this._lastSolved;
    }
    
    public void setLastSolved(boolean _status)
    {
        this._lastSolved = _status;
    }
    
    public void _generateProblem()
    {
        int[] _difficultyMap = new int[FactoryHolder._configManager.getArrayValue("AGENT_SKILLS").size()];
        ArrayList<cSkill> _requirements = new ArrayList<>();
        
        for (int i = 0; i < _difficultyMap.length; i++)
            _difficultyMap[i] = 0;
        
        for (int i = 0; i < FactoryHolder._configManager.getNumberValue("MAX_RANDOMIZED_DIFFICULTYMAP"); i++)
        {
            int _randomAccessSkill = this._random.nextInt(FactoryHolder._configManager.getArrayValue("AGENT_SKILLS").size());
            _difficultyMap[_randomAccessSkill] = this._random.nextInt(FactoryHolder._configManager.getNumberValue("REQUIREMENT_MAXIMUM_RANDOM_EXPERIENCE") + 1);
        }
        
        for (int i = 0; i < _difficultyMap.length; i++)
        {
            _requirements.add(new cSkill(FactoryHolder._configManager.getArrayValue("AGENT_SKILLS").get(i).toString()));
            _requirements.get(i).setExperience(_difficultyMap[i]);
        }
        
        this._challengeProposed = new Challenge(_requirements, _difficultyMap, false, this);
    }
    
    public Challenge getChallengeProposed()
    {
        return this._challengeProposed;
    }
    
    private ArrayList<cSkill> _mutateNegativeSkills()
    {
        int _factor = this._random.nextInt(FactoryHolder._configManager.getNumberValue("PA_MUTATION_RATE_VALUE") + 1);
        ArrayList<cSkill> _mutatedSkills = (ArrayList<cSkill>)this._skills.clone();
        
        for (int i = 0; i < this._skills.size(); i++)
            _mutatedSkills.get(i).setExperience((this._skills.get(i).getExperience() - _factor) 
                                              * (this._skills.get(i).getExperience() / 100));
        
        return _mutatedSkills;
    }
    
    private ArrayList<cSkill> _mutatePositiveSkills()
    {
        int _factor = this._random.nextInt(FactoryHolder._configManager.getNumberValue("PA_MUTATION_RATE_VALUE"));
        ArrayList<cSkill> _mutatedSkills = (ArrayList<cSkill>)this._skills.clone();
        
        for (int i = 0; i < this._skills.size(); i++)
            _mutatedSkills.get(i).setExperience((this._skills.get(i).getExperience() + _factor) 
                                              * (this._skills.get(i).getExperience() / 100));
        
        return _mutatedSkills;
    }
    
    private cSkill _mutateSkill(cSkill _oldSkill, String _rateoSign)
    {
        cSkill _newSkill = _oldSkill.clone();
        int _outExp = 0;
        int _curExp = _oldSkill.getExperience();
        
        if (_rateoSign.equals("+/-"))
        {
            boolean _sign = this._random.nextBoolean();
            if (_sign)
                _outExp = _curExp + (FactoryHolder._configManager.getNumberValue("MUTATION_RATE_VALUE") * _curExp / 100);
            else
                _outExp = _curExp - (FactoryHolder._configManager.getNumberValue("MUTATION_RATE_VALUE") * _curExp / 100);
        } else if (_rateoSign.equals("+")) {
            _outExp = _curExp + (FactoryHolder._configManager.getNumberValue("MUTATION_RATE_VALUE") * _curExp / 100);
        } else if (_rateoSign.equals("-")) {
            _outExp = _curExp - (FactoryHolder._configManager.getNumberValue("MUTATION_RATE_VALUE") * _curExp / 100);
        } else 
            FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_ERROR, "Rateo sign is not recognized.");
        
        _newSkill.setExperience(_outExp);
        return _newSkill;
    }
    
    public ProposerAgent clone()
    {
        ArrayList<cSkill> _newSet = new ArrayList<>();
        cSkill _newSkillSlot = null;
        
        return new ProposerAgent(this._skills);
    }
    
    public ProposerAgent()
    {
        
    }
    
    public ProposerAgent(ArrayList<cSkill> _skills)
    {
        this._skills = (ArrayList<cSkill>) _skills.clone();
    }
}
