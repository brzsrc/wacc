// Generated from /Users/xushitong/wacc_46/antlr_config/WACCLexer.g4 by ANTLR 4.8
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class WACCLexer extends Lexer {
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
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"WS", "SHARP", "EOL", "COMMENT", "INT", "BOOL", "CHAR", "STRING", "BOOL_LITER", 
			"CHAR_LITER", "STR_LITER", "FST", "SND", "PAIR_LITER", "PAIR", "NEWPAIR", 
			"STRUCT", "NEW", "DOT", "EMPTY", "BEGIN", "END", "IS", "SKP", "ASSIGN", 
			"READ", "FREE", "RETURN", "EXIT", "PRINT", "PRINTLN", "IF", "ELSE", "THEN", 
			"FI", "WHILE", "DO", "DONE", "CASE", "FOR", "BREAK", "CONTINUE", "SWITCH", 
			"DEFAULT", "CALL", "SEMICOLON", "COMMA", "IMPORT", "OPEN_PARENTHESES", 
			"CLOSE_PARENTHESES", "OPEN_SQUARE_BRACKET", "CLOSE_SQUARE_BRACKET", "OPEN_CURLY_BRACKET", 
			"CLOSE_CURLY_BRACKET", "PLUS", "MINUS", "NOT", "LEN", "ORD", "CHR", "MUL", 
			"DIV", "MOD", "GREATER", "GREATER_EQUAL", "LESS", "LESS_EQUAL", "EQUAL", 
			"UNEQUAL", "AND", "OR", "BitWiseAnd", "BitWiseOr", "BitWiseComplement", 
			"INT_LITER", "BINARY_LITER", "OCTAL_LITER", "HEX_LITER", "HEX", "OCTAL", 
			"BINARY", "DIGIT", "FILE_NAME", "IDENT", "CHARACTER", "ESCAPED_CHAR"
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


	public WACCLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "WACCLexer.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2R\u023b\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4I"+
		"\tI\4J\tJ\4K\tK\4L\tL\4M\tM\4N\tN\4O\tO\4P\tP\4Q\tQ\4R\tR\4S\tS\4T\tT"+
		"\4U\tU\4V\tV\4W\tW\3\2\6\2\u00b1\n\2\r\2\16\2\u00b2\3\2\3\2\3\3\3\3\3"+
		"\4\3\4\3\5\3\5\7\5\u00bd\n\5\f\5\16\5\u00c0\13\5\3\5\3\5\3\5\3\5\3\6\3"+
		"\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t"+
		"\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\5\n\u00e4\n\n\3\13\3\13\3"+
		"\13\3\13\3\f\3\f\7\f\u00ec\n\f\f\f\16\f\u00ef\13\f\3\f\3\f\3\r\3\r\3\r"+
		"\3\r\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20"+
		"\3\20\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22"+
		"\3\22\3\22\3\23\3\23\3\23\3\23\3\24\3\24\3\25\3\25\3\25\3\25\3\25\3\25"+
		"\3\26\3\26\3\26\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3\30\3\30\3\30\3\31"+
		"\3\31\3\31\3\31\3\31\3\32\3\32\3\33\3\33\3\33\3\33\3\33\3\34\3\34\3\34"+
		"\3\34\3\34\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\36\3\36\3\36\3\36\3\36"+
		"\3\37\3\37\3\37\3\37\3\37\3\37\3 \3 \3 \3 \3 \3 \3 \3 \3!\3!\3!\3\"\3"+
		"\"\3\"\3\"\3\"\3#\3#\3#\3#\3#\3$\3$\3$\3%\3%\3%\3%\3%\3%\3&\3&\3&\3\'"+
		"\3\'\3\'\3\'\3\'\3(\3(\3(\3(\3(\3)\3)\3)\3)\3*\3*\3*\3*\3*\3*\3+\3+\3"+
		"+\3+\3+\3+\3+\3+\3+\3,\3,\3,\3,\3,\3,\3,\3-\3-\3-\3-\3-\3-\3-\3-\3.\3"+
		".\3.\3.\3.\3/\3/\3\60\3\60\3\61\3\61\3\61\3\61\3\61\3\61\3\61\3\62\3\62"+
		"\3\63\3\63\3\64\3\64\3\65\3\65\3\66\3\66\3\67\3\67\38\38\39\39\3:\3:\3"+
		";\3;\3;\3;\3<\3<\3<\3<\3=\3=\3=\3=\3>\3>\3?\3?\3@\3@\3A\3A\3B\3B\3B\3"+
		"C\3C\3D\3D\3D\3E\3E\3E\3F\3F\3F\3G\3G\3G\3H\3H\3H\3I\3I\3J\3J\3K\3K\3"+
		"L\6L\u01ee\nL\rL\16L\u01ef\3M\3M\3M\3M\6M\u01f6\nM\rM\16M\u01f7\3N\3N"+
		"\3N\3N\6N\u01fe\nN\rN\16N\u01ff\3O\3O\3O\3O\6O\u0206\nO\rO\16O\u0207\3"+
		"P\5P\u020b\nP\3Q\3Q\3R\3R\3S\3S\3T\6T\u0214\nT\rT\16T\u0215\3T\3T\7T\u021a"+
		"\nT\fT\16T\u021d\13T\3T\6T\u0220\nT\rT\16T\u0221\3T\3T\3T\3T\3T\3T\3T"+
		"\3U\5U\u022c\nU\3U\3U\7U\u0230\nU\fU\16U\u0233\13U\3V\3V\3V\5V\u0238\n"+
		"V\3W\3W\2\2X\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16"+
		"\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34"+
		"\67\359\36;\37= ?!A\"C#E$G%I&K\'M(O)Q*S+U,W-Y.[/]\60_\61a\62c\63e\64g"+
		"\65i\66k\67m8o9q:s;u<w=y>{?}@\177A\u0081B\u0083C\u0085D\u0087E\u0089F"+
		"\u008bG\u008dH\u008fI\u0091J\u0093K\u0095L\u0097M\u0099N\u009bO\u009d"+
		"P\u009f\2\u00a1\2\u00a3\2\u00a5\2\u00a7Q\u00a9R\u00ab\2\u00ad\2\3\2\13"+
		"\5\2\13\f\17\17\"\"\3\2\f\f\4\2\62;CH\3\2\629\3\2\62\63\3\2\62;\5\2C\\"+
		"aac|\5\2$$))^^\13\2$$))\62\62^^ddhhppttvv\2\u0242\2\3\3\2\2\2\2\5\3\2"+
		"\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21"+
		"\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2"+
		"\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3"+
		"\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3"+
		"\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3"+
		"\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2"+
		"\2\2M\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2"+
		"Y\3\2\2\2\2[\3\2\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2\2e\3"+
		"\2\2\2\2g\3\2\2\2\2i\3\2\2\2\2k\3\2\2\2\2m\3\2\2\2\2o\3\2\2\2\2q\3\2\2"+
		"\2\2s\3\2\2\2\2u\3\2\2\2\2w\3\2\2\2\2y\3\2\2\2\2{\3\2\2\2\2}\3\2\2\2\2"+
		"\177\3\2\2\2\2\u0081\3\2\2\2\2\u0083\3\2\2\2\2\u0085\3\2\2\2\2\u0087\3"+
		"\2\2\2\2\u0089\3\2\2\2\2\u008b\3\2\2\2\2\u008d\3\2\2\2\2\u008f\3\2\2\2"+
		"\2\u0091\3\2\2\2\2\u0093\3\2\2\2\2\u0095\3\2\2\2\2\u0097\3\2\2\2\2\u0099"+
		"\3\2\2\2\2\u009b\3\2\2\2\2\u009d\3\2\2\2\2\u00a7\3\2\2\2\2\u00a9\3\2\2"+
		"\2\3\u00b0\3\2\2\2\5\u00b6\3\2\2\2\7\u00b8\3\2\2\2\t\u00ba\3\2\2\2\13"+
		"\u00c5\3\2\2\2\r\u00c9\3\2\2\2\17\u00ce\3\2\2\2\21\u00d3\3\2\2\2\23\u00e3"+
		"\3\2\2\2\25\u00e5\3\2\2\2\27\u00e9\3\2\2\2\31\u00f2\3\2\2\2\33\u00f6\3"+
		"\2\2\2\35\u00fa\3\2\2\2\37\u00ff\3\2\2\2!\u0104\3\2\2\2#\u010c\3\2\2\2"+
		"%\u0113\3\2\2\2\'\u0117\3\2\2\2)\u0119\3\2\2\2+\u011f\3\2\2\2-\u0125\3"+
		"\2\2\2/\u0129\3\2\2\2\61\u012c\3\2\2\2\63\u0131\3\2\2\2\65\u0133\3\2\2"+
		"\2\67\u0138\3\2\2\29\u013d\3\2\2\2;\u0144\3\2\2\2=\u0149\3\2\2\2?\u014f"+
		"\3\2\2\2A\u0157\3\2\2\2C\u015a\3\2\2\2E\u015f\3\2\2\2G\u0164\3\2\2\2I"+
		"\u0167\3\2\2\2K\u016d\3\2\2\2M\u0170\3\2\2\2O\u0175\3\2\2\2Q\u017a\3\2"+
		"\2\2S\u017e\3\2\2\2U\u0184\3\2\2\2W\u018d\3\2\2\2Y\u0194\3\2\2\2[\u019c"+
		"\3\2\2\2]\u01a1\3\2\2\2_\u01a3\3\2\2\2a\u01a5\3\2\2\2c\u01ac\3\2\2\2e"+
		"\u01ae\3\2\2\2g\u01b0\3\2\2\2i\u01b2\3\2\2\2k\u01b4\3\2\2\2m\u01b6\3\2"+
		"\2\2o\u01b8\3\2\2\2q\u01ba\3\2\2\2s\u01bc\3\2\2\2u\u01be\3\2\2\2w\u01c2"+
		"\3\2\2\2y\u01c6\3\2\2\2{\u01ca\3\2\2\2}\u01cc\3\2\2\2\177\u01ce\3\2\2"+
		"\2\u0081\u01d0\3\2\2\2\u0083\u01d2\3\2\2\2\u0085\u01d5\3\2\2\2\u0087\u01d7"+
		"\3\2\2\2\u0089\u01da\3\2\2\2\u008b\u01dd\3\2\2\2\u008d\u01e0\3\2\2\2\u008f"+
		"\u01e3\3\2\2\2\u0091\u01e6\3\2\2\2\u0093\u01e8\3\2\2\2\u0095\u01ea\3\2"+
		"\2\2\u0097\u01ed\3\2\2\2\u0099\u01f1\3\2\2\2\u009b\u01f9\3\2\2\2\u009d"+
		"\u0201\3\2\2\2\u009f\u020a\3\2\2\2\u00a1\u020c\3\2\2\2\u00a3\u020e\3\2"+
		"\2\2\u00a5\u0210\3\2\2\2\u00a7\u021b\3\2\2\2\u00a9\u022b\3\2\2\2\u00ab"+
		"\u0237\3\2\2\2\u00ad\u0239\3\2\2\2\u00af\u00b1\t\2\2\2\u00b0\u00af\3\2"+
		"\2\2\u00b1\u00b2\3\2\2\2\u00b2\u00b0\3\2\2\2\u00b2\u00b3\3\2\2\2\u00b3"+
		"\u00b4\3\2\2\2\u00b4\u00b5\b\2\2\2\u00b5\4\3\2\2\2\u00b6\u00b7\7%\2\2"+
		"\u00b7\6\3\2\2\2\u00b8\u00b9\7\f\2\2\u00b9\b\3\2\2\2\u00ba\u00be\5\5\3"+
		"\2\u00bb\u00bd\n\3\2\2\u00bc\u00bb\3\2\2\2\u00bd\u00c0\3\2\2\2\u00be\u00bc"+
		"\3\2\2\2\u00be\u00bf\3\2\2\2\u00bf\u00c1\3\2\2\2\u00c0\u00be\3\2\2\2\u00c1"+
		"\u00c2\5\7\4\2\u00c2\u00c3\3\2\2\2\u00c3\u00c4\b\5\2\2\u00c4\n\3\2\2\2"+
		"\u00c5\u00c6\7k\2\2\u00c6\u00c7\7p\2\2\u00c7\u00c8\7v\2\2\u00c8\f\3\2"+
		"\2\2\u00c9\u00ca\7d\2\2\u00ca\u00cb\7q\2\2\u00cb\u00cc\7q\2\2\u00cc\u00cd"+
		"\7n\2\2\u00cd\16\3\2\2\2\u00ce\u00cf\7e\2\2\u00cf\u00d0\7j\2\2\u00d0\u00d1"+
		"\7c\2\2\u00d1\u00d2\7t\2\2\u00d2\20\3\2\2\2\u00d3\u00d4\7u\2\2\u00d4\u00d5"+
		"\7v\2\2\u00d5\u00d6\7t\2\2\u00d6\u00d7\7k\2\2\u00d7\u00d8\7p\2\2\u00d8"+
		"\u00d9\7i\2\2\u00d9\22\3\2\2\2\u00da\u00db\7v\2\2\u00db\u00dc\7t\2\2\u00dc"+
		"\u00dd\7w\2\2\u00dd\u00e4\7g\2\2\u00de\u00df\7h\2\2\u00df\u00e0\7c\2\2"+
		"\u00e0\u00e1\7n\2\2\u00e1\u00e2\7u\2\2\u00e2\u00e4\7g\2\2\u00e3\u00da"+
		"\3\2\2\2\u00e3\u00de\3\2\2\2\u00e4\24\3\2\2\2\u00e5\u00e6\7)\2\2\u00e6"+
		"\u00e7\5\u00abV\2\u00e7\u00e8\7)\2\2\u00e8\26\3\2\2\2\u00e9\u00ed\7$\2"+
		"\2\u00ea\u00ec\5\u00abV\2\u00eb\u00ea\3\2\2\2\u00ec\u00ef\3\2\2\2\u00ed"+
		"\u00eb\3\2\2\2\u00ed\u00ee\3\2\2\2\u00ee\u00f0\3\2\2\2\u00ef\u00ed\3\2"+
		"\2\2\u00f0\u00f1\7$\2\2\u00f1\30\3\2\2\2\u00f2\u00f3\7h\2\2\u00f3\u00f4"+
		"\7u\2\2\u00f4\u00f5\7v\2\2\u00f5\32\3\2\2\2\u00f6\u00f7\7u\2\2\u00f7\u00f8"+
		"\7p\2\2\u00f8\u00f9\7f\2\2\u00f9\34\3\2\2\2\u00fa\u00fb\7p\2\2\u00fb\u00fc"+
		"\7w\2\2\u00fc\u00fd\7n\2\2\u00fd\u00fe\7n\2\2\u00fe\36\3\2\2\2\u00ff\u0100"+
		"\7r\2\2\u0100\u0101\7c\2\2\u0101\u0102\7k\2\2\u0102\u0103\7t\2\2\u0103"+
		" \3\2\2\2\u0104\u0105\7p\2\2\u0105\u0106\7g\2\2\u0106\u0107\7y\2\2\u0107"+
		"\u0108\7r\2\2\u0108\u0109\7c\2\2\u0109\u010a\7k\2\2\u010a\u010b\7t\2\2"+
		"\u010b\"\3\2\2\2\u010c\u010d\7u\2\2\u010d\u010e\7v\2\2\u010e\u010f\7t"+
		"\2\2\u010f\u0110\7w\2\2\u0110\u0111\7e\2\2\u0111\u0112\7v\2\2\u0112$\3"+
		"\2\2\2\u0113\u0114\7p\2\2\u0114\u0115\7g\2\2\u0115\u0116\7y\2\2\u0116"+
		"&\3\2\2\2\u0117\u0118\7\60\2\2\u0118(\3\2\2\2\u0119\u011a\7g\2\2\u011a"+
		"\u011b\7o\2\2\u011b\u011c\7r\2\2\u011c\u011d\7v\2\2\u011d\u011e\7{\2\2"+
		"\u011e*\3\2\2\2\u011f\u0120\7d\2\2\u0120\u0121\7g\2\2\u0121\u0122\7i\2"+
		"\2\u0122\u0123\7k\2\2\u0123\u0124\7p\2\2\u0124,\3\2\2\2\u0125\u0126\7"+
		"g\2\2\u0126\u0127\7p\2\2\u0127\u0128\7f\2\2\u0128.\3\2\2\2\u0129\u012a"+
		"\7k\2\2\u012a\u012b\7u\2\2\u012b\60\3\2\2\2\u012c\u012d\7u\2\2\u012d\u012e"+
		"\7m\2\2\u012e\u012f\7k\2\2\u012f\u0130\7r\2\2\u0130\62\3\2\2\2\u0131\u0132"+
		"\7?\2\2\u0132\64\3\2\2\2\u0133\u0134\7t\2\2\u0134\u0135\7g\2\2\u0135\u0136"+
		"\7c\2\2\u0136\u0137\7f\2\2\u0137\66\3\2\2\2\u0138\u0139\7h\2\2\u0139\u013a"+
		"\7t\2\2\u013a\u013b\7g\2\2\u013b\u013c\7g\2\2\u013c8\3\2\2\2\u013d\u013e"+
		"\7t\2\2\u013e\u013f\7g\2\2\u013f\u0140\7v\2\2\u0140\u0141\7w\2\2\u0141"+
		"\u0142\7t\2\2\u0142\u0143\7p\2\2\u0143:\3\2\2\2\u0144\u0145\7g\2\2\u0145"+
		"\u0146\7z\2\2\u0146\u0147\7k\2\2\u0147\u0148\7v\2\2\u0148<\3\2\2\2\u0149"+
		"\u014a\7r\2\2\u014a\u014b\7t\2\2\u014b\u014c\7k\2\2\u014c\u014d\7p\2\2"+
		"\u014d\u014e\7v\2\2\u014e>\3\2\2\2\u014f\u0150\7r\2\2\u0150\u0151\7t\2"+
		"\2\u0151\u0152\7k\2\2\u0152\u0153\7p\2\2\u0153\u0154\7v\2\2\u0154\u0155"+
		"\7n\2\2\u0155\u0156\7p\2\2\u0156@\3\2\2\2\u0157\u0158\7k\2\2\u0158\u0159"+
		"\7h\2\2\u0159B\3\2\2\2\u015a\u015b\7g\2\2\u015b\u015c\7n\2\2\u015c\u015d"+
		"\7u\2\2\u015d\u015e\7g\2\2\u015eD\3\2\2\2\u015f\u0160\7v\2\2\u0160\u0161"+
		"\7j\2\2\u0161\u0162\7g\2\2\u0162\u0163\7p\2\2\u0163F\3\2\2\2\u0164\u0165"+
		"\7h\2\2\u0165\u0166\7k\2\2\u0166H\3\2\2\2\u0167\u0168\7y\2\2\u0168\u0169"+
		"\7j\2\2\u0169\u016a\7k\2\2\u016a\u016b\7n\2\2\u016b\u016c\7g\2\2\u016c"+
		"J\3\2\2\2\u016d\u016e\7f\2\2\u016e\u016f\7q\2\2\u016fL\3\2\2\2\u0170\u0171"+
		"\7f\2\2\u0171\u0172\7q\2\2\u0172\u0173\7p\2\2\u0173\u0174\7g\2\2\u0174"+
		"N\3\2\2\2\u0175\u0176\7e\2\2\u0176\u0177\7c\2\2\u0177\u0178\7u\2\2\u0178"+
		"\u0179\7g\2\2\u0179P\3\2\2\2\u017a\u017b\7h\2\2\u017b\u017c\7q\2\2\u017c"+
		"\u017d\7t\2\2\u017dR\3\2\2\2\u017e\u017f\7d\2\2\u017f\u0180\7t\2\2\u0180"+
		"\u0181\7g\2\2\u0181\u0182\7c\2\2\u0182\u0183\7m\2\2\u0183T\3\2\2\2\u0184"+
		"\u0185\7e\2\2\u0185\u0186\7q\2\2\u0186\u0187\7p\2\2\u0187\u0188\7v\2\2"+
		"\u0188\u0189\7k\2\2\u0189\u018a\7p\2\2\u018a\u018b\7w\2\2\u018b\u018c"+
		"\7g\2\2\u018cV\3\2\2\2\u018d\u018e\7u\2\2\u018e\u018f\7y\2\2\u018f\u0190"+
		"\7k\2\2\u0190\u0191\7v\2\2\u0191\u0192\7e\2\2\u0192\u0193\7j\2\2\u0193"+
		"X\3\2\2\2\u0194\u0195\7f\2\2\u0195\u0196\7g\2\2\u0196\u0197\7h\2\2\u0197"+
		"\u0198\7c\2\2\u0198\u0199\7w\2\2\u0199\u019a\7n\2\2\u019a\u019b\7v\2\2"+
		"\u019bZ\3\2\2\2\u019c\u019d\7e\2\2\u019d\u019e\7c\2\2\u019e\u019f\7n\2"+
		"\2\u019f\u01a0\7n\2\2\u01a0\\\3\2\2\2\u01a1\u01a2\7=\2\2\u01a2^\3\2\2"+
		"\2\u01a3\u01a4\7.\2\2\u01a4`\3\2\2\2\u01a5\u01a6\7k\2\2\u01a6\u01a7\7"+
		"o\2\2\u01a7\u01a8\7r\2\2\u01a8\u01a9\7q\2\2\u01a9\u01aa\7t\2\2\u01aa\u01ab"+
		"\7v\2\2\u01abb\3\2\2\2\u01ac\u01ad\7*\2\2\u01add\3\2\2\2\u01ae\u01af\7"+
		"+\2\2\u01aff\3\2\2\2\u01b0\u01b1\7]\2\2\u01b1h\3\2\2\2\u01b2\u01b3\7_"+
		"\2\2\u01b3j\3\2\2\2\u01b4\u01b5\7}\2\2\u01b5l\3\2\2\2\u01b6\u01b7\7\177"+
		"\2\2\u01b7n\3\2\2\2\u01b8\u01b9\7-\2\2\u01b9p\3\2\2\2\u01ba\u01bb\7/\2"+
		"\2\u01bbr\3\2\2\2\u01bc\u01bd\7#\2\2\u01bdt\3\2\2\2\u01be\u01bf\7n\2\2"+
		"\u01bf\u01c0\7g\2\2\u01c0\u01c1\7p\2\2\u01c1v\3\2\2\2\u01c2\u01c3\7q\2"+
		"\2\u01c3\u01c4\7t\2\2\u01c4\u01c5\7f\2\2\u01c5x\3\2\2\2\u01c6\u01c7\7"+
		"e\2\2\u01c7\u01c8\7j\2\2\u01c8\u01c9\7t\2\2\u01c9z\3\2\2\2\u01ca\u01cb"+
		"\7,\2\2\u01cb|\3\2\2\2\u01cc\u01cd\7\61\2\2\u01cd~\3\2\2\2\u01ce\u01cf"+
		"\7\'\2\2\u01cf\u0080\3\2\2\2\u01d0\u01d1\7@\2\2\u01d1\u0082\3\2\2\2\u01d2"+
		"\u01d3\7@\2\2\u01d3\u01d4\7?\2\2\u01d4\u0084\3\2\2\2\u01d5\u01d6\7>\2"+
		"\2\u01d6\u0086\3\2\2\2\u01d7\u01d8\7>\2\2\u01d8\u01d9\7?\2\2\u01d9\u0088"+
		"\3\2\2\2\u01da\u01db\7?\2\2\u01db\u01dc\7?\2\2\u01dc\u008a\3\2\2\2\u01dd"+
		"\u01de\7#\2\2\u01de\u01df\7?\2\2\u01df\u008c\3\2\2\2\u01e0\u01e1\7(\2"+
		"\2\u01e1\u01e2\7(\2\2\u01e2\u008e\3\2\2\2\u01e3\u01e4\7~\2\2\u01e4\u01e5"+
		"\7~\2\2\u01e5\u0090\3\2\2\2\u01e6\u01e7\7(\2\2\u01e7\u0092\3\2\2\2\u01e8"+
		"\u01e9\7~\2\2\u01e9\u0094\3\2\2\2\u01ea\u01eb\7\u0080\2\2\u01eb\u0096"+
		"\3\2\2\2\u01ec\u01ee\5\u00a5S\2\u01ed\u01ec\3\2\2\2\u01ee\u01ef\3\2\2"+
		"\2\u01ef\u01ed\3\2\2\2\u01ef\u01f0\3\2\2\2\u01f0\u0098\3\2\2\2\u01f1\u01f2"+
		"\7\62\2\2\u01f2\u01f3\7d\2\2\u01f3\u01f5\3\2\2\2\u01f4\u01f6\5\u00a3R"+
		"\2\u01f5\u01f4\3\2\2\2\u01f6\u01f7\3\2\2\2\u01f7\u01f5\3\2\2\2\u01f7\u01f8"+
		"\3\2\2\2\u01f8\u009a\3\2\2\2\u01f9\u01fa\7\62\2\2\u01fa\u01fb\7q\2\2\u01fb"+
		"\u01fd\3\2\2\2\u01fc\u01fe\5\u00a1Q\2\u01fd\u01fc\3\2\2\2\u01fe\u01ff"+
		"\3\2\2\2\u01ff\u01fd\3\2\2\2\u01ff\u0200\3\2\2\2\u0200\u009c\3\2\2\2\u0201"+
		"\u0202\7\62\2\2\u0202\u0203\7z\2\2\u0203\u0205\3\2\2\2\u0204\u0206\5\u009f"+
		"P\2\u0205\u0204\3\2\2\2\u0206\u0207\3\2\2\2\u0207\u0205\3\2\2\2\u0207"+
		"\u0208\3\2\2\2\u0208\u009e\3\2\2\2\u0209\u020b\t\4\2\2\u020a\u0209\3\2"+
		"\2\2\u020b\u00a0\3\2\2\2\u020c\u020d\t\5\2\2\u020d\u00a2\3\2\2\2\u020e"+
		"\u020f\t\6\2\2\u020f\u00a4\3\2\2\2\u0210\u0211\t\7\2\2\u0211\u00a6\3\2"+
		"\2\2\u0212\u0214\5\u00abV\2\u0213\u0212\3\2\2\2\u0214\u0215\3\2\2\2\u0215"+
		"\u0213\3\2\2\2\u0215\u0216\3\2\2\2\u0216\u0217\3\2\2\2\u0217\u0218\7\61"+
		"\2\2\u0218\u021a\3\2\2\2\u0219\u0213\3\2\2\2\u021a\u021d\3\2\2\2\u021b"+
		"\u0219\3\2\2\2\u021b\u021c\3\2\2\2\u021c\u021f\3\2\2\2\u021d\u021b\3\2"+
		"\2\2\u021e\u0220\5\u00abV\2\u021f\u021e\3\2\2\2\u0220\u0221\3\2\2\2\u0221"+
		"\u021f\3\2\2\2\u0221\u0222\3\2\2\2\u0222\u0223\3\2\2\2\u0223\u0224\7\60"+
		"\2\2\u0224\u0225\7j\2\2\u0225\u0226\7y\2\2\u0226\u0227\7c\2\2\u0227\u0228"+
		"\7e\2\2\u0228\u0229\7e\2\2\u0229\u00a8\3\2\2\2\u022a\u022c\t\b\2\2\u022b"+
		"\u022a\3\2\2\2\u022c\u0231\3\2\2\2\u022d\u0230\t\b\2\2\u022e\u0230\5\u00a5"+
		"S\2\u022f\u022d\3\2\2\2\u022f\u022e\3\2\2\2\u0230\u0233\3\2\2\2\u0231"+
		"\u022f\3\2\2\2\u0231\u0232\3\2\2\2\u0232\u00aa\3\2\2\2\u0233\u0231\3\2"+
		"\2\2\u0234\u0238\n\t\2\2\u0235\u0236\7^\2\2\u0236\u0238\5\u00adW\2\u0237"+
		"\u0234\3\2\2\2\u0237\u0235\3\2\2\2\u0238\u00ac\3\2\2\2\u0239\u023a\t\n"+
		"\2\2\u023a\u00ae\3\2\2\2\23\2\u00b2\u00be\u00e3\u00ed\u01ef\u01f7\u01ff"+
		"\u0207\u020a\u0215\u021b\u0221\u022b\u022f\u0231\u0237\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}