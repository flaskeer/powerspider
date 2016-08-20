package com.hao.spider.service;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixObservableCommand;
import org.junit.Test;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

/**
 * Created by donghao on 16/7/24.
 */
public class TestCommand {

    public class CommandHello extends HystrixCommand<String> {

        private final String name;

        protected CommandHello(String name) {
            super(HystrixCommandGroupKey.Factory.asKey("testGroup"));
            this.name = name;
        }

        @Override
        protected String getFallback() {
            return "hello failure " + name + "!";
        }

        @Override
        protected String run() throws Exception {
            return "hello " + name;
        }

        @Override
        protected String getCacheKey() {
            return super.getCacheKey();
        }
    }

    public class HelloWorldCommand extends HystrixObservableCommand<String> {

        private final String name;

        protected HelloWorldCommand(String name) {
            super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("testGroup"))
            .andCommandKey(HystrixCommandKey.Factory.asKey("helloWorld")));
            this.name = name;
        }

        @Override
        protected String getFallbackMethodName() {
            System.out.println("name");
            return super.getFallbackMethodName();
        }

        @Override
        protected Observable<String> resumeWithFallback() {
            return super.resumeWithFallback();
        }

        @Override
        protected Observable<String> construct() {
            return Observable.create(new Observable.OnSubscribe<String>() {
                @Override
                public void call(Subscriber<? super String> subscriber) {
                    try {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onNext("hello");
                            subscriber.onNext(name + "!");
                            subscriber.onCompleted();
                        }
                    } catch (Exception e) {
                        subscriber.onError(e);
                    }
                }
            });
        }
    }
    @Test
    public void testCommand() throws InterruptedException, ExecutionException, TimeoutException {
        HelloWorldCommand command = new HelloWorldCommand("hehe");
        Observable<String> observable = command.construct();
        observable.subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                System.out.println("success");
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println(throwable.getMessage());
            }

            @Override
            public void onNext(String s) {
                System.out.println(s);
            }
        });
        Future<String> stringFuture = observable.toBlocking().toFuture();
        System.out.println(stringFuture);


    }
}
