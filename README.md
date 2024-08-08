README.md
markdown
Copy code
# Paystack Payment Integration

This project integrates Paystack payment gateway using Spring Boot for the backend and React for the frontend.

## Table of Contents

- [Backend Setup](#backend-setup)
- [Frontend Setup](#frontend-setup)
- [Running the Application](#running-the-application)

## Backend Setup

### Prerequisites

- Java 21 or higher
- Maven
- MySQL
- Paystack account

### Setup Instructions

1. **Clone the repository:**

    ```sh
    git clone https://github.com/yourusername/paystack-payment.git
    cd paystack-payment/backend
    ```

2. **Configure the Database:**

   Update the `src/main/resources/application.properties` file with your MySQL configuration:

    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/paystack_db
    spring.datasource.username=root
    spring.datasource.password=yourpassword
    spring.jpa.hibernate.ddl-auto=update
    ```

3. **Add Paystack API Keys:**

   Create a file named `application.properties` in the `src/main/resources/` directory and add your Paystack API keys:

    ```properties
    paystack.secret.key=sk_test_your_secret_key
    paystack.public.key=pk_test_your_public_key
    ```

4. **Build the project:**

    ```sh
    mvn clean install
    ```

5. **Run the application:**

    ```sh
    mvn spring-boot:run
    ```

### Running Tests

To run the tests, use the following command:

```sh
mvn test
