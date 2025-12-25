import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class interpreter {
//25 minutes

    public static void main(String[] args){
        interpreter   interpreter = new interpreter();
        String out = interpreter.evalFile("src/test.txt");
        System.out.println(out);
    }
    public String evalProgram() {
        return evalFile("src/test.txt");
    }

    public String evalProgram(String source) {
        Token lexer = new Token();
        ArrayList<Token.Lexeme> lexemes = lexer.scanWithLexemes(source);
        Parser parser = new Parser();
        parser.loadLexemes(lexemes);
        AST.Program program = parser.buildAST(source);
        return evaluateProgram(program);
    }

    public String evalFile(String path) {
        try {
            String source = Files.readString(Path.of(path));
            return evalProgram(source);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read source file: " + path, e);
        }
    }

    private String evaluateProgram(AST.Program program) {

        if (program == null || program.statements == null) {
            return "";
        }
       StringBuilder output = new StringBuilder();
        for(AST.Statement statement : program.statements) {
            String value = evaluateStatement(statement);
            if(!value.isEmpty()) {

                if(output.length() > 0 )  {
                    output.append("\n");

                }
                output.append(value);
            }

        }
        return output.toString();
    }

    private String evaluateStatement(AST.Statement statement) {
        if (statement instanceof AST.DefineStatement) {
            AST.DefineStatement def = (AST.DefineStatement) statement;
            return evaluateExpressionBlock(def.value);
        }
        return "";
    }

    private String evaluateExpressionBlock(AST.ExpressionBlock block) {
        if (block == null || block.body == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("$");
        for (String piece : block.body) {
            sb.append(piece);
        }
        sb.append("$");
        return sb.toString();
    }

    public String evalute(){
        values vals =  new values();
        String program = vals.getProgram();
        switch (program) {
            case "program":

                return vals.getProgram();
            case "expression":
                return vals.getExpression();
            case "identifier":
                return vals.getIdentifier();
            case "assignment":
                return vals.getAssignment();
        }
        return "";
    }
}
