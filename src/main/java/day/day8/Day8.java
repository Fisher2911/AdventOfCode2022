package day.day8;

import day.Day;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SuppressWarnings("DuplicatedCode")
public class Day8 extends Day {

    public Day8() {
        super("day8.txt");
    }

    @Override
    public void part1() {
        final int[][] array = this.readLines()
                .map(s -> s.chars().map(c -> Integer.parseInt(String.valueOf((char) c))).toArray())
                .toArray(int[][]::new);
        final Set<Tree> all = new HashSet<>();
        int count = 0;
        for (int y = 0; y < array.length; y++) {
            for (int x = 0; x < array[y].length; x++) {
                final Tree current = Tree.create(array, new Point(y, x));
                if (all.contains(current)) continue;
                all.add(current);
                for (Direction direction : Direction.values()) {
                    final boolean visible = isVisible(array, direction, new Point(y, x));
                    if (visible) {
                        count++;
                        break;
                    }
                }
            }
        }
        System.out.println("Part 1: " + count);
    }

    @Override
    public void part2() {
        final int[][] array = this.readLines()
                .map(s -> s.chars().map(c -> Integer.parseInt(String.valueOf((char) c))).toArray())
                .toArray(int[][]::new);
        final Set<Tree> all = new HashSet<>();
        int highestScore = 0;
        for (int y = 0; y < array.length; y++) {
            for (int x = 0; x < array[y].length; x++) {
                final Tree current = Tree.create(array, new Point(y, x));
                if (all.contains(current)) continue;
                all.add(current);
                int score = 1;
                for (Direction direction : Direction.values()) {
                     score *= countViewDistance(array, direction, new Point(y, x));
                }
                highestScore = Math.max(highestScore, score);
            }
        }
        System.out.println("Part 2: " + highestScore);
    }

    private static boolean isVisible(int[][] array, Direction direction, Point start) {
        final List<Tree> path = direction.pathToEdge(array, start).stream().map(point -> Tree.create(array, point)).toList();
        final Tree current = Tree.create(array, start);
        for (final Tree tree : path) {
            if (current.height() <= tree.height) return false;
        }
        return true;
    }

    private static int countViewDistance(int[][] array, Direction direction, Point start) {
        final List<Tree> path = direction.pathToEdge(array, start).stream().map(point -> Tree.create(array, point)).toList();
        final Tree current = Tree.create(array, start);
        int distance = 0;
        for (final Tree tree : path) {
            distance++;
            if (current.height() <= tree.height) return distance;
        }
        return distance;
    }

    private enum Direction {

        UP(-1, 0),
        DOWN(1, 0),
        RIGHT(0, 1),
        LEFT(0, -1);

        private final int y;
        private final int x;

        Direction(int y, int x) {
            this.x = x;
            this.y = y;
        }

        private List<Point> pathToEdge(int[][] array, Point start) {
            if (this.isVertical()) {
                final int min = this.y == -1 ? 0 : start.y() + 1;
                final int max = this.y == -1 ? start.y() : array.length;
                final List<Point> list =  IntStream.range(min, max).mapToObj(y -> new Point(y, start.x())).collect(Collectors.toList());
                if (this == UP) {
                    Collections.reverse(list);
                }
                return list;
            }
            final int min = this.x == -1 ? 0 : start.x() + 1;
            final int max = this.x == -1 ? start.x() : array[start.y()].length;
            final List<Point> list = IntStream.range(min, max).mapToObj(x -> new Point(start.y(), x)).collect(Collectors.toList());
            if (this == LEFT) {
                Collections.reverse(list);
            }
            return list;
        }

        private boolean isSide() {
            return this == RIGHT || this == LEFT;
        }

        private boolean isVertical() {
            return !this.isSide();
        }
    }

    private static record Point(int y, int x) {
    }

    private static record Tree(Point point, int height) {

        public static Tree create(int[][] array, Point point) {
            return new Tree(point, array[point.y()][point.x()]);
        }

    }

}
