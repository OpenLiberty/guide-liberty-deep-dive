// tag::copyright[]
/*******************************************************************************
 * Copyright (c) 2022 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - Initial implementation
 *******************************************************************************/
// end::copyright[]
package io.openliberty.deep.dive.inventory.client;

import java.util.logging.Logger;

import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;

import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;

public class UnknownUriExceptionMapper
	implements ResponseExceptionMapper<UnknownUriException> {
		  Logger LOG = Logger.getLogger(UnknownUriExceptionMapper.class.getName());

		  @Override
		  public boolean handles(int status, MultivaluedMap<String, Object> headers) {
		    LOG.info("status = " + status);
		    return status == 404;
		  }

		  @Override
		  public UnknownUriException toThrowable(Response response) {
		    return new UnknownUriException();
		  }
}
