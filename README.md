# Selenium Automation Framework (Java + TestNG)

Production-focused automation framework with Selenium 4, TestNG, Maven, and CI/CD for multi-application UI and API testing.

## Current App Status

| App | Status | Coverage |
| --- | --- | --- |
| `vwo` | Active | POM + TestNG + BDD + config |
| `orangehrm` | Active | POM + TestNG + BDD + config |
| `katalon` | Active | POM + TestNG + BDD + config |

## Tech Stack

- Java 17
- Selenium 4
- TestNG
- Maven
- Cucumber (BDD)
- REST Assured
- Allure
- JaCoCo
- Checkstyle
- OWASP Dependency Check
- GitHub Actions + Jenkins

## Project Structure

```text
src/
  main/
    java/com/prasad_v/
      base/                      # CommonToAllPage (app-agnostic)
      driver/                    # DriverManagerTL, DriverManagerCloud
      utils/                     # ConfigManager, LoggerUtil, APIUtil, etc.
      apps/
        vwo/pages/               # VWO Page Objects
        orangehrm/pages/         # OrangeHRM Page Objects
        katalon/pages/           # Katalon Page Objects
    resources/
      config/
        vwo/
          qa.properties
          prod.properties
        orangehrm/
          qa.properties
        katalon/
          qa.properties
  test/
    java/com/prasad_v/
      base/                      # CommonToAllTest
      listeners/                 # RetryAnalyzer, ScreenshotListener
      apps/
        vwo/
          tests/
          runner/
          definitions/
        orangehrm/
          tests/
          runner/
          definitions/
        katalon/
          tests/
          runner/
          definitions/
    resources/features/
      vwo/
      orangehrm/
      katalon/
```

## TestNG Suites

| Suite | Description |
| --- | --- |
| `testng_vwo.xml` | VWO UI tests |
| `testng_vwo_bdd.xml` | VWO BDD runner |
| `testng_orangehrm.xml` | OrangeHRM UI tests |
| `testng_orangehrm_bdd.xml` | OrangeHRM BDD runner |
| `testng_katalon.xml` | Katalon UI tests |
| `testng_katalon_bdd.xml` | Katalon BDD runner |
| `testng_api_tests.xml` | API tests |
| `testng_docker_grid.xml` | Selenium Grid tests |

## Local Execution

Prerequisites:
- Java 17+
- Maven 3.6+
- Chrome or Firefox installed

### UI Tests

```bash
# VWO
mvn clean test -Dapp=vwo -Denv=qa -Dbrowser=chrome -Dsurefire.suiteXmlFiles=testng_vwo.xml

# OrangeHRM
mvn clean test -Dapp=orangehrm -Denv=qa -Dbrowser=chrome -Dsurefire.suiteXmlFiles=testng_orangehrm.xml

# Katalon
mvn clean test -Dapp=katalon -Denv=qa -Dbrowser=chrome -Dsurefire.suiteXmlFiles=testng_katalon.xml
```

### BDD Tests

```bash
# VWO BDD
mvn clean test -Dapp=vwo -Denv=qa -Dbrowser=chrome -Dsurefire.suiteXmlFiles=testng_vwo_bdd.xml

# OrangeHRM BDD
mvn clean test -Dapp=orangehrm -Denv=qa -Dbrowser=chrome -Dsurefire.suiteXmlFiles=testng_orangehrm_bdd.xml

# Katalon BDD
mvn clean test -Dapp=katalon -Denv=qa -Dbrowser=chrome -Dsurefire.suiteXmlFiles=testng_katalon_bdd.xml
```

### Tag Filtering (BDD)

```bash
# Smoke only
mvn clean test -Dapp=vwo -Denv=qa -Dsurefire.suiteXmlFiles=testng_vwo_bdd.xml -Dcucumber.filter.tags="@Smoke"

# Regression only
mvn clean test -Dapp=vwo -Denv=qa -Dsurefire.suiteXmlFiles=testng_vwo_bdd.xml -Dcucumber.filter.tags="@Regression"
```

### API Tests

```bash
mvn clean test -Dsurefire.suiteXmlFiles=testng_api_tests.xml
```

### Docker Grid

```bash
docker-compose up -d
mvn clean test -Dsurefire.suiteXmlFiles=testng_docker_grid.xml
docker-compose down
```

### Allure Report

```bash
mvn allure:serve
```

## CI/CD (Multi-App)

### GitHub Actions

- `.github/workflows/pr-checks.yml`
  - Build verification
  - UI + BDD matrix tests for all apps (Chrome)
  - Strict retry policy (`retryCount=0`)
  - Checkstyle, Sonar (optional), OWASP scan, Docker config validation
  - Slack notification on pass/fail
- `.github/workflows/selenium-tests.yml`
  - Push + nightly + manual runs
  - Matrix across apps (`vwo`, `orangehrm`, `katalon`), suite types (`ui`, `bdd`), and browser
  - Manual input supports single-app or all-app execution and configurable retry count
  - Slack notification per run + nightly summary
- `.github/workflows/release.yml`
  - Tag-based release workflow (`v*.*.*`)
  - Auto-generates GitHub Release with quick start examples
  - Slack notification on release

### Jenkins

- Parameterized pipeline (`Jenkinsfile`)
- Parameters: browser, env, suite, retry count
- Parallel stages: Checkstyle + OWASP security scan
- Allure report generation
- Docker Compose validation on master
- Slack notifications: pass / fail / unstable

## Reports

| Report | Location |
| --- | --- |
| Surefire XML | `target/surefire-reports/` |
| Allure | `target/site/allure-maven-plugin/` |
| JaCoCo Coverage | `target/site/jacoco/` |
| Cucumber HTML | `target/cucumber-reports/{app}/cucumber.html` |
| Cucumber JSON | `target/cucumber-reports/{app}/cucumber.json` |
| Security (OWASP) | `target/dependency-check-report.html` |

## Roadmap (Next)

- Retry mechanism across all apps
- App-level data-driven datasets (Excel/POI)
