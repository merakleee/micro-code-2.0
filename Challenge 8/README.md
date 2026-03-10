# Challenge 8 — The Healing Game

Adel repairs a strategic board game terminal used by children in cognitive recovery. The terminal must find the optimal move using forward simulation.

## Input Format

```
GRID_SIZE;YOUR_SYMBOL;DEPTH;WEIGHTS   ← config line
row0                                   ← board rows (GRID_SIZE lines)
row1
...
```

- `GRID_SIZE`: 5 to 7 (board is GRID_SIZE × GRID_SIZE)
- `YOUR_SYMBOL`: `X` or `O` (opponent uses the other)
- `DEPTH`: total placements simulated including your first move
- `WEIGHTS`: scenario list for Part 2 (`W-R,C|W-R,C|...`)
- `.` = empty cell, `X` and `O` = placed pieces
- Position number = `row × GRID_SIZE + col`

## Win Condition

Four consecutive pieces in a row, column, or diagonal.

## Parts

### Part 1 — Minimax

Find your best move using minimax simulation:

- Try every empty cell as your candidate move
- Simulate `DEPTH` total placements with both sides playing optimally
- **+100** if you get 4 in a row, **-100** if opponent does, **0** if depth/full board reached
- Pick the move with the highest outcome
- **Tie-break:** lowest position number wins

### Part 2 — Tournament Mode (Weighted Scenarios)

Before each opponent turn, a hidden piece is placed somewhere on the board. You don't know where, but you have a weighted list of possibilities.

For each candidate move, compute a **weighted total** across all scenarios:

| Situation | Contribution |
|---|---|
| Hidden cell already occupied | skip scenario |
| Hidden piece gives opponent 4 in a row | `W × (-100)` |
| Hidden piece lands on your candidate cell | `W × (-100)` |
| Otherwise | `W × minimax outcome` |

Sum contributions across all scenarios → weighted total.
Pick the move with the highest weighted total. Tie-break: lowest position number.

## Example

```
5;X;2;3-0,0|7-0,4
.....
.....
.XXX.
.....
.....
```

**Part 1:** Playing at (2,0) or (2,4) both complete 4 in a row → both score +100 → tie → pick lowest position → **output: 10**

**Part 2:** Both moves score weighted total 1000 → tie → lowest position → **output: 10**

## Solution Files

| File | Description |
|---|---|
| `part1.java` | Minimax best move |
| `part2.java` | Weighted scenario best move |

## How to Run

```bash
javac part1.java && java part1
javac part2.java && java part2
```

> Input is read from `data.txt` in the same directory.