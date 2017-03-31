package devoxxfr2017;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.procedure.Context;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;

import java.util.HashMap;
import java.util.Map;

public class UserDefinedFunction {

   @Context
   public GraphDatabaseService graphDb;

   @UserFunction
   public Double averageRating(@Name("title") String title) {

      try (Transaction tx = graphDb.beginTx()) {
         String cql = "MATCH (m: Movie {title: {title}})<-[r:RATED]-() RETURN AVG(r.stars) as average;";
         Map<String, Object> params = new HashMap<>();
         params.put("title", title);

         Result result = graphDb.execute(cql, params);
         tx.success();
         if (!result.hasNext()) {
            return null;
         }
         Object average = result.next().get("average");
         return average == null ? -1d : Double.parseDouble(average.toString());
      }
   }

}
