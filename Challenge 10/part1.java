import java.io.*;
import java.util.*;

public class part1 {

    static final long INF = Long.MAX_VALUE / 2;
    static int N;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("data.txt"));

        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());

        int[] ea = new int[M], eb = new int[M], ew = new int[M];

        // adj[u] = list of {v, weight, edgeIndex}
        List<int[]>[] adj = new List[N];
        for (int i = 0; i < N; i++) adj[i] = new ArrayList<>();

        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            int w = Integer.parseInt(st.nextToken());
            ea[i] = a; eb[i] = b; ew[i] = w;
            adj[a].add(new int[]{b, w, i});
        }

        long best = INF;

        // Case 1: No reversal
        
        for (int i = 0; i < M; i++) {
            int u = ea[i], v = eb[i], w = ew[i];
            long d = dijkstra(adj, N, v, u, -1);
            if (d < INF) {
                best = Math.min(best, w + d);
            }
        }

        // Case 2: Reverse exactly one edge idx
        
        for (int i = 0; i < M; i++) {
            int u = ea[i], v = eb[i], w = ew[i];
            long d = dijkstra(adj, N, u, v, i);
            if (d < INF) {
                best = Math.min(best, w + d);
            }
        }

        System.out.println(best);
    }

    static long dijkstra(List<int[]>[] adj, int n, int src, int dst, int bannedIdx) {
        long[] dist = new long[n];
        Arrays.fill(dist, INF);
        dist[src] = 0;

        // PriorityQueue: {distance, node}
        PriorityQueue<long[]> pq = new PriorityQueue<>(Comparator.comparingLong(x -> x[0]));
        pq.offer(new long[]{0, src});

        while (!pq.isEmpty()) {
            long[] cur = pq.poll();
            long d = cur[0];
            int u = (int) cur[1];

            if (d > dist[u]) continue;
            if (u == dst) return d;

            for (int[] edge : adj[u]) {
                int v   = edge[0];
                int w   = edge[1];
                int idx = edge[2];

                if (idx == bannedIdx) continue; // skip banned edge

                long nd = dist[u] + w;
                if (nd < dist[v]) {
                    dist[v] = nd;
                    pq.offer(new long[]{nd, v});
                }
            }
        }

        return INF;
    }
}
