package test;

import java.text.ParseException;

import model.businessLogic.GestoreStaff;
import model.businessLogic.GestoreStaff.StatoLogin;
import model.businessLogic.GestoreUtenti;
import model.pojo.BookUser;
import model.pojo.StaffMember;
import utility.StatoRegistrazione;

@SuppressWarnings("unused")
public class TestUtenti 
{
	public static void main(String[] args) throws Exception 
	{
		
//		GestoreUtenti gu = new GestoreUtenti();
//		BookUser bu = creaEsempioUser();
		
//		StatoRegistrazione sr = gu.insertUser(bu);
//		System.out.println(sr.message);
		
//		StatoRegistrazione sr = gu.updateUser("leonard_99@live.it", bu);
//		System.out.println(sr.message);
		
		GestoreStaff gs = new GestoreStaff();
		StaffMember sm = creaEsempioStaff();
		
		StatoLogin stato = gs.login(sm.getUsername(), sm.getPassword());
		System.out.println(stato);
		
//		StatoRegistrazione sr = gs.insertStaffMember(sm);
//		System.out.println(sr.message);
		
//		sm.setUsername("CaptainAmerica");
//		sm.setPassword("iosonodegno");
//		StatoRegistrazione sr2 = gs.updateStaffMember("Steve_Rogers@live.it", sm);
//		System.out.println(sr2.message);
	}
	
	@SuppressWarnings("unused")
	private static BookUser creaEsempioUser() throws ParseException
	{
	    BookUser bu = new BookUser();
		
	    bu.setFirstname("Leonardo");
	    bu.setLastname("Mazzaracchio");
	    bu.setEmail("leonard_94@live.it");
	    bu.setPhonenumber("3925952564");
		
		return bu;
	}
	
	@SuppressWarnings("unused")
	private static StaffMember creaEsempioStaff() throws ParseException
	{
		StaffMember s = new StaffMember();
		
	    s.setFirstname("Steve");
	    s.setLastname("Rogers");
	    s.setEmail("Steve_Rogers@live.it");
	    s.setUsername("CaptainAmerica");
	    s.setPassword("iosonodegno");
		
		return s;
	}
}
