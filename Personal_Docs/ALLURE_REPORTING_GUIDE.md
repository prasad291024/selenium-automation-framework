# 📊 Allure Reporting Guide

## 📋 Overview
Complete guide for Allure Report configuration, customization, and best practices.

---

## ✅ Configuration Files

### 1. categories.json
**Location**: Project root  
**Purpose**: Classify test failures

```json
[
  {
    "name": "Product Defects",
    "matchedStatuses": ["failed"],
    "messageRegex": ".*AssertionError.*"
  },
  {
    "name": "Test Defects",
    "matchedStatuses": ["broken"],
    "messageRegex": ".*NullPointerException.*"
  }
]
```

### 2. environment.properties
**Location**: Project root  
**Purpose**: Display environment information

```properties
Browser=Chrome
Environment=QA
Java.Version=17
OS=Windows
```

---

## 🎯 Allure Annotations

### @Epic
Groups features into business initiatives
```java
@Epic("VWO Application")
public class TestVWOLogin {
}
```

### @Feature
Groups tests by feature
```java
@Feature("Login Functionality")
public class TestVWOLogin {
}
```

### @Story
Describes user story
```java
@Story("Valid Login")
@Test
public void testValidLogin() {
}
```

### @Severity
Sets test priority
```java
@Severity(SeverityLevel.BLOCKER)  // BLOCKER, CRITICAL, NORMAL, MINOR, TRIVIAL
@Test
public void testCriticalFeature() {
}
```

### @Description
Adds test description
```java
@Description("Test login with valid credentials")
@Test
public void testLogin() {
}
```

### @Owner
Assigns test owner
```java
@Owner("Prasad")
@Test
public void testFeature() {
}
```

### @Link
Adds external links
```java
@Link(name = "VWO", url = "https://app.vwo.com")
@Test
public void testVWO() {
}
```

### @Issue / @TmsLink
Links to issue tracker
```java
@Issue("VWO-123")
@TmsLink("TC-001")
@Test
public void testBugFix() {
}
```

---

## 📝 Allure Steps

### Manual Steps
```java
@Test
public void testLogin() {
    Allure.step("Navigate to login page");
    Allure.step("Enter credentials");
    Allure.step("Click login button");
    Allure.step("Verify dashboard");
}
```

### Step with Lambda
```java
Allure.step("Enter username", () -> {
    driver.findElement(By.id("username")).sendKeys("user");
});
```

### Attachments
```java
// Text attachment
Allure.addAttachment("Username", "test@example.com");

// Screenshot attachment
byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
Allure.addAttachment("Screenshot", new ByteArrayInputStream(screenshot));
```

---

## 🚀 Generating Reports

### Generate and View
```bash
# Run tests
mvn clean test

# Generate and open report
mvn allure:serve
```

### Generate Only
```bash
mvn allure:report
```

### View Existing Report
```bash
mvn allure:serve
```

---

## 📊 Report Features

### Overview
- Total tests
- Pass/Fail rate
- Duration
- Trends

### Suites
- Test organization
- Execution time
- Status

### Graphs
- Status chart
- Severity chart
- Duration chart
- Categories

### Timeline
- Test execution timeline
- Parallel execution view

### Behaviors
- Tests by Epic/Feature/Story
- BDD-style view

### Packages
- Tests by package
- Class organization

---

## 🎨 Customization

### Custom Categories
Edit `categories.json`:
```json
[
  {
    "name": "UI Issues",
    "matchedStatuses": ["failed"],
    "messageRegex": ".*ElementNotFound.*"
  }
]
```

### Environment Info
Edit `environment.properties`:
```properties
Browser=Chrome
Browser.Version=120.0
Environment=QA
Base.URL=https://app.vwo.com
Test.Suite=Regression
Execution.Date=2024-01-15
```

### Report Title
In `pom.xml`:
```xml
<plugin>
  <groupId>io.qameta.allure</groupId>
  <artifactId>allure-maven</artifactId>
  <configuration>
    <reportVersion>2.26.0</reportVersion>
    <resultsDirectory>${project.build.directory}/allure-results</resultsDirectory>
  </configuration>
</plugin>
```

---

## 📈 Best Practices

### 1. Use Meaningful Names
```java
@Epic("E-Commerce Platform")
@Feature("Shopping Cart")
@Story("Add Product to Cart")
```

### 2. Set Appropriate Severity
- BLOCKER: Critical functionality
- CRITICAL: Important features
- NORMAL: Standard tests
- MINOR: Edge cases
- TRIVIAL: UI/cosmetic

### 3. Add Steps
```java
Allure.step("Step 1: Open application");
Allure.step("Step 2: Login");
Allure.step("Step 3: Verify dashboard");
```

### 4. Attach Evidence
```java
Allure.addAttachment("Request", requestBody);
Allure.addAttachment("Response", responseBody);
Allure.addAttachment("Screenshot", screenshot);
```

### 5. Link to Requirements
```java
@TmsLink("REQ-123")
@Issue("BUG-456")
```

---

## 🔧 CI/CD Integration

### GitHub Actions
```yaml
- name: Generate Allure Report
  run: mvn allure:report

- name: Upload Report
  uses: actions/upload-artifact@v4
  with:
    name: allure-report
    path: target/site/allure-maven-plugin/
```

### Publish to GitHub Pages
```yaml
- name: Deploy Report
  uses: peaceiris/actions-gh-pages@v3
  with:
    github_token: ${{ secrets.GITHUB_TOKEN }}
    publish_dir: ./target/site/allure-maven-plugin
```

---

## 📊 Report Metrics

### Key Metrics
- Pass Rate: (Passed / Total) × 100
- Flaky Tests: Tests with inconsistent results
- Duration: Average test execution time
- Trends: Pass rate over time

### Categories
- Product Defects: Assertion failures
- Test Defects: Code issues
- Timeout Issues: Performance problems
- Configuration Issues: Setup problems

---

## 🚨 Troubleshooting

### Report Not Generated
```bash
# Check allure-results directory
ls target/allure-results/

# Verify plugin in pom.xml
mvn help:effective-pom | grep allure
```

### Missing Test Results
```bash
# Clean and rebuild
mvn clean test

# Check TestNG listeners
@Listeners({AllureTestNG.class})
```

### Screenshots Not Showing
```java
// Use byte array
byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
Allure.addAttachment("Screenshot", "image/png", new ByteArrayInputStream(screenshot), ".png");
```

---

## 📁 Directory Structure

```
project/
├── categories.json              # Test categories
├── environment.properties       # Environment info
├── target/
│   ├── allure-results/         # Test results (JSON)
│   └── site/
│       └── allure-maven-plugin/ # Generated report
```

---

## ✅ Checklist

- [x] Allure Maven plugin configured
- [x] categories.json created
- [x] environment.properties created
- [x] Annotations added to tests
- [x] Steps implemented
- [x] Screenshots attached
- [x] Report generation tested
- [ ] CI/CD integration configured
- [ ] GitHub Pages deployment (optional)

---

**File**: `Personal_Docs/ALLURE_REPORTING_GUIDE.md`  
**Last Updated**: Phase 9 - Reporting  
**Status**: ✅ Complete
