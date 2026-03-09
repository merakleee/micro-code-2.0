# Challenge 7 — The Children's Ward

Adel helps Dr. Mkouli simulate genetic code evolution in young patients to predict treatment needs.

## Input Format

```
P                  ← number of patient groups
seq1               ← 4 lines per group, each an 8-bit binary string
seq2
seq3
seq4
...                ← repeats P times
```

## Rules Per Generation (repeat 1000 times per group)

### Step 1: Selection
- Count `1` bits in each sequence → that's its **strength**
- **First** = sequence with highest strength (tie → higher index wins)
- **Second** = next highest (same tie rule, must be different from First)

### Step 2: Blend Point
```
split = (strengthFirst + strengthSecond) % 7 + 1
```
Always between 1 and 7.

### Step 3: Recombination
```
Offspring1 = First[0..split] + Second[split..8]
Offspring2 = Second[0..split] + First[split..8]
```

### Step 4: Radiation Strike
```
strike = (g × 3 + strengthFirst) % 8
```
Flip the bit at position `strike` in both offspring.

### Step 5: Array Rotation
- Form array `[First, Second, Offspring1, Offspring2]`
- Rotate right by `g % 4` positions
- Last k elements wrap to front: `[A,B,C,D]` → right by 1 → `[D,A,B,C]`
- This becomes the new population

### Record
After each generation, sum the decimal values of all 4 sequences and add to the group's treatment burden.

## Output

Sum the treatment burdens of all P groups and print as a single integer.

## Example (Generation 1)

**Population:** `[10100000, 00001010, 11100000, 00000111]`

| Step | Result |
|---|---|
| Strengths | 2, 2, 3, 3 |
| First | `00000111` (index 3), Second = `11100000` (index 2) |
| split | (3+3) % 7 + 1 = **7** |
| Offspring1 | `0000011` + `0` = `00000110` |
| Offspring2 | `1110000` + `1` = `11100001` |
| strike | (1×3 + 3) % 8 = **6** → flip bit 6 |
| After flip | `00000100`, `11100011` |
| Rotate right 1 | `[11100011, 00000111, 11100000, 00000100]` |
| Decimal sum | 227 + 7 + 224 + 4 = **462** |

## Solution Files

| File | Description |
|---|---|
| `part1.java` | Strength-based simulation |
| `part2.java` | Resemblance-based simulation with shifting reference key |

## How to Run

```bash
javac part1.java && java part1
javac part2.java && java part2
```

> Input is read from `data.txt` in the same directory.