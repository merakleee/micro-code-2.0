import java.io.*;
import java.util.*;

public class part1 {

    // 32-bit unsigned behavior via masking
    private static final long MASK32 = 0xFFFF_FFFFL;

    public static void main(String[] args) throws Exception {
        List<String> lines = readLinesFromFile("data.txt");
        Deque<Long> stack = new ArrayDeque<>();

        for (String rawLine : lines) {
            String line = rawLine.trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\s+");
            String instr = parts[0];

            switch (instr) {
                case "push": {
                    if (parts.length != 2)
                        throw new IllegalArgumentException("push requires 1 argument: " + line);
                    long v = Long.parseUnsignedLong(parts[1]);
                    v &= MASK32;
                    stack.push(v);
                    break;
                }
                case "pop": {
                    requireSize(stack, 1, line);
                    stack.pop();
                    break;
                }
                case "dup": {
                    requireSize(stack, 1, line);
                    long a = stack.peek();
                    stack.push(a);
                    break;
                }
                case "dup2": {
                    requireSize(stack, 2, line);
                    long b = stack.pop();
                    long a = stack.pop();
                    // [... A B] -> [... A B A B]
                    stack.push(a);
                    stack.push(b);
                    stack.push(a);
                    stack.push(b);
                    break;
                }
                case "dup3": {
                    requireSize(stack, 3, line);
                    long c = stack.pop();
                    long b = stack.pop();
                    long a = stack.pop();
                    // [... A B C] -> [... A B C A B C]
                    stack.push(a);
                    stack.push(b);
                    stack.push(c);
                    stack.push(a);
                    stack.push(b);
                    stack.push(c);
                    break;
                }
                case "rol": {
                    if (parts.length != 2)
                        throw new IllegalArgumentException("rol requires 1 argument: " + line);
                    int n = Integer.parseInt(parts[1]);
                    if (n <= 0)
                        throw new IllegalArgumentException("rol N must be > 0: " + line);
                    requireSize(stack, n, line);

                    // Pop top N into temp: temp[0] = original top
                    List<Long> temp = new ArrayList<>(n);
                    for (int i = 0; i < n; i++) {
                        temp.add(stack.pop());
                    }
                    // Move original top to bottom of these N elements
                    Long topElem = temp.remove(0);   // original top
                    temp.add(topElem);               // now original top is bottom

                    // Push back so that last in temp is the new top of stack
                    for (int i = temp.size() - 1; i >= 0; i--) {
                        stack.push(temp.get(i));
                    }
                    break;
                }
                case "xor": {
                    binaryOp(stack, line, (a, b) -> (a ^ b) & MASK32);
                    break;
                }
                case "or": {
                    binaryOp(stack, line, (a, b) -> (a | b) & MASK32);
                    break;
                }
                case "and": {
                    binaryOp(stack, line, (a, b) -> (a & b) & MASK32);
                    break;
                }
                case "sub": {
                    binaryOp(stack, line, (a, b) -> (a - b) & MASK32);
                    break;
                }
                case "sum": {
                    binaryOp(stack, line, (a, b) -> (a + b) & MASK32);
                    break;
                }
                case "inc": {
                    requireSize(stack, 1, line);
                    long a = stack.pop();
                    a = (a + 1) & MASK32;
                    stack.push(a);
                    break;
                }
                case "dec": {
                    requireSize(stack, 1, line);
                    long a = stack.pop();
                    a = (a - 1) & MASK32;
                    stack.push(a);
                    break;
                }
                case "not": {
                    requireSize(stack, 1, line);
                    long a = stack.pop();
                    a = (~a) & MASK32;
                    stack.push(a);
                    break;
                }
                case "shl": {
                    binaryOp(stack, line, (a, b) -> (a << (b & 31)) & MASK32);
                    break;
                }
                case "shr": {
                    // unsigned right shift
                    binaryOp(stack, line, (a, b) -> (a >>> (b & 31)) & MASK32);
                    break;
                }
                default:
                    throw new IllegalArgumentException("Unknown instruction: " + line);
            }
        }

        // XOR-reduction of final stack
        long result = 0;
        for (long v : stack) {
            result ^= v;
            result &= MASK32;
        }

        // Print as unsigned decimal
        System.out.println(Long.toUnsignedString(result));
    }

    private static void requireSize(Deque<Long> st, int n, String context) {
        if (st.size() < n) {
            throw new IllegalStateException("Stack underflow (" + n + " needed) at: " + context);
        }
    }

    private interface BinOp {
        long apply(long a, long b);
    }

    private static void binaryOp(Deque<Long> stack, String ctx, BinOp op) {
        requireSize(stack, 2, ctx);
        long b = stack.pop(); // top
        long a = stack.pop(); // below
        long r = op.apply(a, b);
        stack.push(r);
    }

    private static List<String> readLinesFromFile(String filename) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String s;
            while ((s = br.readLine()) != null) {
                lines.add(s);
            }
        }
        return lines;
    }
}
