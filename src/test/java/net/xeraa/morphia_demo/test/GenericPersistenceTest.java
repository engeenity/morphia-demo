package net.xeraa.morphia_demo.test;

import net.xeraa.morphia_demo.entities.CompanyEntity;
import net.xeraa.morphia_demo.entities.WorkerEntity;

import org.bson.types.ObjectId;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Testing our entities and the MongodbGenericPersistence.
 */
public class GenericPersistenceTest extends BaseTest {

  /**
   * Ensure a generic entity is saved and the ObjectId is generated correctly.
   */
  @Test
  public void persist() {
    CompanyEntity company = new CompanyEntity().setName("Test company");
    ObjectId id1 = genericPersistence.persist(company);
    assertNotNull("An ObjectId should have been generated when saving the entity", id1);
    assertEquals("The return value and actual value of the ObjectId should match",
		 company.getId(), id1);
    WorkerEntity worker = new WorkerEntity().setFirstname("firstname");
    ObjectId id2 = genericPersistence.persist(worker);
    assertEquals("The return value and actual value of the ObjectId should match",
		 worker.getId(), id2);
  }

  /**
   * Check that all documents in a collection are counted correctly.
   */
  @Test
  public void count() {
    assertEquals("In a clean database there should be zero entries", 0,
		 genericPersistence.count(CompanyEntity.class));
    assertEquals("In a clean database there should be zero entries", 0,
		 genericPersistence.count(WorkerEntity.class));
    CompanyEntity company1 = new CompanyEntity().setName("Test company 1");
    genericPersistence.persist(company1);
    assertEquals("After adding an entity there should be one entry", 1,
		 genericPersistence.count(CompanyEntity.class));
    CompanyEntity company2 = new CompanyEntity().setName("Test company 2");
    genericPersistence.persist(company2);
    assertEquals("After adding another entity there should be two entries", 2,
		 genericPersistence.count(CompanyEntity.class));
    assertEquals("An unrelated collection should still have zero entries", 0,
		 genericPersistence.count(WorkerEntity.class));
  }

  /**
   * Try to get entities by their ObjectId.
   */
  @Test
  public void getById() {
    CompanyEntity company = new CompanyEntity().setName("Test company");
    ObjectId id = genericPersistence.persist(company);
    assertNull("An invalid ObjectId should return null",
	       genericPersistence.get(CompanyEntity.class, new ObjectId()));
    assertNotNull("An entity should be available under the given ObjectId",
		  genericPersistence.get(CompanyEntity.class, id));
    assertEquals("The entity should be the one of the returned ObjectId", "Test company",
                 genericPersistence.get(CompanyEntity.class, id).getName());
    assertNull("A valid ObjectId for a different collection should return null",
	       genericPersistence.get(WorkerEntity.class, id));
  }

}
