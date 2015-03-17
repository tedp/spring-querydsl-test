package querydsldemo;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document
public class A {
	@Id String id;
	String name;
	List<B> matches;

}
