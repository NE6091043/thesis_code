/*******************************************************************************
 * Copyright (c) 2013-2015 LAAS-CNRS (www.laas.fr)
 * 7 Colonel Roche 31077 Toulouse - France
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Thierry Monteil (Project co-founder) - Management and initial specification,
 *         conception and documentation.
 *     Mahdi Ben Alaya (Project co-founder) - Management and initial specification,
 *         conception, implementation, test and documentation.
 *     Christophe Chassot - Management and initial specification.
 *     Khalil Drira - Management and initial specification.
 *     Yassine Banouar - Initial specification, conception, implementation, test
 *         and documentation.
 *     Guillaume Garzone - Conception, implementation, test and documentation.
 *     Francois Aissaoui - Conception, implementation, test and documentation.
 ******************************************************************************/

// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2013.06.24 at 02:12:54 AM CEST
//


package org.eclipse.om2m.commons.resource;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java Class for PermissionType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="PermissionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://uri.etsi.org/m2m}permissionFlags"/>
 *         &lt;element ref="{http://uri.etsi.org/m2m}permissionHolders"/>
 *       &lt;/sequence>
 *       &lt;attribute ref="{http://uri.etsi.org/m2m}id"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PermissionType", propOrder = {
        "permissionFlags",
        "permissionHolders"
})
@Embeddable
public class PermissionType {
	
    @XmlElement(required = true)
    @Embedded
    protected PermissionFlagListType permissionFlags;
    @XmlElement(required = true)
    protected PermissionHolderType permissionHolders;
    @XmlAttribute(name = "id", namespace = "http://uri.etsi.org/m2m")
    @XmlSchemaType(name = "anyURI")
    protected String id;

    /**
     * Gets the value of the property permissionFlags.
     *
     * @return
     *     possible object is
     *     {@link PermissionFlagListType }
     *
     */
    public PermissionFlagListType getPermissionFlags() {
        return permissionFlags;
    }

    /**
     * Sets the value of the property permissionFlags.
     *
     * @param value
     *     allowed object is
     *     {@link PermissionFlagListType }
     *
     */
    public void setPermissionFlags(PermissionFlagListType value) {
        this.permissionFlags = value;
    }

    /**
     * Gets the value of the property permissionHolders.
     *
     * @return
     *     possible object is
     *     {@link PermissionHolderType }
     *
     */
    public PermissionHolderType getPermissionHolders() {
        return permissionHolders;
    }

    /**
     * Sets the value of the property permissionHolders.
     *
     * @param value
     *     allowed object is
     *     {@link PermissionHolderType }
     *
     */
    public void setPermissionHolders(PermissionHolderType value) {
        this.permissionHolders = value;
    }

    /**
     * Gets the value of the property id.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the property id.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setId(String value) {
        this.id = value;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((permissionFlags == null) ? 0 : permissionFlags.hashCode());
		result = prime
				* result
				+ ((permissionHolders == null) ? 0 : permissionHolders
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		PermissionType other = (PermissionType) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (permissionFlags == null) {
			if (other.permissionFlags != null) {
				return false;
			}
		} else if (!permissionFlags.equals(other.permissionFlags)) {
			return false;
		}
		if (permissionHolders == null) {
			if (other.permissionHolders != null) {
				return false;
			}
		} else if (!permissionHolders.equals(other.permissionHolders)) {
			return false;
		}
		return true;
	}

}