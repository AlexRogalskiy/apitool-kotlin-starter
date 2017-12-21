package starter.apitool

import io.restassured.RestAssured.given
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jooby.Status
import org.junit.Assert.assertEquals
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
object AppTest : Spek({
    jooby(App()) {
        describe("swagger.json") {
            given("Pets API") {
                it("should get a swagger representation") {
                    given()
                            .`when`()
                            .get("/swagger/swagger.json")
                            .then()
                            .assertThat()
                            .statusCode(Status.OK.value())
                            .extract()
                            .asString()
                            .let {
                                assertEquals(it, """{"swagger":"2.0","info":{"version":"v1","title":"Pets Kotlin API"},"basePath":"/","tags":[{"name":"Pets","description":"Everything about your Pets."}],"schemes":["http"],"consumes":["application/json"],"produces":["application/json"],"paths":{"/api/pets":{"get":{"tags":["Pets"],"summary":"List pets ordered by id","description":"","operationId":"getPets","parameters":[{"name":"start","in":"query","description":"Start offset, useful for paging. Default is `0`.","required":false,"type":"integer","default":0,"format":"int32"},{"name":"max","in":"query","description":"Max page size, useful for paging. Default is `50`.","required":false,"type":"integer","default":20,"format":"int32"}],"responses":{"200":{"description":"Pets ordered by name.","schema":{"type":"array","items":{"${'$'}ref":"#/definitions/Pet"}}}}},"post":{"tags":["Pets"],"summary":"Add a new pet to the store","description":"","operationId":"postPets","parameters":[{"in":"body","name":"body","description":"Pet object that needs to be added to the store.","required":true,"schema":{"${'$'}ref":"#/definitions/Pet"}}],"responses":{"200":{"description":"Returns a saved pet.","schema":{"${'$'}ref":"#/definitions/Pet"}}}},"put":{"tags":["Pets"],"summary":"Update an existing pet","description":"","operationId":"putPets","parameters":[{"in":"body","name":"body","description":"Pet object that needs to be updated.","required":true,"schema":{"${'$'}ref":"#/definitions/Pet"}}],"responses":{"200":{"description":"Returns a saved pet.","schema":{"${'$'}ref":"#/definitions/Pet"}}}}},"/api/pets/{id}":{"get":{"tags":["Pets"],"description":"Find pet by ID","operationId":"getPetsById","parameters":[{"name":"id","in":"path","description":"Pet ID.","required":true,"type":"integer","format":"int64"}],"responses":{"200":{"description":"Returns `200` with a single pet or `404`","schema":{"${'$'}ref":"#/definitions/Pet"}},"404":{"description":"Not Found"}}},"delete":{"tags":["Pets"],"summary":"Deletes a pet by ID","description":"","operationId":"deletePets","parameters":[{"name":"id","in":"path","description":"Pet ID.","required":true,"type":"integer","format":"int64"}],"responses":{"204":{"description":"A `204`","schema":{"${'$'}ref":"#/definitions/Result"}}}}}},"definitions":{"Pet":{"type":"object","properties":{"id":{"type":"integer","format":"int64"},"name":{"type":"string"}}},"Result":{"type":"object"}}}""")
                            }
                }
            }
        }

        describe("api.raml") {
            given("Pets API") {
                it("should get a raml representation") {
                    given()
                            .`when`()
                            .get("/raml/api.raml")
                            .then()
                            .assertThat()
                            .statusCode(Status.OK.value())
                            .extract()
                            .asString()
                            .let {
                                assertEquals(it, """#%RAML 1.0
---
title: Pets Kotlin API
version: 0.0.0
baseUri: https://jooby-spec.herokuapp.com
mediaType:
- application/json
protocols:
- HTTP
types:
  Pet:
    type: object
    properties:
      id?: integer
      name?: string
    example:
      id: 0
      name: string
  Result:
    type: object
/api:
  /pets:
    description: Everything about your Pets.
    get:
      description: List pets ordered by id.
      queryParameters:
        start:
          required: false
          description: Start offset, useful for paging. Default is `0`.
          default: 0
          type: integer
        max:
          required: false
          description: Max page size, useful for paging. Default is `50`.
          default: 20
          type: integer
      responses:
        200:
          description: Pets ordered by name.
          body:
            type: Pet[]
    post:
      description: Add a new pet to the store.
      responses:
        200:
          description: Returns a saved pet.
          body:
            type: Pet
      body:
        type: Pet
    put:
      description: Update an existing pet.
      responses:
        200:
          description: Returns a saved pet.
          body:
            type: Pet
      body:
        type: Pet
    /{id}:
      description: Everything about your Pets.
      uriParameters:
        id:
          required: true
          description: Pet ID.
          type: integer
      get:
        description: Find pet by ID
        responses:
          200:
            description: Returns `200` with a single pet or `404`
            body:
              type: Pet
          404:
            description: Not Found
      delete:
        description: Deletes a pet by ID.
        responses:
          204:
            description: A `204`
            body:
              type: Result
""")
                            }
                }
            }
        }
    }
})
