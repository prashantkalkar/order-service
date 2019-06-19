# order-service
This is the consumer service for CDC expert talk. 

## How to start a service:
Make sure you have <strong>jdk</strong> and <strong>mvn</strong> installed and added in the path

### Run following command to make service up:
```mvn spring-boot:run -Dserver.port=9000```

## Service endpoints: 

### /order POST request:
```
curl -X POST \
  http://localhost:9000/order \
  -H 'content-type: application/json' \
  -d '{
	"productId":"1",
	"quantity":"2",
	"userId":"myId"
}'
```
### Response: HTTP 200 with body:
```
{
    "orderId": "7998289398179"
}
```

## How to run CDC test:
```mvn clean install``` or ```mvn test```

It will create Pact files in target\pacts folder.
