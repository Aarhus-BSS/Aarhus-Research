/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionManager.SaveManager;

import Agents.Group.Group;
import Agents.Properties.cSkill;
import Agents.ProposerAgent;
import Agents.SolverAgent;
import Challenge.Challenge;
import Common.MsgBoxer.MBox;
import auresearch.FactoryHolder;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author d3vil401
 */
public class SaveManager 
{
    private static final int MAGIC_VALUE = (0x4C4D4600 + FactoryHolder._GLOBALS._softwareVersion);
    
    private static ArrayList<SolverAgent> _sagentList = new ArrayList<>();
    private static ArrayList<ProposerAgent> _pagentList = new ArrayList<>();
    private static ArrayList<Challenge> _challengeList = new ArrayList<>();
    private static ArrayList<Group> _groupList = new ArrayList<>();
    private static ArrayList<Object> _detailsList = new ArrayList<>();
    
    private static File _fileHandle = null;
    private static DataOutputStream _stream = null;
    
    public SaveManager()
    {
        
    }
    
    /*
    
    |------------------|------------------|
    | MAGIC + Version  | CRC of content   |
    |------------------|------------------|
    | SAgents count    | PAgents count    |
    |------------------|------------------|
    | Challenge count  | Group counts     | 24 bytes
    |-------------------------------------|                sizeof(SolverAgent) * SAgents count
    | SAgents List {...}                  | ----------------------------------------------------------------> |------------------|------------------|
    |-------------------------------------|                                                                   | Skills Count     | Skills           | 8 bytes
    | PAgents List {...}                  |                                                                   | Skill X Exp {...}| Skill Name Size  | 4 + 4 + length(SkillName)
    |-------------------------------------|                                                                   | Skill Name       | Stats.trials     | 4 * 3
    | Challenges List {...}               |                                                                   | Stats.successes  | Stats.money      | = 8 + 8 + X * Skill count + 12 => 28 + X * skill count
    |-------------------------------------|
    | Groups List {...}                   |
    |-------------------------------------|
    
    */
    
    public static void assignSAList(ArrayList<SolverAgent> _solvers)
    {
        _sagentList = _solvers;
    }
    
    public static void assignPAList(ArrayList<ProposerAgent> _proposers)
    {
        _pagentList = _proposers;
    }
    
    public static void assignCList(ArrayList<Challenge> _challenges)
    {
        _challengeList = _challenges;
    }
    
    public static void assignGList(ArrayList<Group> _groups)
    {
        _groupList = _groups;
    }
    
    public static void addDetail(Object _detail)
    {
        _detailsList.add(_detail);
    }
    
    public static boolean createFile(String _name)
    {
        try 
        {
            _stream = new DataOutputStream(new FileOutputStream(_fileHandle = new File(_name)));
            _stream.flush(); // Force file creation for now.
        } catch (FileNotFoundException ex) {
            MBox.showBox("Could not initialize the save output file " + _name + ": " + ex.getMessage() + ".", "Save output fail!", MBox._MSGBOX_TYPE.TYPE_ERROR);
            return false;
        } catch (IOException ex) {
            MBox.showBox("Could not write the save output file " + _name + ": " + ex.getMessage() + ".", "Save output fail!", MBox._MSGBOX_TYPE.TYPE_ERROR);
            return false;
        }
        
        return true;
    }
    
    private static void write(Object _value)
    {
        try {
            
            if (_value instanceof Byte)
                    _stream.write((byte)_value);
            else if (_value instanceof Integer)
                _stream.write(Integer.parseInt(String.valueOf(_value)));
            else if (_value instanceof SolverAgent)
            {
                _stream.write((int)((SolverAgent) _value).getSkills().size());
                for (cSkill i: ((SolverAgent)_value).getSkills())
                {
                    _stream.write(i.getExperience());
                    _stream.write(i.getName().length());
                    _stream.writeUTF(i.getName());
                }
                
                _stream.write((int)((SolverAgent) _value).getStats()._trials);
                _stream.write((int)((SolverAgent) _value).getStats()._successTrials);
                _stream.write((int)((SolverAgent) _value).getStats()._money);
            }
            
            _stream.flush();
        } catch (IOException ex) {
            MBox.showBox("Could not write the save output file " + _fileHandle.getName() + ": " + ex.getMessage() + ".", "Save output fail!", MBox._MSGBOX_TYPE.TYPE_ERROR);
        }
    }
    
    public static void compile()
    {
        write(MAGIC_VALUE);
        write(0xFFFFFFFF); // We're not going to write CRC now, we'll wait for final step.
        write(_sagentList.size());
        write(_pagentList.size());
        write(_challengeList.size());
        write(_groupList.size());
        
        //_sagentList.forEach((i) -> { write(i); });
        //_pagentList.forEach((i) -> { write(i); });
        //_challengeList.forEach((i) -> { write(i); });
        //_groupList.forEach((i) -> { write(i); });
        
        
    }
}
