import java.util.*;

public class Main {

    //public static int maxSize = 0;

    public static void main(String[] args) throws InterruptedException {

        ThreadGroup mainGroup = new ThreadGroup("main group");

        String[] texts = new String[25];

        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("aab", 30_000);
        }

        long startTs = System.currentTimeMillis(); // start time

        List<Thread> threads = new ArrayList<>();
        for (String text : texts) {

            Thread bigThread = new Thread(mainGroup, () -> {
                int maxSize = 0;
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

            });
            bigThread.start();
            threads.add(bigThread);


        }
        // + Дьячко почему join даёт абсолютно всем потокам запуститься?
        //for (Thread thread : threads) {
        //    thread.join(); // зависаем, ждём когда поток объект которого лежит в thread завершится
        //}

        // + Дьячко можно ли заставить поток-демона в завершении работы процесса замерить время?
//        Thread daemon = new Thread(() -> {
//            while (true) {
//                if (!this.isAlive()) {
//                    long endTs = System.currentTimeMillis(); // end time
//
//                    System.out.println("Time: " + (endTs - startTs) + "ms");
//                }
//            }
//        });
//        daemon.setDaemon(true);
//        daemon.start();
//        if(!daemon.isAlive()) {
//            long endTs = System.currentTimeMillis(); // end time
//
//            System.out.println("Time: " + (endTs - startTs) + "ms");
//        }


        // + Дьячко этот вариант хуже чем join
        while (true){
            if (mainGroup.activeCount() == 0) {
                long endTs = System.currentTimeMillis(); // end time

                System.out.println("Time: " + (endTs - startTs) + "ms");
                break;
            }
        }



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