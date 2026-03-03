import java.io.*;
import java.util.*;

public class part1 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("data.txt"));

        String[] tokens = br.readLine().trim().split(",");
        long[] dishes = new long[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            dishes[i] = Long.parseLong(tokens[i].trim());
        }

        long K = Long.parseLong(br.readLine().trim());
        br.close();

        int n = dishes.length ;

        long total = 0;
        for (long d : dishes) total += d;

        long maxDishes = 0;
        for (long d : dishes) maxDishes = Math.max(maxDishes, d);

        long indNum = n / 2;

        long maxAdjacent = 0;
         for (int i = 0; i < n; i++) {
            maxAdjacent = Math.max(maxAdjacent, dishes[i] + dishes[(i+1) % n]);
        }

        long rounds = Math.max(maxDishes, Math.max((total + indNum - 1) / indNum, maxAdjacent));

        System.out.println(rounds);
    }
}