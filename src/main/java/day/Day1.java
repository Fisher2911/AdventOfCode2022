package day;

import day.Day;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Day1 extends Day {

    public Day1() {
        super("day1.txt");
    }

    private List<Integer> getSorted() {
//        final String[] array = this.readLines().toList();
        List<Integer> amounts = new ArrayList<>();
        List<String> lines = new ArrayList<>();
        for (String s : this.readLines().toList()) {
            if (!s.isBlank()) {
                lines.add(s);
                continue;
            }
            int current = 0;
            for (String s2 : lines){
                current += Integer.parseInt(s2);
            }
            lines.clear();
            amounts.add(current);
        }
        Collections.sort(amounts);
        Collections.reverse(amounts);
        return amounts;
    }

    @Override
    public void part1() {
        System.out.println(getSorted().get(0));
    }

    @Override
    public void part2() {
        final var sorted = getSorted();
        System.out.println(sorted.get(0) + sorted.get(1) + sorted.get(2));
    }
}
