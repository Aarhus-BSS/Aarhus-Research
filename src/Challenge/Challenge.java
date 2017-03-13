/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Challenge;

import Agents.Properties.cSkill;
import Agents.ProposerAgent;
import Agents.SolverAgent;
import Common.Logging.ILogManager;
import Common.Utils.GradeTableConverter;
import auresearch.FactoryHolder;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author d3vil401
 */
public class Challenge implements Comparable<Challenge>
{
    private ArrayList<cSkill> _skillTypes = new ArrayList<>();
    private int[] _difficultyMap = null;
    private int _reward = 0;
    private int _totalDifficulty = 0;
    private ProposerAgent _author = null;
    private Random _random = new Random();
    private ArrayList<SolverAgent> _solvers = new ArrayList();
    private ArrayList<SolverAgent> _tryHarders = new ArrayList<>();
    private ArrayList<SolverAgent> _tryHarderRejected = new ArrayList<>();
    private boolean _isSolved = false;
    public int _idledRounds = 0;
    
    // We check the difficulty map is correct (has maximum the same amount of values of the amount of skills registered)
    private int _getBound()
    {
        if (this._difficultyMap.length <= FactoryHolder._configManager.getArrayValue("AGENT_SKILLS").size())
            return this._difficultyMap.length;
        else
            return FactoryHolder._configManager.getArrayValue("AGENT_SKILLS").size();
    }
    
    private void _calculateTotalDifficulty()
    {
        for (int i = 0; i < this._getBound(); i++)
            this._totalDifficulty += this._difficultyMap[i];
        
    }
    
    public int getReward()
    {
        return this._reward;
    }
    
    public int getTotalDifficulty()
    {
        return this._totalDifficulty;
    }
    
    public int[] getDifficultyMap()
    {
        return this._difficultyMap;
    }
    
    public ProposerAgent getAuthor()
    {
        return this._author;
    }
    
    public ArrayList<SolverAgent> getSolver()
    {
        return this._solvers;
    }
    
    public boolean hasSolved(SolverAgent _agent)
    {
        for (int i = 0; i < this._solvers.size(); i++)
            if (this._solvers.get(i).equals(_agent))
                return true;
        
        return false;
    }
    
    public boolean isSolved()
    {
        return this._isSolved;
    }
    
    public void setSolvedStatus(boolean _status)
    {
        this._isSolved = _status;
    }
    
    public ArrayList<SolverAgent> getTryHarders()
    {
        return this._tryHarders;
    }
    
    public Challenge(ArrayList<cSkill> _requirements, int[] _difficulty, boolean _solved, ProposerAgent _author)
    {
        this._skillTypes = _requirements;
        
        if (_difficulty.length != FactoryHolder._configManager.getArrayValue("AGENT_SKILLS").size())
            FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_ERROR, "Difficulty map is different from skill count registered!");
        
        this._difficultyMap = _difficulty;
        
        this._calculateTotalDifficulty();
        
        this._reward = this._totalDifficulty / 2;
        
        this._author = _author;
    }
    
    private void _mutatePositive()
    {
        int _rate = this._random.nextInt(FactoryHolder._configManager.getNumberValue("CH_MUTATION_RATE") + 1);
        
        for (int i = 0; i < this._getBound(); i++)
            this._difficultyMap[i] += _rate * this._difficultyMap[i] / 100;
    }
    
    private void _mutateNegative()
    {
        int _rate = this._random.nextInt(FactoryHolder._configManager.getNumberValue("CH_MUTATION_RATE") + 1);
        
        for (int i = 0; i < this._getBound(); i++)
            this._difficultyMap[i] -= _rate * this._difficultyMap[i] / 100;
    }
    
    private void _mutateNegative(int _index) 
    {
        int _rate = this._random.nextInt(FactoryHolder._configManager.getNumberValue("CH_MUTATION_RATE") + 1);
        this._difficultyMap[_index] = (this._difficultyMap[_index] - _rate) * (this._difficultyMap[_index] / 100);
    }

    private void _mutatePositive(int _index) 
    {
        int _rate = this._random.nextInt(FactoryHolder._configManager.getNumberValue("CH_MUTATION_RATE") + 1);
        this._difficultyMap[_index] = (this._difficultyMap[_index] + _rate) * (this._difficultyMap[_index] / 100);
    }
    
    public void mutate()
    {
        if (FactoryHolder._configManager.getStringValue("CH_ENABLE_MUTATION").equals("true"))
        {
            boolean _sign = false;
            
            if (FactoryHolder._configManager.getStringValue("CH_MUTATION_SIGN").equals("+/-"))
            {
                for (int i = 0; i < this._getBound(); i++)
                {
                    _sign = this._random.nextBoolean();
                
                    if (_sign)
                        this._mutatePositive(i);
                    else
                        this._mutateNegative(i);
                }
            } else if (FactoryHolder._configManager.getStringValue("CH_MUTATION_SIGN").equals("-"))
                this._mutateNegative();
            else if (FactoryHolder._configManager.getStringValue("CH_MUTATION_SIGN").equals("+"))
                this._mutatePositive();
            else
                FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_ERROR, "Mutation sign for challenge is unrecognized.");
        }
        this._calculateTotalDifficulty();
        this._reward = this._totalDifficulty / 2;
        this._solvers.clear();
        this._tryHarders.clear();
        this._isSolved = false;
        this._idledRounds = 0;
    }
    
    public Challenge giveMutate()
    {
        this.mutate();
        return this;
    }

    @Override
    public int compareTo(Challenge _challenge) 
    {
        return this._totalDifficulty - _challenge._totalDifficulty;
    }
    
    // I moved the trySolve method from the agent to the problem, because it's an agent to try to solve a problem
    // and not a problem given to the agent (since it's the agent to decide wheter reject it or try).
    public boolean attemptSolve(SolverAgent _agent)
    {
        double _chance = 1.0;
        double _randomer = 0.0;
        
        for (int i = 0; i < this._getBound(); i++)
        {
            _randomer = this._random.nextDouble();
            if (_agent.getSkill(this._skillTypes.get(i).getName()).getExperience() != 0)
            {
                    if ((_agent.getSkill(this._skillTypes.get(i).getName()).getExperience()
                            - this._difficultyMap[i])
                            >= FactoryHolder._configManager.getNumberValue("CH_MINIMAL_DIFFERENCE")
                            && !FactoryHolder._configManager.getStringValue("CH_EASYREJECTOR").equals("true"))
                    {
                        /*
                        this._tryHarders.add(_agent);
                        
                        double _difference = (double)(
                                    (_agent.getSkill(this._skillTypes.get(i).getName()).getExperience())
                                    - this._difficultyMap[i]
                                ) / 100;
                        
                        _chance *= _difference;
                        
                        if ((_randomer < _chance)) 
                        {
                            _agent._incrementRejected();
                            this._tryHarderRejected.add(_agent);
                            return false;
                        }
                        */
                        this._solvers.add(_agent);
                        this._tryHarders.remove(_agent);
                        _agent.giveReward(this._reward);
                        this._author.setLastSolved(true);
                        this._isSolved = true;
                        
                        return true;
                    }
            }
        }
        
        return false;
    }
    
    public void forceSolver(SolverAgent _agent)
    {
        this._solvers.add(_agent);
    }
    
    public void forceAssignSuccess(SolverAgent _agent)
    {
        this._isSolved = true;
        this.forceSolver(_agent);
        this._tryHarders.remove(_agent);
        _agent.giveReward(this._reward);
        this._author.setLastSolved(true);
    }
    
    public String getCompositeString()
    {
        double compositeCounter = 0.0D;
        
        for (int i = 0; i < this._difficultyMap.length; i++)
            compositeCounter += (double)this._difficultyMap[i];
        
        return String.valueOf(compositeCounter / this._difficultyMap.length);
    }
     
     
}
