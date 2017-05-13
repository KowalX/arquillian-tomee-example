package pl.raszkowski.examples;

import java.util.Date;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;

@RunWith(Arquillian.class)
public class BookCreatorIT {
    @Deployment
    public static JavaArchive createDeployment() {
        return new DeploymentBuilder()
                .withClass(BookCreator.class)
                .withClass(Book.class)
                .build();
    }

    @EJB
    private BookCreator bookCreator;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void create() {
        //GIVEN
        Book book = new Book();
        book.setTitle("Achaja Tom I");
        book.setAuthor("Andrzej Ziemiański");
        book.setYear(new Date());

        //WHEN
        bookCreator.create(book);

        //THEN
        TypedQuery<Book> query = entityManager.createQuery("SELECT b FROM Book b WHERE b.title = :title", Book.class);
        query.setParameter("title", "Achaja Tom I");

        Book persistedBook = query.getSingleResult();

        assertNotNull(persistedBook);
    }
}