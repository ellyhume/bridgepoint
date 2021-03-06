/*
 * generated by Xtext 2.9.2
 */
package org.xtuml.bp.xtext.masl.ui.labeling

import com.google.inject.Inject
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider
import org.eclipse.xtext.ui.editor.model.IXtextDocument
import org.eclipse.xtext.ui.label.DefaultEObjectLabelProvider
import org.xtuml.bp.xtext.masl.MASLExtensions
import org.xtuml.bp.xtext.masl.masl.structure.AttributeDefinition
import org.xtuml.bp.xtext.masl.masl.structure.DomainDefinition
import org.xtuml.bp.xtext.masl.masl.structure.EventDefinition
import org.xtuml.bp.xtext.masl.masl.structure.ExceptionDeclaration
import org.xtuml.bp.xtext.masl.masl.structure.ObjectDeclaration
import org.xtuml.bp.xtext.masl.masl.structure.ObjectDefinition
import org.xtuml.bp.xtext.masl.masl.structure.Parameterized
import org.xtuml.bp.xtext.masl.masl.structure.RelationshipDefinition
import org.xtuml.bp.xtext.masl.masl.structure.RelationshipEnd
import org.xtuml.bp.xtext.masl.masl.structure.StateDeclaration
import org.xtuml.bp.xtext.masl.masl.structure.StateDefinition
import org.xtuml.bp.xtext.masl.masl.structure.TerminatorDefinition
import org.xtuml.bp.xtext.masl.masl.types.EnumerationTypeDefinition
import org.xtuml.bp.xtext.masl.masl.types.Enumerator
import org.xtuml.bp.xtext.masl.masl.types.StructureComponentDefinition
import org.xtuml.bp.xtext.masl.masl.types.StructureTypeDefinition
import org.xtuml.bp.xtext.masl.masl.types.TypeDeclaration
import org.xtuml.bp.xtext.masl.typesystem.MaslTypeProvider
import org.xtuml.bp.xtext.masl.validation.SignatureProvider

import static org.xtuml.bp.xtext.masl.typesystem.BuiltinType.*
import org.xtuml.bp.xtext.masl.masl.structure.DomainServiceDeclaration
import org.xtuml.bp.xtext.masl.masl.structure.DomainServiceDefinition
import org.xtuml.bp.xtext.masl.masl.structure.TerminatorServiceDeclaration
import org.xtuml.bp.xtext.masl.masl.structure.TerminatorServiceDefinition
import org.xtuml.bp.xtext.masl.masl.structure.ObjectServiceDeclaration
import org.xtuml.bp.xtext.masl.masl.structure.ObjectServiceDefinition

/**
 * Provides labels for EObjects.
 * 
 * See https://www.eclipse.org/Xtext/documentation/304_ide_concepts.html#label-provider
 */
class MASLLabelProvider extends DefaultEObjectLabelProvider {

	@Inject extension MASLExtensions
	@Inject extension MaslTypeProvider
	@Inject extension SignatureProvider
	
	@Inject
	new(AdapterFactoryLabelProvider delegate) {
		super(delegate);
	}

	def text(IXtextDocument it) {
		readOnly [ resource |
			URI.decode(resource.URI.lastSegment)
		]
	}
	
	def text(AttributeDefinition it) {
		name + ' : ' + maslType 
	}

	def text(StructureComponentDefinition it) {
		name + ' : ' + maslType 		
	}

	def text(Parameterized it) {
		val type = maslType
		name + parametersAsString
			 + if(type != ANY_TYPE && type != NO_TYPE) ' : ' + maslType else '' 
	}

	def image(EObject it) {
		switch it {
			DomainDefinition:
				return 'model/Component.gif'
			ObjectDeclaration,
			ObjectDefinition:
				return 'model/Class.gif'
			DomainServiceDeclaration,
			DomainServiceDefinition,
			ObjectServiceDeclaration,
			ObjectServiceDefinition:
				return 'model/Function.gif'
			RelationshipDefinition:
				return 'model/Associative.gif'
			RelationshipEnd:
				if(eContainmentFeature.name == 'forwards') 
					return 'model/AssociativeOneEnd.gif' 
				else 
					return 'model/AssociativeOtherEnd.gif'
			AttributeDefinition:
				if (identifier)
					return 'model/Identifier.gif'
				else
					return 'model/Attribute.gif'
			TerminatorDefinition:
				return 'model/Port.gif'
			TerminatorServiceDefinition,
			TerminatorServiceDeclaration:
				return 'model/Bridge.gif'
			TypeDeclaration:
				return switch definition {
					EnumerationTypeDefinition: 'model/Enumeration.gif'
					StructureTypeDefinition: 'model/StructuredDataType.gif'
					default: 'model/Datatype.gif'
				}
			Enumerator:
				return 'model/Enumerator.gif'
			StructureComponentDefinition:
				return 'model/StructuredDataTypeMember.gif'
			StateDefinition,
			StateDeclaration:
				return 'model/State.gif'
			EventDefinition:
				return 'model/Event.gif'
			ExceptionDeclaration:
				return 'model/Exception.gif'
		}
	}
}
