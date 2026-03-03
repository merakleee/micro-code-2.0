import java.io.*;
import java.util.*;

class part2 {
    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("data.txt"));
            String line;

            List<Long> stableValues = new ArrayList<>();
            long S = 0;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split("\\s*\\|\\s*");
                if (parts.length != 2) continue;

                long value = Long.parseLong(parts[0].trim());

                String[] modeParts = parts[1].split("\\s*:\\s*");
                if (modeParts.length != 2) continue;

                String MODE_S = modeParts[0].trim();
                String MODE_R = modeParts[1].trim();

                if (MODE_S.equals(MODE_R)) {
                    S++;
                    stableValues.add(value);
                }
            }
            br.close();

            int[] bits = signalsToBits(stableValues);
            int N = bits.length;

            int mutatedLen = N - 4;
            int[] mutated = new int[mutatedLen];

            for (int i = 0; i <= N - 5; i++) {
                int W = bits[i] * 16 + bits[i+1] * 8 + bits[i+2] * 4
                      + bits[i+3] * 2 + bits[i+4];

                if (W % 3 == 0) {
                    mutated[i] = 1;
                } else if (W % 5 == 0) {
                    mutated[i] = 0;
                } else {
                    mutated[i] = (bits[i] + bits[i+1] + bits[i+2]
                                + bits[i+3] + bits[i+4]) % 2;
                }
            }

            int L = findSmallestPeriod(mutated);

            long positionSum = 0;
            for (int i = 0; i < mutatedLen; i++) {
                if (mutated[i] == 1) {
                    positionSum += (i + 1);
                }
            }

            long entropy = positionSum * L;

            System.out.println("Entropy Score = " + entropy);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static int findSmallestPeriod(int[] seq) {
        int len = seq.length;
        for (int L = 1; L <= len; L++) {
            if (len % L != 0) continue;
            boolean valid = true;
            for (int i = 0; i < len; i++) {
                if (seq[i] != seq[i % L]) {
                    valid = false;
                    break;
                }
            }
            if (valid) return L;
        }
        return len;
    }

    static int[] signalsToBits(List<Long> signals) {
        List<Integer> bits = new ArrayList<>();
        for (long signal : signals) {
            long absVal = Math.abs(signal);
            if (absVal == 0) continue;
            bits.add(isPrime(absVal) ? 1 : 0);
        }
        return bits.stream().mapToInt(Integer::intValue).toArray();
    }

    static boolean isPrime(long n) {
        if (n <= 1) return false;
        if (n <= 3) return true;
        if (n % 2 == 0 || n % 3 == 0) return false;
        for (long i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0) return false;
        }
        return true;
    }

    static long gcd(long a, long b) {
        while (b != 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
}