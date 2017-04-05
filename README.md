# Devoxx France 2017 - Neo4j Lab - Cypher Extensions

## Goal

You need to complete two classes in `src/main/java`:

 - `devoxxfr2017.UserDefinedFunction`: this class defines a user-defined Cypher function, callable as any function. This will compute the average rating for a given movie title.
 - `devoxxfr2017.UserDefinedProcedure`: this class defines a user-defined Cypher procedure, callable via `CALL my.procedure YIELD property RETURN...`. This will recommend movies for a user based on what another user has rated.

In both cases, there are integration tests that will fail until the extensions are properly implemented. These also give you an example of how to call your extensions in Cypher ;-)

## Build

As simple as:

```
 $> mvn
```

## Deploy

Once the project is built, copy `target/devoxxfr-extensions.jar` to the `plugins` directory of your Neo4j instance (if you installed Neo4j with an installer, the `plugins` folder lives in your custom data directory).
