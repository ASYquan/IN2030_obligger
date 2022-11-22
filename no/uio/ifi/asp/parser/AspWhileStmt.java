package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

class AspWhileStmt extends AspCompoundStmt{
    AspExpr ae;
    AspSuite as;

    AspWhileStmt(int n){
        super(n);
    }

    static AspWhileStmt parse(Scanner s){
        enterParser("while stmt");
        AspWhileStmt aws = new AspWhileStmt(s.curLineNum());

        skip(s, whileToken);
        aws.ae = AspExpr.parse(s);
        skip(s, colonToken);
        aws.as = AspSuite.parse(s);

        leaveParser("while stmt");
        //System.out.println(aws);
        
        return aws;
    }


    @Override
    public void prettyPrint() {
        prettyWrite("while ");
        ae.prettyPrint();
        prettyWrite(": ");
        as.prettyPrint();
    }



    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
	//-- Must be changed in part 3:
    // If test-value is false, stop the loop
    while(true){
        RuntimeValue testValue = as.eval(curScope);
        if( !testValue.getBoolValue("while loop test", this)){
          break;
        }
        trace("while True:...");
        ae.eval(curScope);
      }
      trace("while False:");
      return null;
    }
}
