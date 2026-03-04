# Challenge 6 — The Alien Tongue

After a crash landing on an alien planet, Adel must analyze a linguistic tree structure to power a translation matrix and establish communication with the LMKOULYIN.

## Input Format

```
N                  ← number of nodes (words), labeled 0 to N-1
P C                ← node P is the parent of node C
P C
...                ← N-1 lines total
```

- Tree is rooted at node 0
- Each node has at most two children
- Parent always appears before its children in the input

## Parts

### Part 1 — Ancestor Matrix

Build an ancestor matrix where `mat[i][j] = 1` if node `i` is an ancestor of node `j` (a node is NOT its own ancestor here).

Compute:
```
result = sum of mat[i][j] × (j - i)²   for all 0 <= i, j < N
```

Output `result mod 1,000,000,007`.

**Efficient approach:** for each node `i`, loop over all its descendants `j` and add `(j - i)²`. Use DFS to collect descendants in one pass.

### Part 2 — LCA Depth Sum

The LCA of two nodes `i` and `j` is the deepest node that is an ancestor of both. A node is its own ancestor (`LCA(i, i) = i`).

Compute:
```
S = sum of depth(LCA(i, j))   for all 0 <= i < j < N
```

Output `S mod 1,000,000,007`.

**Efficient approach:** for each node `v`, count how many pairs have `v` as their LCA:
```
pairs_where_LCA_is_v = C(subtree_size[v], 2) - sum of C(subtree_size[child], 2)
```
Multiply by `depth[v]` and sum over all nodes.

## Example

**Tree:**
```
        0          ← depth 0
       / \
      1   2        ← depth 1
     / \
    3   4          ← depth 2
```

**Part 1:** result = 1 + 4 + 9 + 16 + 4 + 9 = **43**

**Part 2:** S = 0+0+0+0+0+1+1+0+0+1 = **3**

## Solution Files

| File | Description |
|---|---|
| `part1.java` | Computes the compressed ancestor value |
| `part2.java` | Computes the LCA depth sum |

## How to Run

```bash
javac part1.java && java part1
javac part2.java && java part2
```

> Input is read from `data.txt` in the same directory.