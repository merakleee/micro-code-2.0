# Challenge 1 — The Crescent Signal

A space-themed coding challenge where a crew must analyze 50,000 frequency readings to confirm the crescent moon's position and mark the beginning of Ramadan.

## Problem Summary

Given a target resonance `T` and a list of 50,000 frequency readings, find specific entries that sum to `T` and compute their product.

## Parts

### Part 1
Find **two** frequency readings that sum to exactly `T`. Output their product `A * B`.

### Part 2
Find **three** frequency readings that sum to exactly `T`, where the spread condition is met:
```
max(A, B, C) - min(A, B, C) >= 1000
```
Output their product `A * B * C`.

## Input Format

```
T
reading_1
reading_2
...
reading_50000
```

- Line 1: Target resonance `T`
- Lines 2–50001: 50,000 positive integer frequency readings, each smaller than `T`

## Example

**Input:**
```
10000
1200
4500
3300
5500
```

## Solution Files

| File | Description |
|------|-------------|
| `part1.java` | Finds the valid pair using a HashSet for O(n) lookup |
| `part2.java` | Finds the valid triplet using a HashSet for O(n²) lookup |

## How to Run

```bash
javac part1.java
java part1

javac part2.java
java part2
```

> Make sure `data.txt` is in the same directory as the compiled files.