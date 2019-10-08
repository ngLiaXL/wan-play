/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ldroid.kwei;


import com.ldroid.kwei.exception.ExceptionHandler;
import com.ldroid.kwei.transformer.ErrorTransformer;
import com.ldroid.kwei.transformer.SchedulerTransformer;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.observers.DisposableObserver;


public class UseCaseHandler {

    private final CompositeDisposable mDisposables;

    public UseCaseHandler() {
        mDisposables = new CompositeDisposable();
    }

    public <T extends UseCase.RequestValues, R extends UseCase.ResponseValue> void execute(
            final UseCase<T, R> useCase, UseCase.UseCaseCallback<R> callback) {
        execute(useCase, new SchedulerTransformer<R>(), callback);
    }

    public <T extends UseCase.RequestValues, R extends UseCase.ResponseValue> void execute(
            final UseCase<T, R> useCase, ObservableTransformer<R, R> composer, UseCase.UseCaseCallback<R> callback) {
        Observable<R> observable = useCase.buildObservable()
                .compose(composer)
                .compose(new ErrorTransformer<R>());
        addDisposable(observable.subscribeWith(new DefaultObserver<>(callback)));

    }


    public <T1 extends UseCase.RequestValues, T2 extends UseCase.RequestValues,
            R1 extends UseCase.ResponseValue, R2 extends UseCase.ResponseValue, R> void execute2(
            UseCase<T1, R1> useCase1, UseCase<T2, R2> useCase2, final ZipCallback<R1, R2, R> zipCallback,
            UseCase.UseCaseCallback<R> resultCallback) {
        execute2(useCase1, useCase2, new SchedulerTransformer<R>(), zipCallback, resultCallback);
    }


    public <T1 extends UseCase.RequestValues, T2 extends UseCase.RequestValues,
            R1 extends UseCase.ResponseValue, R2 extends UseCase.ResponseValue, R> void execute2(
            UseCase<T1, R1> useCase1, UseCase<T2, R2> useCase2, ObservableTransformer<R, R> composer,
            final ZipCallback<R1, R2, R> zipCallback,
            UseCase.UseCaseCallback<R> resultCallback) {
        Observable<R> observable = Observable.zip(useCase1.buildObservable(), useCase2.buildObservable(), new BiFunction<R1, R2, R>() {
            @Override
            public R apply(R1 r1, R2 r2) {
                return zipCallback.apply(r1, r2);
            }
        }).compose(composer).compose(new ErrorTransformer<R>());
        addDisposable(observable.subscribeWith(new DefaultObserver<>(resultCallback)));
    }


    public interface ZipCallback<R1, R2, R> {
        R apply(R1 r1, R2 r2);
    }


    private static final class DefaultObserver<R> extends
            DisposableObserver<R> {

        private final UseCase.UseCaseCallback<R> mCallback;

        public DefaultObserver(UseCase.UseCaseCallback<R> mCallback) {
            this.mCallback = mCallback;
        }

        @Override
        public void onNext(R r) {
            mCallback.onSuccess(r);
        }

        @Override
        public void onComplete() {

        }

        @Override
        public void onError(Throwable exception) {
            mCallback.onError(ExceptionHandler.handleException(exception));
        }
    }


    public void dispose() {
        mDisposables.clear();
    }

    private void addDisposable(Disposable disposable) {
        mDisposables.add(disposable);
    }


}
