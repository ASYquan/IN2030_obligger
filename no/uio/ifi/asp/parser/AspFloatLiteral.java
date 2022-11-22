package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

class AspFloatLiteral extends AspAtom{
    double floatNr;

    AspFloatLiteral(int n){
        super(n);
    }

    static AspFloatLiteral parse(Scanner s){
        enterParser("float literal");

        AspFloatLiteral afl = new AspFloatLiteral(s.curLineNum());

        afl.floatNr = s.curToken().floatLit;
        s.readNextToken();
        leaveParser("float literal");
        return afl;
    }

    @Override
    public void prettyPrint() {
	    prettyWrite(floatNr + "");
    }


    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return new RuntimeFloatValue(floatNr);
    }
}
