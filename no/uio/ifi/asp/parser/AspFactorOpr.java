package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

class AspFactorOpr extends AspSyntax{
    String opr;
    TokenKind kind;

    AspFactorOpr(int n){
        super(n);
    }

    static AspFactorOpr parse(Scanner s){
        enterParser("factor opr");

        AspFactorOpr afo = new AspFactorOpr(s.curLineNum());

        if(s.curToken().kind == astToken){
            afo.opr = "*";
            afo.kind = astToken;
            skip(s, astToken);
        }
        else if(s.curToken().kind == slashToken){
            afo.opr = "/";
            afo.kind = slashToken;
            skip(s, slashToken);
        }
        else if(s.curToken().kind == percentToken){
            afo.opr = "%";
            afo.kind = percentToken;
            skip(s, percentToken);
        }
        else if(s.curToken().kind == doubleSlashToken){
            afo.opr = "//";
            afo.kind = doubleSlashToken;
            skip(s, doubleSlashToken);
        }

        leaveParser("factor opr");
        return afo;
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
