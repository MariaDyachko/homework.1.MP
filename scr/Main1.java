import java.util.*;
import java.util.concurrent.*;

public class Main1 {

    //public static int maxSize = 0;

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        long startTs = System.currentTimeMillis(); // start time

        Callable<Integer> callableOfMaxSize = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                int maxSize = 0;
                String text = generateText("aab", 30_000);
                for (int i = 0; i < text.length(); i++) {
                    for (int j = 0; j < text.length(); j++) {
                        if (i >= j) {
                            continue;
                        }
                        boolean bFound = false;
                        for (int k = i; k < j; k++) {
                            if (text.charAt(k) == 'b') {
                                bFound = true;
                                break;
                            }
                        }
                        if (!bFound && maxSize < j - i) {
                            maxSize = j - i;
                        }
                    }
                }
                System.out.println(text.substring(0, 100) + " -> " + maxSize);
                return maxSize;
            }
        };
        int maxNumberOfMaxSize = 0;
        final ExecutorService threadPool = Executors.newFixedThreadPool(25);
        for (int i = 0; i < 26; i++) {
            final Future<Integer> task = threadPool.submit(callableOfMaxSize);
            final Integer resultOfTask = task.get();
            if(resultOfTask > maxNumberOfMaxSize){
                maxNumberOfMaxSize = resultOfTask;
            }
        }

        System.out.println(maxNumberOfMaxSize + " maximum of not interrupted 'a'");

        long endTs = System.currentTimeMillis(); // end time

        System.out.println("Time: " + (endTs - startTs) + "ms");

        threadPool.shutdown();

    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}