FOOD DELIVERY SYSTEM - JDBC + MySQL + VS CODE

1) Requirements
- JDK 17 or newer
- MySQL Server
- VS Code with Extension Pack for Java
- MySQL Connector/J JAR

2) Project structure
src/
  com/tns/fooddeliverysystem/
    application/FoodDeliverySystem.java
    db/DBConnection.java
    entities/
    services/
sql/food_delivery_db.sql
lib/mysql-connector-j.jar

3) Database setup
Open CMD:
    mysql -u root -p

Then:
    source sql/food_delivery_db.sql;

If source does not work because of path, use:
    source C:/FULL/PATH/TO/FoodDeliveryJDBC_VSCode/sql/food_delivery_db.sql;

4) Configure password
Open:
src/com/tns/fooddeliverysystem/db/DBConnection.java

Change:
    YOUR_MYSQL_PASSWORD

to your real MySQL root password.

5) Compile in CMD from project folder
Windows:
    if not exist bin mkdir bin
    javac -cp "lib\mysql-connector-j.jar" -d bin src\com\tns\fooddeliverysystem\db\DBConnection.java src\com\tns\fooddeliverysystem\entities\*.java src\com\tns\fooddeliverysystem\services\*.java src\com\tns\fooddeliverysystem\application\FoodDeliverySystem.java

6) Run
    java -cp "bin;lib\mysql-connector-j.jar" com.tns.fooddeliverysystem.application.FoodDeliverySystem

7) VS Code
Open the project folder. Install Extension Pack for Java. You can run the main class from VS Code, but CMD commands above are enough.

NOTE:
The original case study uses collections such as Map and List. This version keeps those OOP/entity concepts while using JDBC + MySQL for persistent storage. Cart/order relationships are represented by database tables.
