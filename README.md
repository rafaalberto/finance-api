# Finance API

- Project created to study how to implement Web Application with Clojure.

### Technologies & Libraries

- Compojure (Routing Library for Ring)
- Ring (Web Application for Clojure)
- Midje (Alternative library for tests in Clojure)
- Cloverage (Test Coverage)

### Commands

 - Running

   ` lein ring server`


 - Testing
   
   1. General
 
     ` lein midje`


   2. With Filter

      ` lein midje :filters acceptance`


   3. With Filter except...
   
      ` lein midje :filters -acceptance`

 - AutoTesting

    ` lein midje :autotest`


 - Coverage

    `lein cloverage --runner :midje`