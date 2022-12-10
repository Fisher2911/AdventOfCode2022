package day.day10;

import day.Day;

public class Day10 extends Day {

    public Day10() {
        super("day10.txt");
    }

    @Override
    public void part1() {
        final var ticks = this.readLines().map(Tick::parse).toList();
        int total = 1;
        int tickNum = 1;
        int sum = 0;
        for (var tick : ticks) {
            for (int i = 0; i < tick.length(); i++) {
                if ((tickNum - 20) % 40 == 0) {
                    sum += total * tickNum;
                }
                tickNum++;
            }
            total += tick.value();
        }
        System.out.println(sum);
    }

    private static final int WIDTH = 40;
    private static final int HEIGHT = 6;

    @Override
    public void part2() {
        final var ticks = this.readLines().map(Tick::parse).toList();
        int tickNum = 0;
        int x = 1;
        final char[][] array = new char[HEIGHT][WIDTH];
        for (var tick : ticks) {
            for (int i = 0; i < tick.length(); i++) {
                for (int j = -1; j <= 1; j++) {
                    if (j + x == tickNum % 40) {
                        array[tickNum / 40][tickNum % 40] = '#';
                        break;
                    } else {
                        array[tickNum / 40][tickNum % 40] = '.';
                    }
                }
                tickNum++;
            }
            x += tick.value();
        }
        System.out.println(formatArray(array));
    }

    private static String formatArray(char[][] array) {
        final StringBuilder builder = new StringBuilder();
        for (char[] chars : array) {
            for (char c : chars) {
                builder.append(c).append(" ");
            }
            builder.deleteCharAt(builder.length() - 1);
            builder.append("\n");
        }
        return builder.toString();
    }

    private static record Tick(int length, int value) {

        public static final String NOOP = "noop";

        public static Tick parse(String s) {
            if (s.equals(NOOP)) return new Tick(1, 0);
            final String[] parts = s.split(" ");
            if (parts[0].equals("addx")) {
                return new Tick(2, Integer.parseInt(parts[1]));
            }
            throw new IllegalArgumentException();
        }

    }

}
