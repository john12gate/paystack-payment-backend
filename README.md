# Frontend Setup
## Prerequisites
Node.js
npm or yarn
Vercel account (for deployment)
Setup Instructions
Navigate to the frontend directory:

cd paystack-payment/frontend
Install dependencies:

npm install
or
yarn install
Create an environment file:

Create a file named .env in the root of the frontend directory and add the following:

REACT_APP_PAYSTACK_PUBLIC_KEY=pk_test_your_public_key
<br/>
REACT_APP_BACKEND_URL=http://localhost:9090/api/v1
Run the application:


npm start
or

yarn start
Running Tests
To run the tests, use the following command:

npm test


Frontend
The frontend will be running on http://localhost:3000.

API Endpoints
POST /api/v1/payments/initiate - Initiate a payment.
GET /api/v1/payments/verify - Verify a payment.



### Notes:

1. Replace placeholders such as `yourusername`, `yourpassword`, `sk_test_your_secret_key`, `pk_test_your_public_key` with your actual data.
2. Make sure your database is set up and running before starting the backend application.
3. Ensure you have the correct Paystack keys in your `application.properties` and `.env` files.





