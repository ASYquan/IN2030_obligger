package no.uio.ifi.asp.runtime;


//import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;


public class RuntimeFloatValue extends RuntimeValue{
    double floatValue;

    public RuntimeFloatValue(double v){
        floatValue = v; 
    }

    @Override
    public String toString(){
        return String.valueOf(floatValue);
    }

    @Override
    String typeName(){
        return "float";
    }

    @Override
    public double getFloatValue(String what, AspSyntax where){
        return (double) floatValue;
    }

    @Override
    public boolean getBoolValue(String what, AspSyntax where){
        return(floatValue != 0.0000);
    }
    
    @Override
    public String getStringValue(String what, AspSyntax where){
        return this.toString();
    }

    @Override
    public RuntimeValue evalAdd(RuntimeValue v, AspSyntax where) {
        if(v instanceof RuntimeFloatValue){
            return new RuntimeFloatValue(floatValue + v.getFloatValue("+ Operator",where));
        }
        if(v instanceof RuntimeIntValue){
            return new RuntimeFloatValue(floatValue + v.getIntValue("+ Operator",where));
        }
        runtimeError("'+' or 'addition' is undefined for " + typeName()
         + " and " + v.typeName(), where);
        return null;
    }
    
    @Override
    public RuntimeValue evalDivide(RuntimeValue v, AspSyntax where) {
        if(v instanceof RuntimeFloatValue){
            return new RuntimeFloatValue(floatValue / v.getFloatValue("/ Operator", where));
        }
        if(v instanceof RuntimeIntValue){
            return new RuntimeFloatValue(floatValue / v.getIntValue("/ Operator", where));
        }
        runtimeError("'/' or 'division' is undefined for " + typeName() 
        + " and " + v.typeName(), where);
        return null;
    }

    @Override
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
        if(v instanceof RuntimeFloatValue){
            return new RuntimeBoolValue(floatValue == v.getFloatValue("== Operator", where));
        }
        if(v instanceof RuntimeIntValue){
            RuntimeFloatValue floatVal = new RuntimeFloatValue(v.getIntValue("== Operator", where));
            return new RuntimeBoolValue(floatValue == floatVal.floatValue);
        }
        if(v instanceof RuntimeNoneValue){
            return new RuntimeBoolValue(v.getBoolValue("== operator", where));
        }
        runtimeError("'==' or 'equals' is undefined for " 
        + typeName() + " and " + v.typeName(), where);
        return null;
    }

    @Override
    public RuntimeValue evalGreater(RuntimeValue v, AspSyntax where) {
        if(v instanceof RuntimeIntValue){
            return new RuntimeBoolValue(floatValue > v.getIntValue("> Operator", where));
        }
        if(v instanceof RuntimeFloatValue){
            return new RuntimeBoolValue(floatValue > v.getFloatValue("> Operator", where));
        }
        runtimeError("'>' or 'greater than' is undefined for " + typeName() 
        + " and " + v.typeName(), where);
        return null;
    }

    @Override
    public RuntimeValue evalGreaterEqual(RuntimeValue v, AspSyntax where) {
        if(v instanceof RuntimeIntValue){
            return new RuntimeBoolValue(floatValue >= v.getIntValue(">= Operator", where));
        }
        if(v instanceof RuntimeFloatValue){
            return new RuntimeBoolValue(floatValue >= v.getFloatValue(">= Operator", where));
        }
        runtimeError("'>=' or 'greater equals' is undefined for "
         + typeName() + " and " + v.typeName(), where);
        return null;
    }

    @Override
    public RuntimeValue evalIntDivide(RuntimeValue v, AspSyntax where) {
        if(v instanceof RuntimeFloatValue){
            return new RuntimeFloatValue((double) (Math.floor(floatValue / v.getFloatValue("// Operator", where))));
        }
        if(v instanceof RuntimeIntValue){
            return new RuntimeFloatValue((double) (Math.floor(floatValue / v.getIntValue("// Operator", where))));
        }
        runtimeError("'//' or 'division int' is undefined for " 
        + typeName() + " and " + v.typeName(), where);
        return null;
    }

    @Override
    public RuntimeValue evalLess(RuntimeValue v, AspSyntax where) {
        if(v instanceof RuntimeIntValue){
            return new RuntimeBoolValue(floatValue < v.getIntValue("< Operator", where));
        }
        if(v instanceof RuntimeFloatValue){
            return new RuntimeBoolValue(floatValue < v.getFloatValue("< Operator", where));
        }
        runtimeError("'<' or 'less than' is undefined for "
         + typeName() + " and " + v.typeName() , where);
        return null;
    }

    @Override
    public RuntimeValue evalLessEqual(RuntimeValue v, AspSyntax where) {
        if(v instanceof RuntimeIntValue){
            return new RuntimeBoolValue(floatValue <= v.getIntValue("<= Operator", where));
        }
        if(v instanceof RuntimeFloatValue){
            return new RuntimeBoolValue(floatValue <= v.getFloatValue("<= Operator", where));
        }
        runtimeError("'<=' or 'eval less equal' is undefined for "
         + typeName() + " and " + v.typeName(), where);
        return null;
    }

    @Override
    public RuntimeValue evalModulo(RuntimeValue v, AspSyntax where) {
        if(v instanceof RuntimeIntValue){
            long next = v.getIntValue("% Operator", where);
            double newValue = floatValue - next*Math.floor(floatValue/next);
            return new RuntimeFloatValue(newValue);
        }
        if(v instanceof RuntimeFloatValue){
            double next = v.getFloatValue("% Operator", where);
            double newValue = floatValue - next*Math.floor(floatValue/next);
            return new RuntimeFloatValue(newValue);
        }
        runtimeError("'%' or 'moudlo' is undefined for "
         + typeName() + " and " + v.typeName(), where);
        return null;
    }

    @Override
    public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where) {
        if(v instanceof RuntimeFloatValue){
            return new RuntimeFloatValue((floatValue * v.getFloatValue("* Operator", where)));
        }
        if(v instanceof RuntimeIntValue){
            return new RuntimeFloatValue(floatValue * v.getIntValue("* Operator", where));
        }
        runtimeError("'*' or 'multiplication' is undefined for "
         + typeName() + " and " + v.typeName() , where);
        return null;
    }

    @Override
    public RuntimeValue evalNegate(AspSyntax where) {
        return new RuntimeFloatValue(-floatValue);
    }

    @Override
    public RuntimeValue evalNot(AspSyntax where) {
        if(floatValue == 0){
            return new RuntimeBoolValue(true);
        }
        return new RuntimeBoolValue(false);
    }

    @Override
    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
        if(v instanceof RuntimeIntValue){
            return new RuntimeBoolValue(floatValue != v.getIntValue("!= Operator", where));
        }
        if(v instanceof RuntimeFloatValue){
            return new RuntimeBoolValue(floatValue != v.getFloatValue("!= Operator", where));
        }
        runtimeError("'!=' or 'not equal' is undefined for "
         + typeName() + " and " + v.typeName(),  where);
        return null;
    }

    @Override
    public RuntimeValue evalPositive(AspSyntax where) {
        return new RuntimeFloatValue(floatValue);
    }

    @Override
    public RuntimeValue evalSubtract(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeFloatValue){
            return new RuntimeFloatValue(floatValue - v.getFloatValue(" - Operator", where));
        }
        if(v instanceof RuntimeIntValue){
            return new RuntimeFloatValue(floatValue - v.getIntValue(" - Operator", where));
        }
        runtimeError("'-' or 'subtraction' is undefined for "
         + typeName() + " and " + v.typeName(), where);
        return null;
    }

}