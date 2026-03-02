import java.io.*;
import java.util.*;

class part2 {
    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("data.txt"));
            int T = Integer.parseInt(reader.readLine().trim());
            List<Integer> numbers = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                numbers.add(Integer.parseInt(line.trim()));
            }

            Set<Integer> numSet = new HashSet<>(numbers);

            boolean found = false;
            for (int i = 0; i < numbers.size() && !found; i++) {
                for (int j = i + 1; j < numbers.size() && !found; j++) {
                    int complement = T - numbers.get(i) - numbers.get(j);
                    if (complement != numbers.get(i) && complement != numbers.get(j) && numSet.contains(complement)) {
                        long product = (long)numbers.get(i) * numbers.get(j) * complement;
                        System.out.println("Found: " + numbers.get(i) + " + " + numbers.get(j) + " + " + complement + " = " + T);
                        System.out.println("Product: " + product);
                        found = true;
                    }
                }
            }

            if (!found) System.out.println("No triplet found that sums to " + T);

        } catch (IOException e) {
            System.out.println("Error reading input: " + e.getMessage());
        }
    }
}