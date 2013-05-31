/*
 *  The scanner definition for COOL.
 */

import java_cup.runtime.Symbol;

%%

%class CoolLexer
%cup
%line
%column

%{
    // Max size of string constants
    static int MAX_STR_CONST = 1025;
    static String messageError;
    private String filename;

    // For assembling string constants
    StringBuffer buf = new StringBuffer();

	public int getCurrentLine(){
		return yyline;
	}
	
	public void setFileName(String file){
		filename = file;
	}
	public String getFileName(){
		return filename;
	}
	
	static private int commentLevel = -1;

%}

%init{
    // empty for now
%init}





/* White space is a line terminator, space, tab, or line feed. */
WhiteSpace = [ \n\f\r\t\v\013]
LineTerminator = \r|\n|\r\n
Digit	= [0-9]
Integer = {Digit}+
idchar	= [A-Za-z0-9_]

typeid	= [A-Z]{idchar}*
objid	= [a-z]{idchar}*

class	= [Cc][Ll][Aa][Ss][Ss]
else	= [Ee][Ll][Ss][Ee]
fi		= [Ff][Ii]
if		= [Ii][Ff]
in		= [Ii][Nn]
inherits = [Ii][Nn][Hh][Ee][Rr][Ii][Tt][Ss]
isvoid	= [Ii][Ss][Vv][Oo][Ii][Dd]
let		= [Ll][Ee][Tt]
loop	= [Ll][Oo][Oo][Pp]
pool	= [Pp][Oo][Oo][Ll]
then	= [Tt][Hh][Ee][Nn]
while	= [Ww][Hh][Ii][Ll][Ee]
case	= [Cc][Aa][Ss][Ee]
esac	= [Ee][Ss][Aa][Cc]
new		= [Nn][Ee][Ww]
of		= [Oo][Ff]
not		= [Nn][Oo][Tt]

false	= f[Aa][Ll][Ss][Ee]
true	= t[Rr][Uu][Ee]

%state STRING
%state COMMENT
%state SINGLE_COMMENT
%state STRERROR


%eofval{
    switch(yystate()) {
	    case YYINITIAL:
			break;
		case COMMENT:
			yybegin(YYINITIAL);
			return new Symbol(sym.ERROR, "EOF in comment");
		case SINGLE_COMMENT:
			yybegin(YYINITIAL);
			break;
		case STRING:
			yybegin(YYINITIAL);
			return new Symbol(sym.ERROR, "EOF in string constant");
    }
    return new Symbol(sym.EOF);
%eofval}

%%


<YYINITIAL>\"		{ buf.setLength(0); yybegin(STRING);}
<STRING> {
	\"				{ yybegin(YYINITIAL);
					  if(buf.length() >= 1025)
					  	return new Symbol(sym.ERROR, "String constant too long"); 
					  return new Symbol(sym.STR_CONST, buf.toString()); }
	[^\"\n\\\0]+	{ buf.append(yytext());}
	\\b				{ buf.append('\b');}
	\\t				{ buf.append('\t');}
	\\n				{ buf.append('\n');}
	\\f				{ buf.append('\f');}
	\\\0			{ yybegin(STRERROR); messageError = "String contains escaped null character."; }
	
	\0				{ yybegin(STRERROR); messageError = "String contains null character."; }
	
	\\[ \t\v]*\n	{ buf.append('\n');}
	\\.				{ buf.append(yytext().charAt(1));}
	\n
					{ yybegin(YYINITIAL); return new Symbol(sym.ERROR, "Unterminated string constant"); }
}
<STRERROR>{
	\"			{ yybegin(YYINITIAL); return new Symbol(sym.ERROR, messageError); }
	[^\"\n]*	{ }
	\\\n		{ }
	\n			{ yybegin(YYINITIAL); return new Symbol(sym.ERROR, messageError); }
}


<YYINITIAL>"--"								{ yybegin(SINGLE_COMMENT); }
<SINGLE_COMMENT>[^\r\n]*{LineTerminator} 	{ yybegin(YYINITIAL); }
<SINGLE_COMMENT>[^\r\n]*					{ yybegin(YYINITIAL); }

<YYINITIAL>"(*"		{ commentLevel = 1; yybegin(COMMENT); }
<YYINITIAL>"*)"		{ return new Symbol(sym.ERROR, "Unmatched *)"); }
<COMMENT> {
	"(*"			{ commentLevel++;}
	[^*(]*			{ }
	"(*"			{ commentLevel++;}
	\(				{ }
	"*)"			{ commentLevel--; if(commentLevel == 0) yybegin(YYINITIAL); }
	\*				{ }		

}





<YYINITIAL>{WhiteSpace}	{}

/******** Keywords ********/
{class}			{return new Symbol(sym.CLASS);}
{else}			{return new Symbol(sym.ELSE);}
{fi}			{return new Symbol(sym.FI);}
{if}			{return new Symbol(sym.IF);}
{in}			{return new Symbol(sym.IN);}
{inherits}		{return new Symbol(sym.INHERITS);}
{isvoid}		{return new Symbol(sym.ISVOID);}
{let}			{return new Symbol(sym.LET);}
{loop}			{return new Symbol(sym.LOOP);}
{pool}			{return new Symbol(sym.POOL);}
{then}			{return new Symbol(sym.THEN);}
{while}			{return new Symbol(sym.WHILE);}
{case}			{return new Symbol(sym.CASE);}
{esac}			{return new Symbol(sym.ESAC);}
{new}			{return new Symbol(sym.NEW);}
{of}			{return new Symbol(sym.OF);}
{not}			{return new Symbol(sym.NOT);}


/******* Constants *******/
{Integer}		{ return new Symbol(sym.INT_CONST, yytext());}
{false}			{ return new Symbol(sym.BOOL_CONST, false);}
{true}			{ return new Symbol(sym.BOOL_CONST, true);}


/******* Identifiers ******/
{objid}		{ return new Symbol(sym.OBJECTID, yytext());}
{typeid}	{ return new Symbol(sym.TYPEID, yytext());}


/******** Operators *******/
"+"		{ return new Symbol(sym.PLUS);}
"-"		{ return new Symbol(sym.MINUS);}
"*"		{ return new Symbol(sym.MULT);}
"/"		{ return new Symbol(sym.DIV);}
"~"		{ return new Symbol(sym.NEG);}
"<"		{ return new Symbol(sym.LT);}
"<="	{ return new Symbol(sym.LE);}
"="		{ return new Symbol(sym.EQ);}

/******** Special **********/
","  	{ return new Symbol(sym.COMMA); }
";"  	{ return new Symbol(sym.SEMI); }
":"  	{ return new Symbol(sym.COLON); }
"("  	{ return new Symbol(sym.LPAREN); }
")"  	{ return new Symbol(sym.RPAREN); }
"@"  	{ return new Symbol(sym.AT); }
"{"		{ return new Symbol(sym.LBRACE); }
"}"  	{ return new Symbol(sym.RBRACE); }
"=>"	{ return new Symbol(sym.DARROW); }
"<-"	{ return new Symbol(sym.ASSIGN); }
"."		{ return new Symbol(sym.DOT);}

.       { return new Symbol(sym.ERROR, yytext()); }




