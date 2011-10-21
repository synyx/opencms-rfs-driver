/**
 *  This file is part of "Metamesh RFS Driver for OpenCms".
 *
 *  "Metamesh RFS Driver for OpenCms" is free software: 
 *  you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.

 *  "Metamesh RFS Driver for OpenCms" is distributed in the 
 *  hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.

 *  You should have received a copy of the GNU General Public License
 *  along with "Metamesh RFS Driver for OpenCms".  
 *  If not, see <http://www.gnu.org/licenses/>.
 *  
 *  
 *  Copyright (c) Stephan Hartmann (http://www.metamesh.com)
 *  
 */
package com.metamesh.opencms.rfs;


import org.opencms.file.CmsFile;
import org.opencms.file.CmsObject;
import org.opencms.file.CmsResource;
import org.opencms.flex.CmsFlexController;
import org.opencms.loader.I_CmsResourceLoader;
import org.opencms.loader.Messages;
import org.opencms.main.CmsException;
import org.opencms.main.CmsLog;
import org.opencms.main.OpenCms;
import org.opencms.util.CmsRequestUtil;
import org.opencms.util.CmsStringUtil;
import org.opencms.workplace.CmsWorkplaceManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.ExtendedProperties;

public class RfsFileLoader implements I_CmsResourceLoader {

      /** The id of this loader. */
      public static final int RESOURCE_LOADER_ID = 45538;

      /** The maximum age for dumped contents in the clients cache. */
      private static long m_clientCacheMaxAge;

      /** The resource loader configuration. */
      private Map m_configuration;

      /**
       * The constructor of the class is empty and does nothing.<p>
       */
      public RfsFileLoader() {

          m_configuration = new TreeMap();
      }

      /**
       * @see org.opencms.configuration.I_CmsConfigurationParameterHandler#addConfigurationParameter(java.lang.String, java.lang.String)
       */
      public void addConfigurationParameter(String paramName, String paramValue) {

          m_configuration.put(paramName, paramValue);
      }

      /** 
       * Destroy this ResourceLoder, this is a NOOP so far.<p>
       */
      public void destroy() {

          // NOOP
      }

      /**
       * @see org.opencms.loader.I_CmsResourceLoader#dump(org.opencms.file.CmsObject, org.opencms.file.CmsResource, java.lang.String, java.util.Locale, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
       */
      public byte[] dump(
          CmsObject cms,
          CmsResource resource,
          String element,
          Locale locale,
          HttpServletRequest req,
          HttpServletResponse res) throws CmsException {

          return cms.readFile(resource).getContents();
      }

      /**
       * @see org.opencms.loader.I_CmsResourceLoader#export(org.opencms.file.CmsObject, org.opencms.file.CmsResource, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
       */
      public byte[] export(CmsObject cms, CmsResource resource, HttpServletRequest req, HttpServletResponse res)
      throws IOException, CmsException {

          CmsFile file = cms.readFile(resource);

          // if no request and response are given, the resource only must be exported and no
          // output must be generated
          if ((req != null) && (res != null)) {
              // overwrite headers if set as default
              for (Iterator i = OpenCms.getStaticExportManager().getExportHeaders().listIterator(); i.hasNext();) {
                  String header = (String)i.next();

                  // set header only if format is "key: value"
                  String[] parts = CmsStringUtil.splitAsArray(header, ':');
                  if (parts.length == 2) {
                      res.setHeader(parts[0], parts[1]);
                  }
              }
              load(cms, file, req, res);
          }

          return file.getContents();
      }

      /**
       * Will always return <code>null</code> since this loader does not 
       * need to be configured.<p>
       * 
       * @see org.opencms.configuration.I_CmsConfigurationParameterHandler#getConfiguration()
       */
      public Map getConfiguration() {

          // return the configuration in an immutable form
          return Collections.unmodifiableMap(m_configuration);
      }

      /**
       * @see org.opencms.loader.I_CmsResourceLoader#getLoaderId()
       */
      public int getLoaderId() {

          return RESOURCE_LOADER_ID;
      }

      public String getResourceLoaderInfo() {

          return "RFS File Loader";
      }

      /**
       * @see org.opencms.configuration.I_CmsConfigurationParameterHandler#initConfiguration()
       */
      public void initConfiguration() {

          ExtendedProperties config = new ExtendedProperties();
          config.putAll(m_configuration);

          String maxAge = config.getString("client.cache.maxage");
          if (maxAge == null) {
              m_clientCacheMaxAge = -1;
          } else {
              m_clientCacheMaxAge = Long.parseLong(maxAge);
          }

          if (CmsLog.INIT.isInfoEnabled()) {
              if (maxAge != null) {
                  CmsLog.INIT.info(Messages.get().getBundle().key(Messages.INIT_CLIENT_CACHE_MAX_AGE_1, maxAge));
              }
              CmsLog.INIT.info(Messages.get().getBundle().key(
                  Messages.INIT_LOADER_INITIALIZED_1,
                  this.getClass().getName()));
          }
      }

      /**
       * @see org.opencms.loader.I_CmsResourceLoader#isStaticExportEnabled()
       */
      public boolean isStaticExportEnabled() {

          return false;
      }

      /**
       * @see org.opencms.loader.I_CmsResourceLoader#isStaticExportProcessable()
       */
      public boolean isStaticExportProcessable() {

          return false;
      }

      /**
       * @see org.opencms.loader.I_CmsResourceLoader#isUsableForTemplates()
       */
      public boolean isUsableForTemplates() {

          return false;
      }

      /**
       * @see org.opencms.loader.I_CmsResourceLoader#isUsingUriWhenLoadingTemplate()
       */
      public boolean isUsingUriWhenLoadingTemplate() {

          return false;
      }

      /**
       * @see org.opencms.loader.I_CmsResourceLoader#load(org.opencms.file.CmsObject, org.opencms.file.CmsResource, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
       */
      public void load(CmsObject cms, CmsResource resource, HttpServletRequest req, HttpServletResponse res)
      throws IOException, CmsException {

          if (canSendLastModifiedHeader(resource, req, res)) {
              // no further processing required
              return;
          }

          // set response status to "200 - OK" (required for static export "on-demand")
          res.setStatus(HttpServletResponse.SC_OK);
          // set content length header
          res.setContentLength(resource.getLength());

          if (CmsWorkplaceManager.isWorkplaceUser(req)) {
              // prevent caching for Workplace users
              res.setDateHeader(CmsRequestUtil.HEADER_LAST_MODIFIED, System.currentTimeMillis());
              CmsRequestUtil.setNoCacheHeaders(res);
          } else {
              // set date last modified header
              res.setDateHeader(CmsRequestUtil.HEADER_LAST_MODIFIED, resource.getDateLastModified());

              // set "Expires" only if cache control is not already set
              if (!res.containsHeader(CmsRequestUtil.HEADER_CACHE_CONTROL)) {
                  long expireTime = resource.getDateExpired();
                  if (expireTime == CmsResource.DATE_EXPIRED_DEFAULT) {
                      expireTime--;
                      // flex controller will automatically reduce this to a reasonable value
                  }
                  // now set "Expires" header        
                  CmsFlexController.setDateExpiresHeader(res, expireTime, m_clientCacheMaxAge);
              }
          }

          service(cms, resource, req, res);
      }

      /**
       * @see org.opencms.loader.I_CmsResourceLoader#service(org.opencms.file.CmsObject, org.opencms.file.CmsResource, javax.servlet.ServletRequest, javax.servlet.ServletResponse)
       */
      public void service(CmsObject cms, CmsResource resource, ServletRequest req, ServletResponse res)
      throws CmsException, IOException {

        RfsFile rfsFile = (RfsFile)resource;
        FileInputStream fis = null;
        try {
          fis = new FileInputStream(rfsFile.getRfsPath());
          byte[] buffy = new byte[4096];
          int bytesRead;
          while((bytesRead = fis.read(buffy)) >= 0) {
            res.getOutputStream().write(buffy, 0, bytesRead);
            res.getOutputStream().flush();
          }
        }
        finally {
          if (fis != null) {
            fis.close();
          }
        }
      }

      /**
       * Checks if the requested resource must be send to the client by checking the "If-Modified-Since" http header.<p>
       * 
       * If the resource has not been modified, the "304 - not modified" 
       * header is send to the client and <code>true</code>
       * is returned, otherwise nothing is send and <code>false</code> is returned.<p>
       * 
       * @param resource the resource to check
       * @param req the current request
       * @param res the current response
       * 
       * @return <code>true</code> if the "304 - not modified" header has been send to the client
       */
      protected boolean canSendLastModifiedHeader(CmsResource resource, HttpServletRequest req, HttpServletResponse res) {

          // resource state must be unchanged
          if (resource.getState().isUnchanged()
          // the request must not have been send by a workplace user (we can't use "304 - not modified" in workplace
              && !CmsWorkplaceManager.isWorkplaceUser(req)
              // last modified header must match the time form the resource
              && CmsFlexController.isNotModifiedSince(req, resource.getDateLastModified())) {
              long now = System.currentTimeMillis();
              if ((resource.getDateReleased() < now) && (resource.getDateExpired() > now)) {
                  // resource is available and not expired 
                  CmsFlexController.setDateExpiresHeader(res, resource.getDateExpired(), m_clientCacheMaxAge);
                  // set status 304 - not modified
                  res.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                  return true;
              }
          }
          return false;
      }
}
