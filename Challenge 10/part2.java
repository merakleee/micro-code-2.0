import java.io.*;
import java.util.*;

public class part2 {

    static final long INF = Long.MAX_VALUE / 2;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("data.txt"));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int N = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());

        int[] ea = new int[M], eb = new int[M], ew = new int[M];
        List<Integer>[] g  = new List[N];  // forward graph
        List<Integer>[] rg = new List[N];  // reverse graph

        for (int i = 0; i < N; i++) {
            g[i]  = new ArrayList<>();
            rg[i] = new ArrayList<>();
        }

        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            ea[i] = Integer.parseInt(st.nextToken());
            eb[i] = Integer.parseInt(st.nextToken());
            ew[i] = Integer.parseInt(st.nextToken());
            g[ea[i]].add(eb[i]);
            rg[eb[i]].add(ea[i]);
        }

        // Step 1: Kosaraju SCC 
        boolean[] vis = new boolean[N];
        List<Integer> order = new ArrayList<>();
        for (int u = 0; u < N; u++)
            if (!vis[u])
                dfs1(u, g, vis, order);

        int[] compId = new int[N];
        Arrays.fill(compId, -1);
        List<List<Integer>> comps = new ArrayList<>();

        for (int i = order.size() - 1; i >= 0; i--) {
            int u = order.get(i);
            if (compId[u] == -1) {
                int cid = comps.size();
                comps.add(new ArrayList<>());
                dfs2(u, rg, compId, comps, cid);
            }
        }

        int K = comps.size();

        // Step 2: Build per-SCC edge lists + condensation DAG
        List<int[]>[] compEdges = new List[K]; // internal edges per SCC
        for (int i = 0; i < K; i++) compEdges[i] = new ArrayList<>();

        Set<Long> crossSeen = new HashSet<>();
        List<int[]>[] dag = new List[K];
        int[] indeg = new int[K];
        for (int i = 0; i < K; i++) dag[i] = new ArrayList<>();

        for (int i = 0; i < M; i++) {
            int ca = compId[ea[i]];
            int cb = compId[eb[i]];
            if (ca == cb) {
                compEdges[ca].add(new int[]{ea[i], eb[i], ew[i]});
            } else {
                long key = (long) ca * K + cb;
                if (!crossSeen.contains(key)) {
                    crossSeen.add(key);
                    dag[ca].add(new int[]{cb});
                    indeg[cb]++;
                }
            }
        }

        // Step 3: Compute SCC weights (min internal cycle)
        long[] sccWeight = new long[K];
        for (int cid = 0; cid < K; cid++) {
            List<Integer> nodes = comps.get(cid);
            if (nodes.size() < 2) {
                sccWeight[cid] = 0;
                continue;
            }

            // Local relabeling
            int sz = nodes.size();
            Map<Integer, Integer> localIdx = new HashMap<>();
            for (int i = 0; i < sz; i++)
                localIdx.put(nodes.get(i), i);

            List<int[]>[] localAdj = new List[sz];
            for (int i = 0; i < sz; i++) localAdj[i] = new ArrayList<>();

            List<int[]> cedges = compEdges[cid];
            for (int i = 0; i < cedges.size(); i++) {
                int[] e = cedges.get(i);
                int la = localIdx.get(e[0]);
                int lb = localIdx.get(e[1]);
                localAdj[la].add(new int[]{lb, e[2], i});
            }

            long cyc = INF;
            for (int[] e : cedges) {
                int lu = localIdx.get(e[0]);
                int lv = localIdx.get(e[1]);
                int w  = e[2];
                long d = dijkstra(localAdj, sz, lv, lu, -1);
                if (d < INF) cyc = Math.min(cyc, w + d);
            }

            sccWeight[cid] = (cyc >= INF) ? 0 : cyc;
        }

        // Step 4: Topological sort of condensation DAG 
        Deque<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < K; i++)
            if (indeg[i] == 0) queue.add(i);

        List<Integer> topo = new ArrayList<>();
        while (!queue.isEmpty()) {
            int u = queue.poll();
            topo.add(u);
            for (int[] e : dag[u]) {
                int v = e[0];
                if (--indeg[v] == 0) queue.add(v);
            }
        }

        // Step 5: DP on DAG — longest path, tie-break by max weight sum
        long[] dpLen = new long[K];
        long[] dpW   = new long[K];
        Arrays.fill(dpLen, 1);
        for (int i = 0; i < K; i++) dpW[i] = sccWeight[i];

        for (int i = topo.size() - 1; i >= 0; i--) {
            int u = topo.get(i);
            long bestLen = 1;
            long bestW   = sccWeight[u];
            for (int[] e : dag[u]) {
                int v = e[0];
                long candLen = 1 + dpLen[v];
                long candW   = sccWeight[u] + dpW[v];
                if (candLen > bestLen
                        || (candLen == bestLen && candW > bestW)) {
                    bestLen = candLen;
                    bestW   = candW;
                }
            }
            dpLen[u] = bestLen;
            dpW[u]   = bestW;
        }

        // Step 6: Find answer 
        long ansLen = 0, ansW = Long.MIN_VALUE;
        for (int i = 0; i < K; i++) {
            if (dpLen[i] > ansLen
                    || (dpLen[i] == ansLen && dpW[i] > ansW)) {
                ansLen = dpLen[i];
                ansW   = dpW[i];
            }
        }

        System.out.println(ansW);
    }

    // Kosaraju DFS1: iterative post-order on forward graph 
    static void dfs1(int start, List<Integer>[] g, boolean[] vis, List<Integer> order) {
        Deque<int[]> stack = new ArrayDeque<>();
        stack.push(new int[]{start, 0});
        vis[start] = true;
        while (!stack.isEmpty()) {
            int[] cur = stack.peek();
            int u = cur[0], idx = cur[1];
            if (idx < g[u].size()) {
                cur[1]++;
                int v = g[u].get(idx);
                if (!vis[v]) {
                    vis[v] = true;
                    stack.push(new int[]{v, 0});
                }
            } else {
                stack.pop();
                order.add(u);
            }
        }
    }

    // Kosaraju DFS2: iterative DFS on reverse graph 
    static void dfs2(int start, List<Integer>[] rg, int[] compId,
                     List<List<Integer>> comps, int cid) {
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(start);
        compId[start] = cid;
        while (!stack.isEmpty()) {
            int u = stack.pop();
            comps.get(cid).add(u);
            for (int v : rg[u]) {
                if (compId[v] == -1) {
                    compId[v] = cid;
                    stack.push(v);
                }
            }
        }
    }

    // Dijkstra from src to dst, skipping bannedIdx (-1 = none)
    static long dijkstra(List<int[]>[] adj, int n, int src, int dst, int bannedIdx) {
        long[] dist = new long[n];
        Arrays.fill(dist, INF);
        dist[src] = 0;

        PriorityQueue<long[]> pq = new PriorityQueue<>(Comparator.comparingLong(x -> x[0]));
        pq.offer(new long[]{0, src});

        while (!pq.isEmpty()) {
            long[] cur = pq.poll();
            long d = cur[0];
            int u = (int) cur[1];
            if (d > dist[u]) continue;
            if (u == dst) return d;
            for (int[] e : adj[u]) {
                int v = e[0], w = e[1], idx = e[2];
                if (idx == bannedIdx) continue;
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
