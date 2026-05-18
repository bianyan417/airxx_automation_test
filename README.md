# XXX Air Company — Automation Test Framework

A professional, scalable, and maintainable test automation framework for the XXX Air Company web application.  
Supports **UI** (Selenium + TestNG), **API** (RestAssured), and **BDD** (Cucumber 7) testing in one unified Maven project.

## 📋 Table of Contents

- [Features](#features)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Project Structure](#project-structure)
- [Configuration](#configuration)
- [Running Tests](#running-tests)
- [Reporting](#reporting)
- [Best Practices](#best-practices)
- [Contributing](#contributing)

## ✨ Features

- **Multi-Browser Support**: Chrome, Firefox, Edge, Safari — configured via `selenium.properties`
- **Page Object Model (POM)**: `BasePage` → `HomePage` / `BookingPage` / `TicketPage` / `LoginPage`
- **API Testing**: RestAssured-backed `ApiClient`; responses deserialised to typed models (`BookingResponse`, `TicketTestData`) with `SoftAssert` attribute verification
- **Extensible Test Data Model**: Abstract `TestData` with CRTP generic builder; `TestData.generateTestData(TestDataType, Map)` factory; `TicketTestData` as the first concrete type
- **BDD Support**: Cucumber 7 + TestNG; `LoginSteps`, `BookingSteps`, `Hooks`; tags `@smoke`, `@regression`, `@ui`, `@api`, `@security`
- **Parallel Execution**: TestNG `parallel="classes"` + Cucumber `@DataProvider(parallel=true)`
- **Cross-Environment**: `qa.properties` drives base URL, credentials, feature flags; override with `-Denv=staging`
- **Comprehensive Reporting**: Allure (steps, attachments, history) + Cucumber HTML/JSON/JUnit + rerun support
- **Screenshot Capture**: Auto-capture on TestNG failure (`TestListener`) and Cucumber step failure (`Hooks`); attached as Base64 to Allure reports
- **Structured Logging**: Log4j2 with separate appenders for API requests, test execution, and errors
- **Data-Driven Testing**: JSON test data (`BookingData.json`, `TicketData.json`, `loginData.json`) loaded via `TestDataLoader`
- **CI/CD Ready**: Maven profiles (`smoke`, `regression`, `api`, `ui`, `bdd`, `headless`) for pipeline usage

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                        Test Layer                                │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────────┐  │
│  │   UI Tests  │  │  API Tests  │  │  BDD Tests (Cucumber)   │  │
│  └─────────────┘  └─────────────┘  └─────────────────────────┘  │
├─────────────────────────────────────────────────────────────────┤
│                      Business Layer                              │
│  ┌─────────────────┐  ┌─────────────────┐  ┌────────────────┐   │
│  │   Page Objects  │  │   API Services  │  │ Step Definitions│  │
│  └─────────────────┘  └─────────────────┘  └────────────────┘   │
├─────────────────────────────────────────────────────────────────┤
│                       Core Framework                             │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌────────┐│
│  │ Config   │ │ Factory  │ │  Utils   │ │Listeners │ │Constants││
│  │ Manager  │ │(Browser) │ │ (Wait,   │ │          │ │         ││
│  │          │ │          │ │Screenshot│ │          │ │         ││
│  └──────────┘ └──────────┘ └──────────┘ └──────────┘ └────────┘│
├─────────────────────────────────────────────────────────────────┤
│           Infrastructure (Selenium, RestAssured, TestNG)         │
└─────────────────────────────────────────────────────────────────┘
```

## 📦 Prerequisites

- **Java JDK 17** or higher
- **Maven 3.8+**
- **Chrome/Firefox/Edge** browser installed
- **Git** for version control
- **IDE**: Eclipse, IntelliJ IDEA, or VS Code

## 🚀 Installation

### 1. Clone the Repository

```bash
git clone https://github.com/bianyan417/airxx_automation_test.git
cd airxx_automation_test
```

### 2. Install Dependencies

```bash
mvn clean install -DskipTests
```

### 3. Verify Installation

```bash
mvn test -Dtest=LoginTest#testLoginPageLoads
```

## 📁 Project Structure

```
airnz_automation_test/
│
├── src/
│   └── com/
│       └── airxx/
│           │
│           ├── core/                          # Framework core components
│           │   ├── config/                    # Driver & config management
│           │   │   ├── ConfigManager.java     # Loads qa.properties / selenium.properties;
│           │   │   │                          #   resolves env / system-property overrides
│           │   │   └── DriverManager.java     # Thread-local WebDriver lifecycle
│           │   │                              #   (initDriver / getDriver / quitDriver)
│           │   ├── factory/                   # Browser instantiation
│           │   │   └── BrowserFactory.java    # Creates Chrome / Firefox / Edge / Safari;
│           │   │                              #   supports local, remote & headless modes
│           │   ├── constants/                 # Shared magic-value-free constants
│           │   │   └── FrameworkConstants.java
│           │   ├── listeners/                 # TestNG event hooks
│           │   │   └── TestListener.java      # Logs pass / fail / skip; auto-captures
│           │   │                              #   screenshot on failure via ScreenshotUtils
│           │   └── utils/                     # Reusable helpers
│           │       ├── WaitUtils.java         # FluentWait / explicit waits wrappers
│           │       ├── ScreenshotUtils.java   # Saves PNG; returns byte[] for Allure/Cucumber
│           │       ├── JsonUtils.java         # Gson-backed read / write / merge helpers
│           │       └── TestDataLoader.java    # Loads BookingData / TicketData / loginData
│           │                                  #   JSON files into Map<String, String>
│           │
│           ├── pages/                         # Page Object Model classes
│           │   ├── BasePage.java              # Abstract base: wait helpers, click, type,
│           │   │                              #   JS executor, PageFactory init
│           │   ├── HomePage.java              # Flight search widget, navigation, login CTA
│           │   ├── BookingPage.java           # Flight results, fare selection, filters/sort
│           │   ├── TicketPage.java            # Passenger form, seat map, extras, totals
│           │   └── LoginPage.java             # Email/password form, LoyaltyPoints login,
│           │                                  #   forgot-password, register, social login
│           │
│           ├── api/                           # API testing layer
│           │   ├── client/
│           │   │   └── ApiClient.java         # RestAssured wrapper: GET/POST/PUT/PATCH/DELETE;
│           │   │                              #   auth-token header, response-time util,
│           │   │                              #   getBodyAs() deserialiser
│           │   ├── models/
│           │   │   ├── TestData.java          # Abstract base for all test data models.
│           │   │   │                          #   • CRTP generic Builder<T,B>
│           │   │   │                          #   • TestDataType enum (TICKET, …)
│           │   │   │                          #   • generateTestData(type, params) factory
│           │   │   │                          #   • validate() / describe() contract
│           │   │   │                          #   • toJson() / copy() utilities
│           │   │   ├── TicketTestData.java    # Concrete TestData for flight tickets.
│           │   │   │                          #   • All ticket fields + getters/setters
│           │   │   │                          #   • Inner Builder extends TestData.Builder
│           │   │   │                          #   • validate() checks 7 required fields
│           │   │   └── BookingResponse.java   # API response model: bookingReference,
│           │   │                              #   status, List<FlightSegment>, List<Passenger>,
│           │   │                              #   Pricing, List<TicketTestData>, errors;
│           │   │                              #   rich validation helpers (isConfirmed(),
│           │   │                              #   hasValidTotalAmount(), etc.)
│           │   └── services/
│           │       └── BookingService.java    # High-level booking API calls:
│           │                                  #   searchFlights, createBooking, getBooking,
│           │                                  #   updateBooking, addPassenger, selectSeat,
│           │                                  #   checkIn, cancelBooking, getBookingDetails
│           │
│           ├── tests/                         # Test classes
│           │   ├── ui/
│           │   │   ├── BookingTest.java       # 14 UI tests: search, fare selection,
│           │   │   │                          #   multi-passenger, seat, validation, promo
│           │   │   └── LoginTest.java         # 20 UI tests: valid/invalid login, register,
│           │   │                              #   LoyaltyPoints, logout, XSS/SQLi prevention
│           │   ├── api/
│           │   │   ├── BookingAPITest.java    # 21 API tests: deserialises responses to
│           │   │   │                          #   BookingResponse / TicketTestData; uses
│           │   │   │                          #   SoftAssert for attribute-level verification
│           │   │   └── LoginApiTest.java      # 21 API tests: auth endpoints, token flow,
│           │   │                              #   rate-limiting, security input validation
│           │   └── bdd/
│           │       ├── stepdefinitions/
│           │       │   ├── LoginSteps.java    # Gherkin → LoginPage method mappings
│           │       │   ├── BookingSteps.java  # Gherkin → HomePage / BookingPage / TicketPage
│           │       │   └── Hooks.java         # @Before / @After / @AfterStep: driver init,
│           │       │                          #   screenshot attach, tag-based setup (@ui/@api)
│           │       ├── runners/
│           │       │   └── TestRunner.java    # CucumberOptions: features, glue, Allure plugin,
│           │       │                          #   rerun.txt; parallel DataProvider
│           │       └── features/
│           │           ├── Booking.feature    # 17 scenarios: domestic/intl/return search,
│           │           │                      #   fare types, cabin class, sorting, seat,
│           │           │                      #   extras, validation, promo, manage-booking
│           │           └── login.feature      # 16 scenarios: valid login, invalid creds,
│           │                                  #   register, LoyaltyPoints, modal, security tags
│           │
│           └── resources/                     # Static test resources
│               ├── config/
│               │   ├── qa.properties          # Base URL, API URL, credentials, timeouts,
│               │   │                          #   retry settings, feature flags
│               │   └── selenium.properties    # Browser, headless, window size, waits,
│               │                              #   download dir, Grid / BrowserStack / LambdaTest
│               ├── testdata/
│               │   ├── BookingData.json       # 12 booking scenarios (domestic, intl, return,
│               │   │                          #   multi-pax, business, promo, API, check-in…)
│               │   ├── TicketData.json        # 8 passenger profiles (adult, child, infant,
│               │   │                          #   international, business, special-assistance,
│               │   │                          #   frequent-flyer, unaccompanied minor)
│               │   └── loginData.json         # 13 login scenarios (valid, invalid, empty,
│               │                              #   LoyaltyPoints, registration, SQLi, XSS…)
│               └── log4j2.xml                 # Console + rolling-file appenders; separate
│                                              #   logs for api-requests, test-execution, errors
│
├── reports/                                   # Generated test reports (HTML, JSON, Allure)
├── logs/                                      # Runtime log files
├── pom.xml                                    # Maven: all dependencies + profile definitions
├── testng.xml                                 # TestNG suite: parallel class execution
├── .gitignore                                 # Ignores target/, logs/, reports/, secrets
└── README.md                                  # This file
```

## ⚙️ Configuration

### Environment Configuration

Edit `src/com/airnz/resources/config/qa.properties`:

```properties
base.url=https://www.xxxaircompany.com
api.base.url=https://api.xxxaircompany.com
```

### Selenium Configuration

Edit `src/com/airnz/resources/config/selenium.properties`:

```properties
browser=chrome
headless=false
implicit.wait=10
explicit.wait=15
```

### Test Data

Test data is stored in JSON files under `src/com/airnz/resources/testdata/`:

- `loginData.json`   — 13 login scenarios (valid, invalid, empty, LoyaltyPoints, registration, SQLi, XSS)
- `BookingData.json` — 12 booking scenarios (domestic, international, return, multi-pax, business class, promo, API booking, check-in)
- `TicketData.json`  — 8 passenger profiles (adult, child, infant, international, business, special-assistance, frequent-flyer, unaccompanied minor)

Loaded at runtime via `TestDataLoader.getLoginData(scenario)`, `TestDataLoader.getBookingData(scenario)`, and `TestDataLoader.getTicketData(scenario)`.  
Fields map directly to `SerializedName` keys used by `TestData.generateTestData()` and the concrete builders.

## 🏃 Running Tests

### Run All Tests

```bash
mvn clean test
```

### Run Specific Test Class

```bash
mvn test -Dtest=LoginTest
```

### Run Specific Test Method

```bash
mvn test -Dtest=LoginTest#testValidLogin
```

### Run Tests by Profile

```bash
# Smoke tests
mvn test -Psmoke

# Regression tests
mvn test -Pregression

# API tests only
mvn test -Papi

# UI tests only
mvn test -Pui

# BDD/Cucumber tests
mvn test -Pbdd
```

### Run Tests with Parameters

```bash
# Different browser
mvn test -Dbrowser=firefox

# Headless mode
mvn test -Dheadless=true

# Different environment
mvn test -Denv=staging

# Combined parameters
mvn test -Dbrowser=chrome -Dheadless=true -Denv=qa
```

### Run Cucumber Tests with Tags

```bash
mvn test -Pbdd -Dcucumber.filter.tags="@smoke"
mvn test -Pbdd -Dcucumber.filter.tags="@regression"
mvn test -Pbdd -Dcucumber.filter.tags="@booking and not @wip"
```

## 📊 Reporting

### Allure Reports

Generate Allure report:

```bash
mvn allure:serve
```

Or generate HTML report:

```bash
mvn allure:report
```

Reports are generated in `target/site/allure-maven-plugin/`

### Cucumber Reports

Cucumber reports are generated in:
- HTML: `reports/cucumber-reports/cucumber-html-report.html`
- JSON: `reports/cucumber-reports/cucumber.json`

### Screenshots

Failed test screenshots are saved in `reports/screenshots/`

## 🎯 Best Practices

### Page Object Model

```java
public class LoginPage extends BasePage {
    @FindBy(css = "[data-testid='email-input']")
    private WebElement emailInput;
    
    public LoginPage enterEmail(String email) {
        type(emailInput, email);
        return this;  // fluent return
    }
}
```

### Fluent Interface Pattern

```java
homePage
    .selectReturnTrip()
    .enterOrigin("Auckland")
    .enterDestination("Wellington")
    .enterDepartureDate("15 Jun 2026")
    .searchFlights();
```

### Data-Driven Testing with `@DataProvider`

```java
@DataProvider(name = "loginData")
public Object[][] loginDataProvider() {
    return new Object[][] {
        {"validLogin", true},
        {"invalidPassword", false}
    };
}
```

### Test Data — Direct Builder

Use `TicketTestData.builder()` when all fields are known at compile time:

```java
TicketTestData ticket = TicketTestData.builder()
    .ticketNumber("086-1234567890")
    .bookingReference("ABC123")
    .passengerTitle("Mr")
    .passengerFirstName("John")
    .passengerLastName("Smith")
    .flightNumber("NZ101")
    .origin("AKL")
    .destination("WLG")
    .cabinClass("Economy")
    .price(199.00)
    .currency("NZD")
    .buildAndValidate();  // throws if required fields missing
```

### Test Data — Factory Method (runtime / JSON-driven)

Use `TestData.generateTestData()` when the type or values come from a JSON file:

```java
// Load scenario from TicketData.json
Map<String, String> params = TestDataLoader.getTicketData("adultPassenger");
TicketTestData ticket = TestData.generateTestData(TestData.TestDataType.TICKET, params);
```

### Adding a New Test Data Type

1. Create `PassengerTestData extends TestData` with its own inner `Builder`
2. Add `PASSENGER` to `TestData.TestDataType`
3. Add one line to the static registry in `TestData`:
```java
registry.put(TestDataType.PASSENGER, TestData::buildPassengerTestData);
```

### API Response Assertion with `SoftAssert`

Deserialise the raw `Response` to a typed model then verify every attribute in one block:

```java
BookingResponse booking = ApiClient.getBodyAs(response, BookingResponse.class);

SoftAssert softAssert = new SoftAssert();
softAssert.assertNotNull(booking, "BookingResponse should not be null");
softAssert.assertEquals(booking.getBookingReference(), expectedRef, "Ref should match");
softAssert.assertTrue(booking.hasValidTotalAmount(), "Total should be > 0");
softAssert.assertFalse(booking.hasErrors(), "No errors expected");
softAssert.assertAll();  // reports all failures together
```

### BDD Scenarios

```gherkin
@smoke @booking-flow
Scenario: Complete flight booking flow
  Given I am on the XXX Air Company booking page
  And I have selected a one-way trip
  When I search for a domestic flight
  And I select the first available flight
  And I select the "Seat + Bag" fare
  And I continue to passenger details
  Then I should be on the passenger details page
```

## 🔧 IDE Setup

### Eclipse

1. Import as Maven Project: `File > Import > Maven > Existing Maven Projects`
2. Select the project root directory
3. Install TestNG plugin from Eclipse Marketplace
4. Install Cucumber plugin for feature file support

### IntelliJ IDEA

1. Open project: `File > Open > Select pom.xml`
2. Enable auto-import for Maven
3. Install Cucumber and TestNG plugins

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 Code Standards

- Follow Java naming conventions
- Add Javadoc comments for all public methods
- Write meaningful test descriptions
- Keep methods focused and small
- Use descriptive variable names

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📞 Support

For support, email automation-team@xxxaircompany.com or create an issue in this repository.

---

**Built with ❤️ by the XAC Automation Team**
