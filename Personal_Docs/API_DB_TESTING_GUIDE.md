# 🔌 API & Database Testing Guide

## 📋 Overview
Complete guide for API and database testing using APIUtil and DatabaseUtil.

---

## ✅ API Testing

### Setup
API tests use **REST Assured** library with custom APIUtil wrapper.

**Utility**: `com.prasad_v.utils.APIUtil`

### Supported Operations
- `GET` - Retrieve data
- `POST` - Create data
- `PUT` - Update data
- `DELETE` - Remove data

---

## 🧪 API Test Examples

### 1. GET Request
```java
Response response = APIUtil.get("https://reqres.in", "/api/users/2");
assertThat(response.getStatusCode()).isEqualTo(200);
String name = response.jsonPath().getString("data.first_name");
```

### 2. POST Request
```java
String body = "{ \"name\": \"John\", \"job\": \"QA\" }";
Response response = APIUtil.post("https://reqres.in", "/api/users", body);
assertThat(response.getStatusCode()).isEqualTo(201);
```

### 3. PUT Request
```java
String body = "{ \"name\": \"John Updated\", \"job\": \"Senior QA\" }";
Response response = APIUtil.put("https://reqres.in", "/api/users/2", body);
assertThat(response.getStatusCode()).isEqualTo(200);
```

### 4. DELETE Request
```java
Response response = APIUtil.delete("https://reqres.in", "/api/users/2");
assertThat(response.getStatusCode()).isEqualTo(204);
```

---

## 🗄️ Database Testing

### Setup
Database tests use **JDBC** with custom DatabaseUtil wrapper.

**Utility**: `com.prasad_v.utils.DatabaseUtil`

### Configuration
Update credentials in test class or `data.properties`:
```properties
db.url=jdbc:mysql://localhost:3306/testdb
db.username=root
db.password=password
```

---

## 🧪 Database Test Examples

### 1. Connect to Database
```java
DatabaseUtil db = new DatabaseUtil();
db.connect(DB_URL, DB_USER, DB_PASSWORD);
```

### 2. Execute Query (SELECT)
```java
String query = "SELECT * FROM users WHERE status = 'active'";
List<Map<String, Object>> results = db.executeQuery(query);
assertThat(results).isNotEmpty();
```

### 3. Execute Update (INSERT/UPDATE/DELETE)
```java
String insert = "INSERT INTO users (name, email) VALUES ('John', 'john@test.com')";
int rowsAffected = db.executeUpdate(insert);
assertThat(rowsAffected).isEqualTo(1);
```

### 4. Disconnect
```java
db.disconnect();
```

---

## 🚀 Running Tests

### Run API Tests
```bash
# Run all API tests
mvn test -Dtest=TestAPIExample

# Run specific test
mvn test -Dtest=TestAPIExample#testGetUser

# Run API test suite
mvn test -Dsurefire.suiteXmlFiles=testng_api_tests.xml
```

### Run Database Tests
```bash
# Enable DB tests first (set enabled=true in test class)
mvn test -Dtest=TestDatabaseExample
```

---

## 📊 Test Structure

### API Test Class
```java
@Epic("API Testing")
@Feature("REST API Validation")
public class TestAPIExample {
    
    @Test
    @Story("Get User API")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetUser() {
        LoggerUtil.info("Starting API test");
        Response response = APIUtil.get(baseUrl, endpoint);
        assertThat(response.getStatusCode()).isEqualTo(200);
    }
}
```

### Database Test Class
```java
@Epic("Database Testing")
@Feature("Database Operations")
public class TestDatabaseExample {
    
    private DatabaseUtil db;
    
    @BeforeMethod
    public void setup() throws SQLException {
        db = new DatabaseUtil();
        db.connect(DB_URL, DB_USER, DB_PASSWORD);
    }
    
    @AfterMethod
    public void teardown() throws SQLException {
        db.disconnect();
    }
    
    @Test
    public void testQuery() throws SQLException {
        List<Map<String, Object>> results = db.executeQuery("SELECT * FROM users");
        assertThat(results).isNotEmpty();
    }
}
```

---

## 🎯 Best Practices

### API Testing
1. ✅ Use meaningful test names
2. ✅ Validate status codes
3. ✅ Verify response body
4. ✅ Add logging for debugging
5. ✅ Use Allure annotations
6. ✅ Handle authentication
7. ✅ Test error scenarios

### Database Testing
1. ✅ Use @BeforeMethod for connection
2. ✅ Use @AfterMethod for cleanup
3. ✅ Disable tests by default (enabled=false)
4. ✅ Use transactions for rollback
5. ✅ Avoid hardcoded credentials
6. ✅ Test data isolation
7. ✅ Clean up test data

---

## 🔧 Configuration

### Add to data.properties
```properties
# API Configuration
api.base.url=https://reqres.in
api.timeout=30000

# Database Configuration
db.url=jdbc:mysql://localhost:3306/testdb
db.username=root
db.password=password
db.driver=com.mysql.cj.jdbc.Driver
```

### Use ConfigManager
```java
String apiUrl = ConfigManager.get("api.base.url");
String dbUrl = ConfigManager.get("db.url");
```

---

## 📈 Integration with CI/CD

### Add to pr-checks.yml
```yaml
- name: Run API Tests
  run: mvn test -Dsurefire.suiteXmlFiles=testng_api_tests.xml
```

### Separate Job for API Tests
```yaml
api-tests:
  runs-on: ubuntu-latest
  steps:
    - name: Checkout
      uses: actions/checkout@v4
    - name: Setup JDK
      uses: actions/setup-java@v4
      with:
        java-version: '17'
    - name: Run API Tests
      run: mvn test -Dtest=TestAPIExample
```

---

## 🧪 Sample Test Data

### API Test Endpoints
- **GET**: https://reqres.in/api/users/2
- **POST**: https://reqres.in/api/users
- **PUT**: https://reqres.in/api/users/2
- **DELETE**: https://reqres.in/api/users/2

### Database Schema (Example)
```sql
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100),
    email VARCHAR(100),
    status VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

---

## 🚨 Troubleshooting

### API Tests Fail
**Issue**: Connection timeout
**Solution**: Check network, increase timeout in APIUtil

### Database Tests Fail
**Issue**: Connection refused
**Solution**: 
- Verify MySQL is running
- Check credentials
- Verify database exists

### Tests Disabled
**Issue**: DB tests not running
**Solution**: Set `enabled=true` in @Test annotation

---

## 📝 Maintenance

### Weekly
- Review API test coverage
- Update test data
- Check for deprecated endpoints

### Monthly
- Update REST Assured version
- Review database schema changes
- Optimize slow queries

---

## ✅ Checklist

- [x] APIUtil created
- [x] DatabaseUtil created
- [x] API test examples created
- [x] DB test examples created
- [x] TestNG suite for API tests
- [x] Documentation completed
- [ ] Configure database credentials
- [ ] Enable DB tests when ready
- [ ] Add to CI/CD pipeline

---

**File**: `Personal_Docs/API_DB_TESTING_GUIDE.md`  
**Last Updated**: Phase 8 - API & DB Testing  
**Status**: ✅ Complete
