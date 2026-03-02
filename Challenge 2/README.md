# Challenge 2 — The Fasting Formula

A space-themed coding challenge where a crew must compute precise fasting windows using deeply nested compound energy formulas.

## Problem Summary

Given a formula string with base elements and nested parenthesized groups, recursively evaluate the total energy from the inside out.

## Energy Values

| Element | Energy |
|---------|--------|
| `A` | 247 |
| `B` | 383 |
| `C` | 156 |
| `D` | 512 |

## Syntax

- `(…){N}` means the contents inside the parentheses are multiplied by `N`
- Parentheses can be nested up to 50 levels deep
- Every `(` has a matching `)` immediately followed by `{N}`

## Parts

### Part 1 — Basic Evaluation
Parse the formula and compute the total energy by evaluating nested groups recursively (innermost first). Output the total energy as a single integer.

### Part 2 — Collapse Rule
Same as Part 1, but with a stabilization rule applied at every bracket closure:

1. Compute internal energy inside `(...)`
2. If internal energy **> 1,000** → replace with `internal_energy mod 1,000`
3. Multiply by `N`
4. Add to the parent chain

Output the final stabilized energy as a single integer.

## Example

**Input:**
```
A(B(C){4}){2}D
```

**Part 1 Walkthrough:**
1. `C` = 156
2. `(C){4}` = 156 × 4 = 624
3. `B(C){4}` = 383 + 624 = 1007
4. `(B(C){4}){2}` = 1007 × 2 = 2014
5. Total: 247 + 2014 + 512 = **2773**

**Part 2 Walkthrough:**
1. `(C){4}`: internal = 156, not > 1000 → 156 × 4 = 624
2. `(B(C){4}){2}`: internal = 383 + 624 = 1007, > 1000 → 1007 mod 1000 = 7 → 7 × 2 = 14
3. Total: 247 + 14 + 512 = **773**

## Solution Files

| File | Description |
|------|-------------|
| `part1.java` | Recursive formula evaluator |
| `part2.java` | Recursive evaluator with collapse rule |

## How to Run

```bash
javac part1.java
java part1

javac part2.java
java part2
```

> The formula is read from `data.txt` in the same directory.