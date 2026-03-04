import java.util.*;
import java.io.*;

public class part2 {
    static final long MOD = 1000000007L;
    static List<Integer>[] adj;
    static int[] depth;
    static int[] subtreeSize;
    static List<Integer>[] children;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("data.txt"));
        int N = Integer.parseInt(br.readLine().trim());

        adj = new List[N];
        children = new List[N];
        depth = new int[N];
        subtreeSize = new int[N];
        for (int i = 0; i < N; i++) {
            adj[i] = new ArrayList<>();
            children[i] = new ArrayList<>();
        }

        for (int i = 0; i < N - 1; i++) {
            String[] parts = br.readLine().trim().split("\\s+");
            int P = Integer.parseInt(parts[0]);
            int C = Integer.parseInt(parts[1]);
            adj[P].add(C);
        }
        br.close();

        // Iterative DFS to compute depth and subtree sizes
        Deque<int[]> stack = new ArrayDeque<>();
        stack.push(new int[]{0, -1, 0}); // node, parent, visited
        while (!stack.isEmpty()) {
            int[] top = stack.pop();
            int node = top[0], parent = top[1], visited = top[2];
            if (visited == 1) {
                for (int child : adj[node]) {
                    if (child != parent) {
                        subtreeSize[node] += subtreeSize[child];
                    }
                }
            } else {
                stack.push(new int[]{node, parent, 1});
                for (int child : adj[node]) {
                    if (child != parent) {
                        depth[child] = depth[node] + 1;
                        children[node].add(child);
                        stack.push(new int[]{child, node, 0});
                    }
                }
                subtreeSize[node] = 1;
            }
        }

        long S = 0;
        for (int v = 0; v < N; v++) {
            long k = subtreeSize[v];
            long pairsV = (k * (k - 1) / 2) % MOD;

            long pairsChildren = 0;
            for (int c : children[v]) {
                long kc = subtreeSize[c];
                pairsChildren = (pairsChildren + kc * (kc - 1) / 2) % MOD;
            }

            long lcaPairs = (pairsV - pairsChildren + MOD) % MOD;
            S = (S + depth[v] % MOD * lcaPairs) % MOD;
        }

        System.out.println(S);
    }
}
