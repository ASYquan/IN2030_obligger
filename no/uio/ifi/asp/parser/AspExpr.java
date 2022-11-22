// © 2021 Dag Langmyhr, Institutt for informatikk, Universitetet i Oslo

package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspExpr extends AspSyntax {
    //-- Must be changed in part 2:
    ArrayList<AspAndTest> andTests = new ArrayList<>();

    AspExpr(int n) {
        super(n);
    }


    public static AspExpr parse(Scanner s) {
    	enterParser("expr");

	    //-- Must be changed in part 2:
	    AspExpr ae = new AspExpr(s.curLineNum());
 
        while(true){
            ae.andTests.add(AspAndTest.parse(s));
            if(s.curToken().kind != orToken){
                break;
            }
            else {
                skip(s, orToken);
            }
        }

        leaveParser("expr");
        return ae;
    }


    @Override
    public void prettyPrint() {
	//-- Must be changed in part 2:
        int count = 0;
        for(AspAndTest prettyAndTest: andTests){
            if(count > 0){
                prettyWrite(" or ");
            }
            prettyAndTest.prettyPrint();
            count++;
        }
    }


    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
	//-- Must be changed in part 3:
        RuntimeValue returnValue = andTests.get(0).eval(curScope);
        for(int i=1; i<andTests.size(); i++){
            if(returnValue.getBoolValue("or operand", this)){
                return returnValue;
            }
            returnValue = andTests.get(i).eval(curScope);
        }
        return returnValue;
    }
}
