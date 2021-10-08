# scala3-scalajs
scala3-scalajs

## open live
https://scala3-scalajs.herokuapp.com/

## open dev
xdg-open js/src/main/resources/index-dev.html

## run
```
sbt stage
./jvm/target/universal/stage/bin/scala3-scalajs
```

## docker
`docker login`
### auto:
`sbt appJVM/Docker/publish`
### manually:
```
sbt appJVM/Docker/publishLocal
docker tag scala3-scalajs:0.0.1 oen9/scala3-scalajs:0.0.1
docker push oen9/scala3-scalajs:0.0.1
```
### manally2:
```
sbt appJVM/Docker/stage
cd jvm/target/docker/stage
docker build -t oen9/scala3-scalajs:0.0.1 .
docker push oen9/scala3-scalajs:0.0.1
```
### run
`docker run -p 8080:8080 oen9/scala3-scalajs:0.0.1`
