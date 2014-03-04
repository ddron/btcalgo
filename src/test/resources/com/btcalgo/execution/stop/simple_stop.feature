@stop
Feature: Simple Stop Order

  Scenario: Simple stop order

    Given btcalgo engine
    Given has valid keys

    Then md for "BTCUSD" is buy="600.25", sell="590.40"
    Then put new buy "Stop Loss" order "2.1@620.25" of "BTCUSD" with stopPrice = "610.50"

    Then md for "BTCUSD" is buy="615.25", sell="608.40"
    Then buy order "2.1@620.25" of "BTCUSD" sent to market