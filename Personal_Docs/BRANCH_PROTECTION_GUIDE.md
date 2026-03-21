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
       - build-and-test
       - code-quality
       - security-scan
       - docker-build

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
   - build-and-test

✅ Allow force pushes (for rebasing)
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
Settings → Secrets → Actions → New secret

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
Settings → Secrets → Actions

DOCKER_USERNAME: your-dockerhub-username
DOCKER_PASSWORD: your-dockerhub-password
```

### Docker Compose Validation in CI
The `docker-build` job in `pr-checks.yml` automatically:
- Validates `docker-compose.yml` syntax
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
- [ ] Branch protection on `master`
- [ ] Branch protection on `develop`
- [ ] CODEOWNERS configured
- [ ] PR template in place
- [ ] Required status checks set

### Secrets
- [ ] `SLACK_WEBHOOK_URL`
- [ ] `SONAR_TOKEN` (optional)
- [ ] `BROWSERSTACK_USERNAME`
- [ ] `BROWSERSTACK_ACCESS_KEY`
- [ ] `CODECOV_TOKEN` (optional)

### Jenkins
- [ ] Plugins installed
- [ ] JDK 17 configured
- [ ] Maven 3.9 configured
- [ ] Slack plugin configured
- [ ] Pipeline job created
- [ ] GitHub webhook added

### Slack
- [ ] App created
- [ ] Webhook URL generated
- [ ] Channel `#selenium-tests` created
- [ ] Webhook added to GitHub secrets
- [ ] Webhook added to Jenkins

---

**File**: `Personal_Docs/BRANCH_PROTECTION_GUIDE.md`
**Status**: ✅ Complete
