val scala3Version = "3.0.2"

lazy val sharedSettings = Seq(
  libraryDependencies ++= Seq(),
  name             := "scala3-scalajs",
  scalaVersion     := scala3Version,
  version          := "0.1.0-SNAPSHOT",
  organization     := "com.github.oen9",
  organizationName := "oen9",
  scalacOptions ++= Seq(
    "-unchecked",
    "-deprecation",
    "-feature",
    "-language:higherKinds"
  )
)

lazy val jsSettings = Seq(
  libraryDependencies ++= Seq(),
  Compile / npmDependencies ++= Seq(),
  scalaJSUseMainModuleInitializer := true,
  webpack / version               := "4.46.0",
  webpackBundlingMode             := BundlingMode.Application,
  fastOptJS / webpackBundlingMode := BundlingMode.LibraryOnly()
)

lazy val jvmSettings = Seq(
  libraryDependencies ++= Seq(
    "com.novocode" % "junit-interface" % "0.11" % "test"
  ),
  target := baseDirectory.value / ".." / "target"
)

lazy val app =
  crossProject(JSPlatform, JVMPlatform)
    .crossType(CrossType.Full)
    .in(file("."))
    .settings(sharedSettings)
    .jsSettings(jsSettings)
    .jvmSettings(jvmSettings)

lazy val appJS = app.js
  .enablePlugins(ScalaJSBundlerPlugin)
  .disablePlugins(RevolverPlugin)

lazy val appJVM = app.jvm
  .enablePlugins(JavaAppPackaging)
  .settings(
    Compile / unmanagedResourceDirectories += (appJS / Compile / resourceDirectory).value,
    Universal / mappings ++= (appJS / Compile / fullOptJS / webpack).value.map { f =>
      f.data -> s"assets/${f.data.getName()}"
    },
    bashScriptExtraDefines += """addJava "-Dassets=${app_home}/../assets""""
  )

disablePlugins(RevolverPlugin)
