package com.github.vaerys.tags.cctags;

import com.github.vaerys.enums.TagType;
import com.github.vaerys.masterobjects.CommandObject;
import com.github.vaerys.templates.TagObject;

public class TagMath extends TagObject {

    public TagMath(int priority, TagType... types) {
        super(priority, types);
    }

    @Override
    public String execute(String from, CommandObject command, String args) {
        String contents = getContents(from);
        try {
            from = replaceFirstTag(from, "" + fmt(evalMath(contents)));
        } catch (RuntimeException e) {
            from = replaceFirstTag(from, error);
        }
        return from;
    }

    // Source: https://stackoverflow.com/a/14126736
    // Nicely formats doubles.
    private static String fmt(double d)
    {
        if(d == (long) d)
            return String.format("%d",(long)d);
        else
            return String.format("%s",d);
    }

    // Source: https://stackoverflow.com/a/26227947
    // Evaluates a mathematical expression given in string form.
    // Returns a double.
    // Supports addition, subtraction, multiplication (*), division (/),
    //  exponentiation (^), modulus/remainder (%), basic trig, square root (sqrt),
    //  floor, ceil/ceiling
    private static double evalMath(String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException();
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            //        | number | functionName factor | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else if (eat('%')) x %= parseFactor(); // modulus
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else if (func.equals("floor")) x = Math.floor(x);
                    else if (func.equals("ceil") ||
                            func.equals("ceiling")) x = Math.ceil(x);
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }

    @Override
    public String tagName() {
        return "<math>";
    }

    @Override
    public int argsRequired() {
        return 1;
    }

    @Override
    public String usage() {
        return "Expression";
    }

    @Override
    public String desc() {
        return "Evaluates an arithmetic expression and returns the result.\n"+
                "Supports addition, subtraction, multiplication (`*`), division (`/`), " +
                "exponentiation (`^`), modulus/remainder (%), basic trig, square root (sqrt), " +
                "floor, ceil/ceiling.";
    }
}
