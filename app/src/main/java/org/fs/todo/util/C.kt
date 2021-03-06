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
package org.fs.todo.util

sealed class C {
  companion object {
    const val DATABASE_NAME = "todo.db"
    const val DATABASE_VERSION = 1

    const val ENTRY_TABLE_NAME = "entries"


    const val REFRESH = 0x01
    const val CREATE = 0x02
    const val DELETE = 0x03
    const val UPDATE = 0x04
  }
}