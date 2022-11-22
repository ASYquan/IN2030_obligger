
package no.uio.ifi.asp.runtime;

import java.util.ArrayList;
//import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;


public class RuntimeDictValue extends RuntimeValue {
    ArrayList <RuntimeValue> keys;
    ArrayList <RuntimeValue> values;
    
    public RuntimeDictValue(ArrayList <RuntimeValue> key, ArrayList <RuntimeValue> val) {
        keys = key;
        values = val; 
    }

    @Override 
    String typeName() {
        return "Dictionary";
    }

    @Override
    public String toString() {
        String printDict ="{";
        for(int i = keys.size(); i-- > 0; ){

            printDict += keys.get(i).toString() + ": " + values.get(i).toString();

            if (!(i == 0)){
                printDict += ", ";

            }
        }
        printDict += "}";
        return printDict; 
    }

    @Override
    public boolean getBoolValue(String what, AspSyntax where) {
        if (keys.size() == 0){
            return false;    
        }
        return true;
    }

    @Override
    public String getStringValue(String what, AspSyntax where) {
        return this.toString();
        }

    @Override
    public RuntimeValue evalSubscription(RuntimeValue v, AspSyntax where) {
        if(!(v instanceof RuntimeStringValue)){
            runtimeError("Index value:" + v + "of" + typeName() + "is not a string", where);
        }
        for (int i = 0; i < keys.size(); i++){
            if (keys.get(i).toString().equals(v.toString())){
                return values.get(i);
            }
            if (v.getStringValue("String", where).equals(keys.get(i).getStringValue("String", where))){
                return values.get(i);
            }

        }
        runtimeError("Index value:" + v + "of" + typeName() + "does not exis", where);
        return null;
    }

    @Override
    public void evalAssignElem(RuntimeValue inx, RuntimeValue val, AspSyntax where) {
        this.addElement(inx, val);
    }

    @Override 
    public RuntimeValue evalNot(AspSyntax where){
        if (keys.size() == 0){
            return new RuntimeBoolValue(true);
        }
        return new RuntimeBoolValue(false);
    }
        
    private void addElement(RuntimeValue key, RuntimeValue value){
        for (int i = 0; i < keys.size(); i++){
            if (key.getStringValue("String", null)
            == keys.get(i).getStringValue("String", null)){
                values.set(i, value);
                return; 
            }
        }
        keys.add(0, key);
        values.add(0, value); 

    }

    @Override
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
	    if (v instanceof RuntimeNoneValue) {
			return new RuntimeBoolValue(false);
      	}
	    runtimeError("Error for Dict when ==.", where);
	    return null;  // Required by the compiler
	}
    
}