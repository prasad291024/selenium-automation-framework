# ☁️ Cloud Grid Setup Guide

## 🎯 Overview
Run your tests on BrowserStack or LambdaTest cloud without local browser setup.

---

## 📋 Prerequisites

### 1. BrowserStack Account
- Sign up: https://www.browserstack.com/
- Get credentials: Account → Settings → Access Key
- Copy: Username & Access Key

### 2. LambdaTest Account
- Sign up: https://www.lambdatest.com/
- Get credentials: Profile → Access Key
- Copy: Username & Access Token

---

## ⚙️ Configuration

### Step 1: Configure GitHub Secrets (for CI/CD)
Go to: **Settings → Secrets and variables → Actions → New repository secret**

```
BROWSERSTACK_USERNAME    # Your BrowserStack username
BROWSERSTACK_ACCESS_KEY  # Your BrowserStack access key
LAMBDATEST_USERNAME      # Your LambdaTest username
LAMBDATEST_ACCESS_KEY    # Your LambdaTest access key
```

### Step 2: Local Configuration (for local execution)
Update `src/main/resources/data.properties`:

```properties
# Replace with your actual credentials
browserstack.username=your_username_here
browserstack.accesskey=your_access_key_here
lambdatest.username=your_username_here
lambdatest.accesskey=your_access_token_here
```

---

## 🚀 Usage

### Option 1: Use TestCloudExecution Example

**Enable the test** in `src/test/java/com/prasad_v/tests/examples/TestCloudExecution.java`:
```java
@Test(enabled = true) // Change from false to true
public void testBrowserStack() throws Exception {
    // test code
}
```

**Run**:
```bash
mvn test -Dtest=TestCloudExecution#testBrowserStack
```

### Option 2: Use in Your Tests

```java
import com.prasad_v.driver.DriverManagerCloud;

@Test
public void myCloudTest() throws Exception {
    // For BrowserStack
    DriverManagerCloud.initBrowserStack();
    WebDriver driver = DriverManagerCloud.getDriver();
    
    // Your test code
    driver.get("https://example.com");
    
    // Cleanup
    DriverManagerCloud.quit();
}
```

### Option 3: CI/CD Execution
Cloud execution is also available in GitHub Actions workflows when the appropriate secrets are configured.

---

## 🌐 Supported Configurations

### BrowserStack (Current Implementation)
- **OS**: Windows 11
- **Browser**: Chrome (latest)
- **Project**: ATB10x Selenium Framework
- **Build**: Auto-generated with timestamp
- **Session Name**: "Test Session"

### LambdaTest (Current Implementation)
- **Platform**: Windows 11
- **Browser**: Chrome (latest)
- **Project**: ATB10x Selenium Framework
- **Build**: Auto-generated with timestamp
- **Session Name**: "Test Session"
- **W3C**: Enabled

---

## 🔧 Customization

### Change Browser/OS/Capabilities

Edit `src/main/java/com/prasad_v/driver/DriverManagerCloud.java`:

```java
// BrowserStack Example
bsOptions.put("os", "OS X");
bsOptions.put("osVersion", "Monterey");
bsOptions.put("browserName", "Firefox");
bsOptions.put("browserVersion", "latest");

// LambdaTest Example
ltOptions.put("platformName", "macOS Ventura");
ltOptions.put("browserName", "Firefox");
ltOptions.put("browserVersion", "latest");
```

### Available Capabilities

**BrowserStack**: https://www.browserstack.com/automate/capabilities
**LambdaTest**: https://www.lambdatest.com/support/docs/dev-tools-capabilities/

---

## 🐳 Docker Integration Notes

When using cloud execution in CI/CD:
- No local Selenium Grid required
- Tests execute remotely on BrowserStack/LambdaTest infrastructure
- Video recording, network logs, and screenshots are automatically available
- Parallel execution based on your plan limits

---

## 💰 Pricing

### BrowserStack
- Free trial: 100 minutes
- Paid plans: From $29/month
- Parallel tests: Based on plan

### LambdaTest
- Free trial: 100 minutes
- Paid plans: From $15/month
- Parallel tests: Based on plan

---

## 🐛 Troubleshooting

### Error: Authentication Failed
```
Solution: Check username and access key are correct
Verify secrets are properly configured in GitHub Actions or local data.properties
```

### Error: Timeout
```
Solution: Check internet connection
Increase timeout in test if needed
```

### Error: Browser/OS not available
```
Solution: Check browser/OS combination is supported
Refer to provider documentation for available options
```

### Error: Capabilities not applied
```
Solution: Verify capability names are correct for each provider
Check that capabilities are being set before RemoteWebDriver initialization
```

---

## 📊 Benefits

✅ No local browser setup  
✅ Cross-browser testing (Chrome, Firefox, Safari, Edge)  
✅ Multiple OS support (Windows, macOS, Linux)  
✅ Parallel execution based on plan limits  
✅ Video recording of test sessions  
✅ Network logs for debugging  
✅ Screenshots on failure  
✅ Real device testing (mobile plans)  
✅ Geolocation testing (available on higher tiers)

---

## 🎯 Next Steps

1. Sign up for BrowserStack/LambdaTest
2. Get credentials (username & access key/token)
3. Configure GitHub Secrets for CI/CD usage
4. Update local `data.properties` for local development/testing
5. Enable tests in `TestCloudExecution.java` or create your own cloud tests
6. Run: `mvn test -Dtest=TestCloudExecution`

---

## 📁 Related Files

- `src/main/java/com/prasad_v/driver/DriverManagerCloud.java` - Cloud driver implementation
- `src/test/java/com/prasad_v/tests/examples/TestCloudExecution.java` - Example cloud tests
- `src/main/resources/data.properties` - Local configuration
- `.github/workflows/` - GitHub Actions workflows (when secrets are configured)

---

## ✅ Checklist

- [x] BrowserStack credentials obtained
- [x] LambdaTest credentials obtained
- [x] GitHub Secrets configured (BROWSERSTACK_USERNAME, BROWSERSTACK_ACCESS_KEY, LAMBDATEST_USERNAME, LAMBDATEST_ACCESS_KEY)
- [x] Local data.properties updated with credentials
- [x] DriverManagerCloud.java reviewed for capability customization
- [x] TestCloudExecution.java reviewed and tests enabled as needed
- [x] Cloud test execution verified locally
- [x] Cloud test execution verified in CI/CD (when secrets configured)
- [x] Documentation reviewed

---

**File**: `docs/CLOUD_SETUP_GUIDE.md`
**Last Updated**: September 17, 2026
**Status**: ✅ Complete