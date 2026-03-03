# Challenge 3 — The Lost Recipe

Adel needs to recover a crashed signal processor by replaying its instruction log to restore the communication signal before iftar.

## Problem Summary

Simulate a stack-based processor that operates on **32-bit unsigned integers**. Execute all instructions, then XOR all remaining stack values together to get the output.

## Input Format

A text file where each line is one instruction.

## Instructions

| Instruction | Description |
|---|---|
| `push V` | Push value V onto the stack |
| `pop` | Remove top element |
| `dup` | Copy top element |
| `dup2` | Copy top 2: `[A B]` → `[A B A B]` |
| `dup3` | Copy top 3: `[A B C]` → `[A B C A B C]` |
| `rol N` | Rotate top N: `[A B C D]` → `[D A B C]` |
| `xor` | A XOR B |
| `or` | A OR B |
| `and` | A AND B |
| `sub` | A - B |
| `sum` | A + B |
| `inc` | Top + 1 |
| `dec` | Top - 1 |
| `not` | Bitwise NOT of top |
| `shl` | A << B |
| `shr` | A >>> B (unsigned) |

For all binary ops: **B is popped first (top), then A (below).**

## Parts

### Part 1 — Basic Simulation
Execute all instructions. Output the XOR of all remaining stack values.

### Part 2 — Dynamic Reactions
Same as Part 1, but after every binary operation, check the result T:

1. If **bit 31 is set** → reverse the entire stack
2. Else if **bit 0 is set** → next binary op swaps its operands (`B - A` instead of `A - B`)

The two checks are mutually exclusive. The flag is consumed after one use.

## Important Notes

- All values are **32-bit unsigned**: use `& 0xFFFFFFFF` after every operation
- `shl` and `shr` use `shift % 32` to avoid shifting by more than 31 bits
- Wrap-around on `sub` and `dec` is handled by `% 2^32`

## Example

```
push 10   → [10]
push 20   → [10, 20]
xor       → 10 XOR 20 = 30 → [30]
push 5    → [30, 5]
sub       → 30 - 5 = 25 → [25]
Output: 25
```

## How to Run

```bash
javac part1.java && java part1
javac part2.java && java part2
```

> Instructions are read from `data.txt` in the same directory.