import java.util.ArrayList;

/**
 * Parser consumes a stream of token type strings and constructs a minimal AST representation
 * <p>
 * The surrounding project currently pushes only token type names (e.g., {@code DEFINE}) into the parser
 * Once the lexer carries lexeme information this class can be extended to populate richer AST nodes
 * </p>
 */
public class Parser  {
// 35 minute mark Parser part 3




    ArrayList<String> tokens = new ArrayList<String>();
    ArrayList<Token.Lexeme> lexemeStream = new ArrayList<Token.Lexeme>();
    private boolean useLexemes = false;

    public void loadLexemes(ArrayList<Token.Lexeme> lexemes) {
        this.lexemeStream = new ArrayList<>(lexemes);
        this.useLexemes = true;
        this.tokens.clear();
    }

    /**
     * @return {@code true} when there are remaining token type entries to process
     */
    private boolean notEof(){
        if (useLexemes) {
            return !lexemeStream.isEmpty();
        }
        return !tokens.isEmpty();
    }

    /**
     * Builds a {@link AST.Program} tree by iterating through the previously supplied {@link #tokens}
     * @param sourceCode  so callers can keep passing the original source text
     * @return A string representation of the program subtree for quick debugging and logging
     */
    public String produceAST(String sourceCode) {
        AST.Program program = buildAST(sourceCode);
        return printProgram(program);
    }

    public AST.Program buildAST(String sourceCode) {
        AST ast = new AST();
        AST.Program program = ast.new Program();
        program.statements = new ArrayList<AST.Statement>();

        while (notEof()) {
            String t = peek();
            if ("DEFINE".equals(t)) {
                program.statements.add(parseDefine(ast));
            } else {
                consume();
            }
        }

        return program;
    }

    //Parsing expression logic
    //get previous token and advance to the next token
    /**
     * Removes and returns the next token type string from the stream
     * Using a simple {@link ArrayList} keeps the parser approachable for classroom contexts
     */
    private String consume() {
        if (useLexemes) {
            Token.Lexeme lx = lexemeStream.remove(0);
            return lx.type.name();
        }
        return tokens.remove(0);
    }

    /**
     * @return The next token type without removing it; callers must ensure the stream is not empty.
     */
    private String peek() {
        if (useLexemes) {
            return lexemeStream.get(0).type.name();
        }
        return tokens.get(0);
    }

    private Token.Lexeme peekLexeme() {
        if (!useLexemes || lexemeStream.isEmpty()) {
            return null;
        }
        return lexemeStream.get(0);
    }

    /**
     * Checks whether the next token matches the expected type.
     * @param type token type string such as {@code DEFINE} or {@code IDENTIFIER}.
     */
    private boolean match(String type) {
        return notEof() && type.equals(peek());
    }

    /**
     * Enforces that the next token matches {@code type}; throws a runtime error when it does not.
     * @param type expected token type.
     * @param msg additional context that will be appended to the error message.
     */
    private void expect(String type, String msg) {
        if (!match(type)) {
            throw new RuntimeException("Parse error: expected " + type + " but found " + (notEof() ? peek() : "<EOF>") + ". " + msg);
        }
        consume();
    }


    /**
     * Parses the most basic expressions (identifiers, literal markers, or operators).
     * This is a placeholder until the rest of the precedence climbing stack is implemented.
     */
    private String parsePrimaryExpr() {
        if (!notEof()){
            return "<EOF>";
        }
        String tk = peek(); // type view even when lexemes available

        if ("EXPR".equals(tk)) {
            return "EXPR";
        }
        if ("IDENTIFIER".equals(tk)) {
            return "IDENTIFIER";
        }
        return tk; // PLUS, MINUS, etc
    }

    // DefineStatement -> DEFINE IDENTIFIER EQUALS ExpressionBlock
    /**
     * Parses a {@code define} statement and connects it to {@link AST.DefineStatement}
     * @param ast shared AST factory instance used to construct nested nodes
     */
    private AST.DefineStatement parseDefine(AST ast) {
        expect("DEFINE", "define statement must start with 'define'.");
        Token.Lexeme idLex = peekLexeme();
        expect("IDENTIFIER", "identifier required after 'define'.");
        String nameToken = idLex != null ? idLex.text : "IDENTIFIER";
        expect("EQUALS", "expected '=' after identifier.");
        AST.ExpressionBlock block = parseExpressionBlock(ast);

        AST.DefineStatement def = ast.new DefineStatement();
        AST.Identifier id = ast.new Identifier();
        id.name = nameToken; // placeholder until lexer carries names
        def.name = id;
        def.value = block;
        return def;
    }

    // ExpressionBlock -> LEFTCURLY (EXPR|IDENTIFIER|PLUS|MINUS|SLASH|DOLLARSIGN)* RIGHTCURLY
    /**
     * Collects the sequence of expression-related token types inside a block delimited by curly braces
     * @param ast shared AST factory instance reused across parse methods
     */
    private AST.ExpressionBlock parseExpressionBlock(AST ast) {
        expect("LEFTCURLY", "expected '{' to start expression block.");
        AST.ExpressionBlock blk = ast.new ExpressionBlock();

        while (notEof() && !match("RIGHTCURLY")) {
            String t = peek();

            if ("EXPR".equals(t) ||
                    "IDENTIFIER".equals(t) ||
                    "PLUS".equals(t) ||
                    "MINUS".equals(t) ||
                    "SLASH".equals(t) ||
                    "DOLLARSIGN".equals(t)) {
                Token.Lexeme lx = peekLexeme();
                String stored = (lx != null) ? lx.text : t;
                blk.body.add(stored);
                consume();
                continue;
            }


            consume();
        }

        expect("RIGHTCURLY", "expected '}' to close expression block");
        return blk;
    }

    /*
    Prints the program using StringBuilder
     */
    private String printProgram(AST.Program p) {
        StringBuilder sb = new StringBuilder();
        sb.append("PROGRAM\n");
        if (p.statements != null) {
            for (AST.Statement st : p.statements) {
                if (st instanceof AST.DefineStatement) {
                    AST.DefineStatement d = (AST.DefineStatement) st;
                    sb.append("  DEFINE\n");
                    sb.append("    name: ").append(d.name != null ? d.name.name : "<null>").append("\n");
                    sb.append("    block: ").append(d.value != null ? d.value.body.toString() : "<null>").append("\n");
                }
            }
        }
        return sb.toString();
    }

    /*
      Tests parser implementation - Handles numbers dollarsigns letters,and words
     */
    public static void main(String[] args){
        Parser p = new Parser();
        Token lexer = new Token();
        ArrayList<Token.Lexeme> lexemes = lexer.scanWithLexemes("define test = { 1 2 3 $ }");
        p.loadLexemes(lexemes);
        System.out.println(p.produceAST(""));
    }
}

