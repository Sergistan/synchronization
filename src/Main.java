import java.util.*;

public class Main {

    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();
    private static final int countsOfThread = 10;
    private static final List<Thread> threads = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {

        for (int i = 0; i < countsOfThread; i++) {
            new Thread(() -> {
                synchronized (sizeToFreq) {
                    String route = generateRoute("RLRFR", 100);
                    int charCount = 0;
                    char temp;

                    for (int j = 0; j < route.length(); j++) {
                        temp = route.charAt(j);

                        if (temp == 'R')
                            charCount++;
                    }
                    if (!sizeToFreq.containsKey(charCount)) {
                        sizeToFreq.put(charCount, 1);
                    } else {
                        Integer integer = sizeToFreq.get(charCount);
                        sizeToFreq.put(charCount, ++integer);
                    }
                    sizeToFreq.notify();
                }
            }).start();
        }

        for (int i = 0; i < sizeToFreq.size(); i++) {
            synchronized (sizeToFreq) {
                sizeToFreq.wait();
                Thread thread = new Thread(() -> {
                    while (!Thread.interrupted()) {
                        Integer maxValueInMap = Collections.max(sizeToFreq.values());
                        for (Map.Entry<Integer, Integer> entry : sizeToFreq.entrySet()) {
                            if (entry.getValue() == maxValueInMap) {
                                Integer key = entry.getKey();
                                System.out.println("Текущий лидер среди частот: " + key);
                            }
                        }
                    }
                });
                thread.start();
                threads.add(thread);
            }
        }

        for (Thread thread : threads) {
            thread.join();
            thread.interrupt();
        }

    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }
}
