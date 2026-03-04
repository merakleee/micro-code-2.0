import java.util.*;
import java.io.*;

public class part1 {
    static final long MOD = 1000000007L;
    static List<Integer>[] adj;
    static List<Integer>[] descendants;
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("data.txt"));
        
        int N = Integer.parseInt(br.readLine().trim());
        
        adj = new List[N];
        descendants = new List[N];
        for (int i = 0; i < N; i++) {
            adj[i] = new ArrayList<>();
            descendants[i] = new ArrayList<>();
        }
        
        for (int i = 0; i < N - 1; i++) {
            String[] parts = br.readLine().trim().split("\\s+");
            int P = Integer.parseInt(parts[0]);
            int C = Integer.parseInt(parts[1]);
            adj[P].add(C);
        }
        
        dfs(0, -1);
        
        long result = 0;
        for (int i = 0; i < N; i++) {
            for (int j : descendants[i]) {
                long diff = (j - i);
                long contrib = (diff * diff) % MOD;
                result = (result + contrib) % MOD;
            }
        }
        
        System.out.println(result);
    }
    
    static void dfs(int node, int parent) {
        descendants[node].clear();
        
        for (int child : adj[node]) {
            if (child != parent) {
                dfs(child, node);

                descendants[node].addAll(descendants[child]);
    
                descendants[node].add(child);
            }
        }
    }
}
