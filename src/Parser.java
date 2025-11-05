import java.util.ArrayList;

public class Parser  {
// 35 minute mark Parser part 3


    //AST.NodeType nodeType = AST.NodeType.PROGRAM;

    ArrayList<String> tokens = new ArrayList<String>();

    private boolean notEof(){
        return !tokens.isEmpty();
    }

    public String produceAST(String sourceCode) {
        // builds a PROGRAM with zero or more define statements
        AST ast = new AST();
        AST.Program program = ast.new Program();
        program.statements = new ArrayList<AST.Statement>();

        while (notEof()) {
            String t = peek();
            if ("DEFINE".equals(t)) {
                program.statements.add(parseDefine(ast));
            } else {
                // skip unknown tokens to avoid infinite loop
                consume();
            }
        }

        return printProgram(program);
    }

    //Parsing expression logic
    //get previous token and advance to the next token
    private String consume() {
        return tokens.remove(0);
    }

    private String peek() {
        return tokens.get(0);
    }

    private boolean match(String type) {
        return notEof() && type.equals(peek());
    }

    private void expect(String type, String msg) {
        if (!match(type)) {
            throw new RuntimeException("Parse error: expected " + type + " but found " + (notEof() ? peek() : "<EOF>") + ". " + msg);
        }
        consume();
    }

    // Order of Prescidence
// AssignmentExpr
// MemberExpr
// FunctionCall
// LogicalExpr
// ComparisonExpr
// AdditiveExpr
// MultiplicativeExpr
// UnaryExpr
// PrimaryExpr
    private String parsePrimaryExpr() {
        if (!notEof()){
            return "<EOF>";
        }
        String tk = tokens.get(0); // peek only; no .getValue() available

        if ("EXPR".equals(tk)) {
            return "EXPR";
        }
        if ("IDENTIFIER".equals(tk)) {
            return "IDENTIFIER";
        }
        return tk; // PLUS, MINUS, etc.
    }

    // DefineStatement -> DEFINE IDENTIFIER EQUALS ExpressionBlock
    private AST.DefineStatement parseDefine(AST ast) {
        expect("DEFINE", "define statement must start with 'define'.");
        expect("IDENTIFIER", "identifier required after 'define'.");
        String nameToken = "IDENTIFIER"; // no lexeme available from Token.java (kept unchanged)
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
                blk.body.add(t); // type-only for now
                consume();
                continue;
            }


            consume();
        }

        expect("RIGHTCURLY", "expected '}' to close expression block.");
        return blk;
    }

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

    public static void main(String[] args){
        Parser p = new Parser();
        Token lexer = new Token();
        p.tokens = lexer.Tokenize("define test = { 1 2 3 $$ }");
        System.out.println(p.produceAST(""));
    }
}

