package day;

import com.sun.tools.javac.Main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Stream;

public abstract class Day {

    private final String fileName;

    public Day(String fileName) {
        this.fileName = fileName;
    }

    protected Stream<String> readLines() {
        try {
            return new BufferedReader(new FileReader(Main.class.getClassLoader().getResource(this.fileName).getFile())).lines();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract void part1();

    public abstract void part2();

}
