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
 * 		conception and documentation.
 *     Mahdi Ben Alaya (Project co-founder) - Management and initial specification, 
 * 		conception, implementation, test and documentation.
 *     Christophe Chassot - Management and initial specification.
 *     Khalil Drira - Management and initial specification.
 *     Yassine Banouar - Initial specification, conception, implementation, test 
 * 		and documentation.
 *     Guillaume Garzone - Conception, implementation, test and documentation.
 *     Francois Aissaoui - Conception, implementation, test and documentation.
 ******************************************************************************/
package org.eclipse.om2m.core.controller;

import javax.persistence.EntityManager;

import org.eclipse.om2m.commons.resource.Applications;
import org.eclipse.om2m.commons.resource.ErrorInfo;
import org.eclipse.om2m.commons.resource.StatusCode;
import org.eclipse.om2m.commons.resource.Refs;
import org.eclipse.om2m.commons.rest.RequestIndication;
import org.eclipse.om2m.commons.rest.ResponseConfirm;
import org.eclipse.om2m.core.constants.Constants;
import org.eclipse.om2m.core.dao.DAOFactory;
import org.eclipse.om2m.core.dao.DBAccess;

/**
 * Implements Create, Retrieve, Update, Delete and Execute methods to handle
 * generic REST request for {@link Applications} collection resource.
 *
 * @author <ul>
 *         <li>Yassine Banouar < ybanouar@laas.fr > < yassine.banouar@gmail.com ></li>
 *         <li>Mahdi Ben Alaya < ben.alaya@laas.fr > < benalaya.mahdi@gmail.com ></li>
 *         </ul>
 */

public class ApplicationsController extends Controller {

    /**
     * Creates {@link Applications} resource. It is not allowed Through the API.
     * @param requestIndication - The generic request to handle.
     * @return The generic returned response.
     */
    public ResponseConfirm doCreate (RequestIndication requestIndication) {

        // applicationCollection:       (createReq NA) (response M)
        // applicationAnncCollection:   (createReq NA) (response M)
        // subscriptionsReference:      (createReq NA) (response M)
        // accessRightID:               (createReq NA) (response O)
        // creationTime:                (createReq NA) (response M)
        // lastModifiedTime:            (createReq NA) (response M)

        // Response
        return new ResponseConfirm(new ErrorInfo(StatusCode.STATUS_METHOD_NOT_ALLOWED,requestIndication.getMethod()+" Method is not allowed"));
    }

    /**
     * Retrieves {@link Applications} resource.
     * @param requestIndication - The generic request to handle.
     * @return The generic returned response.
     */
    public ResponseConfirm doRetrieve (RequestIndication requestIndication) {

        // applicationCollection:       (response M)
        // applicationAnncCollection:   (response M)
        // subscriptionsReference:      (response M)
        // accessRightID:               (response O)
        // creationTime:                (response M)
        // lastModifiedTime:            (response M)

        ResponseConfirm errorResponse = new ResponseConfirm();
        EntityManager em = DBAccess.createEntityManager();
        em.getTransaction().begin();
        String accessRightID = getAccessRightId(requestIndication.getTargetID(), em);
        
        // Check resource existence
        if (accessRightID == null) {
        	em.close();
            return new ResponseConfirm(new ErrorInfo(StatusCode.STATUS_NOT_FOUND,requestIndication.getTargetID()+" does not exist")) ;
        }
        // Check AccessRight
        errorResponse = checkAccessRight(accessRightID, requestIndication.getRequestingEntity(), Constants.AR_READ);
        if (errorResponse != null) {
        	em.close();
            return errorResponse;
        }
        
        Applications applications = DAOFactory.getApplicationsDAO().find(requestIndication.getTargetID(), em);
		applications.setMgmtObjsReference(applications.getUri() + Refs.MGMTOBJS_REF);
		applications.setSubscriptionsReference(applications.getUri() + Refs.SUBSCRIPTIONS_REF);
		applications.setAccessRightID(accessRightID);
		
		em.close();
		
        // Response
        return new ResponseConfirm(StatusCode.STATUS_OK, applications);
    }

    /**
     * Updates {@link Applications} resource.
     * @param requestIndication - The generic request to handle.
     * @return The generic returned response.
     */
    public ResponseConfirm doUpdate (RequestIndication requestIndication)  {

        // applicationCollection:       (updateReq NP) (response M)
        // applicationAnncCollection:   (updateReq NP) (response M)
        // subscriptionsReference:      (updateReq NP) (response M)
        // accessRightID:               (updateReq O)  (response O)
        // creationTime:                (updateReq NP) (response M)
        // lastModifiedTime:            (updateReq NP) (response M)

        return new ResponseConfirm(StatusCode.STATUS_NOT_IMPLEMENTED);
    }

    /**
     * Deletes {@link Applications} resource. It is not allowed Through the API.
     * @param requestIndication - The generic request to handle.
     * @return The generic returned response.
     */
    public ResponseConfirm doDelete (RequestIndication requestIndication) {

        // Response
        return new ResponseConfirm(new ErrorInfo(StatusCode.STATUS_METHOD_NOT_ALLOWED,requestIndication.getMethod()+" Method is not allowed"));
    }

    /**
     * Executes {@link Applications} resource.
     * @param requestIndication - The generic request to handle.
     * @return The generic returned response.
     */
    public ResponseConfirm doExecute (RequestIndication requestIndication) {

        // Response
        return new ResponseConfirm(new ErrorInfo(StatusCode.STATUS_NOT_IMPLEMENTED,requestIndication.getMethod()+" Method is not implmented"));
    }
}
