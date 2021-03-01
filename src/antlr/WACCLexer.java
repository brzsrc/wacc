// Generated from ./WACCLexer.g4 by ANTLR 4.9.1
package antlr;
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
	static { RuntimeMetaData.checkVersion("4.9.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		WS=1, SHARP=2, EOL=3, COMMENT=4, INT=5, BOOL=6, CHAR=7, STRING=8, BOOL_LITER=9, 
		CHAR_LITER=10, STR_LITER=11, FST=12, SND=13, PAIR_LITER=14, PAIR=15, NEWPAIR=16, 
		BEGIN=17, END=18, IS=19, SKP=20, ASSIGN=21, READ=22, FREE=23, RETURN=24, 
		EXIT=25, PRINT=26, PRINTLN=27, IF=28, ELSE=29, THEN=30, FI=31, WHILE=32, 
		DO=33, DONE=34, CALL=35, SEMICOLON=36, COMMA=37, OPEN_PARENTHESES=38, 
		CLOSE_PARENTHESES=39, OPEN_SQUARE_BRACKET=40, CLOSE_SQUARE_BRACKET=41, 
		PLUS=42, MINUS=43, NOT=44, LEN=45, ORD=46, CHR=47, MUL=48, DIV=49, MOD=50, 
		GREATER=51, GREATER_EQUAL=52, LESS=53, LESS_EQUAL=54, EQUAL=55, UNEQUAL=56, 
		AND=57, OR=58, INT_LITER=59, IDENT=60;
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
			"BEGIN", "END", "IS", "SKP", "ASSIGN", "READ", "FREE", "RETURN", "EXIT", 
			"PRINT", "PRINTLN", "IF", "ELSE", "THEN", "FI", "WHILE", "DO", "DONE", 
			"CALL", "SEMICOLON", "COMMA", "OPEN_PARENTHESES", "CLOSE_PARENTHESES", 
			"OPEN_SQUARE_BRACKET", "CLOSE_SQUARE_BRACKET", "PLUS", "MINUS", "NOT", 
			"LEN", "ORD", "CHR", "MUL", "DIV", "MOD", "GREATER", "GREATER_EQUAL", 
			"LESS", "LESS_EQUAL", "EQUAL", "UNEQUAL", "AND", "OR", "INT_LITER", "DIGIT", 
			"IDENT", "CHARACTER", "ESCAPED_CHAR"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, "'#'", "'\n'", null, "'int'", "'bool'", "'char'", "'string'", 
			null, null, null, "'fst'", "'snd'", "'null'", "'pair'", "'newpair'", 
			"'begin'", "'end'", "'is'", "'skip'", "'='", "'read'", "'free'", "'return'", 
			"'exit'", "'print'", "'println'", "'if'", "'else'", "'then'", "'fi'", 
			"'while'", "'do'", "'done'", "'call'", "';'", "','", "'('", "')'", "'['", 
			"']'", "'+'", "'-'", "'!'", "'len'", "'ord'", "'chr'", "'*'", "'/'", 
			"'%'", "'>'", "'>='", "'<'", "'<='", "'=='", "'!='", "'&&'", "'||'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "WS", "SHARP", "EOL", "COMMENT", "INT", "BOOL", "CHAR", "STRING", 
			"BOOL_LITER", "CHAR_LITER", "STR_LITER", "FST", "SND", "PAIR_LITER", 
			"PAIR", "NEWPAIR", "BEGIN", "END", "IS", "SKP", "ASSIGN", "READ", "FREE", 
			"RETURN", "EXIT", "PRINT", "PRINTLN", "IF", "ELSE", "THEN", "FI", "WHILE", 
			"DO", "DONE", "CALL", "SEMICOLON", "COMMA", "OPEN_PARENTHESES", "CLOSE_PARENTHESES", 
			"OPEN_SQUARE_BRACKET", "CLOSE_SQUARE_BRACKET", "PLUS", "MINUS", "NOT", 
			"LEN", "ORD", "CHR", "MUL", "DIV", "MOD", "GREATER", "GREATER_EQUAL", 
			"LESS", "LESS_EQUAL", "EQUAL", "UNEQUAL", "AND", "OR", "INT_LITER", "IDENT"
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2>\u018b\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\3\2\6\2\u0083\n\2\r\2\16\2\u0084\3\2\3\2\3\3\3\3\3"+
		"\4\3\4\3\5\3\5\7\5\u008f\n\5\f\5\16\5\u0092\13\5\3\5\3\5\3\5\3\5\3\6\3"+
		"\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t"+
		"\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\5\n\u00b6\n\n\3\13\3\13\3"+
		"\13\3\13\3\f\3\f\7\f\u00be\n\f\f\f\16\f\u00c1\13\f\3\f\3\f\3\r\3\r\3\r"+
		"\3\r\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20"+
		"\3\20\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22"+
		"\3\22\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\25\3\25\3\25\3\25\3\25\3\26"+
		"\3\26\3\27\3\27\3\27\3\27\3\27\3\30\3\30\3\30\3\30\3\30\3\31\3\31\3\31"+
		"\3\31\3\31\3\31\3\31\3\32\3\32\3\32\3\32\3\32\3\33\3\33\3\33\3\33\3\33"+
		"\3\33\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\35\3\35\3\35\3\36\3\36"+
		"\3\36\3\36\3\36\3\37\3\37\3\37\3\37\3\37\3 \3 \3 \3!\3!\3!\3!\3!\3!\3"+
		"\"\3\"\3\"\3#\3#\3#\3#\3#\3$\3$\3$\3$\3$\3%\3%\3&\3&\3\'\3\'\3(\3(\3)"+
		"\3)\3*\3*\3+\3+\3,\3,\3-\3-\3.\3.\3.\3.\3/\3/\3/\3/\3\60\3\60\3\60\3\60"+
		"\3\61\3\61\3\62\3\62\3\63\3\63\3\64\3\64\3\65\3\65\3\65\3\66\3\66\3\67"+
		"\3\67\3\67\38\38\38\39\39\39\3:\3:\3:\3;\3;\3;\3<\6<\u0175\n<\r<\16<\u0176"+
		"\3=\3=\3>\5>\u017c\n>\3>\3>\7>\u0180\n>\f>\16>\u0183\13>\3?\3?\3?\5?\u0188"+
		"\n?\3@\3@\2\2A\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16"+
		"\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34"+
		"\67\359\36;\37= ?!A\"C#E$G%I&K\'M(O)Q*S+U,W-Y.[/]\60_\61a\62c\63e\64g"+
		"\65i\66k\67m8o9q:s;u<w=y\2{>}\2\177\2\3\2\b\5\2\13\f\17\17\"\"\3\2\f\f"+
		"\3\2\62;\5\2C\\aac|\5\2$$))^^\13\2$$))\62\62^^ddhhppttvv\2\u018f\2\3\3"+
		"\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2"+
		"\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3"+
		"\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2"+
		"%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61"+
		"\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2"+
		"\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I"+
		"\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2"+
		"\2\2\2W\3\2\2\2\2Y\3\2\2\2\2[\3\2\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2"+
		"\2c\3\2\2\2\2e\3\2\2\2\2g\3\2\2\2\2i\3\2\2\2\2k\3\2\2\2\2m\3\2\2\2\2o"+
		"\3\2\2\2\2q\3\2\2\2\2s\3\2\2\2\2u\3\2\2\2\2w\3\2\2\2\2{\3\2\2\2\3\u0082"+
		"\3\2\2\2\5\u0088\3\2\2\2\7\u008a\3\2\2\2\t\u008c\3\2\2\2\13\u0097\3\2"+
		"\2\2\r\u009b\3\2\2\2\17\u00a0\3\2\2\2\21\u00a5\3\2\2\2\23\u00b5\3\2\2"+
		"\2\25\u00b7\3\2\2\2\27\u00bb\3\2\2\2\31\u00c4\3\2\2\2\33\u00c8\3\2\2\2"+
		"\35\u00cc\3\2\2\2\37\u00d1\3\2\2\2!\u00d6\3\2\2\2#\u00de\3\2\2\2%\u00e4"+
		"\3\2\2\2\'\u00e8\3\2\2\2)\u00eb\3\2\2\2+\u00f0\3\2\2\2-\u00f2\3\2\2\2"+
		"/\u00f7\3\2\2\2\61\u00fc\3\2\2\2\63\u0103\3\2\2\2\65\u0108\3\2\2\2\67"+
		"\u010e\3\2\2\29\u0116\3\2\2\2;\u0119\3\2\2\2=\u011e\3\2\2\2?\u0123\3\2"+
		"\2\2A\u0126\3\2\2\2C\u012c\3\2\2\2E\u012f\3\2\2\2G\u0134\3\2\2\2I\u0139"+
		"\3\2\2\2K\u013b\3\2\2\2M\u013d\3\2\2\2O\u013f\3\2\2\2Q\u0141\3\2\2\2S"+
		"\u0143\3\2\2\2U\u0145\3\2\2\2W\u0147\3\2\2\2Y\u0149\3\2\2\2[\u014b\3\2"+
		"\2\2]\u014f\3\2\2\2_\u0153\3\2\2\2a\u0157\3\2\2\2c\u0159\3\2\2\2e\u015b"+
		"\3\2\2\2g\u015d\3\2\2\2i\u015f\3\2\2\2k\u0162\3\2\2\2m\u0164\3\2\2\2o"+
		"\u0167\3\2\2\2q\u016a\3\2\2\2s\u016d\3\2\2\2u\u0170\3\2\2\2w\u0174\3\2"+
		"\2\2y\u0178\3\2\2\2{\u017b\3\2\2\2}\u0187\3\2\2\2\177\u0189\3\2\2\2\u0081"+
		"\u0083\t\2\2\2\u0082\u0081\3\2\2\2\u0083\u0084\3\2\2\2\u0084\u0082\3\2"+
		"\2\2\u0084\u0085\3\2\2\2\u0085\u0086\3\2\2\2\u0086\u0087\b\2\2\2\u0087"+
		"\4\3\2\2\2\u0088\u0089\7%\2\2\u0089\6\3\2\2\2\u008a\u008b\7\f\2\2\u008b"+
		"\b\3\2\2\2\u008c\u0090\5\5\3\2\u008d\u008f\n\3\2\2\u008e\u008d\3\2\2\2"+
		"\u008f\u0092\3\2\2\2\u0090\u008e\3\2\2\2\u0090\u0091\3\2\2\2\u0091\u0093"+
		"\3\2\2\2\u0092\u0090\3\2\2\2\u0093\u0094\5\7\4\2\u0094\u0095\3\2\2\2\u0095"+
		"\u0096\b\5\2\2\u0096\n\3\2\2\2\u0097\u0098\7k\2\2\u0098\u0099\7p\2\2\u0099"+
		"\u009a\7v\2\2\u009a\f\3\2\2\2\u009b\u009c\7d\2\2\u009c\u009d\7q\2\2\u009d"+
		"\u009e\7q\2\2\u009e\u009f\7n\2\2\u009f\16\3\2\2\2\u00a0\u00a1\7e\2\2\u00a1"+
		"\u00a2\7j\2\2\u00a2\u00a3\7c\2\2\u00a3\u00a4\7t\2\2\u00a4\20\3\2\2\2\u00a5"+
		"\u00a6\7u\2\2\u00a6\u00a7\7v\2\2\u00a7\u00a8\7t\2\2\u00a8\u00a9\7k\2\2"+
		"\u00a9\u00aa\7p\2\2\u00aa\u00ab\7i\2\2\u00ab\22\3\2\2\2\u00ac\u00ad\7"+
		"v\2\2\u00ad\u00ae\7t\2\2\u00ae\u00af\7w\2\2\u00af\u00b6\7g\2\2\u00b0\u00b1"+
		"\7h\2\2\u00b1\u00b2\7c\2\2\u00b2\u00b3\7n\2\2\u00b3\u00b4\7u\2\2\u00b4"+
		"\u00b6\7g\2\2\u00b5\u00ac\3\2\2\2\u00b5\u00b0\3\2\2\2\u00b6\24\3\2\2\2"+
		"\u00b7\u00b8\7)\2\2\u00b8\u00b9\5}?\2\u00b9\u00ba\7)\2\2\u00ba\26\3\2"+
		"\2\2\u00bb\u00bf\7$\2\2\u00bc\u00be\5}?\2\u00bd\u00bc\3\2\2\2\u00be\u00c1"+
		"\3\2\2\2\u00bf\u00bd\3\2\2\2\u00bf\u00c0\3\2\2\2\u00c0\u00c2\3\2\2\2\u00c1"+
		"\u00bf\3\2\2\2\u00c2\u00c3\7$\2\2\u00c3\30\3\2\2\2\u00c4\u00c5\7h\2\2"+
		"\u00c5\u00c6\7u\2\2\u00c6\u00c7\7v\2\2\u00c7\32\3\2\2\2\u00c8\u00c9\7"+
		"u\2\2\u00c9\u00ca\7p\2\2\u00ca\u00cb\7f\2\2\u00cb\34\3\2\2\2\u00cc\u00cd"+
		"\7p\2\2\u00cd\u00ce\7w\2\2\u00ce\u00cf\7n\2\2\u00cf\u00d0\7n\2\2\u00d0"+
		"\36\3\2\2\2\u00d1\u00d2\7r\2\2\u00d2\u00d3\7c\2\2\u00d3\u00d4\7k\2\2\u00d4"+
		"\u00d5\7t\2\2\u00d5 \3\2\2\2\u00d6\u00d7\7p\2\2\u00d7\u00d8\7g\2\2\u00d8"+
		"\u00d9\7y\2\2\u00d9\u00da\7r\2\2\u00da\u00db\7c\2\2\u00db\u00dc\7k\2\2"+
		"\u00dc\u00dd\7t\2\2\u00dd\"\3\2\2\2\u00de\u00df\7d\2\2\u00df\u00e0\7g"+
		"\2\2\u00e0\u00e1\7i\2\2\u00e1\u00e2\7k\2\2\u00e2\u00e3\7p\2\2\u00e3$\3"+
		"\2\2\2\u00e4\u00e5\7g\2\2\u00e5\u00e6\7p\2\2\u00e6\u00e7\7f\2\2\u00e7"+
		"&\3\2\2\2\u00e8\u00e9\7k\2\2\u00e9\u00ea\7u\2\2\u00ea(\3\2\2\2\u00eb\u00ec"+
		"\7u\2\2\u00ec\u00ed\7m\2\2\u00ed\u00ee\7k\2\2\u00ee\u00ef\7r\2\2\u00ef"+
		"*\3\2\2\2\u00f0\u00f1\7?\2\2\u00f1,\3\2\2\2\u00f2\u00f3\7t\2\2\u00f3\u00f4"+
		"\7g\2\2\u00f4\u00f5\7c\2\2\u00f5\u00f6\7f\2\2\u00f6.\3\2\2\2\u00f7\u00f8"+
		"\7h\2\2\u00f8\u00f9\7t\2\2\u00f9\u00fa\7g\2\2\u00fa\u00fb\7g\2\2\u00fb"+
		"\60\3\2\2\2\u00fc\u00fd\7t\2\2\u00fd\u00fe\7g\2\2\u00fe\u00ff\7v\2\2\u00ff"+
		"\u0100\7w\2\2\u0100\u0101\7t\2\2\u0101\u0102\7p\2\2\u0102\62\3\2\2\2\u0103"+
		"\u0104\7g\2\2\u0104\u0105\7z\2\2\u0105\u0106\7k\2\2\u0106\u0107\7v\2\2"+
		"\u0107\64\3\2\2\2\u0108\u0109\7r\2\2\u0109\u010a\7t\2\2\u010a\u010b\7"+
		"k\2\2\u010b\u010c\7p\2\2\u010c\u010d\7v\2\2\u010d\66\3\2\2\2\u010e\u010f"+
		"\7r\2\2\u010f\u0110\7t\2\2\u0110\u0111\7k\2\2\u0111\u0112\7p\2\2\u0112"+
		"\u0113\7v\2\2\u0113\u0114\7n\2\2\u0114\u0115\7p\2\2\u01158\3\2\2\2\u0116"+
		"\u0117\7k\2\2\u0117\u0118\7h\2\2\u0118:\3\2\2\2\u0119\u011a\7g\2\2\u011a"+
		"\u011b\7n\2\2\u011b\u011c\7u\2\2\u011c\u011d\7g\2\2\u011d<\3\2\2\2\u011e"+
		"\u011f\7v\2\2\u011f\u0120\7j\2\2\u0120\u0121\7g\2\2\u0121\u0122\7p\2\2"+
		"\u0122>\3\2\2\2\u0123\u0124\7h\2\2\u0124\u0125\7k\2\2\u0125@\3\2\2\2\u0126"+
		"\u0127\7y\2\2\u0127\u0128\7j\2\2\u0128\u0129\7k\2\2\u0129\u012a\7n\2\2"+
		"\u012a\u012b\7g\2\2\u012bB\3\2\2\2\u012c\u012d\7f\2\2\u012d\u012e\7q\2"+
		"\2\u012eD\3\2\2\2\u012f\u0130\7f\2\2\u0130\u0131\7q\2\2\u0131\u0132\7"+
		"p\2\2\u0132\u0133\7g\2\2\u0133F\3\2\2\2\u0134\u0135\7e\2\2\u0135\u0136"+
		"\7c\2\2\u0136\u0137\7n\2\2\u0137\u0138\7n\2\2\u0138H\3\2\2\2\u0139\u013a"+
		"\7=\2\2\u013aJ\3\2\2\2\u013b\u013c\7.\2\2\u013cL\3\2\2\2\u013d\u013e\7"+
		"*\2\2\u013eN\3\2\2\2\u013f\u0140\7+\2\2\u0140P\3\2\2\2\u0141\u0142\7]"+
		"\2\2\u0142R\3\2\2\2\u0143\u0144\7_\2\2\u0144T\3\2\2\2\u0145\u0146\7-\2"+
		"\2\u0146V\3\2\2\2\u0147\u0148\7/\2\2\u0148X\3\2\2\2\u0149\u014a\7#\2\2"+
		"\u014aZ\3\2\2\2\u014b\u014c\7n\2\2\u014c\u014d\7g\2\2\u014d\u014e\7p\2"+
		"\2\u014e\\\3\2\2\2\u014f\u0150\7q\2\2\u0150\u0151\7t\2\2\u0151\u0152\7"+
		"f\2\2\u0152^\3\2\2\2\u0153\u0154\7e\2\2\u0154\u0155\7j\2\2\u0155\u0156"+
		"\7t\2\2\u0156`\3\2\2\2\u0157\u0158\7,\2\2\u0158b\3\2\2\2\u0159\u015a\7"+
		"\61\2\2\u015ad\3\2\2\2\u015b\u015c\7\'\2\2\u015cf\3\2\2\2\u015d\u015e"+
		"\7@\2\2\u015eh\3\2\2\2\u015f\u0160\7@\2\2\u0160\u0161\7?\2\2\u0161j\3"+
		"\2\2\2\u0162\u0163\7>\2\2\u0163l\3\2\2\2\u0164\u0165\7>\2\2\u0165\u0166"+
		"\7?\2\2\u0166n\3\2\2\2\u0167\u0168\7?\2\2\u0168\u0169\7?\2\2\u0169p\3"+
		"\2\2\2\u016a\u016b\7#\2\2\u016b\u016c\7?\2\2\u016cr\3\2\2\2\u016d\u016e"+
		"\7(\2\2\u016e\u016f\7(\2\2\u016ft\3\2\2\2\u0170\u0171\7~\2\2\u0171\u0172"+
		"\7~\2\2\u0172v\3\2\2\2\u0173\u0175\5y=\2\u0174\u0173\3\2\2\2\u0175\u0176"+
		"\3\2\2\2\u0176\u0174\3\2\2\2\u0176\u0177\3\2\2\2\u0177x\3\2\2\2\u0178"+
		"\u0179\t\4\2\2\u0179z\3\2\2\2\u017a\u017c\t\5\2\2\u017b\u017a\3\2\2\2"+
		"\u017c\u0181\3\2\2\2\u017d\u0180\t\5\2\2\u017e\u0180\5y=\2\u017f\u017d"+
		"\3\2\2\2\u017f\u017e\3\2\2\2\u0180\u0183\3\2\2\2\u0181\u017f\3\2\2\2\u0181"+
		"\u0182\3\2\2\2\u0182|\3\2\2\2\u0183\u0181\3\2\2\2\u0184\u0188\n\6\2\2"+
		"\u0185\u0186\7^\2\2\u0186\u0188\5\177@\2\u0187\u0184\3\2\2\2\u0187\u0185"+
		"\3\2\2\2\u0188~\3\2\2\2\u0189\u018a\t\7\2\2\u018a\u0080\3\2\2\2\f\2\u0084"+
		"\u0090\u00b5\u00bf\u0176\u017b\u017f\u0181\u0187\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}