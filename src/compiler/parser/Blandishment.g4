grammar Blandishment;

@header {    
package blandishment.syntactics;
import blandishment.environment.Environment;
import blandishment.types.*;
import blandishment.types.Void;
import blandishment.semantics.*;
import java.util.*;
}

@parser::members {
boolean typeDefinitionFlag = false, functionDefinitionFlag = false, parameterFlag = false;
List<Integer> compoundStatementCounter = new ArrayList<Integer>(); //count the number of compound statements we go into
int iterationCounter = 0;
Stack<Type> definitionType = new Stack<Type>();
Stack<Function> functionType = new Stack<Function>();
}

program : {Environment.startProgram();} (declaration | functionDefinition)+
        ;

declaration: 'typedef' typeSpecifier 
             {
                 definitionType.push($typeSpecifier.ctx.ty);
                 typeDefinitionFlag = true;
             } 
             declarators 
             {
                 typeDefinitionFlag = false;
                 definitionType.pop();
             }
             ';'  
           | typeSpecifier 
             {definitionType.push($typeSpecifier.ctx.ty);} 
             initDeclarators? ';'
             {definitionType.pop();} 
           ;

functionDefinition: {
                        ParameterList para = null; 
                        functionDefinitionFlag = true;
                    }
                    typeSpecifier plainDeclarator
                    {
                        Environment.beginScope(); 
                        compoundStatementCounter.add(0);
                    } 
                    '(' (parameters {para = $parameters.ctx.ty;})? ')' 
                    {
                        Function tmpTy = TypeFactory.functionFactory($typeSpecifier.ctx.ty, $plainDeclarator.ctx.starCounter, para, true);
                        Semantics.checkFunctionDefinition($plainDeclarator.ctx.name, tmpTy, $typeSpecifier.start.getLine(), $typeSpecifier.start.getCharPositionInLine());
                        Environment.now.parent.identifiers.put($plainDeclarator.ctx.name, tmpTy);
                        functionDefinitionFlag = false;
                        functionType.push(tmpTy);
                        //++functionCounter;
                    }
                    compoundStatement
                    {
                        //--functionCounter;
                        functionType.pop();
                        Environment.endScope(); 
                        compoundStatementCounter.remove(compoundStatementCounter.size()-1);
                    }
                   ;

parameters
locals [ParameterList ty]
           : {$ty = new ParameterList();}
             plainDeclaration
             {$ty.addParameter($plainDeclaration.ctx.ty);}
             (',' plainDeclaration
             {$ty.addParameter($plainDeclaration.ctx.ty);}
             )* (',' '...' {$ty.setVaryingArgument(true);})?
           ;

declarators : declarator (',' declarator)*
            ;

initDeclarators : initDeclarator (',' initDeclarator)*
                ;

initDeclarator
locals [Type ty]
                : declarator {$ty = $declarator.ctx.ty;} ('=' initializer 
                  {TypeFactory.checkInit($declarator.ctx.ty, $initializer.ctx.ty, $initializer.start.getLine(), $initializer.start.getCharPositionInLine());}
                  )?
                ;

initializer
locals [Type ty]
           : assignmentExpression 
             {
                 $ty = $assignmentExpression.ctx.ty;
             }
           | {
                 $ty = new ParameterList();
             }
             '{' initializer 
             {((ParameterList)($ty)).addParameter($initializer.ctx.ty);}
             (',' initializer
             {((ParameterList)($ty)).addParameter($initializer.ctx.ty);}
             )*'}'
           ;

typeSpecifier
locals [Type ty, String name]
              : 'void' {$ty = Void.getInstance();}
              | 'char' {$ty = Char.getInstance();}
              | 'int' {$ty = Int.getInstance();}
              | typedefName 
                {
                    if (Environment.isTypedefName($typedefName.text))
                        $ty = Environment.getByTypedefName($typedefName.text);
                    else {
                        Environment.addError($typedefName.start.getLine(), $typedefName.start.getCharPositionInLine(), "Type name undefined.");
                        $ty = Void.getInstance();
                    }
                }
              | {String name = null; }
                structOrUnion
                {
                    if ($structOrUnion.text.equals("struct"))
                        $ty = new Struct();
                    else 
                        $ty = new Union();
                }
                (identifier 
                {
                    name = $identifier.text;
                }
                )? 
                '{'
                {
                   Environment.beginScope();
                } 
                (typeSpecifier {definitionType.push($typeSpecifier.ctx.ty);} declarators ';' {definitionType.pop();} 
                )+ 
                {
                    Iterator iter = Environment.now.identifiers.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry entry = (Map.Entry)iter.next();
                        String key = (String)entry.getKey();
                        Type value = (Type)entry.getValue();
                        ((Record)($ty)).fields.add(new Record.RecordField(value, key));
                    }
                    iter = Environment.now.recordNames.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry entry = (Map.Entry)iter.next();
                        String key = (String)entry.getKey();
                        Record value = (Record)entry.getValue();
                        Environment.putRecordName(key, value);
                    }
                    Environment.endScope();
                }
                '}'
                {
                    if (name != null) 
                        if (!Environment.isLocalRecordName(name)) 
                            Environment.putRecordName(name, $ty);
                        else
                            Environment.addError($identifier.start.getLine(), $identifier.start.getCharPositionInLine(), "Struct or union name redefined.");
                }
              | structOrUnion identifier 
                {
                    if ($structOrUnion.text.equals("struct"))
                        $ty = new StructName($identifier.text, Environment.now);
                    else
                        $ty = new UnionName($identifier.text, Environment.now);
                }
              ;

structOrUnion: 'struct' | 'union'
               ;

plainDeclaration
locals [Type ty]
                 : typeSpecifier 
                   {definitionType.push($typeSpecifier.ctx.ty);}
                   declarator
                   {$ty = $declarator.ctx.ty; definitionType.pop();}
                 ;

declarator
locals [Type ty]
           : {
             ParameterList para = null;
             }
             plainDeclarator '(' (
             {
                 parameterFlag = true;
             }
             parameters
             {
                 parameterFlag = false;
                 para = $parameters.ctx.ty;
             }
             )? ')'
             {
             $ty = TypeFactory.functionFactory(definitionType.peek(), $plainDeclarator.ctx.starCounter, para, false);
             if (typeDefinitionFlag) {
                 if (!Environment.isLocalTypedefName($plainDeclarator.ctx.name))
                     Environment.putTypedefName($plainDeclarator.ctx.name, $ty);
                 else
                     Environment.addError($plainDeclarator.start.getLine(), $plainDeclarator.start.getCharPositionInLine(), "Type name redefined.");
             } else {
                 if (!Environment.isLocalIdentifier($plainDeclarator.ctx.name))
                     Environment.putIdentifier($plainDeclarator.ctx.name, $ty);
                 else 
                     Environment.addError($plainDeclarator.start.getLine(), $plainDeclarator.start.getCharPositionInLine(), "Variable name redefined.");
             }
             }
           | {int co = 0;}plainDeclarator ('[' constantExpression ']' {++co;})*
             {
             if (co > 0) $ty = TypeFactory.arrayFactory(definitionType.peek(), co);
             else $ty = definitionType.peek();
             System.out.println("dt:"+definitionType.peek().getClass());
             $ty = TypeFactory.typeNameFactory($ty, $plainDeclarator.ctx.starCounter);
             if (typeDefinitionFlag) { 
                 if (!Environment.isLocalTypedefName($plainDeclarator.ctx.name))
                     Environment.putTypedefName($plainDeclarator.ctx.name, $ty);
                 else
                     Environment.addError($plainDeclarator.start.getLine(), $plainDeclarator.start.getCharPositionInLine(), "Type name redefined.");
             } else {
                 if (!Environment.isLocalIdentifier($plainDeclarator.ctx.name))
                     Environment.putIdentifier($plainDeclarator.ctx.name, $ty);
                 else
                     Environment.addError($plainDeclarator.start.getLine(), $plainDeclarator.start.getCharPositionInLine(), "Variable name redefined.");
             }
             }
           ;
 
plainDeclarator 
locals [String name, int starCounter]
               : ('*' {$starCounter++;})* identifier {$name = $identifier.text;} 
                ;

statement: expressionStatement
         | compoundStatement
         | selectionStatement
         | iterationStatement
         | jumpStatement
         ;
 
expressionStatement: expression? ';'
                    ;
 
compoundStatement: '{' 
                    {
                        if (compoundStatementCounter.get(compoundStatementCounter.size() - 1) != 0) 
                            Environment.beginScope();
                        compoundStatementCounter.set(compoundStatementCounter.size() - 1, compoundStatementCounter.get(compoundStatementCounter.size() - 1) + 1);
                    } 
                    declaration* statement* 
                    {
                        compoundStatementCounter.set(compoundStatementCounter.size() - 1, compoundStatementCounter.get(compoundStatementCounter.size() - 1) - 1);
                        if (compoundStatementCounter.get(compoundStatementCounter.size() - 1) != 0)
                            Environment.endScope();
                    } 
                    '}'
                  ;
 
selectionStatement: 'if' '(' expression ')' statement ('else' statement)?
                  ;
 
iterationStatement: 'while' '(' expression ')' 
                    {
                        ++iterationCounter;
                    }
                    statement
                    {
                        --iterationCounter;
                    }
                  | 'for' '(' expression? ';' expression? ';' expression? ')' 
                    {
                        ++iterationCounter;
                    }
                    statement
                    {
                        --iterationCounter;
                    }
                  ;
 
jumpStatement: 'continue' ';'
               {
                   if (iterationCounter == 0)
                       Environment.addError($jumpStatement.start.getLine(), $jumpStatement.start.getCharPositionInLine(), "\"continue\" only occurs in an iteration.");
               }
             | 'break' ';'
               {
                   if (iterationCounter == 0)
                       Environment.addError($jumpStatement.start.getLine(), $jumpStatement.start.getCharPositionInLine(), "\"break\" only occurs in an iteration.");
               }
             | {
                   if (functionType.empty())
                       Environment.addError($jumpStatement.start.getLine(), $jumpStatement.start.getCharPositionInLine(), "\"return\" only occurs in a function.");
               }
               'return' (expression {if (!functionType.empty())Semantics.checkFunctionReturn(functionType.peek(), $expression.ctx.ty, $expression.start.getLine(), $expression.start.getCharPositionInLine());})? ';'
               
             ;

expression
locals [Type ty, boolean lv]
           : assignmentExpression {$ty = $assignmentExpression.ctx.ty; $lv = $assignmentExpression.ctx.lv;} 
             (',' assignmentExpression {$ty = null; $lv = false;})* 
           ;

assignmentExpression
locals [Type ty, boolean lv]
                     : logicalOrExpression 
                       {$ty = $logicalOrExpression.ctx.ty; $lv = $logicalOrExpression.ctx.lv;}
                     | unaryExpression assignmentOperator assignmentExpression 
                       {
                           TypeFactory.checkLValue($unaryExpression.ctx.lv, $unaryExpression.start.getLine(), $unaryExpression.start.getCharPositionInLine());
                           $ty = TypeFactory.assignmentFactory($unaryExpression.ctx.ty, $assignmentOperator.text, $assignmentExpression.ctx.ty, $unaryExpression.start.getLine(), $unaryExpression.start.getCharPositionInLine()); 
                           $lv = false;
                       } 
                     ;
 
assignmentOperator: '=' | '*=' | '/=' | '%=' | '+=' | '-=' | '<<=' | '>>=' | '&=' | '^=' | '|='
                   ;

constantExpression
locals [Type ty, boolean lv]
                   : logicalOrExpression 
                     {$ty = $logicalOrExpression.ctx.ty; $lv = $logicalOrExpression.ctx.lv; }
                   ;

logicalOrExpression
locals [Type ty, boolean lv]
                    : logicalAndExpression {$ty = $logicalAndExpression.ctx.ty; $lv = $logicalAndExpression.ctx.lv; } 
                      ('||' logicalAndExpression
                      {
                          $ty = TypeFactory.logicalOrFactory($ty, $logicalAndExpression.ctx.ty, $logicalAndExpression.start.getLine(), $logicalAndExpression.start.getCharPositionInLine()); 
                          $lv = false; 
                      }
                      )*   
                    ;

logicalAndExpression
locals [Type ty, boolean lv]
                      : inclusiveOrExpression {$ty = $inclusiveOrExpression.ctx.ty; $lv = $inclusiveOrExpression.ctx.lv; } 
                        ('&&' inclusiveOrExpression
                        {
                            $ty = TypeFactory.logicalAndFactory($ty, $inclusiveOrExpression.ctx.ty, $inclusiveOrExpression.start.getLine(), $inclusiveOrExpression.start.getCharPositionInLine()); 
                            $lv = false;
                        }
                        )*
                      ;

inclusiveOrExpression
locals [Type ty, boolean lv]
                       : exclusiveOrExpression {$ty = $exclusiveOrExpression.ctx.ty; $lv = $exclusiveOrExpression.ctx.lv; }
                         ('|' exclusiveOrExpression 
                         {
                             $ty = TypeFactory.inclusiveOrFactory($ty, $exclusiveOrExpression.ctx.ty, $exclusiveOrExpression.start.getLine(), $exclusiveOrExpression.start.getCharPositionInLine()); 
                             $lv = false;
                         }
                         )*
                       ;

exclusiveOrExpression
locals [Type ty, boolean lv]
                       : andExpression {$ty = $andExpression.ctx.ty; $lv = $andExpression.ctx.lv; } 
                         ('^' andExpression
                         {
                             $ty = TypeFactory.exclusiveOrFactory($ty, $andExpression.ctx.ty, $andExpression.start.getLine(), $andExpression.start.getCharPositionInLine()); 
                             $lv = false;
                         }
                         )*
                       ;
 
andExpression
locals [Type ty, boolean lv]
              : equalityExpression {$ty = $equalityExpression.ctx.ty; $lv = $equalityExpression.ctx.lv;} 
                ('&' equalityExpression
                {
                    $ty = TypeFactory.andFactory($ty, $equalityExpression.ctx.ty, $equalityExpression.start.getLine(), $equalityExpression.start.getCharPositionInLine()); 
                    $lv = false;
                }
                )*
              ;

equalityExpression
locals [Type ty, boolean lv]
                   : relationalExpression {$ty = $relationalExpression.ctx.ty; $lv = $relationalExpression.ctx.lv; } 
                     (equalityOperator relationalExpression
                     {
                         $ty = TypeFactory.equalFactory($ty, $relationalExpression.ctx.ty, $relationalExpression.start.getLine(), $relationalExpression.start.getCharPositionInLine()); 
                         $lv = false; 
                     }
                     )*
                   ;

equalityOperator: '==' | '!='
                 ;

relationalExpression
locals [Type ty, boolean lv]
                     : shiftExpression {$ty = $shiftExpression.ctx.ty; $lv = $shiftExpression.ctx.lv; }
                       (relationalOperator shiftExpression
                       {
                           $ty = TypeFactory.relationFactory($ty, $shiftExpression.ctx.ty, $shiftExpression.start.getLine(), $shiftExpression.start.getCharPositionInLine()); 
                           $lv = false;
                       }
                       )*
                     ;

relationalOperator: '<' | '>' | '<=' | '>='
                   ;
 
shiftExpression
locals [Type ty, boolean lv] 
                : additiveExpression 
                  {$ty = $additiveExpression.ctx.ty; $lv = $additiveExpression.ctx.lv; }
                  (shiftOperator additiveExpression
                  {
                      $ty = TypeFactory.shiftFactory($ty, $additiveExpression.ctx.ty, $additiveExpression.start.getLine(), $additiveExpression.start.getCharPositionInLine()); 
                      $lv = false;
                  }
                  )*
                ;

shiftOperator: '<<' | '>>'
              ;

additiveExpression
locals [Type ty, boolean lv]   
                   : multiplicativeExpression 
                     {
                         $ty = $multiplicativeExpression.ctx.ty; 
                         $lv = $multiplicativeExpression.ctx.lv; 
                     }
                     (additiveOperator multiplicativeExpression
                     {
                         $ty = TypeFactory.addFactory($ty, $additiveOperator.text, $multiplicativeExpression.ctx.ty, $multiplicativeExpression.start.getLine(), $multiplicativeExpression.start.getCharPositionInLine()); 
                         $lv = false;
                     }
                     )*
                   ;

additiveOperator: '+' | '-'
                 ;

multiplicativeExpression
locals [Type ty, boolean lv]
                         : castExpression {$ty = $castExpression.ctx.ty; $lv = $castExpression.ctx.lv; }
                           (multiplicativeOperator castExpression 
                           {
                               $ty = TypeFactory.multiplyFactory($ty, $castExpression.ctx.ty, $castExpression.start.getLine(), $castExpression.start.getCharPositionInLine()); 
                               $lv = false; 
                           }
                           )*
                         ;

multiplicativeOperator: '*' | '/' | '%'
                       ;

castExpression
locals [Type ty, boolean lv]
               : unaryExpression {$ty = $unaryExpression.ctx.ty; $lv = $unaryExpression.ctx.lv; }
               | '(' typeName ')' castExpression {$ty = $typeName.ctx.ty; $lv = $castExpression.ctx.lv;}
               ;

typeName
locals [Type ty]
         : typeSpecifier {int co = 0; }('*' {++co;})* 
           {
               $ty = TypeFactory.typeNameFactory($typeSpecifier.ctx.ty, co);
           }
         ;

unaryExpression
locals [Type ty, boolean lv]
                : postfixExpression {$ty = $postfixExpression.ctx.ty; $lv = $postfixExpression.ctx.lv; }
                | '++' unaryExpression 
                  {
                      TypeFactory.checkLValue($unaryExpression.ctx.lv, $unaryExpression.start.getLine(), $unaryExpression.start.getCharPositionInLine()); 
                      $ty = $unaryExpression.ctx.ty; $lv = true;
                  }
                | '--' unaryExpression 
                  {
                      TypeFactory.checkLValue($unaryExpression.ctx.lv, $unaryExpression.start.getLine(), $unaryExpression.start.getCharPositionInLine()); 
                      $ty = $unaryExpression.ctx.ty; $lv = true;
                  }
                | unaryOperator castExpression 
                  {
                      if ($unaryOperator.text.equals("&")) TypeFactory.checkLValue($castExpression.ctx.lv, $castExpression.start.getLine(), $castExpression.start.getCharPositionInLine());
                      $ty = TypeFactory.unaryFactory($unaryOperator.text, $castExpression.ctx.ty, $castExpression.start.getLine(), $castExpression.start.getCharPositionInLine());
                      if ($unaryOperator.text.equals("*") && $castExpression.ctx.ty instanceof Pointer && !(((Pointer)($castExpression.ctx.ty)).elementType instanceof Void)) 
                          $lv = true; 
                      else 
                          $lv = false;
                  }
                | 'sizeof' unaryExpression {$ty = Int.getInstance(); $lv = false;}
                | 'sizeof' '(' typeName ')' {$ty = Int.getInstance(); $lv = false;}
                ;

unaryOperator: '&' | '*' | '+' | '-' | '~' | '!'
              ;

postfixExpression
locals [Type ty, boolean lv]  
                  : primaryExpression 
                    {$ty = $primaryExpression.ctx.ty; $lv = $primaryExpression.ctx.lv; }
                    (
                    '[' expression ']' 
                    {
                        $ty = TypeFactory.indexFactory($ty, $expression.ctx.ty, $primaryExpression.start.getLine(), $primaryExpression.start.getCharPositionInLine()); 
                        $lv = true; 
                    }
                    | {ParameterList para = null;}
                    '(' (arguments {para = $arguments.ctx.ty; })? 
                    {
                        $ty = TypeFactory.functionReferenceFactory($ty, para, $primaryExpression.start.getLine(), $primaryExpression.start.getCharPositionInLine());
                        if ($ty instanceof Void) $ty = null; 
                        $lv = false; 
                    } 
                    ')' 
                    | '.' identifier 
                    {
                        $ty = TypeFactory.objectReferenceFactory($ty, $identifier.text, $primaryExpression.start.getLine(), $primaryExpression.start.getCharPositionInLine()); 
                        $lv = true; 
                    }
                    | '->' identifier 
                    {
                        $ty = TypeFactory.pointerReferenceFactory($ty, $identifier.text, $primaryExpression.start.getLine(), $primaryExpression.start.getCharPositionInLine()); 
                        $lv = true; 
                    }
                    | '++' 
                    {
                        TypeFactory.checkLValue($primaryExpression.ctx.lv, $primaryExpression.start.getLine(), $primaryExpression.start.getCharPositionInLine()); 
                        $lv = true;
                    }
                    | '--' 
                    {
                       TypeFactory.checkLValue($primaryExpression.ctx.lv, $primaryExpression.start.getLine(), $primaryExpression.start.getCharPositionInLine()); 
                       $lv = true;
                    }
                    )*
                  ;
 
arguments
locals [ParameterList ty]
          : {$ty = new ParameterList();}
            assignmentExpression 
            {$ty.addParameter($assignmentExpression.ctx.ty);}
            (',' assignmentExpression
            {$ty.addParameter($assignmentExpression.ctx.ty);}
            )*
          ;

primaryExpression
locals [Type ty, boolean lv]
                  : identifier 
                    {
                        if (Environment.isIdentifier($identifier.text))
                            $ty = Environment.getByIdentifier($identifier.text);
                        else
                            Environment.addError($identifier.start.getLine(), $identifier.start.getCharPositionInLine(), "Identifier undefined.");
                        if ($ty instanceof Function || $ty instanceof Array)
                            $lv = false;
                        else
                            $lv = true;
                    }
                  | constant {$ty = $constant.ctx.ty; $lv = false;}
                  | string {$ty = $string.ctx.ty; $lv = false;}
                  | '(' expression ')' {$ty = $expression.ctx.ty; $lv = $expression.ctx.lv;}
                  ;

constant
locals [Type ty, boolean lv = false]
        : integerConstant {$ty = Int.getInstance();}
        | characterConstant {$ty = Char.getInstance();}
        ;

identifier

           : Identifier  
           ;

typedefName: {Environment.isTypedefName(getCurrentToken().getText())}? Identifier
              ;
integerConstant: HexLiteral
                | DecimalLiteral
                | OctalLiteral
                ;

characterConstant: CharacterLiteral
                  ;

string
locals [Type ty]
      : StringLiteral {$ty = TypeFactory.stringFactory($StringLiteral.text);}
      ;

// LEXER

HexLiteral : '0' ('x'|'X') HexDigit+ IntegerTypeSuffix? ;

DecimalLiteral : ('0' | '1'..'9' '0'..'9'*) IntegerTypeSuffix? ;

OctalLiteral : '0' ('0'..'7')+ IntegerTypeSuffix? ;

fragment
HexDigit : ('0'..'9'|'a'..'f'|'A'..'F') ;

fragment
IntegerTypeSuffix : ('l'|'L') ;

CharacterLiteral
    : '\'' ( EscapeSequence | ~('\''|'\\') ) '\''
    ;

StringLiteral
    : '"' ( EscapeSequence | ~('\\'|'"') )* '"'
    ;

fragment
EscapeSequence
    : '\\' ('b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\')
    ;


Identifier
    : Letter (Letter | '0'..'9')*
    ;


Letter
    : '$' | 'A'..'Z' | '_' |  'a'..'z'
    ;

WS : [ \r\t\n]+ -> channel(HIDDEN)
    ;

COMMENT
    : '/*' .*? '*/' -> channel(HIDDEN)
    ;

LINE_COMMENT
    : '//' ~[\r\n]* ('\r'? '\n' | EOF) -> channel(HIDDEN)
    ;
