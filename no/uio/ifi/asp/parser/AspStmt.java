package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

abstract class AspStmt extends AspSyntax{

    AspStmt(int n){
        super(n);
    }

    static AspStmt parse(Scanner s){
        enterParser("stmt");

        AspStmt as = null;

        switch (s.curToken().kind){
            case forToken:
                as = AspCompoundStmt.parse(s);
                break;

            case ifToken:
                as = AspCompoundStmt.parse(s);
                break;

            case whileToken:
                as = AspCompoundStmt.parse(s);
                break;

            case defToken:
                as = AspCompoundStmt.parse(s);
                break;
            case nameToken:
                as = AspSmallStmtList.parse(s);
                break;
            case passToken:
                as = AspSmallStmtList.parse(s);
                break;
            case returnToken:
                as = AspSmallStmtList.parse(s);
                break;
            case newLineToken:
                skip(s, newLineToken);
                break;
            default:
                as = AspSmallStmtList.parse(s);
                break;
        }

        leaveParser("stmt");
        //System.out.println(as);
        return as;
    }
}
