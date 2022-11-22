package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspDictDisplay extends AspAtom {
    ArrayList<AspStringLiteral> stringLiteral = new ArrayList<>();
    ArrayList<AspExpr> expr = new ArrayList<>();
    
    AspDictDisplay(int n){
        super(n);
    }

    static AspDictDisplay parse(Scanner s) {
        enterParser("dict display");

        AspDictDisplay aDictDis = new AspDictDisplay(s.curLineNum());

        skip(s, leftBraceToken);
        if (s.curToken().kind  == stringToken) {
            while (true) {
                aDictDis.stringLiteral.add(AspStringLiteral.parse(s));
                skip(s, colonToken);
                aDictDis.expr.add(AspExpr.parse(s));
                if(s.curToken().kind != commaToken){
                    break;
                }
                skip(s, commaToken);
            }
        }
        skip(s, rightBraceToken);

        leaveParser("dict display");

        return aDictDis;
    }

    @Override
    public void prettyPrint() {
        int nPrinted = 0;
        prettyWrite("{");
        for (AspStringLiteral strLiteral: stringLiteral) {
            if (nPrinted > 0) {
                prettyWrite(", ");
            }
            AspExpr expression = expr.get(nPrinted);
            strLiteral.prettyPrint();
            prettyWrite(":");
            expression.prettyPrint();
            nPrinted++;
        
        }
        prettyWrite("}");
    }
    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        //-- Must be changed in part 3:
        RuntimeDictValue dict = new RuntimeDictValue(new ArrayList <RuntimeValue>(),
        new ArrayList <RuntimeValue>());

        int i = 0; //iterating using indexation to retrieve keys and values
        for(AspStringLiteral key: stringLiteral){
            dict.evalAssignElem(key.eval(curScope), expr.get(i).eval(curScope), this);
            i++;
        }
        return dict;
    }
}
