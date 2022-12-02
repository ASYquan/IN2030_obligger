package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

class AspForStmt extends AspCompoundStmt{
    AspName an;
    AspExpr ae;
    AspSuite as;

    AspForStmt(int n){
        super(n);
    }

    static AspForStmt parse(Scanner s){
        enterParser("for stmt");
        AspForStmt afs = new AspForStmt(s.curLineNum());

        skip(s, forToken);
        afs.an = AspName.parse(s);
        skip(s, inToken);
        afs.ae = AspExpr.parse(s);
        skip(s, colonToken);
        afs.as = AspSuite.parse(s);

        leaveParser("for stmt");
        return afs;
    }

    @Override
    public void prettyPrint() {
        prettyWrite("for ");
        an.prettyPrint();
        prettyWrite(" in ");
        ae.prettyPrint();
        prettyWrite(": ");
        as.prettyPrint();
    }


    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
	//-- Must be changed in part 3:
        RuntimeValue testList = ae.eval(curScope);

        if(testList instanceof RuntimeListValue){
            ArrayList<RuntimeValue> list = ((RuntimeListValue) testList).getElements(this);

            for(int i=0; i<list.size()-1; i++){
                trace("for #" + (i+1) + ": " + an.name + " = " + list.get(i).showInfo());
                curScope.assign(an.name, list.get(i));
                testList = ae.eval(curScope);
                as.eval(curScope);
            }

        }
        else if(testList instanceof RuntimeStringValue){
            String text = testList.getStringValue("Subscription", this);
            for(int i=0; i<text.length()-1; i++){
                trace("for #" + (i+1) + ": " + an.name + " = " + text.charAt(i));
                curScope.assign(an.name, new RuntimeStringValue(""+text.charAt(i)));
            }
        }
        else{
            RuntimeValue.runtimeError("forStmt - Expression is not itereable", this);
        }
        return testList;
  }
}


