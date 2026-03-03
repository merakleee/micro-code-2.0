# Challenge 4 — The Iftar Table

Crew members sit around a circular dining table and need to cook dishes in rotation. The challenge is to schedule everyone optimally given space and fatigue constraints.

## Input Format

```
dish1,dish2,dish3,...   ← dishes each person must cook (clockwise order)
K                       ← cooldown rounds (used in Part 2)
```

The table is **circular**: the first and last person are neighbors.

## Parts

### Part 1 — Adjacency Constraint
Each round, pick any group of **non-adjacent** crew members. Each picked person cooks one dish. Find the **minimum number of rounds** to finish everything.

**Three lower bounds — answer is the max of all three:**
1. `max(dishes)` — the busiest person needs at least this many rounds
2. `ceil(total / floor(n/2))` — at most `floor(n/2)` people can cook per round
3. `max(dishes[i] + dishes[i+1])` — two neighbors can never cook together, so their combined dishes is a hard lower bound

### Part 2 — Adjacency + Cooldown
Same as Part 1, but after cooking, a crew member must **wait K rounds** before cooking again.

**Additional lower bound:**
- A person with `D` dishes and cooldown `K` needs at least `1 + (D-1) × (K+1)` rounds

**Answer = max of all four bounds.**

## Example

**Input:**
```
3,2,1,4,2
1
```

**Part 1:** Answer = `6`
- max dishes = 4, throughput = ceil(12/2) = 6, max adjacent pair = 4+2 = 6
- max(4, 6, 6) = **6**

**Part 2:** Answer = `7`
- Person 3 has 4 dishes with K=1: needs 1 + 3×2 = 7 rounds
- max(6, 7) = **7**

## Solution Files

| File | Description |
|---|---|
| `part1.java` | Solves adjacency-only scheduling |
| `part2.java` | Solves adjacency + cooldown scheduling |

## How to Run

```bash
javac part1.java && java part1
javac part2.java && java part2
```

> Input is read from `data.txt` in the same directory.