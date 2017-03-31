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

         /*
           TODO: write the Cypher query that computes the average ratings for a movie
           the value of parameter "X" is accessed this way: MATCH (n:MyNode {my_property: {X}})...
          */
         String cql = "RETURN 'TODO'";
         Map<String, Object> params = new HashMap<>();
         params.put("username1", username1);
         params.put("username2", username2);
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
