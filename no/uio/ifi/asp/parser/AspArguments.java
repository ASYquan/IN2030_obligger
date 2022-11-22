package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

class AspArguments extends AspPrimarySuffix{
    ArrayList<AspExpr> expr = new ArrayList<>();

    AspArguments(int n){
        super(n);
    }

    static AspArguments parse(Scanner s){
        enterParser("arguments");

        AspArguments aa = new AspArguments(s.curLineNum());

        skip(s, leftParToken);

        if(s.curToken().kind != rightParToken){
            while(true){
                aa.expr.add(AspExpr.parse(s));
                if(s.curToken().kind != commaToken){
                    break;
                }
                skip(s, commaToken);
            }
        }
        skip(s, rightParToken);

        leaveParser("arguments");
        return aa;
    }

    @Override
    public void prettyPrint(){
        prettyWrite("(");
        int nPrinted = 0;
        for(AspExpr ae : expr){
            if(nPrinted > 0){
                prettyWrite(", ");
            }
            ae.prettyPrint();
            nPrinted++;
        }
        prettyWrite(")");
    }
    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        ArrayList<RuntimeValue> returnList = new ArrayList<>();
        for (AspExpr x: expr)
        {
          returnList.add(x.eval(curScope));
        }
    
        return new RuntimeListValue(returnList);
    } 
}
