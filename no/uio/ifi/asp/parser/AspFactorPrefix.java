package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

class AspFactorPrefix extends AspSyntax{
    Token token;
    AspFactorPrefix(int n){
        super(n);
    }

    static AspFactorPrefix parse(Scanner s){
        enterParser("factor prefix");

        AspFactorPrefix afp = new AspFactorPrefix(s.curLineNum());

        afp.token = s.curToken();
        skip(s, s.curToken().kind);

        leaveParser("factor prefix");
        return afp;
    }

    @Override
    public void prettyPrint() {
        
        prettyWrite(token.toString());
        prettyWrite(" ");
    }


    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
	//-- Must be changed in part 3:
	return null;
    }
}
