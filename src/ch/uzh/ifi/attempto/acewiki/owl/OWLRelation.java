// This file is part of AceWiki.
// Copyright 2008-2011, AceWiki developers.
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

package ch.uzh.ifi.attempto.acewiki.owl;

import org.semanticweb.owlapi.model.OWLObjectProperty;

import ch.uzh.ifi.attempto.acewiki.core.Relation;

/**
 * This class stands for relation ontology elements with representations in OWL.
 * 
 * @author Tobias Kuhn
 */
public abstract class OWLRelation extends AbstractOWLOntoElement implements Relation {
	
	public OWLObjectProperty getOWLRepresentation() {
		return getOWLDataFactory().getOWLObjectProperty(getIRI());
	}

}
