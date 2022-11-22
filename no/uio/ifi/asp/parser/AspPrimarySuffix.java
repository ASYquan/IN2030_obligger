package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

abstract class AspPrimarySuffix extends AspSyntax{

    AspPrimarySuffix(int n){
        super(n);
    }

    static AspPrimarySuffix parse(Scanner s){
        enterParser("primary suffix");

        AspPrimarySuffix aps = null;

        switch (s.curToken().kind){
            case leftBracketToken:
                aps = AspSubscription.parse(s); break;
            case leftParToken:
                aps = AspArguments.parse(s); break;
            default:
                parserError("Expected an expression primary suffix, but found a " + s.curToken().kind + "!", s.curLineNum());
        }
        leaveParser("primary suffix");
        return aps;
    }
}
