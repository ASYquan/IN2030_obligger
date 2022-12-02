package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspFuncDef extends AspCompoundStmt{
    AspName str_name;
    AspSuite as;
    ArrayList<AspName> name = new ArrayList<>();

    AspFuncDef(int n){
        super(n);
    }

    static AspFuncDef parse(Scanner s)
    {
      enterParser("Function definition");
      AspFuncDef afd = new AspFuncDef(s.curLineNum());
      afd.name = new ArrayList <> ();
  
      // Lagrer funksjonsnavn
      skip(s, defToken);
      afd.str_name = AspName.parse(s);
      skip(s, leftParToken);
  
      // Lagrer navn på formelle paramatre
      if (s.curToken().kind != rightParToken)
      {
        afd.name.add(AspName.parse(s));
      }
      while (s.curToken().kind != rightParToken)
      {
        skip(s, commaToken);
        afd.name.add(AspName.parse(s));
      }
      skip(s, rightParToken);
      skip(s, colonToken);
  
      // Lagrer innsiden av funksjonen
      afd.as = AspSuite.parse(s);
      leaveParser("Function definition");
  
      return afd;
    }

   @Override
    public void prettyPrint() {
        int nPrinted = 0;
        prettyWrite("def ");
        name.get(0).prettyPrint();
        prettyWrite(" ");
        prettyWrite("(");
        if(name.size() > 1){
            for(int i = 1; i < name.size(); i++){
                name.get(i).prettyPrint();
                nPrinted++;
                if(nPrinted < name.size()-1){
                    prettyWrite(", ");
                }
            }
        }
        prettyWrite("): ");

        as.prettyPrint();
        prettyWriteLn();

    } 


    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue{
  
        ArrayList<RuntimeValue> args = new ArrayList<>();
        RuntimeStringValue func = new RuntimeStringValue(str_name.name);
  
        for (AspName formal: name){
            args.add(new RuntimeStringValue(formal.name));
        }

        //Stores relevant parameters as a string in the input 
        RuntimeFuncValue newFunction = new RuntimeFuncValue(func, args, curScope, this);
        curScope.assign(str_name.name, newFunction);
    
        trace("def " + str_name.name);
    
        return null;
    }
  
    //A function used in RuntimeFuncValue to create a scope 
    public RuntimeValue runFunc(RuntimeScope curScope) throws RuntimeReturnValue{
      return as.eval(curScope);
    }
    

    public RuntimeValue evalFunc(RuntimeScope curScope) throws RuntimeReturnValue {
	//-- Must be changed in part 3:
        return as.eval(curScope);
    }

}
