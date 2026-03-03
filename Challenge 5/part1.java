import java.io.*;
import java.util.*;

class part1 {
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

            long powerIndex = computePowerIndex(bits, S);

            System.out.println("S = " + S);
            System.out.println("Power Index = " + powerIndex);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int[] signalsToBits(List<Long> signals) {
        List<Integer> bits = new ArrayList<>();

        for (long signal : signals) {
            long absVal = Math.abs(signal);

            if (absVal == 0) continue;

            if (isPrime(absVal)) {
                bits.add(1);
            } else {
                bits.add(0);
            }
        }

        return bits.stream().mapToInt(Integer::intValue).toArray();
    }

    public static boolean isPrime(long n) {
        if (n <= 1) return false;
        if (n <= 3) return true;
        if (n % 2 == 0 || n % 3 == 0) return false;

        for (long i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0) return false;
        }
        return true;
    }

    public static long computePowerIndex(int[] bits, long S) {
        int N = bits.length;
        if (N == 0) return 0;

        long shift = S % 7;
        long power = 0;

        for (int i = 1; i <= N; i++) {
            // E = ((i + shift - 1) mod N) + 1
            long E = ((i + shift - 1) % N + N) % N + 1;

            if (bits[i - 1] == 1) {
                power += E * E;
            } else {
                power -= gcd(E, N);
            }
        }

        return power;
    }

    public static long gcd(long a, long b) {
        while (b != 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
}