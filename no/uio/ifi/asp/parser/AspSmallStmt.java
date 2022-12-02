package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;


abstract class AspSmallStmt extends AspSyntax {
    AspSmallStmt(int n) {
        super(n);
    }
    static AspSmallStmt parse(Scanner s) {
        enterParser("small stmt");
        AspSmallStmt as = null;

        if (s.anyEqualToken()){
          as = AspAssignment.parse(s);
        }
        else{
          switch(s.curToken().kind){
            case globalToken:
              as = AspGlobalStmt.parse(s);
              break;
            case returnToken:
              as = AspReturnStmt.parse(s);
              break;
            case passToken:
              as = AspPassStmt.parse(s);
              break;
            default:
              as = AspExprStmt.parse(s);
              break;
          }
        }
        leaveParser("small stmt");
        return as;
        }
    abstract RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue;

}
