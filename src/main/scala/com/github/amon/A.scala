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
* Date: 12/8/12
* Time: 12:12 PM
*/

package com.github.amon

import com.esotericsoftware.kryo._
import io.{Input, Output}
import java.io._

class Person {
  var name: String = ""

  var age: Int = 0
}

class KV[T] {
  var key: String = _
  var value: T = _
}

object KV {

  def apply[T](key: String, v: T): KV[T] = {
    val kv = new KV[T]
    kv.key = key
    kv.value = v
    kv
  }
}

object MyApp extends App {
  val people = Array(new Person, new Person)
  val foo: (String, Array[Byte]) = ("ff", Array())
  val kryo = new Kryo
  kryo.setRegistrationRequired(false)
  val output = new Output(new FileOutputStream("file.bin"))
  kryo.writeObject(output, KV("111", 9))
  output.close()
  val input = new Input(new FileInputStream("file.bin"))
  val o = kryo.readObject(input, classOf[KV[Int]])
  o match {
    case kv: KV[Int] => println("key:" + kv.key)
    case any => println("Something: " + any)
  }
  input.close()


}