Feature: Create Project

    Scenario: Successfully create a new Project

        Given I have the following project details:
        | title | startDate                  | endDate                  | status       | description               |
        | "TPG" | "2024-06-02T21:47:04.216Z" | 2024-06-29T21:47:04.216Z | "Finalizado" | "Trabajo Práctico Grupal" |
        When I send a request to create a new project
        Then the response status should be 201
        And the project should be created with the above details