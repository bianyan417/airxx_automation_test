@booking @ui
Feature: Flight Booking
  As a customer
  I want to search and book flights on XXX Air Company
  So that I can travel to my desired destination

  Background:
    Given I am on the XXX Air Company booking page

  @smoke @domestic
  Scenario: Search for domestic one-way flight
    Given I have selected a one-way trip
    When I search for flights from "Auckland" to "Wellington"
    And I select departure date "15 Jun 2026"
    And I click search flights
    Then I should see available flights
    And I should see fare options

  @smoke @domestic
  Scenario: Search for domestic return flight
    Given I have selected a return trip
    When I search for flights from "Auckland" to "Christchurch"
    And I select departure date "20 Jun 2026"
    And I select return date "25 Jun 2026"
    And I click search flights
    Then I should see available flights
    And I should see at least 1 flight

  @regression @international
  Scenario: Search for international flight
    Given I have selected a return trip
    When I search for flights with the following details:
      | origin   | destination | departureDate | returnDate  | adults |
      | Auckland | Sydney      | 01 Jul 2026   | 10 Jul 2026 | 2      |
    And I click search flights
    Then I should see available flights
    And I should see flight prices

  @regression @multipassenger
  Scenario: Book flight for multiple passengers
    Given I have selected a return trip
    When I search for flights from "Auckland" to "Wellington"
    And I select departure date "15 Jul 2026"
    And I select return date "20 Jul 2026"
    And I select 2 adults
    And I select 1 children
    And I click search flights
    Then I should see available flights

  @regression @fare-selection
  Scenario Outline: Select different fare types
    Given I have selected a one-way trip
    When I search for a domestic flight
    And I select the first available flight
    And I select the "<fareType>" fare
    Then I should see the total price

    Examples:
      | fareType      |
      | Seat          |
      | Seat + Bag    |
      | The Works     |

  @regression @cabin-class
  Scenario Outline: Filter flights by cabin class
    Given I have selected a one-way trip
    When I search for an international flight
    And I filter by "<cabinClass>" cabin class
    Then I should see available flights

    Examples:
      | cabinClass       |
      | Economy          |
      | Premium Economy  |
      | Business         |

  @regression @sorting
  Scenario: Sort flights by price
    Given I have selected a one-way trip
    When I search for a domestic flight
    And I sort flights by price
    Then I should see available flights
    And flights should be sorted by price ascending

  @regression @sorting
  Scenario: Sort flights by duration
    Given I have selected a one-way trip
    When I search for a domestic flight
    And I sort flights by duration
    Then I should see available flights

  @regression @filter
  Scenario: Filter non-stop flights only
    Given I have selected a one-way trip
    When I search for a domestic flight
    And I filter to show non-stop flights only
    Then I should see available flights

  @smoke @booking-flow
  Scenario: Complete flight booking flow
    Given I have selected a one-way trip
    When I search for a domestic flight
    And I select the first available flight
    And I select the "Seat + Bag" fare
    And I continue to passenger details
    Then I should be on the passenger details page

  @regression @passenger-details
  Scenario: Enter passenger details
    Given I have selected a one-way trip
    When I search for a domestic flight
    And I select the first available flight
    And I select the "Seat" fare
    And I continue to passenger details
    And I enter passenger details:
      | title | firstName | lastName | email              | phone      |
      | Mr    | John      | Smith    | john@example.com   | 0212345678 |
    Then I should see the passenger summary

  @regression @seat-selection
  Scenario: Select seat during booking
    Given I have selected a one-way trip
    When I search for a domestic flight
    And I select the first available flight
    And I select the "The Works" fare
    And I continue to passenger details
    And I enter passenger details:
      | title | firstName | lastName | email              | phone      |
      | Mrs   | Jane      | Doe      | jane@example.com   | 0219876543 |
    And I select seat "12A"
    Then I should see the flight summary

  @regression @extras
  Scenario: Add extra baggage to booking
    Given I have selected a one-way trip
    When I search for a domestic flight
    And I select the first available flight
    And I select the "Seat" fare
    And I continue to passenger details
    And I enter passenger details:
      | title | firstName | lastName | email              | phone      |
      | Ms    | Sarah     | Wilson   | sarah@example.com  | 0218765432 |
    And I add extra baggage
    Then the total amount should be displayed

  @regression @validation
  Scenario: Validate required passenger fields
    Given I have selected a one-way trip
    When I search for a domestic flight
    And I select the first available flight
    And I select the "Seat" fare
    And I continue to passenger details
    And I continue to payment
    Then I should see validation errors

  @regression @promo-code
  Scenario: Apply promo code to booking
    Given I have selected a return trip
    When I search for flights from "Auckland" to "Wellington"
    And I select departure date "15 Aug 2026"
    And I select return date "20 Aug 2026"
    And I enter promo code "AIRPOINTS10"
    And I click search flights
    Then the booking page should load

  @regression @manage-booking
  Scenario: Retrieve existing booking
    When I retrieve booking with reference "ABC123" and last name "Smith"
    Then I should see the booking details

  @negative @no-flights
  Scenario: No flights available for invalid route
    Given I have selected a one-way trip
    When I search for flights from "Auckland" to "Auckland"
    And I select departure date "15 Jun 2026"
    And I click search flights
    Then I should see no flights available message
