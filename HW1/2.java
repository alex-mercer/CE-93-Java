import java.util.Scanner;

public class Main {
    /**
     * Calculates the expression a (op) b
     *
     * @param a  left operand
     * @param op the operator from the set +,-,/,*
     * @param b  right operand
     * @return the result of operation
     */
    static int parseOp(int a, char op, int b) {
        if (op == '+') return a + b;
        if (op == '-') return a - b;
        if (op == '*') return a * b;
        if (op == '/') return a / b;
        return 0;
    }

    /**
     * Handles the cases where str is a number or a function
     *
     * @param str the string to evaluate
     * @return an integer which is the result of str evaluation
     */
    static int parseFunc(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            if (str.equals(""))
                return 0;
            if (str.substring(0, 3).equals("abs"))
                return Math.abs(parsePlusMinus(str.substring(4, str.length() - 1)));
            else if (str.substring(0, 4).equals("sqrt"))
                return (int) Math.sqrt(parsePlusMinus(str.substring(5, str.length() - 1)));
            throw e;
        }
    }

    /**
     * Handles * and / operators
     *
     * @param str the string to evaluate
     * @return an integer which is the result of str evaluation
     */
    static int parseMultiDiv(String str) {
        int depth = 0;
        int last = 0;
        char lastsign = '+';
        int ans = 0;
        for (int i = 0; i < str.length(); i++) {
            char cur = str.charAt(i);
            if (cur == '(')
                depth++;
            else if (cur == ')')
                depth--;
            else if ((cur == '*' || cur == '/') && depth == 0) {
                ans = parseOp(ans, lastsign, parseFunc(str.substring(last, i)));
                lastsign = cur;
                last = i + 1;
            }
        }
        ans = parseOp(ans, lastsign, parseFunc(str.substring(last, str.length())));
        return ans;
    }

    /**
     * Handles + and - operators
     *
     * @param str the string to evaluate
     * @return an integer which is the result of str evaluation
     */
    static int parsePlusMinus(String str) {
        int depth = 0;
        int last = 0;
        char lastsign = '+';
        int ans = 0;
        for (int i = 0; i < str.length(); i++) {
            char cur = str.charAt(i);
            if (cur == '(')
                depth++;
            else if (cur == ')')
                depth--;
            else if ((cur == '+' || cur == '-') && depth == 0) { //we don't handle the + and - if it's inside ()
                ans = parseOp(ans, lastsign, parseMultiDiv(str.substring(last, i)));
                lastsign = cur;
                last = i + 1;
            }
        }
        ans = parseOp(ans, lastsign, parseMultiDiv(str.substring(last, str.length())));
        return ans;
    }

    public static void main(String[] args) {
        Scanner console = new Scanner(System.in);
        String exp = console.nextLine();
        exp = exp.replaceAll(" ", "");
        System.out.println((int) parsePlusMinus(exp));
    }
}
