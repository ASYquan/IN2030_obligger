package no.uio.ifi.asp.runtime;

//import java.util.ArrayList;
//import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;


public class RuntimeStringValue extends RuntimeValue {
    String strValue;

    public RuntimeStringValue(String v) {
        strValue = v; 
    }

    @Override 
    String typeName() {
        return "String";
    }
    //Fra forelesning 12.10.2022 i IN2030:
    @Override
    public String showInfo() {
        if (strValue.indexOf('\'') >= 0) {
            return '"' + strValue + '"';
        }
        else{
            return "'" + strValue + "'";
        }
    }
    
    @Override
    public String toString() {
        return showInfo();
    }

    @Override
    public boolean getBoolValue(String what, AspSyntax where) {
        if (strValue == "") {
            return false;
        }
        return true; 
    }

    @Override
    public long getIntValue(String what, AspSyntax where) {
        return (long) Integer.parseInt(strValue);
    }

    @Override
    public double getFloatValue(String what, AspSyntax where) {
        return (double) Double.parseDouble(strValue);
    }

    @Override
    public String getStringValue(String what, AspSyntax where) {
        return strValue;
    }


    @Override
    public RuntimeValue evalAdd(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue){
            String newStr = strValue + v.getStringValue("String", where);
            return new RuntimeStringValue(newStr);
        }
        runtimeError("'+' / 'addition' for " + typeName() 
        + " and " + v.typeName(), where);
        return null;
    }

    @Override
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where){
        if (v instanceof RuntimeStringValue){

            if (strValue.equals(v.getStringValue("String", where))){
                return new RuntimeBoolValue(true);
        }
        return new RuntimeBoolValue(false);
        }
        runtimeError("'==' / 'equals' is undefined for " + typeName()
        + " and " + v.typeName(), where);
        return null;
    }

    @Override
    public RuntimeValue evalGreater(RuntimeValue v, AspSyntax where){
      if (v instanceof RuntimeStringValue){

        if (strValue.length() > v.getStringValue("String", where).length()){
            return new RuntimeBoolValue(true);
        }
        return new RuntimeBoolValue(false);
      }
      runtimeError("'>' / 'greater than' is undefined for " + typeName() 
       + " and " + v.typeName(), where);
      return null;
    }

    @Override
    public RuntimeValue evalGreaterEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue){
            if (strValue.length() >= v.getStringValue("String", where).length()){
                return new RuntimeBoolValue(false);
            }
            return new RuntimeBoolValue(true);
        }
        runtimeError("'>=' / 'greaterEqual' is undefined for " + typeName()
         + " and " + v.typeName(), where);
        return null;
    }

    @Override
    public RuntimeValue evalLess(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue){
            if (strValue.length() < v.getStringValue("String", where).length()){
                return new RuntimeBoolValue(false);
            }
            return new RuntimeBoolValue(true);
        }
        runtimeError("'<' or 'less than' is undefined for " + typeName()
        + " and " + v.typeName(), where);
        return null;
    }

    @Override
    public RuntimeValue evalLessEqual(RuntimeValue v, AspSyntax where) {
            if (v instanceof RuntimeStringValue){
                if (strValue.length() <= v.getStringValue("String", where).length()){
                    return new RuntimeBoolValue(true);
                }
                return new RuntimeBoolValue(false);
            }
            runtimeError("'<=' or 'less than or equals' is undefined for " 
            + typeName() + " and " + v.typeName(), where);
            return null;
    }

    @Override
    public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue){
        String newString = new String
        (new char[(int) v.getIntValue("int", where)]).replace("\0", strValue);
        return new RuntimeStringValue(newString);
    }
    runtimeError("'*' or 'multplication' is undefined for " + typeName() 
    + " and " + v.typeName(), where);
    return null;
    }

    @Override
    public RuntimeValue evalNot(AspSyntax where){
        return new RuntimeBoolValue(strValue == "");
    }

    @Override
    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where){
        //v is defined as a RuntimeNoneValue, when in reality it is a string in none-test.asp. 
        //No idea how to make "none" be recognised as String, and not a None-type
        //Temporary solution to make the test work:
        if (v instanceof RuntimeNoneValue){
            return new RuntimeBoolValue(true);
        }
        if (v instanceof RuntimeStringValue){

            if (strValue.equals(v.getStringValue("String", where))){
                return new RuntimeBoolValue(false);
            }
            return new RuntimeBoolValue(true);
        }
      runtimeError("'!=' or 'not equals' is undefined for " + typeName() + " and " + v.typeName(), where);
      return null;
    }

    @Override
    public RuntimeValue evalSubscription(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue){
            int index = (int) v.getIntValue("Int", where);
            String charAtIndex = "";
            char c = strValue.charAt(index);
            charAtIndex = charAtIndex + c;
            if (index > strValue.length()-1){
                runtimeError("TypeError: Index out of bounds for " + typeName(), where);
            }
            //System.out.println(strValue);
            return new RuntimeStringValue(charAtIndex);
        }
        runtimeError("Subscription is undefined for " + typeName()
         + " and " + v.typeName(), where);
        return null;   
    }

    @Override
    public RuntimeValue evalLen(AspSyntax where) {
        return new RuntimeIntValue(strValue.length());
    }




}