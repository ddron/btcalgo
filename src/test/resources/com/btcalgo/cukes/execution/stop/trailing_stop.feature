@trailing_stop
Feature: Trailing Stop Order

  Scenario: Trailing Stop Order

    Given btcalgo engine
    Given has valid keys

    Then put new buy "Trailing Stop" order "2.5@10" of "BTCUSD" with stopPrice = "9" offset="1.5"

    Then md for "BTCUSD" is buy="8.8", sell="7.0"
    Then new order stopPrice="9", limitPrice="10"
    Then nothing sent to market

    Then md for "BTCUSD" is buy="7.4", sell="7.0"
    Then new order stopPrice="8.9", limitPrice="9.9"
    Then nothing sent to market

    Then md for "BTCUSD" is buy="6.2", sell="6.0"
    Then new order stopPrice="7.7", limitPrice="8.7"
    Then nothing sent to market

    Then md for "BTCUSD" is buy="6.9", sell="6.0"
    Then new order stopPrice="7.7", limitPrice="8.7"
    Then nothing sent to market

    Then md for "BTCUSD" is buy="7.8", sell="6.0"
    Then new order stopPrice="7.7", limitPrice="8.7"
    Then buy order "2.5@8.7" of "BTCUSD" sent to market
