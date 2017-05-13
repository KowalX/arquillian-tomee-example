package pl.raszkowski.examples;

import java.util.HashSet;
import java.util.Set;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.formatter.Formatters;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

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

    public JavaArchive build() {
        //@formatter:off
        JavaArchive archive = ShrinkWrap.create(JavaArchive.class)
//                .addAsResource("liquibase/db-changelog.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsResource("resources.xml", "resources.xml")
                .addAsManifestResource("test-persistence.xml", "persistence.xml");
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
