package model.pojo;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(StaffMember.class)
public abstract class StaffMember_ {

	public static volatile SingularAttribute<StaffMember, String> firstname;
	public static volatile SingularAttribute<StaffMember, String> password;
	public static volatile SingularAttribute<StaffMember, Integer> id;
	public static volatile ListAttribute<StaffMember, StaffMember> transactions;
	public static volatile SingularAttribute<StaffMember, String> email;
	public static volatile SingularAttribute<StaffMember, String> lastname;
	public static volatile SingularAttribute<StaffMember, String> username;

	public static final String FIRSTNAME = "firstname";
	public static final String PASSWORD = "password";
	public static final String ID = "id";
	public static final String TRANSACTIONS = "transactions";
	public static final String EMAIL = "email";
	public static final String LASTNAME = "lastname";
	public static final String USERNAME = "username";

}

