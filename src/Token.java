import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
// sample syntax: define expression = {}
//  Lexer basically done

/*
I need to implement  the rest of the tokens of the language
 */
public class Token {

    public static class Lexeme {
        public final TokenType type;
        public final String text; // original lexeme text

        public Lexeme(TokenType type, String text) {
            this.type = type;
            this.text = text;
        }

        @Override
        public String toString() {
            return type.name() + "(" + text + ")";
        }
    }


    public enum TokenType {
        LEFTCURLY,
        RIGHTCURLY,
        EXPR,
        EQUALS,
        SLASH,
        PLUS,
        MINUS,
        DEFINE,
        INDENT,
        IDENTIFIER,
        DOLLARSIGN,
        EOF,//end of file
    }
    //Involved with ast
    // keyword table
    public Map<String, TokenType> tokens() {
        Map<String, TokenType> keywords = new HashMap<>();
        keywords.put("define", TokenType.DEFINE);   // use lowercase for case-insensitive match
        return keywords;
    }
    //Covers whitespace,newline and tab
    public Boolean isSkip(String str){
        return " ".equals(str) || "\n".equals(str) || "\t".equals(str);
    }
    //Creates a public that takes a String and returns value
    public String tkn(String value){

        return value;
    }

    //Checks whether a String is numeric or a string
    public boolean isAlpha(String s){
        if (s == null || s.isEmpty()){
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isLetter(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public boolean isInt(String s){
        if (s == null || s.isEmpty()){
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))){
                return false;
            }
        }
        return true;
    }
    //Adds  the letters  into an arraylist
    public ArrayList<Lexeme> scanWithLexemes(String sourceCode) {
        ArrayList<Lexeme> out = new ArrayList<>();
        ArrayList<String> src = new ArrayList<>(Arrays.asList(sourceCode.trim().split("\\s+")));
        Map<String, TokenType> kw = tokens();

        while (!src.isEmpty()) {
            String first = src.remove(0);

            if (first.equals("{")) {
                out.add(new Lexeme(TokenType.LEFTCURLY, first));
                continue;
            } else if (first.equals("}")) {
                out.add(new Lexeme(TokenType.RIGHTCURLY, first));
                continue;
            } else if (first.equals("=")) {
                out.add(new Lexeme(TokenType.EQUALS, first));
                continue;
            } else if (first.equals("+")) {
                out.add(new Lexeme(TokenType.PLUS, first));
                continue;
            } else if (first.equals("-")) {
                out.add(new Lexeme(TokenType.MINUS, first));
                continue;
            } else if (first.equals("/")) {
                out.add(new Lexeme(TokenType.SLASH, first));
                continue;
            } else if (first.equals("$")) {
                out.add(new Lexeme(TokenType.DOLLARSIGN, first));
                continue;
            }

            if (isInt(first)) {
                out.add(new Lexeme(TokenType.EXPR, first));
            } else if (isAlpha(first)) {
                TokenType maybeKw = kw.get(first.toLowerCase());
                if (maybeKw != null) {
                    out.add(new Lexeme(maybeKw, first));
                } else {
                    out.add(new Lexeme(TokenType.IDENTIFIER, first));
                }
            } else {
                System.out.println("Unrecognized character found in source " + first);
            }
        }
        return out;
    }

    public ArrayList<String> Tokenize(String sourceCode){
        ArrayList<String> out = new ArrayList<>();
        ArrayList<String> src = new ArrayList<>(Arrays.asList(sourceCode.trim().split("\\s+")));
        Map<String, TokenType> kw = tokens();

        // build until end of token stream
        while (!src.isEmpty()) {
            String first = src.remove(0); // consume

            // single-character tokens
            if (first.equals("{")) {
                out.add(TokenType.LEFTCURLY.name());
                continue;
            } else if (first.equals("}")) {
                out.add(TokenType.RIGHTCURLY.name());
                continue;
            } else if (first.equals("=")) {
                out.add(TokenType.EQUALS.name());
                continue;
            } else if (first.equals("+")) {
                out.add(TokenType.PLUS.name());
                continue;
            } else if (first.equals("-")) {
                out.add(TokenType.MINUS.name());
                continue;
            } else if (first.equals("/")) {
                out.add(TokenType.SLASH.name());
                continue;
            }
            else if (first.equals("$")){
                out.add(TokenType.DOLLARSIGN.name());
                continue;
            }

            // multi-character tokens (numbers / identifiers / keywords)
            if (isInt(first)) {
                out.add(TokenType.EXPR.name()); // treat numbers as EXPR per your enum
            } else if (isAlpha(first)) {
                TokenType maybeKw = kw.get(first.toLowerCase());
                if (maybeKw != null) {
                    out.add(maybeKw.name());
                } else {
                    out.add(TokenType.IDENTIFIER.name());
                }
            } else {
                // unrecognized token shape; ignore or handle as needed
                if(isInt(src.get(0))){

                }else if(isAlpha(src.get(0))){

                }
                else if(isInt(src.get(0))){
                    src.remove(0);
                }
                else{
                    System.out.println("Unrecognized character found in source " + src.get(0));
                }

            }
        }
        return out;
    }

    //Now we have to read the file
    public static void main(String[] args){
        //You need to create a new object if you are going from non-static to static method
        Token lexer = new Token();
        try (BufferedReader br = new BufferedReader(new FileReader("src/test.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                ArrayList<String> toks = lexer.Tokenize(line);
                if (!toks.isEmpty()) {
                    System.out.println(toks);  // prints token types like [DEFINE, IDENTIFIER, ...]
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
