package devoxxfr2017;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.neo4j.driver.v1.Config;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.harness.junit.Neo4jRule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Maps.newHashMap;

public class UserDefinedProcedureTest {

   @Rule
   public Neo4jRule rule = new Neo4jRule().withProcedure(UserDefinedProcedure.class);

   @Before
   public void prepare() {

      try (Driver driver = GraphDatabase.driver(rule.boltURI(), config());
           Session session = driver.session()) {

         session.run(
               "CREATE (m1:Movie {title: 'Matrix 1'}) " +
                     "CREATE (m2: Movie {title: 'Matrix 2'}) " +
                     "CREATE (m3: Movie {title: 'Matrix 3'}) " +
                     "CREATE (m4: Movie {title: 'Matrix 4'}) " +

                     "CREATE (u1:User {name: 'Marouane'}) " +
                     "CREATE (u2:User {name: 'Florent'}) " +
                     // Marouane
                     "CREATE (u1)-[:RATED {stars: 3}]->(m1) " +
                     "CREATE (u1)-[:RATED {stars: 4}]->(m2) " +
                     // Florent
                     "CREATE (u2)-[:RATED {stars: 4}]->(m1) " +
                     "CREATE (u2)-[:RATED {stars: 5}]->(m2) " +
                     "CREATE (u2)-[:RATED {stars: 3}]->(m3)"
         );
      }
   }

   @Test
   public void should_return_top_3_recommendations() {
      try (Driver driver = GraphDatabase.driver(rule.boltURI(), config());
           Session session = driver.session()) {

         StatementResult result = session.run("CALL devoxxfr2017.recommendMovies('Marouane', 'Florent') YIELD title RETURN title AS title");

         assertThat(result.list(Record::asMap))
               .containsExactly(newHashMap("title", "Matrix 3"));
      }
   }

   private Config config() {
      return Config.build().withEncryptionLevel(Config.EncryptionLevel.NONE).toConfig();
   }
}
