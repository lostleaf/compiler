package appetizer.syntactic;

%%

%unicode
%line
%column
%cup
%implements Symbols

%{
	private StringBuilder sb = null;


	private void err(String message) {
		System.out.println("Scanning error in line " + yyline + ", column " + yycolumn + ": " + message);
	}

	private java_cup.runtime.Symbol tok(int kind) {
		return new java_cup.runtime.Symbol(kind, yyline, yycolumn);
	}

	private java_cup.runtime.Symbol tok(int kind, Object value) {
		return new java_cup.runtime.Symbol(kind, yyline, yycolumn, value);
	}
%}

%eofval{
	{
		if (yystate() == MULTICOMMENT) {
			err("Comment symbol do not match (EOF)!");
		}
		return tok(EOF, null);
	}
%eofval}

LineTerm = \n|\r|\r\n
Identifier = [a-zA-Z_$][0-9a-zA-Z_$]*
Integer = ([1-9][0-9]*)|(0[xX][0-9a-fA-F]+)|(0[0-7]+)
Whitespace = {LineTerm}|[ \t\f]

%state	STRING, MULTICOMMENT, SINGLECOMMENT

%%

<SINGLECOMMENT> {
  {LineTerm} 		{ yybegin(YYINITIAL); }
  . 						{ /*do nothing*/ }
}


<MULTICOMMENT>	{
  \*\/ 					{ yybegin(YYINITIAL); }
  .|{LineTerm} 	{ /*do nothing*/ }
}



<STRING>	{
	\" 						{ yybegin(YYINITIAL);return tok(STRING, sb.toString()); }
  .|\\\"        { tmpString.append(yytext()); }
}

<YYINITIAL> {

/* comments */

  \/\/          {yybegin(SINGLECOMMENT);}
  \/\*          {yybegin(MULTICOMMENT);}

/* keywords */

	"typedef"  		{ return tok(TYPEDEF); }
	"void"  			{ return tok(VOID); }
	"char"  			{ return tok(CHAR); }
	"int"  				{ return tok(INT); }
	"struct"  		{ return tok(STRUCT); }
	"union"  			{ return tok(UNION); }
	"if"  				{ return tok(IF); }
	"else"  			{ return tok(ELSE); }
	"while"  			{ return tok(WHILE); }
	"for"  				{ return tok(FOR); }
	"continue"  	{ return tok(CONTINUE); }
	"break"  			{ return tok(BREAK); }
	"return"  		{ return tok(RETURN); }
	"sizeof"  		{ return tok(SIZEOF); }

/* operator */
	"(" 					{ return tok(LPAREN); }
	")" 					{ return tok(RPAREN); }
	"{" 					{ return tok(LBRACE); }
	"}" 					{ return tok(RBRACE); }
	"[" 					{ return tok(LBRACKET); }
	"]" 					{ return tok(RBRACKET); }
	"," 					{ return tok(COMMA); }
	";" 					{ return tok(SEMICOLON); }
	"|" 					{ return tok(BITOR); }
	"&" 					{ return tok(BITAND); }
	"^" 					{ return tok(BITXOR); }
	"!" 					{ return tok(NOT); }
	"<" 					{ return tok(GREATER); }
	">" 					{ return tok(LESS); }
	"~" 					{ return tok(COMPLEMENT); }
	"." 					{ return tok(ATTRIBUTE); }
	"+" 					{ return tok(PLUS); }
	"-" 					{ return tok(MINUS); }
	"*" 					{ return tok(TIMES); }
	"/" 					{ return tok(DIVIDE); }
	"=" 					{ return tok(ASSIGN); }
	"%" 					{ return tok(MOD); }
	"||"  				{ return tok(OR); }
	"&&"  				{ return tok(AND); }
	"=="  				{ return tok(EQ); }
	"!="  				{ return tok(NE); }
	"<="  				{ return tok(LE); }
	">="  				{ return tok(GE); }
	"<<"  				{ return tok(SHL); }
	">>"  				{ return tok(SHR); }
	"++"  				{ return tok(INC); }
	"--"  				{ return tok(DEC); }
	"->"  				{ return tok(PTR); }
	"..." 				{ return tok(ELLIPSIS); }
	"*="  				{ return tok(MUL_ASSIGN); }
	"/="  				{ return tok(DIV_ASSIGN); }
	"%="  				{ return tok(MOD_ASSIGN); }
	"+="  				{ return tok(ADD_ASSIGN); }
	"-="  				{ return tok(SUB_ASSIGN); }
	"<<=" 				{ return tok(SHL_ASSIGN); }
	">>=" 				{ return tok(SHR_ASSIGN); }
	"&="  				{ return tok(AND_ASSIGN); }
	"^="  				{ return tok(XOR_ASSIGN); }
	"|="  				{ return tok(OR_ASSIGN); }

/* string */

  \"						{ tmpString = new StringBuilder(); yybegin(STRING); }

	{Identifier} 	{ return tok(ID,  new String(yytext()) ); }
	{Integer} 		{ return tok(NUM, new Integer(yytext()) ); }
	{Whitespace} 	{ /* do nothing */ }


	[^] { throw new RuntimeException("Illegal character " + yytext() +
																		" in line " + (yyline + 1) + ", column " + (yycolumn + 1)); }
}
