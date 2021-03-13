// Generated from /Users/xushitong/wacc_46/antlr_config/WACCParser.g4 by ANTLR 4.8
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class WACCParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		WS=1, SHARP=2, EOL=3, COMMENT=4, INT=5, BOOL=6, CHAR=7, STRING=8, BOOL_LITER=9, 
		CHAR_LITER=10, STR_LITER=11, FST=12, SND=13, PAIR_LITER=14, PAIR=15, NEWPAIR=16, 
		STRUCT=17, NEW=18, DOT=19, EMPTY=20, BEGIN=21, END=22, IS=23, SKP=24, 
		ASSIGN=25, READ=26, FREE=27, RETURN=28, EXIT=29, PRINT=30, PRINTLN=31, 
		IF=32, ELSE=33, THEN=34, FI=35, WHILE=36, DO=37, DONE=38, CASE=39, FOR=40, 
		BREAK=41, CONTINUE=42, SWITCH=43, DEFAULT=44, CALL=45, SEMICOLON=46, COMMA=47, 
		IMPORT=48, OPEN_PARENTHESES=49, CLOSE_PARENTHESES=50, OPEN_SQUARE_BRACKET=51, 
		CLOSE_SQUARE_BRACKET=52, OPEN_CURLY_BRACKET=53, CLOSE_CURLY_BRACKET=54, 
		PLUS=55, MINUS=56, NOT=57, LEN=58, ORD=59, CHR=60, MUL=61, DIV=62, MOD=63, 
		GREATER=64, GREATER_EQUAL=65, LESS=66, LESS_EQUAL=67, EQUAL=68, UNEQUAL=69, 
		AND=70, OR=71, BitWiseAnd=72, BitWiseOr=73, BitWiseComplement=74, INT_LITER=75, 
		BINARY_LITER=76, OCTAL_LITER=77, HEX_LITER=78, FILE_NAME=79, IDENT=80;
	public static final int
		RULE_import_file = 0, RULE_declarition = 1, RULE_program = 2, RULE_struct = 3, 
		RULE_func = 4, RULE_param_list = 5, RULE_param = 6, RULE_stat = 7, RULE_assign_lhs = 8, 
		RULE_for_stat = 9, RULE_skp = 10, RULE_declare = 11, RULE_assign = 12, 
		RULE_read = 13, RULE_free = 14, RULE_exit = 15, RULE_print = 16, RULE_println = 17, 
		RULE_assign_rhs = 18, RULE_arg_list = 19, RULE_struct_elem = 20, RULE_new_struct = 21, 
		RULE_pair_elem = 22, RULE_type = 23, RULE_base_type = 24, RULE_array_type = 25, 
		RULE_pair_type = 26, RULE_pair_elem_type = 27, RULE_struct_type = 28, 
		RULE_expr = 29, RULE_array_elem = 30, RULE_array_liter = 31;
	private static String[] makeRuleNames() {
		return new String[] {
			"import_file", "declarition", "program", "struct", "func", "param_list", 
			"param", "stat", "assign_lhs", "for_stat", "skp", "declare", "assign", 
			"read", "free", "exit", "print", "println", "assign_rhs", "arg_list", 
			"struct_elem", "new_struct", "pair_elem", "type", "base_type", "array_type", 
			"pair_type", "pair_elem_type", "struct_type", "expr", "array_elem", "array_liter"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, "'#'", "'\n'", null, "'int'", "'bool'", "'char'", "'string'", 
			null, null, null, "'fst'", "'snd'", "'null'", "'pair'", "'newpair'", 
			"'struct'", "'new'", "'.'", "'empty'", "'begin'", "'end'", "'is'", "'skip'", 
			"'='", "'read'", "'free'", "'return'", "'exit'", "'print'", "'println'", 
			"'if'", "'else'", "'then'", "'fi'", "'while'", "'do'", "'done'", "'case'", 
			"'for'", "'break'", "'continue'", "'switch'", "'default'", "'call'", 
			"';'", "','", "'import'", "'('", "')'", "'['", "']'", "'{'", "'}'", "'+'", 
			"'-'", "'!'", "'len'", "'ord'", "'chr'", "'*'", "'/'", "'%'", "'>'", 
			"'>='", "'<'", "'<='", "'=='", "'!='", "'&&'", "'||'", "'&'", "'|'", 
			"'~'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "WS", "SHARP", "EOL", "COMMENT", "INT", "BOOL", "CHAR", "STRING", 
			"BOOL_LITER", "CHAR_LITER", "STR_LITER", "FST", "SND", "PAIR_LITER", 
			"PAIR", "NEWPAIR", "STRUCT", "NEW", "DOT", "EMPTY", "BEGIN", "END", "IS", 
			"SKP", "ASSIGN", "READ", "FREE", "RETURN", "EXIT", "PRINT", "PRINTLN", 
			"IF", "ELSE", "THEN", "FI", "WHILE", "DO", "DONE", "CASE", "FOR", "BREAK", 
			"CONTINUE", "SWITCH", "DEFAULT", "CALL", "SEMICOLON", "COMMA", "IMPORT", 
			"OPEN_PARENTHESES", "CLOSE_PARENTHESES", "OPEN_SQUARE_BRACKET", "CLOSE_SQUARE_BRACKET", 
			"OPEN_CURLY_BRACKET", "CLOSE_CURLY_BRACKET", "PLUS", "MINUS", "NOT", 
			"LEN", "ORD", "CHR", "MUL", "DIV", "MOD", "GREATER", "GREATER_EQUAL", 
			"LESS", "LESS_EQUAL", "EQUAL", "UNEQUAL", "AND", "OR", "BitWiseAnd", 
			"BitWiseOr", "BitWiseComplement", "INT_LITER", "BINARY_LITER", "OCTAL_LITER", 
			"HEX_LITER", "FILE_NAME", "IDENT"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "WACCParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public WACCParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class Import_fileContext extends ParserRuleContext {
		public TerminalNode IMPORT() { return getToken(WACCParser.IMPORT, 0); }
		public TerminalNode FILE_NAME() { return getToken(WACCParser.FILE_NAME, 0); }
		public Import_fileContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_import_file; }
	}

	public final Import_fileContext import_file() throws RecognitionException {
		Import_fileContext _localctx = new Import_fileContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_import_file);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(64);
			match(IMPORT);
			setState(65);
			match(FILE_NAME);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DeclaritionContext extends ParserRuleContext {
		public List<Import_fileContext> import_file() {
			return getRuleContexts(Import_fileContext.class);
		}
		public Import_fileContext import_file(int i) {
			return getRuleContext(Import_fileContext.class,i);
		}
		public List<StructContext> struct() {
			return getRuleContexts(StructContext.class);
		}
		public StructContext struct(int i) {
			return getRuleContext(StructContext.class,i);
		}
		public List<FuncContext> func() {
			return getRuleContexts(FuncContext.class);
		}
		public FuncContext func(int i) {
			return getRuleContext(FuncContext.class,i);
		}
		public DeclaritionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declarition; }
	}

	public final DeclaritionContext declarition() throws RecognitionException {
		DeclaritionContext _localctx = new DeclaritionContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_declarition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(70);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IMPORT) {
				{
				{
				setState(67);
				import_file();
				}
				}
				setState(72);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(77);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << INT) | (1L << BOOL) | (1L << CHAR) | (1L << STRING) | (1L << PAIR) | (1L << STRUCT))) != 0) || _la==IDENT) {
				{
				setState(75);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case STRUCT:
					{
					setState(73);
					struct();
					}
					break;
				case INT:
				case BOOL:
				case CHAR:
				case STRING:
				case PAIR:
				case IDENT:
					{
					setState(74);
					func();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(79);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ProgramContext extends ParserRuleContext {
		public TerminalNode BEGIN() { return getToken(WACCParser.BEGIN, 0); }
		public StatContext stat() {
			return getRuleContext(StatContext.class,0);
		}
		public TerminalNode END() { return getToken(WACCParser.END, 0); }
		public TerminalNode EOF() { return getToken(WACCParser.EOF, 0); }
		public List<Import_fileContext> import_file() {
			return getRuleContexts(Import_fileContext.class);
		}
		public Import_fileContext import_file(int i) {
			return getRuleContext(Import_fileContext.class,i);
		}
		public List<StructContext> struct() {
			return getRuleContexts(StructContext.class);
		}
		public StructContext struct(int i) {
			return getRuleContext(StructContext.class,i);
		}
		public List<FuncContext> func() {
			return getRuleContexts(FuncContext.class);
		}
		public FuncContext func(int i) {
			return getRuleContext(FuncContext.class,i);
		}
		public ProgramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_program; }
	}

	public final ProgramContext program() throws RecognitionException {
		ProgramContext _localctx = new ProgramContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_program);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(83);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IMPORT) {
				{
				{
				setState(80);
				import_file();
				}
				}
				setState(85);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(86);
			match(BEGIN);
			setState(91);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(89);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case STRUCT:
						{
						setState(87);
						struct();
						}
						break;
					case INT:
					case BOOL:
					case CHAR:
					case STRING:
					case PAIR:
					case IDENT:
						{
						setState(88);
						func();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					} 
				}
				setState(93);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			}
			setState(94);
			stat(0);
			setState(95);
			match(END);
			setState(96);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StructContext extends ParserRuleContext {
		public TerminalNode STRUCT() { return getToken(WACCParser.STRUCT, 0); }
		public TerminalNode IDENT() { return getToken(WACCParser.IDENT, 0); }
		public TerminalNode IS() { return getToken(WACCParser.IS, 0); }
		public TerminalNode OPEN_CURLY_BRACKET() { return getToken(WACCParser.OPEN_CURLY_BRACKET, 0); }
		public TerminalNode CLOSE_CURLY_BRACKET() { return getToken(WACCParser.CLOSE_CURLY_BRACKET, 0); }
		public Param_listContext param_list() {
			return getRuleContext(Param_listContext.class,0);
		}
		public StructContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_struct; }
	}

	public final StructContext struct() throws RecognitionException {
		StructContext _localctx = new StructContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_struct);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(98);
			match(STRUCT);
			setState(99);
			match(IDENT);
			setState(100);
			match(IS);
			setState(101);
			match(OPEN_CURLY_BRACKET);
			setState(103);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << INT) | (1L << BOOL) | (1L << CHAR) | (1L << STRING) | (1L << PAIR))) != 0) || _la==IDENT) {
				{
				setState(102);
				param_list();
				}
			}

			setState(105);
			match(CLOSE_CURLY_BRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FuncContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TerminalNode IDENT() { return getToken(WACCParser.IDENT, 0); }
		public TerminalNode OPEN_PARENTHESES() { return getToken(WACCParser.OPEN_PARENTHESES, 0); }
		public TerminalNode CLOSE_PARENTHESES() { return getToken(WACCParser.CLOSE_PARENTHESES, 0); }
		public TerminalNode IS() { return getToken(WACCParser.IS, 0); }
		public StatContext stat() {
			return getRuleContext(StatContext.class,0);
		}
		public TerminalNode END() { return getToken(WACCParser.END, 0); }
		public Param_listContext param_list() {
			return getRuleContext(Param_listContext.class,0);
		}
		public FuncContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_func; }
	}

	public final FuncContext func() throws RecognitionException {
		FuncContext _localctx = new FuncContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_func);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(107);
			type();
			setState(108);
			match(IDENT);
			setState(109);
			match(OPEN_PARENTHESES);
			setState(111);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << INT) | (1L << BOOL) | (1L << CHAR) | (1L << STRING) | (1L << PAIR))) != 0) || _la==IDENT) {
				{
				setState(110);
				param_list();
				}
			}

			setState(113);
			match(CLOSE_PARENTHESES);
			setState(114);
			match(IS);
			setState(115);
			stat(0);
			setState(116);
			match(END);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Param_listContext extends ParserRuleContext {
		public List<ParamContext> param() {
			return getRuleContexts(ParamContext.class);
		}
		public ParamContext param(int i) {
			return getRuleContext(ParamContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(WACCParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(WACCParser.COMMA, i);
		}
		public Param_listContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_param_list; }
	}

	public final Param_listContext param_list() throws RecognitionException {
		Param_listContext _localctx = new Param_listContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_param_list);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(118);
			param();
			setState(123);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(119);
				match(COMMA);
				setState(120);
				param();
				}
				}
				setState(125);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParamContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TerminalNode IDENT() { return getToken(WACCParser.IDENT, 0); }
		public ParamContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_param; }
	}

	public final ParamContext param() throws RecognitionException {
		ParamContext _localctx = new ParamContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_param);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(126);
			type();
			setState(127);
			match(IDENT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StatContext extends ParserRuleContext {
		public StatContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stat; }
	 
		public StatContext() { }
		public void copyFrom(StatContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ReturnStatContext extends StatContext {
		public TerminalNode RETURN() { return getToken(WACCParser.RETURN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ReturnStatContext(StatContext ctx) { copyFrom(ctx); }
	}
	public static class BreakStatContext extends StatContext {
		public TerminalNode BREAK() { return getToken(WACCParser.BREAK, 0); }
		public BreakStatContext(StatContext ctx) { copyFrom(ctx); }
	}
	public static class ContinueStatContext extends StatContext {
		public TerminalNode CONTINUE() { return getToken(WACCParser.CONTINUE, 0); }
		public ContinueStatContext(StatContext ctx) { copyFrom(ctx); }
	}
	public static class StatExitStatContext extends StatContext {
		public ExitContext exit() {
			return getRuleContext(ExitContext.class,0);
		}
		public StatExitStatContext(StatContext ctx) { copyFrom(ctx); }
	}
	public static class ScopeStatContext extends StatContext {
		public TerminalNode BEGIN() { return getToken(WACCParser.BEGIN, 0); }
		public StatContext stat() {
			return getRuleContext(StatContext.class,0);
		}
		public TerminalNode END() { return getToken(WACCParser.END, 0); }
		public ScopeStatContext(StatContext ctx) { copyFrom(ctx); }
	}
	public static class IfStatContext extends StatContext {
		public TerminalNode IF() { return getToken(WACCParser.IF, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode THEN() { return getToken(WACCParser.THEN, 0); }
		public List<StatContext> stat() {
			return getRuleContexts(StatContext.class);
		}
		public StatContext stat(int i) {
			return getRuleContext(StatContext.class,i);
		}
		public TerminalNode ELSE() { return getToken(WACCParser.ELSE, 0); }
		public TerminalNode FI() { return getToken(WACCParser.FI, 0); }
		public IfStatContext(StatContext ctx) { copyFrom(ctx); }
	}
	public static class WhileStatContext extends StatContext {
		public TerminalNode WHILE() { return getToken(WACCParser.WHILE, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode DO() { return getToken(WACCParser.DO, 0); }
		public StatContext stat() {
			return getRuleContext(StatContext.class,0);
		}
		public TerminalNode DONE() { return getToken(WACCParser.DONE, 0); }
		public WhileStatContext(StatContext ctx) { copyFrom(ctx); }
	}
	public static class StatReadStatContext extends StatContext {
		public ReadContext read() {
			return getRuleContext(ReadContext.class,0);
		}
		public StatReadStatContext(StatContext ctx) { copyFrom(ctx); }
	}
	public static class StatFreeStatContext extends StatContext {
		public FreeContext free() {
			return getRuleContext(FreeContext.class,0);
		}
		public StatFreeStatContext(StatContext ctx) { copyFrom(ctx); }
	}
	public static class StatSkipStatContext extends StatContext {
		public SkpContext skp() {
			return getRuleContext(SkpContext.class,0);
		}
		public StatSkipStatContext(StatContext ctx) { copyFrom(ctx); }
	}
	public static class SeqStatContext extends StatContext {
		public List<StatContext> stat() {
			return getRuleContexts(StatContext.class);
		}
		public StatContext stat(int i) {
			return getRuleContext(StatContext.class,i);
		}
		public TerminalNode SEMICOLON() { return getToken(WACCParser.SEMICOLON, 0); }
		public SeqStatContext(StatContext ctx) { copyFrom(ctx); }
	}
	public static class ForStatContext extends StatContext {
		public TerminalNode FOR() { return getToken(WACCParser.FOR, 0); }
		public TerminalNode OPEN_PARENTHESES() { return getToken(WACCParser.OPEN_PARENTHESES, 0); }
		public List<For_statContext> for_stat() {
			return getRuleContexts(For_statContext.class);
		}
		public For_statContext for_stat(int i) {
			return getRuleContext(For_statContext.class,i);
		}
		public List<TerminalNode> SEMICOLON() { return getTokens(WACCParser.SEMICOLON); }
		public TerminalNode SEMICOLON(int i) {
			return getToken(WACCParser.SEMICOLON, i);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode CLOSE_PARENTHESES() { return getToken(WACCParser.CLOSE_PARENTHESES, 0); }
		public TerminalNode DO() { return getToken(WACCParser.DO, 0); }
		public StatContext stat() {
			return getRuleContext(StatContext.class,0);
		}
		public TerminalNode DONE() { return getToken(WACCParser.DONE, 0); }
		public ForStatContext(StatContext ctx) { copyFrom(ctx); }
	}
	public static class StatPrintStatContext extends StatContext {
		public PrintContext print() {
			return getRuleContext(PrintContext.class,0);
		}
		public StatPrintStatContext(StatContext ctx) { copyFrom(ctx); }
	}
	public static class StatPrintlnStatContext extends StatContext {
		public PrintlnContext println() {
			return getRuleContext(PrintlnContext.class,0);
		}
		public StatPrintlnStatContext(StatContext ctx) { copyFrom(ctx); }
	}
	public static class SwitchStatContext extends StatContext {
		public TerminalNode SWITCH() { return getToken(WACCParser.SWITCH, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode DO() { return getToken(WACCParser.DO, 0); }
		public TerminalNode DEFAULT() { return getToken(WACCParser.DEFAULT, 0); }
		public List<StatContext> stat() {
			return getRuleContexts(StatContext.class);
		}
		public StatContext stat(int i) {
			return getRuleContext(StatContext.class,i);
		}
		public TerminalNode DONE() { return getToken(WACCParser.DONE, 0); }
		public List<TerminalNode> CASE() { return getTokens(WACCParser.CASE); }
		public TerminalNode CASE(int i) {
			return getToken(WACCParser.CASE, i);
		}
		public SwitchStatContext(StatContext ctx) { copyFrom(ctx); }
	}
	public static class DoWhileStatContext extends StatContext {
		public TerminalNode DO() { return getToken(WACCParser.DO, 0); }
		public StatContext stat() {
			return getRuleContext(StatContext.class,0);
		}
		public TerminalNode WHILE() { return getToken(WACCParser.WHILE, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public DoWhileStatContext(StatContext ctx) { copyFrom(ctx); }
	}
	public static class StatDeclareStatContext extends StatContext {
		public DeclareContext declare() {
			return getRuleContext(DeclareContext.class,0);
		}
		public StatDeclareStatContext(StatContext ctx) { copyFrom(ctx); }
	}
	public static class StatAssignStatContext extends StatContext {
		public AssignContext assign() {
			return getRuleContext(AssignContext.class,0);
		}
		public StatAssignStatContext(StatContext ctx) { copyFrom(ctx); }
	}

	public final StatContext stat() throws RecognitionException {
		return stat(0);
	}

	private StatContext stat(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		StatContext _localctx = new StatContext(_ctx, _parentState);
		StatContext _prevctx = _localctx;
		int _startState = 14;
		enterRecursionRule(_localctx, 14, RULE_stat, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(193);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				{
				_localctx = new StatSkipStatContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(130);
				skp();
				}
				break;
			case 2:
				{
				_localctx = new StatDeclareStatContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(131);
				declare();
				}
				break;
			case 3:
				{
				_localctx = new StatAssignStatContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(132);
				assign();
				}
				break;
			case 4:
				{
				_localctx = new StatReadStatContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(133);
				read();
				}
				break;
			case 5:
				{
				_localctx = new StatFreeStatContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(134);
				free();
				}
				break;
			case 6:
				{
				_localctx = new ReturnStatContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(135);
				match(RETURN);
				setState(136);
				expr(0);
				}
				break;
			case 7:
				{
				_localctx = new StatExitStatContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(137);
				exit();
				}
				break;
			case 8:
				{
				_localctx = new StatPrintStatContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(138);
				print();
				}
				break;
			case 9:
				{
				_localctx = new StatPrintlnStatContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(139);
				println();
				}
				break;
			case 10:
				{
				_localctx = new BreakStatContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(140);
				match(BREAK);
				}
				break;
			case 11:
				{
				_localctx = new ContinueStatContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(141);
				match(CONTINUE);
				}
				break;
			case 12:
				{
				_localctx = new IfStatContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(142);
				match(IF);
				setState(143);
				expr(0);
				setState(144);
				match(THEN);
				setState(145);
				stat(0);
				setState(146);
				match(ELSE);
				setState(147);
				stat(0);
				setState(148);
				match(FI);
				}
				break;
			case 13:
				{
				_localctx = new ForStatContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(150);
				match(FOR);
				setState(151);
				match(OPEN_PARENTHESES);
				setState(152);
				for_stat(0);
				setState(153);
				match(SEMICOLON);
				setState(154);
				expr(0);
				setState(155);
				match(SEMICOLON);
				setState(156);
				for_stat(0);
				setState(157);
				match(CLOSE_PARENTHESES);
				setState(158);
				match(DO);
				setState(159);
				stat(0);
				setState(160);
				match(DONE);
				}
				break;
			case 14:
				{
				_localctx = new SwitchStatContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(162);
				match(SWITCH);
				setState(163);
				expr(0);
				setState(164);
				match(DO);
				setState(171);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==CASE) {
					{
					{
					setState(165);
					match(CASE);
					setState(166);
					expr(0);
					setState(167);
					stat(0);
					}
					}
					setState(173);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(174);
				match(DEFAULT);
				setState(175);
				stat(0);
				setState(176);
				match(DONE);
				}
				break;
			case 15:
				{
				_localctx = new DoWhileStatContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(178);
				match(DO);
				setState(179);
				stat(0);
				setState(180);
				match(WHILE);
				setState(181);
				expr(0);
				}
				break;
			case 16:
				{
				_localctx = new WhileStatContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(183);
				match(WHILE);
				setState(184);
				expr(0);
				setState(185);
				match(DO);
				setState(186);
				stat(0);
				setState(187);
				match(DONE);
				}
				break;
			case 17:
				{
				_localctx = new ScopeStatContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(189);
				match(BEGIN);
				setState(190);
				stat(0);
				setState(191);
				match(END);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(200);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new SeqStatContext(new StatContext(_parentctx, _parentState));
					pushNewRecursionContext(_localctx, _startState, RULE_stat);
					setState(195);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(196);
					match(SEMICOLON);
					setState(197);
					stat(2);
					}
					} 
				}
				setState(202);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class Assign_lhsContext extends ParserRuleContext {
		public Assign_lhsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assign_lhs; }
	 
		public Assign_lhsContext() { }
		public void copyFrom(Assign_lhsContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class IdentContext extends Assign_lhsContext {
		public TerminalNode IDENT() { return getToken(WACCParser.IDENT, 0); }
		public IdentContext(Assign_lhsContext ctx) { copyFrom(ctx); }
	}
	public static class LHSArrayElemContext extends Assign_lhsContext {
		public Array_elemContext array_elem() {
			return getRuleContext(Array_elemContext.class,0);
		}
		public LHSArrayElemContext(Assign_lhsContext ctx) { copyFrom(ctx); }
	}
	public static class LHSPairElemContext extends Assign_lhsContext {
		public Pair_elemContext pair_elem() {
			return getRuleContext(Pair_elemContext.class,0);
		}
		public LHSPairElemContext(Assign_lhsContext ctx) { copyFrom(ctx); }
	}
	public static class LHSStructElemContext extends Assign_lhsContext {
		public Struct_elemContext struct_elem() {
			return getRuleContext(Struct_elemContext.class,0);
		}
		public LHSStructElemContext(Assign_lhsContext ctx) { copyFrom(ctx); }
	}

	public final Assign_lhsContext assign_lhs() throws RecognitionException {
		Assign_lhsContext _localctx = new Assign_lhsContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_assign_lhs);
		try {
			setState(207);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
			case 1:
				_localctx = new IdentContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(203);
				match(IDENT);
				}
				break;
			case 2:
				_localctx = new LHSArrayElemContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(204);
				array_elem();
				}
				break;
			case 3:
				_localctx = new LHSPairElemContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(205);
				pair_elem();
				}
				break;
			case 4:
				_localctx = new LHSStructElemContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(206);
				struct_elem();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class For_statContext extends ParserRuleContext {
		public For_statContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_for_stat; }
	 
		public For_statContext() { }
		public void copyFrom(For_statContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ForStatSkpContext extends For_statContext {
		public SkpContext skp() {
			return getRuleContext(SkpContext.class,0);
		}
		public ForStatSkpContext(For_statContext ctx) { copyFrom(ctx); }
	}
	public static class ForStatFreeContext extends For_statContext {
		public FreeContext free() {
			return getRuleContext(FreeContext.class,0);
		}
		public ForStatFreeContext(For_statContext ctx) { copyFrom(ctx); }
	}
	public static class ForStatAssignContext extends For_statContext {
		public AssignContext assign() {
			return getRuleContext(AssignContext.class,0);
		}
		public ForStatAssignContext(For_statContext ctx) { copyFrom(ctx); }
	}
	public static class ForStatExitContext extends For_statContext {
		public ExitContext exit() {
			return getRuleContext(ExitContext.class,0);
		}
		public ForStatExitContext(For_statContext ctx) { copyFrom(ctx); }
	}
	public static class ForStatPrintContext extends For_statContext {
		public PrintContext print() {
			return getRuleContext(PrintContext.class,0);
		}
		public ForStatPrintContext(For_statContext ctx) { copyFrom(ctx); }
	}
	public static class ForStatDeclareContext extends For_statContext {
		public DeclareContext declare() {
			return getRuleContext(DeclareContext.class,0);
		}
		public ForStatDeclareContext(For_statContext ctx) { copyFrom(ctx); }
	}
	public static class ForStatPrintlnContext extends For_statContext {
		public PrintlnContext println() {
			return getRuleContext(PrintlnContext.class,0);
		}
		public ForStatPrintlnContext(For_statContext ctx) { copyFrom(ctx); }
	}
	public static class ForStatSeqContext extends For_statContext {
		public List<For_statContext> for_stat() {
			return getRuleContexts(For_statContext.class);
		}
		public For_statContext for_stat(int i) {
			return getRuleContext(For_statContext.class,i);
		}
		public TerminalNode COMMA() { return getToken(WACCParser.COMMA, 0); }
		public ForStatSeqContext(For_statContext ctx) { copyFrom(ctx); }
	}
	public static class ForStatReadContext extends For_statContext {
		public ReadContext read() {
			return getRuleContext(ReadContext.class,0);
		}
		public ForStatReadContext(For_statContext ctx) { copyFrom(ctx); }
	}

	public final For_statContext for_stat() throws RecognitionException {
		return for_stat(0);
	}

	private For_statContext for_stat(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		For_statContext _localctx = new For_statContext(_ctx, _parentState);
		For_statContext _prevctx = _localctx;
		int _startState = 18;
		enterRecursionRule(_localctx, 18, RULE_for_stat, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(218);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
			case 1:
				{
				_localctx = new ForStatSkpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(210);
				skp();
				}
				break;
			case 2:
				{
				_localctx = new ForStatDeclareContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(211);
				declare();
				}
				break;
			case 3:
				{
				_localctx = new ForStatAssignContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(212);
				assign();
				}
				break;
			case 4:
				{
				_localctx = new ForStatReadContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(213);
				read();
				}
				break;
			case 5:
				{
				_localctx = new ForStatFreeContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(214);
				free();
				}
				break;
			case 6:
				{
				_localctx = new ForStatExitContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(215);
				exit();
				}
				break;
			case 7:
				{
				_localctx = new ForStatPrintContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(216);
				print();
				}
				break;
			case 8:
				{
				_localctx = new ForStatPrintlnContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(217);
				println();
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(225);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,14,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new ForStatSeqContext(new For_statContext(_parentctx, _parentState));
					pushNewRecursionContext(_localctx, _startState, RULE_for_stat);
					setState(220);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(221);
					match(COMMA);
					setState(222);
					for_stat(2);
					}
					} 
				}
				setState(227);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,14,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class SkpContext extends ParserRuleContext {
		public SkpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_skp; }
	 
		public SkpContext() { }
		public void copyFrom(SkpContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class SkipStatContext extends SkpContext {
		public TerminalNode SKP() { return getToken(WACCParser.SKP, 0); }
		public SkipStatContext(SkpContext ctx) { copyFrom(ctx); }
	}

	public final SkpContext skp() throws RecognitionException {
		SkpContext _localctx = new SkpContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_skp);
		try {
			_localctx = new SkipStatContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(228);
			match(SKP);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DeclareContext extends ParserRuleContext {
		public DeclareContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declare; }
	 
		public DeclareContext() { }
		public void copyFrom(DeclareContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class DeclareStatContext extends DeclareContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TerminalNode IDENT() { return getToken(WACCParser.IDENT, 0); }
		public TerminalNode ASSIGN() { return getToken(WACCParser.ASSIGN, 0); }
		public Assign_rhsContext assign_rhs() {
			return getRuleContext(Assign_rhsContext.class,0);
		}
		public DeclareStatContext(DeclareContext ctx) { copyFrom(ctx); }
	}

	public final DeclareContext declare() throws RecognitionException {
		DeclareContext _localctx = new DeclareContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_declare);
		try {
			_localctx = new DeclareStatContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(230);
			type();
			setState(231);
			match(IDENT);
			setState(232);
			match(ASSIGN);
			setState(233);
			assign_rhs();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AssignContext extends ParserRuleContext {
		public AssignContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assign; }
	 
		public AssignContext() { }
		public void copyFrom(AssignContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class AssignStatContext extends AssignContext {
		public Assign_lhsContext assign_lhs() {
			return getRuleContext(Assign_lhsContext.class,0);
		}
		public TerminalNode ASSIGN() { return getToken(WACCParser.ASSIGN, 0); }
		public Assign_rhsContext assign_rhs() {
			return getRuleContext(Assign_rhsContext.class,0);
		}
		public AssignStatContext(AssignContext ctx) { copyFrom(ctx); }
	}

	public final AssignContext assign() throws RecognitionException {
		AssignContext _localctx = new AssignContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_assign);
		try {
			_localctx = new AssignStatContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(235);
			assign_lhs();
			setState(236);
			match(ASSIGN);
			setState(237);
			assign_rhs();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ReadContext extends ParserRuleContext {
		public ReadContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_read; }
	 
		public ReadContext() { }
		public void copyFrom(ReadContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ReadStatContext extends ReadContext {
		public TerminalNode READ() { return getToken(WACCParser.READ, 0); }
		public Assign_lhsContext assign_lhs() {
			return getRuleContext(Assign_lhsContext.class,0);
		}
		public ReadStatContext(ReadContext ctx) { copyFrom(ctx); }
	}

	public final ReadContext read() throws RecognitionException {
		ReadContext _localctx = new ReadContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_read);
		try {
			_localctx = new ReadStatContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(239);
			match(READ);
			setState(240);
			assign_lhs();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FreeContext extends ParserRuleContext {
		public FreeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_free; }
	 
		public FreeContext() { }
		public void copyFrom(FreeContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class FreeStatContext extends FreeContext {
		public TerminalNode FREE() { return getToken(WACCParser.FREE, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public FreeStatContext(FreeContext ctx) { copyFrom(ctx); }
	}

	public final FreeContext free() throws RecognitionException {
		FreeContext _localctx = new FreeContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_free);
		try {
			_localctx = new FreeStatContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(242);
			match(FREE);
			setState(243);
			expr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExitContext extends ParserRuleContext {
		public ExitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exit; }
	 
		public ExitContext() { }
		public void copyFrom(ExitContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ExitStatContext extends ExitContext {
		public TerminalNode EXIT() { return getToken(WACCParser.EXIT, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ExitStatContext(ExitContext ctx) { copyFrom(ctx); }
	}

	public final ExitContext exit() throws RecognitionException {
		ExitContext _localctx = new ExitContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_exit);
		try {
			_localctx = new ExitStatContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(245);
			match(EXIT);
			setState(246);
			expr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PrintContext extends ParserRuleContext {
		public PrintContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_print; }
	 
		public PrintContext() { }
		public void copyFrom(PrintContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class PrintStatContext extends PrintContext {
		public TerminalNode PRINT() { return getToken(WACCParser.PRINT, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public PrintStatContext(PrintContext ctx) { copyFrom(ctx); }
	}

	public final PrintContext print() throws RecognitionException {
		PrintContext _localctx = new PrintContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_print);
		try {
			_localctx = new PrintStatContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(248);
			match(PRINT);
			setState(249);
			expr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PrintlnContext extends ParserRuleContext {
		public PrintlnContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_println; }
	 
		public PrintlnContext() { }
		public void copyFrom(PrintlnContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class PrintlnStatContext extends PrintlnContext {
		public TerminalNode PRINTLN() { return getToken(WACCParser.PRINTLN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public PrintlnStatContext(PrintlnContext ctx) { copyFrom(ctx); }
	}

	public final PrintlnContext println() throws RecognitionException {
		PrintlnContext _localctx = new PrintlnContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_println);
		try {
			_localctx = new PrintlnStatContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(251);
			match(PRINTLN);
			setState(252);
			expr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Assign_rhsContext extends ParserRuleContext {
		public Assign_rhsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assign_rhs; }
	 
		public Assign_rhsContext() { }
		public void copyFrom(Assign_rhsContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ExprNodeContext extends Assign_rhsContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ExprNodeContext(Assign_rhsContext ctx) { copyFrom(ctx); }
	}
	public static class RHSPairElemContext extends Assign_rhsContext {
		public Pair_elemContext pair_elem() {
			return getRuleContext(Pair_elemContext.class,0);
		}
		public RHSPairElemContext(Assign_rhsContext ctx) { copyFrom(ctx); }
	}
	public static class NewPairContext extends Assign_rhsContext {
		public TerminalNode NEWPAIR() { return getToken(WACCParser.NEWPAIR, 0); }
		public TerminalNode OPEN_PARENTHESES() { return getToken(WACCParser.OPEN_PARENTHESES, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode COMMA() { return getToken(WACCParser.COMMA, 0); }
		public TerminalNode CLOSE_PARENTHESES() { return getToken(WACCParser.CLOSE_PARENTHESES, 0); }
		public NewPairContext(Assign_rhsContext ctx) { copyFrom(ctx); }
	}
	public static class FunctionCallContext extends Assign_rhsContext {
		public TerminalNode CALL() { return getToken(WACCParser.CALL, 0); }
		public TerminalNode IDENT() { return getToken(WACCParser.IDENT, 0); }
		public TerminalNode OPEN_PARENTHESES() { return getToken(WACCParser.OPEN_PARENTHESES, 0); }
		public TerminalNode CLOSE_PARENTHESES() { return getToken(WACCParser.CLOSE_PARENTHESES, 0); }
		public Arg_listContext arg_list() {
			return getRuleContext(Arg_listContext.class,0);
		}
		public FunctionCallContext(Assign_rhsContext ctx) { copyFrom(ctx); }
	}
	public static class ArrayLiteralContext extends Assign_rhsContext {
		public Array_literContext array_liter() {
			return getRuleContext(Array_literContext.class,0);
		}
		public ArrayLiteralContext(Assign_rhsContext ctx) { copyFrom(ctx); }
	}

	public final Assign_rhsContext assign_rhs() throws RecognitionException {
		Assign_rhsContext _localctx = new Assign_rhsContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_assign_rhs);
		int _la;
		try {
			setState(271);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case BOOL_LITER:
			case CHAR_LITER:
			case STR_LITER:
			case PAIR_LITER:
			case NEW:
			case EMPTY:
			case OPEN_PARENTHESES:
			case PLUS:
			case MINUS:
			case NOT:
			case LEN:
			case ORD:
			case CHR:
			case BitWiseComplement:
			case INT_LITER:
			case BINARY_LITER:
			case OCTAL_LITER:
			case HEX_LITER:
			case IDENT:
				_localctx = new ExprNodeContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(254);
				expr(0);
				}
				break;
			case OPEN_SQUARE_BRACKET:
				_localctx = new ArrayLiteralContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(255);
				array_liter();
				}
				break;
			case NEWPAIR:
				_localctx = new NewPairContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(256);
				match(NEWPAIR);
				setState(257);
				match(OPEN_PARENTHESES);
				setState(258);
				expr(0);
				setState(259);
				match(COMMA);
				setState(260);
				expr(0);
				setState(261);
				match(CLOSE_PARENTHESES);
				}
				break;
			case FST:
			case SND:
				_localctx = new RHSPairElemContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(263);
				pair_elem();
				}
				break;
			case CALL:
				_localctx = new FunctionCallContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(264);
				match(CALL);
				setState(265);
				match(IDENT);
				setState(266);
				match(OPEN_PARENTHESES);
				setState(268);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOL_LITER) | (1L << CHAR_LITER) | (1L << STR_LITER) | (1L << PAIR_LITER) | (1L << NEW) | (1L << EMPTY) | (1L << OPEN_PARENTHESES) | (1L << PLUS) | (1L << MINUS) | (1L << NOT) | (1L << LEN) | (1L << ORD) | (1L << CHR))) != 0) || ((((_la - 74)) & ~0x3f) == 0 && ((1L << (_la - 74)) & ((1L << (BitWiseComplement - 74)) | (1L << (INT_LITER - 74)) | (1L << (BINARY_LITER - 74)) | (1L << (OCTAL_LITER - 74)) | (1L << (HEX_LITER - 74)) | (1L << (IDENT - 74)))) != 0)) {
					{
					setState(267);
					arg_list();
					}
				}

				setState(270);
				match(CLOSE_PARENTHESES);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Arg_listContext extends ParserRuleContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(WACCParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(WACCParser.COMMA, i);
		}
		public Arg_listContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arg_list; }
	}

	public final Arg_listContext arg_list() throws RecognitionException {
		Arg_listContext _localctx = new Arg_listContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_arg_list);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(273);
			expr(0);
			setState(278);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(274);
				match(COMMA);
				setState(275);
				expr(0);
				}
				}
				setState(280);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Struct_elemContext extends ParserRuleContext {
		public List<TerminalNode> IDENT() { return getTokens(WACCParser.IDENT); }
		public TerminalNode IDENT(int i) {
			return getToken(WACCParser.IDENT, i);
		}
		public List<TerminalNode> DOT() { return getTokens(WACCParser.DOT); }
		public TerminalNode DOT(int i) {
			return getToken(WACCParser.DOT, i);
		}
		public Struct_elemContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_struct_elem; }
	}

	public final Struct_elemContext struct_elem() throws RecognitionException {
		Struct_elemContext _localctx = new Struct_elemContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_struct_elem);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(281);
			match(IDENT);
			setState(284); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(282);
					match(DOT);
					setState(283);
					match(IDENT);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(286); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,18,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class New_structContext extends ParserRuleContext {
		public TerminalNode NEW() { return getToken(WACCParser.NEW, 0); }
		public TerminalNode IDENT() { return getToken(WACCParser.IDENT, 0); }
		public TerminalNode OPEN_CURLY_BRACKET() { return getToken(WACCParser.OPEN_CURLY_BRACKET, 0); }
		public TerminalNode CLOSE_CURLY_BRACKET() { return getToken(WACCParser.CLOSE_CURLY_BRACKET, 0); }
		public Arg_listContext arg_list() {
			return getRuleContext(Arg_listContext.class,0);
		}
		public New_structContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_new_struct; }
	}

	public final New_structContext new_struct() throws RecognitionException {
		New_structContext _localctx = new New_structContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_new_struct);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(288);
			match(NEW);
			setState(289);
			match(IDENT);
			setState(290);
			match(OPEN_CURLY_BRACKET);
			setState(292);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOL_LITER) | (1L << CHAR_LITER) | (1L << STR_LITER) | (1L << PAIR_LITER) | (1L << NEW) | (1L << EMPTY) | (1L << OPEN_PARENTHESES) | (1L << PLUS) | (1L << MINUS) | (1L << NOT) | (1L << LEN) | (1L << ORD) | (1L << CHR))) != 0) || ((((_la - 74)) & ~0x3f) == 0 && ((1L << (_la - 74)) & ((1L << (BitWiseComplement - 74)) | (1L << (INT_LITER - 74)) | (1L << (BINARY_LITER - 74)) | (1L << (OCTAL_LITER - 74)) | (1L << (HEX_LITER - 74)) | (1L << (IDENT - 74)))) != 0)) {
				{
				setState(291);
				arg_list();
				}
			}

			setState(294);
			match(CLOSE_CURLY_BRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Pair_elemContext extends ParserRuleContext {
		public Pair_elemContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pair_elem; }
	 
		public Pair_elemContext() { }
		public void copyFrom(Pair_elemContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class SndExprContext extends Pair_elemContext {
		public TerminalNode SND() { return getToken(WACCParser.SND, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public SndExprContext(Pair_elemContext ctx) { copyFrom(ctx); }
	}
	public static class FstExprContext extends Pair_elemContext {
		public TerminalNode FST() { return getToken(WACCParser.FST, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public FstExprContext(Pair_elemContext ctx) { copyFrom(ctx); }
	}

	public final Pair_elemContext pair_elem() throws RecognitionException {
		Pair_elemContext _localctx = new Pair_elemContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_pair_elem);
		try {
			setState(300);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FST:
				_localctx = new FstExprContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(296);
				match(FST);
				setState(297);
				expr(0);
				}
				break;
			case SND:
				_localctx = new SndExprContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(298);
				match(SND);
				setState(299);
				expr(0);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeContext extends ParserRuleContext {
		public TypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type; }
	 
		public TypeContext() { }
		public void copyFrom(TypeContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ArrayTypeContext extends TypeContext {
		public Array_typeContext array_type() {
			return getRuleContext(Array_typeContext.class,0);
		}
		public ArrayTypeContext(TypeContext ctx) { copyFrom(ctx); }
	}
	public static class PairTypeContext extends TypeContext {
		public Pair_typeContext pair_type() {
			return getRuleContext(Pair_typeContext.class,0);
		}
		public PairTypeContext(TypeContext ctx) { copyFrom(ctx); }
	}
	public static class StructTypeContext extends TypeContext {
		public Struct_typeContext struct_type() {
			return getRuleContext(Struct_typeContext.class,0);
		}
		public StructTypeContext(TypeContext ctx) { copyFrom(ctx); }
	}
	public static class BaseTypeContext extends TypeContext {
		public Base_typeContext base_type() {
			return getRuleContext(Base_typeContext.class,0);
		}
		public BaseTypeContext(TypeContext ctx) { copyFrom(ctx); }
	}

	public final TypeContext type() throws RecognitionException {
		TypeContext _localctx = new TypeContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_type);
		try {
			setState(306);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
			case 1:
				_localctx = new BaseTypeContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(302);
				base_type();
				}
				break;
			case 2:
				_localctx = new ArrayTypeContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(303);
				array_type(0);
				}
				break;
			case 3:
				_localctx = new PairTypeContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(304);
				pair_type();
				}
				break;
			case 4:
				_localctx = new StructTypeContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(305);
				struct_type();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Base_typeContext extends ParserRuleContext {
		public Base_typeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_base_type; }
	 
		public Base_typeContext() { }
		public void copyFrom(Base_typeContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class BoolTypeContext extends Base_typeContext {
		public TerminalNode BOOL() { return getToken(WACCParser.BOOL, 0); }
		public BoolTypeContext(Base_typeContext ctx) { copyFrom(ctx); }
	}
	public static class StringTypeContext extends Base_typeContext {
		public TerminalNode STRING() { return getToken(WACCParser.STRING, 0); }
		public StringTypeContext(Base_typeContext ctx) { copyFrom(ctx); }
	}
	public static class CharTypeContext extends Base_typeContext {
		public TerminalNode CHAR() { return getToken(WACCParser.CHAR, 0); }
		public CharTypeContext(Base_typeContext ctx) { copyFrom(ctx); }
	}
	public static class IntTypeContext extends Base_typeContext {
		public TerminalNode INT() { return getToken(WACCParser.INT, 0); }
		public IntTypeContext(Base_typeContext ctx) { copyFrom(ctx); }
	}

	public final Base_typeContext base_type() throws RecognitionException {
		Base_typeContext _localctx = new Base_typeContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_base_type);
		try {
			setState(312);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case INT:
				_localctx = new IntTypeContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(308);
				match(INT);
				}
				break;
			case BOOL:
				_localctx = new BoolTypeContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(309);
				match(BOOL);
				}
				break;
			case CHAR:
				_localctx = new CharTypeContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(310);
				match(CHAR);
				}
				break;
			case STRING:
				_localctx = new StringTypeContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(311);
				match(STRING);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Array_typeContext extends ParserRuleContext {
		public Base_typeContext base_type() {
			return getRuleContext(Base_typeContext.class,0);
		}
		public TerminalNode OPEN_SQUARE_BRACKET() { return getToken(WACCParser.OPEN_SQUARE_BRACKET, 0); }
		public TerminalNode CLOSE_SQUARE_BRACKET() { return getToken(WACCParser.CLOSE_SQUARE_BRACKET, 0); }
		public Pair_typeContext pair_type() {
			return getRuleContext(Pair_typeContext.class,0);
		}
		public Struct_typeContext struct_type() {
			return getRuleContext(Struct_typeContext.class,0);
		}
		public Array_typeContext array_type() {
			return getRuleContext(Array_typeContext.class,0);
		}
		public Array_typeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_array_type; }
	}

	public final Array_typeContext array_type() throws RecognitionException {
		return array_type(0);
	}

	private Array_typeContext array_type(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		Array_typeContext _localctx = new Array_typeContext(_ctx, _parentState);
		Array_typeContext _prevctx = _localctx;
		int _startState = 50;
		enterRecursionRule(_localctx, 50, RULE_array_type, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(327);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case INT:
			case BOOL:
			case CHAR:
			case STRING:
				{
				setState(315);
				base_type();
				setState(316);
				match(OPEN_SQUARE_BRACKET);
				setState(317);
				match(CLOSE_SQUARE_BRACKET);
				}
				break;
			case PAIR:
				{
				setState(319);
				pair_type();
				setState(320);
				match(OPEN_SQUARE_BRACKET);
				setState(321);
				match(CLOSE_SQUARE_BRACKET);
				}
				break;
			case IDENT:
				{
				setState(323);
				struct_type();
				setState(324);
				match(OPEN_SQUARE_BRACKET);
				setState(325);
				match(CLOSE_SQUARE_BRACKET);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(334);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,24,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new Array_typeContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_array_type);
					setState(329);
					if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
					setState(330);
					match(OPEN_SQUARE_BRACKET);
					setState(331);
					match(CLOSE_SQUARE_BRACKET);
					}
					} 
				}
				setState(336);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,24,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class Pair_typeContext extends ParserRuleContext {
		public TerminalNode PAIR() { return getToken(WACCParser.PAIR, 0); }
		public TerminalNode OPEN_PARENTHESES() { return getToken(WACCParser.OPEN_PARENTHESES, 0); }
		public List<Pair_elem_typeContext> pair_elem_type() {
			return getRuleContexts(Pair_elem_typeContext.class);
		}
		public Pair_elem_typeContext pair_elem_type(int i) {
			return getRuleContext(Pair_elem_typeContext.class,i);
		}
		public TerminalNode COMMA() { return getToken(WACCParser.COMMA, 0); }
		public TerminalNode CLOSE_PARENTHESES() { return getToken(WACCParser.CLOSE_PARENTHESES, 0); }
		public Pair_typeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pair_type; }
	}

	public final Pair_typeContext pair_type() throws RecognitionException {
		Pair_typeContext _localctx = new Pair_typeContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_pair_type);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(337);
			match(PAIR);
			setState(338);
			match(OPEN_PARENTHESES);
			setState(339);
			pair_elem_type();
			setState(340);
			match(COMMA);
			setState(341);
			pair_elem_type();
			setState(342);
			match(CLOSE_PARENTHESES);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Pair_elem_typeContext extends ParserRuleContext {
		public Pair_elem_typeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pair_elem_type; }
	 
		public Pair_elem_typeContext() { }
		public void copyFrom(Pair_elem_typeContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class PairElemBaseTypeContext extends Pair_elem_typeContext {
		public Base_typeContext base_type() {
			return getRuleContext(Base_typeContext.class,0);
		}
		public PairElemBaseTypeContext(Pair_elem_typeContext ctx) { copyFrom(ctx); }
	}
	public static class PairElemStructTypeContext extends Pair_elem_typeContext {
		public Struct_typeContext struct_type() {
			return getRuleContext(Struct_typeContext.class,0);
		}
		public PairElemStructTypeContext(Pair_elem_typeContext ctx) { copyFrom(ctx); }
	}
	public static class PairElemPairTypeContext extends Pair_elem_typeContext {
		public TerminalNode PAIR() { return getToken(WACCParser.PAIR, 0); }
		public PairElemPairTypeContext(Pair_elem_typeContext ctx) { copyFrom(ctx); }
	}
	public static class PairElemArrayTypeContext extends Pair_elem_typeContext {
		public Array_typeContext array_type() {
			return getRuleContext(Array_typeContext.class,0);
		}
		public PairElemArrayTypeContext(Pair_elem_typeContext ctx) { copyFrom(ctx); }
	}

	public final Pair_elem_typeContext pair_elem_type() throws RecognitionException {
		Pair_elem_typeContext _localctx = new Pair_elem_typeContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_pair_elem_type);
		try {
			setState(348);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,25,_ctx) ) {
			case 1:
				_localctx = new PairElemBaseTypeContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(344);
				base_type();
				}
				break;
			case 2:
				_localctx = new PairElemArrayTypeContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(345);
				array_type(0);
				}
				break;
			case 3:
				_localctx = new PairElemStructTypeContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(346);
				struct_type();
				}
				break;
			case 4:
				_localctx = new PairElemPairTypeContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(347);
				match(PAIR);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Struct_typeContext extends ParserRuleContext {
		public TerminalNode IDENT() { return getToken(WACCParser.IDENT, 0); }
		public Struct_typeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_struct_type; }
	}

	public final Struct_typeContext struct_type() throws RecognitionException {
		Struct_typeContext _localctx = new Struct_typeContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_struct_type);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(350);
			match(IDENT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExprContext extends ParserRuleContext {
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
	 
		public ExprContext() { }
		public void copyFrom(ExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class AndExprContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode AND() { return getToken(WACCParser.AND, 0); }
		public AndExprContext(ExprContext ctx) { copyFrom(ctx); }
	}
	public static class BoolExprContext extends ExprContext {
		public TerminalNode BOOL_LITER() { return getToken(WACCParser.BOOL_LITER, 0); }
		public BoolExprContext(ExprContext ctx) { copyFrom(ctx); }
	}
	public static class PairExprContext extends ExprContext {
		public TerminalNode PAIR_LITER() { return getToken(WACCParser.PAIR_LITER, 0); }
		public PairExprContext(ExprContext ctx) { copyFrom(ctx); }
	}
	public static class BitwiseAndExprContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode BitWiseAnd() { return getToken(WACCParser.BitWiseAnd, 0); }
		public BitwiseAndExprContext(ExprContext ctx) { copyFrom(ctx); }
	}
	public static class IdExprContext extends ExprContext {
		public TerminalNode IDENT() { return getToken(WACCParser.IDENT, 0); }
		public IdExprContext(ExprContext ctx) { copyFrom(ctx); }
	}
	public static class BitwiseOrExprContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode BitWiseOr() { return getToken(WACCParser.BitWiseOr, 0); }
		public BitwiseOrExprContext(ExprContext ctx) { copyFrom(ctx); }
	}
	public static class HexExprContext extends ExprContext {
		public TerminalNode HEX_LITER() { return getToken(WACCParser.HEX_LITER, 0); }
		public HexExprContext(ExprContext ctx) { copyFrom(ctx); }
	}
	public static class BinaryExprContext extends ExprContext {
		public TerminalNode BINARY_LITER() { return getToken(WACCParser.BINARY_LITER, 0); }
		public BinaryExprContext(ExprContext ctx) { copyFrom(ctx); }
	}
	public static class StructElemExprContext extends ExprContext {
		public Struct_elemContext struct_elem() {
			return getRuleContext(Struct_elemContext.class,0);
		}
		public StructElemExprContext(ExprContext ctx) { copyFrom(ctx); }
	}
	public static class UnopExprContext extends ExprContext {
		public Token uop;
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode MINUS() { return getToken(WACCParser.MINUS, 0); }
		public TerminalNode NOT() { return getToken(WACCParser.NOT, 0); }
		public TerminalNode LEN() { return getToken(WACCParser.LEN, 0); }
		public TerminalNode ORD() { return getToken(WACCParser.ORD, 0); }
		public TerminalNode CHR() { return getToken(WACCParser.CHR, 0); }
		public TerminalNode BitWiseComplement() { return getToken(WACCParser.BitWiseComplement, 0); }
		public UnopExprContext(ExprContext ctx) { copyFrom(ctx); }
	}
	public static class StructExprContext extends ExprContext {
		public New_structContext new_struct() {
			return getRuleContext(New_structContext.class,0);
		}
		public StructExprContext(ExprContext ctx) { copyFrom(ctx); }
	}
	public static class OrExprContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode OR() { return getToken(WACCParser.OR, 0); }
		public OrExprContext(ExprContext ctx) { copyFrom(ctx); }
	}
	public static class CharExprContext extends ExprContext {
		public TerminalNode CHAR_LITER() { return getToken(WACCParser.CHAR_LITER, 0); }
		public CharExprContext(ExprContext ctx) { copyFrom(ctx); }
	}
	public static class ArrayExprContext extends ExprContext {
		public Array_elemContext array_elem() {
			return getRuleContext(Array_elemContext.class,0);
		}
		public ArrayExprContext(ExprContext ctx) { copyFrom(ctx); }
	}
	public static class ArithmeticExprContext extends ExprContext {
		public Token bop;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode MUL() { return getToken(WACCParser.MUL, 0); }
		public TerminalNode DIV() { return getToken(WACCParser.DIV, 0); }
		public TerminalNode MOD() { return getToken(WACCParser.MOD, 0); }
		public TerminalNode PLUS() { return getToken(WACCParser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(WACCParser.MINUS, 0); }
		public ArithmeticExprContext(ExprContext ctx) { copyFrom(ctx); }
	}
	public static class EmptyStructExprContext extends ExprContext {
		public TerminalNode EMPTY() { return getToken(WACCParser.EMPTY, 0); }
		public EmptyStructExprContext(ExprContext ctx) { copyFrom(ctx); }
	}
	public static class CmpExprContext extends ExprContext {
		public Token bop;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode GREATER() { return getToken(WACCParser.GREATER, 0); }
		public TerminalNode GREATER_EQUAL() { return getToken(WACCParser.GREATER_EQUAL, 0); }
		public TerminalNode LESS() { return getToken(WACCParser.LESS, 0); }
		public TerminalNode LESS_EQUAL() { return getToken(WACCParser.LESS_EQUAL, 0); }
		public CmpExprContext(ExprContext ctx) { copyFrom(ctx); }
	}
	public static class EqExprContext extends ExprContext {
		public Token bop;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode EQUAL() { return getToken(WACCParser.EQUAL, 0); }
		public TerminalNode UNEQUAL() { return getToken(WACCParser.UNEQUAL, 0); }
		public EqExprContext(ExprContext ctx) { copyFrom(ctx); }
	}
	public static class StrExprContext extends ExprContext {
		public TerminalNode STR_LITER() { return getToken(WACCParser.STR_LITER, 0); }
		public StrExprContext(ExprContext ctx) { copyFrom(ctx); }
	}
	public static class IntExprContext extends ExprContext {
		public TerminalNode INT_LITER() { return getToken(WACCParser.INT_LITER, 0); }
		public TerminalNode PLUS() { return getToken(WACCParser.PLUS, 0); }
		public IntExprContext(ExprContext ctx) { copyFrom(ctx); }
	}
	public static class ParenExprContext extends ExprContext {
		public TerminalNode OPEN_PARENTHESES() { return getToken(WACCParser.OPEN_PARENTHESES, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode CLOSE_PARENTHESES() { return getToken(WACCParser.CLOSE_PARENTHESES, 0); }
		public ParenExprContext(ExprContext ctx) { copyFrom(ctx); }
	}
	public static class OctalExprContext extends ExprContext {
		public TerminalNode OCTAL_LITER() { return getToken(WACCParser.OCTAL_LITER, 0); }
		public OctalExprContext(ExprContext ctx) { copyFrom(ctx); }
	}

	public final ExprContext expr() throws RecognitionException {
		return expr(0);
	}

	private ExprContext expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExprContext _localctx = new ExprContext(_ctx, _parentState);
		ExprContext _prevctx = _localctx;
		int _startState = 58;
		enterRecursionRule(_localctx, 58, RULE_expr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(374);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,26,_ctx) ) {
			case 1:
				{
				_localctx = new IntExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(353);
				match(INT_LITER);
				}
				break;
			case 2:
				{
				_localctx = new IntExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(354);
				match(PLUS);
				setState(355);
				match(INT_LITER);
				}
				break;
			case 3:
				{
				_localctx = new BinaryExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(356);
				match(BINARY_LITER);
				}
				break;
			case 4:
				{
				_localctx = new OctalExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(357);
				match(OCTAL_LITER);
				}
				break;
			case 5:
				{
				_localctx = new HexExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(358);
				match(HEX_LITER);
				}
				break;
			case 6:
				{
				_localctx = new BoolExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(359);
				match(BOOL_LITER);
				}
				break;
			case 7:
				{
				_localctx = new CharExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(360);
				match(CHAR_LITER);
				}
				break;
			case 8:
				{
				_localctx = new StrExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(361);
				match(STR_LITER);
				}
				break;
			case 9:
				{
				_localctx = new PairExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(362);
				match(PAIR_LITER);
				}
				break;
			case 10:
				{
				_localctx = new IdExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(363);
				match(IDENT);
				}
				break;
			case 11:
				{
				_localctx = new ArrayExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(364);
				array_elem();
				}
				break;
			case 12:
				{
				_localctx = new StructElemExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(365);
				struct_elem();
				}
				break;
			case 13:
				{
				_localctx = new StructExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(366);
				new_struct();
				}
				break;
			case 14:
				{
				_localctx = new EmptyStructExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(367);
				match(EMPTY);
				}
				break;
			case 15:
				{
				_localctx = new UnopExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(368);
				((UnopExprContext)_localctx).uop = _input.LT(1);
				_la = _input.LA(1);
				if ( !(((((_la - 56)) & ~0x3f) == 0 && ((1L << (_la - 56)) & ((1L << (MINUS - 56)) | (1L << (NOT - 56)) | (1L << (LEN - 56)) | (1L << (ORD - 56)) | (1L << (CHR - 56)) | (1L << (BitWiseComplement - 56)))) != 0)) ) {
					((UnopExprContext)_localctx).uop = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(369);
				expr(10);
				}
				break;
			case 16:
				{
				_localctx = new ParenExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(370);
				match(OPEN_PARENTHESES);
				setState(371);
				expr(0);
				setState(372);
				match(CLOSE_PARENTHESES);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(402);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,28,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(400);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,27,_ctx) ) {
					case 1:
						{
						_localctx = new ArithmeticExprContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(376);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(377);
						((ArithmeticExprContext)_localctx).bop = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << MUL) | (1L << DIV) | (1L << MOD))) != 0)) ) {
							((ArithmeticExprContext)_localctx).bop = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(378);
						expr(10);
						}
						break;
					case 2:
						{
						_localctx = new ArithmeticExprContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(379);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(380);
						((ArithmeticExprContext)_localctx).bop = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==PLUS || _la==MINUS) ) {
							((ArithmeticExprContext)_localctx).bop = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(381);
						expr(9);
						}
						break;
					case 3:
						{
						_localctx = new CmpExprContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(382);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(383);
						((CmpExprContext)_localctx).bop = _input.LT(1);
						_la = _input.LA(1);
						if ( !(((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (GREATER - 64)) | (1L << (GREATER_EQUAL - 64)) | (1L << (LESS - 64)) | (1L << (LESS_EQUAL - 64)))) != 0)) ) {
							((CmpExprContext)_localctx).bop = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(384);
						expr(8);
						}
						break;
					case 4:
						{
						_localctx = new EqExprContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(385);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(386);
						((EqExprContext)_localctx).bop = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==EQUAL || _la==UNEQUAL) ) {
							((EqExprContext)_localctx).bop = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(387);
						expr(7);
						}
						break;
					case 5:
						{
						_localctx = new BitwiseAndExprContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(388);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(389);
						match(BitWiseAnd);
						setState(390);
						expr(6);
						}
						break;
					case 6:
						{
						_localctx = new BitwiseOrExprContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(391);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(392);
						match(BitWiseOr);
						setState(393);
						expr(5);
						}
						break;
					case 7:
						{
						_localctx = new AndExprContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(394);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(395);
						match(AND);
						setState(396);
						expr(4);
						}
						break;
					case 8:
						{
						_localctx = new OrExprContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(397);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(398);
						match(OR);
						setState(399);
						expr(3);
						}
						break;
					}
					} 
				}
				setState(404);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,28,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class Array_elemContext extends ParserRuleContext {
		public TerminalNode IDENT() { return getToken(WACCParser.IDENT, 0); }
		public List<TerminalNode> OPEN_SQUARE_BRACKET() { return getTokens(WACCParser.OPEN_SQUARE_BRACKET); }
		public TerminalNode OPEN_SQUARE_BRACKET(int i) {
			return getToken(WACCParser.OPEN_SQUARE_BRACKET, i);
		}
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public List<TerminalNode> CLOSE_SQUARE_BRACKET() { return getTokens(WACCParser.CLOSE_SQUARE_BRACKET); }
		public TerminalNode CLOSE_SQUARE_BRACKET(int i) {
			return getToken(WACCParser.CLOSE_SQUARE_BRACKET, i);
		}
		public Array_elemContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_array_elem; }
	}

	public final Array_elemContext array_elem() throws RecognitionException {
		Array_elemContext _localctx = new Array_elemContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_array_elem);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(405);
			match(IDENT);
			setState(410); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(406);
					match(OPEN_SQUARE_BRACKET);
					setState(407);
					expr(0);
					setState(408);
					match(CLOSE_SQUARE_BRACKET);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(412); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,29,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Array_literContext extends ParserRuleContext {
		public TerminalNode OPEN_SQUARE_BRACKET() { return getToken(WACCParser.OPEN_SQUARE_BRACKET, 0); }
		public TerminalNode CLOSE_SQUARE_BRACKET() { return getToken(WACCParser.CLOSE_SQUARE_BRACKET, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(WACCParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(WACCParser.COMMA, i);
		}
		public Array_literContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_array_liter; }
	}

	public final Array_literContext array_liter() throws RecognitionException {
		Array_literContext _localctx = new Array_literContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_array_liter);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(414);
			match(OPEN_SQUARE_BRACKET);
			setState(423);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOL_LITER) | (1L << CHAR_LITER) | (1L << STR_LITER) | (1L << PAIR_LITER) | (1L << NEW) | (1L << EMPTY) | (1L << OPEN_PARENTHESES) | (1L << PLUS) | (1L << MINUS) | (1L << NOT) | (1L << LEN) | (1L << ORD) | (1L << CHR))) != 0) || ((((_la - 74)) & ~0x3f) == 0 && ((1L << (_la - 74)) & ((1L << (BitWiseComplement - 74)) | (1L << (INT_LITER - 74)) | (1L << (BINARY_LITER - 74)) | (1L << (OCTAL_LITER - 74)) | (1L << (HEX_LITER - 74)) | (1L << (IDENT - 74)))) != 0)) {
				{
				setState(415);
				expr(0);
				setState(420);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(416);
					match(COMMA);
					setState(417);
					expr(0);
					}
					}
					setState(422);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(425);
			match(CLOSE_SQUARE_BRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 7:
			return stat_sempred((StatContext)_localctx, predIndex);
		case 9:
			return for_stat_sempred((For_statContext)_localctx, predIndex);
		case 25:
			return array_type_sempred((Array_typeContext)_localctx, predIndex);
		case 29:
			return expr_sempred((ExprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean stat_sempred(StatContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean for_stat_sempred(For_statContext _localctx, int predIndex) {
		switch (predIndex) {
		case 1:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean array_type_sempred(Array_typeContext _localctx, int predIndex) {
		switch (predIndex) {
		case 2:
			return precpred(_ctx, 4);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 3:
			return precpred(_ctx, 9);
		case 4:
			return precpred(_ctx, 8);
		case 5:
			return precpred(_ctx, 7);
		case 6:
			return precpred(_ctx, 6);
		case 7:
			return precpred(_ctx, 5);
		case 8:
			return precpred(_ctx, 4);
		case 9:
			return precpred(_ctx, 3);
		case 10:
			return precpred(_ctx, 2);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3R\u01ae\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\3\2\3\2\3\2\3\3\7\3G\n\3\f\3\16\3J\13\3\3\3\3\3\7\3N\n\3\f\3\16\3"+
		"Q\13\3\3\4\7\4T\n\4\f\4\16\4W\13\4\3\4\3\4\3\4\7\4\\\n\4\f\4\16\4_\13"+
		"\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\5\5j\n\5\3\5\3\5\3\6\3\6\3\6\3"+
		"\6\5\6r\n\6\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\7\7|\n\7\f\7\16\7\177\13\7"+
		"\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3"+
		"\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t"+
		"\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\7\t\u00ac\n\t\f\t\16\t\u00af\13\t\3\t"+
		"\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3"+
		"\t\5\t\u00c4\n\t\3\t\3\t\3\t\7\t\u00c9\n\t\f\t\16\t\u00cc\13\t\3\n\3\n"+
		"\3\n\3\n\5\n\u00d2\n\n\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\5"+
		"\13\u00dd\n\13\3\13\3\13\3\13\7\13\u00e2\n\13\f\13\16\13\u00e5\13\13\3"+
		"\f\3\f\3\r\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\20\3\20"+
		"\3\20\3\21\3\21\3\21\3\22\3\22\3\22\3\23\3\23\3\23\3\24\3\24\3\24\3\24"+
		"\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\5\24\u010f\n\24\3\24"+
		"\5\24\u0112\n\24\3\25\3\25\3\25\7\25\u0117\n\25\f\25\16\25\u011a\13\25"+
		"\3\26\3\26\3\26\6\26\u011f\n\26\r\26\16\26\u0120\3\27\3\27\3\27\3\27\5"+
		"\27\u0127\n\27\3\27\3\27\3\30\3\30\3\30\3\30\5\30\u012f\n\30\3\31\3\31"+
		"\3\31\3\31\5\31\u0135\n\31\3\32\3\32\3\32\3\32\5\32\u013b\n\32\3\33\3"+
		"\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\5\33\u014a"+
		"\n\33\3\33\3\33\3\33\7\33\u014f\n\33\f\33\16\33\u0152\13\33\3\34\3\34"+
		"\3\34\3\34\3\34\3\34\3\34\3\35\3\35\3\35\3\35\5\35\u015f\n\35\3\36\3\36"+
		"\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37"+
		"\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\5\37\u0179\n\37\3\37\3\37\3\37"+
		"\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37"+
		"\3\37\3\37\3\37\3\37\3\37\3\37\3\37\7\37\u0193\n\37\f\37\16\37\u0196\13"+
		"\37\3 \3 \3 \3 \3 \6 \u019d\n \r \16 \u019e\3!\3!\3!\3!\7!\u01a5\n!\f"+
		"!\16!\u01a8\13!\5!\u01aa\n!\3!\3!\3!\2\6\20\24\64<\"\2\4\6\b\n\f\16\20"+
		"\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:<>@\2\7\4\2:>LL\3\2?A\3\2"+
		"9:\3\2BE\3\2FG\2\u01e2\2B\3\2\2\2\4H\3\2\2\2\6U\3\2\2\2\bd\3\2\2\2\nm"+
		"\3\2\2\2\fx\3\2\2\2\16\u0080\3\2\2\2\20\u00c3\3\2\2\2\22\u00d1\3\2\2\2"+
		"\24\u00dc\3\2\2\2\26\u00e6\3\2\2\2\30\u00e8\3\2\2\2\32\u00ed\3\2\2\2\34"+
		"\u00f1\3\2\2\2\36\u00f4\3\2\2\2 \u00f7\3\2\2\2\"\u00fa\3\2\2\2$\u00fd"+
		"\3\2\2\2&\u0111\3\2\2\2(\u0113\3\2\2\2*\u011b\3\2\2\2,\u0122\3\2\2\2."+
		"\u012e\3\2\2\2\60\u0134\3\2\2\2\62\u013a\3\2\2\2\64\u0149\3\2\2\2\66\u0153"+
		"\3\2\2\28\u015e\3\2\2\2:\u0160\3\2\2\2<\u0178\3\2\2\2>\u0197\3\2\2\2@"+
		"\u01a0\3\2\2\2BC\7\62\2\2CD\7Q\2\2D\3\3\2\2\2EG\5\2\2\2FE\3\2\2\2GJ\3"+
		"\2\2\2HF\3\2\2\2HI\3\2\2\2IO\3\2\2\2JH\3\2\2\2KN\5\b\5\2LN\5\n\6\2MK\3"+
		"\2\2\2ML\3\2\2\2NQ\3\2\2\2OM\3\2\2\2OP\3\2\2\2P\5\3\2\2\2QO\3\2\2\2RT"+
		"\5\2\2\2SR\3\2\2\2TW\3\2\2\2US\3\2\2\2UV\3\2\2\2VX\3\2\2\2WU\3\2\2\2X"+
		"]\7\27\2\2Y\\\5\b\5\2Z\\\5\n\6\2[Y\3\2\2\2[Z\3\2\2\2\\_\3\2\2\2][\3\2"+
		"\2\2]^\3\2\2\2^`\3\2\2\2_]\3\2\2\2`a\5\20\t\2ab\7\30\2\2bc\7\2\2\3c\7"+
		"\3\2\2\2de\7\23\2\2ef\7R\2\2fg\7\31\2\2gi\7\67\2\2hj\5\f\7\2ih\3\2\2\2"+
		"ij\3\2\2\2jk\3\2\2\2kl\78\2\2l\t\3\2\2\2mn\5\60\31\2no\7R\2\2oq\7\63\2"+
		"\2pr\5\f\7\2qp\3\2\2\2qr\3\2\2\2rs\3\2\2\2st\7\64\2\2tu\7\31\2\2uv\5\20"+
		"\t\2vw\7\30\2\2w\13\3\2\2\2x}\5\16\b\2yz\7\61\2\2z|\5\16\b\2{y\3\2\2\2"+
		"|\177\3\2\2\2}{\3\2\2\2}~\3\2\2\2~\r\3\2\2\2\177}\3\2\2\2\u0080\u0081"+
		"\5\60\31\2\u0081\u0082\7R\2\2\u0082\17\3\2\2\2\u0083\u0084\b\t\1\2\u0084"+
		"\u00c4\5\26\f\2\u0085\u00c4\5\30\r\2\u0086\u00c4\5\32\16\2\u0087\u00c4"+
		"\5\34\17\2\u0088\u00c4\5\36\20\2\u0089\u008a\7\36\2\2\u008a\u00c4\5<\37"+
		"\2\u008b\u00c4\5 \21\2\u008c\u00c4\5\"\22\2\u008d\u00c4\5$\23\2\u008e"+
		"\u00c4\7+\2\2\u008f\u00c4\7,\2\2\u0090\u0091\7\"\2\2\u0091\u0092\5<\37"+
		"\2\u0092\u0093\7$\2\2\u0093\u0094\5\20\t\2\u0094\u0095\7#\2\2\u0095\u0096"+
		"\5\20\t\2\u0096\u0097\7%\2\2\u0097\u00c4\3\2\2\2\u0098\u0099\7*\2\2\u0099"+
		"\u009a\7\63\2\2\u009a\u009b\5\24\13\2\u009b\u009c\7\60\2\2\u009c\u009d"+
		"\5<\37\2\u009d\u009e\7\60\2\2\u009e\u009f\5\24\13\2\u009f\u00a0\7\64\2"+
		"\2\u00a0\u00a1\7\'\2\2\u00a1\u00a2\5\20\t\2\u00a2\u00a3\7(\2\2\u00a3\u00c4"+
		"\3\2\2\2\u00a4\u00a5\7-\2\2\u00a5\u00a6\5<\37\2\u00a6\u00ad\7\'\2\2\u00a7"+
		"\u00a8\7)\2\2\u00a8\u00a9\5<\37\2\u00a9\u00aa\5\20\t\2\u00aa\u00ac\3\2"+
		"\2\2\u00ab\u00a7\3\2\2\2\u00ac\u00af\3\2\2\2\u00ad\u00ab\3\2\2\2\u00ad"+
		"\u00ae\3\2\2\2\u00ae\u00b0\3\2\2\2\u00af\u00ad\3\2\2\2\u00b0\u00b1\7."+
		"\2\2\u00b1\u00b2\5\20\t\2\u00b2\u00b3\7(\2\2\u00b3\u00c4\3\2\2\2\u00b4"+
		"\u00b5\7\'\2\2\u00b5\u00b6\5\20\t\2\u00b6\u00b7\7&\2\2\u00b7\u00b8\5<"+
		"\37\2\u00b8\u00c4\3\2\2\2\u00b9\u00ba\7&\2\2\u00ba\u00bb\5<\37\2\u00bb"+
		"\u00bc\7\'\2\2\u00bc\u00bd\5\20\t\2\u00bd\u00be\7(\2\2\u00be\u00c4\3\2"+
		"\2\2\u00bf\u00c0\7\27\2\2\u00c0\u00c1\5\20\t\2\u00c1\u00c2\7\30\2\2\u00c2"+
		"\u00c4\3\2\2\2\u00c3\u0083\3\2\2\2\u00c3\u0085\3\2\2\2\u00c3\u0086\3\2"+
		"\2\2\u00c3\u0087\3\2\2\2\u00c3\u0088\3\2\2\2\u00c3\u0089\3\2\2\2\u00c3"+
		"\u008b\3\2\2\2\u00c3\u008c\3\2\2\2\u00c3\u008d\3\2\2\2\u00c3\u008e\3\2"+
		"\2\2\u00c3\u008f\3\2\2\2\u00c3\u0090\3\2\2\2\u00c3\u0098\3\2\2\2\u00c3"+
		"\u00a4\3\2\2\2\u00c3\u00b4\3\2\2\2\u00c3\u00b9\3\2\2\2\u00c3\u00bf\3\2"+
		"\2\2\u00c4\u00ca\3\2\2\2\u00c5\u00c6\f\3\2\2\u00c6\u00c7\7\60\2\2\u00c7"+
		"\u00c9\5\20\t\4\u00c8\u00c5\3\2\2\2\u00c9\u00cc\3\2\2\2\u00ca\u00c8\3"+
		"\2\2\2\u00ca\u00cb\3\2\2\2\u00cb\21\3\2\2\2\u00cc\u00ca\3\2\2\2\u00cd"+
		"\u00d2\7R\2\2\u00ce\u00d2\5> \2\u00cf\u00d2\5.\30\2\u00d0\u00d2\5*\26"+
		"\2\u00d1\u00cd\3\2\2\2\u00d1\u00ce\3\2\2\2\u00d1\u00cf\3\2\2\2\u00d1\u00d0"+
		"\3\2\2\2\u00d2\23\3\2\2\2\u00d3\u00d4\b\13\1\2\u00d4\u00dd\5\26\f\2\u00d5"+
		"\u00dd\5\30\r\2\u00d6\u00dd\5\32\16\2\u00d7\u00dd\5\34\17\2\u00d8\u00dd"+
		"\5\36\20\2\u00d9\u00dd\5 \21\2\u00da\u00dd\5\"\22\2\u00db\u00dd\5$\23"+
		"\2\u00dc\u00d3\3\2\2\2\u00dc\u00d5\3\2\2\2\u00dc\u00d6\3\2\2\2\u00dc\u00d7"+
		"\3\2\2\2\u00dc\u00d8\3\2\2\2\u00dc\u00d9\3\2\2\2\u00dc\u00da\3\2\2\2\u00dc"+
		"\u00db\3\2\2\2\u00dd\u00e3\3\2\2\2\u00de\u00df\f\3\2\2\u00df\u00e0\7\61"+
		"\2\2\u00e0\u00e2\5\24\13\4\u00e1\u00de\3\2\2\2\u00e2\u00e5\3\2\2\2\u00e3"+
		"\u00e1\3\2\2\2\u00e3\u00e4\3\2\2\2\u00e4\25\3\2\2\2\u00e5\u00e3\3\2\2"+
		"\2\u00e6\u00e7\7\32\2\2\u00e7\27\3\2\2\2\u00e8\u00e9\5\60\31\2\u00e9\u00ea"+
		"\7R\2\2\u00ea\u00eb\7\33\2\2\u00eb\u00ec\5&\24\2\u00ec\31\3\2\2\2\u00ed"+
		"\u00ee\5\22\n\2\u00ee\u00ef\7\33\2\2\u00ef\u00f0\5&\24\2\u00f0\33\3\2"+
		"\2\2\u00f1\u00f2\7\34\2\2\u00f2\u00f3\5\22\n\2\u00f3\35\3\2\2\2\u00f4"+
		"\u00f5\7\35\2\2\u00f5\u00f6\5<\37\2\u00f6\37\3\2\2\2\u00f7\u00f8\7\37"+
		"\2\2\u00f8\u00f9\5<\37\2\u00f9!\3\2\2\2\u00fa\u00fb\7 \2\2\u00fb\u00fc"+
		"\5<\37\2\u00fc#\3\2\2\2\u00fd\u00fe\7!\2\2\u00fe\u00ff\5<\37\2\u00ff%"+
		"\3\2\2\2\u0100\u0112\5<\37\2\u0101\u0112\5@!\2\u0102\u0103\7\22\2\2\u0103"+
		"\u0104\7\63\2\2\u0104\u0105\5<\37\2\u0105\u0106\7\61\2\2\u0106\u0107\5"+
		"<\37\2\u0107\u0108\7\64\2\2\u0108\u0112\3\2\2\2\u0109\u0112\5.\30\2\u010a"+
		"\u010b\7/\2\2\u010b\u010c\7R\2\2\u010c\u010e\7\63\2\2\u010d\u010f\5(\25"+
		"\2\u010e\u010d\3\2\2\2\u010e\u010f\3\2\2\2\u010f\u0110\3\2\2\2\u0110\u0112"+
		"\7\64\2\2\u0111\u0100\3\2\2\2\u0111\u0101\3\2\2\2\u0111\u0102\3\2\2\2"+
		"\u0111\u0109\3\2\2\2\u0111\u010a\3\2\2\2\u0112\'\3\2\2\2\u0113\u0118\5"+
		"<\37\2\u0114\u0115\7\61\2\2\u0115\u0117\5<\37\2\u0116\u0114\3\2\2\2\u0117"+
		"\u011a\3\2\2\2\u0118\u0116\3\2\2\2\u0118\u0119\3\2\2\2\u0119)\3\2\2\2"+
		"\u011a\u0118\3\2\2\2\u011b\u011e\7R\2\2\u011c\u011d\7\25\2\2\u011d\u011f"+
		"\7R\2\2\u011e\u011c\3\2\2\2\u011f\u0120\3\2\2\2\u0120\u011e\3\2\2\2\u0120"+
		"\u0121\3\2\2\2\u0121+\3\2\2\2\u0122\u0123\7\24\2\2\u0123\u0124\7R\2\2"+
		"\u0124\u0126\7\67\2\2\u0125\u0127\5(\25\2\u0126\u0125\3\2\2\2\u0126\u0127"+
		"\3\2\2\2\u0127\u0128\3\2\2\2\u0128\u0129\78\2\2\u0129-\3\2\2\2\u012a\u012b"+
		"\7\16\2\2\u012b\u012f\5<\37\2\u012c\u012d\7\17\2\2\u012d\u012f\5<\37\2"+
		"\u012e\u012a\3\2\2\2\u012e\u012c\3\2\2\2\u012f/\3\2\2\2\u0130\u0135\5"+
		"\62\32\2\u0131\u0135\5\64\33\2\u0132\u0135\5\66\34\2\u0133\u0135\5:\36"+
		"\2\u0134\u0130\3\2\2\2\u0134\u0131\3\2\2\2\u0134\u0132\3\2\2\2\u0134\u0133"+
		"\3\2\2\2\u0135\61\3\2\2\2\u0136\u013b\7\7\2\2\u0137\u013b\7\b\2\2\u0138"+
		"\u013b\7\t\2\2\u0139\u013b\7\n\2\2\u013a\u0136\3\2\2\2\u013a\u0137\3\2"+
		"\2\2\u013a\u0138\3\2\2\2\u013a\u0139\3\2\2\2\u013b\63\3\2\2\2\u013c\u013d"+
		"\b\33\1\2\u013d\u013e\5\62\32\2\u013e\u013f\7\65\2\2\u013f\u0140\7\66"+
		"\2\2\u0140\u014a\3\2\2\2\u0141\u0142\5\66\34\2\u0142\u0143\7\65\2\2\u0143"+
		"\u0144\7\66\2\2\u0144\u014a\3\2\2\2\u0145\u0146\5:\36\2\u0146\u0147\7"+
		"\65\2\2\u0147\u0148\7\66\2\2\u0148\u014a\3\2\2\2\u0149\u013c\3\2\2\2\u0149"+
		"\u0141\3\2\2\2\u0149\u0145\3\2\2\2\u014a\u0150\3\2\2\2\u014b\u014c\f\6"+
		"\2\2\u014c\u014d\7\65\2\2\u014d\u014f\7\66\2\2\u014e\u014b\3\2\2\2\u014f"+
		"\u0152\3\2\2\2\u0150\u014e\3\2\2\2\u0150\u0151\3\2\2\2\u0151\65\3\2\2"+
		"\2\u0152\u0150\3\2\2\2\u0153\u0154\7\21\2\2\u0154\u0155\7\63\2\2\u0155"+
		"\u0156\58\35\2\u0156\u0157\7\61\2\2\u0157\u0158\58\35\2\u0158\u0159\7"+
		"\64\2\2\u0159\67\3\2\2\2\u015a\u015f\5\62\32\2\u015b\u015f\5\64\33\2\u015c"+
		"\u015f\5:\36\2\u015d\u015f\7\21\2\2\u015e\u015a\3\2\2\2\u015e\u015b\3"+
		"\2\2\2\u015e\u015c\3\2\2\2\u015e\u015d\3\2\2\2\u015f9\3\2\2\2\u0160\u0161"+
		"\7R\2\2\u0161;\3\2\2\2\u0162\u0163\b\37\1\2\u0163\u0179\7M\2\2\u0164\u0165"+
		"\79\2\2\u0165\u0179\7M\2\2\u0166\u0179\7N\2\2\u0167\u0179\7O\2\2\u0168"+
		"\u0179\7P\2\2\u0169\u0179\7\13\2\2\u016a\u0179\7\f\2\2\u016b\u0179\7\r"+
		"\2\2\u016c\u0179\7\20\2\2\u016d\u0179\7R\2\2\u016e\u0179\5> \2\u016f\u0179"+
		"\5*\26\2\u0170\u0179\5,\27\2\u0171\u0179\7\26\2\2\u0172\u0173\t\2\2\2"+
		"\u0173\u0179\5<\37\f\u0174\u0175\7\63\2\2\u0175\u0176\5<\37\2\u0176\u0177"+
		"\7\64\2\2\u0177\u0179\3\2\2\2\u0178\u0162\3\2\2\2\u0178\u0164\3\2\2\2"+
		"\u0178\u0166\3\2\2\2\u0178\u0167\3\2\2\2\u0178\u0168\3\2\2\2\u0178\u0169"+
		"\3\2\2\2\u0178\u016a\3\2\2\2\u0178\u016b\3\2\2\2\u0178\u016c\3\2\2\2\u0178"+
		"\u016d\3\2\2\2\u0178\u016e\3\2\2\2\u0178\u016f\3\2\2\2\u0178\u0170\3\2"+
		"\2\2\u0178\u0171\3\2\2\2\u0178\u0172\3\2\2\2\u0178\u0174\3\2\2\2\u0179"+
		"\u0194\3\2\2\2\u017a\u017b\f\13\2\2\u017b\u017c\t\3\2\2\u017c\u0193\5"+
		"<\37\f\u017d\u017e\f\n\2\2\u017e\u017f\t\4\2\2\u017f\u0193\5<\37\13\u0180"+
		"\u0181\f\t\2\2\u0181\u0182\t\5\2\2\u0182\u0193\5<\37\n\u0183\u0184\f\b"+
		"\2\2\u0184\u0185\t\6\2\2\u0185\u0193\5<\37\t\u0186\u0187\f\7\2\2\u0187"+
		"\u0188\7J\2\2\u0188\u0193\5<\37\b\u0189\u018a\f\6\2\2\u018a\u018b\7K\2"+
		"\2\u018b\u0193\5<\37\7\u018c\u018d\f\5\2\2\u018d\u018e\7H\2\2\u018e\u0193"+
		"\5<\37\6\u018f\u0190\f\4\2\2\u0190\u0191\7I\2\2\u0191\u0193\5<\37\5\u0192"+
		"\u017a\3\2\2\2\u0192\u017d\3\2\2\2\u0192\u0180\3\2\2\2\u0192\u0183\3\2"+
		"\2\2\u0192\u0186\3\2\2\2\u0192\u0189\3\2\2\2\u0192\u018c\3\2\2\2\u0192"+
		"\u018f\3\2\2\2\u0193\u0196\3\2\2\2\u0194\u0192\3\2\2\2\u0194\u0195\3\2"+
		"\2\2\u0195=\3\2\2\2\u0196\u0194\3\2\2\2\u0197\u019c\7R\2\2\u0198\u0199"+
		"\7\65\2\2\u0199\u019a\5<\37\2\u019a\u019b\7\66\2\2\u019b\u019d\3\2\2\2"+
		"\u019c\u0198\3\2\2\2\u019d\u019e\3\2\2\2\u019e\u019c\3\2\2\2\u019e\u019f"+
		"\3\2\2\2\u019f?\3\2\2\2\u01a0\u01a9\7\65\2\2\u01a1\u01a6\5<\37\2\u01a2"+
		"\u01a3\7\61\2\2\u01a3\u01a5\5<\37\2\u01a4\u01a2\3\2\2\2\u01a5\u01a8\3"+
		"\2\2\2\u01a6\u01a4\3\2\2\2\u01a6\u01a7\3\2\2\2\u01a7\u01aa\3\2\2\2\u01a8"+
		"\u01a6\3\2\2\2\u01a9\u01a1\3\2\2\2\u01a9\u01aa\3\2\2\2\u01aa\u01ab\3\2"+
		"\2\2\u01ab\u01ac\7\66\2\2\u01acA\3\2\2\2\"HMOU[]iq}\u00ad\u00c3\u00ca"+
		"\u00d1\u00dc\u00e3\u010e\u0111\u0118\u0120\u0126\u012e\u0134\u013a\u0149"+
		"\u0150\u015e\u0178\u0192\u0194\u019e\u01a6\u01a9";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}