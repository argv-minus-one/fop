/*
 * $Id: BoxPropShorthandParser.java,v 1.3 2003/03/05 21:48:01 jeremias Exp $
 * ============================================================================
 *                    The Apache Software License, Version 1.1
 * ============================================================================
 *
 * Copyright (C) 1999-2003 The Apache Software Foundation. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modifica-
 * tion, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment: "This product includes software
 *    developed by the Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "FOP" and "Apache Software Foundation" must not be used to
 *    endorse or promote products derived from this software without prior
 *    written permission. For written permission, please contact
 *    apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache", nor may
 *    "Apache" appear in their name, without prior written permission of the
 *    Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * APACHE SOFTWARE FOUNDATION OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLU-
 * DING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * ============================================================================
 *
 * This software consists of voluntary contributions made by many individuals
 * on behalf of the Apache Software Foundation and was originally created by
 * James Tauber <jtauber@jtauber.com>. For more information on the Apache
 * Software Foundation, please see <http://www.apache.org/>.
 */
package org.apache.fop.fo;

import org.apache.fop.fo.properties.ListProperty;
import org.apache.fop.fo.properties.Property;
import org.apache.fop.fo.properties.PropertyMaker;

/**
 * Shorthand property parser for Box properties
 */
public class BoxPropShorthandParser extends GenericShorthandParser {

    /**
     * @see org.apache.fop.fo.GenericShorthandParser#GenericShorthandParser()
     */
    public BoxPropShorthandParser() {
    }

    /**
     * Stores 1 to 4 values of same type.
     * Set the given property based on the number of values set.
     * Example: padding, border-width, border-color, border-style, margin
     * @see org.apache.fop.fo.GenericShorthandParser#convertValueForProperty(
     * int, ListProperty, Property.Maker, PropertyList)
     */
    protected Property convertValueForProperty(int propId,
                                               ListProperty listProperty,
                                               PropertyMaker maker,
                                               PropertyList propertyList) {
        String name = FOPropertyMapping.getPropertyName(propId);
        Property p = null;
        int count = listProperty.getList().size();
        if (name.indexOf("-top") >= 0) {
            p = getElement(listProperty, 0);
        } else if (name.indexOf("-right") >= 0) {
            p = getElement(listProperty, count > 1 ? 1 : 0);
        } else if (name.indexOf("-bottom") >= 0) {
            p = getElement(listProperty, count > 2 ? 2 : 0);
        } else if (name.indexOf("-left") >= 0) {
            p = getElement(listProperty, count > 3 ? 3 : (count > 1 ? 1 : 0));
        }
        // if p not null, try to convert it to a value of the correct type
        if (p != null) {
            return maker.convertShorthandProperty(propertyList, p, null);
        }
        return p;
    }

}
