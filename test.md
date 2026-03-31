@Given("the user is on the login page")
public void userIsOnLoginPage() {
driver.get("https://myapp.com/login");  // real Selenium/Playwright code here
}
```

Cucumber finds this method, runs it, and moves to the next step. **The Gherkin is documentation AND a test script at the same time.**

---

## Is Cucumber a Dedicated/Standalone Framework?

Yes and no — and this is an important nuance. Cucumber handles **one specific layer**: the BDD layer (parsing Gherkin, matching steps, reporting). It does **not** handle things like browser automation or assertions on its own.

In practice, Cucumber is almost always used **alongside other tools**:

In a typical Java project, Cucumber sits on top of JUnit or TestNG (as the test runner) and works alongside Selenium or Playwright (for browser automation). Cucumber is the orchestrator that reads your `.feature` files and calls your step definitions, but the actual "click this button" work is done by Selenium.

A typical Java stack looks like: **Cucumber + JUnit5 + Selenium/Playwright + Maven/Gradle**.

---

## Do You Need a Separate Project?

This is a great practical question. You have two real options:

**Option 1: Separate dedicated test project (most common for large teams).** You create a new Maven/Gradle project that contains only your `.feature` files, step definitions, and test config. It depends on your main app as an external dependency (or just hits its deployed URL). This keeps test code cleanly separated from production code, which is the industry standard for enterprise projects.

**Option 2: Submodule within the same project.** In smaller projects or monorepos, teams sometimes add a `src/test` folder with Cucumber tests inside the same project. This is simpler but can get messy as the project grows.

The project structure of a typical standalone Cucumber project looks like this:
```
my-cucumber-tests/
├── src/
│   ├── test/
│   │   ├── java/
│   │   │   ├── steps/          ← your Step Definition classes
│   │   │   ├── hooks/          ← Before/After hooks (setup/teardown)
│   │   │   └── runners/        ← JUnit runner that triggers Cucumber
│   │   └── resources/
│   │       └── features/       ← your .feature (Gherkin) files
└── pom.xml                     ← Maven dependencies