package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

class AspTermOpr extends AspSyntax{
    String opr;
    TokenKind kinder;

    AspTermOpr(int n){
        super(n);
    }

    public static AspTermOpr parse(Scanner s){
        enterParser("term opr");
        AspTermOpr ato = new AspTermOpr(s.curLineNum());
        if(s.curToken().kind == plusToken){
          skip(s, plusToken);
          ato.opr = " + ";
          ato.kinder = plusToken;
          leaveParser("term opr");
          return ato;
        }
        else if(s.curToken().kind == minusToken){
          skip(s, minusToken);
          ato.opr = " - ";
          ato.kinder = minusToken;
          leaveParser("term opr");
          return ato;
        }
        return null;
    }

    @Override
    public void prettyPrint() {
        TokenKind thisTokenKind = kinder;
        switch (thisTokenKind) {
            case plusToken:
                prettyWrite(" + ");
                break;
            case minusToken:
                prettyWrite(" - ");
                break;
            default: 
                break;
        }
    }


    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return null;
    }

}
