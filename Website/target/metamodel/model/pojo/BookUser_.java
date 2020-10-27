package model.pojo;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(BookUser.class)
public abstract class BookUser_ {

	public static volatile SingularAttribute<BookUser, String> firstname;
	public static volatile SingularAttribute<BookUser, String> phonenumber;
	public static volatile SingularAttribute<BookUser, Integer> id;
	public static volatile ListAttribute<BookUser, BookTransaction> transactions;
	public static volatile SingularAttribute<BookUser, String> email;
	public static volatile SingularAttribute<BookUser, String> lastname;

	public static final String FIRSTNAME = "firstname";
	public static final String PHONENUMBER = "phonenumber";
	public static final String ID = "id";
	public static final String TRANSACTIONS = "transactions";
	public static final String EMAIL = "email";
	public static final String LASTNAME = "lastname";

}

