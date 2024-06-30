Feature: Create Task

    Scenario: Successfully create a new Task

        Given I have the following task details:
        | title         | startDate                  | endDate                    | status       | description   | estimation |
        | "Crear boton" | "2024-06-02T21:47:04.216Z" | "2024-06-29T21:47:04.216Z" | "Finalizado" | "nuevo boton" | "0"        |
        When I send a request to create a new task
        Then the response status should be 201
        And the task should be created with the above details

 