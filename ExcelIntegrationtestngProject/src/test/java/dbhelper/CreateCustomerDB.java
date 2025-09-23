package dbhelper;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
public class CreateCustomerDB {
    String jdbcUrlNoDB = "jdbc:mysql://localhost:3306/"; // No DB specified here
    String jdbcUrlWithDB = "jdbc:mysql://localhost:3306/testcustomer"; // For table creation
    String username = "root";  // DB username
    String password = "pass";  // DB password

    public void createdbcon() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        // Step 1: Create database if it doesn't exist
        String createSchemaSQL = "CREATE DATABASE IF NOT EXISTS testcustomer";

        try (
                Connection connection = DriverManager.getConnection(jdbcUrlNoDB, username, password);
                Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate(createSchemaSQL);
            System.out.println("Database 'Testing' checked/created successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        // Step 2: Create table inside Testing schema
        String createTableSQL = "CREATE TABLE IF NOT EXISTS btob_customer (\n" +
                "    ID                          VARCHAR(20)     NOT NULL PRIMARY KEY,\n" +
                "    CREATED_BY                 VARCHAR(50)     NOT NULL,\n" +
                "    CREATED_DT                 DATETIME        NOT NULL,\n" +
                "    UPDATED_BY                 VARCHAR(50)     NOT NULL,\n" +
                "    UPDATED_DT                 DATETIME        NOT NULL,\n" +
                "    MAIN_ADDR_ID               VARCHAR(20),\n" +
                "    CATG_NAME                  VARCHAR(255),\n" +
                "    CITY                       VARCHAR(255),\n" +
                "    COMPANY_BRANCH_ID          VARCHAR(20)     NOT NULL,\n" +
                "    cust_catg_id               VARCHAR(20),\n" +
                "    CUST_COMPANY_BRANCH_ID     VARCHAR(20),\n" +
                "    DLVY_ADDR_ID               VARCHAR(20),\n" +
                "    DESCRIPTION                TEXT,\n" +
                "    EMAIL                      VARCHAR(255),\n" +
                "    FAX                        VARCHAR(25),\n" +
                "    FACEBOOK_PAGE              VARCHAR(255),\n" +
                "    IS_ACTIVE                  VARCHAR(1),\n" +
                "    LOCATION                   VARCHAR(255),\n" +
                "    MOBILE_NUM                 VARCHAR(25),\n" +
                "    NAME                       VARCHAR(255),\n" +
                "    PHONE_NUM                  VARCHAR(25),\n" +
                "    REG_DATE                   DATE,\n" +
                "    RELATED_CUSTOMER_ID        VARCHAR(25),\n" +
                "    RELATION_TYPE              VARCHAR(50),\n" +
                "    STATUS                     VARCHAR(25),\n" +
                "    TIN                        VARCHAR(20),\n" +
                "    WEBSITE_URL                VARCHAR(255),\n" +
                "    ADDITIONAL_DISCOUNT        VARCHAR(6),\n" +
                "    INCENTIVE_DISCOUNT         VARCHAR(6),\n" +
                "    TURNOVER_DISCOUNT          VARCHAR(6),\n" +
                "    DISPATCH_ROUTE             VARCHAR(50),\n" +
                "    CASH_OUTSTANDING_LIMIT     VARCHAR(20),\n" +
                "    CREDIT_OUTSTANDING_LIMIT   VARCHAR(20),\n" +
                "    CASH_OUTSTANDING_DAYS      VARCHAR(3),\n" +
                "    CREDIT_OUTSTANDING_DAYS    VARCHAR(3),\n" +
                "    SALES_TARGET_MONTHLY       VARCHAR(20),\n" +
                "    GSTIN                      VARCHAR(20),\n" +
                "    activation_code            VARCHAR(10),\n" +
                "    KEMP_ID                    VARCHAR(20),\n" +
                "    KEMP_NAME                  VARCHAR(1024),\n" +
                "    K_CATEGORY                 VARCHAR(50),\n" +
                "    IS_TANDCAGREED             VARCHAR(1),\n" +
                "    PRIMARY_SERVICE_OFFERED    VARCHAR(1024),\n" +
                "    SECONDARY_SERVICE_OFFERED  VARCHAR(1024),\n" +
                "    CALL_REMARKS               LONGTEXT,\n" +
                "    CALL_STATUS                VARCHAR(256),\n" +
                "    CALL_UPDATE_DATE           DATETIME,\n" +
                "    OP_PROMO_CODE              TEXT,\n" +
                "    TELE_CALLER_ID             VARCHAR(20),\n" +
                "    TELE_CALLER_NAME           VARCHAR(1024),\n" +
                "    REGION                     VARCHAR(100),\n" +
                "    ZONE                       VARCHAR(100),\n" +
                "    LATTITUDE                  VARCHAR(20),\n" +
                "    LONGITUDE                  VARCHAR(20),\n" +
                "    STATE                      VARCHAR(100),\n" +
                "    CONTACT_PERSON_NAME        VARCHAR(1024),\n" +
                "    CONTACT_PERSON_ROLE        VARCHAR(1024),\n" +
                "    CUSTOMER_CODE              VARCHAR(100),\n" +
                "    REGION_CODE                VARCHAR(20),\n" +
                "    CUSTOMER_TYPE              VARCHAR(100),\n" +
                "    PIN_CODE                   VARCHAR(10),\n" +
                "    CUSTOMER_CLASSIFY          VARCHAR(100),\n" +
                "    VEH_SEGMENT                VARCHAR(100),\n" +
                "    APPLIED_PROMO_CODE         TEXT,\n" +
                "    PRICE_LIST_NAME            VARCHAR(1024),\n" +
                "    PRICE_LIST_NUM             VARCHAR(20),\n" +
                "    PARENT_COMPANY_BRANCH_ID   VARCHAR(20),\n" +
                "    IS_TCS_DEDUCT              VARCHAR(1),\n" +
                "    GST_FILE_NAME              TEXT,\n" +
                "    BANK_VID                   VARCHAR(100),\n" +
                "    PROFILE_FILE_NAME          TEXT,\n" +
                "    PREFERRED_TRANSPORT_VENDOR VARCHAR(1024),\n" +
                "    IS_FAST_DELIVERY           VARCHAR(1),\n" +
                "    REGISTRATION_TYPE          VARCHAR(100),\n" +
                "    INDEX (COMPANY_BRANCH_ID),\n" +
                "    INDEX (KEMP_ID),\n" +
                "    INDEX (PARENT_COMPANY_BRANCH_ID)\n" +
                ");";


        try (
                Connection connection = DriverManager.getConnection(jdbcUrlWithDB, username, password);
                Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate(createTableSQL);
            System.out.println("Table 'btob_customer' created successfully in database 'Testing'.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
