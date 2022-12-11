package day;

import day.Day;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class Day7 extends Day {

    public Day7() {
        super("day7.txt");
    }

    @Override
    public void part1() {
        final FileData root = FileData.createRoot();
        final FileSearcher searcher = new FileSearcher(root);
        CommandPrompt.readAll(searcher, this.readLines().toList());
        searcher.moveToParent();
        System.out.println("Part 1: " +
                searcher.currentDir.findMatching(d -> d.calculateSize() < 100_000 && d.isDir())
                        .stream()
                        .mapToInt(FileData::calculateSize)
                        .sum()
        );
    }

    private static final int AVAILABLE_SPACE = 70_000_000;
    private static final int REQUIRED_SPACE = 30_000_000;

    @Override
    public void part2() {
        final FileData root = FileData.createRoot();
        final FileSearcher searcher = new FileSearcher(root);
        CommandPrompt.readAll(searcher, this.readLines().toList());
        searcher.moveToParent();
        final int usedSpace = root.calculateSize();
        final int required = REQUIRED_SPACE - (AVAILABLE_SPACE - usedSpace);
        System.out.println("Part 2: " +
                searcher.currentDir.findMatching(d -> d.calculateSize() >= required && d.isDir())
                        .stream()
                        .mapToInt(FileData::calculateSize)
                        .sorted()
                        .findFirst()
                        .orElse(-1)
        );
    }

    private static class CommandPrompt {

        private static final String CD = "$ cd";
        private static final String LS = "$ ls";
        private static final String DIR = "dir";

        private static final BiConsumer<String, FileSearcher> CD_CONSUMER = (input, searcher) -> {
            final String instruction = input.split(Pattern.quote(CD + " "))[1];
            switch (instruction) {
                case ".." -> searcher.moveUp();
                case FileData.ROOT_DIRECTORY -> searcher.moveToParent();
                default -> searcher.moveTo(instruction);
            }
        };

        public static void readAll(FileSearcher searcher, List<String> lines) {
            int i = 0;
            while (i < lines.size()) {
                i = doFunction(searcher, lines, i);
            }
        }

        private static int doFunction(FileSearcher searcher, List<String> lines, int index) {
            final StringBuilder builder = new StringBuilder();
            final String line = lines.get(index);
            for (char c : line.toCharArray()) {
                builder.append(c);
                if (builder.toString().equals(CD)) {
                    CD_CONSUMER.accept(line, searcher);
                    return index + 1;
                }
                if (builder.toString().equals(LS)) {
                    return handleLS(searcher, searcher.currentDir, lines, index);
                }
            }
            return index;
        }

        private static int handleLS(FileSearcher searcher, FileData fileData, List<String> lines, int index) {
            int i = index + 1;
            String line = lines.get(i);
            while (doFunction(searcher, lines, i) == i) {
                addFileFromLs(fileData, line);
                i++;
                if (i >= lines.size()) break;
                line = lines.get(i);
            }
            return i + 1;
        }

        private static void addFileFromLs(FileData fileData, String input) {
            if (input.startsWith(DIR)) {
                final String name = input.split(Pattern.quote(DIR + " "))[1];
                if (fileData.children.containsKey(name)) return;
                fileData.addChildDir(name);
                return;
            }
            int size = 0;
            int index = 0;
            for (char c : input.toCharArray()) {
                try {
                    index++;
                    final int value = Integer.parseInt(String.valueOf(c));
                    size *= 10;
                    size += value;
                } catch (NumberFormatException e) {
                    break;
                }
            }
            fileData.addChild(new FileData(fileData, input.substring(index), size));
        }
    }

    private static class FileSearcher {

        private FileData currentDir;

        public FileSearcher(FileData currentDir) {
            this.currentDir = currentDir;
        }

        public void moveUp() {
            this.currentDir = this.currentDir.parent;
        }

        public void moveToParent() {
            while (this.currentDir.parent != null) {
                this.currentDir = this.currentDir.parent;
            }
        }

        public void moveTo(String name) {
            this.currentDir = this.currentDir.getDir(name);
        }
    }

    private static class FileData {

        public static final String ROOT_DIRECTORY = "/";

        private final FileData parent;
        private final String name;
        private final Map<String, FileData> children;
        private final int size;

        public static FileData createRoot() {
            return new FileData(null, ROOT_DIRECTORY, 0);
        }

        public FileData(FileData parent, String name, int size) {
            this.parent = parent;
            this.name = name;
            this.children = new HashMap<>();
            this.size = size;
        }

        public void addChild(FileData fileData) {
            this.children.put(fileData.name, fileData);
        }

        public void addChildDir(String fileName) {
            this.children.put(fileName, new FileData(this, fileName, 0));
        }

        public FileData getDir(String name) {
            return this.children.computeIfAbsent(name, k -> new FileData(this, name, 0));
        }

        public int calculateSize() {
            int currentSize = this.size;
            for (FileData data : this.children.values()) {
                currentSize += data.calculateSize();
            }
            return currentSize;
        }

        public Collection<FileData> findMatching(Predicate<FileData> predicate) {
            final List<FileData> data = new ArrayList<>();
            for (FileData child : this.children.values()) {
                if (predicate.test(child)) {
                    data.add(child);
                }
                data.addAll(child.findMatching(predicate));
            }
            return data;
        }

        public boolean isDir() {
            return this.size == 0;
        }
    }
}
