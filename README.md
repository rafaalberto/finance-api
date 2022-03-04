# Finance API

- Project created to study how to implement Web Application with Clojure.

### Technologies & Libraries

- Compojure (Routing Library for Ring)
- Ring (Web Application Library for Clojure)
- Midje (Alternative library for tests in Clojure)
- Cloverage (Test Coverage)
- Ring JSON (Handle JSON requests and responses)

### Commands

 - Create project

    `lein new compojure finance-api`


 - Running (Default port 3000)

   ` lein ring server`

 - Running (Alternative port)

  ` lein ring server 8080`

 - Running without opening a Web Browser

  ` lein ring server-headless`

 - Testing
   
   1. Run all tests
 
     ` lein midje`


   2. Run tests applying Filter

      ` lein midje :filters acceptance`


   3. Run tests except...
   
      ` lein midje :filters -acceptance`

 - AutoTesting

    ` lein midje :autotest`


 - Coverage

    `lein cloverage --runner :midje`