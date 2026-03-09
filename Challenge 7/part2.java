import java.io.*;

public class part2 {
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
            String rkey = "11010110";

            for (int g = 1; g <= 1000; g++) {

                if (g % 100 == 0) {
                    rkey = rkey.substring(1) + rkey.charAt(0);
                    rkey = flipBit(rkey, 2);
                    rkey = flipBit(rkey, 5);
                }

                if (g % 250 == 0) {
                    rkey = new StringBuilder(rkey).reverse().toString();
                }

                int[] resemblance = new int[4];
                for (int j = 0; j < 4; j++) {
                    resemblance[j] = 8 - difference(group[j], rkey);
                }

                int firstIdx = -1, secondIdx = -1;
                for (int j = 0; j < 4; j++) {
                    if (firstIdx == -1 || resemblance[j] > resemblance[firstIdx] ||
                       (resemblance[j] == resemblance[firstIdx] && j > firstIdx)) {
                        firstIdx = j;
                    }
                }
                for (int j = 0; j < 4; j++) {
                    if (j == firstIdx) continue;
                    if (secondIdx == -1 || resemblance[j] > resemblance[secondIdx] ||
                       (resemblance[j] == resemblance[secondIdx] && j > secondIdx)) {
                        secondIdx = j;
                    }
                }

                String first = group[firstIdx];
                String second = group[secondIdx];
                int resemblanceFirst = resemblance[firstIdx];
                int resemblanceSecond = resemblance[secondIdx];

                int split;
                if (countOnes(rkey) > 4) {
                    split = (resemblanceFirst + resemblanceSecond + 2) % 7 + 1;
                } else {
                    split = (resemblanceFirst + resemblanceSecond) % 7 + 1;
                }

                String offspring1 = first.substring(0, split) + second.substring(split, 8);
                String offspring2 = second.substring(0, split) + first.substring(split, 8);

                int strike = (g * 3 + resemblanceFirst) % 8;
                offspring1 = flipBit(offspring1, strike);
                offspring2 = flipBit(offspring2, strike);

                String[] population = {first, second, offspring1, offspring2};
                int k = g % 4;
                String[] rotated = new String[4];
                for (int j = 0; j < 4; j++) {
                    rotated[(j + k) % 4] = population[j];
                }
                group = rotated;

                for (int j = 0; j < 4; j++) {
                    treatmentBurden += Integer.parseInt(group[j], 2);
                }
            }

            grandTotal += treatmentBurden;
        }

        System.out.println(grandTotal);
    }

    static int difference(String s, String rkey) {
        int count = 0;
        for (int i = 0; i < 8; i++) {
            if (s.charAt(i) != rkey.charAt(i)) count++;
        }
        return count;
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