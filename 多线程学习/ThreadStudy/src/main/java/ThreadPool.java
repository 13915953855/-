import java.util.concurrent.*;

public class ThreadPool {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5,10,30, TimeUnit.SECONDS,new ArrayBlockingQueue<>(10),
                Executors.defaultThreadFactory(),new ThreadPoolExecutor.DiscardPolicy());
        executor.allowCoreThreadTimeOut(true);
        executor.execute(()->{
            System.out.println("测试线程池1");
        });
        Future<String> submit = executor.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "回调";
            }
        });
        System.out.println(submit.get());
        executor.shutdown();
    }
}
