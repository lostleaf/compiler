grammar C;



program: (declaration | functionDefinition)+
       ;

declaration: 'typedef' typeSpecifier declarators ';'
           | typeSpecifier initDeclarators? ';'
           ;

functionDefinition: typeSpecifier plainDeclarator '(' parameters? ')' compoundStatement
                   ;

parameters: plainDeclaration (',' plainDeclaration)* (',' '...')?
          ;

declarators: declarator (',' declarator)*
           ;

initDeclarators: initDeclarator (',' initDeclarator)*
                ;

initDeclarator: declarator ('=' initializer)?
               ;

initializer: assignmentExpression
           | '{' initializer+ '}'
           ;

typeSpecifier: 'void' | 'char' | 'int' | typedefName
              | structOrUnion identifier? '{' plainDeclaration+ '}'
              | structOrUnion identifier
              ;

structOrUnion: 'struct' | 'union'
               ;

plainDeclaration: typeSpecifier declarator+
                 ;

declarator: plainDeclarator '(' parameters? ')'
          | plainDeclarator ('[' constantExpression ']')*
          ;

plainDeclarator: '*'* identifier
                ;

statement: expressionStatement
         | compoundStatement
         | selectionStatement
         | iterationStatement
         | jumpStatement
         ;

expressionStatement: expression? ';'
                    ;

compoundStatement: '{' declaration* statement* '}'
                  ;

selectionStatement: 'if' '(' expression ')' statement ('else' statement)?
                   ;

iterationStatement: 'while' '(' expression ')' statement
                   | 'for' '(' expression? ';' expression? ';' expression? ')' statement
                   ;

jumpStatement: 'continue' ';'
              | 'break' ';'
              | 'return' expression? ';'
              ;

expression: assignmentExpression (',' assignmentExpression)*
          ;

assignmentExpression: logicalOrExpression
                     | unaryExpression assignmentOperator assignmentExpression
                     ;

assignmentOperator: '=' | '*=' | '/=' | '%=' | '+=' | '-=' | '<<=' | '>>=' | '&=' | '^=' | '|='
                   ;

constantExpression: logicalOrExpression
                   ;

logicalOrExpression: logicalAndExpression ('||' logicalAndExpression)*
                     ;

logicalAndExpression: inclusiveOrExpression ('&&' inclusiveOrExpression)*
                      ;

inclusiveOrExpression: exclusiveOrExpression ('|' exclusiveOrExpression)*
                       ;

exclusiveOrExpression: andExpression ('^' andExpression)*
                       ;

andExpression: equalityExpression ('&' equalityExpression)*
              ;

equalityExpression: relationalExpression (equalityOperator relationalExpression)*
                   ;

equalityOperator: '==' | '!='
                 ;

relationalExpression: shiftExpression (relationalOperator shiftExpression)*
                     ;

relationalOperator: '<' | '>' | '<=' | '>='
                   ;

shiftExpression: additiveExpression (shiftOperator additiveExpression)*
                ;

shiftOperator: '<<' | '>>'
              ;

additiveExpression: multiplicativeExpression (additiveOperator multiplicativeExpression)*
                   ;

additiveOperator: '+' | '-'
                 ;

multiplicativeExpression: castExpression (multiplicativeOperator castExpression)*
                         ;

multiplicativeOperator: '*' | '/' | '%'
                       ;

castExpression: unaryExpression
               | '(' typeName ')' castExpression
               ;

typeName: typeSpecifier '*'*
         ;

unaryExpression: postfixExpression
                | '++' unaryExpression
                | '--' unaryExpression
                | unaryOperator castExpression
                | 'sizeof' unaryExpression
                | 'sizeof' '(' typeName ')'
                ;

unaryOperator: '&' | '*' | '+' | '-' | '~' | '!'
              ;

postfixExpression: primaryExpression postfix*
                  ;

postfix: '[' expression ']'
       | '(' arguments? ')'
       | '.' identifier
       | '->' identifier
       | '++'
       | '--'
       ;

arguments: assignmentExpression (',' assignmentExpression)*
         ;

primaryExpression: identifier
                  | constant
                  | string
                  | '(' expression ')'
                  ;

constant: integerConstant
        | characterConstant
        ;

identifier: Identifier;

typedefName: Identifier;

integerConstant: Hex | Dec | Oct;

characterConstant: Character;


string: String;


// LEXER =====================================================

Whitespace : [ \r\t\n]+ -> channel(HIDDEN);

fragment EOL : '\r' | '\n' | ('\r''\n') ;

Multi_comment : '/*' .*? '*/' -> channel(HIDDEN);
Single_comment : '//' ~[\r\n]* (EOL) -> channel(HIDDEN);

Hex : '0' ('x'|'X') HexDigit+ ;
Dec : '0' | ('1'..'9' Digit+)  ;
Oct : '0' ('0'..'7')+  ;

fragment Digit : '0'..'9';
fragment HexDigit : (Digit |'a'..'f'|'A'..'F') ;
fragment Letter: '$' | 'A'..'Z' | '_' |  'a'..'z';

Character : '\'' ( ('\\' ('b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\')) | ~('\''|'\\') ) '\'';
String : '"' ( ('\\' ('b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\')) | ~('\\'|'"') )* '"';

Identifier : Letter (Letter | Digit)*;
