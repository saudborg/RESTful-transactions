# Number26 - RESTful Transaction
This project was developed for Number26. It represents a system  through restful you can add a transaction or get informations about it

### Prerequisities
- [Apache Maven](https://maven.apache.org/index.html)
- [GitHub](https://help.github.com/desktop/guides/getting-started/installing-github-desktop/)


## Built with
- Java 1.8
- Maven 3.2.1
- Spring Boot (JPA, TEST, MVC)
- H2 (run time)
- JUnit


## Installation
- Clone the project
- Download all the maven dependencies in the project and run on your terminal
```
mvn clean install
sudo mvn jetty:run
```


### How to use 
When you see on your terminal `[INFO] Started Jetty Server` it is ready to use

To test the application you should use a browse extension like [Advanced Rest Client](https://chrome.google.com/webstore/detail/advanced-rest-client/hgmloofddffdnphfgcellkdfbfbjeloo)

- Create Transactions
```
PUT /transactionservice/transaction/1
params:
	amount:		100
	type:		car


OR

PUT /transactionservice/transaction/2
params:
	amount:		200
	type:		car
	parent_id:	1


Response
{"status": "ok"}
```

- Get Transaction

```
GET /transactionservice/transaction/1

Response

{
"amount": 100
"type": "car"
}
```

- Get List Transactions with same type

```
GET /transactionservice/types/car

RESPONSE

[2]
0:  1
1:  2

```

- Get a sum of all transactions that are transitively linked by their parent_id

```
GET /transactionservice/sum/1

RESPONSE

{
"sum": 300
}
```





