package com.arny.arnylib;

import com.arny.arnylib.utils.Params;
import com.arny.arnylib.utils.Stopwatch;
import com.arny.arnylib.utils.Utility;
import io.reactivex.Observable;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class LibUnitTest {
//
//    @ClassRule
//    public static final RxImmediateSchedulerRule schedulers = new RxImmediateSchedulerRule();
//
//    @BeforeClass
//    public static void setUpRxSchedulers() {
//        Scheduler immediate = new Scheduler() {
//            @Override
//            public Disposable scheduleDirect(@NonNull Runnable run, long delay, @NonNull TimeUnit unit) {
//                // this prevents StackOverflowErrors when scheduling with a delay
//                return super.scheduleDirect(run, 0, unit);
//            }
//
//            @Override
//            public Worker createWorker() {
//                return new ExecutorScheduler.ExecutorWorker(Runnable::run);
//            }
//        };
//
//        RxJavaPlugins.setInitIoSchedulerHandler(scheduler -> immediate);
//        RxJavaPlugins.setInitComputationSchedulerHandler(scheduler -> immediate);
//        RxJavaPlugins.setInitNewThreadSchedulerHandler(scheduler -> immediate);
//        RxJavaPlugins.setInitSingleSchedulerHandler(scheduler -> immediate);
//        RxAndroidPlugins.setInitMainThreadSchedulerHandler(scheduler -> immediate);
//    }

    @Before
    public void init() throws Exception {
        // override Schedulers.io()
        RxJavaPlugins.setIoSchedulerHandler(schedulerCallable -> Schedulers.trampoline());
        // override AndroidSchedulers.mainThread()
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(schedulerCallable -> Schedulers.trampoline());
    }

    @Test
    public void rx() throws Exception {
        Stopwatch stopwatch = new Stopwatch();
        stopwatch.start();
        System.out.println("rx: start:" + Utility.getThread() + " time:" + stopwatch.getElapsedTimeSecs(3));
        Utility.mainThreadObservable(Observable.fromCallable(() -> {
            System.out.println("rx: in:" + Utility.getThread() + " time:" + stopwatch.getElapsedTimeSecs(3));
            return 0;
        })).delay(10, TimeUnit.SECONDS).subscribe(integer -> {
            System.out.println("rx: end in thread:" + Utility.getThread() + " time:" + stopwatch.getElapsedTimeSecs(3));
        }, Throwable::printStackTrace);
    }

    //    @Test
    public void emails_isCorrect() throws Exception {
        List<String> list = Arrays.asList("","4844","@","a@m.r","a@.ru");
        for (String s : list) {
            assertTrue(!Utility.isEmailValid(s));
        }
    }

    //    @Test
    public void test_is_empty() throws Exception {
        assertTrue(Utility.empty(""));
        assertTrue(Utility.empty(null));
        assertTrue(Utility.empty("null"));
    }

    //    @Test
    public void params() throws Exception {
        Params params = new Params("{}");
        assertThat(!Utility.empty(params));
        assertThat(params.toString()).isNull();
        Params params1 = new Params("param1=1");
        assertThat(params1).isNotNull();
    }


}