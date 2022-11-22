package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;
import no.uio.ifi.asp.parser.*;
import java.util.ArrayList;

public class RuntimeFuncValue extends RuntimeValue{
    public ArrayList<RuntimeValue> args = new ArrayList<RuntimeValue>();

    AspFuncDef funcDef;
    RuntimeScope scope;
    String name;

    public RuntimeFuncValue(RuntimeValue v, ArrayList<RuntimeValue> args, RuntimeScope curScope, AspFuncDef def){
        name = v.showInfo();
        this.args = args;
        scope = curScope;
        this.funcDef = def;
    }

    public RuntimeFuncValue(String name){
        this.name = name;
    }

    @Override
    public String typeName(){
        return "function";
    }

    @Override
    public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where){
        RuntimeValue returnVal = null;

        RuntimeValue val;

        if(actualParams.size() == args.size()){
            RuntimeScope newScope = new RuntimeScope(this.scope);
            //System.out.println(params.size());
            for(int i = 0; i < args.size(); i++){
                val = actualParams.get(i);
                if(val != null){
                    newScope.assign(args.get(i).getStringValue("string", where), val);
                }else{
                    newScope.assign(args.get(i).getStringValue("string", where), actualParams.get(i));
                }
            }
            try{
                val = funcDef.evalFunc(newScope);
            }
            catch(RuntimeReturnValue v){
                return v.value;
            }

        }else{
            Main.error("arguments are not equal");
        }
        return returnVal;
    }
}

