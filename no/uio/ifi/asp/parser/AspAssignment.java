package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

class AspAssignment extends AspSmallStmt{
    AspName an;
    AspExpr ae;

    ArrayList<AspSubscription> sub = new ArrayList<>();

    AspAssignment(int n){
        super(n);
    }

    static AspAssignment parse(Scanner s){
        enterParser("assignment");

        AspAssignment aa = new AspAssignment(s.curLineNum());
        aa.an = AspName.parse(s);
        while(s.curToken().kind != equalToken){
            aa.sub.add(AspSubscription.parse(s));
        }
        skip(s, equalToken);
        aa.ae = AspExpr.parse(s);

        leaveParser("assignment");
        return aa;
    }

    @Override
    public void prettyPrint() {
        an.prettyPrint();
        for(AspSubscription as : sub){
            as.prettyPrint();
        }
        prettyWrite(" = ");
        ae.prettyPrint();
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
       RuntimeValue expression = ae.eval(curScope);

      // Inserting AspName.name in scope
      if(sub.size() == 0){
        String id = an.name;
        curScope.assign(id, expression);
        trace(id + " = " + expression.toString());
      }
      else{
        RuntimeValue v1 = an.eval(curScope);
  
        for(int i=0; i<sub.size()-1; i++){
          RuntimeValue v2 = sub.get(i).eval(curScope);
          v1 = v1.evalSubscription(v2, this);
        }

        AspSubscription lastSub = sub.get(sub.size()-1);
        RuntimeValue index = lastSub.eval(curScope);
        trace(an.name + "[" + index.toString() + "] = " + expression.toString());
        v1.evalAssignElem(index, expression, this);
      }
      return null;
    }
  }

