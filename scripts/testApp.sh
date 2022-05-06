#!/bin/bash
set -euxo pipefail


echo ===== Test module-getting-started =====
cd module-getting-started || exit

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

echo ===== Test module-jwt =====
cd ../module-jwt || exit
mvn -Dhttp.keepAlive=false \
    -Dmaven.wagon.http.pool=false \
    -Dmaven.wagon.httpconnectionManager.ttlSeconds=120 \
    -q clean package liberty:create liberty:install-feature liberty:deploy

mvn liberty:start

cd ../system || exit

mvn liberty:start

curl -k --user bob:bobpwd -X POST 'https://localhost:9443/inventory/api/systems/client/localhost' | grep "{ \"ok\" : \"localhost was added.\" }" || echo failed; exit 1

curl 'http://localhost:9080/inventory/api/systems' | grep "\"heapSize\":" || echo failed; exit 1

mvn liberty:stop 

cd ../module-jwt || exit

mvn liberty:stop

echo ===== Test module-health-checks =====
cd ../module-health-checks || exit
mvn -Dhttp.keepAlive=false \
    -Dmaven.wagon.http.pool=false \
    -Dmaven.wagon.httpconnectionManager.ttlSeconds=120 \
    -q clean package liberty:create liberty:install-feature liberty:deploy

mvn liberty:start

cp ../module-jwt/pom.xml .

cp -i ../module-jwt/src/main/liberty/config/server.xml ./src/main/liberty/config/server.xml

curl `http://localhost:9080/health/started` | grep "\"status\":\"UP\"" || echo failed; exit 1

curl `http://localhost:9080/health/live` | grep "\"status\":\"UP\"" || echo failed; exit 1

curl `http://localhost:9080/health/ready` | grep "\"status\":\"UP\"" || echo failed; exit 1

mvn liberty:stop 