# Challenge 9 — The Galactic Records

Adel helps LMKOULYIN hospitals locate patient records hidden as keywords inside a massive character matrix, then cross-reference each record with its nearest matching entry using toroidal distance.

## Input Format

```
TARGET: T        ← signed integer checksum
WORD: W          ← keyword to locate
MATRIX:          ← header line
row0             ← character matrix rows
row1
...
```

- Matrix dimensions: up to 600 × 600 characters
- Grid coordinates are 0-indexed

## Parts

### Part 1 — Keyword Path Lookup

Find the unique sequence of coordinates `[(R1,C1), ..., (RL,CL)]` that spells `WORD` character by character, satisfying:

**Column progression:** each character must be strictly to the right of the previous one
```
C1 < C2 < ... < CL
```

**Target checksum:**
```
(R1-C1) + (R2-C2) + ... + (RL-CL) = TARGET
```

There is exactly one valid sequence. Compute the positional hash using 1-based indices:
```
Hash = (R1+1)×(C1+1) + (R2+1)×(C2+1) + ... + (RL+1)×(CL+1)
```

Output the hash as a single integer.

### Part 2 — Nearest Twin Extraction

Using the coordinates from Part 1, for each coordinate `(Ri, Ci)` containing character `X`:

1. Find the nearest **twin** — another cell with the same character where:
   - It is not the original cell
   - It is not on column `0` or column `COLS-1`
2. Use **toroidal distance** (grid wraps around):
```
D = min(|r1-r2|, ROWS-|r1-r2|) + min(|c1-c2|, COLS-|c1-c2|)
```
3. **Tie-break:** lowest row index first, then lowest column index
4. Extract the twin's left and right neighbors: `matrix[r][c-1]` and `matrix[r][c+1]`

Concatenate all extracted pairs into a payload string of length `2L`, then compute:
```
Value = sum of ASCII(payload[k]) × k    for k = 1 to 2L
```

Output the value as a single integer.

## Example

```
TARGET: 1
WORD: AB
MATRIX:
xAxxB
xxBxA
AxBxx
```

**Part 1:** A at `(2,0)`, B at `(1,2)` → Hash = **9**

**Part 2:** Payload = `xxxx` → Value = **1200**

## Solution Files

| File | Description |
|---|---|
| `part1.java` | Keyword path lookup via DP with checksum |
| `part2.java` | Nearest twin extraction with toroidal distance |

## How to Run

```bash
javac part1.java && java part1
javac part2.java && java part2
```

> Input is read from `data.txt` in the same directory.