# 🛍️ Java E-Commerce Cart

A Maven-based shopping system demonstrating:
- Object-oriented design
- File persistence (CSV + serialization)
- User/cart management

## Features
- **Admin Portal**
   - Add/view products
- **Customer Flow**
   - Browse products → Add to cart → Checkout
- **Data Persistence**
   - Products saved as CSV
   - Carts saved per user

## Tech Stack
- Java 11+
- Maven
- JUnit (tests)

## Setup
1. Clone repo
2. Build: `mvn clean package`
3. Run: `mvn exec:java -Dexec.mainClass="com.example.Main"`

## Future Improvements
- [ ] Add payment processing
- [ ] Implement discount codes