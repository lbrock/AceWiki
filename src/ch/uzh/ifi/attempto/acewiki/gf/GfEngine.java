// This file is part of AceWiki.
// Copyright 2008-2013, AceWiki developers.
// 
// AceWiki is free software: you can redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation, either version 3 of
// the License, or (at your option) any later version.
// 
// AceWiki is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
// even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License along with AceWiki. If
// not, see http://www.gnu.org/licenses/.

package ch.uzh.ifi.attempto.acewiki.gf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.uzh.ifi.attempto.acewiki.core.AbstractAceWikiEngine;
import ch.uzh.ifi.attempto.acewiki.core.AceWikiGrammarEditor;
import ch.uzh.ifi.attempto.acewiki.core.AceWikiReasoner;
import ch.uzh.ifi.attempto.acewiki.core.Concept;
import ch.uzh.ifi.attempto.acewiki.core.GeneralTopic;
import ch.uzh.ifi.attempto.acewiki.core.Individual;
import ch.uzh.ifi.attempto.acewiki.core.LanguageHandler;
import ch.uzh.ifi.attempto.acewiki.core.Ontology;
import ch.uzh.ifi.attempto.acewiki.core.OntologyElement;
import ch.uzh.ifi.attempto.acewiki.core.Sentence;
import ch.uzh.ifi.attempto.acewiki.owl.AceWikiOWLReasoner2;
import ch.uzh.ifi.attempto.acewiki.owl.OWLFunctionalSyntaxExporter;
import ch.uzh.ifi.attempto.acewiki.owl.OWLXMLExporter;

/**
 * This AceWiki engine uses a GF (Grammatical Framework) grammar.
 * 
 * @author Kaarel Kaljurand
 */
public class GfEngine extends AbstractAceWikiEngine {

	private AceWikiOWLReasoner2 reasoner = new AceWikiOWLReasoner2();

	// TODO: support the creation of dynamic queries
	// public static final String TYPE_QUERY = "query";

	private Map<String, GfHandler> languageHandlers = new HashMap<String, GfHandler>();

	private String[] languages;

	private GfGrammar gfGrammar;

	private GfGrammarEditor gfGrammarEditor;

	/**
	 * Creates a new GF-based AceWiki engine.
	 */
	public GfEngine() {
		addExporter(new GfReportExporter());
		addExporter(new OWLFunctionalSyntaxExporter(true));
		addExporter(new OWLFunctionalSyntaxExporter(false));
		addExporter(new OWLXMLExporter(true));
		addExporter(new OWLXMLExporter(false));
		setLexicalTypes(GeneralTopic.NORMAL_TYPE, TypeGfModule.INTERNAL_TYPE);
	}

	public void init(Ontology ontology) {
		gfGrammar = new GfGrammar(ontology);
		gfGrammarEditor = new GfGrammarEditor(gfGrammar);

		Set<String> hiddenLanguages = ontology.getParameterAsSetOfString(GfParameters.HIDDEN_LANGUAGES);

		// Sort languages alphabetically according to displayed language name:
		List<String> languageNames = new ArrayList<>();
		Map<String,String> languageMap = new HashMap<>();
		for (String l : gfGrammar.getLanguages()) {
			if (! hiddenLanguages.contains(l)) {
				String n = getLanguageHandler(l).getLanguageName();
				languageNames.add(n);
				languageMap.put(n, l);
			}
		}
		Collections.sort(languageNames);
		languages = new String[languageNames.size()];
		int i = 0;
		for (String l : languageNames) {
			languages[i++] = languageMap.get(l);
		}

		super.init(ontology);
	}


	public LanguageHandler getLanguageHandler(String language) {
		GfHandler lh = languageHandlers.get(language);
		if (lh == null) {
			lh = new GfHandler(language, gfGrammar);
			languageHandlers.put(language, lh);
		}
		return lh;
	}


	public String[] getLanguages() {
		return languages;
	}


	/**
	 * Returns the grammar object.
	 * 
	 * @return The grammar object.
	 */
	public GfGrammar getGfGrammar() {
		return gfGrammar;
	}

	// TODO: implement a reasoner that does ACE reasoning if ACE is
	// one of the languages
	public AceWikiReasoner getReasoner() {
		return reasoner;
	}

	public OntologyElement createOntologyElement(String type) {
		if (GeneralTopic.NORMAL_TYPE.equals(type)) {
			return GeneralTopic.makeNormal("");
		} else if (TypeGfModule.hasType(type)) {
			return new TypeGfModule(this);
		}
		return null;
	}


	public Sentence createSentence(String serialized) {
		return GfSentence.createGfSentence(gfGrammar, GfGrammar.deserialize(serialized));
	}


	public Sentence createAssignmentSentence(Individual ind, Concept concept) {
		// TODO
		return null;
	}


	public Sentence createHierarchySentence(Concept subConcept, Concept superConcept) {
		// TODO
		return null;
	}

	@Override
	public AceWikiGrammarEditor getGrammarEditor() {
		return gfGrammarEditor;
	}

}
