package com.arny.arnylib;

import android.util.Log;
import com.arny.arnylib.utils.Params;
import com.arny.arnylib.utils.Stopwatch;
import com.arny.arnylib.utils.Utility;
import io.reactivex.Observable;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.observers.TestObserver;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import org.junit.Before;

import static java.util.concurrent.TimeUnit.SECONDS;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class LibUnitTest {

	private Stopwatch stopwatch;
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
	public void testUsingImmediateSchedulersRule() {
		// given:
		stopwatch = new Stopwatch();
		stopwatch.start();
		TestObserver<String> observer = new TestObserver<>();
		System.out.println("rx: start:" + Utility.getThread() + " time:" + stopwatch.getElapsedTimeSecs(3));
		Observable.fromCallable(() -> {
			System.out.println("rx: fromCallable:" + Utility.getThread() + " time:" + stopwatch.getElapsedTimeSecs(3));
			return londTimeFunctionString();
		}).subscribeOn(Schedulers.computation()).subscribe(observer);
		// then:
		observer.assertComplete();
		observer.assertNoErrors();
		System.out.println("rx: end:" + Utility.getThread() + " time:" + stopwatch.getElapsedTimeSecs(3));
		stopwatch.stop();
	}

	private String londTimeFunctionString() throws Exception {
		Stopwatch stopwatch = new Stopwatch();
		stopwatch.start();
		System.out.println( "londTimeFunctionString: started:");
		try {
			for (int i = 0; i < 5; i++) {
				Thread.sleep(1000);
				System.out.println( "londTimeFunctionString update in thread " + Utility.getThread() + ": time:" + stopwatch.getElapsedTimeSecs(3) + " sec");
				if (i == 3) {
					throw new Exception("Ops");
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println( "londTimeFunctionString: finished:" + stopwatch.getElapsedTimeSecs(3) + " sec");
		stopwatch.stop();
		return "long_time_function_result";
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