package querydsldemo;

import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import querydsldemo.SpringDataTest.SomeConfig;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mysema.query.types.expr.BooleanExpression;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SomeConfig.class)
public class SpringDataTest {
 
	@Configuration
	@EnableMongoRepositories(considerNestedRepositories = true)
	public static class SomeConfig extends AbstractMongoConfiguration {
 
		@Override
		protected String getDatabaseName() {
			return "db-ref-test";
		}
 
		@Override
		public Mongo mongo() throws Exception {
			return new MongoClient();
		}
 
	}
 
	@Autowired MongoTemplate template;
	@Autowired ARepository aRepo;
 
	@Before
	public void setUp() {
		template.dropCollection(A.class);
	}
	
	@Test
	public void testFindWhereCollectionNotEmpty() {
 
		B b1 = new B();
		b1.name = "b1";
 
		B b2 = new B();
		b2.name = "b2";
 
		template.save(b1);
		template.save(b2);
 
		A a1 = new A();
		a1.id = "a1";
		a1.matches = Arrays.asList(b1, b2);
 
		A a2 = new A();
		a2.id = "a2";
		a2.matches = new ArrayList<B>();
 
		A a3 = new A();
		a3.id = "a3";
		a3.matches = null;
 
		template.save(a1);
		template.save(a2);
		template.save(a3);
		
		QA qa = QA.a;
		

		BooleanExpression expr = qa.matches.isNotEmpty();
		Iterable<A> result = aRepo.findAll(expr);
 
		assertThat(result, is(not((emptyIterable()))));
	}
 
	static interface ARepository extends CrudRepository<A, String>, QueryDslPredicateExecutor<A> {
	}
}