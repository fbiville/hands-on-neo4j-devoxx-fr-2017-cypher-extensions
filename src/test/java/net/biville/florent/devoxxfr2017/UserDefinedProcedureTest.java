package net.biville.florent.devoxxfr2017;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.neo4j.driver.v1.Config;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.harness.junit.Neo4jRule;

public class UserDefinedProcedureTest {

   @Rule
   public Neo4jRule rule = new Neo4jRule().withProcedure(UserDefinedProcedure.class);

   @Test
   public void should_() {
      try (Driver driver = GraphDatabase.driver(rule.boltURI(), config());
           Session session = driver.session()) {

         Record result = session.run("CALL net.biville.florent.devoxxfr2017.foo() YIELD value RETURN value AS result").single();

         assertThat(result.get("result").asLong()).isEqualTo(42L);
      }
   }

   private Config config() {
      return Config.build().withEncryptionLevel(Config.EncryptionLevel.NONE).toConfig();
   }
}