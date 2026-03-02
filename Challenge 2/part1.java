import java.io.*;
import java.util.*;

class part1 {
    static Map<Character, Integer> values = new HashMap<>();

    public static void main(String[] args) {
        values.put('A', 247);
        values.put('B', 383);
        values.put('C', 156);
        values.put('D', 512);

        try {
            BufferedReader br = new BufferedReader(new FileReader("data.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(calculate(line));
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    static long calculate(String s) {
        // Base case: no parentheses, just sum up letter values
        if (!s.contains("(")) {
            long sum = 0;
            for (char c : s.toCharArray()) {
                if (values.containsKey(c)) sum += values.get(c);
            }
            return sum;
        }

        // Find the LAST closing parenthesis and its matching opening
        int close = s.lastIndexOf(')');
        int open = findMatchingOpen(s, close);

        // Extract N from {N} after the closing )
        int braceEnd = s.indexOf('}', close);
        int N = Integer.parseInt(s.substring(close + 2, braceEnd));

        // Content inside the parentheses
        String inside = s.substring(open + 1, close);

        // Recursively calculate inside, multiply by N
        long innerValue = calculate(inside) * N;

        // Replace the (content){N} with a placeholder and recurse on the rest
        String before = s.substring(0, open);
        String after = s.substring(braceEnd + 1);

        return calculate(before) + innerValue + calculate(after);
    }

    static int findMatchingOpen(String s, int closeIndex) {
        int count = 0;
        for (int i = closeIndex; i >= 0; i--) {
            if (s.charAt(i) == ')') count++;
            if (s.charAt(i) == '(') count--;
            if (count == 0) return i;
        }
        return -1;
    }
}