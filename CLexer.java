// Generated from C.g4 by ANTLR 4.0
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class CLexer extends Lexer {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__65=1, T__64=2, T__63=3, T__62=4, T__61=5, T__60=6, T__59=7, T__58=8, 
		T__57=9, T__56=10, T__55=11, T__54=12, T__53=13, T__52=14, T__51=15, T__50=16, 
		T__49=17, T__48=18, T__47=19, T__46=20, T__45=21, T__44=22, T__43=23, 
		T__42=24, T__41=25, T__40=26, T__39=27, T__38=28, T__37=29, T__36=30, 
		T__35=31, T__34=32, T__33=33, T__32=34, T__31=35, T__30=36, T__29=37, 
		T__28=38, T__27=39, T__26=40, T__25=41, T__24=42, T__23=43, T__22=44, 
		T__21=45, T__20=46, T__19=47, T__18=48, T__17=49, T__16=50, T__15=51, 
		T__14=52, T__13=53, T__12=54, T__11=55, T__10=56, T__9=57, T__8=58, T__7=59, 
		T__6=60, T__5=61, T__4=62, T__3=63, T__2=64, T__1=65, T__0=66, SPACE=67, 
		MULTI_COMMENT=68, SINGLE_COMMENT=69, Hex_Digit=70, Identifier=71, Digit=72, 
		Typedef_name=73, Character_constant=74, Integer_constant=75, HEX=76, DEC=77, 
		OCT=78;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"'&'", "'['", "'*'", "'<'", "'--'", "'continue'", "'!='", "'<='", "'<<'", 
		"'}'", "'char'", "'%'", "'->'", "'*='", "'union'", "')'", "'t'", "'='", 
		"'\\'", "'|='", "'|'", "'!'", "'sizeof'", "'b'", "'<<='", "']'", "'-='", 
		"','", "'while'", "'-'", "'('", "'&='", "'if'", "'int'", "'f'", "'void'", 
		"'>>='", "'''", "'{'", "'...'", "'break'", "'+='", "'^='", "'n'", "'else'", 
		"'struct'", "'++'", "'>>'", "'^'", "'.'", "'+'", "'for'", "'typedef'", 
		"'return'", "'r'", "';'", "'&&'", "'||'", "'>'", "'%='", "'\"'", "'/='", 
		"'=='", "'/'", "'~'", "'>='", "SPACE", "MULTI_COMMENT", "SINGLE_COMMENT", 
		"Hex_Digit", "Identifier", "Digit", "Typedef_name", "Character_constant", 
		"Integer_constant", "HEX", "DEC", "OCT"
	};
	public static final String[] ruleNames = {
		"T__65", "T__64", "T__63", "T__62", "T__61", "T__60", "T__59", "T__58", 
		"T__57", "T__56", "T__55", "T__54", "T__53", "T__52", "T__51", "T__50", 
		"T__49", "T__48", "T__47", "T__46", "T__45", "T__44", "T__43", "T__42", 
		"T__41", "T__40", "T__39", "T__38", "T__37", "T__36", "T__35", "T__34", 
		"T__33", "T__32", "T__31", "T__30", "T__29", "T__28", "T__27", "T__26", 
		"T__25", "T__24", "T__23", "T__22", "T__21", "T__20", "T__19", "T__18", 
		"T__17", "T__16", "T__15", "T__14", "T__13", "T__12", "T__11", "T__10", 
		"T__9", "T__8", "T__7", "T__6", "T__5", "T__4", "T__3", "T__2", "T__1", 
		"T__0", "SPACE", "MULTI_COMMENT", "SINGLE_COMMENT", "Hex_Digit", "Identifier", 
		"Letter", "Digit", "Typedef_name", "Character_constant", "Integer_constant", 
		"HEX", "DEC", "OCT"
	};


	public CLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "C.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	@Override
	public void action(RuleContext _localctx, int ruleIndex, int actionIndex) {
		switch (ruleIndex) {
		case 66: SPACE_action((RuleContext)_localctx, actionIndex); break;

		case 67: MULTI_COMMENT_action((RuleContext)_localctx, actionIndex); break;

		case 68: SINGLE_COMMENT_action((RuleContext)_localctx, actionIndex); break;
		}
	}
	private void SINGLE_COMMENT_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 2: _channel = HIDDEN;  break;
		}
	}
	private void SPACE_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0: _channel = HIDDEN;  break;
		}
	}
	private void MULTI_COMMENT_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 1: _channel = HIDDEN;  break;
		}
	}

	public static final String _serializedATN =
		"\2\4P\u01c8\b\1\4\2\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t"+
		"\b\4\t\t\t\4\n\t\n\4\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20"+
		"\t\20\4\21\t\21\4\22\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27"+
		"\t\27\4\30\t\30\4\31\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36"+
		"\t\36\4\37\t\37\4 \t \4!\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4"+
		"(\t(\4)\t)\4*\t*\4+\t+\4,\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62"+
		"\t\62\4\63\t\63\4\64\t\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4"+
		":\t:\4;\t;\4<\t<\4=\t=\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\t"+
		"E\4F\tF\4G\tG\4H\tH\4I\tI\4J\tJ\4K\tK\4L\tL\4M\tM\4N\tN\4O\tO\4P\tP\3"+
		"\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\3\7"+
		"\3\7\3\7\3\b\3\b\3\b\3\t\3\t\3\t\3\n\3\n\3\n\3\13\3\13\3\f\3\f\3\f\3\f"+
		"\3\f\3\r\3\r\3\16\3\16\3\16\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3"+
		"\20\3\21\3\21\3\22\3\22\3\23\3\23\3\24\3\24\3\25\3\25\3\25\3\26\3\26\3"+
		"\27\3\27\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\31\3\31\3\32\3\32\3\32\3"+
		"\32\3\33\3\33\3\34\3\34\3\34\3\35\3\35\3\36\3\36\3\36\3\36\3\36\3\36\3"+
		"\37\3\37\3 \3 \3!\3!\3!\3\"\3\"\3\"\3#\3#\3#\3#\3$\3$\3%\3%\3%\3%\3%\3"+
		"&\3&\3&\3&\3\'\3\'\3(\3(\3)\3)\3)\3)\3*\3*\3*\3*\3*\3*\3+\3+\3+\3,\3,"+
		"\3,\3-\3-\3.\3.\3.\3.\3.\3/\3/\3/\3/\3/\3/\3/\3\60\3\60\3\60\3\61\3\61"+
		"\3\61\3\62\3\62\3\63\3\63\3\64\3\64\3\65\3\65\3\65\3\65\3\66\3\66\3\66"+
		"\3\66\3\66\3\66\3\66\3\66\3\67\3\67\3\67\3\67\3\67\3\67\3\67\38\38\39"+
		"\39\3:\3:\3:\3;\3;\3;\3<\3<\3=\3=\3=\3>\3>\3?\3?\3?\3@\3@\3@\3A\3A\3B"+
		"\3B\3C\3C\3C\3D\6D\u0176\nD\rD\16D\u0177\3D\3D\3E\3E\3E\3E\7E\u0180\n"+
		"E\fE\16E\u0183\13E\3E\3E\3E\3E\3E\3F\3F\3F\3F\7F\u018e\nF\fF\16F\u0191"+
		"\13F\3F\3F\3G\3G\5G\u0197\nG\3H\3H\3H\7H\u019c\nH\fH\16H\u019f\13H\3I"+
		"\3I\3J\3J\3K\3K\3L\3L\3L\3L\3L\3L\5L\u01ad\nL\3M\3M\3M\5M\u01b2\nM\3N"+
		"\3N\3N\6N\u01b7\nN\rN\16N\u01b8\3O\3O\7O\u01bd\nO\fO\16O\u01c0\13O\3P"+
		"\3P\7P\u01c4\nP\fP\16P\u01c7\13P\3\u0181Q\3\3\1\5\4\1\7\5\1\t\6\1\13\7"+
		"\1\r\b\1\17\t\1\21\n\1\23\13\1\25\f\1\27\r\1\31\16\1\33\17\1\35\20\1\37"+
		"\21\1!\22\1#\23\1%\24\1\'\25\1)\26\1+\27\1-\30\1/\31\1\61\32\1\63\33\1"+
		"\65\34\1\67\35\19\36\1;\37\1= \1?!\1A\"\1C#\1E$\1G%\1I&\1K\'\1M(\1O)\1"+
		"Q*\1S+\1U,\1W-\1Y.\1[/\1]\60\1_\61\1a\62\1c\63\1e\64\1g\65\1i\66\1k\67"+
		"\1m8\1o9\1q:\1s;\1u<\1w=\1y>\1{?\1}@\1\177A\1\u0081B\1\u0083C\1\u0085"+
		"D\1\u0087E\2\u0089F\3\u008bG\4\u008dH\1\u008fI\1\u0091\2\1\u0093J\1\u0095"+
		"K\1\u0097L\1\u0099M\1\u009bN\1\u009dO\1\u009fP\1\3\2\n\5\13\f\17\17\""+
		"\"\4\f\f\17\17\4CHch\6&&C\\aac|\3\62;\3^^\n$$))^^ddhhppttvv\4ZZzz\u01d2"+
		"\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2"+
		"\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2"+
		"\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2"+
		"\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2"+
		"\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3"+
		"\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2"+
		"\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2"+
		"U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2\2\2[\3\2\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3"+
		"\2\2\2\2c\3\2\2\2\2e\3\2\2\2\2g\3\2\2\2\2i\3\2\2\2\2k\3\2\2\2\2m\3\2\2"+
		"\2\2o\3\2\2\2\2q\3\2\2\2\2s\3\2\2\2\2u\3\2\2\2\2w\3\2\2\2\2y\3\2\2\2\2"+
		"{\3\2\2\2\2}\3\2\2\2\2\177\3\2\2\2\2\u0081\3\2\2\2\2\u0083\3\2\2\2\2\u0085"+
		"\3\2\2\2\2\u0087\3\2\2\2\2\u0089\3\2\2\2\2\u008b\3\2\2\2\2\u008d\3\2\2"+
		"\2\2\u008f\3\2\2\2\2\u0093\3\2\2\2\2\u0095\3\2\2\2\2\u0097\3\2\2\2\2\u0099"+
		"\3\2\2\2\2\u009b\3\2\2\2\2\u009d\3\2\2\2\2\u009f\3\2\2\2\3\u00a1\3\2\2"+
		"\2\5\u00a3\3\2\2\2\7\u00a5\3\2\2\2\t\u00a7\3\2\2\2\13\u00a9\3\2\2\2\r"+
		"\u00ac\3\2\2\2\17\u00b5\3\2\2\2\21\u00b8\3\2\2\2\23\u00bb\3\2\2\2\25\u00be"+
		"\3\2\2\2\27\u00c0\3\2\2\2\31\u00c5\3\2\2\2\33\u00c7\3\2\2\2\35\u00ca\3"+
		"\2\2\2\37\u00cd\3\2\2\2!\u00d3\3\2\2\2#\u00d5\3\2\2\2%\u00d7\3\2\2\2\'"+
		"\u00d9\3\2\2\2)\u00db\3\2\2\2+\u00de\3\2\2\2-\u00e0\3\2\2\2/\u00e2\3\2"+
		"\2\2\61\u00e9\3\2\2\2\63\u00eb\3\2\2\2\65\u00ef\3\2\2\2\67\u00f1\3\2\2"+
		"\29\u00f4\3\2\2\2;\u00f6\3\2\2\2=\u00fc\3\2\2\2?\u00fe\3\2\2\2A\u0100"+
		"\3\2\2\2C\u0103\3\2\2\2E\u0106\3\2\2\2G\u010a\3\2\2\2I\u010c\3\2\2\2K"+
		"\u0111\3\2\2\2M\u0115\3\2\2\2O\u0117\3\2\2\2Q\u0119\3\2\2\2S\u011d\3\2"+
		"\2\2U\u0123\3\2\2\2W\u0126\3\2\2\2Y\u0129\3\2\2\2[\u012b\3\2\2\2]\u0130"+
		"\3\2\2\2_\u0137\3\2\2\2a\u013a\3\2\2\2c\u013d\3\2\2\2e\u013f\3\2\2\2g"+
		"\u0141\3\2\2\2i\u0143\3\2\2\2k\u0147\3\2\2\2m\u014f\3\2\2\2o\u0156\3\2"+
		"\2\2q\u0158\3\2\2\2s\u015a\3\2\2\2u\u015d\3\2\2\2w\u0160\3\2\2\2y\u0162"+
		"\3\2\2\2{\u0165\3\2\2\2}\u0167\3\2\2\2\177\u016a\3\2\2\2\u0081\u016d\3"+
		"\2\2\2\u0083\u016f\3\2\2\2\u0085\u0171\3\2\2\2\u0087\u0175\3\2\2\2\u0089"+
		"\u017b\3\2\2\2\u008b\u0189\3\2\2\2\u008d\u0196\3\2\2\2\u008f\u0198\3\2"+
		"\2\2\u0091\u01a0\3\2\2\2\u0093\u01a2\3\2\2\2\u0095\u01a4\3\2\2\2\u0097"+
		"\u01ac\3\2\2\2\u0099\u01b1\3\2\2\2\u009b\u01b3\3\2\2\2\u009d\u01ba\3\2"+
		"\2\2\u009f\u01c1\3\2\2\2\u00a1\u00a2\7(\2\2\u00a2\4\3\2\2\2\u00a3\u00a4"+
		"\7]\2\2\u00a4\6\3\2\2\2\u00a5\u00a6\7,\2\2\u00a6\b\3\2\2\2\u00a7\u00a8"+
		"\7>\2\2\u00a8\n\3\2\2\2\u00a9\u00aa\7/\2\2\u00aa\u00ab\7/\2\2\u00ab\f"+
		"\3\2\2\2\u00ac\u00ad\7e\2\2\u00ad\u00ae\7q\2\2\u00ae\u00af\7p\2\2\u00af"+
		"\u00b0\7v\2\2\u00b0\u00b1\7k\2\2\u00b1\u00b2\7p\2\2\u00b2\u00b3\7w\2\2"+
		"\u00b3\u00b4\7g\2\2\u00b4\16\3\2\2\2\u00b5\u00b6\7#\2\2\u00b6\u00b7\7"+
		"?\2\2\u00b7\20\3\2\2\2\u00b8\u00b9\7>\2\2\u00b9\u00ba\7?\2\2\u00ba\22"+
		"\3\2\2\2\u00bb\u00bc\7>\2\2\u00bc\u00bd\7>\2\2\u00bd\24\3\2\2\2\u00be"+
		"\u00bf\7\177\2\2\u00bf\26\3\2\2\2\u00c0\u00c1\7e\2\2\u00c1\u00c2\7j\2"+
		"\2\u00c2\u00c3\7c\2\2\u00c3\u00c4\7t\2\2\u00c4\30\3\2\2\2\u00c5\u00c6"+
		"\7\'\2\2\u00c6\32\3\2\2\2\u00c7\u00c8\7/\2\2\u00c8\u00c9\7@\2\2\u00c9"+
		"\34\3\2\2\2\u00ca\u00cb\7,\2\2\u00cb\u00cc\7?\2\2\u00cc\36\3\2\2\2\u00cd"+
		"\u00ce\7w\2\2\u00ce\u00cf\7p\2\2\u00cf\u00d0\7k\2\2\u00d0\u00d1\7q\2\2"+
		"\u00d1\u00d2\7p\2\2\u00d2 \3\2\2\2\u00d3\u00d4\7+\2\2\u00d4\"\3\2\2\2"+
		"\u00d5\u00d6\7v\2\2\u00d6$\3\2\2\2\u00d7\u00d8\7?\2\2\u00d8&\3\2\2\2\u00d9"+
		"\u00da\7^\2\2\u00da(\3\2\2\2\u00db\u00dc\7~\2\2\u00dc\u00dd\7?\2\2\u00dd"+
		"*\3\2\2\2\u00de\u00df\7~\2\2\u00df,\3\2\2\2\u00e0\u00e1\7#\2\2\u00e1."+
		"\3\2\2\2\u00e2\u00e3\7u\2\2\u00e3\u00e4\7k\2\2\u00e4\u00e5\7|\2\2\u00e5"+
		"\u00e6\7g\2\2\u00e6\u00e7\7q\2\2\u00e7\u00e8\7h\2\2\u00e8\60\3\2\2\2\u00e9"+
		"\u00ea\7d\2\2\u00ea\62\3\2\2\2\u00eb\u00ec\7>\2\2\u00ec\u00ed\7>\2\2\u00ed"+
		"\u00ee\7?\2\2\u00ee\64\3\2\2\2\u00ef\u00f0\7_\2\2\u00f0\66\3\2\2\2\u00f1"+
		"\u00f2\7/\2\2\u00f2\u00f3\7?\2\2\u00f38\3\2\2\2\u00f4\u00f5\7.\2\2\u00f5"+
		":\3\2\2\2\u00f6\u00f7\7y\2\2\u00f7\u00f8\7j\2\2\u00f8\u00f9\7k\2\2\u00f9"+
		"\u00fa\7n\2\2\u00fa\u00fb\7g\2\2\u00fb<\3\2\2\2\u00fc\u00fd\7/\2\2\u00fd"+
		">\3\2\2\2\u00fe\u00ff\7*\2\2\u00ff@\3\2\2\2\u0100\u0101\7(\2\2\u0101\u0102"+
		"\7?\2\2\u0102B\3\2\2\2\u0103\u0104\7k\2\2\u0104\u0105\7h\2\2\u0105D\3"+
		"\2\2\2\u0106\u0107\7k\2\2\u0107\u0108\7p\2\2\u0108\u0109\7v\2\2\u0109"+
		"F\3\2\2\2\u010a\u010b\7h\2\2\u010bH\3\2\2\2\u010c\u010d\7x\2\2\u010d\u010e"+
		"\7q\2\2\u010e\u010f\7k\2\2\u010f\u0110\7f\2\2\u0110J\3\2\2\2\u0111\u0112"+
		"\7@\2\2\u0112\u0113\7@\2\2\u0113\u0114\7?\2\2\u0114L\3\2\2\2\u0115\u0116"+
		"\7)\2\2\u0116N\3\2\2\2\u0117\u0118\7}\2\2\u0118P\3\2\2\2\u0119\u011a\7"+
		"\60\2\2\u011a\u011b\7\60\2\2\u011b\u011c\7\60\2\2\u011cR\3\2\2\2\u011d"+
		"\u011e\7d\2\2\u011e\u011f\7t\2\2\u011f\u0120\7g\2\2\u0120\u0121\7c\2\2"+
		"\u0121\u0122\7m\2\2\u0122T\3\2\2\2\u0123\u0124\7-\2\2\u0124\u0125\7?\2"+
		"\2\u0125V\3\2\2\2\u0126\u0127\7`\2\2\u0127\u0128\7?\2\2\u0128X\3\2\2\2"+
		"\u0129\u012a\7p\2\2\u012aZ\3\2\2\2\u012b\u012c\7g\2\2\u012c\u012d\7n\2"+
		"\2\u012d\u012e\7u\2\2\u012e\u012f\7g\2\2\u012f\\\3\2\2\2\u0130\u0131\7"+
		"u\2\2\u0131\u0132\7v\2\2\u0132\u0133\7t\2\2\u0133\u0134\7w\2\2\u0134\u0135"+
		"\7e\2\2\u0135\u0136\7v\2\2\u0136^\3\2\2\2\u0137\u0138\7-\2\2\u0138\u0139"+
		"\7-\2\2\u0139`\3\2\2\2\u013a\u013b\7@\2\2\u013b\u013c\7@\2\2\u013cb\3"+
		"\2\2\2\u013d\u013e\7`\2\2\u013ed\3\2\2\2\u013f\u0140\7\60\2\2\u0140f\3"+
		"\2\2\2\u0141\u0142\7-\2\2\u0142h\3\2\2\2\u0143\u0144\7h\2\2\u0144\u0145"+
		"\7q\2\2\u0145\u0146\7t\2\2\u0146j\3\2\2\2\u0147\u0148\7v\2\2\u0148\u0149"+
		"\7{\2\2\u0149\u014a\7r\2\2\u014a\u014b\7g\2\2\u014b\u014c\7f\2\2\u014c"+
		"\u014d\7g\2\2\u014d\u014e\7h\2\2\u014el\3\2\2\2\u014f\u0150\7t\2\2\u0150"+
		"\u0151\7g\2\2\u0151\u0152\7v\2\2\u0152\u0153\7w\2\2\u0153\u0154\7t\2\2"+
		"\u0154\u0155\7p\2\2\u0155n\3\2\2\2\u0156\u0157\7t\2\2\u0157p\3\2\2\2\u0158"+
		"\u0159\7=\2\2\u0159r\3\2\2\2\u015a\u015b\7(\2\2\u015b\u015c\7(\2\2\u015c"+
		"t\3\2\2\2\u015d\u015e\7~\2\2\u015e\u015f\7~\2\2\u015fv\3\2\2\2\u0160\u0161"+
		"\7@\2\2\u0161x\3\2\2\2\u0162\u0163\7\'\2\2\u0163\u0164\7?\2\2\u0164z\3"+
		"\2\2\2\u0165\u0166\7$\2\2\u0166|\3\2\2\2\u0167\u0168\7\61\2\2\u0168\u0169"+
		"\7?\2\2\u0169~\3\2\2\2\u016a\u016b\7?\2\2\u016b\u016c\7?\2\2\u016c\u0080"+
		"\3\2\2\2\u016d\u016e\7\61\2\2\u016e\u0082\3\2\2\2\u016f\u0170\7\u0080"+
		"\2\2\u0170\u0084\3\2\2\2\u0171\u0172\7@\2\2\u0172\u0173\7?\2\2\u0173\u0086"+
		"\3\2\2\2\u0174\u0176\t\2\2\2\u0175\u0174\3\2\2\2\u0176\u0177\3\2\2\2\u0177"+
		"\u0175\3\2\2\2\u0177\u0178\3\2\2\2\u0178\u0179\3\2\2\2\u0179\u017a\bD"+
		"\2\2\u017a\u0088\3\2\2\2\u017b\u017c\7\61\2\2\u017c\u017d\7,\2\2\u017d"+
		"\u0181\3\2\2\2\u017e\u0180\13\2\2\2\u017f\u017e\3\2\2\2\u0180\u0183\3"+
		"\2\2\2\u0181\u0182\3\2\2\2\u0181\u017f\3\2\2\2\u0182\u0184\3\2\2\2\u0183"+
		"\u0181\3\2\2\2\u0184\u0185\7,\2\2\u0185\u0186\7\61\2\2\u0186\u0187\3\2"+
		"\2\2\u0187\u0188\bE\3\2\u0188\u008a\3\2\2\2\u0189\u018a\7\61\2\2\u018a"+
		"\u018b\7\61\2\2\u018b\u018f\3\2\2\2\u018c\u018e\n\3\2\2\u018d\u018c\3"+
		"\2\2\2\u018e\u0191\3\2\2\2\u018f\u018d\3\2\2\2\u018f\u0190\3\2\2\2\u0190"+
		"\u0192\3\2\2\2\u0191\u018f\3\2\2\2\u0192\u0193\bF\4\2\u0193\u008c\3\2"+
		"\2\2\u0194\u0197\5\u0093J\2\u0195\u0197\t\4\2\2\u0196\u0194\3\2\2\2\u0196"+
		"\u0195\3\2\2\2\u0197\u008e\3\2\2\2\u0198\u019d\5\u0091I\2\u0199\u019c"+
		"\5\u0091I\2\u019a\u019c\5\u0093J\2\u019b\u0199\3\2\2\2\u019b\u019a\3\2"+
		"\2\2\u019c\u019f\3\2\2\2\u019d\u019b\3\2\2\2\u019d\u019e\3\2\2\2\u019e"+
		"\u0090\3\2\2\2\u019f\u019d\3\2\2\2\u01a0\u01a1\t\5\2\2\u01a1\u0092\3\2"+
		"\2\2\u01a2\u01a3\t\6\2\2\u01a3\u0094\3\2\2\2\u01a4\u01a5\5\u008fH\2\u01a5"+
		"\u0096\3\2\2\2\u01a6\u01a7\7)\2\2\u01a7\u01ad\n\7\2\2\u01a8\u01a9\7^\2"+
		"\2\u01a9\u01aa\t\b\2\2\u01aa\u01ab\3\2\2\2\u01ab\u01ad\7)\2\2\u01ac\u01a6"+
		"\3\2\2\2\u01ac\u01a8\3\2\2\2\u01ad\u0098\3\2\2\2\u01ae\u01b2\5\u009bN"+
		"\2\u01af\u01b2\5\u009dO\2\u01b0\u01b2\5\u009fP\2\u01b1\u01ae\3\2\2\2\u01b1"+
		"\u01af\3\2\2\2\u01b1\u01b0\3\2\2\2\u01b2\u009a\3\2\2\2\u01b3\u01b4\7\62"+
		"\2\2\u01b4\u01b6\t\t\2\2\u01b5\u01b7\5\u008dG\2\u01b6\u01b5\3\2\2\2\u01b7"+
		"\u01b8\3\2\2\2\u01b8\u01b6\3\2\2\2\u01b8\u01b9\3\2\2\2\u01b9\u009c\3\2"+
		"\2\2\u01ba\u01be\4\63;\2\u01bb\u01bd\5\u0093J\2\u01bc\u01bb\3\2\2\2\u01bd"+
		"\u01c0\3\2\2\2\u01be\u01bc\3\2\2\2\u01be\u01bf\3\2\2\2\u01bf\u009e\3\2"+
		"\2\2\u01c0\u01be\3\2\2\2\u01c1\u01c5\7\62\2\2\u01c2\u01c4\4\629\2\u01c3"+
		"\u01c2\3\2\2\2\u01c4\u01c7\3\2\2\2\u01c5\u01c3\3\2\2\2\u01c5\u01c6\3\2"+
		"\2\2\u01c6\u00a0\3\2\2\2\u01c7\u01c5\3\2\2\2\16\2\u0177\u0181\u018f\u0196"+
		"\u019b\u019d\u01ac\u01b1\u01b8\u01be\u01c5";
	public static final ATN _ATN =
		ATNSimulator.deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
	}
}