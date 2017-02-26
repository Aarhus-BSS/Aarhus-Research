/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Common.Lua;

import org.luaj.vm2.*;
import org.luaj.vm2.lib.jse.*;

/**
 *
 * @author d3vil401
 */
public class LuaManager 
{
    private Globals _global;
    
    public LuaManager()
    {
        this._global = JsePlatform.standardGlobals();
    }
}
