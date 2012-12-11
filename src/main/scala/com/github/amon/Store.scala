/*
* Copyright 2011 P.Budzik
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*
* User: przemek
* Date: 2/25/12
* Time: 5:10 PM
*/

package com.github.amon

import com.github.bytecask.{IndexEntry, Bytes, Bytecask}


trait Store {

  def put(key: Array[Byte], value: Array[Byte])

  def get(key: Array[Byte]): Option[Bytes]

  def delete(key: Array[Byte]): Option[IndexEntry]

  def close()

  def destroy()

  def merge()

  def properties: Map[String, Any]
}

class BytecaskStore(bytecask: Bytecask) extends Store {
  def put(key: Array[Byte], value: Array[Byte]) {
    bytecask.put(key, value)
  }

  def get(key: Array[Byte]) = bytecask.get(key)

  def delete(key: Array[Byte]) = throw new RuntimeException("Not implemented") // bytecask.delete(key)

  def close() {
    bytecask.close()
  }

  def destroy() {
    bytecask.destroy()
  }

  def merge() {
    bytecask.merge()
  }

  def properties = Map("type" -> "bytecask", "directory" -> bytecask.dir)

}

class DummyStore() extends Store {

  def put(key: Array[Byte], value: Array[Byte]) {
    throw new RuntimeException("Not implemented")
  }

  def get(key: Array[Byte]) = throw new RuntimeException("Not implemented")

  def delete(key: Array[Byte]) = throw new RuntimeException("Not implemented")

  def close() {
  }

  def destroy() {
  }

  def merge() {
  }

  def properties = Map()
}