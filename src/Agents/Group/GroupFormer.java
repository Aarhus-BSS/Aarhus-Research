/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agents.Group;

import Agents.Group.Group;
import Agents.Group._MODEL_SETUP;
import Agents.Properties.cSkill;
import Agents.SolverAgent;
import Challenge.Challenge;
import Common.Configuration.ConfigManager;
import Common.Logging.ILogManager;
import auresearch.FactoryHolder;
import java.util.ArrayList;
import java.util.Collections;
/**
 *
 * @author d3vil401
 */
public class GroupFormer 
{
    private ArrayList<Group> _groupPool = new ArrayList();
    private ArrayList<SolverAgent> _sAPool = new ArrayList();
    private _MODEL_SETUP _requestedMethod = _MODEL_SETUP.MODEL_1A;
    private Challenge _challenge = null;

    public GroupFormer(ArrayList<SolverAgent> _sAgents, _MODEL_SETUP _model, Challenge _challenge) {
        this._challenge = _challenge;
        this._createGroups(_sAgents, _model);
    }

    public GroupFormer(ArrayList<SolverAgent> _sAgents, Challenge _challenge) {
        this._challenge = _challenge;
        this._createGroups(_sAgents, _MODEL_SETUP.MODEL_1A);
    }

    private boolean _canProceedWithChallenge(SolverAgent _agent, Challenge _skillMap) {
        for (int k = 0; k < FactoryHolder._configManager.getArrayValue("AGENT_SKILLS").size(); ++k) {
            if (_agent.getSkills().get(k).getExperience() > _skillMap.getDifficultyMap()[k]) continue;
            return false;
        }
        return _agent.getTryHarded() <= FactoryHolder._configManager.getNumberValue("NUMBER_OF_TRIALS_FOR_SINGLE_AGENT_SOLVING");
    }

    private void _createGroups(ArrayList<SolverAgent> _sAgents, _MODEL_SETUP _model) {
        this._requestedMethod = _model;
        this._sAPool = (ArrayList)_sAgents.clone();
        switch (this._requestedMethod) {
            case MODEL_1A: {
                Collections.sort(this._sAPool, (p1, p2) -> p1.getSkills().get(0).getExperience() - p2.getSkills().get(0).getExperience());
                for (SolverAgent i : this._sAPool) {
                    if (!this._canProceedWithChallenge(i, this._challenge)) continue;
                }
                break;
            }
            case MODEL_1B: {
                break;
            }
            default: {
                FactoryHolder._logManager.print(ILogManager._LOG_TYPE.TYPE_ERROR, "Incorrect model specified in group formation process.");
            }
        }
    }

    public ArrayList<Group> getFormedGroups() {
        return this._groupPool;
    }

    public long getGroupCount() {
        return this._groupPool.size();
    }
}
