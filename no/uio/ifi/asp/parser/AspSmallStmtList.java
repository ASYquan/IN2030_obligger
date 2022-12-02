package no.uio.ifi.asp.parser;
import java.util.Collections;
import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

class AspSmallStmtList extends AspStmt{
    ArrayList<AspSmallStmt> smallStmt = new ArrayList<>();

    public static int nCountSC = 0;
    AspSmallStmtList(int n){
        super(n);
    }
	public static AspSmallStmtList parse(Scanner s){
        AspSmallStmtList assl = new AspSmallStmtList(s.curLineNum());
        enterParser("small stmt list");
        assl.smallStmt = new ArrayList <> ();

        assl.smallStmt.add(AspSmallStmt.parse(s));
        while (s.curToken().kind == semicolonToken){
            skip(s, semicolonToken);

            if (s.curToken().kind == newLineToken){
                break;
            }
            assl.smallStmt.add(AspSmallStmt.parse(s));
        }

        skip(s, newLineToken);
        leaveParser("small stmt list");
        return assl;
    }


    @Override
    public void prettyPrint(){
        int counter = 1;
        for(AspSmallStmt small: smallStmt){

        if (counter > 1)
        {
            prettyWrite("; ");
        }
        small.prettyPrint();
        counter++;
        }
        prettyWriteLn();
    }
  
    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue{
      RuntimeValue returns = null;
  
      for(AspSmallStmt small: smallStmt){
        returns = small.eval(curScope);
      }
      return returns;
    }
}
