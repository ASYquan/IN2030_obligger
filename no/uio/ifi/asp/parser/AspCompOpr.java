package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

class AspCompOpr extends AspSyntax{
    String opr;
    TokenKind kind;

    AspCompOpr(int n){
        super(n);
    }

    static AspCompOpr parse(Scanner s){
        enterParser("comp opr");

        AspCompOpr aco = new AspCompOpr(s.curLineNum());

        if(s.curToken().kind == lessToken){
            aco.opr = "<";
            aco.kind = lessToken;
            skip(s, lessToken);
        }
        else if(s.curToken().kind == greaterToken){
            aco.opr = ">";
            aco.kind = greaterToken;
            skip(s, greaterToken);
        }
        else if(s.curToken().kind == doubleEqualToken){
            aco.opr = "==";
            aco.kind = doubleEqualToken;
            skip(s, doubleEqualToken);
        }
        else if(s.curToken().kind == greaterEqualToken){
            aco.opr = ">=";
            aco.kind = greaterEqualToken;
            skip(s, greaterEqualToken);
        }
        else if(s.curToken().kind == lessEqualToken){
            aco.opr = "<=";
            aco.kind = lessEqualToken;
            skip(s, lessEqualToken);
        }
        else if(s.curToken().kind == notEqualToken){
            aco.opr = "!=";
            aco.kind = notEqualToken;
            skip(s, notEqualToken);
        }
        else{
            parserError("Expected an expression comp opr, but found a " + s.curToken().kind + "!", s.curLineNum());
        }
        leaveParser("comp opr");
        return aco;
    }

    @Override
    public void prettyPrint(){
        prettyWrite(" ");
        prettyWrite(opr);
        prettyWrite(" ");
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return null;
    }
}
