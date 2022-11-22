package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

class AspPrimary extends AspSyntax{
    AspAtom aa;
    ArrayList<AspPrimarySuffix> primarySuffix = new ArrayList<>();


    AspPrimary(int n){
        super(n);
    }

    static AspPrimary parse(Scanner s){
        enterParser("primary");

        AspPrimary ap = new AspPrimary(s.curLineNum());

        ap.aa = AspAtom.parse(s);

        if(s.curToken().kind == leftBracketToken || s.curToken().kind == leftParToken){
            while(s.curToken().kind == leftBracketToken || s.curToken().kind == leftParToken){
                ap.primarySuffix.add(AspPrimarySuffix.parse(s));
            }
        }

        leaveParser("primary");
        return ap;
    }

    @Override
    public void prettyPrint() {
        aa.prettyPrint();
        if(primarySuffix.size() != 0){
            for(AspPrimarySuffix aps : primarySuffix){
                aps.prettyPrint();
            }
        }
    }


    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        //-- Must be changed in part 3:
        RuntimeValue returnValue = aa.eval(curScope);
        ArrayList<RuntimeValue> args;
    
        //Handling of lists and other supported dataStructures, in consideration of primary suffix index
        for(AspPrimarySuffix suffix : primarySuffix){
          if(returnValue instanceof RuntimeDictValue || returnValue instanceof RuntimeListValue
              || returnValue instanceof RuntimeStringValue){
              returnValue = returnValue.evalSubscription(suffix.eval(curScope), this);
          }else if(suffix instanceof AspArguments){
            args = suffix.eval(curScope).getElements(this);
            returnValue = returnValue.evalFuncCall(args, this);
          }
        }
        return returnValue;
      }

}
