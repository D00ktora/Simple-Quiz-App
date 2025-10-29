# Simple-Quiz-App
## This app is made to cover exam criteria of Sirma Academy in module Spring MVC

---

## How to Start:
### Make sure you have installed:
- **Java 23**
- **Maven**
---
## Run the Project:
- ###  Clone the repository
- ### Build the project
- ### Set up your editor to use annotations
- ### Run the application

---

## Additional information:
- ### In resource folder you can find a file from which you can load quizzes in to the app.
- #### Go to end-point : **http://localhost:8080/**
- #### From navigation bar you can click on add button, to Add Quiz a external .json file.
- #### Pick a file from the resources with name "impleImput.json".
- #### Click Upload Quizzes.
- #### It will automatically redirect you on home page, where you will find all quizzes that are loaded from the .json file.

---

## End-Points:
- #### http://localhost:8080/ - Home page.
- #### http://localhost:8080/create/json - Create Quizzes from Json file.
- #### http://localhost:8080/quiz/{id} - (Before using this end point upload quizzes) Navigate to quiz with inputted id.
- #### http://localhost:8080/quiz/result - Result page. That is available when you finish the quiz.
- #### http://localhost:8080/?lang=bg - Pick bulgarian language.
- #### http://localhost:8080/?lang=en - Pick english language.

---

## Additional from the author:
- I`m not good at front end and this take all of my time.
- Implementation of tests is must in later stage. 
- Exceptions do not have handlers. They need to be implemented. 
- Repositories are local, they need to be from real database.
- There is no validation of the data. Its need to be added. 
- Quiz Service is massive, its need to be changed to different services. 
- If the app is made with database, it will be easier :).
