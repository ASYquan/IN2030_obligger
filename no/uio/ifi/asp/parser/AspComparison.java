package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

class AspComparison extends AspSyntax{
    ArrayList<AspCompOpr> compOpr = new ArrayList<>();
    ArrayList<AspTerm> term = new ArrayList<>();

    AspComparison(int n){
        super(n);
    }

    static AspComparison parse(Scanner s){
        enterParser("comparison");

        AspComparison ac = new AspComparison(s.curLineNum());

        while(true){
            ac.term.add(AspTerm.parse(s));
            if(s.isCompOpr()){
                ac.compOpr.add(AspCompOpr.parse(s));
            }
            else{
                break;
            }
        }
        leaveParser("comparison");
        return ac;
    }

    @Override
    public void prettyPrint() {

        int nPrinted = 0;

        for (AspTerm at : term){
            at.prettyPrint();
        
            if(nPrinted < compOpr.size()){
                compOpr.get(nPrinted++).prettyPrint();
            }
        }
    }
    

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = term.get(0).eval(curScope);
        for (int i = 1; i < term.size(); i++) {
            TokenKind k = compOpr.get(i-1).kind;

            switch (k) {
                case lessToken:
                    v = v.evalLess(term.get(i).eval(curScope), this);
                    if (!v.getBoolValue("comparison", this)){
                        return new RuntimeBoolValue(false);
                    }
                    v = term.get(i).eval(curScope);
                    break;
                case greaterToken:
                    v = v.evalGreater(term.get(i).eval(curScope), this);
                    if (!v.getBoolValue("comparison", this)){
                        return new RuntimeBoolValue(false);
                    }
                    v = term.get(i).eval(curScope);
                    break;
                case doubleEqualToken:
                    v = v.evalEqual(term.get(i).eval(curScope), this);
                    if (!v.getBoolValue("comparison", this)){
                        return new RuntimeBoolValue(false);
                    }
                    v = term.get(i).eval(curScope);
                    break;
                case lessEqualToken:
                    v = v.evalLessEqual(term.get(i).eval(curScope), this);
                    if (!v.getBoolValue("comparison", this)){
                        return new RuntimeBoolValue(false);
                    }
                    v = term.get(i).eval(curScope);
                    break;
                case greaterEqualToken:
                    v = v.evalGreaterEqual(term.get(i).eval(curScope), this);
                    if (!v.getBoolValue("comparison", this)){
                        return new RuntimeBoolValue(false);
                    }
                    v = term.get(i).eval(curScope);
                    break;
                case notEqualToken:
                    v = v.evalNotEqual(term.get(i).eval(curScope), this);
                    if (!v.getBoolValue("comparison", this)){
                        return new RuntimeBoolValue(false);
                    }
                    v = term.get(i).eval(curScope);
                    break;
                default:
                    Main.panic("Illegal comp operator: " + k + "!");
            }
            if (i == term.size()-1) {
                return new RuntimeBoolValue(true);
            }
        }
	    return v;
    }
    
}
