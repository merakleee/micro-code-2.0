# Challenge 5 — Dead Star Drift

The ship has drifted into a dead star's gravity well. Adel must filter thousands of old transmissions to extract stable signals and compute calibration values to fire the thruster override.

## Input Format

```
2000–2500 lines, each either blank or in the format:
VALUE | MODE_S : MODE_R
```
- `VALUE` is a signed integer (may have leading zeros)
- `MODE_S` and `MODE_R` are single uppercase letters
- Whitespace around `|`, `:`, and edges is random
- Blank lines are noise — ignore them

## Parts

### Part 1 — Power Index

**Step 1: Filter stable signals**
Keep only lines where `MODE_S == MODE_R`. Count them as `S`.

**Step 2: Build bit sequence**
For each stable value, take its absolute value:
- If `0` → discard
- If prime → bit = `1`
- Otherwise → bit = `0`

This gives bit sequence of length `N`.

**Step 3: Compute Power Index**
Let `shift = S mod 7`. For each position `i` from 1 to N:
```
E = ((i + shift - 1) mod N) + 1
```
- If bit is `1` → add `E × E` to Power
- If bit is `0` → subtract `gcd(E, N)` from Power

Output the final Power Index.

---

### Part 2 — Entropy Score

**Step 1: Mutate the bit sequence**
Slide a window of 5 bits across the sequence (length `N - 4` windows).
For each window, pack bits into `W = b0×16 + b1×8 + b2×4 + b3×2 + b4`, then:
- If `W % 3 == 0` → emit `1`
- Else if `W % 5 == 0` → emit `0`
- Else → emit `(b0 + b1 + b2 + b3 + b4) % 2` (parity)

**Step 2: Find smallest period L**
Find the smallest `L` that divides the mutated sequence length where `M[i] == M[i mod L]` for all `i`.

**Step 3: Compute Entropy Score**
Sum all `(i + 1)` where `M[i] == 1`, then multiply by `L`.

---

## Example

**Input:**
```
  42 | A : A
 -17 | B : C
  00007 | D : D
  0 | E : E
  3  |  F :  F
```

**Part 1:**
- Stable signals: 42, 7, 0, 3 → S = 4
- Bits: 42→0, 7→1, 0→discard, 3→1 → `[0, 1, 1]`, N = 3
- shift = 4 mod 7 = 4
- Power = -gcd(2,3) + 9 + 1 = **9**

**Part 2:**
- Mutated sequence `[1, 0, 1, 0, 1, 0]` → L = 2
- Position sum = 1 + 3 + 5 = 9
- Entropy = 9 × 2 = **18**

## Solution Files

| File | Description |
|---|---|
| `part1.java` | Filters signals and computes Power Index |
| `part2.java` | Mutates bit sequence and computes Entropy Score |

## How to Run

```bash
javac part1.java && java part1
javac part2.java && java part2
```

> Input is read from `data.txt` in the same directory.