package com.ldroid.kwei.transformer;

import com.ldroid.kwei.UseCase;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class SchedulerTrampolineTransformer<T extends UseCase.ResponseValue> implements ObservableTransformer<T, T> {

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream.subscribeOn(Schedulers.trampoline()).observeOn(Schedulers.trampoline());
    }
}
