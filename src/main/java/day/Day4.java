package day;

import day.Day;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Day4 extends Day {

    public Day4() {
        super("day4.txt");
    }

    @Override
    public void part1() {
        System.out.println(
                this.readLines()
                .map(s -> Map.entry(genRange(s.split(",")[0]), genRange(s.split(",")[1])))
                .map(e -> e.getKey().containsAll(e.getValue()) || e.getValue().containsAll(e.getKey()))
                .mapToInt(b -> b ? 1 : 0)
                        .sum()
        );
    }

    private Set<Integer> genRange(String s) {
        final var set = new HashSet<Integer>();
        for (int i = Integer.parseInt(s.split("-")[0]); i <= Integer.parseInt(s.split("-")[1]); i++) {
            set.add(i);
        }
        return set;
    }

    @Override
    public void part2() {
        System.out.println(
                this.readLines()
                        .map(s -> Map.entry(genRange(s.split(",")[0]), genRange(s.split(",")[1])))
                        .map(e -> containsAny(e.getKey(), e.getValue()))
                        .mapToInt(b -> b ? 1 : 0)
                        .sum()
        );
    }

    private boolean containsAny(Set<Integer> first, Set<Integer> second) {
        for (int i : first) {
            if (second.contains(i)) return true;
        }
        for (int i : second) {
            if (first.contains(i)) return true;
        }
        return false;
    }
}
