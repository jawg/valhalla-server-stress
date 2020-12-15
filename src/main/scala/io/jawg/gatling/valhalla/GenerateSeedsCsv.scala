/*
 * Copyright 2020 Jawg
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.jawg.gatling.valhalla

import java.io.File
import java.io.IOException
import java.io.PrintWriter
import java.util.Random

object GenerateSeedsCsv {

  @throws[IOException]
  def main(args: Array[String]): Unit = {
    val file = new File(Parameters.SEED_FILE)
    file.createNewFile
    val writer = new PrintWriter(file)
    val rand = new Random
    writer.append("seed")
    writer.append("\n")
    writer.flush()

    for (_ <- 0 until Parameters.USERS) {
      val randInt = Math.abs(rand.nextInt)
      writer.append(String.valueOf(randInt))
      writer.append("\n")
      writer.flush()
    }
  }
}