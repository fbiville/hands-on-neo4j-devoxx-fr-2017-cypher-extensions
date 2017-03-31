package devoxxfr2017;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.neo4j.driver.v1.Config;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.harness.junit.Neo4jRule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Offset.offset;

public class UserDefinedFunctionTest {

   @Rule
   public Neo4jRule rule = new Neo4jRule().withFunction(UserDefinedFunction.class);

   @Before
   public void prepare() {
      try (Driver driver = GraphDatabase.driver(rule.boltURI(), config());
           Session session = driver.session()) {

         session.run(
               "CREATE (matrix1:Movie {title: 'Matrix 1'}) " +
                     "CREATE (matrix2: Movie {title: 'Matrix 2'}) " +
                     "CREATE (matrix3: Movie {title: 'Matrix 3'}) " +
                     "CREATE (marouane:User {name: 'Marouane'}) " +
                     "CREATE (florent:User {name: 'Florent'}) " +
                     "CREATE (marouane)-[:RATED {stars: 3}]->(matrix1) " +
                     "CREATE (marouane)-[:RATED {stars: 4}]->(matrix2) " +
                     "CREATE (florent)-[:RATED {stars: 4}]->(matrix1) " +
                     "CREATE (florent)-[:RATED {stars: 5}]->(matrix2) " +
                     "CREATE (florent)-[:RATED {stars: 3}]->(matrix3)"
         );
      }
   }

   @Test
   public void should_return_average_rating_for_a_given_movie() throws Exception {
      try (Driver driver = GraphDatabase.driver(rule.boltURI(), config());
           Session session = driver.session()) {

         StatementResult result = session.run("RETURN devoxxfr2017.averageRating('Matrix 1') as average");

         assertThat(result.hasNext()).isTrue();
         assertThat(result.next().get("average").asDouble()).isEqualTo(3.5, offset(0.01));
         assertThat(result.hasNext()).isFalse();
      }
   }

   @Test
   public void should_return_negative_value_for_unknown_movies() throws Exception {
      try (Driver driver = GraphDatabase.driver(rule.boltURI(), config());
           Session session = driver.session()) {

         StatementResult result = session.run("RETURN devoxxfr2017.averageRating('UNKNOWN') as average");

         assertThat(result.hasNext()).isTrue();
         assertThat(result.next().get("average").asDouble()).isEqualTo(-1d);
         assertThat(result.hasNext()).isFalse();
      }
   }

   private Config config() {
      return Config.build().withEncryptionLevel(Config.EncryptionLevel.NONE).toConfig();
   }
}
