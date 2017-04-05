package devoxxfr2017;

import java.util.HashMap;
import java.util.Map;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.procedure.Context;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;

public class UserDefinedFunction {

   @Context
   public GraphDatabaseService graphDb;

   @UserFunction
   public double averageRating(@Name("title") String title) {

      try (Transaction tx = graphDb.beginTx()) {
         String cql = "MATCH (m: Movie {title: {title}})<-[r:RATED]-() RETURN AVG(r.stars) as average;";
         Map<String, Object> params = new HashMap<>();
         params.put("title", title);

         Result result = graphDb.execute(cql, params);
         double average = (double) result.next().get("average");

         tx.success();

         return average;
      }
   }
}
