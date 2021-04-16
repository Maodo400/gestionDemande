package sn.esp.gestion;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("sn.esp.gestion");

        noClasses()
            .that()
            .resideInAnyPackage("sn.esp.gestion.service..")
            .or()
            .resideInAnyPackage("sn.esp.gestion.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..sn.esp.gestion.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
