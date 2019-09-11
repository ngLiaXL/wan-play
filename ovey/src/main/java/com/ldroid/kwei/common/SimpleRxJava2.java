package com.ldroid.kwei.common;

import com.ldroid.kwei.common.trace.Timber;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ngliaxl on 2018/4/9.
 *
 *         SimpleRxJava2.create(new SimpleRxJava2.EmitterCallback<String>() {
 *             @Override
 *             public void getEmitDatas(List<String> datas) throws Exception {
 *                 Log.d("OWEN", "getEmitDatas: 1111  " + Thread.currentThread().getName());
 *                 datas.add("1");
 *             }
 *         }).switchThread().concatMap(new SimpleRxJava2.FunctionCallback<String, Integer>() {
 *             @Override
 *             public SimpleRxJava2<Integer> apply(String s) throws Exception {
 *                 Log.d("OWEN", "apply:1111  " + Thread.currentThread().getName());
 *                 if(s.equals("")){
 *                     return null;
 *                 }else{
 *                     return SimpleRxJava2.create(new SimpleRxJava2.EmitterCallback<Integer>() {
 *                         @Override
 *                         public void getEmitDatas(List<Integer> datas) throws Exception {
 *                             Log.d("OWEN", "getEmitDatas: 2222  " + Thread.currentThread().getName());
 *                             datas.add(2);
 *                         }
 *                     }).switchThread();
 *                 }
 *             }
 *         }).concatMap(new SimpleRxJava2.FunctionCallback<Integer, Boolean>() {
 *             @Override
 *             public SimpleRxJava2<Boolean> apply(Integer integer) throws Exception {
 *                 Log.d("OWEN", "apply:2222  " + Thread.currentThread().getName());
 *
 *                 return SimpleRxJava2.create(new SimpleRxJava2.EmitterCallback<Boolean>() {
 *                     @Override
 *                     public void getEmitDatas(List<Boolean> datas) throws Exception {
 *                         Log.d("OWEN", "getEmitDatas: 3333  " + Thread.currentThread().getName());
 *                         datas.add(Boolean.TRUE);
 *                     }
 *                 }).switchThread();
 *             }
 *         })
 *           .deliveryResult(new SimpleRxJava2.ConsumerCallback<Boolean>(){
 *             @Override
 *             public void onReceive(Boolean data) {
 *                 Log.d("OWEN", "============= : " + data + Thread.currentThread().getName());
 *             }
 *         });
 */
public class SimpleRxJava2<T> {

    private Observable<T> mObservable;

    public SimpleRxJava2() {
    }

    public SimpleRxJava2(Observable<T> observable) {
        this.mObservable = observable;
    }


    public static <T> SimpleRxJava2<T> create(final EmitterCallback<T> callback) {
        Observable<T> observable = Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> emitter) {
                List<T> datas = new ArrayList<>();
                Throwable trowable = null;
                try {
                    callback.getEmitDatas(datas);
                } catch (Exception e) {
                    e.printStackTrace();
                    trowable = e;
                } finally {
                    for (T data : datas) {
                        emitter.onNext(data);
                    }
                    if (!emitter.isDisposed() && trowable != null) emitter.onError(trowable);
                    else emitter.onComplete();
                }
            }
        });
        return new SimpleRxJava2<>(observable);
    }

    public static <T> SimpleRxJava2<T> create2(final EmitterCallback<T> callback) {
        return create(callback).switchThread();
    }


    public static <T> SimpleRxJava2<T> empty() {
        return SimpleRxJava2.create(new SimpleRxJava2.EmitterCallback<T>() {
            @Override
            public void getEmitDatas(List<T> datas) {

            }
        }).switchThread();
    }


    public static <T> SimpleRxJava2<T> createFrom(final FutureCallback<T> callback) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        final FutureTask<T> future = new FutureTask<>(new Callable<T>() {
            @Override
            public T call() throws InterruptedException {
                return callback.call();
            }
        });
        service.submit(future);
        Observable<T> observable = Observable.fromFuture(future);
        return new SimpleRxJava2<>(observable);
    }

    public static <T> T getFromFuture(final FutureCallback<T> callback) {
        final List<T> t = new ArrayList<>();
        SimpleRxJava2.createFrom(callback).deliveryResult(new ConsumerCallback<T>() {
            @Override
            public void onReceive(T data) {
                t.add(data);
            }
        });
        return t.isEmpty() ? null : t.get(0);
    }


    public <Q> SimpleRxJava2<Q> transform(final Transform<T, Q> callback) {
        Observable<Q> observable = getObservable().map(new Function<T, Q>() {
            @Override
            public Q apply(T t) throws Exception {
                return callback.apply(t);
            }
        });
        return new SimpleRxJava2<>(observable);
    }


    public <Q> SimpleRxJava2<Q> concatMap(final FunctionCallback<T, Q> callback) {
        Observable<Q> observable = getObservable().concatMap(new Function<T, ObservableSource<Q>>() {
            @Override
            public ObservableSource<Q> apply(T t) throws Exception {
                SimpleRxJava2<Q> apply = callback.apply(t);
                if (apply != null) return apply.getObservable();
                return null;
            }
        });
        return new SimpleRxJava2<>(observable);
    }


    public <H, Q> SimpleRxJava2<T> compose(SimpleRxJava2<H> s1, SimpleRxJava2<Q> s2, final
    ApplyCallback<H, Q, T> callback) {
        Observable<T> observable = Observable.zip(s1.getObservable(), s2.getObservable(), new
                BiFunction<H, Q, T>() {
                    @Override
                    public T apply(H h, Q q) throws Exception {
                        return callback.apply(h, q);
                    }
                });
        return new SimpleRxJava2<>(observable);
    }


    public SimpleRxJava2<T> deliveryResult(final ConsumerCallback<T> callback) {
        getObservable().subscribe(new Observer<T>() {
            @Override
            public void onSubscribe(Disposable d) {
                if (callback != null) {
                    callback.onStart(d);
                }
            }

            @Override
            public void onNext(T t) {
                if (callback != null) {
                    callback.onReceive(t);
                }
            }

            @Override
            public void onError(Throwable e) {
                if (!e.getMessage().contains("Future returned null")
                        && !e.getMessage().contains("onNext called with null")) {
                    Timber.e(e);
                }
                if (callback != null) {
                    callback.onError(e);
                }
            }

            @Override
            public void onComplete() {
                if (callback != null)
                    callback.onComplete();
            }
        });
        return this;
    }

    public interface EmitterCallback<T> {
        void getEmitDatas(List<T> datas) throws Exception;
    }

    public interface FutureCallback<T> {
        T call();
    }


    public interface FunctionCallback<T, Q> {
        SimpleRxJava2<Q> apply(@NonNull T t) throws Exception;
    }


    public interface Transform<T, Q> {
        Q apply(T t);
    }

    public interface ApplyCallback<H, Q, T> {
        T apply(H h, Q q);
    }

    public static class ConsumerCallback<T> {

        public void onStart(Disposable d) {
        }

        public void onReceive(T data) {
        }

        public void onError(Throwable t) {
        }

        public void onComplete() {
        }
    }


    public static Scheduler getIoScheduler(){
        return Schedulers.io();
    }

    public SimpleRxJava2<T> subscribeOnIO() {
        return new SimpleRxJava2<>(getObservable().subscribeOn(Schedulers.io()));
    }

    public SimpleRxJava2<T> subscribeOnMain() {
        return subscribeOn(AndroidSchedulers.mainThread());
    }

    public SimpleRxJava2<T> subscribeOnLine() {
        return subscribeOn(Schedulers.trampoline());
    }

    public SimpleRxJava2<T> subscribeOn(Scheduler scheduler) {
        return new SimpleRxJava2<>(getObservable().subscribeOn(scheduler));
    }

    public SimpleRxJava2<T> observeOnIO() {
        return observeOn(Schedulers.io());
    }

    public SimpleRxJava2<T> observeOnMain() {
        return observeOn(AndroidSchedulers.mainThread());
    }

    public SimpleRxJava2<T> observeOn(Scheduler scheduler) {
        return new SimpleRxJava2<>(getObservable().observeOn(scheduler));
    }


    public SimpleRxJava2<T> switchThread() {
        return new SimpleRxJava2<>(getObservable().subscribeOn(Schedulers.io()).observeOn
                (AndroidSchedulers.mainThread()));
    }

    public SimpleRxJava2<T> singleThread() {
        Scheduler scheduler = Schedulers.from(Executors.newSingleThreadExecutor());
        return new SimpleRxJava2<>(getObservable().subscribeOn(scheduler).observeOn
                (scheduler));
    }

    public Observable<T> getObservable() {
        return mObservable;
    }
}
