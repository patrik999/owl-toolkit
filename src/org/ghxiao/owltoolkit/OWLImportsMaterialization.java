package org.ghxiao.owltoolkit;
import java.io.File;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

public class OWLImportsMaterialization {
	public static void main(String... args)
			throws OWLOntologyCreationException, OWLOntologyStorageException {
		if (args.length != 1){
			printUsage();
			System.exit(0);
		}
		
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology ontology = manager.loadOntologyFromOntologyDocument(new File(args[0]));

		
		Set<OWLOntology> importsClosure = ontology.getImportsClosure();
		
		OWLOntologyManager manager1 = OWLManager
				.createOWLOntologyManager();
		OWLOntology materializedOntology = manager1.createOntology(ontology.getOntologyID()) ;
		
		for(OWLOntology ont: importsClosure){
			for(OWLAxiom ax : ont.getAxioms()){
				manager1.addAxiom(materializedOntology, ax);
			}
		}

		manager.saveOntology(materializedOntology, System.out);
	}

	private static void printUsage() {
		System.err.println("Usage: owl-materialize-imports  file.owl");
		
	}
}

