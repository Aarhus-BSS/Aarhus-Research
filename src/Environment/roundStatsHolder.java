/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Environment;

/**
 *
 * @author d3vil401
 */
public class roundStatsHolder 
{
    public int _SAgentsCountPerRound = 0;
    public int _PAgentsCountPerRound = 0;
    
    // PER ROUND PROPERTY
    public double _stdDevianceSAgents = 0;
    public double _avgExpPerRound = 0;
    public double _avgChallengeCountPerRound = 0;
    public double _stdDevianceChallenges = 0;
    public int    _solvedAgents = 0;
    
    // GLOBAL ONLY PROPERTY
    public double[] g_avgSuccessAgentsPerRound = null;
    public double[] g_stdMinDevianceAVG = null;
    public double[] g_stdPlusDevianceAVG = null;
    public double[] g_avgProblemsPerRound = null;
    public double[] g_stdMinDevianceAVGProblems = null;
    public double[] g_stdPlusDevianceAVGProblems = null;
    
    
    public roundStatsHolder()
    {
        
    }
    
    public double getSolvedAgents()
    {
        return this._solvedAgents;
    }
    
    public void setSolvedAgents(int _amount)
    {
        this._solvedAgents = _amount;
    }
    
    public int getSAgentsCount()
    {
        return this._SAgentsCountPerRound;
    }
    
    public int getPAgentsCount()
    {
        return this._PAgentsCountPerRound;
    }
    
    public double getStdDevianceSAgents()
    {
        return this._stdDevianceSAgents;
    }
    
    public double getAvgExp()
    {
        return this._avgExpPerRound;
    }
    
    public double getAvgChallengeCount()
    {
        return this._avgChallengeCountPerRound;
    }
    
    public double getStdDevianceChallenge()
    {
        return this._stdDevianceChallenges;
    }
    
    public void setSAgentsCount(int _count)
    {
        this._SAgentsCountPerRound = _count;
    }
    
    public void setPAgentsCount(int _count)
    {
        this._PAgentsCountPerRound = _count;
    }
    
    public void setSAgentsDeviance(double _value)
    {
        this._stdDevianceSAgents = _value;
    }
    
    public void setAvgExpPerRound(double _value)
    {
        this._avgExpPerRound = _value;
    }
    
    public void setAvgChallengeCountPerRound(double _value)
    {
        this._avgChallengeCountPerRound = _value;
    }
    
    public void setDevianceChallenges(double _value)
    {
        this._stdDevianceChallenges = _value;
    }
}
