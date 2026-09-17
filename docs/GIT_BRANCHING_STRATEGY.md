# 🌳 Git Branching Strategy & PR Workflow

## 📋 Overview
Industry-standard Git branching strategy with pull request workflow and automated checks.

---

## 🌳 Branch Structure

```
main (protected)
  ↑
develop (protected)
  ↑
feature/* (developers work here)
bugfix/*
hotfix/*
release/*
```

> **Note**: The default branch was renamed from `master` to `main` in late 2022 for inclusivity.

---

## 🔐 GitHub Branch Protection Rules

### Main Branch (Strictest)
Go to: **Settings → Branches → Add rule → `main`**

```
✅ Require a pull request before merging
   └── Required approvals: 1
   └── Dismiss stale reviews on new commits
   └── Require review from Code Owners

✅ Require status checks to pass before merging
   └── Require branches to be up to date
   └── Required checks:
       - build-verification
       - ui-matrix-tests
       - code-quality
       - security-scan
       - docker-build
       - notify-success

✅ Require conversation resolution before merging
✅ Require signed commits
✅ Include administrators
✅ Do not allow force pushes
✅ Do not allow deletions
```

### Develop Branch (Relaxed)
Go to: **Settings → Branches → Add rule → `develop`**

```
✅ Require a pull request before merging
   └── Required approvals: 1

✅ Require status checks to pass:
   - build-verification
   - ui-matrix-tests
   - code-quality
   - security-scan
   - docker-build
   - notify-success
```

---

## 📋 PR Workflow

### Step 1: Create Feature Branch
```bash
git checkout develop
git pull origin develop
git checkout -b feature/login-enhancement
```

### Step 2: Work & Commit
```bash
git add .
git commit -m "feat: add enhanced login validation"
git push origin feature/login-enhancement
```

### Step 3: Create PR
- Go to GitHub
- Create PR: `feature/login-enhancement` → `develop`
- Fill PR template
- Wait for checks

### Step 4: Automated Checks Run
- ✅ Build verification (unit tests)
- ✅ UI Matrix Tests (VWO/OrangeHRM/Katalon × chrome/firefox × ui/bdd)
- ✅ Code Quality (Checkstyle + SonarQube if configured)
- ✅ Security Scan (OWASP Dependency Check)
- ✅ Docker Build Verification
- ✅ Notify Success (Slack notification)

### Step 5: Code Review
- Reviewers approve/request changes
- Address feedback
- Push updates

### Step 6: Merge
- All checks pass ✅
- Approvals received ✅
- Merge to develop
- Delete feature branch

### Step 7: Release to Main
```bash
# Create release branch
git checkout -b release/v1.0.0 develop

# After testing, merge to main
PR: release/v1.0.0 → main
```

---

## 🔍 Pre-Merge Checks

1. **Build Check** - Code compiles and unit tests pass
2. **UI Matrix Tests** - Cross-browser, cross-application E2E scenarios pass
3. **Code Quality** - Checkstyle passes, SonarQube gate (if configured)
4. **Security Scan** - OWASP Dependency Check (CVSS 8+ can fail build)
5. **Docker Build** - Docker Compose validation and image pulling
6. **Notifications** - Slack notification on success

---

## ✅ Post-Merge Actions

1. **Auto Deploy to Dev** (from develop branch via CI/CD)
2. **Auto Deploy to Staging** (from release branch)
3. **Auto Deploy to Prod** (from main branch)
4. **Tag Release** (main branch only)
5. **Generate Release Notes**
6. **Notify Team** (Slack/Email)
7. **Delete Feature Branch**

---

## 📝 Commit Message Convention

```
<type>(<scope>): <subject>

<body>

<footer>
```

### Types:
- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation
- `style`: Formatting
- `refactor`: Code restructuring
- `test`: Adding tests
- `chore`: Maintenance

### Examples:
```bash
git commit -m "feat(login): add remember me functionality"
git commit -m "fix(dashboard): resolve null pointer exception"
git commit -m "test(api): add integration tests for user service"
```

---

## 🎯 GitHub Settings to Configure

### 1. Branch Protection (Settings → Branches)

**For `main` branch:**
```
Branch name pattern: main
☑ Require pull request before merging
  ☑ Require approvals: 1
  ☑ Dismiss stale reviews
  ☑ Require review from Code Owners
☑ Require status checks to pass
  ☑ Require branches to be up to date
  ☑ build-verification
  ☑ ui-matrix-tests
  ☑ code-quality
  ☑ security-scan
  ☑ docker-build
  ☑ notify-success
☑ Require signed commits
☑ Include administrators
☐ Allow force pushes: Never
☐ Allow deletions: Never
```

**For `develop` branch:**
```
Branch name pattern: develop
☑ Require pull request before merging
  ☑ Require approvals: 1
☑ Require status checks to pass
  ☑ Require branches to be up to date
  ☑ build-verification
  ☑ ui-matrix-tests
  ☑ code-quality
  ☑ security-scan
  ☑ docker-build
  ☑ notify-success
```

### 2. Auto-delete Branches
```
Settings → General → Pull Requests
☑ Automatically delete head branches
```

### 3. Required Reviewers (CODEOWNERS)
See `.github/CODEOWNERS` file for automatic reviewer assignment.

---

## 📄 PR Template

The PR template is located at: `.github/PULL_REQUEST_TEMPLATE.md`

Create it if it doesn't exist with standard sections:
- Description of changes
- Related issues
- Screenshots (if applicable)
- Testing performed
- Checklist

---

## 🔐 Required Secrets (GitHub Settings)

Configure these in: **Settings → Secrets and variables → Actions → New repository secret**

#### Required for Test Execution:
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

#### Optional (for enhanced functionality):
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

## 🚀 Quick Commands

```bash
# Start new feature
git checkout develop && git pull
git checkout -b feature/my-feature

# Update from develop
git checkout develop && git pull
git checkout feature/my-feature
git rebase develop

# Push feature
git push origin feature/my-feature

# After PR merged
git checkout develop && git pull
git branch -d feature/my-feature
```

---

## ⚠️ Hotfix Process

```bash
# Critical bug in production
git checkout main
git checkout -b hotfix/critical-bug
# Fix the bug
git push origin hotfix/critical-bug

# Create PR to main (fast-track)
# After merge to main, also merge to develop
```

---

## ✅ Current Status Checklist

- [x] Branch protection on `main`
- [x] Branch protection on `develop`
- [x] CODEOWNERS configured (see .github/CODEOWNERS)
- [x] PR template in place (.github/PULL_REQUEST_TEMPLATE.md)
- [x] Required status checks set (6 jobs in pr-checks.yml)
- [x] GitHub Secrets configured (test execution credentials)
- [x] Automated branch deletion enabled
- [x] Slack notifications configured
- [x] Docker build verification in CI
- [x] Concurrent build prevention (via concurrency groups)
- [x] 30-minute timeout (default GitHub Actions limit)
- [x] Workspace cleanup (GitHub Actions provides clean runners)

---

**File**: `docs/GIT_BRANCHING_STRATEGY.md`
**Last Updated**: September 17, 2026
**Status**: ✅ Complete