package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

class AspFactor extends AspSyntax{
    ArrayList<AspFactorPrefix> factorPrefix = new ArrayList<>();
    ArrayList<AspPrimary> primary = new ArrayList<>();
    ArrayList<AspFactorOpr> factorOpr = new ArrayList<>();

    AspFactor(int n){
        super(n);
    }

    static AspFactor parse (Scanner s){
        enterParser("factor");

        AspFactor af = new AspFactor(s.curLineNum());

        while(true){
            if(s.isFactorPrefix()){
                af.factorPrefix.add(AspFactorPrefix.parse(s));
            }
            else{
                af.factorPrefix.add(null);
            }

            af.primary.add(AspPrimary.parse(s));

            if(!s.isFactorOpr()){
                break;
            }
            af.factorOpr.add(AspFactorOpr.parse(s));
        }

        leaveParser("factor");
        return af;
    }

    @Override
    public void prettyPrint(){
        for(int i = 0; i < primary.size(); i++){
            if(factorPrefix.get(i) != null){
                factorPrefix.get(i).prettyPrint();
            }
            primary.get(i).prettyPrint();

            if(i < factorOpr.size()){
                factorOpr.get(i).prettyPrint();
            }
        }
    }


    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue runtimeValue = primary.get(0).eval(curScope);
        if (factorPrefix.size() != 0) {
            //Checks for plus and minus token of the first number
            if (factorPrefix.get(0) != null) {
                TokenKind tokenKind = factorPrefix.get(0).token.kind;
                switch (tokenKind) {
                    case plusToken:
                        runtimeValue = runtimeValue.evalPositive(this);
                        break;
                    case minusToken:
                        runtimeValue = runtimeValue.evalNegate(this);
                        break;
                    default:
                        Main.panic("Illegal factor operator" + tokenKind);
                }
            }
        }
            //iteration for plus- and minus tokens
            for (int i = 1; i < primary.size(); i++) {
                TokenKind tokenKind = factorOpr.get(i-1).kind;
                RuntimeValue next = primary.get(i).eval(curScope);
                if (factorPrefix.get(i) != null) {
                    TokenKind nextKind = factorPrefix.get(i).token.kind;
                    switch (nextKind) {
                        case plusToken:
                            next = next.evalPositive(this);
                            break;
                        case minusToken:
                            next = next.evalNegate(this);
                            break;
                        default:
                            Main.panic("Illegal factor operator" + tokenKind);
                    }
                }
                //checks respective operation
                switch (tokenKind) {
                    case percentToken:
                        runtimeValue = runtimeValue.evalModulo(next, this);
                        break;
                    case slashToken:
                        runtimeValue = runtimeValue.evalDivide(next, this);
                        break;
                    case doubleSlashToken:
                        runtimeValue = runtimeValue.evalIntDivide(next, this);
                        break;
                    case astToken:
                        runtimeValue = runtimeValue.evalMultiply(next, this);
                        break;
                    default:
                        Main.panic("illefal factor operator " + tokenKind);
                }

            }
        return runtimeValue;
    }

}
