package com.hao.spider.rx;

import com.hao.spider.fetch.FetcherPicture;
import lombok.extern.slf4j.Slf4j;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import java.util.List;

/**
 * @author dongh38@ziroom
 * @Date 16/9/16
 * @Time 上午10:41
 */
@Slf4j
public class RxSpider {


    public static void reactive() {
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {
                log.info("now is complete!");
            }

            @Override
            public void onError(Throwable throwable) {
                log.warn("occurred:",throwable);
            }

            @Override
            public void onNext(String s) {
                FetcherPicture.download(s);
            }
        };
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                //这里应该生产字符串
                List<String> links = FetcherPicture.allLinks();
                links.forEach(subscriber::onNext);
            }
        });
        observable.observeOn(Schedulers.computation()).subscribe(observer);

    }


    public static void main(String[] args) {
        reactive();
    }



}
