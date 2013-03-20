str = <<STR
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
STR


token = []
str.each_line { |line|
	line.match(/tok\((.*?)\);/)
	token << $1 if $1
}

puts token.join(", ")
puts token.size
