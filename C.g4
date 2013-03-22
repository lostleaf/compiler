grammar C;

program: (declaration | function_definition)+
;

declaration: 'typedef' type_specifier declarators ';'
| type_specifier init_declarators? ';'
;

function_definition: type_specifier plain_declarator '(' parameters? ')' compound_statement
;

parameters: plain_declaration (',' plain_declaration)* (',' '...')?
;

declarators: declarator (',' declarator)*
;

init_declarators: init_declarator (',' init_declarator)*
;

init_declarator: declarator ('=' initializer)?
;

initializer: assignment_expression
| '{' initializer (',' initializer)* '}'
;

type_specifier: 'void' | 'char' | 'int' | typedef_name
| struct_or_union identifier? '{' (type_specifier declarators ';')+ '}'
| struct_or_union identifier
;

struct_or_union: 'struct' | 'union'
;

plain_declaration: type_specifier declarator
;

declarator: plain_declarator '(' parameters? ')'
| plain_declarator ('[' constant_expression ']')*
;

plain_declarator: '*'* identifier
;

statement: expression_statement
| compound_statement
| selection_statement
| iteration_statement
| jump_statement
;

expression_statement: expression? ';'
;

compound_statement: '{' declaration* statement* '}'
;

selection_statement: 'if' '(' expression ')' statement ('else' statement)?
;

iteration_statement: 'while' '(' expression ')' statement
| 'for' '(' expression? ';' expression? ';' expression? ')' statement
;

jump_statement: 'continue' ';'
| 'break' ';'
| 'return' expression? ';'
;

expression: assignment_expression (',' assignment_expression)*
;

assignment_expression: logical_or_expression
| unary_expression assignment_operator assignment_expression
;

assignment_operator: '=' | '*=' | '/=' | '%=' | '+=' | '-=' | '<<=' | '>>=' | '&=' | '^=' | '|='
;

constant_expression: logical_or_expression
;

logical_or_expression: logical_and_expression ('||' logical_and_expression)*
;

logical_and_expression: inclusive_or_expression ('&&' inclusive_or_expression)*
;

inclusive_or_expression: exclusive_or_expression ('|' exclusive_or_expression)*
;

exclusive_or_expression: and_expression ('^' and_expression)*
;

and_expression: equality_expression ('&' equality_expression)*
;

equality_expression: relational_expression (equality_operator relational_expression)*
;

equality_operator: '==' | '!='
;

relational_expression: shift_expression (relational_operator shift_expression)*
;

relational_operator: '<' | '>' | '<=' | '>='
;

shift_expression: additive_expression (shift_operator additive_expression)*
;

shift_operator: '<<' | '>>'
;

additive_expression: multiplicative_expression (additive_operator multiplicative_expression)*
;

additive_operator: '+' | '-'
;

multiplicative_expression: cast_expression (multiplicative_operator cast_expression)*
;

multiplicative_operator: '*' | '/' | '%'
;

cast_expression: unary_expression
| '(' type_name ')' cast_expression
;

type_name: type_specifier '*'*
;

unary_expression: postfix_expression
| '++' unary_expression
| '--' unary_expression
| unary_operator cast_expression
| 'sizeof' unary_expression
| 'sizeof' '(' type_name ')'
;

unary_operator: '&' | '*' | '+' | '-' | '~' | '!'
;

postfix_expression: primary_expression postfix*
;

postfix: '[' expression ']'
| '(' arguments ')'
| '.' identifier
| '->' identifier
| '++'
| '--'
;

arguments: assignment_expression (',' assignment_expression)*
;

primary_expression: identifier
| constant
| string
| '(' expression ')'
;

constant: integer_constant
| character_constant
;

integer_constant : Integer_constant;
character_constant : Character_constant;
identifier : Identifier;
typedef_name : Identifier;
string : String;

// LEXER =====================================================
SPACE : [ \r\t\n]+ -> channel(HIDDEN);

MULTI_COMMENT : '/*' .*? '*/' -> channel(HIDDEN);

SINGLE_COMMENT : '//' ~[\r\n]* -> channel(HIDDEN);



Hex_Digit : Digit | 'A'..'F' | 'a'..'f';

Identifier : Letter (Letter|Digit)*  ;

fragment Letter : [a-zA-Z_$] ;
fragment Digit : [0-9] ;

Character_constant : '\'' ~('\\') | ( '\\' ('b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\') )'\'';

Integer_constant :  ( '0' ('x'|'X') Hex_Digit+ ) | ( ('1'..'9') Digit* ) | ( '0' ('0'..'7')* );

String : '\"' (~('\\') | ( '\\' ('b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\') ))*? '\"';

