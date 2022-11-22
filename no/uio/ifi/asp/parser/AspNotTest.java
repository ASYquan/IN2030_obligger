package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

class AspNotTest extends AspSyntax{
    AspComparison ac;
    Boolean not = false;

    AspNotTest(int n){
        super(n);
    }

    static AspNotTest parse (Scanner s){
        enterParser("not test");

        AspNotTest ant = new AspNotTest(s.curLineNum());

        if(s.curToken().kind == notToken){
            skip(s, notToken);
            ant.not = true;
        }
        ant.ac = AspComparison.parse(s);

        leaveParser("not test");
        return ant;
    }

    @Override
    public void prettyPrint() {
        if(not){
            prettyWrite("not ");
        }
        ac.prettyPrint();
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue returnValue = ac.eval(curScope);
        if (not){
          return returnValue.evalNot(this);
        }
        else{
          return returnValue;
        }
    }
}
