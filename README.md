# Trade Pipeline

## Introduction

Welcome to the Trade pipeline project! This repository hosts a solution for efficiently managing incoming trades. Trades are stored in a structured format based on their unique identifiers, enabling organized retrieval and analysis. This documentation provides an overview of the project's usage instructions.

## Getting Started

### Prerequisites

- Java 17
- Spring boot 3.8.1

### Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/aloktripathy2698/Trade-Pipeline.git
   cd trade
   ```

2. **Build the project:**
   ```bash
   mvn clean install
   ```
Note - This step may take 20+ mins to download all the dependencies because as it downloads and processes the data from the National Vulnerability Database (NVD) hosted by NIST: https://nvd.nist.gov. After the first batch download, as long as the plug-in is executed at least once every seven days the update will only take a few seconds.

3. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```
Alternatively we can follow below steps:

1 **Build the Jar file**
   ```bash
   mvn clean package
   ```
2 **Run the application**
   ```bash
   java -jar target/trade-0.0.1-SNAPSHOT.jar
   ```

The application will start and run on `http://localhost:8080`.

## Testing the Code

The project includes unit tests for the service and controller layers using JUnit 5 and Mockito. These tests ensure that the application behaves as expected.

### Running Tests

To execute all tests, use the following Maven command:

```bash
mvn test
```

### Test Structure

- **Controller Tests:**
  - Located in `src/test/java/com/alok/trade/controller/TradeControllerTest.java`
  - Tests the endpoints of the `TradeController` class.
  - Ensures that HTTP requests to the endpoints return the expected responses.

- **Service Tests:**
  - Located in `src/test/java/com/alok/trade/service/TradeServiceTest.java`
  - Tests the business logic in the `TradeService` class.
  - Ensures that trades are saved correctly, expired trades are updated, and exceptions are thrown for invalid data.

### Example Test Cases

#### TradeControllerTest.java

- **`createTrade_ShouldReturnCreatedStatus_WhenValidRequest`**
  - Tests that a valid trade creation request returns a `201 Created` status.

- **`getAllTrades_ShouldReturnTradeDtoList`**
  - Tests that the endpoint to get all trades returns a list of `TradeDto` objects.

#### TradeServiceTest.java

- **`saveTrade_ShouldSaveTrade_WhenValidTradeDto`**
  - Tests that a valid trade DTO is saved correctly.

- **`saveTrade_ShouldThrowException_WhenLowerVersionTrade`**
  - Tests that an exception is thrown if a trade with a lower or equal version is saved.

- **`saveTrade_ShouldThrowException_WhenMaturityDateBeforeToday`**
  - Tests that an exception is thrown if a trade with a maturity date before today is saved.

- **`scheduleTask_ShouldUpdateExpiredFlagForExpiredTrades`**
  - Tests that expired trades are correctly marked as expired by the scheduled task.

- **`getAllTrades_ShouldReturnTradeDtoList`**
  - Tests that the service method returns a list of trade DTOs.

### Running Specific Test Classes

To run a specific test class, use the following command:

```bash
mvn -Dtest=TradeControllerTest test
```

Or for service tests:

```bash
mvn -Dtest=TradeServiceTest test
```

These commands will run only the specified test class, allowing for focused testing during development.

### Viewing Test Reports

After running the tests, you can view the test reports in the `target/surefire-reports` directory. These reports provide detailed information about the test execution and results.
