package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

class AspBooleanLiteral extends AspAtom{
    String bool;
    Boolean boolReturn;

    AspBooleanLiteral(int n){
        super(n);
    }

    static AspBooleanLiteral parse(Scanner s){
        AspBooleanLiteral abl = new AspBooleanLiteral(s.curLineNum());

        enterParser("boolean literal");

        if(s.curToken().kind == trueToken){
            abl.bool = "True";
            abl.boolReturn = true;
            skip(s, trueToken);
        }
        else if(s.curToken().kind == falseToken){
            abl.bool = "False";
            abl.boolReturn = false;
            skip(s, falseToken);
        }
        else{
            parserError("Expected an expression boolean literal, but found a " + s.curToken().kind + "!", s.curLineNum());
        }

        leaveParser("boolean literal");
        return abl;
    }

    @Override
    public void prettyPrint(){
        prettyWrite(bool);
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return new RuntimeBoolValue(boolReturn);
    }
}
