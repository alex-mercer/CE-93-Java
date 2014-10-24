import java.util.Scanner;
//   __                                           _            _    _    _ 
//   \ \   __ _ __   __  __ _   ___  _   _   ___ | | __ ___   / \  / \  / \
//    \ \ / _` |\ \ / / / _` | / __|| | | | / __|| |/ // __| /  / /  / /  /
// /\_/ /| (_| | \ V / | (_| | \__ \| |_| || (__ |   < \__ \/\_/ /\_/ /\_/ 
// \___/  \__,_|  \_/   \__,_| |___/ \__,_| \___||_|\_\|___/\/   \/   \/   
//                                                                         
public class Main {
    public static void main(String[] args) {
        Scanner console = new Scanner(System.in);
        int n = console.nextInt();
        String st[] = new String[n];
        for (int i = 0; i < n; i++)
            st[i] = console.next();
        String best = "";
        for (int i = 0; i < st[0].length(); i++)
            for (int j = i + 1; j <= st[0].length(); j++) {
                String cand = st[0].substring(i, j);
                String rev = new StringBuffer(cand).reverse().toString();
                boolean isG = true;
                for (int k = 1; k < n && isG; k++)
                    if (!st[k].contains(cand) && !st[k].contains(rev))
                        isG = false;
                if (isG && best.length() <= cand.length())//using less equal because we want the last longest substring
                    best = cand;
            }
        if (!best.equals(""))
            System.out.println(best);
    }
}
