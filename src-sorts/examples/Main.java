package examples;

import java.util.Arrays;
import java.util.Random;
import java.lang.StringBuilder;
import algorithms.Quicksort;

public class Main {
    private static final int[] SIZES = { 5, 20, 50, 100, 200, 500, 1000, 5000, 10000 };

    private static final int NUM_EXECS = 630;

    private static final int MAX_INT = 1024;

    private static int currentSize;

    public static void testSize(int size){
    	System.out.println("Testing arrays for size "+size+"...");
    	Random random = new Random();
    	int[] values = new int[size];
    	for (int it = 0; it < NUM_EXECS; it++) {
           for (int i = 0; i < size; i++) {
               values[i] = random.nextInt(MAX_INT);
           }
           Quicksort.sort(values); // sorting unsorted array
       }
       return;
    }

    public static void main(String[] args) throws InterruptedException {

        for (int size : SIZES) {
            setCurrentSize(size);
            testSize(size);
        }
    }

    public static int getCurrentSize() {
        return currentSize;
    }

    public static void setCurrentSize(int currentSize) {
        Main.currentSize = currentSize;
    }
}