# order-service
This is the consumer service for CDC expert talk. 

## Service endpoints. 

### /order POST request:
```
curl -X POST \
  http://localhost:9000/order
  -H 'cache-control: no-cache' 
  -H 'content-type: application/json'
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
