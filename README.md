# Mobile Test Automation Framework

A comprehensive mobile test automation framework that supports both Android and iOS platforms using Appium.

## Features

- **Cross-Platform Support**: Run tests on both Android and iOS devices
- **Device Management**: Support for emulators/simulators and real devices
- **Test Types**: Smoke, regression, and end-to-end testing
- **Page Object Model**: Well-structured test code using the Page Object pattern
- **Reporting**: Rich Allure reports with screenshots and logs
- **CI/CD Integration**: GitHub Actions workflow for continuous integration
- **Parallel Execution**: Run tests in parallel on different devices

## Prerequisites

- Java JDK 11 or higher
- Maven 3.6 or higher
- Appium 2.0 or higher
- Android SDK (for Android testing)
- Xcode (for iOS testing)
- Node.js and npm

## Setup Instructions

### 1. Install Dependencies

#### Java and Maven
```bash
# Check Java version
java -version

# Check Maven version
mvn -version
```

#### Appium
```bash
# Install Appium
npm install -g appium

# Install Appium drivers
appium driver install uiautomator2
appium driver install xcuitest
```

#### Android SDK
- Download and install Android Studio
- Set ANDROID_HOME environment variable
- Add platform-tools to PATH

#### Xcode (for macOS only)
- Install Xcode from the App Store
- Install Xcode Command Line Tools

### 2. Clone the Repository
```bash
git clone https://github.com/yourusername/mobile-test-framework.git
cd mobile-test-framework
```

### 3. Configure the Framework

Update the configuration in `src/main/resources/config/config.properties`:

```properties
# Android Configuration
android.deviceName=Your Android Device Name
android.platformVersion=Your Android Version
android.appPackage=Your App Package
android.appActivity=Your App Activity

# iOS Configuration
ios.deviceName=Your iOS Device Name
ios.platformVersion=Your iOS Version
ios.bundleId=Your App Bundle ID
```

## Running Tests

### Start Appium Server
```bash
appium -p 4723
```

### Run Tests with Maven
```bash
# Run all tests
mvn clean test

# Run specific test suite
mvn clean test -DsuiteXmlFile=src/test/resources/testng.xml

# Run only smoke tests
mvn clean test -Dgroups=smoke
```

### Generate and View Allure Reports

#### Generate Report
```bash
mvn allure:report
```

The report will be generated in `target/site/allure-maven-plugin/index.html`

#### Serve Report (Interactive Mode)
```bash
mvn allure:serve
```
This will generate the report and automatically open it in your default browser.

#### Allure Report Features
- **Dashboard**: Overview of test execution with statistics
- **Suites**: Detailed test suite information
- **Graphs**: Visual representation of test results
- **Timeline**: Chronological view of test execution
- **Categories**: Tests grouped by failure categories
- **Attachments**: Screenshots, logs, and other test artifacts

#### Allure Configuration
The framework includes the following Allure configuration:
- **allure.properties**: Configures report directories and link patterns
- **categories.json**: Defines custom categories for test failures
- **TestNG Listener**: Automatically captures test execution data

## Project Structure

```
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── mobiletest
│   │   │           └── framework
│   │   │               ├── config
│   │   │               │   └── ConfigReader.java
│   │   │               ├── core
│   │   │               │   ├── BaseTest.java
│   │   │               │   └── DeviceManager.java
│   │   │               ├── pages
│   │   │               │   ├── BasePage.java
│   │   │               │   └── LoginPage.java
│   │   │               └── utils
│   │   │                   └── TestUtils.java
│   │   └── resources
│   │       └── config
│   │           └── config.properties
│   └── test
│       ├── java
│       │   └── com
│       │       └── mobiletest
│       │           └── tests
│       │               └── LoginTest.java
│       └── resources
│           └── testng.xml
├── .github
│   └── workflows
│       └── mobile-tests.yml
├── pom.xml
└── README.md
```

## Adding New Tests

1. Create a new page object class in `src/main/java/com/mobiletest/framework/pages`
2. Create a new test class in `src/test/java/com/mobiletest/tests`
3. Add the test class to the TestNG XML file

Example:
```java
@Test(groups = {"regression"})
@Description("Test description")
public void testExample() {
    // Test code here
}
```

## Best Practices

- Use the Page Object Model pattern
- Group tests appropriately (smoke, regression, etc.)
- Take screenshots on test failures
- Log important steps and information
- Keep tests independent of each other

### Using Allure Annotations

Enhance your test reports with Allure annotations:

```java
@Test
@Description("Detailed test description")
@Feature("Feature name")
@Story("User story reference")
@Severity(SeverityLevel.CRITICAL)
@Issue("JIRA-123")
@TmsLink("TMS-456")
@Link(name = "Documentation", url = "https://example.com/docs")
public void testExample() {
    // Test steps
}
```

#### Common Annotations:
- **@Description**: Detailed test description
- **@Feature**: High-level feature being tested
- **@Story**: User story or requirement being verified
- **@Severity**: Test importance (BLOCKER, CRITICAL, NORMAL, MINOR, TRIVIAL)
- **@Issue**: Link to issue tracker
- **@TmsLink**: Link to test management system
- **@Link**: Custom link to external resources

#### Adding Steps and Attachments:
```java
@Step("Login with username: {0} and password: {1}")
public void login(String username, String password) {
    // Login implementation
}

@Attachment(value = "Screenshot", type = "image/png")
public byte[] takeScreenshot() {
    // Screenshot implementation
    return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
}
```

## Troubleshooting

### General Issues
- **Appium Connection Issues**: Ensure Appium server is running and the correct port is configured
- **Device Not Found**: Check device/emulator is running and properly configured
- **Element Not Found**: Verify locators and add appropriate waits
- **Test Failures**: Check screenshots and logs for details

### Allure Report Issues
- **Missing Reports**: Ensure tests are running with the Allure listener enabled in testng.xml
- **No Screenshots in Report**: Verify that screenshots are being taken and attached using @Attachment
- **Empty Categories**: Check that categories.json is properly configured
- **Report Not Opening**: Try using `mvn allure:serve` instead of opening the HTML file directly
- **Missing Test Steps**: Ensure methods are annotated with @Step
- **Broken Links**: Update link patterns in allure.properties to match your issue tracking system

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a new Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.
