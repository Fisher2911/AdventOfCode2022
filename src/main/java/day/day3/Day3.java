package day.day3;

import day.Day;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Day3 extends Day {

    public Day3() {
        super("day3.txt");
    }

    @Override
    public void part1() {
        System.out.println(
                this.readLines()
                        .map(s -> Map.entry(s.substring(0, s.length() / 2), s.substring(s.length() / 2)))
                        .map(e -> findCommon(e.getKey(), e.getValue()))
                        .mapToInt(c -> Character.isUpperCase(c) ? Math.abs(c - 'A') + 27 : Math.abs(c - 'a') + 1)
                        .sum()
        );
    }

    private char findCommon(String first, String second) {
        for (char c : first.toCharArray()) {
            for (char c2 : second.toCharArray()) {
                if (c == c2) return c;
            }
        }
        throw new RuntimeException();
    }

    @Override
    public void part2() {
        final List<List<String>> lines = new ArrayList<>();
        int i = 0;
        List<String> current = new ArrayList<>();
        for (String l : this.readLines().toList()) {
            current.add(l);
            i++;
            if (i % 3 == 0) {
                lines.add(current);
                current = new ArrayList<>();
            }
        }
        int sum = 0;
        for (var list : lines) {
            char c = findCommon(list.get(0), list.get(1), list.get(2));
            sum += Character.isUpperCase(c) ? Math.abs(c - 'A') + 27 : Math.abs(c - 'a') + 1;
        }
        System.out.println(sum);
    }

    private char findCommon(String first, String second, String third) {
        for (char c : first.toCharArray()) {
            for (char c2 : second.toCharArray()) {
                for (char c3 : third.toCharArray()) {
                    if (c == c2 && c2 == c3) return c;
                }
            }
        }
        throw new RuntimeException();
    }
}
