// Generated from C.g4 by ANTLR 4.0
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.Token;

public interface CListener extends ParseTreeListener {
	void enterExpression(CParser.ExpressionContext ctx);
	void exitExpression(CParser.ExpressionContext ctx);

	void enterDeclarator(CParser.DeclaratorContext ctx);
	void exitDeclarator(CParser.DeclaratorContext ctx);

	void enterAssignment_expression(CParser.Assignment_expressionContext ctx);
	void exitAssignment_expression(CParser.Assignment_expressionContext ctx);

	void enterMultiplicative_expression(CParser.Multiplicative_expressionContext ctx);
	void exitMultiplicative_expression(CParser.Multiplicative_expressionContext ctx);

	void enterJump_statement(CParser.Jump_statementContext ctx);
	void exitJump_statement(CParser.Jump_statementContext ctx);

	void enterCompound_statement(CParser.Compound_statementContext ctx);
	void exitCompound_statement(CParser.Compound_statementContext ctx);

	void enterCast_expression(CParser.Cast_expressionContext ctx);
	void exitCast_expression(CParser.Cast_expressionContext ctx);

	void enterCharacter_constant(CParser.Character_constantContext ctx);
	void exitCharacter_constant(CParser.Character_constantContext ctx);

	void enterEquality_expression(CParser.Equality_expressionContext ctx);
	void exitEquality_expression(CParser.Equality_expressionContext ctx);

	void enterParameters(CParser.ParametersContext ctx);
	void exitParameters(CParser.ParametersContext ctx);

	void enterFunction_definition(CParser.Function_definitionContext ctx);
	void exitFunction_definition(CParser.Function_definitionContext ctx);

	void enterDeclaration(CParser.DeclarationContext ctx);
	void exitDeclaration(CParser.DeclarationContext ctx);

	void enterInit_declarator(CParser.Init_declaratorContext ctx);
	void exitInit_declarator(CParser.Init_declaratorContext ctx);

	void enterShift_operator(CParser.Shift_operatorContext ctx);
	void exitShift_operator(CParser.Shift_operatorContext ctx);

	void enterExclusive_or_expression(CParser.Exclusive_or_expressionContext ctx);
	void exitExclusive_or_expression(CParser.Exclusive_or_expressionContext ctx);

	void enterStatement(CParser.StatementContext ctx);
	void exitStatement(CParser.StatementContext ctx);

	void enterLogical_and_expression(CParser.Logical_and_expressionContext ctx);
	void exitLogical_and_expression(CParser.Logical_and_expressionContext ctx);

	void enterAdditive_expression(CParser.Additive_expressionContext ctx);
	void exitAdditive_expression(CParser.Additive_expressionContext ctx);

	void enterUnary_operator(CParser.Unary_operatorContext ctx);
	void exitUnary_operator(CParser.Unary_operatorContext ctx);

	void enterProgram(CParser.ProgramContext ctx);
	void exitProgram(CParser.ProgramContext ctx);

	void enterShift_expression(CParser.Shift_expressionContext ctx);
	void exitShift_expression(CParser.Shift_expressionContext ctx);

	void enterLogical_or_expression(CParser.Logical_or_expressionContext ctx);
	void exitLogical_or_expression(CParser.Logical_or_expressionContext ctx);

	void enterIteration_statement(CParser.Iteration_statementContext ctx);
	void exitIteration_statement(CParser.Iteration_statementContext ctx);

	void enterType_name(CParser.Type_nameContext ctx);
	void exitType_name(CParser.Type_nameContext ctx);

	void enterAdditive_operator(CParser.Additive_operatorContext ctx);
	void exitAdditive_operator(CParser.Additive_operatorContext ctx);

	void enterPlain_declaration(CParser.Plain_declarationContext ctx);
	void exitPlain_declaration(CParser.Plain_declarationContext ctx);

	void enterIdentifier(CParser.IdentifierContext ctx);
	void exitIdentifier(CParser.IdentifierContext ctx);

	void enterStruct_or_union(CParser.Struct_or_unionContext ctx);
	void exitStruct_or_union(CParser.Struct_or_unionContext ctx);

	void enterInclusive_or_expression(CParser.Inclusive_or_expressionContext ctx);
	void exitInclusive_or_expression(CParser.Inclusive_or_expressionContext ctx);

	void enterConstant_expression(CParser.Constant_expressionContext ctx);
	void exitConstant_expression(CParser.Constant_expressionContext ctx);

	void enterMultiplicative_operator(CParser.Multiplicative_operatorContext ctx);
	void exitMultiplicative_operator(CParser.Multiplicative_operatorContext ctx);

	void enterEquality_operator(CParser.Equality_operatorContext ctx);
	void exitEquality_operator(CParser.Equality_operatorContext ctx);

	void enterRelational_expression(CParser.Relational_expressionContext ctx);
	void exitRelational_expression(CParser.Relational_expressionContext ctx);

	void enterPostfix_expression(CParser.Postfix_expressionContext ctx);
	void exitPostfix_expression(CParser.Postfix_expressionContext ctx);

	void enterAssignment_operator(CParser.Assignment_operatorContext ctx);
	void exitAssignment_operator(CParser.Assignment_operatorContext ctx);

	void enterDeclarators(CParser.DeclaratorsContext ctx);
	void exitDeclarators(CParser.DeclaratorsContext ctx);

	void enterUnary_expression(CParser.Unary_expressionContext ctx);
	void exitUnary_expression(CParser.Unary_expressionContext ctx);

	void enterConstant(CParser.ConstantContext ctx);
	void exitConstant(CParser.ConstantContext ctx);

	void enterSelection_statement(CParser.Selection_statementContext ctx);
	void exitSelection_statement(CParser.Selection_statementContext ctx);

	void enterExpression_statement(CParser.Expression_statementContext ctx);
	void exitExpression_statement(CParser.Expression_statementContext ctx);

	void enterPlain_declarator(CParser.Plain_declaratorContext ctx);
	void exitPlain_declarator(CParser.Plain_declaratorContext ctx);

	void enterInit_declarators(CParser.Init_declaratorsContext ctx);
	void exitInit_declarators(CParser.Init_declaratorsContext ctx);

	void enterRelational_operator(CParser.Relational_operatorContext ctx);
	void exitRelational_operator(CParser.Relational_operatorContext ctx);

	void enterPostfix(CParser.PostfixContext ctx);
	void exitPostfix(CParser.PostfixContext ctx);

	void enterArguments(CParser.ArgumentsContext ctx);
	void exitArguments(CParser.ArgumentsContext ctx);

	void enterTypedef_name(CParser.Typedef_nameContext ctx);
	void exitTypedef_name(CParser.Typedef_nameContext ctx);

	void enterInteger_constant(CParser.Integer_constantContext ctx);
	void exitInteger_constant(CParser.Integer_constantContext ctx);

	void enterAnd_expression(CParser.And_expressionContext ctx);
	void exitAnd_expression(CParser.And_expressionContext ctx);

	void enterPrimary_expression(CParser.Primary_expressionContext ctx);
	void exitPrimary_expression(CParser.Primary_expressionContext ctx);

	void enterString(CParser.StringContext ctx);
	void exitString(CParser.StringContext ctx);

	void enterType_specifier(CParser.Type_specifierContext ctx);
	void exitType_specifier(CParser.Type_specifierContext ctx);

	void enterInitializer(CParser.InitializerContext ctx);
	void exitInitializer(CParser.InitializerContext ctx);
}