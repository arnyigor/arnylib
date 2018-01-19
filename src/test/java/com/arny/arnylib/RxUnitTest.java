package com.arny.arnylib;

import com.arny.arnylib.utils.Params;
import com.arny.arnylib.utils.Stopwatch;
import com.arny.arnylib.utils.TestingUtils;
import com.arny.arnylib.utils.Utility;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.observers.TestObserver;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

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
public class RxUnitTest {

    private boolean err = true;
    private Stopwatch stopwatch;
    private int retryCount;
    private int currentRetries;
    private static class ImmediateSchedulersRule implements TestRule {
        @Override
        public Statement apply(final Statement base, Description description) {
            return new Statement() {
                @Override
                public void evaluate() throws Throwable {
                    RxJavaPlugins.setIoSchedulerHandler(scheduler -> Schedulers.trampoline());
                    RxJavaPlugins.setComputationSchedulerHandler(scheduler -> Schedulers.trampoline());
                    RxJavaPlugins.setNewThreadSchedulerHandler(scheduler -> Schedulers.trampoline());

					try {
						base.evaluate();
					} finally {
						RxJavaPlugins.reset();
					}
				}
			};
		}
	}

	@Rule
	public final ImmediateSchedulersRule schedulers = new ImmediateSchedulersRule();

	@Test
    public void testRxRetry() {
        stopwatch = new Stopwatch();
        stopwatch.start();
        retryCount = 0;
        currentRetries = 5;//текущее количество попыток
        int maxRetries = 10;//максимальное количество попыток
        int connectionTime = 230;// время долгой операции
        int minChangeTime = 250;//минимальное время для изменения количества попыток
        int retryDiff = 2;//изменение количества попыток
        TestObserver<Boolean> observer = new TestObserver<>();
        System.out.println("rx start time:" + stopwatch.getElapsedTimeSecs(3));
        Observable.fromCallable(() -> TestingUtils.londTimeConnection(connectionTime, err))
                .retryWhen(throwableObservable -> throwableObservable
                        .flatMap((Function<Throwable, Observable<?>>) throwable -> {
                            //Меняем условия возникновения ошибки,ошибка уходит на последней попытке,в зависимости от количества
                            boolean errCond = retryCount == ((connectionTime > minChangeTime) ? (currentRetries - 2) : maxRetries - 2);
                            if (errCond) {
                                err = false;
                            }

                            long elapsedTimeMili = stopwatch.getElapsedTimeMili();
                            System.out.println("retryWhen err:" + err + " maxRetries:" + maxRetries + " retryCount:" + retryCount + " currentRetries:" + currentRetries + " time:" + elapsedTimeMili);
                            stopwatch.restart();
                            //замерили время операции,если меньше минимального - меняем количество
                            if (elapsedTimeMili <= minChangeTime) {
                                currentRetries += retryDiff;
                            }
                            //ограничиваем максимальным количеством
                            if (currentRetries >= maxRetries) {
                                currentRetries = maxRetries;
                            }
                            //увеличиваем и проверяем с текущем количеством и пробуем снова
                            if (++retryCount < currentRetries) {
                                return Observable.timer(1, TimeUnit.MILLISECONDS);
                            }
                            return Observable.error(throwable);
                        }))
                .subscribeOn(Schedulers.computation()).subscribe(observer);
        observer.assertComplete();
        observer.assertNoErrors();
        System.out.println("rx: end  time:" + stopwatch.getElapsedTimeSecs(3));
        stopwatch.stop();
    }

	//    @Test
	public void emails_isCorrect() throws Exception {
		List<String> list = Arrays.asList("", "4844", "@", "a@m.r", "a@.ru");
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