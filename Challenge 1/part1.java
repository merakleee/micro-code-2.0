import java.io.*;
import java.util.*;

class part1 {
    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("data.txt"));
            int T = Integer.parseInt(reader.readLine().trim());
            
            List<Integer> numbers = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                numbers.add(Integer.parseInt(line.trim()));
            }

            boolean found = false;
            for (int i = 0; i < numbers.size() && !found; i++) {
                for (int j = i + 1; j < numbers.size() && !found; j++) {
                    if (numbers.get(i) + numbers.get(j) == T) {
                        System.out.println("Found: " + numbers.get(i) + " + " + numbers.get(j) + " = " + T);
                        long product = (long)numbers.get(i) * numbers.get(j);
                        System.out.println("Product: " + product);
                        found = true;
                    }
                }
            }

            if (!found) System.out.println("No pair found that sums to " + T);

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}