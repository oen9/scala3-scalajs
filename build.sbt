val ver = new {
  val http4s         = "0.23.4"
  val catsEffect     = "3.2.9"
  val log4cats       = "2.1.1"
  val logback        = "1.2.5"
  val circe          = "0.14.1"
  val typesafeConfig = "1.4.1"
}

val scala3Version = "3.0.2"
val scala2Version = "2.13.6"

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
  scalaVersion := scala2Version,
  libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "1.2.0" // scala3 not supported yet
  ),
  Compile / npmDependencies ++= Seq(),
  scalaJSUseMainModuleInitializer := true,
  webpack / version               := "4.46.0",
  webpackBundlingMode             := BundlingMode.Application,
  fastOptJS / webpackBundlingMode := BundlingMode.LibraryOnly()
)

lazy val jvmSettings = Seq(
  libraryDependencies ++= Seq(
    "org.typelevel" %% "log4cats-core"       % ver.log4cats,
    "org.typelevel" %% "log4cats-slf4j"      % ver.log4cats,
    "ch.qos.logback" % "logback-classic"     % ver.logback,
    "org.http4s"    %% "http4s-dsl"          % ver.http4s,
    "org.http4s"    %% "http4s-blaze-server" % ver.http4s,
    "org.http4s"    %% "http4s-blaze-client" % ver.http4s,
    "org.http4s"    %% "http4s-circe"        % ver.http4s,
    "org.typelevel" %% "cats-effect"         % ver.catsEffect,
    "io.circe"      %% "circe-core"          % ver.circe,
    "io.circe"      %% "circe-generic"       % ver.circe,
    "com.typesafe"   % "config"              % ver.typesafeConfig
  ),
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
