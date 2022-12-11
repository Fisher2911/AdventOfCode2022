package day;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.LongConsumer;
import java.util.function.LongPredicate;
import java.util.function.LongUnaryOperator;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Day11 extends Day {

    public Day11() {
        super("day11.txt");
    }

    @Override
    public void part1() {
        final Map<Integer, Monkey> monkeyMap = this.parseMonkeyMap();
        this.doRounds(true, monkeyMap, 20);
        System.out.println("Part 1: " + this.calculateMonkeyBusiness(monkeyMap));
//        System.out.println(27 % 5);
    }

    @Override
    public void part2() {
        final Map<Integer, Monkey> monkeyMap = this.parseMonkeyMap();
        this.doRounds(false, monkeyMap, 10_000);
        System.out.println("Part 2: " + this.calculateMonkeyBusiness(monkeyMap));
    }

    private Map<Integer, Monkey> parseMonkeyMap() {
        return this.parseMonkeys(
                this.readLines()
                        .map(s -> s.replace("  ", "").replace("    ", ""))
                        .toList()
        );
    }

    private void doRounds(boolean divideBy3, Map<Integer, Monkey> monkeyMap, int totalRounds) {
        for (int i = 0; i < totalRounds; i++) {
            monkeyMap.values().forEach(monkey -> monkey.throwItems(monkeyMap, divideBy3));
        }
    }

    private long calculateMonkeyBusiness(Map<Integer, Monkey> monkeyMap) {
        final List<Monkey> topMonkeys = monkeyMap.values()
                .stream()
                .sorted((m1, m2) -> m2.totalInspected <= m1.totalInspected ? -1 : 1)
                .peek(m -> System.out.println(m.totalInspected))
                .limit(2)
                .collect(Collectors.toList());
        long current = 1;
        for (Monkey monkey : topMonkeys) {
            current *= monkey.totalInspected;
        }
        return current;
    }

    private Map<Integer, Monkey> parseMonkeys(List<String> lines) {
        final Map<Integer, Monkey> monkeyMap = new HashMap<>();
        final int linesPerMonkey = 7;
        for (int i = 0; i < lines.size(); i += linesPerMonkey) {
            final Monkey monkey = this.readMonkey(monkeyMap, lines, i);
            monkeyMap.put(monkey.id, monkey);
        }
        return monkeyMap;
    }

    private Monkey readMonkey(Map<Integer, Monkey> monkeyMap, List<String> lines, int index) {
        final int monkeyNum = Integer.parseInt(lines.get(index).replace(":", "").split("Monkey ")[1]);
        final List<Long> items = Arrays.stream(lines.get(index + 1).replace("Starting items: ", "").split(", ")).map(Long::parseLong).collect(Collectors.toList());
        final String[] operatorParts = lines.get(index + 2).split("Operation: new = ")[1].split(" ");
        final MonkeyLongUnaryOperator operator = this.parseOperator(
                operatorParts[1].charAt(0),
                operatorParts[2]
        );
        final int divisibleBy = Integer.parseInt(lines.get(index + 3).split("Test: divisible by ")[1]);
        final int throwToIfTrue = Integer.parseInt(lines.get(index + 4).split("If true: throw to monkey ")[1]);
        final int throwToIfFalse = Integer.parseInt(lines.get(index + 5).split("If false: throw to monkey ")[1]);
        return new Monkey(
                monkeyNum,
                items,
                operator,
                i -> i % divisibleBy == 0,
                throwToIfTrue,
                throwToIfFalse,
                divisibleBy
        );
    }

    private MonkeyLongUnaryOperator parseOperator(char c, String value) {
        final LongUnaryOperator valueOperator = i -> value.equals("old") ? i : Long.parseLong(value);
        return new MonkeyLongUnaryOperator(
                switch (c) {
                    case '+' -> (LongUnaryOperator) operand -> operand + valueOperator.applyAsLong(operand);
                    case '-' -> (LongUnaryOperator) operand -> operand - valueOperator.applyAsLong(operand);
                    case '*' -> (LongUnaryOperator) operand -> operand * valueOperator.applyAsLong(operand);
                    case '/' -> (LongUnaryOperator) operand -> operand / valueOperator.applyAsLong(operand);
                    default -> throw new IllegalArgumentException("Invalid Operator: " + c);
                },
                Operation.fromChar(c),
                valueOperator
        );
    }

    private static record MonkeyLongUnaryOperator(LongUnaryOperator operator, Operation operation,
                                                  LongUnaryOperator applied) {
    }

    private enum Operation {

        ADD,
        SUB,
        DIV,
        MULT;

        public static Operation fromChar(char c) {
            return switch (c) {
                case '+' -> ADD;
                case '-' -> SUB;
                case '/' -> DIV;
                case '*' -> MULT;
                default -> throw new IllegalArgumentException("Illegal operator: " + c);
            };
        }

    }

    private static class Monkey {

        private final int id;
        private final List<Long> items;
        private final MonkeyLongUnaryOperator operation;
        private final LongPredicate predicate;
        private int trueMonkey;
        private int falseMonkey;
        private final long divisibleBy;
        private long totalInspected;

        public Monkey(int id, List<Long> items, MonkeyLongUnaryOperator operation, LongPredicate predicate, int trueMonkey, int falseMonkey, long divisibleBy) {
            this.id = id;
            this.items = items;
            this.operation = operation;
            this.predicate = predicate;
            this.trueMonkey = trueMonkey;
            this.falseMonkey = falseMonkey;
            this.divisibleBy = divisibleBy;
        }

        public void throwItems(Map<Integer, Monkey> monkeyMap, boolean divideBy3) {
            final Iterator<Long> iterator = this.items.iterator();
            while (iterator.hasNext()) {
                final long item = iterator.next();
                final long worry = this.operation.operator().applyAsLong(item);
                if (!divideBy3) {
                    if (this.predicate.test(worry)) {
                        final Monkey monkey = monkeyMap.get(this.trueMonkey);
                        monkey.receive(findLowerNum(monkeyMap, item, worry, monkey));
                    } else {
                        final Monkey monkey = monkeyMap.get(this.falseMonkey);
                        monkey.receive(findLowerNum(monkeyMap, item, worry, monkey));
                    }
                } else {
                    final long newValue = worry / 3;
                    if (this.predicate.test(newValue)) {
                        final Monkey monkey = monkeyMap.get(this.trueMonkey);
                        monkey.receive(findLowerNum(monkeyMap, item, newValue, monkey));
                    } else {
                        final Monkey monkey = monkeyMap.get(this.falseMonkey);
                        monkey.receive(findLowerNum(monkeyMap, item, newValue, monkey));
                    }
                }
                iterator.remove();
                this.totalInspected++;
            }
        }

        private long findHighestFactor(long toFind) {
            for (long l = toFind / 2; l > 0; l--) {
                if (toFind % l == 0) {
                    return l;
                }
            }
            return toFind;
        }

        private long findLowerNum(Map<Integer, Monkey> monkeyMap, long original, long newValue, Monkey next) {
//            final long thisRemainder = newValue % this.divisibleBy;
//            final long nextLong = next.operation.operator().applyAsLong(newValue);
//            final long nextRemainder = nextLong % next.divisibleBy;
//            if (next.operation.operation != Operation.MULT) return newValue;
//            for (long l = original; l < newValue; l++) {
//                if (l % this.divisibleBy == thisRemainder && next.operation.operator().applyAsLong(l) % next.divisibleBy == nextRemainder) {
//                    return l;
//                }
//            }
            return newValue;
        }

        public void receive(long item) {
            this.items.add(item);
        }

        @Override
        public String toString() {
            return "Monkey{" +
                    "id=" + id +
                    ", items=" + items +
                    '}';
        }
    }
}
