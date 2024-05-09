# ClusteredData Warehouse

The ClusteredData Warehouse is a robust data management solution developed by our Scrum team for Bloomberg, tailored specifically for analyzing FX (foreign exchange) deals. One of the pivotal customer stories driving our development efforts is the seamless acceptance and persistence of deal details into the database.

## Project Description

### Request Logic:

Our system is designed to process incoming deal requests with the following key fields:

- Deal Unique Id
- From Currency ISO Code (Ordering Currency)
- To Currency ISO Code
- Deal Timestamp
- Deal Amount in Ordering Currency

### Validation:
To ensure data integrity, our system performs thorough validation of the request structure. This includes checks for missing fields and adherence to specified data types. While it's impossible to cover every conceivable case, our validation framework is robust and adaptable to various scenarios.

### Duplicate Prevention:
An essential feature of our system is the prevention of duplicate imports. We employ mechanisms to detect and reject identical requests, ensuring that each deal is imported into the database only once.

### Non-Rollback Policy:
In adherence to our stringent data integrity standards, our system operates under a strict non-rollback policy. Once a deal is imported, it is permanently saved in the database without the option for rollback.

## Getting Started
To get started with the ClusteredData Warehouse Project, start by doing these steps:

1. **Build the Project**: Build the project.

    ```shell
    mvn clean install package
    ```
   
2. **Run the Application using Docker Compose**: Use Docker Compose to start the application.

    ```shell
    docker compose up
    ```
   
3. **Accessing the Endpoints**:

- **Add a Deal**
    
    Endpoint: `POST /add`

    Use this endpoint to add a new deal. 

    **Sample input**:
    ```json
    {
        "deal_id": "289",
        "from_currency": "USD",
        "to_currency": "EUR",
        "deal_timestamp": "20-04-2024 10:15:30",
        "deal_amount": 14000
    }
    ```

- **Get Deal by ID**

  Endpoint: `GET /getByID/{id}`

  Use this endpoint to retrieve a specific Deal by its unique ID. Replace `{id}` with the Deal ID your want to get.


- **Get all Deals**

  Endpoint: `GET /getAll`

  Use this endpoint to retrieve all Deals.