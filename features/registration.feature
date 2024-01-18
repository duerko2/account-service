Feature: Account registration feature

  Scenario: Account registration
  	When the account is being registered
  	Then the tokensRequestedEvent event is sent
  	When the TokensAssigned event is sent with a list of 6 tokens
  	Then the account is registered and his id is set and has 6 tokens


