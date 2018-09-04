/*
 * MVI Kotlin Copyright (C) 2018 Fatih.
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
package org.fs.mvi.todo.util

import android.util.Log
import io.reactivex.*
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import org.fs.mvi.todo.BuildConfig
import java.io.PrintWriter
import java.io.StringWriter
import java.util.concurrent.TimeUnit

inline fun <reified T> T.logEnabled(): Boolean = BuildConfig.DEBUG
inline fun <reified T> T.classTag(): String = T::class.java.simpleName
inline fun <reified T> T.log(msg: String) = log(Log.DEBUG, msg)
inline fun <reified T> T.log(error: Throwable) {
  val sw = StringWriter()
  val pw = PrintWriter(sw)
  error.printStackTrace(pw)
  log(Log.ERROR, sw.toString())
}
inline fun <reified T> T.log(level: Int, msg: String) {
  if (logEnabled()) {
    Log.println(level, classTag(), msg)
  }
}

fun <T> Observable<T>.async(): Observable<T> = subscribeOn(Schedulers.io())

fun <T> Flowable<T>.async(): Flowable<T> = subscribeOn(Schedulers.io())

fun <T> Single<T>.async(): Single<T> = subscribeOn(Schedulers.io())

fun <T> Maybe<T>.async(): Maybe<T> = subscribeOn(Schedulers.io())

fun Completable.async(): Completable = subscribeOn(Schedulers.io())

fun <T> Observable<T>.errorRetry(): Observable<T> = retryWhen { errors ->
  errors.zipWith(Observable.range(1, 3), BiFunction<Throwable, Int, Long> { _, i -> i.toLong() })
    .flatMap { retryCount -> Observable.timer(retryCount, TimeUnit.SECONDS) }
}

fun <T> Flowable<T>.errorRetry(): Flowable<T> = retryWhen { errors ->
  errors.zipWith(Flowable.range(1, 3), BiFunction<Throwable, Int, Long> { _, i -> i.toLong() })
    .flatMap { retryCount -> Flowable.timer(retryCount, TimeUnit.SECONDS) }
}

fun <T> Maybe<T>.errorRetry(): Maybe<T> = retryWhen { errors ->
  errors.zipWith(Flowable.range(1, 3), BiFunction<Throwable, Int, Long> { _, i -> i.toLong() })
    .flatMap { retryCount -> Flowable.timer(retryCount, TimeUnit.SECONDS) }
}

fun <T> Single<T>.errorRetry(): Single<T> = retryWhen { errors ->
  errors.zipWith(Flowable.range(1, 3), BiFunction<Throwable, Int, Long> { _, i -> i.toLong() })
    .flatMap { retryCount -> Flowable.timer(retryCount, TimeUnit.SECONDS) }
}

fun Completable.errorRetry(): Completable = retryWhen { errors ->
  errors.zipWith(Flowable.range(1, 3), BiFunction<Throwable, Int, Long> { _, i -> i.toLong() })
    .flatMap { retryCount -> Flowable.timer(retryCount, TimeUnit.SECONDS) }
}
