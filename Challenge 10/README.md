# Challenge 10 — The Last Transmission

Adel routes scientific data through a directed relay network back to Earth before Ramadan ends. He must find the cheapest feedback loop to calibrate the signal, then decompose the network into clusters and find the strongest transmission chain.

## Input Format

```
N M              ← number of nodes and edges
A B W            ← directed edge from A to B with weight W
...              ← M lines total
```

- Nodes labeled `0` to `N-1`, all weights are positive integers, edges are one-way

## Parts

### Part 1 — Minimum Cycle with One Reversal

Find the minimum total weight of any directed cycle. You may reverse **at most one edge** (flip A→B to B→A, keeping weight W).

- Find the minimum cycle using original directions
- For each edge A→B(W), try reversing it and find the shortest path from A back to B in the remaining graph. Cycle weight = W + path weight
- Output the overall minimum

### Part 2 — Longest SCC Chain

**Find all SCCs:** Partition the graph into Strongly Connected Components — maximal groups where every node can reach every other.

**Compute SCC weights:** For each SCC with 2+ nodes, find its minimum internal directed cycle (no reversals). Single-node SCCs have weight 0.

**Build condensation DAG:** Each SCC becomes one node. Add an edge from SCC X to SCC Y if any original edge crosses between them (one edge per pair).

**Find longest path:** Find the path through the most SCCs. Tie-break by largest sum of SCC weights along the path.

Output the total SCC weight sum of the winning path.

## Example

```
6 8
0 1 5
1 2 5
2 0 50
0 2 20
3 4 4
4 5 4
5 3 40
1 3 1
```

**Part 1:** Reverse `0→2(20)` → cycle weight = 20 + 10 = **30**

**Part 2:**

| SCC | Nodes | Min Cycle |
|---|---|---|
| A | {0, 1, 2} | 60 |
| B | {3, 4, 5} | 48 |

Condensation: A → B. Longest path = 2 SCCs. Output: 60 + 48 = **108**

## Solution Files

| File | Description |
|---|---|
| `part1.java` | Minimum cycle with at most one edge reversal |
| `part2.java` | SCC decomposition and longest weighted chain |

## How to Run

```bash
javac part1.java && java part1
javac part2.java && java part2
```

> Input is read from `data.txt` in the same directory.