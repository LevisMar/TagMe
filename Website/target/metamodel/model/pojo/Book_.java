package model.pojo;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Book.class)
public abstract class Book_ {

	public static volatile SingularAttribute<Book, Integer> quantity;
	public static volatile SingularAttribute<Book, String> name;
	public static volatile SingularAttribute<Book, String> description;
	public static volatile SingularAttribute<Book, Integer> edition;
	public static volatile SingularAttribute<Book, Publisher> publisher;
	public static volatile SingularAttribute<Book, BookCategory> category;
	public static volatile SingularAttribute<Book, String> barcode;
	public static volatile SetAttribute<Book, Author> authors;

	public static final String QUANTITY = "quantity";
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String EDITION = "edition";
	public static final String PUBLISHER = "publisher";
	public static final String CATEGORY = "category";
	public static final String BARCODE = "barcode";
	public static final String AUTHORS = "authors";

}

