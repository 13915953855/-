import java.util.List;
import java.util.Vector;


/**
 *  关闭偏向锁,-XX:-UseBiasedLocking
 */
public class SynchronizedTest2 {

    private static List<Integer> list = new Vector<>();

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        for (int i = 0; i < 10000000; i++) {
            list.add(i);
        }

        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }

}
