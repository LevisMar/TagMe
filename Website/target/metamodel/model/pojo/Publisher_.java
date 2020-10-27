package model.pojo;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Publisher.class)
public abstract class Publisher_ {

	public static volatile SingularAttribute<Publisher, String> country;
	public static volatile SetAttribute<Publisher, Book> books;
	public static volatile SingularAttribute<Publisher, String> name;
	public static volatile SingularAttribute<Publisher, Integer> id;

	public static final String COUNTRY = "country";
	public static final String BOOKS = "books";
	public static final String NAME = "name";
	public static final String ID = "id";

}

