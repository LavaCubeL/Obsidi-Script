import java.util.List;
import java.util.ArrayList;

public class AST {
//Now I got to implement the AST
//I have no clue what the hell I am doing
    /*AST representation
    DefineStatement
 ├── Identifier:
 └── ExpressionBlock
      └── tokens (type-only for now): EXPR / EXPR $
     */

    public enum NodeType{
        PROGRAM,
        EXPRESSION,
        IDENTIFIER,
        ASSIGNMENT,
    }

    public interface Node{

    }

    public interface Statement extends Node{

    }

    public interface Expression extends Node{

    }

    class Program implements Node {
        List<Statement> statements; // parser will allocate
    }

    class DefineStatement implements Statement {
        Identifier name;
        ExpressionBlock value; // <-- minimal: RHS block
    }

    class Identifier implements Expression {
        String name; // no lexeme available yet; parser will put "IDENTIFIER"
    }

    class ExpressionBlock implements Expression {
        ArrayList<String> body = new ArrayList<String>(); // <-- minimal: token types inside { ... }
    }

}

