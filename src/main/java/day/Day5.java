package day;

import day.Day;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Day5 extends Day {

    public Day5() {
        super("day5.txt");
    }

    @Override
    public void part1() {
        final Map<Integer, Stack> stacks = new HashMap<>();
        final List<String> input = this.readLines().toList();
        int[] crateNumbers = null;
        int index = 0;
        while (crateNumbers == null) {
            final String line = input.get(index);
            crateNumbers = this.getCrateNumbers(line);
            if (crateNumbers != null) {
                index++;
                continue;
            }
            final var crates = this.readCrateLetters(line);
            crates.forEach((key, value) -> stacks.computeIfAbsent(key, k -> new Stack()).add(value));
            index++;
        }
        final List<String> instructions = input.subList(index + 1, input.size());
        for (String instruction : instructions) {
            move(instruction, stacks, false);
        }
        final StringBuilder builder = new StringBuilder();
        for (var stack : stacks.values()) {
            builder.append(stack.getTop());
        }
        System.out.println(builder);
    }

    private void move(String instruction, Map<Integer, Stack> stacks, boolean efficient) {
        final int moveIndex = instruction.indexOf("move ") + "move ".length();
        final int fromIndex = instruction.indexOf("from ") + "from ".length();
        final int toIndex = instruction.indexOf("to ") + "to ".length();
        final char[] array = instruction.toCharArray();
        final int move = parseInstructionInt(array, moveIndex);
        final int from = parseInstructionInt(array, fromIndex);
        final int to = parseInstructionInt(array, toIndex);
        if (efficient) {
            stacks.get(from).moveEfficient(stacks.get(to), move);
            return;
        }
        stacks.get(from).move(stacks.get(to), move);
    }

    private int parseInstructionInt(char[] array, int start) {
        int num = 0;
        try {
            for (int i = start; i < array.length; i++) {
                final int j = Integer.parseInt(String.valueOf(array[i]));
                num *= 10;
                num += j;
            }
        } catch (NumberFormatException e) {
            return num;
        }
        return num;
    }

    @Override
    public void part2() {
        final Map<Integer, Stack> stacks = new HashMap<>();
        final List<String> input = this.readLines().toList();
        int[] crateNumbers = null;
        int index = 0;
        while (crateNumbers == null) {
            final String line = input.get(index);
            crateNumbers = this.getCrateNumbers(line);
            if (crateNumbers != null) {
                index++;
                continue;
            }
            final var crates = this.readCrateLetters(line);
            crates.forEach((key, value) -> stacks.computeIfAbsent(key, k -> new Stack()).add(value));
            index++;
        }
        final List<String> instructions = input.subList(index + 1, input.size());
        for (String instruction : instructions) {
            move(instruction, stacks, true);
        }
        final StringBuilder builder = new StringBuilder();
        for (var stack : stacks.values()) {
            builder.append(stack.getTop());
        }
        System.out.println(builder);
    }

    private Map<Integer, Character> readCrateLetters(String line) {
        final Map<Integer, Character> crates = new HashMap<>();
        int currentNumber = 1;
        char previous = ' ';
        int emptySpaceCount = 0;
        for (char c : line.toCharArray()) {
            switch (c) {
                case '[' -> previous = c;
                case ']' -> {
                    if (Character.isLetter(previous)) {
                        crates.put(currentNumber, previous);
                    }
                    currentNumber++;
                    previous = c;
                }
                default -> {
                    if (Character.isWhitespace(c) && previous != ']') {
                        emptySpaceCount++;
                    }
                    previous = c;
                    if (emptySpaceCount == 4) {
                        emptySpaceCount = 0;
                        currentNumber++;
                    }
                }
            }
        }
        return crates;
    }


    private int[] getCrateNumbers(String string) {
        final String replaced = string.replaceAll("[\s]+", " ");
        if (replaced.isBlank()) return null;
        final String input = replaced.substring(1);
        final int[] array = new int[input.length() / 2 + 1];
        try {
            int i = 0;
            for (String s : input.split(" ")) {
                array[i] = Integer.parseInt(s);
                i++;
            }
        } catch (Exception e) {
            return null;
        }
        return array;
    }

    private static class Stack {

        private final Deque<Character> queue = new LinkedList<>();

        public void add(Character character) {
            this.queue.addLast(character);
        }

        public void move(Stack other, int count) {
            for (int i = 0; i < count; i++) {
                other.queue.addFirst(this.queue.pollFirst());
            }
        }

        public void moveEfficient(Stack other, int count) {
            final List<Character> toMove = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                toMove.add(this.queue.pollFirst());
            }
            Collections.reverse(toMove);
            for (char c : toMove) {
                other.queue.addFirst(c);
            }
        }

        public Character getTop() {
            return this.queue.peekFirst();
        }

        @Override
        public String toString() {
            return "Stack{" +
                    "queue=" + queue +
                    '}';
        }
    }

}
