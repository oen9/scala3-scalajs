# scala3-scalajs
scala3-scalajs

## open live
https://scala3-scalajs.herokuapp.com/

## open dev
xdg-open js/src/main/resources/index-dev.html

## docker
We can't use `sbt docker:publishLocal` due to some resources bug? (it won't add any resource into .jar e.g. application.conf) so don't use it:
```
sbt Docker/publishLocal
docker login
docker tag scala3-scalajs:0.0.1 oen9/scala3-scalajs:0.0.1
docker push oen9/scala3-scalajs:0.0.1
docker run -p 8080:8080 oen9/scala3-scalajs:0.0.1
```
Use this:
```
sbt Docker/stage
cd target/docker/stage
docker build -t oen9/scala3-scalajs:0.0.1 .
docker login
docker push oen9/scala3-scalajs:0.0.1
docker run -p 8080:8080 oen9/scala3-scalajs:0.0.1
```
