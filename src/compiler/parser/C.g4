grammar C;

@header {
  package compiler.parser;
  import compiler.absyn.*;
  import compiler.symbol.*;
  import static compiler.symbol.Symbol.symbol;
}


program returns[Program ret]
locals[List<Node> list=new ArrayList<Node>()]
@after {	$ret = new Program($list);}
:
(de=declaration {$list.add($de.ret);} | fd=function_definition {$list.add($fd.ret);})+
;

declaration returns[Declaration ret]
: 'typedef' ts=type_specifier de=declarators ';' {$ret = new Declaration($ts.ret,$de.ret);}
| {InitDeclarators init = null;}
  ts=type_specifier (id=init_declarators {init = $id.ret;})? ';'
  {$ret = new Declaration($ts.ret, init);}
;

function_definition returns[FunctionDefinition ret]
: {Parameters para = null;}
  ts=type_specifier pd=plain_declarator '(' (p=parameters {para = $p.ret;})? ')'
   cs=compound_statement
  {$ret = new FunctionDefinition($ts.ret,$pd.ret,para,$cs.ret);}
;

parameters returns[Parameters ret]
locals[List<PlainDeclaration> list = new ArrayList<PlainDeclaration>(), boolean varpar = false]
@after{$ret = new Parameters($list,$varpar);}
: pd=plain_declaration {$list.add($pd.ret);}
  (',' pd=plain_declaration {$list.add($pd.ret);})* (',' '...'{$varpar = true;})?
;

declarators returns[Declarators ret]
: {List<Declarator> decl = new ArrayList<Declarator>();}
  d=declarator {decl.add($d.ret);} (',' d=declarator {decl.add($d.ret);} )*
  {$ret = new Declarators(decl);}
;

init_declarators returns[InitDeclarators ret]
: {List<InitDeclarator> id = new ArrayList<InitDeclarator>();}
  i=init_declarator {id.add($i.ret);} (',' i=init_declarator {id.add($i.ret);})*
  {$ret = new InitDeclarators(id);}
;

init_declarator returns[InitDeclarator ret]
: {Initializer init = null;}
  d=declarator ('=' i=initializer {init = $i.ret;})?
  {$ret = new InitDeclarator($d.ret, init);}
;

initializer returns[Initializer ret]
: assignment_expression {$ret = new Initializer($assignment_expression.ret, null);}
| {List<Initializer> ini = new ArrayList<Initializer>();}
  '{' (initializer {ini.add($initializer.ret);} )+ '}'
  {$ret = new Initializer(null, ini);}
;

type_specifier returns[TypeSpecifier ret]
: 'void' {$ret = new VoidType();}
| 'char' {$ret = new CharType();}
| 'int'  {$ret = new IntType();}
| typedef_name {$ret = new NameType($typedef_name.ret);}
| {
    List<PlainDeclaration> pla = new ArrayList<PlainDeclaration>();
    Id iden = null;
  }
  sou=struct_or_union (i=identifier {iden = $i.ret;})?
  '{' (p=plain_declaration {pla.add($p.ret);})+ '}'
  {$ret = new RecordType($sou.ret, iden, pla);}
| sou=struct_or_union i=identifier
  {$ret = new RecordType($sou.ret, $i.ret, null);}
;

struct_or_union returns[StructUnion ret]
: 'struct'  {$ret = StructUnion.STRUCT;}
| 'union'   {$ret = StructUnion.UNION;}
;


plain_declaration returns[PlainDeclaration ret]
: {List<Declarator> decl = new ArrayList<Declarator>();}
  t=type_specifier (d=declarator {decl.add($d.ret);})+
  {$ret = new PlainDeclaration($t.ret,decl);}
;


declarator returns [Declarator ret]
: {Parameters para = null;}
  p1=plain_declarator '(' (p2=parameters {para = $p2.ret;} )? ')'
  {$ret = new FunDeclarator($p1.ret, para);}
| {List<ConstExpr> expr = new ArrayList<ConstExpr>();}
  p=plain_declarator ('[' c=constant_expression ']'{expr.add($c.ret);})*
  {$ret = new ArrDeclarator($p.ret,expr);}
;

plain_declarator returns[PlainDeclarator ret]
locals[int count = 0]
: ('*' {$count++;})* i=identifier {$ret = new PlainDeclarator($count,$i.ret);}
;

statement returns[Stmt ret]
: expression_statement {$ret = $expression_statement.ret;}
| compound_statement {$ret = $compound_statement.ret;}
| selection_statement {$ret = $selection_statement.ret;}
| iteration_statement {$ret = $iteration_statement.ret;}
| jump_statement {$ret = $jump_statement.ret;}
;

expression_statement returns[ExprStmt ret]
: {Expr expr = null;}
  (e=expression {expr = $e.ret;})? ';' {$ret = new ExprStmt(expr);}
;

compound_statement returns[CompStmt ret]
locals [List<Declaration> dList = new ArrayList<Declaration>(),
 List<Stmt> sList= new ArrayList<Stmt>()]
@after {$ret = new CompStmt($dList, $sList);}
: '{' (d=declaration {$dList.add($d.ret);})* (s=statement {$sList.add($s.ret);})* '}'
;

selection_statement returns[SelStmt ret]
: {Stmt stmt = null;}
  'if' '(' e=expression ')' s1=statement ('else' s2=statement {stmt = $s2.ret;})?
  {$ret = new SelStmt($e.ret, $s1.ret, stmt);}
;

iteration_statement returns [IterStmt ret]
: 'while' '(' expression ')' statement {$ret = new WhileStmt($expression.ret, $statement.ret);}
| {Expr expr1 = null, expr2 = null, expr3 = null;}
  'for' '(' (e1=expression {expr1 = $e1.ret;})? ';'
    (e2=expression {expr2 = $e2.ret;})? ';'
    (e3=expression {expr3 = $e3.ret;})? ')' s=statement
  {$ret = new ForStmt(expr1, expr2, expr3, $s.ret);}
;

jump_statement returns[JumpStmt ret]
: 'continue' ';' {$ret = new ContinueStmt();}
| 'break' ';' {$ret = new BreakStmt();}
| {Expr expr = null;}
  'return' (expression {expr = $expression.ret;})? ';'
  {$ret = new ReturnStmt(expr);}
;

expression returns[Expr ret]
locals[List<AssExpr> list = new ArrayList<AssExpr>()]
@after{$ret = new Expr($list);}
: a=assignment_expression {$list.add($a.ret);}
  (',' a=assignment_expression {$list.add($a.ret);})*
;

assignment_expression returns[AssExpr ret]
: l=logical_or_expression {$ret = new AssExpr($l.ret,null,null,null);}
| u=unary_expression ao=assignment_operator ae=assignment_expression
  {$ret = new AssExpr(null,$u.ret,$ao.ret,$ae.ret);}
;

assignment_operator returns[BinOp ret]
: '=' {$ret = BinOp.ASSIGN;}
| '*=' {$ret = BinOp.MULASSIGN;}
| '/=' {$ret = BinOp.DIVASSIGN;}
| '%=' {$ret = BinOp.MODASSIGN;}
| '+=' {$ret = BinOp.ADDASSIGN;}
| '-=' {$ret = BinOp.SUBASSIGN;}
| '<<=' {$ret = BinOp.LSHASSIGN;}
| '>>=' {$ret = BinOp.RSHASSIGN;}
| '&=' {$ret = BinOp.ANDASSIGN;}
| '^=' {$ret = BinOp.XORASSIGN;}
| '|=' {$ret = BinOp.ORASSIGN;}
;

constant_expression returns[ConstExpr ret]
: logical_or_expression {$ret = new ConstExpr($logical_or_expression.ret);}
;

logical_or_expression returns[LogOrExpr ret]
locals[List<LogAndExpr> exp = new ArrayList<LogAndExpr>()]
@after {$ret = new LogOrExpr($exp, null);}
: l=logical_and_expression {$exp.add($l.ret);}
  ('||' l=logical_and_expression {$exp.add($l.ret);})*
;

logical_and_expression returns[LogAndExpr ret]
locals[List<InOrExpr> exp = new ArrayList<InOrExpr>()]
@after {$ret = new LogAndExpr($exp, null);}
: i=inclusive_or_expression {$exp.add($i.ret);}
  ('&&' i=inclusive_or_expression {$exp.add($i.ret);})*
;

inclusive_or_expression returns [InOrExpr ret]
locals[List<ExOrExpr> exp = new ArrayList<ExOrExpr>()]
@after {$ret = new InOrExpr($exp, null);}
: e=exclusive_or_expression  {$exp.add($e.ret);}
  ('|' e=exclusive_or_expression {$exp.add($e.ret);})*
;

exclusive_or_expression returns[ExOrExpr ret]
locals[List<AndExpr> exp = new ArrayList<AndExpr>()]
@after {$ret = new ExOrExpr($exp, null);}
: a=and_expression {$exp.add($a.ret);}
  ('^' a=and_expression {$exp.add($a.ret);})*
;

and_expression returns[AndExpr ret]
locals[List<EquExpr> exp = new ArrayList<EquExpr>()]
@after {$ret = new AndExpr($exp, null);}
: equality_expression {$exp.add($equality_expression.ret);}
  ('&' e=equality_expression {$exp.add($e.ret);})*
;

equality_expression returns[EquExpr ret]
locals[List<RelExpr> exp = new ArrayList<RelExpr>(), List<BinOp> binOp = new ArrayList<BinOp>()]
@after {$ret = new EquExpr($exp,$binOp);}
: relational_expression {$exp.add($relational_expression.ret);}
  (e=equality_operator {$binOp.add($e.ret);} r=relational_expression {$exp.add($r.ret);})*
;

equality_operator returns[BinOp ret]
: '==' {$ret = BinOp.EQ;}
| '!=' {$ret = BinOp.NEQ;}
;

relational_expression returns[RelExpr ret]
locals[List<ShiftExpr> exp = new ArrayList<ShiftExpr>(), List<BinOp> binOp = new ArrayList<BinOp>()]
@after {$ret = new RelExpr($exp,$binOp);}
: shift_expression {$exp.add($shift_expression.ret);}
  (r=relational_operator {$binOp.add($r.ret);} s=shift_expression {$exp.add($s.ret);})*
;

relational_operator returns[BinOp ret]
: '<' {$ret = BinOp.LESS;}
| '>' {$ret = BinOp.GREATER;}
| '<=' {$ret = BinOp.LEQ;}
| '>=' {$ret = BinOp.GEQ;}
;

shift_expression returns[ShiftExpr ret]
locals[List<AddExpr> exp = new ArrayList<AddExpr>(), List<BinOp> binOp = new ArrayList<BinOp>()]
@after {$ret = new ShiftExpr($exp,$binOp);}
: additive_expression {$exp.add($additive_expression.ret);}
  (s=shift_operator {$binOp.add($s.ret);} a=additive_expression {$exp.add($a.ret);})*
;

shift_operator returns[BinOp ret]
: '<<' {$ret = BinOp.LEFTSHIFT;}
| '>>' {$ret = BinOp.RIGHTSHIFT;}
;

additive_expression returns[AddExpr ret]
locals[List<MulExpr> exp = new ArrayList<MulExpr>(), List<BinOp> binOp = new ArrayList<BinOp>()]
@after {$ret = new AddExpr($exp,$binOp);}
: m=multiplicative_expression {$exp.add($m.ret);}
  (a=additive_operator {$binOp.add($a.ret);} m=multiplicative_expression {$exp.add($m.ret);})*
;

additive_operator returns[BinOp ret]
: '+' {$ret = BinOp.ADD;}
| '-' {$ret = BinOp.SUB;}
;

multiplicative_expression returns[MulExpr ret]
locals[List<CastExpr> exp = new ArrayList<CastExpr>(), List<BinOp> binOp = new ArrayList<BinOp>()]
@after {$ret = new MulExpr($exp,$binOp);}
: c=cast_expression {$exp.add($c.ret);}
  (m=multiplicative_operator {$binOp.add($m.ret);} c=cast_expression {$exp.add($c.ret);})*
;

multiplicative_operator returns[BinOp ret]
: '*' {$ret = BinOp.MUL;}
| '/' {$ret = BinOp.DIV;}
| '%' {$ret = BinOp.MOD;}
;

cast_expression returns[CastExpr ret]
: unary_expression {$ret = new CastExpr(null, $unary_expression.ret);}
| '(' t=type_name ')' c=cast_expression  {$ret = new CastExpr($t.ret,$c.ret);}
;

type_name returns[TypeName ret]
: {int count = 0;}
  t=type_specifier ('*' {count++;})*
  {$ret = new TypeName($t.ret,count);}
;

unary_expression returns[UnaryExpr ret]
: p=postfix_expression {$ret=new UnaryExpr(UnaryExpr.POSTEXP, $p.ret);}
| '++' u=unary_expression {$ret = new UnaryExpr(UnaryExpr.PREINC, $u.ret);}
| '--' u=unary_expression {$ret = new UnaryExpr(UnaryExpr.PREDEC, $u.ret);}
| uo=unary_operator c=cast_expression {$ret = new UnaryExpr(UnaryExpr.UNARYOP, $uo.ret, $c.ret);}
| 'sizeof' u=unary_expression {$ret = new UnaryExpr(UnaryExpr.SIZEUEXP, $u.ret);}
| 'sizeof' '(' t=type_name ')' {$ret = new UnaryExpr(UnaryExpr.SIZETYNAME, $t.ret);}
;

unary_operator  returns[UnaryOp ret]
: '&' {$ret = UnaryOp.AND;}
| '*' {$ret = UnaryOp.STAR;}
| '+' {$ret = UnaryOp.PLUS;}
| '-' {$ret = UnaryOp.MINUS;}
| '~' {$ret = UanryOp.TILDE;}
| '!' {$ret = UnaryOp.NOT;}
;

postfix_expression returns[PostExpr ret]
locals[ArrayList<Postfix> list = new ArrayList<Postfix>(), PriExpr exp]
@after{$ret=new PostExpr($list, $exp);}
: p=primary_expression{$exp = $p.ret;} (postfix {$list.add($postfix.ret);})*
;

postfix returns[Postfix ret]
: '[' expression ']' {$ret = new ArrPostfix($expression.ret);}
|  {Arguments argu = null;}
  '(' (arguments {argu = $arguments.ret;})? ')'
  {$ret = new FunPostfix(argu);}
| '.' identifier {$ret = new ValAttrPostfix($identifier.ret);}
| '->' identifier {$ret = new PtrAttrPostfix($identifier.ret);}
| '++' {$ret = new SelfIncPostfix();}
| '--' {$ret = new SelfDecPostfix();}
;

arguments returns [Arguments ret]
locals [List<AssExpr> list=new ArrayList<AssExpr>()]
@after {$ret = new Arguments($list);}
: a=assignment_expression{$list.add($a.ret);} (',' ae=assignment_expression {$list.add($ae.ret);})*
;

primary_expression returns[PriExpr ret]
: identifier {$ret = new PriExpr($identifier.ret);}
| constant {$ret = new PriExpr($constant.ret);}
| stringExpr {$ret = new PriExpr($stringExpr.ret);}
| '(' expression ')' {$ret = new PriExpr($expression.ret);}
;

constant returns[Constant ret]
: i=integer_constant {$ret = $i.ret;}
| c=character_constant {$ret = $c.ret;}
;


identifier returns[Id ret]
: i=Identifier {$ret = new Id(symbol($i.text));}
;

typedef_name returns[TypedefName ret]
: i=Identifier {$ret = new TypedefName(symbol($i.text));}
;

integer_constant returns[IntConst ret]
: Hex {$ret = new IntConst($Hex.int);}
| Dec {$ret = new IntConst($Dec.int);}
| Oct {$ret = new IntConst($Oct.int);}
;

character_constant returns[CharConst ret]
: c=CharacterLiteral {$ret=new CharConst(new StringBuilder($c.text).toString());}
;


stringExpr returns[StringExpr ret]
: s=StringLiteral {$ret=new StringExpr(new StringBuilder($s.text).toString());};


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

CharacterLiteral : '\'' ( ('\\' ('b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\')) | ~('\''|'\\') ) '\'';
StringLiteral : '"' ( ('\\' ('b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\')) | ~('\\'|'"') )* '"';

Identifier : Letter (Letter | Digit)*;
