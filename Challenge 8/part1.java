import java.io.*;

public class part1 {
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

        char[][] grid = new char[GRID_SIZE][GRID_SIZE];
        for (int i = 0; i < GRID_SIZE; i++) {
            line = data.readLine();
            for (int j = 0; j < GRID_SIZE; j++) {
                grid[i][j] = line.charAt(j);
            }
        }

        int bestScore = Integer.MIN_VALUE;
        int bestRow = -1, bestCol = -1;

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (grid[i][j] == '.') {
                    grid[i][j] = YOUR_SYMBOL;
                    int score = minimax(grid, DEPTH - 1, false);
                    grid[i][j] = '.';
                    if (score > bestScore) {
                        bestScore = score;
                        bestRow = i;
                        bestCol = j;
                    }
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
        // Rows
        for (int i = 0; i < GRID_SIZE; i++)
            for (int j = 0; j <= GRID_SIZE - 4; j++) {
                boolean win = true;
                for (int k = 0; k < 4; k++)
                    if (grid[i][j+k] != symbol) { win = false; break; }
                if (win) return true;
            }
        // Columns
        for (int i = 0; i <= GRID_SIZE - 4; i++)
            for (int j = 0; j < GRID_SIZE; j++) {
                boolean win = true;
                for (int k = 0; k < 4; k++)
                    if (grid[i+k][j] != symbol) { win = false; break; }
                if (win) return true;
            }
        // Diagonals (top-left to bottom-right)
        for (int i = 0; i <= GRID_SIZE - 4; i++)
            for (int j = 0; j <= GRID_SIZE - 4; j++) {
                boolean win = true;
                for (int k = 0; k < 4; k++)
                    if (grid[i+k][j+k] != symbol) { win = false; break; }
                if (win) return true;
            }
        // Anti-diagonals (top-right to bottom-left)
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