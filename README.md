# Selenium Automation Framework (Java + TestNG)

Production-focused automation framework with Selenium 4, TestNG, Maven, and CI/CD for multi-application UI and API testing.

## Current App Status

| App | Status | Coverage |
| --- | --- | --- |
| `vwo` | Active | POM + TestNG + BDD + config |
| `orangehrm` | Active | POM + TestNG + BDD + config |
| `katalon` | Active | POM + TestNG + config |

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
    java/com/prasad_v/apps/
      vwo/pages/
      orangehrm/pages/
      katalon/pages/
    java/com/prasad_v/utils/
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
    java/com/prasad_v/apps/
      vwo/tests/
      vwo/runner/
      vwo/definitions/
      orangehrm/tests/
      orangehrm/runner/
      orangehrm/definitions/
      katalon/tests/
    resources/features/
      vwo/
      orangehrm/
```

## TestNG Suites

- `testng_vwo.xml` - VWO UI tests
- `testng_vwo_bdd.xml` - VWO BDD runner
- `testng_orangehrm.xml` - OrangeHRM UI tests
- `testng_orangehrm_bdd.xml` - OrangeHRM BDD runner
- `testng_katalon.xml` - Katalon UI tests
- `testng_api_tests.xml` - API tests
- `testng_docker_grid.xml` - Selenium Grid tests

## Local Execution

Prerequisites:
- Java 17+
- Maven 3.6+
- Chrome or Firefox installed

Run VWO UI:

```bash
mvn clean test -Dapp=vwo -Denv=qa -Dbrowser=chrome -Dsurefire.suiteXmlFiles=testng_vwo.xml
```

Run OrangeHRM UI:

```bash
mvn clean test -Dapp=orangehrm -Denv=qa -Dbrowser=chrome -Dsurefire.suiteXmlFiles=testng_orangehrm.xml
```

Run VWO BDD:

```bash
mvn clean test -Dapp=vwo -Denv=qa -Dbrowser=chrome -Dsurefire.suiteXmlFiles=testng_vwo_bdd.xml
```

Run OrangeHRM BDD:

```bash
mvn clean test -Dapp=orangehrm -Denv=qa -Dbrowser=chrome -Dsurefire.suiteXmlFiles=testng_orangehrm_bdd.xml
```

Run API suite (ReqRes key required):

```bash
mvn clean test -Dsurefire.suiteXmlFiles=testng_api_tests.xml -Dreqres.api.key=<your_key>
```

Run Katalon UI:

```bash
mvn clean test -Dapp=katalon -Denv=qa -Dbrowser=chrome -Dsurefire.suiteXmlFiles=testng_katalon.xml

- `testng_vwo.xml` - VWO UI tests
- `testng_vwo_bdd.xml` - VWO BDD runner
- `testng_orangehrm.xml` - OrangeHRM UI tests
- `testng_orangehrm_bdd.xml` - OrangeHRM BDD runner
- `testng_api_tests.xml` - API tests
- `testng_docker_grid.xml` - Selenium Grid tests

## Local Execution

Prerequisites:
- Java 17+
- Maven 3.6+
- Chrome or Firefox installed

Run VWO UI:

```bash
mvn clean test -Dapp=vwo -Denv=qa -Dbrowser=chrome -DretryCount=1 -Dsurefire.suiteXmlFiles=testng_vwo.xml
```

Run OrangeHRM UI:

```bash
mvn clean test -Dapp=orangehrm -Denv=qa -Dbrowser=chrome -DretryCount=1 -Dsurefire.suiteXmlFiles=testng_orangehrm.xml
```

Run VWO BDD:

```bash
mvn clean test -Dapp=vwo -Denv=qa -Dbrowser=chrome -DretryCount=1 -Dsurefire.suiteXmlFiles=testng_vwo_bdd.xml
```

Run OrangeHRM BDD:

```bash
mvn clean test -Dapp=orangehrm -Denv=qa -Dbrowser=chrome -DretryCount=1 -Dsurefire.suiteXmlFiles=testng_orangehrm_bdd.xml
```

Run API suite (ReqRes key required):

```bash
mvn clean test -DretryCount=1 -Dsurefire.suiteXmlFiles=testng_api_tests.xml -Dreqres.api.key=<your_key>
```

Run Katalon UI:

```bash
mvn clean test -Dapp=katalon -Denv=qa -Dbrowser=chrome -DretryCount=1 -Dsurefire.suiteXmlFiles=testng_katalon.xml
```

Retry behavior:
- `-DretryCount=1` is the recommended default for unstable environments.
- `-DretryCount=0` runs in strict mode with no retry.

## CI/CD (Multi-App)

### GitHub Actions

- `.github/workflows/pr-checks.yml`
  - Build verification
  - UI matrix tests for `vwo`, `orangehrm`, and `katalon` (Chrome)
  - Strict retry policy (`retryCount=0`)
  - Checkstyle, Sonar (optional), OWASP scan, Docker config validation
- `.github/workflows/selenium-tests.yml`
  - Push + nightly + manual runs
  - Matrix across apps (`vwo`, `orangehrm`, `katalon`), suite types (`ui`, `bdd`), and browser
  - Manual input supports single-app or all-app execution
  - Manual input supports single-app or all-app execution and configurable retry count
- `.github/workflows/release.yml`
  - Tag-based release workflow
  - Includes multi-app quick start examples

### Jenkins

- Parameterized pipeline supports browser, env, suite, and optional Docker Grid
- Retry is configurable by `RETRY_COUNT` parameter (`1` default, `0` strict)
- For API suite, provide ReqRes key using either:
  - `REQRES_API_KEY` environment variable, or
  - `-Dreqres.api.key=...` via `MAVEN_OPTS` / `JAVA_TOOL_OPTIONS`

## Reports

- Surefire XML: `target/surefire-reports/`
- Allure report: `target/site/allure-maven-plugin/`
- JaCoCo report: `target/site/jacoco/`

## Roadmap (Next)

- App-level data-driven datasets (Excel/POI)
