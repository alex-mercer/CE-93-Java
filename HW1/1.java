import java.util.Collections;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    /**
     * Converts the number to string and do some formatting stuff
     *
     * @param d the number to format
     * @return the string representing d with 0.01 precision and trailing zeros removed
     */
    static String get_num(double d) {
        String res = String.format("%+.4f", d);
        res = res.substring(0, res.length() - 2);
        while (res.endsWith("0"))//removing trailing zeros
            res = res.substring(0, res.length() - 1);
        if (res.endsWith("."))
            res = res.substring(0, res.length() - 1);
        if (res.length() == 2) {
            if (res.charAt(1) == '0')
                res = "0";
            else if (res.charAt(1) == '1')
                res = res.substring(0, 1);
        }
        return res;
    }

    public static void main(String[] args) {
        Scanner console = new Scanner(System.in);
        String exp = console.nextLine();
        exp = exp.replaceAll(" ", "");
        if (exp.charAt(0) != '-' && exp.charAt(0) != '+')//for more convenience later in string parsing
            exp = "+" + exp;
        exp = exp.replaceAll("-x", "-1x");
        exp = exp.replaceAll("[+]x", "+1x");
        Pattern pattern = Pattern.compile("(?<sign>[+-])(?<zarib>\\d+)(?<hasX>x)?(\\^(?<power>\\d+))?");
        Matcher matcher = pattern.matcher(exp);
        TreeMap<Integer, Integer> tm = new TreeMap<Integer, Integer>(Collections.reverseOrder());
        while (matcher.find()) {
            if (matcher.group().equals(""))
                continue;
            int zarib = Integer.parseInt(matcher.group("zarib"));
            if (matcher.group("sign").equals("-")) zarib = -zarib;
            int p = 1;
            if (matcher.group("power") != null && !matcher.group("power").isEmpty())
                p = Integer.parseInt(matcher.group("power"));
            if (matcher.group("hasX") == null || matcher.group("hasX").isEmpty()) p = 0;
            if (tm.get(p) == null) tm.put(p, 0);
            tm.put(p, tm.get(p) + zarib);
        }
        boolean isFirst = true;
        for (Map.Entry<Integer, Integer> entry : tm.entrySet()) { //automatically iterate the Map from highest to lowest
            int p = entry.getKey();
            int zarib = entry.getValue();
            String z = get_num((double) zarib / (p + 1));
            if (z.equals("0")) continue;
            if (isFirst && z.charAt(0) == '+')//no + before the first expression
                z = z.substring(1);
            System.out.print(z + "x");
            if (p != 0)
                System.out.print("^" + (p + 1));
            isFirst = false;
        }
        if (isFirst) //if nothing is printed we should print just 0
            System.out.print('0');
    }
}
