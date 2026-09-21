# Selenium Automation Framework

> Engineering-focused Selenium automation framework built with Java, TestNG, Maven, and Cucumber, designed to support reusable UI automation, API testing, multi-application execution, Docker/Grid execution, CI/CD pipelines, and automated reporting.

This repository demonstrates the design and evolution of a reusable Selenium automation framework rather than a collection of isolated UI tests.

The framework currently supports multiple application implementations — **VWO, OrangeHRM, and Katalon** — while sharing common framework components for driver management, configuration, logging, API utilities, listeners, reporting, and test execution.

---

## 🎯 What This Project Demonstrates

The framework is designed around the following engineering layers:

```text
Application Tests
       │
       ▼
Page Objects / BDD Definitions
       │
       ▼
Reusable Test & Page Foundations
       │
       ▼
Driver / Configuration / Utilities
       │
       ▼
TestNG / Cucumber Execution
       │
       ▼
Docker / Selenium Grid / CI
       │
       ▼
Reports / Quality & Security Checks
```

The primary goal is to keep **application-specific test logic separated from reusable framework infrastructure**.

This makes the same framework structure applicable across multiple applications without duplicating core automation components.

---

## 🚀 Key Capabilities

| Capability | Implementation |
|---|---|
| UI Automation | Selenium 4 |
| Programming Language | Java 17 |
| Test Framework | TestNG |
| BDD | Cucumber |
| Build Management | Maven |
| Page Object Model | Application-specific Page Objects |
| Driver Management | Centralized driver layer |
| Configuration | Environment/application-specific properties |
| API Testing | REST Assured |
| Parallel / Grid Execution | Selenium Grid + Docker |
| CI/CD | GitHub Actions + Jenkins |
| Reporting | Allure, Surefire, Cucumber |
| Code Coverage | JaCoCo |
| Code Quality | Checkstyle |
| Dependency Security | OWASP Dependency Check |
| Failure Handling | Retry Analyzer + Screenshot Listener |

---

# 🧩 Application Coverage

The framework currently contains automation implementations for three applications.

| Application | UI | BDD | Configuration | Page Objects |
|---|:---:|:---:|:---:|:---:|
| VWO | ✅ | ✅ | ✅ | ✅ |
| OrangeHRM | ✅ | ✅ | ✅ | ✅ |
| Katalon | ✅ | ✅ | ✅ | ✅ |

The multi-application structure demonstrates how common automation infrastructure can be reused while keeping application-specific implementations isolated.

---

# 🏗️ Framework Architecture

The project follows a layered structure.

```text
src/
├── main/
│   ├── java/com/prasad_v/
│   │
│   ├── base/
│   │   └── CommonToAllPage
│   │
│   ├── driver/
│   │   ├── DriverManagerTL
│   │   └── DriverManagerCloud
│   │
│   ├── utils/
│   │   ├── ConfigManager
│   │   ├── LoggerUtil
│   │   ├── APIUtil
│   │   ├── CredentialResolver
│   │   └── ...
│   │
│   └── apps/
│       ├── vwo/
│       │   └── pages/
│       │
│       ├── orangehrm/
│       │   └── pages/
│       │
│       └── katalon/
│           └── pages/
│
├── resources/
│   └── config/
│       ├── vwo/
│       ├── orangehrm/
│       └── katalon/
│
└── test/
    ├── java/com/prasad_v/
    │
    ├── base/
    │   └── CommonToAllTest
    │
    ├── cucumber/
    │   └── hooks/
    │
    ├── listeners/
    │   ├── RetryAnalyzer
    │   └── ScreenshotListener
    │
    └── apps/
        ├── vwo/
        │   ├── tests/
        │   ├── runner/
        │   └── definitions/
        │
        ├── orangehrm/
        │   ├── tests/
        │   ├── runner/
        │   └── definitions/
        │
        └── katalon/
            ├── tests/
            ├── runner/
            └── definitions/

src/test/resources/
└── features/
    ├── vwo/
    ├── orangehrm/
    └── katalon/
```

---

# 🔧 Framework Design

## 1. Reusable Base Layer

Common functionality is centralized in reusable base classes rather than being duplicated across application tests.

Examples include:

- `CommonToAllPage`
- `CommonToAllTest`

This provides a common foundation for page objects and test classes.

---

## 2. Driver Management

Browser/WebDriver management is separated into the framework's driver layer.

```text
driver/
├── DriverManagerTL
└── DriverManagerCloud
```

Keeping driver management outside individual test classes allows browser/session handling to remain independent from application-specific test logic.

---

## 3. Page Object Model

Application-specific page objects are maintained under their respective application modules.

```text
apps/
├── vwo/pages/
├── orangehrm/pages/
└── katalon/pages/
```

This separates:

```text
Test Intent
     ↓
Page Object
     ↓
UI Interaction
```

from test implementation.

The result is a cleaner separation between **what the test verifies** and **how the application UI is interacted with**.

---

## 4. Configuration Management

Configuration is separated by application and environment.

```text
resources/
└── config/
    ├── vwo/
    │   ├── qa.properties
    │   └── prod.properties
    │
    ├── orangehrm/
    │   └── qa.properties
    │
    └── katalon/
        └── qa.properties
```

This allows application and environment configuration to be managed independently from test logic.

---

## 5. Cucumber / BDD Layer

BDD support is organized into:

```text
features
   ↓
runner
   ↓
step definitions
   ↓
page objects
```

Cucumber hooks provide shared lifecycle behavior for BDD execution.

BDD scenarios can also be filtered using Cucumber tags.

Example:

```bash
-Dcucumber.filter.tags="@Smoke"
```

or:

```bash
-Dcucumber.filter.tags="@Regression"
```

---

## 6. Failure Handling

The framework includes listener-based support for test failures.

```text
listeners/
├── RetryAnalyzer
└── ScreenshotListener
```

The retry mechanism provides controlled handling of failures, while screenshot listeners help capture additional evidence when tests fail.

Retry behavior can also be controlled from CI execution.

---

# 🧪 Test Execution

The framework supports multiple execution models.

## TestNG

Dedicated TestNG suites are provided for the different applications and execution types.

| Suite | Purpose |
|---|---|
| `testng_vwo.xml` | VWO UI tests |
| `testng_vwo_bdd.xml` | VWO BDD execution |
| `testng_orangehrm.xml` | OrangeHRM UI tests |
| `testng_orangehrm_bdd.xml` | OrangeHRM BDD execution |
| `testng_katalon.xml` | Katalon UI tests |
| `testng_katalon_bdd.xml` | Katalon BDD execution |
| `testng_api_tests.xml` | API tests |
| `testng_docker_grid.xml` | Docker/Grid execution |

---

# ▶️ Running the Framework Locally

## Prerequisites

Install:

- Java 17+
- Maven 3.6+
- Chrome or Firefox
- Docker — required for Docker/Grid execution

Verify Java and Maven:

```bash
java -version
mvn -version
```

---

## UI Tests

### VWO

```bash
mvn clean test \
-Dapp=vwo \
-Denv=qa \
-Dbrowser=chrome \
-Dsurefire.suiteXmlFiles=testng_vwo.xml
```

### OrangeHRM

```bash
mvn clean test \
-Dapp=orangehrm \
-Denv=qa \
-Dbrowser=chrome \
-Dsurefire.suiteXmlFiles=testng_orangehrm.xml
```

### Katalon

```bash
mvn clean test \
-Dapp=katalon \
-Denv=qa \
-Dbrowser=chrome \
-Dsurefire.suiteXmlFiles=testng_katalon.xml
```

---

# 🥒 BDD Execution

### VWO

```bash
mvn clean test \
-Dapp=vwo \
-Denv=qa \
-Dbrowser=chrome \
-Dsurefire.suiteXmlFiles=testng_vwo_bdd.xml
```

### OrangeHRM

```bash
mvn clean test \
-Dapp=orangehrm \
-Denv=qa \
-Dbrowser=chrome \
-Dsurefire.suiteXmlFiles=testng_orangehrm_bdd.xml
```

### Katalon

```bash
mvn clean test \
-Dapp=katalon \
-Denv=qa \
-Dbrowser=chrome \
-Dsurefire.suiteXmlFiles=testng_katalon_bdd.xml
```

---

# 🏷️ BDD Tag Filtering

Run only smoke scenarios:

```bash
mvn clean test \
-Dapp=vwo \
-Denv=qa \
-Dsurefire.suiteXmlFiles=testng_vwo_bdd.xml \
-Dcucumber.filter.tags="@Smoke"
```

Run regression scenarios:

```bash
mvn clean test \
-Dapp=vwo \
-Denv=qa \
-Dsurefire.suiteXmlFiles=testng_vwo_bdd.xml \
-Dcucumber.filter.tags="@Regression"
```

---

# 🔌 API Tests

API tests can be executed using the dedicated TestNG suite:

```bash
mvn clean test \
-Dsurefire.suiteXmlFiles=testng_api_tests.xml
```

---

# 🌐 Docker + Selenium Grid

Start the Docker-based Grid environment:

```bash
docker-compose up -d
```

Execute the Grid suite:

```bash
mvn clean test \
-Dsurefire.suiteXmlFiles=testng_docker_grid.xml
```

Stop the Grid environment:

```bash
docker-compose down
```

This provides a repeatable browser execution environment for Grid-based automation.

---

# 🔄 CI/CD

The framework is integrated with both **GitHub Actions** and **Jenkins**.

## GitHub Actions

### Pull Request Checks

The PR workflow performs framework and automation checks including:

- Build verification
- UI test execution
- BDD execution
- Multi-application execution
- Checkstyle
- Optional Sonar analysis
- OWASP dependency scanning
- Docker configuration validation
- Slack notifications

Retry behavior is intentionally controlled for PR validation.

---

### Selenium Test Workflow

The Selenium workflow supports:

- Push-triggered execution
- Nightly execution
- Manual execution
- Multiple applications
- UI and BDD suites
- Browser selection
- Configurable retry count
- Slack notifications
- Nightly execution summaries

The execution model can be represented as:

```text
Git Push / PR / Nightly / Manual
              │
              ▼
       GitHub Actions
              │
              ▼
        Execution Matrix
       ┌──────┼──────┐
       ▼      ▼      ▼
      VWO  OrangeHRM Katalon
       │      │      │
       └──────┼──────┘
              ▼
          Test Results
              │
              ▼
       Reports / Alerts
```

---

### Release Workflow

The release workflow supports tag-based releases using:

```text
v*.*.*
```

and automatically generates a GitHub release with quick-start information.

---

# 🔨 Jenkins

The repository also contains a parameterized Jenkins pipeline.

Supported pipeline parameters include:

- Browser
- Environment
- Suite
- Retry count

The pipeline also includes parallel quality/security stages such as:

```text
Jenkins
   │
   ├── Test Execution
   │
   ├── Checkstyle
   │
   ├── OWASP Dependency Check
   │
   ├── Docker Compose Validation
   │
   └── Allure Reporting
```

Slack notifications are used for pipeline status reporting.

---

# 📊 Reporting & Quality Checks

The framework produces multiple forms of test and engineering feedback.

| Report / Check | Location |
|---|---|
| Surefire XML | `target/surefire-reports/` |
| Allure | `target/site/allure-maven-plugin/` |
| JaCoCo | `target/site/jacoco/` |
| Cucumber HTML | `target/cucumber-reports/{app}/cucumber.html` |
| Cucumber JSON | `target/cucumber-reports/{app}/cucumber.json` |
| OWASP Dependency Check | `target/dependency-check-report.html` |

### Allure

Generate and serve the Allure report:

```bash
mvn allure:serve
```

---

# 🛡️ Quality Engineering Practices

The framework incorporates quality checks beyond UI test execution.

### Code Quality

```text
Checkstyle
    ↓
Code quality validation
```

### Dependency Security

```text
OWASP Dependency Check
    ↓
Dependency vulnerability analysis
```

### Code Coverage

```text
JaCoCo
    ↓
Coverage reporting
```

### Test Reporting

```text
TestNG / Surefire
       +
Cucumber
       +
Allure
```

The objective is to provide feedback not only on whether tests passed, but also on the quality and maintainability of the automation codebase.

---

# 📁 Repository Structure

At the repository level:

```text
selenium-automation-framework/
│
├── .github/
│   └── workflows/
│
├── src/
│   ├── main/
│   └── test/
│
├── Personal_Docs/
│
├── .env.example
├── .gitignore
├── Dockerfile
├── Jenkinsfile
│
├── categories.json
├── docker-compose.yml
├── environment.properties
├── pom.xml
│
├── test-ci-workflow.md
│
├── testng_api_tests.xml
├── testng_docker_grid.xml
│
├── testng_vwo.xml
├── testng_vwo_bdd.xml
├── testng_vwo_pom.xml
├── testng_vwo_pom_retry.xml
│
├── testng_orangehrm.xml
├── testng_orangehrm_bdd.xml
│
├── testng_katalon.xml
└── testng_katalon_bdd.xml
```

---

# 🧠 Engineering Principles

The framework is organized around several core principles:

### Separation of Concerns

Application-specific automation remains separate from reusable framework infrastructure.

### Reusability

Common driver, configuration, logging, API, and test functionality is centralized rather than duplicated.

### Maintainability

Page Objects isolate UI interaction details from test intent.

### Configurability

Application and environment configuration are separated from test implementation.

### Multiple Execution Models

The same framework supports:

- TestNG
- Cucumber / BDD
- API testing
- Docker/Grid execution
- CI/CD execution

### Fast Feedback

CI pipelines provide automated feedback through:

- Test execution
- Code quality checks
- Dependency security checks
- Reports
- Notifications

---

# 🔮 Roadmap

Planned framework improvements include:

- Extend retry mechanisms consistently across application suites
- Introduce application-level data-driven datasets using Excel/Apache POI

Additional improvements will be evaluated as the framework evolves.

---

# 🔐 Security

Sensitive credentials and environment-specific secrets should not be committed to source control.

Use the provided environment/configuration mechanisms and CI secret management where applicable.

The repository includes:

```text
.env.example
```

as a reference for environment configuration.

---

# 📌 Why This Repository Exists

This project is intended to demonstrate how a Selenium automation codebase can evolve beyond individual test scripts into a reusable automation framework.

The focus is on:

```text
Reusable Automation
        +
Framework Architecture
        +
Multi-Application Support
        +
CI/CD
        +
Containerized Execution
        +
Reporting
        +
Quality & Security Checks
```

rather than simply demonstrating Selenium syntax.

---

## Author

**Prasad**

Software Test Engineer / SDET focused on test automation, framework engineering, and quality engineering.
```
