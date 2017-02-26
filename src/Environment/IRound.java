/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Environment;

import Agents.Properties.cSkill;
import Agents.ProposerAgent;
import Agents.SolverAgent;

/**
 *
 * @author d3vil401
 */
public interface IRound 
{
    public IRound        createRound();
    public void          setID(String _id);
    public String        getID();
    public int           getRoundIndex();
    public void          setRoundIndex(int _round);
}
