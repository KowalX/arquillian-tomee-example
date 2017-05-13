package pl.raszkowski.examples;

import java.util.HashSet;
import java.util.Set;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.formatter.Formatters;
import org.jboss.shrinkwrap.api.spec.WebArchive;

public class DeploymentBuilder {

    private Set<Class<?>> additionalClasses = new HashSet<>();

    private Set<Package> additionalRecursivePackages = new HashSet<>();

    public DeploymentBuilder withClass(Class<?> classToAdd) {
        additionalClasses.add(classToAdd);
        return this;
    }

    public DeploymentBuilder withRecursivePackages(Package packageToAdd) {
        additionalRecursivePackages.add(packageToAdd);
        return this;
    }

    public WebArchive build() {
        //@formatter:off
        WebArchive archive = ShrinkWrap.create(WebArchive.class, "test.war")
//                .addAsResource("liquibase/db-changelog.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource("resources.xml", "resources.xml")
                .addAsResource("test-persistence.xml", "/META-INF/persistence.xml");
        //@formatter:on

        for (Class<?> additionalClass : additionalClasses) {
            archive.addClass(additionalClass);
        }

        for (Package additionalPackage : additionalRecursivePackages) {
            archive.addPackages(true, additionalPackage);
        }

		archive.writeTo(System.out, Formatters.VERBOSE);

		return archive;
	}
}
