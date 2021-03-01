// Generated from ./WACCParser.g4 by ANTLR 4.9.1
package antlr;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link WACCParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface WACCParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link WACCParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(WACCParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link WACCParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc(WACCParser.FuncContext ctx);
	/**
	 * Visit a parse tree produced by {@link WACCParser#param_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParam_list(WACCParser.Param_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link WACCParser#param}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParam(WACCParser.ParamContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ReturnStat}
	 * labeled alternative in {@link WACCParser#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnStat(WACCParser.ReturnStatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExitStat}
	 * labeled alternative in {@link WACCParser#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExitStat(WACCParser.ExitStatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ScopeStat}
	 * labeled alternative in {@link WACCParser#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitScopeStat(WACCParser.ScopeStatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IfStat}
	 * labeled alternative in {@link WACCParser#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStat(WACCParser.IfStatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FreeStat}
	 * labeled alternative in {@link WACCParser#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFreeStat(WACCParser.FreeStatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SkipStat}
	 * labeled alternative in {@link WACCParser#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSkipStat(WACCParser.SkipStatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code WhileStat}
	 * labeled alternative in {@link WACCParser#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileStat(WACCParser.WhileStatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SeqStat}
	 * labeled alternative in {@link WACCParser#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSeqStat(WACCParser.SeqStatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ReadStat}
	 * labeled alternative in {@link WACCParser#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReadStat(WACCParser.ReadStatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PrintlnStat}
	 * labeled alternative in {@link WACCParser#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrintlnStat(WACCParser.PrintlnStatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AssignStat}
	 * labeled alternative in {@link WACCParser#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignStat(WACCParser.AssignStatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DeclareStat}
	 * labeled alternative in {@link WACCParser#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclareStat(WACCParser.DeclareStatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PrintStat}
	 * labeled alternative in {@link WACCParser#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrintStat(WACCParser.PrintStatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Ident}
	 * labeled alternative in {@link WACCParser#assign_lhs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdent(WACCParser.IdentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LHSArrayElem}
	 * labeled alternative in {@link WACCParser#assign_lhs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLHSArrayElem(WACCParser.LHSArrayElemContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LHSPairElem}
	 * labeled alternative in {@link WACCParser#assign_lhs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLHSPairElem(WACCParser.LHSPairElemContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExprNode}
	 * labeled alternative in {@link WACCParser#assign_rhs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprNode(WACCParser.ExprNodeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ArrayLiteral}
	 * labeled alternative in {@link WACCParser#assign_rhs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayLiteral(WACCParser.ArrayLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NewPair}
	 * labeled alternative in {@link WACCParser#assign_rhs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNewPair(WACCParser.NewPairContext ctx);
	/**
	 * Visit a parse tree produced by the {@code RHSPairElem}
	 * labeled alternative in {@link WACCParser#assign_rhs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRHSPairElem(WACCParser.RHSPairElemContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FunctionCall}
	 * labeled alternative in {@link WACCParser#assign_rhs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionCall(WACCParser.FunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link WACCParser#arg_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArg_list(WACCParser.Arg_listContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FstExpr}
	 * labeled alternative in {@link WACCParser#pair_elem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFstExpr(WACCParser.FstExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SndExpr}
	 * labeled alternative in {@link WACCParser#pair_elem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSndExpr(WACCParser.SndExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BaseType}
	 * labeled alternative in {@link WACCParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBaseType(WACCParser.BaseTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ArrayType}
	 * labeled alternative in {@link WACCParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayType(WACCParser.ArrayTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PairType}
	 * labeled alternative in {@link WACCParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPairType(WACCParser.PairTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IntType}
	 * labeled alternative in {@link WACCParser#base_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntType(WACCParser.IntTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BoolType}
	 * labeled alternative in {@link WACCParser#base_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolType(WACCParser.BoolTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CharType}
	 * labeled alternative in {@link WACCParser#base_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCharType(WACCParser.CharTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code StringType}
	 * labeled alternative in {@link WACCParser#base_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringType(WACCParser.StringTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link WACCParser#array_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArray_type(WACCParser.Array_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link WACCParser#pair_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPair_type(WACCParser.Pair_typeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PairElemBaseType}
	 * labeled alternative in {@link WACCParser#pair_elem_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPairElemBaseType(WACCParser.PairElemBaseTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PairElemArrayType}
	 * labeled alternative in {@link WACCParser#pair_elem_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPairElemArrayType(WACCParser.PairElemArrayTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PairElemPairType}
	 * labeled alternative in {@link WACCParser#pair_elem_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPairElemPairType(WACCParser.PairElemPairTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BoolExpr}
	 * labeled alternative in {@link WACCParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolExpr(WACCParser.BoolExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PairExpr}
	 * labeled alternative in {@link WACCParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPairExpr(WACCParser.PairExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IdExpr}
	 * labeled alternative in {@link WACCParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdExpr(WACCParser.IdExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AndOrExpr}
	 * labeled alternative in {@link WACCParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndOrExpr(WACCParser.AndOrExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UnopExpr}
	 * labeled alternative in {@link WACCParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnopExpr(WACCParser.UnopExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CharExpr}
	 * labeled alternative in {@link WACCParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCharExpr(WACCParser.CharExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ArrayExpr}
	 * labeled alternative in {@link WACCParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayExpr(WACCParser.ArrayExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ArithmeticExpr}
	 * labeled alternative in {@link WACCParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArithmeticExpr(WACCParser.ArithmeticExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CmpExpr}
	 * labeled alternative in {@link WACCParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmpExpr(WACCParser.CmpExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code EqExpr}
	 * labeled alternative in {@link WACCParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqExpr(WACCParser.EqExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code StrExpr}
	 * labeled alternative in {@link WACCParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStrExpr(WACCParser.StrExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IntExpr}
	 * labeled alternative in {@link WACCParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntExpr(WACCParser.IntExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParenExpr}
	 * labeled alternative in {@link WACCParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenExpr(WACCParser.ParenExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link WACCParser#array_elem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArray_elem(WACCParser.Array_elemContext ctx);
	/**
	 * Visit a parse tree produced by {@link WACCParser#array_liter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArray_liter(WACCParser.Array_literContext ctx);
}