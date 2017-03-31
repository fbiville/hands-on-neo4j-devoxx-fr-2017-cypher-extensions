package devoxxfr2017;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.procedure.Context;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.Procedure;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;


public class UserDefinedProcedure {

   @Context
   public GraphDatabaseService graphDb;

   @Procedure
   public Stream<MovieRecord> recommendMovies(@Name("username1") String username1, @Name("username2") String username2) {

      try (Transaction tx = graphDb.beginTx()) {
         Map<String, Object> params = new HashMap<>();
         params.put("username1", username1);
         params.put("username2", username2);

         // Hint: Use name between curly braces
         String cql =
               "MATCH (user2:User {name: {username2}})-[r:RATED]->(movie:Movie), (user1: User {name: {username1}}) " +
               "WHERE NOT((user1)-[:RATED]->(movie)) " +
               "RETURN DISTINCT movie.title AS title " +
               "ORDER BY title ASC";
         Result result = graphDb.execute(cql, params);


         Stream<MovieRecord> records = result.stream()
               .map(row -> new MovieRecord((String) row.get("title")));
         tx.success();
         return records;

      }
   }

   public static class MovieRecord {
      public final String title;

      public MovieRecord(String title) {
         this.title = title;
      }
   }
}
