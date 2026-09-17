# 🚀 CI/CD Setup Guide

## 📋 Overview
Complete guide for setting up Continuous Integration and Continuous Deployment using GitHub Actions.

---

## ✅ What's Included

### 1. Automated Workflows
- **PR Checks** (`pr-checks.yml`) - Runs on every pull request
- **Selenium Tests** (`selenium-tests.yml`) - Runs on push to master/develop and schedule
- **Release Workflow** (`release.yml`) - Automated release process

### 2. Quality Gates
- ✅ Build verification
- ✅ UI & integration tests (multiple browsers)
- ✅ Code coverage (JaCoCo)
- ✅ Code quality (Checkstyle)
- ✅ Security scanning (OWASP)
- ✅ Docker build verification
- ✅ Slack notifications
- ✅ Allure report generation
- ✅ SonarQube analysis (optional)
- ✅ Codecov integration (optional)

---

## 🔧 Setup Instructions

### Step 1: Configure GitHub Secrets

Go to: **Settings → Secrets and variables → Actions → New repository secret**

#### Required Secrets for Test Execution:
```
VWO_USERNAME             # VWO valid username
VWO_PASSWORD             # VWO valid password
VWO_INVALID_USERNAME     # VWO invalid username for negative tests
VWO_INVALID_PASSWORD     # VWO invalid password for negative tests
VWO_EXPECTED_USERNAME    # VWO expected username after login
OHR_USERNAME             # OrangeHRM valid username
OHR_PASSWORD             # OrangeHRM valid password
KATALON_USERNAME         # Katalon valid username
KATALON_PASSWORD         # Katalon valid password
KATALON_INVALID_USERNAME # Katalon invalid username for negative tests
KATALON_INVALID_PASSWORD # Katalon invalid password for negative tests
APP_USERNAME             # Generic app username
APP_PASSWORD             # Generic app password
APP_INVALID_USERNAME     # Generic app invalid username for negative tests
APP_INVALID_PASSWORD     # Generic app invalid password for negative tests
APP_EXPECTED_USERNAME    # Generic app expected username after login
```

#### Optional Secrets:
```
SONAR_TOKEN              # From SonarCloud (optional)
BROWSERSTACK_USERNAME    # For cloud execution
BROWSERSTACK_ACCESS_KEY  # For cloud execution
LAMBDATEST_USERNAME      # For cloud execution (optional)
LAMBDATEST_ACCESS_KEY    # For cloud execution (optional)
NVD_API_KEY              # For OWASP NVD API (optional)
SLACK_WEBHOOK_URL        # For Slack notifications
CODECOV_TOKEN            # For Codecov integration
```

---

## 📊 Workflow Details

### PR Checks Workflow (.github/workflows/pr-checks.yml)

**Triggers**: Pull requests to `master` or `develop`

**Concurrency**: Cancels in-progress runs for same PR

**Jobs**:

1. **build-verification**
   - Checkout code
   - Set up JDK 21
   - Build & run unit tests (testng_unit.xml)
   - Generate JaCoCo report
   - Upload unit test artifacts

2. **ui-matrix-tests** (runs after build-verification & security-scan)
   - Tests VWO, OrangeHRM, Katalon applications
   - Matrix: app × browser (chrome/firefox) × suite_type (ui/bdd)
   - Setup Chrome/Firefox browsers
   - Run UI suite with appropriate credentials
   - Generate JaCoCo report
   - Upload coverage to Codecov (optional)
   - Generate Allure report
   - Upload test artifacts (surefire-reports, allure-report, jacoco)

3. **code-quality** (runs after ui-matrix-tests)
   - Checkout code with full history
   - Set up JDK 21
   - Download all test artifacts
   - Run Checkstyle
   - Compile classes for Sonar analysis
   - Run SonarQube scan (if SONAR_TOKEN provided)
   - Continue on error for Sonar scan

4. **security-scan** (runs after build-verification)
   - Checkout code
   - Set up JDK 21
   - Cache OWASP dependency-check NVD database
   - Run OWASP Dependency Check
   - Upload security report

5. **docker-build** (runs after build-verification, security-scan, code-quality)
   - Checkout code
   - Verify Docker Compose configuration
   - Pull Docker images

6. **notify-success** (runs after all jobs succeed)
   - Send Slack notification if SLACK_WEBHOOK_URL is set

### Selenium Tests Workflow (.github/workflows/selenium-tests.yml)

**Triggers**: 
- Push to `master` or `develop`
- Schedule: Daily at 2 AM
- Workflow dispatch (manual trigger)

**Jobs**:
- **test** matrix: VWO/OrangeHRM/Katalon × chrome/firefox × ui/bdd/both
  - Checkout code
  - Set up JDK 21
  - Setup Chrome/Firefox
  - Run tests with credentials from secrets
  - Generate Allure report
  - Upload Allure report artifact
  - Upload screenshots on failure
  - Send Slack notification on completion
- **nightly-report** (runs after test on schedule trigger)
  - Send Slack summary of nightly test run

### Release Workflow (.github/workflows/release.yml)

**Triggers**: 
- Push to master tag
- Workflow dispatch

**Jobs**:
- **create-release**
  - Checkout code
  - Set up JDK 21
  - Create GitHub release from tag
  - Upload release notes
  - Send Slack notification

---

## 🎯 Quality Metrics

### Code Coverage
- **Target**: 70%+
- **Tool**: JaCoCo
- **Report**: `target/site/jacoco/index.html`

### Code Quality
- **Tool**: Checkstyle (Google style)
- **Report**: `target/checkstyle-result.xml`

### Security
- **Tool**: OWASP Dependency Check
- **Report**: `target/dependency-check-report.html`
- **Threshold**: CVSS 8+ fails build (can be overridden with continue-on-error)

---

## 📈 Best Practices

### 1. Fast Feedback
- Run unit tests first in build-verification job
- Parallel job execution using needs dependencies
- Cache Maven dependencies and OWASP NVD database
- Use fail-fast: false in matrices to run all combinations

### 2. Fail Fast
- Stop on critical failures in individual jobs
- Continue on warnings for optional integrations (Sonar, Codecov)
- Clear error messages in workflow steps
- Use continue-on-error: true for non-critical steps

### 3. Artifact Management
- Upload test results, coverage reports, security reports
- Keep artifacts for 30 days (except screenshots: 7 days)
- Download artifacts for local analysis from Actions tab

### 4. Security
- Never commit secrets
- Use GitHub Secrets for all credentials
- Rotate tokens regularly
- Use environment variables in jobs, not in plain text

### 5. Notifications
- Slack integration for build status, test completion, nightly summaries
- PR comments with results via workflow
- Email notifications available through GitHub Actions

### 6. Optimization
- Use concurrency groups to cancel redundant runs
- Matrix strategies for parallel execution across browsers/apps
- Conditional job execution based on event type
- Retry mechanisms for flaky tests via -DretryCount parameter

---

## 📝 Maintenance

### Weekly Tasks
- Review failed builds in Actions tab
- Update dependencies via Dependabot
- Check security alerts in GitHub Security tab
- Monitor artifact storage usage

### Monthly Tasks
- Review code coverage trends across workflow runs
- Update quality gates based on project evolution
- Rotate access tokens for third-party services
- Review and optimize workflow performance

### Quarterly Tasks
- Update GitHub Actions versions to latest stable
- Review and optimize workflows for efficiency
- Team training on CI/CD best practices
- Audit secret usage and rotation

---

## 🎓 Additional Resources

- [GitHub Actions Docs](https://docs.github.com/en/actions)
- [Maven Surefire Plugin](https://maven.apache.org/surefire/maven-surefire-plugin/)
- [JaCoCo Documentation](https://www.jacoco.org/jacoco/trunk/doc/)
- [SonarCloud Setup](https://sonarcloud.io/documentation)
- [OWASP Dependency Check](https://owasp.org/www-project-dependency-check/)
- [Browser Actions Setup](https://github.com/marketplace/actions/setup-chrome-firefox)
- [Slack GitHub Action](https://github.com/slackapi/slack-github-action)
- [Codecov Action](https://github.com/codecov/codecov-action)

---

## ✅ Checklist

- [x] GitHub Secrets configured (test execution credentials)
- [x] Branch protection enabled (via BRANCH_PROTECTION_GUIDE.md)
- [ ] SonarQube project created (optional)
- [x] Test PR created and verified
- [x] All checks passing (in PR checks workflow)
- [x] Team trained on workflow (implied by usage)
- [x] Documentation reviewed

---

**File**: `docs/CICD_SETUP_GUIDE.md`
**Last Updated**: September 17, 2026
**Status**: ✅ Complete