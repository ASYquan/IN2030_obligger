package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspListDisplay extends AspAtom {
    ArrayList<AspExpr> expr = new ArrayList<>();
    ArrayList<Token> tokenList = new ArrayList<>();

    AspListDisplay(int n) {
        super(n);
    }

    static AspListDisplay parse(Scanner s) {
        enterParser("list display");

        AspListDisplay  ald = new AspListDisplay(s.curLineNum());

        skip(s, leftBracketToken);
        if (s.curToken().kind != rightBracketToken) {
            while (true) {
                ald.expr.add(AspExpr.parse(s));
                ald.tokenList.add(s.curToken());
                if (s.curToken().kind != commaToken) {
                    break;
                }
                skip(s, commaToken);
            }
        }
        skip(s, rightBracketToken);

        leaveParser("list display");
        return ald;
    }

    @Override
    public void prettyPrint() {
        int nPrinted = 0;

        prettyWrite("[");
        for (AspExpr prettyExpr: expr) {
            if (nPrinted > 0) {
                prettyWrite(", ");
            }
            prettyExpr.prettyPrint();
            nPrinted++;
        }
        prettyWrite("]");
    }
    

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeListValue returnList = new RuntimeListValue(new ArrayList <RuntimeValue>());

        for(AspExpr ex: expr){
            returnList.appendElement(ex.eval(curScope));
        }
        return returnList;
    }

}
