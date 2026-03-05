# 🚀 CI/CD Setup Guide

## 📋 Overview
Complete guide for setting up Continuous Integration and Continuous Deployment using GitHub Actions.

---

## ✅ What's Included

### 1. Automated Workflows
- **PR Checks** (`pr-checks.yml`) - Runs on every pull request
- **Selenium Tests** (`selenium-tests.yml`) - Runs on push to master/develop

### 2. Quality Gates
- ✅ Build verification
- ✅ Unit & integration tests
- ✅ Code coverage (JaCoCo)
- ✅ Code quality (Checkstyle)
- ✅ Security scanning (OWASP)
- ✅ SonarQube analysis (optional)

---

## 🔧 Setup Instructions

### Step 1: Configure GitHub Secrets

Go to: **Settings → Secrets and variables → Actions → New repository secret**

#### Required Secrets:
```
SONAR_TOKEN              # From SonarCloud (optional)
BROWSERSTACK_USERNAME    # For cloud execution
BROWSERSTACK_ACCESS_KEY  # For cloud execution
LAMBDATEST_USERNAME      # For cloud execution (optional)
LAMBDATEST_ACCESS_KEY    # For cloud execution (optional)
```

#### Optional Secrets:
```
SLACK_WEBHOOK_URL        # For Slack notifications
CODECOV_TOKEN            # For Codecov integration
```

---

### Step 2: SonarQube Setup (Optional)

1. **Create SonarCloud Account**
   - Go to https://sonarcloud.io
   - Sign in with GitHub
   - Create new organization

2. **Create Project**
   - Click "+" → Analyze new project
   - Select your repository
   - Choose "With GitHub Actions"

3. **Generate Token**
   - My Account → Security → Generate Token
   - Copy token
   - Add to GitHub Secrets as `SONAR_TOKEN`

4. **Add SonarQube Plugin to pom.xml**
   ```xml
   <properties>
     <sonar.organization>your-org</sonar.organization>
     <sonar.host.url>https://sonarcloud.io</sonar.host.url>
   </properties>
   ```

---

### Step 3: Configure Branch Protection

#### For `master` branch:
1. Go to **Settings → Branches → Add rule**
2. Branch name pattern: `master`
3. Enable:
   - ✅ Require a pull request before merging
   - ✅ Require approvals (1)
   - ✅ Require status checks to pass
   - ✅ Require branches to be up to date
   - ✅ Require conversation resolution
   - ✅ Do not allow bypassing the above settings

4. Select required status checks:
   - `build-and-test`
   - `code-quality` (if SonarQube configured)
   - `security-scan`

#### For `develop` branch:
1. Same as master but with:
   - Require approvals: 0 (optional)
   - Allow force pushes (for rebasing)

---

### Step 4: Test the Workflow

```bash
# Create test branch
git checkout -b feature/test-cicd

# Make a small change
echo "# CI/CD Test" >> test.md

# Commit and push
git add test.md
git commit -m "test: verify CI/CD pipeline"
git push origin feature/test-cicd

# Create PR on GitHub
# Watch the checks run
```

---

## 📊 Workflow Details

### PR Checks Workflow

**Triggers**: Pull requests to `master` or `develop`

**Jobs**:

1. **build-and-test**
   - Checkout code
   - Set up JDK 17
   - Build project
   - Run tests
   - Generate coverage report
   - Upload test results

2. **code-quality**
   - SonarQube analysis
   - Code smell detection
   - Technical debt calculation

3. **security-scan**
   - OWASP dependency check
   - Vulnerability scanning
   - Security report generation

**Artifacts**:
- Test results (30 days)
- Coverage reports (30 days)
- Security reports (30 days)

---

### Selenium Tests Workflow

**Triggers**: Push to `master` or `develop`

**Jobs**:
- Run full test suite
- Generate Allure report
- Upload test artifacts

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
- **Threshold**: CVSS 8+ fails build

---

## 🔍 Viewing Reports

### Local Reports

```bash
# Code Coverage
mvn jacoco:report
open target/site/jacoco/index.html

# Checkstyle
mvn checkstyle:check
open target/checkstyle-result.xml

# Security Scan
mvn dependency-check:check
open target/dependency-check-report.html

# Allure Report
mvn allure:serve
```

### GitHub Actions Reports

1. Go to **Actions** tab
2. Click on workflow run
3. Scroll to **Artifacts** section
4. Download reports

---

## 🚨 Troubleshooting

### Build Fails on CI but Passes Locally

**Cause**: Environment differences

**Solution**:
```bash
# Clean build
mvn clean install

# Check Java version
java -version  # Should be 17

# Verify dependencies
mvn dependency:tree
```

---

### Tests Timeout on CI

**Cause**: Slower CI environment

**Solution**: Increase timeout in `testng.xml`
```xml
<suite name="Suite" time-out="300000">
```

---

### SonarQube Scan Fails

**Cause**: Missing token or wrong configuration

**Solution**:
1. Verify `SONAR_TOKEN` in GitHub Secrets
2. Check organization name in workflow
3. Ensure project exists on SonarCloud

---

### Security Scan Takes Too Long

**Cause**: OWASP downloads CVE database

**Solution**: 
- First run takes 10-15 minutes (normal)
- Subsequent runs are faster (cached)
- Use `continue-on-error: true` to not block PR

---

## 📈 Best Practices

### 1. Fast Feedback
- Run unit tests first
- Parallel job execution
- Cache Maven dependencies

### 2. Fail Fast
- Stop on critical failures
- Continue on warnings
- Clear error messages

### 3. Artifact Management
- Upload test results
- Keep reports for 30 days
- Download for local analysis

### 4. Security
- Never commit secrets
- Use GitHub Secrets
- Rotate tokens regularly

### 5. Notifications
- Slack integration (optional)
- Email on failure
- PR comments with results

---

## 🔄 Workflow Optimization

### Cache Maven Dependencies
```yaml
- name: Cache Maven packages
  uses: actions/cache@v3
  with:
    path: ~/.m2
    key: ${{ runner.os }}-m2-${{ hashFiles('**/pom.xml') }}
```

### Parallel Test Execution
```yaml
strategy:
  matrix:
    browser: [chrome, firefox, edge]
```

### Conditional Jobs
```yaml
if: github.event_name == 'pull_request'
```

---

## 📝 Maintenance

### Weekly Tasks
- Review failed builds
- Update dependencies
- Check security alerts

### Monthly Tasks
- Review code coverage trends
- Update quality gates
- Rotate access tokens

### Quarterly Tasks
- Update GitHub Actions versions
- Review and optimize workflows
- Team training on CI/CD

---

## 🎓 Additional Resources

- [GitHub Actions Docs](https://docs.github.com/en/actions)
- [Maven Surefire Plugin](https://maven.apache.org/surefire/maven-surefire-plugin/)
- [JaCoCo Documentation](https://www.jacoco.org/jacoco/trunk/doc/)
- [SonarCloud Setup](https://sonarcloud.io/documentation)
- [OWASP Dependency Check](https://owasp.org/www-project-dependency-check/)

---

## ✅ Checklist

- [ ] GitHub Secrets configured
- [ ] Branch protection enabled
- [ ] SonarQube project created (optional)
- [ ] Test PR created and verified
- [ ] All checks passing
- [ ] Team trained on workflow
- [ ] Documentation reviewed

---

**File**: `Personal_Docs/CICD_SETUP_GUIDE.md`  
**Last Updated**: Phase 7 - CI/CD Setup  
**Status**: ✅ Complete
