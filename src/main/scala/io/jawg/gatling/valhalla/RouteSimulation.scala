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

import java.lang.Math.floorMod

import io.gatling.commons.util.TypeHelper.TypeValidator
import io.gatling.core.Predef._
import io.gatling.core.feeder
import io.gatling.core.structure.PopulationBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.util.Random

class RouteSimulation extends Simulation {

  import Parameters._

  val httpProtocol: HttpProtocolBuilder = http.shareConnections
  val continentMap: Map[String, Seq[feeder.Record[Any]]] = csv(REGIONS_CSV_FILE).readRecords.groupBy(record => record("Continent").toString)

  def scenarios(urls: List[String]): List[PopulationBuilder] =
    urls.map { url =>
      scenario("ValhallaRouteSimulation")
        .feed(csv(REGIONS_CSV_FILE).circular)
        .feed(csv(SEED_FILE).circular)
        .exec { session =>
          val seed: Long = session("seed").as[String].toLong
          val rand = new Random(seed)

          val continent = session("Continent").as[String]
          val regionsOfContinent = continentMap(continent)
          val endRegion = regionsOfContinent(floorMod(rand.nextInt(), regionsOfContinent.length))

          val startLatMin = session("LatMin").as[String].toDouble
          val startLatMax = session("LatMax").as[String].toDouble
          val startLngMin = session("LngMin").as[String].toDouble
          val startLngMax = session("LngMax").as[String].toDouble
          val startRegion = session("Region").as[String]

          val endLatMin = endRegion("LatMin").as[String].toDouble
          val endLatMax = endRegion("LatMax").as[String].toDouble
          val endLngMin = endRegion("LngMin").as[String].toDouble
          val endLngMax = endRegion("LngMax").as[String].toDouble

          val startLng = rand.nextDouble() * (startLngMax - startLngMin) + startLngMin
          val startLat = rand.nextDouble() * (startLatMax - startLatMin) + startLatMin

          val endLng = rand.nextDouble() * (endLngMax - endLngMin) + endLngMin
          val endLat = rand.nextDouble() * (endLatMax - endLatMin) + endLatMin

          session
            .set("startLat", startLat)
            .set("startLng", startLng)
            .set("startRegion", startRegion)
            .set("endLat", endLat)
            .set("endLng", endLng)
            .set("endRegion", endRegion("Region"))
            .set("API_COSTING", API_COSTING)
        }
        .exec(
          http("${startRegion} to ${endRegion}").get(API_ROUTE)
            .queryParam("json", """{"locations":[{"lat":${startLat},"lon":${startLng}},{"lat":${endLat},"lon":${endLng}}],"costing":"${API_COSTING}","directions_type":"none"}""")
            .check(status.in(200))
        )
        .inject(rampUsers(USERS) during RAMP_TIME)
        .protocols(httpProtocol.baseUrl(url))
    }

  setUp(scenarios(VALHALLA_URLS))
}
