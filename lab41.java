import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class lab41 {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        // Generate array 
        CompletableFuture<int[]> generateArrayFuture = CompletableFuture.supplyAsync(() -> {
            long taskStart = System.currentTimeMillis();
            int[] array = new Random().ints(10, 1, 101).toArray();
            System.out.println("Array generated: " + Arrays.toString(array) + " (Task time: " + (System.currentTimeMillis() - taskStart) + " ms)");
            return array;
        });
        //  +10
        CompletableFuture<int[]> addTenFuture = generateArrayFuture.thenApplyAsync(array -> {
            long taskStart = System.currentTimeMillis();
            int[] modifiedArray = Arrays.stream(array).map(x -> x + 10).toArray();
            System.out.println("Array after adding 10: " + Arrays.toString(modifiedArray) + " (Task time: " + (System.currentTimeMillis() - taskStart) + " ms)");
            return modifiedArray;
        });

        //dividing each element by 2
        CompletableFuture<double[]> divideByTwoFuture = addTenFuture.thenApplyAsync(array -> {
            long taskStart = System.currentTimeMillis();
            double[] modifiedArray = Arrays.stream(array).mapToDouble(x -> x / 2.0).toArray();
            System.out.println("Array after dividing by 2: " + Arrays.toString(modifiedArray) + " (Task time: " + (System.currentTimeMillis() - taskStart) + " ms)");
            return modifiedArray;
        });

        CompletableFuture<Void> printFinalResultFuture = divideByTwoFuture.thenAcceptAsync(array -> {
            long taskStart = System.currentTimeMillis();
            System.out.println("Final result (after division): " + Arrays.toString(array) + " (Task time: " + (System.currentTimeMillis() - taskStart) + " ms)");
        });

        CompletableFuture<Void> finalTask = printFinalResultFuture.thenRunAsync(() -> {
            System.out.println("All tasks completed in " + (System.currentTimeMillis() - start) + " ms.");
        });
        try {
            finalTask.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
