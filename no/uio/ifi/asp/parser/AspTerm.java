package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

class AspTerm extends AspSyntax{
    ArrayList<AspFactor> factor = new ArrayList<>();
    ArrayList<AspTermOpr> termOpr = new ArrayList<>();

    AspTerm(int n){
        super(n);
    }

    static AspTerm parse(Scanner s){
        enterParser("term");

        AspTerm at = new AspTerm(s.curLineNum());

        while(true){
            at.factor.add(AspFactor.parse(s));
            if(s.isTermOpr()){
                at.termOpr.add(AspTermOpr.parse(s));
            }
            else{
                break;
            }
        }

        leaveParser("term");
        return at;
    }

    @Override
   public void prettyPrint() {
        int nPrinted = 0;
        for (AspFactor factors: factor) {
            factors.prettyPrint();

            if(nPrinted < termOpr.size()){
                if(termOpr.get(nPrinted) != null){
                    AspTermOpr termOp = termOpr.get(nPrinted);
                    termOp.prettyPrint();
                }
            }
            nPrinted++;
        }
    }


    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue returnValue = factor.get(0).eval(curScope);

        //evals as plus or minus depending on operator
        for(int i=1; i<factor.size(); i++){
            TokenKind kind = termOpr.get(i-1).kinder;
            if(kind == minusToken){
                returnValue = returnValue.evalSubtract(factor.get(i).eval(curScope), this);
            }
            else{
                returnValue = returnValue.evalAdd(factor.get(i).eval(curScope), this);
            }
        }
        return returnValue;
    }
}
