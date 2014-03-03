@stop
Feature: Simple Stop Order

  Scenario: Simple stop order

    Given btcalgo engine
    Given has valid keys


    Then md for "BTCUSD" is buy="600.25", sell="590.40"
    Then put new buy "StopLoss" order "2.1@500.25" of "BTCUSD" with stopPrice = "550.50"