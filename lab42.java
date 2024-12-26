import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.DoubleStream;

public class lab42 {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        //random sequence of 20 real numbers
        CompletableFuture<double[]> generateSequenceFuture = CompletableFuture.supplyAsync(() -> {
            long taskStart = System.currentTimeMillis();
            Random random = new Random();
            double[] sequence = DoubleStream.generate(() -> random.nextDouble() * 100).limit(20).toArray();
            System.out.println("Generated sequence: ");
            printArray(sequence);
            System.out.println("Task time: " + (System.currentTimeMillis() - taskStart) + " ms");
            return sequence;
        });
        CompletableFuture<Double> calculateProductFuture = generateSequenceFuture.thenApplyAsync(sequence -> {
            long taskStart = System.currentTimeMillis();
            double product = 1.0;
            for (int i = 1; i < sequence.length; i++) {
                product *= (sequence[i] - sequence[i - 1]);
            }
            System.out.println("Calculated product: " + product);
            System.out.println("Task time: " + (System.currentTimeMillis() - taskStart) + " ms");
            return product;
        });
        CompletableFuture<Void> finalTask = calculateProductFuture.thenAcceptAsync(product -> {
            long totalTime = System.currentTimeMillis() - startTime;
            System.out.println("Final result (product): " + product);
            System.out.println("Total execution time: " + totalTime + " ms");
        });

        try {
            finalTask.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
    private static void printArray(double[] array) {
        for (double value : array) {
            System.out.printf("%.2f ", value);
        }
        System.out.println();
    }
}
