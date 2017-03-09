/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Environment;

import Agents.Group.Group;
import Agents.Properties.cSkill;
import Agents.ProposerAgent;
import Agents.SolverAgent;
import Challenge.Challenge;
import Common.Logging.ILogManager;
import auresearch.FactoryHolder;
import java.util.Random;
import java.security.SecureRandom;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 *
 * @author d3vil401
 */
public class cRound implements IRound
{
    // Every round has an ID, better trace when we're going to list runtime traces.
    private String              _id = null;
    private Random              _random = new Random();
    private int                 _roundIndex = -1;
    // Every round has a token.
    private SecureRandom        _srandom = new SecureRandom();
    private boolean             _eradicated = false;
    
    private ArrayList<SolverAgent> _sAgents = new ArrayList<>();
    private ArrayList<ProposerAgent> _pAgents = new ArrayList<>();
    private Iterator<ProposerAgent> _pAIter = null;
    // Need to preserve instance.
    private ArrayList<SolverAgent> _deadSAgents = new ArrayList<>();
    private ArrayList<ProposerAgent> _deadPAgents = new ArrayList<>();
    private ArrayList<Challenge> _deadChallenges = new ArrayList<>();
    // May be a good idea preserve groups
    private ArrayList<Group> _groupPool = new ArrayList<>();
    
    private ArrayList<Challenge> _challenge = new ArrayList<>();
    public  roundStatsHolder _stats = new roundStatsHolder();
    
    private void _pGenerationChanceStep()
    {
        int _curChance = this._random.nextInt(100);
        if (_curChance < FactoryHolder._configManager.getNumberValue("PA_EXPONENTIAL_GENERATION_CHANCE"))
        {
            if (!this._pAgents.isEmpty())
            {
                FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_DEBUG, "New pagent clone creation:");
                int _randomPick = this._random.nextInt(this._pAgents.size());
                this._pAgents.add(this._pAgents.get(_randomPick).clone());
                this._pAgents.get(this._pAgents.size() - 1)._generateProblem();
                this._challenge.add(this._pAgents.get(this._pAgents.size() - 1).getChallengeProposed());
                FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_DEBUG, this._pAgents.get(this._pAgents.size() - 1).toString());   
            } else
                FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_DEBUG, "Trying to clone a pagent, but there aren't...");
        }
    }
    
    public ArrayList<SolverAgent> getDeadSolvers()
    {
        return this._deadSAgents;
    }
    
    private void _sGenerationChanceStep()
    {
        int _curChance = this._random.nextInt(100);
        if (_curChance < FactoryHolder._configManager.getNumberValue("SA_EXPONENTIAL_GENERATION_CHANCE"))
        {
            if (!this._sAgents.isEmpty())
            {
                FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_DEBUG, "New sagent clone creation:");
                int _randomPick = this._random.nextInt(this._sAgents.size());
                if (FactoryHolder._configManager.getStringValue("SA_ENABLE_MAX_CLONATION").equals("true")) {
                    if (this._sAgents.get(_randomPick).getStats()._clonedTimes <= FactoryHolder._configManager.getNumberValue("SA_MAX_CLONATIONS")) {
                        this._sAgents.add(this._sAgents.get(_randomPick).clone());
                    } else {
                        FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_DEBUG, "Max clonations for " + this._sAgents.get(_randomPick) + " reached, skipping.");
                    }
                } else {
                    this._sAgents.add(this._sAgents.get(_randomPick).clone());
                }
            } else
                FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_DEBUG, "Trying to clone a sagent, but there aren't...");
        }
    }
    
    public cRound(int _round, ArrayList<SolverAgent> _sAPool, ArrayList<ProposerAgent> _pAPool)
    {
        this.setID(new BigInteger(130, this._srandom).toString(32));
        this.setRoundIndex(_round);
        this.createRound();
        
        // DO NOT CLONE, My bad we had to keep edited agents list, not the original!
        this._sAgents = _sAPool;//(ArrayList<SolverAgent>) _sAPool.clone();
        this._pAgents = _pAPool;//(ArrayList<ProposerAgent>) _pAPool.clone();
        this._pAIter = this._pAgents.iterator();
        
        // Reset agents statuses
        this._sAgents.forEach((i) -> { i.resetForNewRound(); });
        
        while (this._pAIter.hasNext())
        {
            ProposerAgent _tmp = this._pAIter.next();
            _tmp._generateProblem();
            
            this._challenge.add(_tmp.getChallengeProposed());
        }
    }
    
    public ArrayList<Challenge> exportChallenges()
    {
        return this._challenge;
    }
    
    public cRound(int _round, ArrayList<SolverAgent> _sAPool, ArrayList<ProposerAgent> _pAPool, ArrayList<Challenge> _challenges)
    {
        this._sAgents = _sAPool;
        this._pAgents = _pAPool;
        this._challenge = _challenges;
        this._sAgents.forEach((i) -> { i.resetForNewRound(); });
        
        for (int i = 0; i < FactoryHolder._configManager.getNumberValue("MORTALITY_RATE"); i++)
            if (this._random.nextBoolean())
                // old code reference is a == 1
                if (this._sAgents.isEmpty())
                    this._eradicated = true;
                else {
                    SolverAgent _tmp = this._sAgents.get(this._random.nextInt(this._sAgents.size()));
                    this._deadSAgents.add(_tmp);
                    this._sAgents.remove(_tmp);
                }
            else 
                if (this._pAgents.isEmpty())
                    this._eradicated = true;
                else {
                    ProposerAgent _tmp = this._pAgents.get(this._random.nextInt(this._pAgents.size()));
                    this._deadPAgents.add(_tmp);
                    this._pAgents.remove(_tmp);
                }
        for (int i = 0; i < this._challenge.size(); i++)
            if (!this._pAgents.contains(this._challenge.get(i).getAuthor())) 
            {
                Challenge _tmp = this._challenge.get(i);
                this._deadChallenges.add(_tmp);
                this._challenge.remove(i);
            } else if (this._challenge.get(i).isSolved())
                this._challenge.get(i).mutate();
        
        if (!this._eradicated)
            this._pGenerationChanceStep();
        
        for (int i = 0; i < FactoryHolder._configManager.getNumberValue("PA_EXPONENTIAL_GENERATION_CHANCE"); i++)
            this._pGenerationChanceStep();
        
        this._checkRageQuitters();
    }
    
    public cRound(cSkill _type, int _round)
    {
        this.setID(new BigInteger(130, this._srandom).toString(32));
        this.setRoundIndex(_round);
    }
    
    public ArrayList<Group> getGroups()
    {
        return this._groupPool;
    }
    
    public void _checkRageQuitters()
    {
        int _removedAgents = 0;
        int _removedChallenges = 0;
        
        for (int i = 0; i < this._sAgents.size(); i++)
            if (this._sAgents.get(i).getStats()._idledRounds > FactoryHolder._configManager.getNumberValue("SA_MAX_IDLED_ROUNDS"))
            {
                this._deadSAgents.add(this._sAgents.get(i));
                this._sAgents.remove(i);
                _removedAgents++;
            }
        
        for (int i = 0; i < this._challenge.size(); i++)
            if (this._challenge.get(i)._idledRounds > FactoryHolder._configManager.getNumberValue("CH_MAX_IDLE_ROUNDS"))
            {
                ProposerAgent _reference = this._challenge.get(i).getAuthor();
                this._deadPAgents.add(_reference);
                this._pAgents.remove(_reference);
                this._deadChallenges.add(this._challenge.get(i));
                this._challenge.remove(i);
                _removedChallenges++;
            }
        
        FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_DEBUG, "Removed " + _removedAgents + " agents for rage quitting.");
        FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_DEBUG, "Removed " + _removedChallenges + " challenges for community's incompetency.");
    }
    
    public void run()
    {
        if (!this._eradicated)
        {
            for (int i = 0; i < FactoryHolder._configManager.getNumberValue("SA_EXPONENTIAL_GENERATION_CHANCE"); i++)
                this._sGenerationChanceStep();
        
            if (FactoryHolder._configManager.getStringValue("GAME_TYPE").equals("sorted"))
            {
                Collections.sort(this._challenge);
                Collections.sort(this._sAgents);
                this._match();
                
            } else if (FactoryHolder._configManager.getStringValue("GAME_TYPE").equals("random")) {
                FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_ERROR, "Random based game mode is not enabled in this version.");
            } else if (FactoryHolder._configManager.getStringValue("GAME_TYPE").equals("skill_based")) {
                FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_ERROR, "Skill based game mode is not enabled in this version.");
            } else {
                FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_ERROR, "No valid game type selected, aborting.");
            }
        }
    }
    
    private void _match()
    {
        int _trials = 0;
        ArrayList<Challenge> _touchedChallenges = (ArrayList<Challenge>)this._challenge.clone();
        
        while (!this.getUnsolvedChallenges(this._challenge).isEmpty()
                && !this.getUnsolvedSAgents(this._sAgents).isEmpty()
                && _trials < FactoryHolder._configManager.getNumberValue("NUMBER_OF_CHANCES_AN_AGENT_HAS_TO_TRY_TO_FIND_A_PROBLEM"))
        {
            for (int i = 0, k = 0; i < this._challenge.size() && k < this._sAgents.size();) {
                if (this._challenge.get(i).isSolved() && this._sAgents.get(k).getHasSolvedLastChallenge())
                {
                    i++; k++;
                } else if (!this._challenge.get(i).isSolved() && this._sAgents.get(k).getHasSolvedLastChallenge()) {
                    k++;
                } else if (this._challenge.get(i).isSolved() && !this._sAgents.get(k).getHasSolvedLastChallenge()) {
                    i++;
                } else if (!this._challenge.get(i).isSolved() && !this._sAgents.get(k).getHasSolvedLastChallenge()) {
                    if (this._canProceedWithChallenge(this._sAgents.get(k), this._challenge.get(i)))
                    {
                        if (this._challenge.get(i).attemptSolve(this._sAgents.get(k)))
                        {
                            if (!FactoryHolder._configManager.getStringValue("CH_EASYREJECTOR").equals("true"))
                            {
                                this._challenge.get(i).forceAssignSuccess(this._sAgents.get(k));
                                this._sAgents.get(k).setSolvedLastChallenge(true);
                            } else {
                                double _rejectDifference = (Double.parseDouble(this._sAgents.get(k).compositeExperience()) 
                                                            - Double.parseDouble(this._challenge.get(i).getCompositeString()));
                                
                                if (this._random.nextInt((int)(Double.parseDouble(this._sAgents.get(k).compositeExperience()) + 1)) >= _rejectDifference)
                                {
                                    this._challenge.get(i).forceAssignSuccess(this._sAgents.get(k));
                                    this._sAgents.get(k).setSolvedLastChallenge(true);
                                } else {
                                    this._sAgents.get(k).setTryHarder(this._sAgents.get(k).getTryHarded() + 1);
                                    i++; k++;
                                }
                            }
                        } else {
                            this._sAgents.get(k).setTryHarder(this._sAgents.get(k).getTryHarded() + 1);
                            i++; k++;
                        }
                    } else {
                        i++;
                        this._sAgents.get(k).getStats()._idledRounds++;
                    }
                }
            }
            _trials++;
        }
        
        if (FactoryHolder._configManager.getStringValue("ENABLE_MAX_IDLED_ROUNDS_CHALLENGE").equals("true"))

            for (int i = 0; i < this._challenge.size(); i++)
                if (_touchedChallenges.get(i).equals(this._challenge.get(i)))
                {
                    if (_touchedChallenges.get(i).isSolved() == this._challenge.get(i).isSolved())
                        this._challenge.get(i)._idledRounds++;
                } else
                    FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_ERROR, "We're not talking about the same challenge here...");
        
        /*
        
        _trials = 0;
        while (this.checkUnsolvedPresence(this._challenge)
                && !this.getUnsolvedSAgents(this._sAgents).isEmpty()
                && _trials < FactoryHolder._configManager.getNumberValue("NUMBER_OF_CHANCES_A_GROUP_HAS_TO_TRY_TO_FIND_A_PROBLEM"))
        {
            ArrayList<Challenge> _unsolvedChallenges = this.getUnsolvedChallenges(this._challenge);
            
            for (Challenge i: _unsolvedChallenges)
            {
                ArrayList<SolverAgent> _skilledAgents = this.getSkilledSAgents(this.getUnsolvedSAgents(this._sAgents), i);
                try 
                {
                    for (int _agCounter = 0; _agCounter < _skilledAgents.size() && _agCounter % 2 == 0 || _agCounter == 0; _agCounter += 2)
                    {
                        this._groupPool.add(new Group(_skilledAgents.get(_agCounter), _skilledAgents.get(_agCounter + 1)));
                        if (this._groupPool.get(this._groupPool.size() - 1).attemptSolve(i)) // Get previously created group.
                        {
                            FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_DEBUG, "Group " + this._groupPool.get(this._groupPool.size() - 1).toString() + " has solved the challenge.");
                            i.setSolvedStatus(true);
                            i.forceAssignSuccess(_skilledAgents.get(_agCounter));
                            i.forceAssignSuccess(_skilledAgents.get(_agCounter + 1));
                        }
                    }
                } catch (Exception ex) {
                    FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_DEBUG, "Exception thrown in group formation loop: " + ex.getMessage());
                }
            }
            _trials++;
        }
        */
    }
    
    private boolean _canProceedWithChallenge(SolverAgent _agent, Challenge _skillMap)
    {
        for (int k = 0; k < FactoryHolder._configManager.getArrayValue("AGENT_SKILLS").size(); k++)
            if (!(_agent.getSkills().get(k).getExperience() > _skillMap.getDifficultyMap()[k]))
                return false;
        
        return _agent.getTryHarded() <= FactoryHolder._configManager.getNumberValue("NUMBER_OF_TRIALS_FOR_SINGLE_AGENT_SOLVING");
    }
    
    @Override
    public IRound createRound() 
    {
        return null;
    }

    @Override
    public void setID(String _id) 
    {
        this._id = _id;
    }

    @Override
    public String getID() 
    {
        return this._id;
    }

    @Override
    public int getRoundIndex() 
    {
        return this._roundIndex;
    }

    @Override
    public void setRoundIndex(int _round) 
    {
        this._roundIndex = _round;
    }
    
    public ArrayList<ProposerAgent> getRemovedPAgents(ArrayList<ProposerAgent> _agents, int _count)
    {
        if (_count >= FactoryHolder._configManager.getNumberValue("REMOVAL_SOLVER_TRESHOLD"))
            if (_agents.size() > 1)
                for (int i = 0; i < _agents.size(); i++)
                    if (_agents.get(i).getLastSolved()) {
                        ProposerAgent _tmp = _agents.get(i);
                        this._deadPAgents.add(_tmp);
                        _agents.remove(i);
                    }
        
        return _agents;
    }
    
    public ArrayList<SolverAgent> getRemovedSAgents(ArrayList<SolverAgent> _agents, int _count)
    {
        if (_count >= FactoryHolder._configManager.getNumberValue("REMOVAL_SOLVER_TRESHOLD"))
            if (_agents.size() > 0)
                for (int i = 0; i < _agents.size(); i++)
                    if (!_agents.get(i).getHasSolvedLastChallenge()
                            || _agents.get(i).getTryHarded() >= FactoryHolder._configManager.getNumberValue("REMOVAL_SOLVER_TRESHOLD"))
                    {
                        SolverAgent _tmp = _agents.get(i);
                        this._deadSAgents.add(_tmp);
                        _agents.remove(i);
                    }
        
        return _agents;
    }
    
    public ArrayList<SolverAgent> getSkilledSAgents(ArrayList<SolverAgent> _sAgents, Challenge _challenge)
    {
        ArrayList<SolverAgent> _nSAgentsList = new ArrayList<>();
        int _score = 0;
        long _start = 0, _end = 0;
        
        _start = System.nanoTime();
        for (int i = 0; i < _sAgents.size(); i++)// && _nSAgentsList.size() <= 2; i++) 
        {
            for (int k = 0; k < FactoryHolder._configManager.getArrayValue("AGENT_SKILLS").size(); k++)
                if (_sAgents.get(i).getSkills().get(k).getExperience() >= _challenge.getDifficultyMap()[k])
                    _score++;
            
            if (_score == FactoryHolder._configManager.getArrayValue("AGENT_SKILLS").size())
                _nSAgentsList.add(_sAgents.get(i));
            _score = 0;
        }
        
        _end = System.nanoTime();
        FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_DEBUG, "getSkilledSAgents took " + (_end - _start) + " nseconds.");
        
        return _nSAgentsList;
    }
    
    public ArrayList<SolverAgent> getUnsolvedSAgents(ArrayList<SolverAgent> _sAgents)
    {
        ArrayList<SolverAgent> _nSAgentsList = new ArrayList<>();
        for (SolverAgent i: _sAgents)
            if (!i.getHasSolvedLastChallenge())
                _nSAgentsList.add(i);
        
        return _nSAgentsList;
    }
    
    public ArrayList<Challenge> getUnsolvedChallenges(ArrayList<Challenge> _challenges)
    {
        ArrayList<Challenge> _nChallengeList = new ArrayList<>();
        for (Challenge i: _challenges)
            if (!i.isSolved())
                _nChallengeList.add(i);
        
        return _nChallengeList;
    }
    
    public boolean checkUnsolvedAgentsPresence(ArrayList<SolverAgent> _sAgents)
    {
        for (SolverAgent i: _sAgents)
            if (i.getTryHarded() < FactoryHolder._configManager.getNumberValue("MAX_TRIALS_BEFORE_KICKOUT"))
                if (!i.getHasSolvedLastChallenge())
                    return true;
        
        return false;
    }
    
    public ArrayList<SolverAgent> getSolvedAgents()
    {
        ArrayList<SolverAgent> _agents = new ArrayList<>();
        
        for (SolverAgent i: this._sAgents)
                if (i.getHasSolvedLastChallenge())
                    _agents.add(i);
        
        return _agents;
    }
    
    public ArrayList<SolverAgent> getSolverAgents()
    {
        return this._sAgents;
    }
    
    public ArrayList<ProposerAgent> getProposerAgents()
    {
        return this._pAgents;
    }
    
    public void setProposerAgents(ArrayList<ProposerAgent> _pAPool)
    {
        this._pAgents = (ArrayList<ProposerAgent>) _pAPool.clone();
    }
    
    public void setSolverAgents(ArrayList<SolverAgent> _sAPool)
    {
        this._sAgents = (ArrayList<SolverAgent>) _sAPool.clone();
    }
    
    public boolean checkUnsolvedPresence(ArrayList<Challenge> _challenges)
    {
        for (Challenge i: _challenges)
            if (!i.isSolved())
                return true;
        
        return false;
    }

    public void setChallenges(ArrayList<Challenge> _challenges) 
    {
        this._challenge = (ArrayList<Challenge>) _challenges.clone();
    }
    
    public ArrayList<Challenge> getChallanges()
    {
        return this._challenge;
    }
}
