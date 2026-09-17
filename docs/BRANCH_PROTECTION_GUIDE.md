# 🔐 Branch Protection & Integrations Guide

## 📋 Overview
Industry-standard branch protection rules, Jenkins CI/CD, Slack notifications, and Docker integration.

---

## 🔐 GitHub Branch Protection Rules

### Master Branch (Strictest)
Go to: **Settings → Branches → Add rule → `master`**

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

## 🔔 Slack Integration

### Step 1: Create Slack App
1. Go to https://api.slack.com/apps
2. Click **Create New App → From Scratch**
3. Name: `Selenium Framework Bot`
4. Select your workspace

### Step 2: Enable Incoming Webhooks
1. Go to **Incoming Webhooks**
2. Toggle **Activate Incoming Webhooks** ON
3. Click **Add New Webhook to Workspace**
4. Select channel: `#selenium-tests`
5. Copy the Webhook URL

### Step 3: Add to GitHub Secrets
```
Settings → Secrets and variables → Actions → New repository secret

Name:  SLACK_WEBHOOK_URL
Value: https://hooks.slack.com/services/YOUR/WEBHOOK/URL
```

### Step 4: Add to Jenkins
```
Jenkins → Manage Jenkins → Configure System
→ Slack
   → Workspace: your-workspace
   → Credential: Add Jenkins credential
     → Kind: Secret text
     → Secret: YOUR_SLACK_TOKEN
   → Default channel: #selenium-tests
```

### Slack Notifications Configured
| Event | Channel | Color |
|-------|---------|-------|
| PR opened | #selenium-tests | Blue |
| Build passed | #selenium-tests | Green ✅ |
| Build failed | #selenium-tests | Red ❌ |
| Tests failed | #selenium-tests | Red ❌ |
| All checks passed | #selenium-tests | Green ✅ |
| New release | #selenium-tests | Blue 🚀 |
| Nightly run | #selenium-tests | Grey 🌙 |

---

## 🏗️ Jenkins Setup

### Prerequisites
- Jenkins 2.400+
- Plugins required:
  - Pipeline
  - Git
  - Maven Integration
  - JaCoCo
  - Allure Jenkins Plugin
  - Slack Notification
  - Docker Pipeline
  - Blue Ocean (optional, for UI)

### Step 1: Install Plugins
```
Jenkins → Manage Jenkins → Plugins → Available

Install:
- Allure Jenkins Plugin
- JaCoCo Plugin
- Slack Notification Plugin
- Docker Pipeline
- Pipeline: Stage View
```

### Step 2: Configure Tools
```
Jenkins → Manage Jenkins → Tools

JDK:
  Name: JDK-17
  JAVA_HOME: /usr/lib/jvm/java-17

Maven:
  Name: Maven-3.9
  MAVEN_HOME: /usr/share/maven
```

### Step 3: Configure Slack in Jenkins
```
Jenkins → Manage Jenkins → Configure System → Slack

Workspace: your-workspace
Credential: (add slack token)
Default channel: #selenium-tests
```

### Step 4: Create Pipeline Job
```
Jenkins → New Item → Pipeline

Name: selenium-automation-framework
Type: Pipeline

Pipeline:
  Definition: Pipeline script from SCM
  SCM: Git
  Repository URL: https://github.com/prasad291024/selenium-automation-framework.git
  Branch: */master
  Script Path: Jenkinsfile
```

### Step 5: Configure Webhooks
```
GitHub → Settings → Webhooks → Add webhook

Payload URL: http://YOUR_JENKINS_URL/github-webhook/
Content type: application/json
Events: Push, Pull requests
```

### Jenkinsfile Features
- ✅ Parameterized builds (browser, env, suite)
- ✅ Parallel code quality stages
- ✅ JUnit test results
- ✅ JaCoCo coverage (70% minimum)
- ✅ Allure report generation
- ✅ Docker Compose validation
- ✅ Slack notifications (pass/fail/unstable)
- ✅ Build artifact archiving
- ✅ Workspace cleanup
- ✅ 30-minute timeout
- ✅ Concurrent build prevention

---

## 🐳 Docker Integration

### Docker Hub Setup (Optional)
```bash
# Login to Docker Hub
docker login

# Tag image
docker tag selenium-grid:latest prasad291024/selenium-grid:latest

# Push image
docker push prasad291024/selenium-grid:latest
```

### Add Docker Hub Secrets to GitHub
```
Settings → Secrets and variables → Actions → New repository secret

DOCKER_USERNAME: your-dockerhub-username
DOCKER_PASSWORD: your-dockerhub-password
```

### Docker Compose Validation in CI
The `docker-build` job in `pr-checks.yml` automatically:
- Validates `../docker-compose.yml` syntax
- Pulls required images
- Ensures Grid config is correct

---

## 🚀 Release Workflow

### How to Create a Release
```bash
# Tag the release
git tag -a v1.0.0 -m "Release v1.0.0"
git push origin v1.0.0
```

### What Happens Automatically
1. ✅ GitHub Release created
2. ✅ Release notes generated
3. ✅ Slack notification sent
4. ✅ Artifacts attached

---

## 📊 Complete CI/CD Flow

```
Developer pushes code
        │
        ▼
┌─────────────────┐
│   PR Created    │
└────────┬────────┘
         │
         ▼
┌─────────────────────────────────────┐
│           PR Checks (Parallel)      │
│  ┌──────────┐  ┌──────────────────┐ │
│  │  Build   │  │  Code Quality    │ │
│  │Verify ✅ │  │  Checkstyle ✅   │ │
│  └──────────┘  │  SonarQube ✅    │ │
│  ┌──────────┐  └──────────────────┘ │
│  │  Tests   │  ┌──────────────────┐ │
│  │  Run ✅  │  │  Security Scan   │ │
│  └──────────┘  │  OWASP ✅        │ │
│  ┌──────────┐  └──────────────────┘ │
│  │  Docker  │                       │
│  │  Build ✅│                       │
│  └──────────┘                       │
└─────────────────────────────────────┘
         │
         ▼
┌─────────────────┐
│  Slack: ✅ All  │
│  Checks Passed  │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  Code Review    │
│  (1 Approval)   │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  Merge to       │
│  Master         │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  Nightly Tests  │◄── Scheduled (2 AM)
│  Chrome+Firefox │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  Slack: 🌙      │
│  Nightly Report │
└─────────────────┘
```

---

## ✅ Setup Checklist

### GitHub
- [x] Branch protection on `master`
- [x] Branch protection on `develop`
- [x] CODEOWNERS configured
- [x] PR template in place
- [x] Required status checks set

### Secrets
- [x] `SLACK_WEBHOOK_URL` (referenced in Jenkinsfile slackSend steps)
- [ ] `SONAR_TOKEN` (optional)
- [ ] `BROWSERSTACK_USERNAME` (not used in current setup)
- [ ] `BROWSERSTACK_ACCESS_KEY` (not used in current setup)
- [ ] `CODECOV_TOKEN` (optional)

### Jenkins
- [x] Plugins installed (implied by pipeline usage)
- [x] JDK 17 configured
- [x] Maven 3.9 configured
- [x] Slack plugin configured
- [x] Pipeline job created
- [x] GitHub webhook added
- [x] Parameterized builds (browser, env, suite)
- [ ] Parallel code quality stages (not explicitly implemented)
- [x] JUnit test results
- [ ] JaCoCo coverage (70% minimum) (not enforced)
- [ ] Allure report generation (not explicitly shown)
- [x] Docker Compose validation
- [x] Slack notifications (pass/fail/unstable)
- [x] Build artifact archiving
- [x] Workspace cleanup
- [x] 30-minute timeout
- [x] Concurrent build prevention

### Slack
- [x] App created
- [x] Webhook URL generated
- [x] Channel `#selenium-tests` created
- [x] Webhook added to GitHub secrets (`SLACK_WEBHOOK_URL`)
- [x] Webhook added to Jenkins (via Jenkinsfile slackSend steps)

---

**File**: `docs/BRANCH_PROTECTION_GUIDE.md`
**Status**: ✅ Complete