/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Environment;

import Agents.SolverAgent;
import Challenge.Challenge;
import java.util.ArrayList;

/**
 *
 * @author d3vil401
 */
public class roundStatsExport 
{
    private static double _compositeTotal(ArrayList<SolverAgent> _solvers)
    {
        double _counter = 0;
        
        if (!_solvers.isEmpty())
            for (SolverAgent i: _solvers)
                _counter += Double.parseDouble(i.compositeExperience());
        
        return _counter;
    }
    
    public static double avgExpPerRound(cRound _round)
    {
        double _counter = _compositeTotal(_round.getSolverAgents()), _average = 0;
        
        _average = (_counter / (double)_round.getSolverAgents().size());
        
        return _average;
    }
    
    public static double stdDevianceSolvers(cRound _round)
    {
        double _counter = _compositeTotal(_round.getSolverAgents()), _average = 0, _sd = 0;
        
        _average = (_counter / (double)_round.getSolverAgents().size());
        
        for (SolverAgent i: _round.getSolverAgents())
            _sd += Math.pow(Double.parseDouble(i.compositeExperience()) - _average, 2);
        
        _sd /= _round.getSolverAgents().size();
        _sd = Math.sqrt(_sd);
        
        return _sd;
    }
    
    public static double stdDevianceProblems(cRound _round)
    {
        double _counter = 0, _sd = 0, _average = 0;
        
        if (_round.getChallanges().size() > 0)
        {
            for (Challenge i: _round.getChallanges())
                _counter += Double.parseDouble(i.getCompositeString());
        
            _average = (_counter / _round.getChallanges().size());

            for (Challenge i: _round.getChallanges())
                _sd += Math.pow(Double.parseDouble(i.getCompositeString()) - _average, 2);

            _sd /= _round.getChallanges().size();
            _sd = Math.sqrt(_sd);
        }
        
        return _sd;
    }
    
    public static double averageRoundProblems(cRound _round)
    {
        double _counter = 0, _average = 0, _variance = 0, _stdDeviation = 0;
         
        if (_round.getChallanges().size() > 0) 
        {
            for (Challenge i: _round.getChallanges())
                _counter += Double.parseDouble(i.getCompositeString());

            _average = (_counter / _round.getChallanges().size());
        }
        
        return _average;
    }
    
    private static double[] g_averageRoundSuccessSAgents(ArrayList<cRound> _game)
    {
        double _avgMap[] = new double[_game.size()];
        
        for (int i = 0; i < _game.size(); i++)
            _avgMap[i] = (_game.get(i).getSolvedAgents().size() / _game.get(i).getSolverAgents().size());
        
        return _avgMap;
    }
    
    public static void parseStats(cRound _round)
    {
        _round._stats.setPAgentsCount(_round.getProposerAgents().size());
        _round._stats.setSAgentsCount(_round.getSolverAgents().size());
        _round._stats.setDevianceChallenges(stdDevianceProblems(_round));
        _round._stats.setSAgentsDeviance(stdDevianceSolvers(_round));
        _round._stats.setAvgExpPerRound(avgExpPerRound(_round));
        _round._stats.setAvgChallengeCountPerRound(averageRoundProblems(_round));
        _round._stats.setSolvedAgents(_round.getSolvedAgents().size());
    }
    
    public static void parseGlobalStats(ArrayList<cRound> _rounds, roundStatsHolder _gStatsHolder)
    {
        double _av = 0;
        
        _gStatsHolder.g_stdMinDevianceAVG = new double[_rounds.size()];
        _gStatsHolder.g_stdPlusDevianceAVG = new double[_rounds.size()];
        _gStatsHolder.g_stdMinDevianceAVGProblems = new double[_rounds.size()];
        _gStatsHolder.g_stdPlusDevianceAVGProblems = new double[_rounds.size()];
        _gStatsHolder.g_avgProblemsPerRound = new double[_rounds.size()];
        
        // Disgustingly forcefully checking it's set to 0 because I found a nice series of 3.0 which IS NOT MY STUFF.
        for (int i = 0; i < _rounds.size(); i++)
        {
            _gStatsHolder.g_stdMinDevianceAVG[i] = 0;
            _gStatsHolder.g_stdPlusDevianceAVG[i] = 0;
            _gStatsHolder.g_stdMinDevianceAVGProblems[i] = 0;
            _gStatsHolder.g_stdPlusDevianceAVGProblems[i] = 0;
            _gStatsHolder.g_avgProblemsPerRound[i] = 0;
        }
        
        _gStatsHolder.g_avgSuccessAgentsPerRound = g_averageRoundSuccessSAgents(_rounds);
        // Out round
        // Out composite
        
        for (int i = 0; i < _rounds.size(); i++)
        {
            _av = avgExpPerRound(_rounds.get(i));
            _gStatsHolder.g_stdMinDevianceAVG[i] = (_av - (stdDevianceSolvers(_rounds.get(i))));
            _gStatsHolder.g_stdPlusDevianceAVG[i] = (_av + (stdDevianceSolvers(_rounds.get(i))));
            
            _av = averageRoundProblems(_rounds.get(i));
            _gStatsHolder.g_avgProblemsPerRound[i] = _av;
            _gStatsHolder.g_stdMinDevianceAVGProblems[i] = (_av - (stdDevianceProblems(_rounds.get(i))));
            _gStatsHolder.g_stdPlusDevianceAVGProblems[i] = (_av + (stdDevianceProblems(_rounds.get(i))));
        }
    }
}
