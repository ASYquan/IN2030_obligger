package no.uio.ifi.asp.runtime;

//import java.util.ArrayList;
//import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeIntValue extends RuntimeValue {
    long intValue;

    //Fra forelesning 12.10.2022 fra IN2030
    @Override
    public long getIntValue(String what, AspSyntax where) {
        return intValue;
    }

    @Override
    public double getFloatValue(String what, AspSyntax where) {
        return (double) intValue;
    }
    //_________________________________________________________

    public RuntimeIntValue(long v){
        intValue = v; 
    }

    @Override
    public boolean getBoolValue(String what, AspSyntax where){
        return (intValue != 0);
    }


    @Override
    public String getStringValue(String what, AspSyntax where) {
        return String.valueOf(intValue);
    }

    @Override
    public String typeName(){
        return "Integer";
    }

    @Override
    public String toString() {
        return String.valueOf(intValue);
    }

    @Override
    public RuntimeValue evalAdd(RuntimeValue v, AspSyntax where) {
        if(v instanceof RuntimeFloatValue){
            return new RuntimeFloatValue((intValue + v.getFloatValue("+ Operator",where)));
        }
        if(v instanceof RuntimeIntValue){
            return new RuntimeIntValue(intValue + v.getIntValue("+ Operator",where));
        }
        runtimeError("'+' or 'Addition' is undefined for " + typeName() 
        + " and " + v.typeName(), where);
        return null;
    }

    @Override
    public RuntimeValue evalDivide(RuntimeValue v, AspSyntax where) {
        if(v instanceof RuntimeFloatValue){
            return new RuntimeFloatValue((double) intValue / v.getFloatValue("/ Operator", where));
        }
        if(v instanceof RuntimeIntValue){
            return new RuntimeFloatValue((double) intValue / v.getIntValue("/ Operator", where));
        }
        runtimeError("'/' or 'division' is undefined for " + typeName() 
        + " and " + v.typeName(), where);
        return null;
    }


    @Override
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
        if(v instanceof RuntimeIntValue){
            return new RuntimeBoolValue(intValue == v.getIntValue("== Operator", where));
        }
        if(v instanceof RuntimeFloatValue){
            RuntimeFloatValue floatVal = new RuntimeFloatValue((double) intValue);
            return new RuntimeBoolValue(floatVal.getFloatValue("== Operator", where) == v.getFloatValue("== Operator", where));
        }
        runtimeError("'==' or 'equals' is undefined for " + typeName() + " and " + v.typeName(), where);
        return null;
    }

    @Override
    public RuntimeValue evalGreater(RuntimeValue v, AspSyntax where) {
        if(v instanceof RuntimeIntValue){
            return new RuntimeBoolValue(intValue > v.getIntValue("> Operator", where));
        }
        if(v instanceof RuntimeFloatValue){
            return new RuntimeBoolValue(intValue > v.getFloatValue("> Operator", where));
        }
        runtimeError("'>' or 'greater than' is undefined for " 
        + typeName() + " and " + v.typeName(), where);
        return null;
    }

    @Override
    public RuntimeValue evalGreaterEqual(RuntimeValue v, AspSyntax where) {
        if(v instanceof RuntimeIntValue){
            return new RuntimeBoolValue(intValue >= v.getIntValue(">= Operator", where));
        }
        if(v instanceof RuntimeFloatValue){
            return new RuntimeBoolValue(intValue >= v.getFloatValue(">= Operator", where));
        }
        runtimeError("'>=' or 'greater or equals' is undefined for " + typeName()
         + " and " + v.typeName(), where);
        return null;
    }

    @Override
    public RuntimeValue evalIntDivide(RuntimeValue v, AspSyntax where) {
        if(v instanceof RuntimeFloatValue){
            return new RuntimeFloatValue((double) (Math.floor(intValue / v.getFloatValue("// Operator", where))));
        }
        if(v instanceof RuntimeIntValue){
            return new RuntimeIntValue((long) (Math.floor(intValue / v.getIntValue("// Operator", where))));
        }
        runtimeError("'//' or 'int division' is undefined for " + typeName() 
        + " and " + v.typeName(), where);
        return null;
    }

    @Override
    public RuntimeValue evalLess(RuntimeValue v, AspSyntax where) {
        if(v instanceof RuntimeIntValue){
            return new RuntimeBoolValue(intValue < v.getIntValue("< Operator", where));
        }
        if(v instanceof RuntimeFloatValue){
            return new RuntimeBoolValue(intValue < v.getFloatValue("< Operator", where));
        }
        runtimeError("'<' or 'less than 'is undefined for " 
        + typeName() + " and " + v.typeName(), where);
        return null;
    }

    @Override
    public RuntimeValue evalLessEqual(RuntimeValue v, AspSyntax where) {
        if(v instanceof RuntimeIntValue){
            return new RuntimeBoolValue(intValue <= v.getIntValue("<= Operator", where));
        }
        if(v instanceof RuntimeFloatValue){
            return new RuntimeBoolValue(intValue <= v.getFloatValue("<= Operator", where));
        }
        runtimeError("'<=' or 'less than equals' is undefined for " + typeName() 
        + " and " + v.typeName(), where);
        return null;
    }

    @Override
    public RuntimeValue evalModulo(RuntimeValue v, AspSyntax where) {
        if(v instanceof RuntimeIntValue){
            long next = v.getIntValue("% Operator", where);
            long newValue = Math.floorMod(intValue, next);
            return new RuntimeIntValue(newValue);
        }
        if(v instanceof RuntimeFloatValue){
            double next = v.getFloatValue("% Operator", where);
            double newValue = intValue - next*Math.floor(intValue/next);
            return new RuntimeFloatValue(newValue);
        }
        runtimeError("'%' or 'modulo' is undefined for " + typeName() 
        + " and " + v.typeName(), where);
        return null;
    }

    @Override
    public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where) {
        if(v instanceof RuntimeFloatValue){
            return new RuntimeFloatValue((double)(intValue * 
            v.getFloatValue("* Operator", where)));
        }
        if(v instanceof RuntimeIntValue){
            return new RuntimeIntValue(intValue 
            * v.getIntValue("* Operator", where));
        }
        runtimeError("'*' or 'multplication' is undefined for " + typeName() 
        + " and " + v.typeName(), where);
        return null;
    }

    @Override
    public RuntimeValue evalNegate(AspSyntax where) {
        return new RuntimeIntValue(-intValue);
    }

    @Override
    public RuntimeValue evalNot(AspSyntax where) {
        if(intValue == 0){
            return new RuntimeBoolValue(true);
        }
        return new RuntimeBoolValue(false);
    }

    @Override
    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
        if(v instanceof RuntimeIntValue){
            return new RuntimeBoolValue(intValue != v.getIntValue("!= Operator", where));
        }
        if(v instanceof RuntimeFloatValue){
            return new RuntimeBoolValue(intValue != v.getFloatValue("!= Operator", where));
        }
        runtimeError("'!=' or 'not equals' is undefined for " + typeName() 
        + " and " + v.typeName(), where);
        return null;
    }

    @Override
    public RuntimeValue evalPositive(AspSyntax where) {
        return new RuntimeIntValue(intValue);
    }

    @Override
    public RuntimeValue evalSubtract(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeFloatValue){
            RuntimeFloatValue other = (RuntimeFloatValue) v;
            return new RuntimeFloatValue((double) (intValue 
            - other.getFloatValue(" - Operator", where)));
        }

        if(v instanceof RuntimeIntValue){
            return new RuntimeIntValue(intValue 
            - v.getIntValue(" - Operator", where));
        }

        runtimeError("'-' or 'subtraction' is undefined for " + typeName() 
        + " and " + v.typeName(), where);
        return null;
    }
    
}