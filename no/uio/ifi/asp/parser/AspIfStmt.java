package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspIfStmt extends AspCompoundStmt {
    ArrayList <AspExpr> expr = new ArrayList<>();
    ArrayList <AspSuite> suite = new ArrayList<>();
    AspIfStmt elifs;
    Boolean hasElse = false;

    AspIfStmt(int n) {
        super(n);
    }

    static AspIfStmt parse(Scanner s) {
        enterParser("if stmt");

        AspIfStmt ais = new AspIfStmt(s.curLineNum());

        skip(s, ifToken);

        while (true) {
            ais.expr.add(AspExpr.parse(s));
            skip(s, colonToken);
            ais.suite.add(AspSuite.parse(s));
            if (s.curToken().kind != elifToken) {
                break;
            }
            skip(s, elifToken);
        }
        if (s.curToken().kind == newLineToken){
            skip(s, newLineToken);
        }
        if (s.curToken().kind == elseToken) {
            ais.hasElse = true;
            skip(s, elseToken);
            skip(s, colonToken);
            ais.suite.add(AspSuite.parse(s));
        }
        leaveParser("if stmt");
        return ais;
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
    //Part 4
    return null;
  }
}
