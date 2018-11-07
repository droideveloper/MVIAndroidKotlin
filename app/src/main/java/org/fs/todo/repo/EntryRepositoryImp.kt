/*
 * MVI App Copyright (C) 2018 Fatih.
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
package org.fs.todo.repo

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import org.fs.todo.model.Entry
import org.fs.todo.model.EntryState
import org.fs.todo.model.db.EntryDao
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EntryRepositoryImp @Inject constructor(private val dao: EntryDao): EntryRepository {

  override fun all(): Observable<List<Entry>> = dao.load().doRetry().toObservable()

  override fun loadByState(state: EntryState): Observable<List<Entry>> = dao.loadByState(state.ordinal).doRetry().toObservable()

  override fun create(entry: Entry): Completable = Completable.fromAction { dao.create(entry) }.doRetry()

  override fun update(entry: Entry): Completable = Completable.fromAction { dao.update(entry) }.doRetry()

  override fun delete(entry: Entry): Completable = Completable.fromAction { dao.delete(entry) }.doRetry()

  private fun Completable.doRetry(): Completable = retryWhen { errors ->
    errors.zipWith(Flowable.range(1, 3), BiFunction<Throwable, Int, Long> { _, i -> i.toLong() })
      .flatMap { source -> Flowable.timer(source, TimeUnit.SECONDS, Schedulers.io()) }
  }

  private fun <T> Single<T>.doRetry(): Single<T> = retryWhen { errors ->
    errors.zipWith(Flowable.range(1, 3), BiFunction<Throwable, Int, Long> { _, i -> i.toLong() })
      .flatMap { source -> Flowable.timer(source, TimeUnit.SECONDS, Schedulers.io()) }
  }
}