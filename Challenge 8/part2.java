import java.io.*;

public class part2 {
    static char YOUR_SYMBOL;
    static char OPPONENT_SYMBOL;
    static int GRID_SIZE;

    public static void main(String[] args) throws IOException {
        BufferedReader data = new BufferedReader(new FileReader("data.txt"));
        String line = data.readLine();

        String[] parts = line.split(";");
        GRID_SIZE = Integer.parseInt(parts[0]);
        YOUR_SYMBOL = parts[1].charAt(0);
        int DEPTH = Integer.parseInt(parts[2]);
        OPPONENT_SYMBOL = (YOUR_SYMBOL == 'X') ? 'O' : 'X';
        String WEIGHTS = parts[3];

        // Parse all scenarios
        String[] entries = WEIGHTS.split("\\|");
        int[] W = new int[entries.length];
        int[] R = new int[entries.length];
        int[] C = new int[entries.length];
        for (int s = 0; s < entries.length; s++) {
            String[] subE = entries[s].split("-");
            W[s] = Integer.parseInt(subE[0]);
            String[] pos = subE[1].split(",");
            R[s] = Integer.parseInt(pos[0]);
            C[s] = Integer.parseInt(pos[1]);
        }

        // Read the board
        char[][] grid = new char[GRID_SIZE][GRID_SIZE];
        for (int i = 0; i < GRID_SIZE; i++) {
            line = data.readLine();
            for (int j = 0; j < GRID_SIZE; j++) {
                grid[i][j] = line.charAt(j);
            }
        }

        long bestWeightedTotal = Long.MIN_VALUE;
        int bestRow = -1, bestCol = -1;

        // For each candidate move
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (grid[i][j] != '.') continue;

                long weightedTotal = 0;

                // For each scenario
                for (int s = 0; s < entries.length; s++) {
                    // Skip if hidden cell is already occupied
                    if (grid[R[s]][C[s]] != '.') continue;

                    // Place hidden piece
                    grid[R[s]][C[s]] = OPPONENT_SYMBOL;

                    // Check 1: hidden piece gives opponent a win
                    if (checkWin(grid, OPPONENT_SYMBOL)) {
                        weightedTotal += (long) W[s] * -100;
                        grid[R[s]][C[s]] = '.';
                        continue;
                    }

                    // Check 2: candidate cell is occupied by hidden piece
                    if (R[s] == i && C[s] == j) {
                        weightedTotal += (long) W[s] * -100;
                        grid[R[s]][C[s]] = '.';
                        continue;
                    }

                    // Place your piece and simulate
                    grid[i][j] = YOUR_SYMBOL;
                    int outcome = minimax(grid, DEPTH - 1, false);
                    grid[i][j] = '.';

                    weightedTotal += (long) W[s] * outcome;

                    // Undo hidden piece
                    grid[R[s]][C[s]] = '.';
                }

                if (weightedTotal > bestWeightedTotal) {
                    bestWeightedTotal = weightedTotal;
                    bestRow = i;
                    bestCol = j;
                }
            }
        }

        System.out.println(bestRow * GRID_SIZE + bestCol);
    }

    static int minimax(char[][] grid, int depth, boolean isMaximizing) {
        if (checkWin(grid, YOUR_SYMBOL))     return 100;
        if (checkWin(grid, OPPONENT_SYMBOL)) return -100;
        if (depth == 0 || isFull(grid))      return 0;

        if (isMaximizing) {
            int best = Integer.MIN_VALUE;
            for (int i = 0; i < GRID_SIZE; i++)
                for (int j = 0; j < GRID_SIZE; j++)
                    if (grid[i][j] == '.') {
                        grid[i][j] = YOUR_SYMBOL;
                        best = Math.max(best, minimax(grid, depth - 1, false));
                        grid[i][j] = '.';
                    }
            return best;
        } else {
            int best = Integer.MAX_VALUE;
            for (int i = 0; i < GRID_SIZE; i++)
                for (int j = 0; j < GRID_SIZE; j++)
                    if (grid[i][j] == '.') {
                        grid[i][j] = OPPONENT_SYMBOL;
                        best = Math.min(best, minimax(grid, depth - 1, true));
                        grid[i][j] = '.';
                    }
            return best;
        }
    }

    static boolean checkWin(char[][] grid, char symbol) {
        for (int i = 0; i < GRID_SIZE; i++)
            for (int j = 0; j <= GRID_SIZE - 4; j++) {
                boolean win = true;
                for (int k = 0; k < 4; k++)
                    if (grid[i][j+k] != symbol) { win = false; break; }
                if (win) return true;
            }
        for (int i = 0; i <= GRID_SIZE - 4; i++)
            for (int j = 0; j < GRID_SIZE; j++) {
                boolean win = true;
                for (int k = 0; k < 4; k++)
                    if (grid[i+k][j] != symbol) { win = false; break; }
                if (win) return true;
            }
        for (int i = 0; i <= GRID_SIZE - 4; i++)
            for (int j = 0; j <= GRID_SIZE - 4; j++) {
                boolean win = true;
                for (int k = 0; k < 4; k++)
                    if (grid[i+k][j+k] != symbol) { win = false; break; }
                if (win) return true;
            }
        for (int i = 0; i <= GRID_SIZE - 4; i++)
            for (int j = 3; j < GRID_SIZE; j++) {
                boolean win = true;
                for (int k = 0; k < 4; k++)
                    if (grid[i+k][j-k] != symbol) { win = false; break; }
                if (win) return true;
            }
        return false;
    }

    static boolean isFull(char[][] grid) {
        for (int i = 0; i < GRID_SIZE; i++)
            for (int j = 0; j < GRID_SIZE; j++)
                if (grid[i][j] == '.') return false;
        return true;
    }
}