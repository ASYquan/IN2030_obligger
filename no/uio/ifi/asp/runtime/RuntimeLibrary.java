// © 2021 Dag Langmyhr, Institutt for informatikk, Universitetet i Oslo

package no.uio.ifi.asp.runtime;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.NoSuchElementException;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeLibrary extends RuntimeScope {
    private Scanner keyboard = new Scanner(System.in);

    public RuntimeLibrary() {
        //assigning float
        assign("float", new RuntimeFuncValue("float"){
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> params, AspSyntax where){
                checkNumParams(params, 1, "float", where);
                try {
                    return new RuntimeFloatValue(params.get(0).getFloatValue("int", where));
                } catch (Exception e) {
                    runtimeError(params.get(0).typeName() + " " + params.get(0).toString() + " is not a legal float", where);
                    return null;
                }
            }
            
        });

        //assigning int
        assign("int", new RuntimeFuncValue("int"){
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> params, AspSyntax where){
                checkNumParams(params, 1, "int", where);
                try {
                    return new RuntimeIntValue(params.get(0).getIntValue("int", where));
                } catch (Exception e) {
                    runtimeError(params.get(0).typeName() + " " + params.get(0).toString() + " is not a legal int", where);
                    return null;
                }
            } 
        });

        //assignning str
        assign("str", new RuntimeFuncValue("str"){
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> params, AspSyntax where){
                checkNumParams(params, 1, "str", where);
                return new RuntimeStringValue(params.get(0).getStringValue("str", where));
            }
        });

        //assigning print
        assign("print", new RuntimeFuncValue("print"){
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> params, AspSyntax where){
                for(RuntimeValue param : params){
                    String outString = param.getStringValue("print", where);
                    System.out.print(outString.substring(0, outString.length()));
                    System.out.print(" ");
                }
                System.out.println();
                return new RuntimeNoneValue();
            }
        });

        //assigning input
        assign("input", new RuntimeFuncValue("input"){
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> params, AspSyntax where){
                String outString = params.get(0).getStringValue("print", where);
                outString = outString.substring(0, outString.length());
                System.out.print(outString);
                return new RuntimeStringValue(keyboard.nextLine());
            }
        });

        //assigning len
        assign("len", new RuntimeFuncValue("len"){
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> params, AspSyntax where){
                checkNumParams(params, 1, "len", where);
                return params.get(0).evalLen(where);
            }
        });

        //assigning range
        assign("range", new RuntimeFuncValue("range"){
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> params, AspSyntax where){
                checkNumParams(params, 2, "range", where);
                long start = params.get(0).getIntValue("range", where);
                long end = params.get(1).getIntValue("range", where);
                if(start > end){
                    runtimeError("range: start must be less than end", where);
                }
                RuntimeListValue list = new RuntimeListValue(new ArrayList<RuntimeValue>());
                for(long i = start; i <= end; i++){
                    list.appendElement(new RuntimeIntValue(i));
                }
                return list;
            }
        });
    }


    private void checkNumParams(ArrayList<RuntimeValue> actArgs, int nCorrect, String id, AspSyntax where) {
	if (actArgs.size() != nCorrect)
	    RuntimeValue.runtimeError("Wrong number of parameters to "+id+"!",where);
    }
}
