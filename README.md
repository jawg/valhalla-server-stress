# valhalla-server-stress

This gatling script allow you to qualify and stress a **Valhalla Server**

The Testing scenario is the following:

-   Each user will randomly select a region in the regions.csv file (round-robin strategy)
-   In this region, the user will randomly choose two positions (lat/lng)
-   Then, the user will request a routing for this positions

## Requirements

-   Java : <https://adoptopenjdk.net/>

## How to use

This script has been tested and approved by both Gatling and Gatling Frontline solutions.

-   Clone the project
    `git clone https://github.com/jawg/valhalla-server-stress.git`
-   Browse the project root and execute the following commands
    `./gradlew shadowJar` to build the project
-   Now you can generate seeds for all simulated users with this command `GENERATE_SEEDS=true ./bin/valhalla-server-stress`. Skip this step if you want to use a custom seeds file or keep the previous one and produce the exact same test.
-   In order to run the stress test use `./bin/valhalla-server-stress route`.

You can override properties with environment variables when you use the script `valhalla-server-stress`.

Available arguments are:

-    `route`: Run the route scenario.

Available environments are:

-    `GATLING_PROPERTIES`: Path to your custom property file
-    `SERVER_URL`: Set your custon server URL
-    `REGIONS_FILE`: Where your region file is
-    `SEEDS_FILE`: Where your seeds file
-    `USERS_COUNT`: Set users count for your simulation (and number of seeds)
-    `USERS_RAMP_TIME`: Set the ramp time for your users
-    `GENERATE_SEEDS`: When not empty, will generate only seeds
-    `AUTO_GENERATE_SEEDS`: When not empty, will generate seed and run the simulation
-    `API_ROUTE`: The path for the route API, default is `route`
-    `API_COSTING`: The profile to use, default is `auto`

A docker image is also available `docker build -t jawg/valhalla-server-stress .` or `docker pull jawg/valhalla-server-streess`

## License

Copyright 2020 Jawg

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

   <http://www.apache.org/licenses/LICENSE-2.0>
Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
