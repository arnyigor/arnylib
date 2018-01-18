package com.arny.arnylib.utils;

public class TestingUtils {
    public static boolean londTimeConnection(int millis, boolean error) throws Exception {
        Stopwatch stopwatch = new Stopwatch();
        stopwatch.start();
        System.out.println("londTimeConnection: started millis:" + millis + " error:" + error);
        try {
            Thread.sleep(millis);
            if (error) {
                throw new Exception("Connection failed");
            }
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("londTimeConnection: finished:" + stopwatch.getElapsedTimeSecs(3) + " sec");
        stopwatch.stop();
        return false;
    }
}
