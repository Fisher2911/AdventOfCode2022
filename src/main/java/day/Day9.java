package day;

import day.Day;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day9 extends Day {

    public Day9() {
        super("day9.txt");
    }

    @Override
    public void part1() {
        final Map map = new Map(new HashSet<>(), Point.origin(), 1);
        this.readLines()
                .map(s -> Pair.of(Direction.fromInput(s.split(" ")[0]), Integer.parseInt(s.split(" ")[1])))
                .forEach(map::moveTo);
        System.out.println("Part 1: " + map.visited.size());
    }

    @Override
    public void part2() {
        final Map map = new Map(new HashSet<>(), Point.origin(), 9);
        this.readLines()
                .map(s -> Pair.of(Direction.fromInput(s.split(" ")[0]), Integer.parseInt(s.split(" ")[1])))
                .forEach(map::moveTo);
        System.out.println("Part 2: " + map.visited.size());
    }

    private static class Map {

        private final Set<Point> visited;
        private final List<Point> points;

        int minY = -5;
        int minX = -11;
        int maxX = 15;
        int maxY = 16;

        public Map(Set<Point> visited, Point head, int totalTails) {
            this.visited = visited;
            this.points = new ArrayList<>();
            this.points.add(head);
            for (int i = 0; i < totalTails; i++) {
                this.points.add(head);
            }
            this.visited.add(head);
        }

        public void moveTo(Direction direction, int amount) {
            for (int i = 0; i < amount; i++) {
                Point previous = this.points.get(0);
                for (int j = 0; j < this.points.size(); j++) {
                    if (j == 0) {
                        final Point head = previous.add(direction.multiply(1));
                        this.points.set(0, head);
                        previous = head;
                        continue;
                    }
                    final Point tail = this.points.get(j);
                    if (previous.distance(tail) <= 1) {
                        previous = tail;
                        this.points.set(j, tail);
                        continue;
                    }
                    final boolean sameXOrY = previous.x == tail.x || previous.y == tail.y;
                    final boolean moveRight = tail.x < previous.x;
                    final boolean moveUp = tail.y < previous.y;
                    final int addX = moveRight ? 1 : -1;
                    final int addY = moveUp ? 1 : -1;
                    final int xDistance = Math.abs(tail.x - previous.x);
                    final int yDistance = Math.abs(tail.y - previous.y);
                    final Point newTail = tail.add(sameXOrY ? (xDistance > yDistance ? addX : 0) : addX, sameXOrY ? (yDistance > xDistance ? addY : 0) : addY);
                    this.points.set(j, newTail);
                    if (j == this.points.size() - 1) {
                        this.visited.add(newTail);
                    }
                    previous = newTail;
                }
                // visualize stuff
                this.minY = Math.min(this.minY, previous.y);
                this.maxY = Math.max(this.maxY, previous.y);
                this.minX = Math.min(this.minX, previous.x);
                this.maxX = Math.max(this.maxX, previous.x);
            }
            this.visualize();
        }

        public char[][] visualize() {
            if (true) return null;
            final char[][] array = new char[this.maxY - this.minY][this.maxX - this.minX];
            for (int x = 0; x < this.maxX - this.minX; x++) {
                for (int y = 0; y < this.maxY - this.minY; y++) {
                    final Point head = this.points.get(0);
                    if (head.x == x + this.minX && head.y == y + this.minY) {
                        array[this.maxY - this.minY - 1 - y][x] = 'H';
                        continue;
                    }
                    if (this.points.contains(Point.at(x + this.minX, y + this.minY))) {
                        array[this.maxY - this.minY - 1 - y][x] = String.valueOf(this.points.indexOf(Point.at(x + this.minX, y + this.minY))).charAt(0);
                        continue;
                    }
                    array[this.maxY - this.minY - 1 - y][x] = '.';
                }
            }
            for (char[] a : array) {
                final StringBuilder builder = new StringBuilder();
                for (char c : a) {
                    builder.append(c).append(" ");
                }
                System.out.println(builder);
            }
            System.out.println("-----------------");
            return array;
        }

        public void moveTo(Pair<Direction, Integer> moveAmount) {
            this.moveTo(moveAmount.first(), moveAmount.second());
        }

    }

    private enum Direction {

        UP(0, 1),
        DOWN(0, -1),
        RIGHT(1, 0),
        LEFT(-1, 0);

        private final int x;
        private final int y;

        Direction(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Point multiply(int i) {
            return Point.at(this.x * i, this.y * i);
        }

        public static Direction fromInput(String input) {
            return switch (input) {
                case "U" -> UP;
                case "D" -> DOWN;
                case "R" -> RIGHT;
                case "L" -> LEFT;
                default -> throw new IllegalArgumentException("Invalid direction");
            };
        }

    }

    private static record Point(int x, int y) {

        public static Point at(int x, int y) {
            return new Point(x, y);
        }

        public static Point origin() {
            return Point.at(0, 0);
        }

        public Point add(int x, int y) {
            return Point.at(this.x + x, this.y + y);
        }

        public Point add(Point point) {
            return this.add(point.x, point.y);
        }

        public int distance(Point point) {
            return (int) Math.sqrt(Math.pow(this.x - point.x, 2) + Math.pow(this.y - point.y, 2));
        }

    }

    private static record Pair<F, S>(F first, S second) {

        public static <F, S> Pair<F, S> of(F first, S second) {
            return new Pair<>(first, second);
        }

    }

}
