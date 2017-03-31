package net.biville.florent.devoxxfr2017;

import java.util.stream.Stream;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.procedure.Context;
import org.neo4j.procedure.Procedure;


public class UserDefinedProcedure {

   @Context
   public GraphDatabaseService graphDb;

   @Procedure
   public Stream<LongValue> foo() {

      return Stream.of(new LongValue(42L));
   }

   public static class LongValue {
      public final Long value;

      public LongValue(Long value) {
         this.value = value;
      }
   }
}
