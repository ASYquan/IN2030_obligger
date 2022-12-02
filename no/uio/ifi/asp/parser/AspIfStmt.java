package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspIfStmt extends AspCompoundStmt {
    ArrayList <AspExpr> expr = new ArrayList<>();
    ArrayList <AspSuite> suite = new ArrayList<>();
    AspSuite hasSuite;

    AspIfStmt(int n) {
        super(n);
    }

    public static AspIfStmt parse(Scanner s) {
        enterParser("if stmt");

        AspIfStmt ifstmt = new AspIfStmt(s.curLineNum());
        skip(s, ifToken);
        ifstmt.expr.add(AspExpr.parse(s));
        skip(s, colonToken);
        ifstmt.suite.add(AspSuite.parse(s));

        while (s.curToken().kind == elifToken) {
            skip(s, elifToken);
            ifstmt.expr.add(AspExpr.parse(s));
            skip(s, colonToken);
            ifstmt.suite.add(AspSuite.parse(s));
        }
        if (s.curToken().kind == elseToken) {
            skip(s, elseToken);
            skip(s, colonToken);
            ifstmt.hasSuite = AspSuite.parse(s);
        }
        leaveParser("if stmt");
        return ifstmt;
    }

  @Override
  public void prettyPrint(){
    prettyWrite("if ");
    expr.get(0).prettyPrint();
    prettyWrite(": ");
    suite.get(0).prettyPrint();

    if(expr.size() > 1){
      for(int i = 1; i <expr.size(); i++){
        prettyWrite("elif ");
        expr.get(i).prettyPrint();
        prettyWrite(": ");
        suite.get(i).prettyPrint();
      }
    }
    if(suite.size() > expr.size()){
      prettyWrite("else");
      prettyWrite(": ");

      suite.get(suite.size()-1).prettyPrint();
    }
  }


  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = null;
        int i = 0;

        for (; i < expr.size(); i++) {
            v = expr.get(i).eval(curScope);
            if (v.getBoolValue("bool", this) == true) {
                trace("if True alt #" + (i+1) + ": ...");
                v = suite.get(i).eval(curScope);
                return v;
            }
        }
        if (hasSuite != null) {
            trace("else: ...");
            v = hasSuite.eval(curScope);
            return v;
        }
        return null;
    }
}
  

