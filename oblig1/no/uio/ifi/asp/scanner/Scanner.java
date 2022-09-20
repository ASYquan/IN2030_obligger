package no.uio.ifi.asp.scanner;

import java.io.*;
import java.util.*;

import no.uio.ifi.asp.main.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class Scanner {
    private LineNumberReader sourceFile = null;
    private String curFileName;
    private ArrayList<Token> curLineTokens = new ArrayList<>();

    private Stack<Integer> indents = new Stack<>();
    private final int TABDIST = 4;

    public Scanner(String fileName){
        curFileName = fileName;
        indents.push(0);

        try{
            sourceFile = new LineNumberReader(new InputStreamReader(new FileInputStream(fileName),"UTF-8"));
        }
        catch (IOException e){
            scannerError("Cannot read " + fileName + "!");
        }
    }

    private void scannerError(String message){
        String m = "Asp scanner error";
        if(curLineNum() > 0)
        m += " on line " + curLineNum();
        m += ": " + message;

        Main.error(m);
    }

    public Token curToken(){
        while (curLineTokens.isEmpty()){
            readNextLine();
        }
        return curLineTokens.get(0);
    }

    public void readNextToken(){
        if (! curLineTokens.isEmpty())
        curLineTokens.remove(0);
    }

    private void readNextLine(){
        curLineTokens.clear();

        // Read the next line:
        String line = null;

	 	try {
		    line = sourceFile.readLine();
		    if (line == null) {
				//Creates dedent tokens at end of line
				while(!indents.empty()){
					if(indents.pop() > 0){
						curLineTokens.add(new Token(dedentToken));
					}
				  }
			sourceFile.close();
			sourceFile = null;
		    } else {
			Main.log.noteSourceLine(curLineNum(), line);
		    }
		} catch (IOException e) {
		    sourceFile = null;
		    scannerError("Unspecified I/O error!");
		}
	


    //-- Must be changed in part 1:
	//-- Traversing the current line to find char 

        //Used to check tokenKinds 
		boolean ignore = false;
		String lineString = "";

		if(line != null){
            //Calculates amount of blanks and handles indentation/dedentation of the current line
            int blanks = 0;
            try{
                while(line.charAt(blanks) == ' '){
                    blanks++;
                }
                if(line.charAt(blanks) == '#'){
                    assert true;
                }
                //Algorithm for indent/dendent handling from the lecture
                else{
                    line = expandLeadingTabs(line); 
                    int indent = findIndent(line);

                    if( indent > indents.peek()){
                        indents.push(indent);
                        curLineTokens.add(new Token(indentToken, curLineNum()));
                    }
                    else{
                        while(indent < indents.peek()){
                            indents.pop();
                            curLineTokens.add(new Token(dedentToken, curLineNum()));
                        }
                    }
                    if(indent != indents.peek()){
                        int lineNr = curLineNum();
                        System.out.println("Error : Bad use of indentation / dedentation" + lineNr);
                        System.exit(1);
                    }
                }
            }
            catch(Exception e){
            }

            //Traversal of the line to check the chars of the line.
            int pos = 0;
            while (pos < line.length()){
                char currChar  = line.charAt(pos++);
                // if the char is a comment and the last char is QQString then skip to next line
                if(currChar == '#' && line.charAt(pos-1) != '\"'){
                    break;
                }

                 // if char is a string
                if(currChar == '\"' || currChar == '\''){
                    //boolean isString = true;
                    int i = 0;  //QString
                    int j = 0;  //QQString

                    boolean isString = true;
                    try{
                    //differentiates between QQString and QString
                        //If QQString it will check for the first and second occurence 
                        //-indicating a string
                        if(currChar == '\"'){
                            j = line.indexOf('\"', line.indexOf('\"') + 1);
                            //If indexation error, will print out error
                            if(i == - 1 || j == -1){
                                System.out.println("Error : String literal not terminated on line:" + curLineNum());
                                System.exit(1);
                            }
                        
                            while(isString == true){
                                if (line.charAt(pos) == '\"'){
                                    isString = false;
                                    pos++;
                                    break;
                                }
                                lineString += line.charAt(pos++);
                            }
                        }
                        //If QString it will check for the first and second occurence
                        //-indicating a string
                        else if(currChar == '\''){
                            i = line.indexOf('\'', line.indexOf('\'') + 1);
                            //If indexation error, will print out error
                            if(i == - 1 || j == -1){
                                System.out.println("Error : String literal not terminated on line:" + curLineNum());
                                System.exit(1);
                            }
                        
                            while(isString == true){
                                if (line.charAt(pos) == '\''){
                                    isString = false;
                                    pos++;
                                    break;
                                }
                                lineString += line.charAt(pos++);
                            }
                        }


                        if(isString == false) {
                            Token strToken = new Token(stringToken,(curLineNum()));
                            strToken.stringLit = lineString;
                            curLineTokens.add(strToken);
                            lineString = "";
                        }
                    }catch (Exception e){
                    }
                }

                // if char is a name 
                if(isLetterAZ(currChar)){
                    boolean isName = true;
                    lineString += currChar;
                    try{
                        while(isLetterAZ(line.charAt(pos)) || isDigit(line.charAt(pos))){
                            lineString += line.charAt(pos++);
                        }
                    }
                    catch (Exception e){
                    }
                    for(TokenKind kind : TokenKind.values()){
                        if(kind.toString().equals(lineString)){
                            curLineTokens.add(new Token(kind, curLineNum()));
                            isName = false;
                            lineString = "";
                        }
                    }
                    if (isName){
                        Token nToken = new Token(nameToken, curLineNum());
                        nToken.name = lineString;
                        curLineTokens.add(nToken);
                        lineString = "";
                    }

                }
                //lineString = "";
                // if char is a number 
                if(isDigit(currChar)){
                    lineString += currChar;
                    boolean isNr = false;
                    try{
                       while(isDigit(line.charAt(pos)) || line.charAt(pos) == '.'){
                            lineString += line.charAt(pos++);
                        }
                    }
                    catch (Exception e){
                    }
                    //Splitting the current line to check if there is a digit before current character
                   //-and storing zeroToken
                    if(lineString.indexOf('.') == -1){
                        if(lineString.indexOf('0') == 0 && lineString.length() > 1){
                            char[] num = lineString.toCharArray();
                            Token intToken = new Token(integerToken, curLineNum());
                            intToken.integerLit = Character.getNumericValue(num[0]);
                            curLineTokens.add(intToken);
                        }
                        Token intToken = new Token(integerToken, curLineNum());
                        intToken.integerLit = Integer.parseInt(lineString);
                        curLineTokens.add(intToken);
                    }
                    else{
                        double floaty = Double.parseDouble(lineString); 
                        if(floaty % 1 == 0){
                            //Catches invalid floats 
                            System.out.println("Error:" + curLineNum() + " Illegal float literal: " + currChar);
                            System.exit(1);
                        }
                        else{
                            Token fToken = new Token(floatToken, curLineNum());
                            fToken.floatLit = Double.parseDouble(lineString);
                            curLineTokens.add(fToken);
                        }
                    }
                
                }
                lineString = "";

            

                //if Char is an operator or delimiter
                if(currChar != ' ' && !isLetterAZ(currChar) && !isDigit(currChar)){
                    Boolean done = false; 
                    if(currChar == '='){
                        try{
                            if(line.charAt(pos++) == '='){
                                curLineTokens.add(new Token(TokenKind.doubleEqualToken, curLineNum()));
                                done = true;
                            }
                            else{
                                pos--;
                            }
                        }catch(Exception e){
                        }
                    }
                    else if(currChar == '/'){
                        try{
                            if(line.charAt(pos++) == '/'){
                                curLineTokens.add(new Token(TokenKind.doubleSlashToken, curLineNum()));
                                done = true;
                            }
                            else{
                                pos--;
                            }
                        }catch(Exception e){
                        }
                    }

                    else if(currChar == '>'){
                        try{
                            if(line.charAt(pos++) == '='){
                                curLineTokens.add(new Token(TokenKind.greaterEqualToken, curLineNum()));
                                done = true;
                            }
                            else{
                                pos--;
                            }
                        }catch(Exception e){
                        }
                    }

                    else if(currChar == '<'){
                        try{
                            if(line.charAt(pos++) == '='){
                                curLineTokens.add(new Token(TokenKind.lessEqualToken, curLineNum()));
                                done = true;
                            }
                            else{
                                pos--;
                            }
                        }catch(Exception e){
                        }
                    }
                    else if(currChar == '+'){
                        try{
                            if(line.charAt(pos++) == '='){
                                curLineTokens.add(new Token(TokenKind.plusToken, curLineNum()));
                                done = true;
                            }
                            else{
                                pos--;
                            }
                        }catch(Exception e){
                        }
                    }

                    else if(currChar == '!'){
                        try{
                            if(line.charAt(pos++) == '='){
                                curLineTokens.add(new Token(TokenKind.notEqualToken, curLineNum()));
                                done = true;
                            }
                            else{
                                pos--;
                            }
                        } catch(Exception e){
                        }
                    }
                    else if(currChar == ':'){
                        try{
                            if(line.charAt(pos++) == '='){
                                curLineTokens.add(new Token(TokenKind.colonToken, curLineNum()));
                                done = true;
                            }
                            else{
                                pos--;
                            }
                        }catch(Exception e){
                        }
                    }

                    if(done == false){
                        lineString = String.valueOf(currChar);
                        for(TokenKind kind : TokenKind.values()){
                            if(kind.toString().equals(lineString)){
                                curLineTokens.add(new Token(kind, curLineNum()));
                                lineString = "";
                                done = true;
                            }
                        }
                    }
                }
            }
        } 
        //Terminate Line:
        // if Line = null, add EndOfFile-token after dedent token
        else{
            curLineTokens.add(new Token(eofToken, curLineNum()));
            ignore = true;
        }

        //Checks if a line has been ignored.
        if (!ignore){
                curLineTokens.add(new Token(newLineToken, curLineNum()));
            } 
        //Checks if a line has not been ignored or if there is a remaining token
        if(ignore || curLineTokens.size() != 1) {
            for (Token t: curLineTokens) 
                Main.log.noteToken(t);
        }
    }



    public int curLineNum(){
        return sourceFile!=null ? sourceFile.getLineNumber():0;
    }

    private int findIndent(String s){
        int indent = 0;

        while (indent<s.length() && s.charAt(indent)==' ') indent++;
        return indent;
    }

    //From the weekly tasks 
    private String expandLeadingTabs(String s){
        String newS = "";
        for (int i = 0;  i < s.length();  i++){
            char c = s.charAt(i);

            if (c == '\t'){
                do{
                    newS += " ";
                }
                while (newS.length()%TABDIST > 0);
            }

            else if(c == ' '){
                newS += " ";
  	        }

            else{
                newS += s.substring(i);
                break;
  	        }
        }
        return newS;
    }

    private boolean isLetterAZ(char c){
        return ('A'<=c && c<='Z') || ('a'<=c && c<='z') || (c=='_');
    }

    private boolean isDigit(char c){
        return '0'<=c && c<='9';
    }

    public boolean isCompOpr(){
        TokenKind k = curToken().kind;

        if(k == lessToken || k == greaterToken || k == doubleEqualToken || k == greaterEqualToken || k == lessEqualToken || k == notEqualToken){
          return true;
        }
        return false;
    }

    public boolean isFactorPrefix(){
        TokenKind k = curToken().kind;

        if(k == plusToken || k == minusToken){
          return true;
        }
        return false;
    }

    public boolean isFactorOpr(){
        TokenKind k = curToken().kind;

        if(k == percentToken || k == slashToken || k == doubleSlashToken || k == astToken){
          return true;
        }
        return false;
    }

    public boolean isTermOpr(){
        TokenKind k = curToken().kind;

        if(k == plusToken || k == minusToken){
          return true;
        }
        return false;
    }

    public boolean anyEqualToken(){
        for (Token t: curLineTokens){
        if (t.kind == equalToken) return true;
        if (t.kind == semicolonToken) return false;
    }
    return false;
    }
}