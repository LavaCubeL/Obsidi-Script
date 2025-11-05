public class values {
/*
   public enum NodeType{
        PROGRAM,
        EXPRESSION,
        IDENTIFIER,
        ASSIGNMENT,
    }
 */
    final String Program = "PROGRAM";
    final String Expression = "EXPRESSION";
    final String IDENTIFIER = "IDENTIFIER";
    final String ASSIGNMENT = "ASSIGNMENT";

    public String getProgram(){
        return Program;
    }

    public String getExpression(){
        return Expression;
    }

    public String getIdentifier(){
        return IDENTIFIER;
    }

    public String getAssignment(){
        return ASSIGNMENT;
    }


}
