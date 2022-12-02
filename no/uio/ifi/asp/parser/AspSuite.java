package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspSuite extends AspSyntax {

	AspSuite(int n){
		super(n);
    }
	AspSmallStmtList smallstmtlist; 
	ArrayList<AspStmt> stmtLst = null;
	
    static int nCountSC = 0;	
	public static AspSuite parse(Scanner s){
		enterParser("suite");
		AspSuite suite = new AspSuite(s.curLineNum());

		suite.stmtLst = new ArrayList<>();
		if (s.curToken().kind == newLineToken){
			//System.out.println("newLineToken");
			skip(s, newLineToken);
			skip(s, indentToken);
			while(s.curToken().kind != dedentToken){
				if(s.curToken().kind == newLineToken){
					skip(s, newLineToken);
				}
				else{
					suite.stmtLst.add(AspStmt.parse(s));
				}
			}
			skip(s, dedentToken);
		}
		else {
			suite.smallstmtlist = AspSmallStmtList.parse(s);
			nCountSC = AspSmallStmtList.nCountSC;
		}
		leaveParser("suite");
		return suite;
	}



	@Override
	public void prettyPrint() {
	    if(smallstmtlist == null){
	      prettyWriteLn();
	      prettyIndent();

	      for(AspStmt stmt: stmtLst ){
		stmt.prettyPrint();
	      }
	      prettyDedent();
	    }else{
	      smallstmtlist.prettyPrint();
	    }
	  }	


	@Override
	public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = null;

        if (smallstmtlist != null) {
            v = smallstmtlist.eval(curScope);
        } else {
            for (AspStmt as : stmtLst) {
                v = as.eval(curScope);
            }
        }
        return v;
    }
    

}
