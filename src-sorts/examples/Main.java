package examples;

import java.util.Arrays;
import java.util.Random;

import algorithms.Quicksort;

public class Main {
    private static final int[] SIZES = { 5, 20, 50, 100, 200, 500, 1000, 5000, 10000 };

    private static final int NUM_EXECS = 630;

    private static final int MAX_INT = 1024;

    private static int currentSize;

    public static void main(String[] args) throws InterruptedException {

        Random random = new Random();
        for (int size : SIZES) {
            setCurrentSize(size);
            int[] values = new int[size];
            for (int it = 0; it < NUM_EXECS; it++) {
                for (int i = 0; i < size; i++) {
                    values[i] = random.nextInt(MAX_INT);
                }
                System.out.println("Unsorted Array #" + (it + 1) + ": " + Arrays.toString(values));
                Quicksort.sort(values); // sorting unsorted array
                System.out.println("  Sorted Array #" + (it + 1) + ": " + Arrays.toString(values));
            }
        }
    }

    public static int getCurrentSize() {
        return currentSize;
    }

    public static void setCurrentSize(int currentSize) {
        Main.currentSize = currentSize;
    }
}