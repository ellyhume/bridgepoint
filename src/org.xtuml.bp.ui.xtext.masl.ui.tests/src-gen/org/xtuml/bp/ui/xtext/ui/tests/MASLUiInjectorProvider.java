/*
 * generated by Xtext 2.9.1
 */
package org.xtuml.bp.ui.xtext.ui.tests;

import com.google.inject.Injector;
import org.eclipse.xtext.junit4.IInjectorProvider;
import org.xtuml.bp.ui.xtext.masl.ui.internal.MaslActivator;

public class MASLUiInjectorProvider implements IInjectorProvider {

	@Override
	public Injector getInjector() {
		return MaslActivator.getInstance().getInjector("org.xtuml.bp.ui.xtext.MASL");
	}

}