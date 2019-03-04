Intro to Scala: Part Four

Welcome to the part four of the Scala training. This repository contains exercises related to such topics as:
  * pattern matching
  * asynchronous processing
  * generics
  * string interpolation
  * advanced Scala Programming.

The "master" branch contains exercises to be solved individually, as well as tests verifying correct implementations
of each of those and a "solutions" branch with reference implementations of those exercises.


 Exercise One

 Implement the "checkBalanceForUser" method in the "PersonalFinancesService" class, basing on the assumption that
 instances of "AccountRepository" and "TransactionRepository" are provided and working correctly, so that it:
 * the entire process should be asynchronous
 * if the account lookup by user ID fails, the entire process should fail with the same exception
 * if for any account, the lookup for its transactions fails, the entire process should fail with the same exception
 * if the account and transactions lookups all succeed, the method should find all accounts and check if their
 given balances and, by going through all incoming and outgoing transactions, check if the balance on the Account
 object is the same as the one calculated from those transactions
 * for each case, where it is not, the method should return a AccountBalanceError with appropriate fields filled
          
          NOTE: The Future.sequence method may be useful in turning a Collection[Future[X]] to a Future[Collection[X]]
          
 Run the PersonalFinancesServiceTest to verify the correctness of your implementation
          
        
 Exercise Two
    
 Implement the "processAsyncResponse" method in the "ResponseProcessor" object so that:
        
 * if the given Future[(Int, String)] evaluates successfully with "timeout", the method returns its very result
 * if the given Future fails with a ConflictException, the method returns a pair with status 422 and the message 
 from that ConflictException
 * if the given Future fails with a BadRequestException, the method returns a pair with status 400 and the message
 from that BadRequestException
 * if the given Future times out (timeout given as implicit), the method returns the status 503
 * in case of any other failure, the method returns the status 500 and the message: "Internal Server Error" 
 
 Run the "ResponseProcessorTest" to verify the correctness of your implementation
 
 Exercise Three
 
 Implement the "DivisibleBySeven" and "IndivisibleByThree" extractors according to their names. Use those to implement 
 the "apply" method in "Divisibility" object so that:
 
 * if i is divisible by 7, but indivisible by 3, the method returns 1
 * if i is divisible by 7 and 3 alike, the method returns 2
 * if i is indivisible by neither 3 nor 7, the method returns 3
 * otherwise, the method returns 4
 
 Run the "DivisiblityTest" to verify the correctness of your implementation
 
 
 Exercise Four
 
 Implement the "intEquality" variable in the Equality object so that:
 * i can only be equal to another Int
 * i and the other Int are only equal when their respective values are exactly equal
 
 Implement the "tolerantDoubleEquality" in the "Equality" object, so that:
 * a Double can only be equal to another Double
 * Doubles are equal when their numeric values differ by 0.01 or less
 
 Implement the "floatEquality" in the "Equality" object so that:
 * a Float can be equal to Float or a Double
 * a Float can be equal to Float / Double if their numeric values differ by 0.01 or less
 
 
 Exercise Five
 
 Implement the "apply" method in the "Retries" object so that:
 
 * if the given task succeeds, no retries are performed from that point on, no matter the retry strategy
 * if the given task fails and the retry strategy suggests retrying, the task is run once again
 * if the given task fails, but the retry strategy suggests not retrying, the task in not retried again and a Failure is
 returned
 
 Implement the "complexRetryPolicy" in the "RetryPolicy" object so that:
 
 * in any case, the "maxRetries" parameter signifies the most times any task can be retried
 * retries should take place only when the cause for failure is a runtime exception