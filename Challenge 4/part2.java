import java.io.*;
import java.util.*;

public class part2 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("data.txt"));

        String[] tokens = br.readLine().trim().split(",");
        long[] dishes = new long[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            dishes[i] = Long.parseLong(tokens[i].trim());
        }

        long K = Long.parseLong(br.readLine().trim());
        br.close();

        int n = dishes.length;

        // Sum and max
        long total = 0;
        long maxDishes = 0;
        for (long d : dishes) {
            total += d;
            maxDishes = Math.max(maxDishes, d);
        }

        // Independence number of cycle Cn = floor(n/2)
        long indNum = n / 2;

        // Lower bound 1: adjacency throughput
        long adjacencyBound = (total + indNum - 1) / indNum;

        // Lower bound 2: per-person cooldown
        long personalBound = 1 + (maxDishes - 1) * (K + 1);

        long answer = Math.max(personalBound, adjacencyBound);

        System.out.println(answer);
    }
}