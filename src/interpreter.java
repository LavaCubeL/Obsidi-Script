public class interpreter {
//20 minutes
// Bro what is even happenging in the video

    public  String evalProgram(){
    String lastEvaluated  =  "test";


        return lastEvaluated;
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
