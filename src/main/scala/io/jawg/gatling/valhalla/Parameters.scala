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

import com.typesafe.config.{Config, ConfigFactory}

import scala.concurrent.duration._

object Parameters {

  private val propertiesFromSystem = ConfigFactory.systemProperties()
  private val propertiesFromFile = fileProperties()
  private val properties = propertiesFromSystem.withFallback(propertiesFromFile)

  // Url of the server to stress test
  val VALHALLA_URLS: List[String] = properties.getString("server.url").trim().split(",").toList

  // Url of the server to stress test
  val API_ROUTE: String = properties.getString("api.route").trim()

  // Costing to use "auto", "bicycle", "bus", "truck", "hov", "taxi", "motor_scooter", "multimodal", "pedestrian"
  val API_COSTING: String = properties.getString("api.costing").trim()

  // File to load containing the region rectangles where users will choose their initial latitudes and longitudes.
  // regions.csv contains an example of the format used.
  val REGIONS_CSV_FILE: String = properties.getString("simulation.regions")

  // File to load containing the region rectangles where users will choose their initial latitudes and longitudes.
  // regions.csv contains an example of the format used.
  val SEED_FILE: String = properties.getString("simulation.seeds")

  // Amount of users. Users will be dispatched as equally as possible across regions.
  val USERS: Int = properties.getString("simulation.users.count").toInt

  // Users amount can be ramped up over this duration in seconds
  val RAMP_TIME: FiniteDuration = properties.getString("simulation.users.ramp.time").toInt.seconds

  private def fileProperties(): Config = {
    val defaultProperties = ConfigFactory.load("parameters.properties")
    if (!propertiesFromSystem.hasPath("properties")) {
      defaultProperties
    } else {
      ConfigFactory.parseFileAnySyntax(new File(propertiesFromSystem.getString("properties")))
        .withFallback(defaultProperties)
    }
  }
}