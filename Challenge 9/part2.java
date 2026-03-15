import java.io.*;
import java.util.*;

public class part2 {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("data.txt"));

        int TARGET = Integer.parseInt(reader.readLine().split(":")[1].trim());
        String WORD = reader.readLine().split(":")[1].trim();
        reader.readLine(); 

        List<String> matrixLines = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            if (!line.isEmpty()) matrixLines.add(line);
        }
        reader.close();

        int ROWS = matrixLines.size();
        int COLS = matrixLines.get(0).length();
        char[][] matrix = new char[ROWS][COLS];
        for (int i = 0; i < ROWS; i++)
            matrix[i] = matrixLines.get(i).toCharArray();

        int L = WORD.length();

        Map<Integer, int[]>[][] dpFull = new HashMap[L][COLS];
        for (int i = 0; i < L; i++)
            for (int c = 0; c < COLS; c++)
                dpFull[i][c] = new HashMap<>();

        // Base case: first character
        for (int r = 0; r < ROWS; r++)
            for (int c = 0; c < COLS; c++)
                if (matrix[r][c] == WORD.charAt(0))
                    dpFull[0][c].put(r - c, new int[]{r, -1, 0});

        // Fill DP for remaining characters
        for (int idx = 1; idx < L; idx++)
            for (int c = 0; c < COLS; c++)
                for (int r = 0; r < ROWS; r++) {
                    if (matrix[r][c] != WORD.charAt(idx)) continue;
                    for (int pc = 0; pc < c; pc++)
                        for (Map.Entry<Integer, int[]> e : dpFull[idx-1][pc].entrySet()) {
                            int newCs = e.getKey() + (r - c);
                            if (!dpFull[idx][c].containsKey(newCs))
                                dpFull[idx][c].put(newCs, new int[]{r, pc, e.getKey()});
                        }
                }

        // Find end of path
        int[] cur = null;
        int ec = -1;
        for (int c = 0; c < COLS; c++) {
            if (dpFull[L-1][c].containsKey(TARGET)) {
                cur = dpFull[L-1][c].get(TARGET);
                ec = c;
                break;
            }
        }

        // Backtrack to reconstruct full path
        int[][] path = new int[L][2];
        path[L-1] = new int[]{cur[0], ec};
        int traceCol = cur[1];
        int traceCs  = cur[2];
        for (int idx = L-2; idx >= 0; idx--) {
            int[] node = dpFull[idx][traceCol].get(traceCs);
            path[idx]  = new int[]{node[0], traceCol};
            traceCol   = node[1];
            traceCs    = node[2];
        }

        StringBuilder payload = new StringBuilder();

        for (int i = 0; i < L; i++) {
            int r1 = path[i][0];
            int c1 = path[i][1];
            char ch = matrix[r1][c1];

            int bestR    = -1;
            int bestC    = -1;
            int bestDist = Integer.MAX_VALUE;

            for (int r2 = 0; r2 < ROWS; r2++) {
                for (int c2 = 0; c2 < COLS; c2++) {
                    // Skip self
                    if (r2 == r1 && c2 == c1) continue;
                    // Must match character
                    if (matrix[r2][c2] != ch) continue;
                    // Border constraint: twin cannot be on col 0 or col COLS-1
                    if (c2 == 0 || c2 == COLS - 1) continue;

                    // Toroidal distance
                    int dr   = Math.abs(r1 - r2);
                    int dc   = Math.abs(c1 - c2);
                    int dist = Math.min(dr, ROWS - dr) + Math.min(dc, COLS - dc);

                    // Pick lowest dist; tie-break: lowest row, then lowest col
                    if (dist < bestDist
                            || (dist == bestDist && r2 < bestR)
                            || (dist == bestDist && r2 == bestR && c2 < bestC)) {
                        bestDist = dist;
                        bestR    = r2;
                        bestC    = c2;
                    }
                }
            }

            // Extract left and right neighbors of the twin
            payload.append(matrix[bestR][bestC - 1]);
            payload.append(matrix[bestR][bestC + 1]);
        }

        // Weighted ASCII sum
        long value = 0;
        for (int k = 1; k <= payload.length(); k++) {
            value += (long) payload.charAt(k - 1) * k;
        }

        System.out.println(value);
    }
}
