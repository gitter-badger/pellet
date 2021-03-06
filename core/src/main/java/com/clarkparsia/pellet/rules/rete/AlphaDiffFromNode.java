// Copyright (c) 2006 - 2008, Clark & Parsia, LLC. <http://www.clarkparsia.com>
// This source code is available under the terms of the Affero General Public License v3.
//
// Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
// Questions, comments, or requests for clarification: licensing@clarkparsia.com

package com.clarkparsia.pellet.rules.rete;

import java.util.Iterator;

import org.mindswap.pellet.ABox;
import org.mindswap.pellet.DependencySet;
import org.mindswap.pellet.Individual;
import org.mindswap.pellet.Node;
import org.mindswap.pellet.utils.iterator.MapIterator;
import org.mindswap.pellet.utils.iterator.NestedIterator;

import com.clarkparsia.pellet.rules.model.DifferentIndividualsAtom;
import com.clarkparsia.pellet.rules.model.RuleAtom;

/**
 */
public class AlphaDiffFromNode extends AlphaNode {
	public AlphaDiffFromNode(ABox abox) {
		super(abox);
    }
	
	public boolean activate(Individual s, Individual o, DependencySet ds) {
		activate(WME.createDiffFrom(s, o, ds));
		return true;
	}

	public Iterator<WME> getMatches(int argIndex, final Node arg) {
		if (argIndex != 0 && argIndex != 1) {
			throw new IndexOutOfBoundsException();
		}
		
		if (!(arg instanceof Individual)) {
			throw new IllegalArgumentException();
		}
		
		return toWMEs(arg);
	}
	
	private Iterator<WME> toWMEs(final Node arg) { 
		return new MapIterator<Node, WME>(arg.getDifferents().iterator()) {
			@Override
            public WME map(Node node) {
	            return WME.createDiffFrom((Individual) arg, (Individual) node, arg.getDifferenceDependency(node));
            }
		};
    }

	public Iterator<WME> getMatches() {
		return new NestedIterator<Individual, WME>(abox.getIndIterator()) {
			@Override
            public Iterator<WME> getInnerIterator(Individual ind) {
				return toWMEs(ind);
            }
		};
	}
	
	public boolean matches(RuleAtom atom) {
		return (atom instanceof DifferentIndividualsAtom);
	}
	
	public String toString() {
		return "DiffFrom(0, 1)";
	}
}
