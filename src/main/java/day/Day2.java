package day;

import day.Day;

import java.util.Map;

public class Day2 extends Day {

    public Day2() {
        super("day2.txt");
    }

    @Override
    public void part1() {
        System.out.println(
                this.readLines()
                .map(s -> Map.entry(s.split(" ")[0].charAt(0), s.split(" ")[1].charAt(0)))
                .map(e -> Map.entry(e.getKey(), e.getValue() - 'X' + 'A'))
                .map(e -> Map.entry(e.getValue() - 'A' + 1, e.getValue() - e.getKey()))
                .map(e -> Map.entry(e.getKey(), Math.abs(e.getValue()) == 2 ? -e.getValue() : e.getValue()))
                .mapToInt(e -> e.getKey() + (e.getValue() < 0 ? 0 : e.getValue() == 0 ? 3 : 6))
                .sum()
        );
    }

    // A = rock
    // B = paper
    // C = scissors

    @Override
    public void part2() {
        System.out.println(
                this.readLines()
                .map(s -> Map.entry(s.split(" ")[0].charAt(0), s.split(" ")[1].charAt(0)))

                .map(e -> Map.entry(e.getKey(), e.getValue() == 'Y' ? e.getKey() :
                        e.getValue() == 'X' ? (e.getKey() == 'C' ? 'B' : e.getKey() == 'B' ? 'A' : 'C') :
                                e.getKey() == 'A' ? 'B' : e.getKey() == 'B' ? 'C' : 'A')
                )
                .map(e -> Map.entry(e.getValue() - 'A' + 1, e.getValue() - e.getKey()))
                .map(e -> Map.entry(e.getKey(), Math.abs(e.getValue()) == 2 ? -e.getValue() : e.getValue()))
                .mapToInt(e -> e.getKey() + (e.getValue() < 0 ? 0 : e.getValue() == 0 ? 3 : 6))
                .sum()
        );
    }

}
