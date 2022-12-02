package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

class AspReturnStmt extends AspSmallStmt{
    AspExpr ae;
    int lineNr;
    AspReturnStmt(int n){
        super(n);
    }

    static AspReturnStmt parse(Scanner s){
        enterParser("return stmt");

        AspReturnStmt ars = new AspReturnStmt(s.curLineNum());

        skip(s, returnToken);
        ars.ae = AspExpr.parse(s);

        leaveParser("return stmt");
        return ars;
    }

    @Override
    public void prettyPrint() {
        prettyWrite("return ");
        ae.prettyPrint();
    }

    //Had heard that by throwing the return-value, it "throws" the return-value higher up in
    //the datastrcture, instead of continuing recursive-call. 
    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = ae.eval(curScope);
        trace("return " + v.showInfo());
        throw new RuntimeReturnValue(v, lineNr);
    }

}
