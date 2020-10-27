package test;

import javax.persistence.EntityManager;

import utility.EntityManagerFactory;

public class Test 
{
	@SuppressWarnings("unused")
	public static void main(String[] args) 
	{
		System.out.println("INIZIO PROVA");
//		EntityManager em = EntityManagerFactory.getIstance().createEntityManager();
		
		LogicaJPA logJPA = new LogicaJPA("percistenceUnit");
	}
}
