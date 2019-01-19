# Toki Flights API
Toki Flights API is a flight aggregator which provides the capability to get all the flights (budget and business) around the world. It has the following features which can be utilized through our simple api design:

  - Getting sorted flight information
  - Filtering based on Source and Destination of the flights
  - Pagination

## API User Documentation
### Get all flights
```sh
/flights
```
### Sorting
```sh
/flights?sort=param
```
Supported parameters:
- source
- destination
- departureTime
- arrivalTime
### Filtering
```sh
/flights?flt=src:param1;dest:param2
```
param1 : Filter by source
param2 : Filter by destination
### Pagination
```sh
/flights?page=param1&size:param2
```
param1: int - page number
param2: int - page size; number of entries in each page

## Tech
The API is build using Java and Spring boot.
It is an open source project. https://github.com/viveksinghk11/flights-aggregator-api
### Local Installation
#### Prerequisites:
- JDK 8
- Apache Maven
#### Source-code
You can download/clone source code from: https://github.com/viveksinghk11/flights-aggregator-api

#### Package and install
Run the following command in cmd-prompt in your local project location:

```sh
mvn package && java -jar target/flight-aggregator-api-0.1.0.jar
```

#### Start using API
https://localhost:8080/flights

Note: 8080 is the default port.

