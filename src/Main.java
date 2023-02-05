import java.util.*;

public class Main {

    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();
    private static final int countsOfThread = 1000;

    public static void main(String[] args) {

        for (int i = 0; i < countsOfThread; i++) {
            new Thread(() -> {
                    String route = generateRoute("RLRFR", 100);
                    int charCount = 0;
                    char temp;

                    for (int j = 0; j < route.length(); j++) {
                        temp = route.charAt(j);

                        if (temp == 'R')
                            charCount++;
                    }
                synchronized (sizeToFreq) {
                    if (!sizeToFreq.containsKey(charCount)) {
                        sizeToFreq.put(charCount, 1);
                    } else {
                        Integer integer = sizeToFreq.get(charCount);
                        sizeToFreq.put(charCount, ++integer);
                    }
                }
            }).start();
        }

        Integer maxValueInMap = Collections.max(sizeToFreq.values());
        for (Map.Entry<Integer, Integer> entry : sizeToFreq.entrySet()) {
            if (entry.getValue() == maxValueInMap) {
                Integer key = entry.getKey();
                System.out.println("Самое частое количество повторений: " + key + " (встретилось " + maxValueInMap + " раз)");
            }
        }

        System.out.println("Другие размеры:");
        sizeToFreq.forEach(
                (key, value)
                        -> System.out.println("-" + key + " (" + value + " раз(а))"));

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
