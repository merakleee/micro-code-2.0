import java.io.*;
import java.util.*;

public class part1 {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("data.txt"));

        int TARGET = Integer.parseInt(reader.readLine().split(":")[1].trim());
        String WORD = reader.readLine().split(":")[1].trim();
        reader.readLine(); // skip "MATRIX:"

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

        Map<Integer, int[]>[] dp = new HashMap[COLS];
        for (int c = 0; c < COLS; c++)
            dp[c] = new HashMap<>();

        // Base case: first letter
        for (int r = 0; r < ROWS; r++) {
            if (matrix[r][0] == WORD.charAt(0)) {
                int checksum = r - 0;
                dp[0].put(checksum, new int[]{r, -1, -1});
            }
        }
        for (int c = 1; c < COLS; c++) {
            if (matrix[0][c] == WORD.charAt(0)) { 
            }
        }
    
        Map<Integer, int[]>[] prev = new HashMap[COLS];
        for (int c = 0; c < COLS; c++) prev[c] = new HashMap<>();

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (matrix[r][c] == WORD.charAt(0)) {
                    int cs = r - c;
                    if (!prev[c].containsKey(cs))
                        prev[c].put(cs, new int[]{r, -1, 0});
                }
            }
        }

        for (int idx = 1; idx < L; idx++) {

            Map<Integer, int[]>[] curr = new HashMap[COLS];
            for (int c = 0; c < COLS; c++) curr[c] = new HashMap<>();

            for (int c = 0; c < COLS; c++) {
                if (matrix[0][c] != WORD.charAt(idx) &&
                    !hasChar(matrix, c, WORD.charAt(idx), ROWS)) continue;
                for (int r = 0; r < ROWS; r++) {
                    if (matrix[r][c] != WORD.charAt(idx)) continue;
                    int delta = r - c;
                    for (int pc = 0; pc < c; pc++) {
                        for (Map.Entry<Integer, int[]> e : prev[pc].entrySet()) {
                            int newCs = e.getKey() + delta;
                            if (!curr[c].containsKey(newCs))
                                curr[c].put(newCs, new int[]{r, pc, e.getKey()});
                        }
                    }
                }
            }
            prev = curr;
        }

        int endCol = -1, endRow = -1, endPrevCol = -1, endPrevCs = -1;
        outer:
        for (int c = 0; c < COLS; c++) {
            if (prev[c].containsKey(TARGET)) {
                int[] val = prev[c].get(TARGET);
                endRow = val[0]; endCol = c;
                endPrevCol = val[1]; endPrevCs = val[2];
                break outer;
            }
        }

        int[][] path = new int[L][2];

        path[L-1] = new int[]{endRow, endCol};
        int curCol = endPrevCol;
        int curCs = endPrevCs;

        Map<Integer, int[]>[][] dpFull = new HashMap[L][COLS];
        for (int i = 0; i < L; i++)
            for (int c = 0; c < COLS; c++)
                dpFull[i][c] = new HashMap<>();

        for (int r = 0; r < ROWS; r++)
            for (int c = 0; c < COLS; c++)
                if (matrix[r][c] == WORD.charAt(0))
                    dpFull[0][c].put(r - c, new int[]{r, -1, 0});

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

        int[] cur = null;
        int ec = -1;
        for (int c = 0; c < COLS; c++) {
            if (dpFull[L-1][c].containsKey(TARGET)) {
                cur = dpFull[L-1][c].get(TARGET);
                ec = c;
                break;
            }
        }

        path[L-1] = new int[]{cur[0], ec};
        int traceCol = cur[1];
        int traceCs = cur[2];
        for (int idx = L-2; idx >= 0; idx--) {
            int[] node = dpFull[idx][traceCol].get(traceCs);
            path[idx] = new int[]{node[0], traceCol};
            traceCol = node[1];
            traceCs = node[2];
        }

        // Compute hash
        long hash = 0;
        for (int i = 0; i < L; i++)
            hash += (long)(path[i][0] + 1) * (path[i][1] + 1);

        System.out.println(hash);
    }

    static boolean hasChar(char[][] matrix, int col, char ch, int rows) {
        for (int r = 0; r < rows; r++)
            if (matrix[r][col] == ch) return true;
        return false;
    }
}