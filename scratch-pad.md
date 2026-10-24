Commands and concepts to remember:
reverse proxies : It is an intermediary server that sits in front of one or more backend web servers, intercepting client request from the internet before they ever reach the origin servers.
nginx:alpine is an example of a reverse proxy from docker.

.\gradlew build
.\gradlew bootRun
.\gradlew test
.\gradlew clean build

<!-- backend running on -> http://127.0.0.1:8080/
 Frontend running on -> http://127.0.0.1:5173  it is running on vite -->

Note:-> access the frontend using only the above url, else a cors error arises.