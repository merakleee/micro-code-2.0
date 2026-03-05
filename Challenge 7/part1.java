import java.io.*;

public class part1 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("data.txt"));

        int P = Integer.parseInt(br.readLine().trim());
        long grandTotal = 0;

        for (int p = 0; p < P; p++) {
            String[] group = new String[4];
            for (int j = 0; j < 4; j++) {
                group[j] = br.readLine().trim();
            }

            long treatmentBurden = 0;

            for (int g = 1; g <= 1000; g++) {

                // Step 1: Selection
                int[] strength = new int[4];
                for (int j = 0; j < 4; j++) {
                    strength[j] = countOnes(group[j]);
                }

                int firstIdx = -1, secondIdx = -1;
                for (int j = 0; j < 4; j++) {
                    if (firstIdx == -1 || strength[j] > strength[firstIdx] ||
                       (strength[j] == strength[firstIdx] && j > firstIdx)) {
                        firstIdx = j;
                    }
                }
                for (int j = 0; j < 4; j++) {
                    if (j == firstIdx) continue;
                    if (secondIdx == -1 || strength[j] > strength[secondIdx] ||
                       (strength[j] == strength[secondIdx] && j > secondIdx)) {
                        secondIdx = j;
                    }
                }

                String first = group[firstIdx];
                String second = group[secondIdx];
                int strengthFirst = strength[firstIdx];
                int strengthSecond = strength[secondIdx];

                // Step 2: Blend point
                int split = (strengthFirst + strengthSecond) % 7 + 1;

                // Step 3: Recombination
                String offspring1 = first.substring(0, split) + second.substring(split, 8);
                String offspring2 = second.substring(0, split) + first.substring(split, 8);

                // Step 4: Radiation strike
                int strike = (g * 3 + strengthFirst) % 8;
                offspring1 = flipBit(offspring1, strike);
                offspring2 = flipBit(offspring2, strike);

                // Step 5: Rotation
                String[] population = {first, second, offspring1, offspring2};
                int k = g % 4;
                String[] rotated = new String[4];
                for (int j = 0; j < 4; j++) {
                    rotated[(j + k) % 4] = population[j];
                }
                group = rotated;

                // Record sum of decimal values
                for (int j = 0; j < 4; j++) {
                    treatmentBurden += Integer.parseInt(group[j], 2);
                }
            }

            grandTotal += treatmentBurden;
        }

        System.out.println(grandTotal);
    }

    static int countOnes(String s) {
        int count = 0;
        for (char c : s.toCharArray()) {
            if (c == '1') count++;
        }
        return count;
    }

    static String flipBit(String s, int pos) {
        char[] arr = s.toCharArray();
        arr[pos] = (arr[pos] == '0') ? '1' : '0';
        return new String(arr);
    }
}