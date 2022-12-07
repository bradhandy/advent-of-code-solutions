package com.dbjgb.advent.twentyTwo.puzzle.seven;

import com.dbjgb.advent.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Solution {

  public static void main(String... args) throws Exception {
    List<String> terminalOutput = Utility.readAllLines("twentyTwo/puzzle/seven/input.txt");
    List<Directory> directories = new ArrayList<>();
    Directory rootDirectory = new Directory("/", null);
    directories.add(rootDirectory);

    Directory currentDirectory = null;
    for (String line : terminalOutput) {
      if (line.startsWith("$ cd ")) {
        String name = line.substring(5);
        if (name.equals(rootDirectory.getName())) {
          currentDirectory = rootDirectory;
        } else if (name.equals("..")) {
          currentDirectory = currentDirectory.getParent();
        } else {
          currentDirectory = currentDirectory.getChild(name);
        }
      } else if (line.startsWith("$ ls")) {
        // do nothing
      } else if (line.startsWith("dir ")) {
        String name = line.substring(4);
        Directory directory = new Directory(name, currentDirectory);
        currentDirectory.addChild(directory);
        directories.add(directory);
      } else if (line.length() > 0) {
        int spaceIndex = line.indexOf(' ');
        long size = Long.parseLong(line.substring(0, spaceIndex));
        String name = line.substring(spaceIndex + 1);
        File file = new File(size, name);
        currentDirectory.addFile(file);
      }
    }

    System.out.printf("Total of Directories under 100,000: %d\n", directories.stream().filter(directory -> directory.totalSize() <= 100000).mapToLong(Directory::totalSize).reduce(0, Long::sum));

    System.out.printf("Root directory size: %d\n", rootDirectory.totalSize());
    long freeSpace = 70000000 - rootDirectory.totalSize();
    System.out.printf("Current free space: %d\n", freeSpace);
    long needToFree = 30000000 - freeSpace;
    System.out.printf("Space need to free: %d\n", needToFree);

    long sizeOfDirectoryToRemove = directories.stream().mapToLong(Directory::totalSize).filter((size) -> size >= needToFree).reduce(Long.MAX_VALUE, Math::min);
    System.out.printf("Size of Directory to Remove: %d\n", sizeOfDirectoryToRemove);
  }

  private static class Directory {

    private final String name;
    private final Directory parent;
    private Map<String, Directory> children;
    private Set<File> files;

    Directory(String name, Directory parent) {
      this.name = name;
      this.parent = parent;
      this.children = new HashMap<>();
      this.files = new HashSet<>();
    }

    void addChild(Directory directory) {
      children.put(directory.getName(), directory);
    }

    String getName() {
      return name;
    }

    void addFile(File file) {
      files.add(file);
    }

    Directory getParent() {
      return parent;
    }

    Directory getChild(String name) {
      return children.get(name);
    }

    long totalSize() {
      long childTotal = children.values().stream().mapToLong(Directory::totalSize).reduce(0, Long::sum);
      return files.stream().mapToLong(File::getSize).reduce(childTotal, Long::sum);
    }
  }

  private static class File {

    private final long size;
    private final String name;

    File(long size, String name) {
      this.size = size;
      this.name = name;
    }

    public long getSize() {
      return size;
    }

    public String getName() {
      return name;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      File file = (File) o;
      return size == file.size && name.equals(file.name);
    }

    @Override
    public int hashCode() {
      return Objects.hash(size, name);
    }
  }
}
