//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.03.28 at 11:33:10 PM PDT 
//


package edu.usc.softarch.extractors.cda.odem;

import javax.xml.bind.annotation.*;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "createdBy"
})
@XmlRootElement(name = "header")
public class Header {

    @XmlElement(name = "created-by", required = true)
    protected CreatedBy createdBy;

    /**
     * Gets the value of the createdBy property.
     * 
     * @return
     *     possible object is
     *     {@link CreatedBy }
     *     
     */
    public CreatedBy getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets the value of the createdBy property.
     * 
     * @param value
     *     allowed object is
     *     {@link CreatedBy }
     *     
     */
    public void setCreatedBy(CreatedBy value) {
        this.createdBy = value;
    }

}
