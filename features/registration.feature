Feature: Account registration feature

  Scenario: Account registration
  	Given there is an account with empty id
  	When the account is being registered
  	Then the "InitialTokensRequested" event is sent
  	When the "InitialTokensAssigned" event is sent with a list of 6 tokens
  	Then the account is registered and his id is set and has 6 tokens


