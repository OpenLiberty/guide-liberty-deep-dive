#!/bin/bash
set -euxo pipefail


echo ===== Test module-getting-started =====
cd module-getting-started || exit

<< 'MULTILINE-COMMENT'
mvn -Dhttp.keepAlive=false \
    -Dmaven.wagon.http.pool=false \
    -Dmaven.wagon.httpconnectionManager.ttlSeconds=120 \
    -q clean package liberty:create liberty:install-feature liberty:deploy

mvn liberty:start
curl -s http://localhost:9080/inventory/api/systems | grep "\\[\\]" || exit 1

curl -s http://localhost:9080/inventory/api/systems | grep "\\[\\]" || exit 1

status="$(curl --write-out "%{http_code}\n" --silent --output /dev/null -X POST "http://localhost:9080/inventory/api/systems?hostname=localhost&osName=mac&javaVersion=11&heapSize=1")"
echo status="$status"
if [ "$status" -ne 200 ] ; then exit $?; fi

curl -s http://localhost:9080/inventory/api/systems | grep localhost || exit 1

status="$(curl --write-out "%{http_code}\n" --silent --output /dev/null -X POST "http://localhost:9080/inventory/api/systems?hostname=localhost&osName=mac&javaVersion=11&heapSize=1")"
echo status="$status"
if [ "$status" -ne 400 ] ; then exit $?; fi

curl -s http://localhost:9080/inventory/api/systems/localhost | grep localhost || exit 1

status="$(curl --write-out "%{http_code}\n" --silent --output /dev/null -X PUT "http://localhost:9080/inventory/api/systems/localhost?osName=mac&javaVersion=17&heapSize=2")"
echo status="$status"
if [ "$status" -ne 200 ] ; then exit $?; fi

status="$(curl --write-out "%{http_code}\n" --silent --output /dev/null -X PUT "http://localhost:9080/inventory/api/systems/unknown?osName=mac&javaVersion=17&heapSize=2")"
echo status="$status"
if [ "$status" -ne 400 ] ; then exit $?; fi

curl -X DELETE http://localhost:9080/inventory/api/systems/unknown | grep "does not exists" || exit 1

curl -X DELETE http://localhost:9080/inventory/api/systems/localhost | grep removed || exit 1

curl -X POST http://localhost:9080/inventory/api/systems/client/localhost | grep "not implemented" || exit 1

mvn liberty:stop

echo ===== Test module-openapi =====
cd ../module-openapi || exit
mvn -Dhttp.keepAlive=false \
    -Dmaven.wagon.http.pool=false \
    -Dmaven.wagon.httpconnectionManager.ttlSeconds=120 \
    -q clean package liberty:create liberty:install-feature liberty:deploy

mvn liberty:start

curl -s http://localhost:9080/inventory/api/systems | grep "\\[\\]" || exit 1

curl -s http://localhost:9080/inventory/api/systems | grep "\\[\\]" || exit 1

status="$(curl --write-out "%{http_code}\n" --silent --output /dev/null -X POST "http://localhost:9080/inventory/api/systems?hostname=localhost&osName=mac&javaVersion=11&heapSize=1")"
echo status="$status"
if [ "$status" -ne 200 ] ; then exit $?; fi

curl -s http://localhost:9080/inventory/api/systems | grep localhost || exit 1

status="$(curl --write-out "%{http_code}\n" --silent --output /dev/null -X POST "http://localhost:9080/inventory/api/systems?hostname=localhost&osName=mac&javaVersion=11&heapSize=1")"
echo status="$status"
if [ "$status" -ne 400 ] ; then exit $?; fi

curl -s http://localhost:9080/inventory/api/systems/localhost | grep localhost || exit 1

status="$(curl --write-out "%{http_code}\n" --silent --output /dev/null -X PUT "http://localhost:9080/inventory/api/systems/localhost?osName=mac&javaVersion=17&heapSize=2")"
echo status="$status"
if [ "$status" -ne 200 ] ; then exit $?; fi

status="$(curl --write-out "%{http_code}\n" --silent --output /dev/null -X PUT "http://localhost:9080/inventory/api/systems/unknown?osName=mac&javaVersion=17&heapSize=2")"
echo status="$status"
if [ "$status" -ne 400 ] ; then exit $?; fi

curl -X DELETE http://localhost:9080/inventory/api/systems/unknown | grep "does not exists" || exit 1

curl -X DELETE http://localhost:9080/inventory/api/systems/localhost | grep removed || exit 1

curl -X POST http://localhost:9080/inventory/api/systems/client/localhost | grep "not implemented" || exit 1

mvn liberty:stop

echo ===== Test module-config =====
cd ../module-config || exit
mvn -Dhttp.keepAlive=false \
    -Dmaven.wagon.http.pool=false \
    -Dmaven.wagon.httpconnectionManager.ttlSeconds=120 \
    -q clean package liberty:create liberty:install-feature liberty:deploy

mvn liberty:start

curl -s http://localhost:9080/inventory/api/systems | grep "\\[\\]" || exit 1

curl -s http://localhost:9080/inventory/api/systems | grep "\\[\\]" || exit 1

status="$(curl --write-out "%{http_code}\n" --silent --output /dev/null -X POST "http://localhost:9080/inventory/api/systems?hostname=localhost&osName=mac&javaVersion=11&heapSize=1")"
echo status="$status"
if [ "$status" -ne 200 ] ; then exit $?; fi

curl -s http://localhost:9080/inventory/api/systems | grep localhost || exit 1

status="$(curl --write-out "%{http_code}\n" --silent --output /dev/null -X POST "http://localhost:9080/inventory/api/systems?hostname=localhost&osName=mac&javaVersion=11&heapSize=1")"
echo status="$status"
if [ "$status" -ne 400 ] ; then exit $?; fi

curl -s http://localhost:9080/inventory/api/systems/localhost | grep localhost || exit 1

status="$(curl --write-out "%{http_code}\n" --silent --output /dev/null -X PUT "http://localhost:9080/inventory/api/systems/localhost?osName=mac&javaVersion=17&heapSize=2")"
echo status="$status"
if [ "$status" -ne 200 ] ; then exit $?; fi

status="$(curl --write-out "%{http_code}\n" --silent --output /dev/null -X PUT "http://localhost:9080/inventory/api/systems/unknown?osName=mac&javaVersion=17&heapSize=2")"
echo status="$status"
if [ "$status" -ne 400 ] ; then exit $?; fi

curl -X DELETE http://localhost:9080/inventory/api/systems/unknown | grep "does not exists" || exit 1

curl -X DELETE http://localhost:9080/inventory/api/systems/localhost | grep removed || exit 1

curl -X POST http://localhost:9080/inventory/api/systems/client/localhost | grep "5555" || exit 1

mvn liberty:stop
MULTILINE-COMMENT

echo ===== Test module-jwt =====
cd ../module-jwt || exit

mkdir ./src/main/java/io/openliberty/deepdive/rest/health
cp ../module-health-checks/src/main/java/io/openliberty/deepdive/rest/health/StartupCheck.java ./src/main/java/io/openliberty/deepdive/rest/health
cp ../module-health-checks/src/main/java/io/openliberty/deepdive/rest/health/LivenessCheck.java ./src/main/java/io/openliberty/deepdive/rest/health
cp ../module-health-checks/src/main/java/io/openliberty/deepdive/rest/health/ReadinessCheck.java ./src/main/java/io/openliberty/deepdive/rest/health
cp ../module-metrics/src/main/liberty/config/server.xml ./src/main/liberty/config/server.xml
cp ../module-metrics/src/main/java/io/openliberty/deepdive/rest/SystemResource.java ./src/main/java/io/openliberty/deepdive/rest/SystemResource.java

mvn -Dhttp.keepAlive=false \
    -Dmaven.wagon.http.pool=false \
    -Dmaven.wagon.httpconnectionManager.ttlSeconds=120 \
    -q clean package liberty:create liberty:install-feature liberty:deploy

mvn liberty:start

cd ../postgres || exit
docker build -t postgres-sample .
docker run --name postgres-container -p 5432:5432 -d postgres-sample

cd ../system || exit

mvn liberty:start

curl -k --user bob:bobpwd -X POST 'https://localhost:9443/inventory/api/systems/client/localhost' | grep "was added" || exit 1

curl 'http://localhost:9080/inventory/api/systems' | grep "\"heapSize\":" || exit 1

echo ===== Test module-health-checks =====

cd ../module-jwt || exit

curl http://localhost:9080/health/started | grep "\"status\":" || exit 1
curl http://localhost:9080/health/live | grep "\"status\":" || exit 1
curl http://localhost:9080/health/ready | grep "\"status\":\"UP\"" || exit 1

echo ===== Test module-metrics =====

curl -k --user bob:bobpwd -X DELETE https://localhost:9443/inventory/api/systems/localhost
curl -X POST http://localhost:9080/inventory/api/systems?heapSize=1048576&hostname=localhost&javaVersion=9&osName=linux
curl -k --user alice:alicepwd -X PUT http://localhost:9080/inventory/api/systems/localhost?heapSize=2097152&javaVersion=11&osName=linux
curl -s http://localhost:9080/inventory/api/systems

curl -k --user bob:bobpwd https://localhost:9443/metrics/application | grep 'application_addSystemClient_total 0\|application_addSystem_total 1\|application_updateSystem_total 1\|application_removeSystem_total 1' || exit 1

mvn liberty:stop 

cd ../system
mvn liberty:stop

docker stop postgres-container
docker rm postgres-container


echo ===== Test module-testcontainers =====

cd ../module-jwt

cp ../module-kubernetes/src/main/liberty/config/server.xml ./src/main/liberty/config/server.xml
cp ../module-kubernetes/Dockerfile .

mvn package
docker build -t liberty-deepdive-inventory:1.0-SNAPSHOT .
docker images
docker ps 

mkdir -p src/test/java/it/io/openliberty/deepdive/rest
mkdir src/test/resources

cp ../module-testcontainers/src/test/java/it/io/openliberty/deepdive/rest/SystemResourceClient.java ./src/test/java/it/io/openliberty/deepdive/rest
cp ../module-testcontainers/src/test/java/it/io/openliberty/deepdive/rest/SystemData.java ./src/test/java/it/io/openliberty/deepdive/rest
cp ../module-testcontainers/src/test/java/it/io/openliberty/deepdive/rest/LibertyContainer.java ./src/test/java/it/io/openliberty/deepdive/rest
cp ../module-testcontainers/src/test/java/it/io/openliberty/deepdive/rest/SystemResourceIT.java ./src/test/java/it/io/openliberty/deepdive/rest
cp ../module-testcontainers/src/test/resources/log4j.properties ./src/test/resources
cp ../module-testcontainers/pom.xml .

mvn verify -Dtest.protocol=http

echo ===== TESTS PASSED =====
exit 0