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
import Common.Utils.OutputNameFormatter;
import Graphics.GraphicManager.ReportManager.ReportCompiler;
import auresearch.FactoryHolder;
import java.util.ArrayList;

/**
 *
 * @author d3vil401
 */
public class RoundManager 
{
    private ArrayList<cRound> _rounds = new ArrayList<>();
    private int               _roundCounter = 1;
    private int               _roundBound = -1;
    
    private ArrayList<SolverAgent> _SAgents = new ArrayList<>();
    private ArrayList<ProposerAgent> _PAgents = new ArrayList<>();
    private ArrayList<Challenge> _Challenges = new ArrayList<>();
    private roundStatsHolder _globalStats = new roundStatsHolder();
    
    private ReportCompiler _problemData = new ReportCompiler(OutputNameFormatter.parseName(FactoryHolder._docNames[0]));
    private ReportCompiler _agentData = new ReportCompiler(OutputNameFormatter.parseName(FactoryHolder._docNames[1])); 
    private ReportCompiler _detailData = new ReportCompiler(OutputNameFormatter.parseName(FactoryHolder._docNames[3]));
    private ReportCompiler _avgData = new ReportCompiler(OutputNameFormatter.parseName(FactoryHolder._docNames[2]));
    private ReportCompiler _compositeData = new ReportCompiler(OutputNameFormatter.parseName(FactoryHolder._docNames[4]));
    private ReportCompiler _groupData = new ReportCompiler(OutputNameFormatter.parseName(FactoryHolder._docNames[5]));
    
    
    private void _populateSolverAgents(int _amount)
    {
        for (int i = 0; i < _amount; i++)
            this._SAgents.add(new SolverAgent());
    }
    
    private void _populateProposerAgents(int _amount)
    {
        for (int i = 0; i < _amount; i++)
            this._PAgents.add(new ProposerAgent());
    }
    
    private void _createReportsInstances()
    {
        this._problemData.createReport(new Object[] {"Round", "Problem", "Type", "Difficulty", "Reward", "Solved"});
        this._agentData.createReport(new Object[] {"Round", "Agent", "Skills", "Experience", "Composite Exp", "Money", "Has Solved", "Was in Group", "Has Rejected"});
        this._detailData.createReport(new Object[] {"Starting SAgents", "Starting PAgents", "Max Spawn Exp", "Number of Rounds", "Game Type"
                                                  , "Deadline", "SA Spawn Ratio", "PA Spawn Ratio", "SA Kill Threshold", "PA Kill Threshold"});
        this._avgData.createReport(new Object[] {});
        this._compositeData.createReport(new Object[] {"Round", "Agent", "Agent stdDev", "Problems", 
                                                       "Problems stdDev", "AVG Problems Amount", "AVG Solvers Amount"});
        
        this._groupData.createReport(new Object[] {"Round", "Challenge", "Members", "Solved", "Total Exp."});
    }
    
    public RoundManager(int _roundBound, int _SAAmount, int _PAAmount)
    {
        this._roundBound = _roundBound;
        
        this._populateProposerAgents(_PAAmount);
        this._populateSolverAgents(_SAAmount);
        
        this._createReportsInstances();
        
        this._detailData.addContent(new Object[] {this._SAgents.size(), this._PAgents.size(),
                                                  FactoryHolder._configManager.getNumberValue("SA_MAXIMUM_EXPERIENCE"),
                                                  this._roundBound, "Sorted", 
                                                  FactoryHolder._configManager.getFloatValue("DEADLINE"),
                                                  1, 1, 1, 1
                                                 });
        
        this._detailData.end();
    }
    
    public void end()
    {
        roundStatsExport.parseGlobalStats(this._rounds, this._globalStats);
        
        this._agentData.end();
        this._problemData.end();
        this._avgData.end();
        this._compositeData.end();
        this._groupData.end();
        
        FactoryHolder._graphsRender = graphStatsExport.renderGraphs(this._rounds, this._globalStats);
    }
    
    public RoundManager(int _roundBound, ArrayList<SolverAgent> _SAPool, ArrayList<ProposerAgent> _PAPool)
    {
        this._roundBound = _roundBound;
        
        this._PAgents = _PAPool;
        this._SAgents = _SAPool;
        
        this._createReportsInstances();
    
        this._detailData.addContent(new Object[] {this._SAgents.size(), this._PAgents.size(),
                                                  FactoryHolder._configManager.getNumberValue("SA_MAXIMUM_EXPERIENCE"),
                                                  this._roundBound, "Sorted", 
                                                  FactoryHolder._configManager.getFloatValue("DEADLINE"),
                                                  1, 1, 1, 1
                                                 });
        
        this._detailData.end();
    }
    
    public boolean hasNext()
    {
        return (this._roundCounter - 1 < this._roundBound);
    }
    
    private String _compileSkillMap(ArrayList<cSkill> _skillSet)
    {
        String _skills = "";
        for (int i = 0; i < _skillSet.size(); i++) {
            _skills += String.valueOf(_skillSet.get(i).getExperience());
            if (i != _skillSet.size() - 1)
                 _skills += ", ";
        }
        
        return _skills;
    }
    
    private int _compileHasRejected(SolverAgent _agent)
    {
        if (!_agent.getHasSolvedLastChallenge())
            return (_agent.getTryHarded() + _agent.getStats()._rejected);
        
        return 0;
    }
    
    // Groups are not enabled.
    private boolean _compileWasInGroup(SolverAgent _agent)
    {
        return false;
    }
    
    private boolean _compileHasBeenSolved(Challenge _challenge)
    {
        return _challenge.getSolver().isEmpty();
    }
    
    private void _compileReportEntries(int _round)
    {   
        for (Challenge i: this._rounds.get(_round - 1).getChallanges())
        {
            this._problemData.addContent(new Object[]{_round, i.toString(), 
                                                      "Sorted", i.getTotalDifficulty(), 
                                                      i.getReward(), this._compileHasBeenSolved(i)});
        }
            
        SolverAgent _slave = null;
        
        for (int i = 0; i < this._rounds.get(_round - 1).getSolverAgents().size(); i++)
        {
            _slave = this._rounds.get(_round - 1).getSolverAgents().get(i);
            if (Float.parseFloat(_slave.compositeExperience()) > 1)
                this._agentData.addContent(new Object[] {_round + 1, _slave.toString(), this._compileSkillMap(_slave.getSkills()),
                                                         _slave.getTotalExperience(), _slave.compositeExperience(), 
                                                         _slave.getStats()._money, _slave.getHasSolvedLastChallenge(), 
                                                         this._compileWasInGroup(_slave), this._compileHasRejected(_slave)});
            //else
            //    FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_DEBUG, "Empty SAgent found: this is a missing instance, should be recovered soon from dead pool.");
        }
        
        for (int i = 0; i < this._rounds.get(_round - 1).getDeadSolvers().size(); i++)
        {
            _slave = this._rounds.get(_round - 1).getDeadSolvers().get(i);
            this._agentData.addContent(new Object[] {_round + 1, _slave.toString(), this._compileSkillMap(_slave.getSkills()),
                                                         _slave.getTotalExperience(), _slave.compositeExperience(), 
                                                         _slave.getStats()._money, _slave.getHasSolvedLastChallenge(), 
                                                         this._compileWasInGroup(_slave), this._compileHasRejected(_slave)});
        }
        
        this._compositeData.addContent(new Object[] {_round + 1, this._rounds.get(_round - 1).getSolverAgents().size(), this._rounds.get(_round - 1)._stats._stdDevianceSAgents,
                                                   this._rounds.get(_round - 1).getChallanges().size(), this._rounds.get(_round - 1)._stats._stdDevianceChallenges,
                                                   this._rounds.get(_round - 1)._stats._avgChallengeCountPerRound, this._rounds.get(_round - 1)._stats._avgExpPerRound});
        
        for (Group group: this._rounds.get(_round - 1).getGroups())
        {
            this._groupData.addContent(new Object[] {_round + 1, group.getSolvedChallenge(),
                                                    (String)(group.getMembers()[0] + ", " + group.getMembers()[1]),
                                                     group.hasSolvedAChallenge(), group.getTotalExp()});
        }
    }
    
    private boolean isFirstRound()
    {
        return this._roundCounter == 1;
    }
    
    public void runLoop()
    {
        while (this.hasNext())
        {
            if (this.isFirstRound())
                this._rounds.add(new cRound(this._roundCounter, this._SAgents, this._PAgents));
            else
                this._rounds.add(new cRound(this._roundCounter, this._SAgents, this._PAgents, this._Challenges));
            
            this._rounds.get(this._roundCounter - 1).run();
            roundStatsExport.parseStats(this._rounds.get(this._roundCounter - 1));
            this._SAgents = (ArrayList<SolverAgent>)this._rounds.get(this._roundCounter - 1).getSolverAgents();//.clone();
            this._PAgents = (ArrayList<ProposerAgent>)this._rounds.get(this._roundCounter - 1).getProposerAgents();//.clone();
            this._Challenges = (ArrayList<Challenge>)this._rounds.get(this._roundCounter - 1).getChallanges();//.clone();
            
            this._compileReportEntries(this._roundCounter++);
        }
    }
    
    public void runNextRound()
    {
        if (this.hasNext())
        {
            if (this.isFirstRound())
                this._rounds.add(new cRound(this._roundCounter, this._SAgents, this._PAgents));
            else
                this._rounds.add(new cRound(this._roundCounter, this._SAgents, this._PAgents, this._Challenges));
            
            this._rounds.get(this._roundCounter - 1).run();
            roundStatsExport.parseStats(this._rounds.get(this._roundCounter - 1));
            this._SAgents = (ArrayList<SolverAgent>)this._rounds.get(this._roundCounter - 1).getSolverAgents();//.clone();
            this._PAgents = (ArrayList<ProposerAgent>)this._rounds.get(this._roundCounter - 1).getProposerAgents();//.clone();
            this._Challenges = (ArrayList<Challenge>)this._rounds.get(this._roundCounter - 1).getChallanges();//.clone();
            
            this._compileReportEntries(this._roundCounter++);
        }
    }
    
    
}
