package model.pojo;

import java.sql.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.pojo.BookTransaction.Codice;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(BookTransaction.class)
public abstract class BookTransaction_ {

	public static volatile SingularAttribute<BookTransaction, String> note;
	public static volatile SingularAttribute<BookTransaction, Date> data;
	public static volatile SingularAttribute<BookTransaction, Integer> id;
	public static volatile SingularAttribute<BookTransaction, Codice> transCode;

	public static final String NOTE = "note";
	public static final String DATA = "data";
	public static final String ID = "id";
	public static final String TRANS_CODE = "transCode";

}

