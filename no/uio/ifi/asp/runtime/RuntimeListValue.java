package no.uio.ifi.asp.runtime;

//import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;

import java.util.ArrayList;


public class RuntimeListValue extends RuntimeValue{
    private ArrayList<RuntimeValue> elements;

    public RuntimeListValue(ArrayList<RuntimeValue> list){
        elements = list; 
    }

    //Helps with appending/adding an element to a list
    public void appendList(ArrayList<RuntimeValue> addedElements){
        elements.addAll(addedElements);
    }
    
    public void appendElement(RuntimeValue element){
        elements.add(element);
    }

    //Helps with retrieval of elements in a list
    public ArrayList<RuntimeValue> getElements(AspSyntax where){
        return elements;
    }

    //Helps with indexation of an element in a list
    public RuntimeValue indexElement(int index){
        return elements.get(index);
    }

    @Override
    public String toString() {
        String print = "[";
        for (int i = 0; i < elements.size(); i++){
            print += elements.get(i);
            if( i != elements.size() - 1){
                print += ", "; 
            }
        }
        print += "]";
        return print;
    }

    @Override
    public String typeName(){
        return "List";
    }

    @Override
    public String getStringValue(String what, AspSyntax where) {
        return this.toString();
    }

    @Override
    public boolean getBoolValue(String what, AspSyntax where) {
        return (elements.size() != 0 );
    }

    @Override
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
        if(v instanceof RuntimeListValue){
            if(this.getElements(where).size() == ((RuntimeListValue) v).getElements(where).size()){
                for(int i=0; i<this.getElements(where).size() - 1; i++){
                    if(getElements(where).get(i) != ((RuntimeListValue) v).getElements(where).get(i)){
                        return new RuntimeBoolValue(false);
                    }
                }
                return new RuntimeBoolValue(true);
            }
            else{
                return new RuntimeBoolValue(false);
            }
        }
        return new RuntimeBoolValue(false);
    }

    @Override
    public RuntimeValue evalLen(AspSyntax where) {
        return new RuntimeIntValue((long) elements.size());
    }

    @Override
    public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where) {
        if(v instanceof RuntimeIntValue){
            long value = v.getIntValue("* Operator", where);
            RuntimeListValue newList = new RuntimeListValue(new ArrayList<RuntimeValue>());

            for(int i = 0; i < value; i++){
                newList.appendList(elements);
            }
            return newList;
        }
        runtimeError("'*' or 'list multiplication' is undefined for "
         + typeName() + " and " + v.typeName(), where);
        return null;
    }

    @Override
    public RuntimeValue evalNot(AspSyntax where) {
        return new RuntimeBoolValue(elements.size() == 0);
    }

    @Override
    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
        if(v instanceof RuntimeListValue){
            if(this.getElements(where).size() == ((RuntimeListValue) v).getElements(where).size()){
                for(int i=0; i<this.getElements(where).size() - 1; i++){
                    if(getElements(where).get(i) != ((RuntimeListValue) v).getElements(where).get(i)){
                        return new RuntimeBoolValue(true);
                    }
                }
                return new RuntimeBoolValue(false);
            }else{
                return new RuntimeBoolValue(true);
            }
        }
        return new RuntimeBoolValue(true);
    }

    @Override
    public RuntimeValue evalSubscription(RuntimeValue v, AspSyntax where) {
        int i = 0;
        if (v instanceof RuntimeIntValue){
            i = (int) v.getIntValue("Subscription", where);
        }
        else if(v instanceof RuntimeFloatValue){
            i = (int) v.getFloatValue("Subscription", where);
        }
        if(i > elements.size() - 1){
            runtimeError("TypeError: Index out of bounds for " + typeName(), where);
        }
        return elements.get(i);
    }

    @Override
    public void evalAssignElem(RuntimeValue inx, RuntimeValue val, AspSyntax where) {
        if (inx.getIntValue("Assignment", where) > elements.size() - 1){
            runtimeError("Assigning to an element not allowed for "+typeName()+"!", where);
        }
        elements.set((int) inx.getIntValue("Assignment", where), val);
    }





    





}