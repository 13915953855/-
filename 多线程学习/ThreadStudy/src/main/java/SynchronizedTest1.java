public class SynchronizedTest1 implements Runnable {

    public static int i = 0;

    private synchronized void increment(){
        i++;
    }

    public static void main(String[] args) throws InterruptedException {
        SynchronizedTest1 demo = new SynchronizedTest1();

        Thread a1 = new Thread(demo);
        a1.start();
        Thread a2 = new Thread(demo);
        a2.start();

        //等待两个线程停止
        a1.join();
        a2.join();

        demo.print();
    }

    @Override
    public void run() {
        for (int j = 0; j < 10000; j++) {
            increment();
        }
    }

    public void print(){
        System.out.println("i="+i);
    }
}
