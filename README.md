# cache
Two level Cache API

Run run.sh or run.bat file inside the cashe folder.

Note : need to install docker in your host run the application. 

Write data in the cache using below commend

curl --location --request POST 'http://localhost:8081/api/cache/12346' --header 'Content-Type: application/json' --data-raw '{"name": "linuxize", "email": "linuxize@example.com"}'

Read data from the cache using below commend

curl --location --request GET 'http://localhost:8081/api/cache/12346'
