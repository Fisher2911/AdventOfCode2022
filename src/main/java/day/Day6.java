package day;

import day.Day;

import java.util.LinkedList;

public class Day6 extends Day {

    public Day6() {
        super("day6.txt");
    }

    @Override
    public void part1() {
        final char[] array = this.readLines().findFirst().orElseThrow().toCharArray();
        final LinkedList<Character> stack = new LinkedList<>();
        int index = 0;
        for (int i = 0; i < array.length; i++) {
            if (stack.size() == 4) {
                index = i;
                break;
            }
            final char c = array[i];
            stack.add(c);
            final int duplicateIndex = stack.indexOf(c);
            if (duplicateIndex != stack.size() - 1) {
                int temp = 0;
                while (temp <= duplicateIndex) {
                    stack.pop();
                    temp++;
                }
            }
        }
        System.out.println(index);
    }

    @Override
    public void part2() {
        final char[] array = this.readLines().findFirst().orElseThrow().toCharArray();
        final LinkedList<Character> stack = new LinkedList<>();
        int index = 0;
        for (int i = 0; i < array.length; i++) {
            if (stack.size() == 14) {
                index = i;
                break;
            }
            final char c = array[i];
            stack.add(c);
            final int duplicateIndex = stack.indexOf(c);
            if (duplicateIndex != stack.size() - 1) {
                int temp = 0;
                while (temp <= duplicateIndex) {
                    stack.pop();
                    temp++;
                }
            }
        }
        System.out.println(index);
    }
}
