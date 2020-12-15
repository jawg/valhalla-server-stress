rootProject.name = "valhalla-server-stress"

pluginManagement {
  plugins {
    id("com.github.ben-manes.versions") version "${extra["version.versions-plugin"]}"
    id("com.github.johnrengelman.shadow") version "${extra["version.shadow-plugin"]}"
  }
}
