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

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.ExtendedProperties;
import org.opencms.configuration.CmsConfigurationManager;
import org.opencms.db.CmsDbContext;
import org.opencms.db.CmsDriverManager;
import org.opencms.db.CmsResourceState;
import org.opencms.db.I_CmsDriver;
import org.opencms.db.I_CmsVfsDriver;
import org.opencms.db.generic.CmsSqlManager;
import org.opencms.file.CmsDataAccessException;
import org.opencms.file.CmsFile;
import org.opencms.file.CmsFolder;
import org.opencms.file.CmsProject;
import org.opencms.file.CmsProperty;
import org.opencms.file.CmsPropertyDefinition;
import org.opencms.file.CmsResource;
import org.opencms.file.CmsPropertyDefinition.CmsPropertyType;
import org.opencms.main.CmsException;
import org.opencms.main.OpenCms;
import org.opencms.relations.CmsRelation;
import org.opencms.relations.CmsRelationFilter;
import org.opencms.util.CmsPropertyUtils;
import org.opencms.util.CmsUUID;

public class RfsVfsDriver implements I_CmsVfsDriver, I_CmsDriver {
  
  private I_CmsVfsDriver successiveDriver;
  
  private CmsDriverManager driverManager;
  
  private CmsDbContext initContext;
  
  private RfsService rfsService;

  @Override
  public int countSiblings(CmsDbContext dbc, CmsUUID projectId,
      CmsUUID resourceId) throws CmsDataAccessException {
    debug();
    return successiveDriver.countSiblings(dbc, projectId, resourceId);
  }

  @Override
  public void createContent(CmsDbContext dbc, CmsUUID projectId,
      CmsUUID resourceId, byte[] content) throws CmsDataAccessException {
    debug();
    successiveDriver.createContent(dbc, projectId, resourceId, content);
  }

  @Override
  public CmsFile createFile(ResultSet res, CmsUUID projectId)
      throws SQLException {
    debug();
    return successiveDriver.createFile(res, projectId);
  }

  @Override
  public CmsFile createFile(ResultSet res, CmsUUID projectId,
      boolean hasFileContentInResultSet) throws SQLException {
    debug();
    return successiveDriver.createFile(res, projectId, hasFileContentInResultSet);
  }

  @Override
  public CmsFolder createFolder(ResultSet res, CmsUUID projectId,
      boolean hasProjectIdInResultSet) throws SQLException {
    debug();
    return successiveDriver.createFolder(res, projectId, hasProjectIdInResultSet);
  }

  @Override
  public void createOnlineContent(CmsDbContext dbc, CmsUUID resourceId,
      byte[] contents, int publishTag, boolean keepOnline,
      boolean needToUpdateContent) throws CmsDataAccessException {
    debug();
    successiveDriver.createOnlineContent(dbc, resourceId, contents, publishTag, keepOnline, needToUpdateContent);
  }

  @Override
  public CmsPropertyDefinition createPropertyDefinition(CmsDbContext dbc,
      CmsUUID projectId, String name, CmsPropertyType type)
      throws CmsDataAccessException {
    debug();
    return successiveDriver.createPropertyDefinition(dbc, projectId, name, type);
  }

  @Override
  public void createRelation(CmsDbContext dbc, CmsUUID projectId,
      CmsRelation relation) throws CmsDataAccessException {
    debug();
    successiveDriver.createRelation(dbc, projectId, relation);
  }

  @Override
  public CmsResource createResource(ResultSet res, CmsUUID projectId)
      throws SQLException {
    debug();
    return successiveDriver.createResource(res, projectId);
  }
  
  @Override
  public void createSibling(CmsDbContext dbc, CmsProject project,
      CmsResource resource) throws CmsDataAccessException {
    debug();
    successiveDriver.createSibling(dbc, project, resource);
  }

  @Override
  public void deletePropertyDefinition(CmsDbContext dbc,
      CmsPropertyDefinition name) throws CmsDataAccessException {
    debug();
    successiveDriver.deletePropertyDefinition(dbc, name);
  }

  @Override
  public void deletePropertyObjects(CmsDbContext dbc, CmsUUID projectId,
      CmsResource resource, int deleteOption) throws CmsDataAccessException {
    debug();
    successiveDriver.deletePropertyObjects(dbc, projectId, resource, deleteOption);
  }

  @Override
  public void deleteRelations(CmsDbContext dbc, CmsUUID projectId,
      CmsResource resource, CmsRelationFilter filter)
      throws CmsDataAccessException {
    debug();
    successiveDriver.deleteRelations(dbc, projectId, resource, filter);
  }

  @Override
  public void destroy() throws Throwable {
    debug();
    successiveDriver.destroy();
  }

  @Override
  public CmsSqlManager getSqlManager() {
    debug();
    return successiveDriver.getSqlManager();
  }

  @Override
  public CmsSqlManager initSqlManager(String classname) {
    debug();
    return successiveDriver.initSqlManager(classname);
  }

  @Override
  public void moveResource(CmsDbContext dbc, CmsUUID projectId,
      CmsResource source, String destinationPath) throws CmsDataAccessException {
    debug();
    successiveDriver.moveResource(dbc, projectId, source, destinationPath);
  }

  @Override
  public void publishResource(CmsDbContext dbc, CmsProject onlineProject,
      CmsResource onlineResource, CmsResource offlineResource)
      throws CmsDataAccessException {
    debug();
    successiveDriver.publishResource(dbc, onlineProject, onlineResource, offlineResource);
  }

  @Override
  public void publishVersions(CmsDbContext dbc, CmsResource resource,
      boolean firstSibling) throws CmsDataAccessException {
    debug();
    successiveDriver.publishVersions(dbc, resource, firstSibling);
  }

  @Override
  public CmsFolder readFolder(CmsDbContext dbc, CmsUUID projectId,
      CmsUUID folderId) throws CmsDataAccessException {
    debug();
    return successiveDriver.readFolder(dbc, projectId, folderId);
  }

  @Override
  public CmsFolder readFolder(CmsDbContext dbc, CmsUUID projectId,
      String foldername) throws CmsDataAccessException {
    debug();
    return successiveDriver.readFolder(dbc, projectId, foldername);
  }

  @Override
  public CmsPropertyDefinition readPropertyDefinition(CmsDbContext dbc,
      String name, CmsUUID projectId) throws CmsDataAccessException {
    debug();
    return successiveDriver.readPropertyDefinition(dbc, name, projectId);
  }

  @Override
  public List readPropertyDefinitions(CmsDbContext dbc, CmsUUID projectId)
      throws CmsDataAccessException {
    debug();
    return successiveDriver.readPropertyDefinitions(dbc, projectId);
  }

  @Override
  public CmsProperty readPropertyObject(CmsDbContext dbc, String key,
      CmsProject project, CmsResource resource) throws CmsDataAccessException {
    debug();
    return successiveDriver.readPropertyObject(dbc, key, project, resource);
  }

  @Override
  public List readPropertyObjects(CmsDbContext dbc, CmsProject project,
      CmsResource resource) throws CmsDataAccessException {
    debug();
    return successiveDriver.readPropertyObjects(dbc, project, resource);
  }

  @Override
  public List readRelations(CmsDbContext dbc, CmsUUID projectId,
      CmsResource resource, CmsRelationFilter filter)
      throws CmsDataAccessException {
    debug();
    return successiveDriver.readRelations(dbc, projectId, resource, filter);
  }
  @Override
  public List readResourceTree(CmsDbContext dbc, CmsUUID projectId,
      String parent, int type, CmsResourceState state, long startTime,
      long endTime, long releasedAfter, long releasedBefore, long expiredAfter,
      long expiredBefore, int mode) throws CmsDataAccessException {
    debug();
    return successiveDriver.readResourceTree(dbc, projectId, parent, type, state, startTime, endTime, releasedAfter, releasedBefore, expiredAfter, expiredBefore, mode);
  }

  @Override
  public List readResources(CmsDbContext dbc, CmsUUID currentProject,
      CmsResourceState state, int mode) throws CmsDataAccessException {
    debug();
    return successiveDriver.readResources(dbc, currentProject, state, mode);
  }

  @Override
  public List readResourcesForPrincipalACE(CmsDbContext dbc,
      CmsProject project, CmsUUID principalId) throws CmsDataAccessException {
    debug();
    return successiveDriver.readResourcesForPrincipalACE(dbc, project, principalId);
  }

  @Override
  public List readResourcesForPrincipalAttr(CmsDbContext dbc,
      CmsProject project, CmsUUID principalId) throws CmsDataAccessException {
    debug();
    return successiveDriver.readResourcesForPrincipalAttr(dbc, project, principalId);
  }

  @Override
  public List readResourcesWithProperty(CmsDbContext dbc, CmsUUID projectId,
      CmsUUID propertyDefinition, String path, String value)
      throws CmsDataAccessException {
    debug();
    return successiveDriver.readResourcesWithProperty(dbc, projectId, propertyDefinition, path, value);
  }

  @Override
  public List readSiblings(CmsDbContext dbc, CmsUUID projectId,
      CmsResource resource, boolean includeDeleted)
      throws CmsDataAccessException {
    debug();
    return successiveDriver.readSiblings(dbc, projectId, resource, includeDeleted);
  }

  @Override
  public Map readVersions(CmsDbContext dbc, CmsUUID projectId,
      CmsUUID resourceId, CmsUUID structureId) throws CmsDataAccessException {
    debug();
    return successiveDriver.readVersions(dbc, projectId, resourceId, structureId);
  }

  @Override
  public void removeFile(CmsDbContext dbc, CmsUUID projectId,
      CmsResource resource) throws CmsDataAccessException {
    debug();
    successiveDriver.removeFile(dbc, projectId, resource);
  }

  @Override
  public void replaceResource(CmsDbContext dbc, CmsResource newResource,
      byte[] newResourceContent, int newResourceType)
      throws CmsDataAccessException {
    debug();
    successiveDriver.replaceResource(dbc, newResource, newResourceContent, newResourceType);
  }

  @Override
  public void transferResource(CmsDbContext dbc, CmsProject project,
      CmsResource resource, CmsUUID createdUser, CmsUUID lastModifiedUser)
      throws CmsDataAccessException {
    debug();
    successiveDriver.transferResource(dbc, project, resource, createdUser, lastModifiedUser);
  }

  @Override
  public void updateRelations(CmsDbContext dbc, CmsProject onlineProject,
      CmsResource offlineResource) throws CmsDataAccessException {
    debug();
    successiveDriver.updateRelations(dbc, onlineProject, offlineResource);
  }

  @Override
  public boolean validateResourceIdExists(CmsDbContext dbc, CmsUUID projectId,
      CmsUUID resourceId) throws CmsDataAccessException {
    debug();
    return successiveDriver.validateResourceIdExists(dbc, projectId, resourceId);
  }

  @Override
  public boolean validateStructureIdExists(CmsDbContext dbc, CmsUUID projectId,
      CmsUUID structureId) throws CmsDataAccessException {
    debug();
    if (rfsService.isRfsResource(structureId)) return true;
    return successiveDriver.validateStructureIdExists(dbc, projectId, structureId);
  }

  @Override
  public void writeContent(CmsDbContext dbc, CmsUUID resourceId, byte[] content)
      throws CmsDataAccessException {
    debug();
    successiveDriver.writeContent(dbc, resourceId, content);
  }

  @Override
  public void writeLastModifiedProjectId(CmsDbContext dbc, CmsProject project,
      CmsUUID projectId, CmsResource resource) throws CmsDataAccessException {
    debug();
    successiveDriver.writeLastModifiedProjectId(dbc, project, projectId, resource);
  }

  @Override
  public void writePropertyObject(CmsDbContext dbc, CmsProject project,
      CmsResource resource, CmsProperty property) throws CmsDataAccessException {
    debug();
    successiveDriver.writePropertyObject(dbc, project, resource, property);
  }

  @Override
  public void writePropertyObjects(CmsDbContext dbc, CmsProject project,
      CmsResource resource, List properties) throws CmsDataAccessException {
    debug();
    successiveDriver.writePropertyObjects(dbc, project, resource, properties);
  }

  @Override
  public void init(CmsDbContext dbc,
      CmsConfigurationManager configurationManager, List successiveDrivers,
      CmsDriverManager driverManager) throws CmsException {
    debug();
    
    rfsService = new RfsService();

    initContext = dbc;
    
    this.driverManager = driverManager;
    
    //
    // TODO: substitute with refresh context menu entry
    OpenCms.getMemoryMonitor().setCacheResourceList(false);

    try {
      ExtendedProperties configuration = CmsPropertyUtils.loadProperties(OpenCms.getSystemInfo()
          .getAbsoluteRfsPathRelativeToWebInf("config/xyz.properties"));
      
      //ldap = new LDAPConnection(configuration);
      
    } catch (IOException e) {
      // TODO Auto-generated catch block
      //e.printStackTrace();
    }
    
    ExtendedProperties config = (ExtendedProperties)configurationManager.getConfiguration();
    
    if (successiveDrivers != null && successiveDrivers.size() > 0) {
      String driverKey = (String)successiveDrivers.get(0) + ".vfs.driver";
      String driverName = config.getString(driverKey);
      List drivers = (successiveDrivers.size() > 1) ? successiveDrivers.subList(1, successiveDrivers.size()) : null;
  
      successiveDriver = (I_CmsVfsDriver)driverManager.newDriverInstance(configurationManager, driverName, drivers);

    }
    else {
      throw new RuntimeException("driver needs successive DB driver.");
    }
  }
  
  //
  // modified methods start here
  //




  @Override
  public void writeResourceState(CmsDbContext dbc, CmsProject project,
      CmsResource resource, int changed, boolean isPublishing)
      throws CmsDataAccessException {
    debug();
    if (rfsService.isRfsResource(resource)) {
      rfsService.setResourceState(resource, project);
    }
    successiveDriver.writeResourceState(dbc, project, resource, changed, isPublishing);
  }
  
  @Override
  public CmsFolder readParentFolder(CmsDbContext dbc, CmsUUID projectId,
      CmsUUID structureId) throws CmsDataAccessException {
    debug();
    if (rfsService.isRfsResource(structureId)) {
      RfsResource res = (RfsResource)rfsService.getRfsResource(structureId);
      // check if parent is RFS or VFS
      File f = new File(res.getRfsPath());
      File base = new File(res.getRfsBase());
      if (f.getParent().equals(base.getPath())) {
        // parent is mapping folder
        return successiveDriver.readFolder(dbc, projectId, 
            CmsResource.getParentFolder(((CmsResource)res).getRootPath()));
      }
      else {
        return rfsService.readParentFolder(dbc, projectId, structureId);
      }
    }
    return successiveDriver.readParentFolder(dbc, projectId, structureId);
  }
  
  @Override
  public void removeFolder(CmsDbContext dbc, CmsProject currentProject,
      CmsResource resource) throws CmsDataAccessException {
    debug();
    if (rfsService.isRfsMappingFolder(resource)) {
      // remove rfs resource states
      rfsService.removeRfsResourceStates(resource, currentProject);
    }
    successiveDriver.removeFolder(dbc, currentProject, resource);
  }

  @Override
  public void writeResource(CmsDbContext dbc, CmsUUID projectId,
      CmsResource resource, int changed) throws CmsDataAccessException {
    debug();
    if (rfsService.isRfsResource(resource)) {
      rfsService.setResourceState(resource, projectId);
      return;
    }
    successiveDriver.writeResource(dbc, projectId, resource, changed);
  }
  
  @Override
  public List readChildResources(CmsDbContext dbc, CmsProject currentProject,
      CmsResource resource, boolean getFolders, boolean getFiles)
      throws CmsDataAccessException {
    debug();
    
    List result = null;
    
    // if resource is a rfs resource or rfs mapping folder
    if (rfsService.isRfsMappingFolder(resource)) {
      CmsProperty titleProp = readPropertyObject(dbc, "Title", currentProject, resource);
      String rfsPath = titleProp.getValue();
      File rfsFile = new File(rfsPath);
      boolean test = rfsFile.exists();
      CmsResourceState state = CmsResourceState.STATE_UNCHANGED;
      if (resource.getState().equals(CmsResourceState.STATE_NEW)) {
        state = CmsResourceState.STATE_NEW;
      }
      else if (resource.getState().equals(CmsResourceState.STATE_DELETED)) {
        state = CmsResourceState.STATE_DELETED;
      }
      result = rfsService.rfsReadChildResources(rfsPath, rfsPath, 
          resource.getRootPath(), currentProject.getUuid(), dbc,
          getFolders, getFiles, state);
    }
    else if (rfsService.isRfsResource(resource) && resource.isFolder()) {
      RfsFolder folder = (RfsFolder)resource;
      CmsResourceState state = CmsResourceState.STATE_UNCHANGED;
      if (folder.getState().equals(CmsResourceState.STATE_NEW)) {
        state = CmsResourceState.STATE_NEW;
      }
      else if (folder.getState().equals(CmsResourceState.STATE_DELETED)) {
        state = CmsResourceState.STATE_DELETED;
      }
      result = rfsService.rfsReadChildResources(folder.getRfsPath(), folder.getRfsBase(), folder.getMountPath(), 
          currentProject.getUuid(), dbc, getFolders, getFiles, state);
    }
    else {
      result = successiveDriver.readChildResources(dbc, currentProject, resource, getFolders, getFiles);
    }
    return result;
  }

  @Override
  public CmsResource readResource(CmsDbContext dbc, CmsUUID projectId,
      CmsUUID structureId, boolean includeDeleted)
      throws CmsDataAccessException {
    debug();

    if (rfsService.isRfsResource(structureId)) {
      CmsResource res = rfsService.getRfsResource(structureId);
      return res;
    }
    return successiveDriver.readResource(dbc, projectId, structureId, includeDeleted);
  }

  @Override
  public CmsResource readResource(CmsDbContext dbc, CmsUUID projectId,
      String filename, boolean includeDeleted) throws CmsDataAccessException {
    debug();

    CmsResource res = null;
    try {
      res = successiveDriver.readResource(dbc, projectId, filename, includeDeleted);
      return res;
    } catch (CmsDataAccessException cdae) {
      RfsMappingData rmd = new RfsMappingData();
      if (isRfsMapped(dbc, projectId, filename, rmd)) {
        String mountPath = rmd.getMountPath();
        String rfsBasePath = rmd.getRfsBasePath();
        CmsResource mappingFolder = rmd.getMappingFolder();
        CmsResourceState state = CmsResourceState.STATE_UNCHANGED;
        if (mappingFolder.getState().equals(CmsResourceState.STATE_NEW)) {
          state = CmsResourceState.STATE_NEW;
        }
        else if (mappingFolder.getState().equals(CmsResourceState.STATE_DELETED)) {
          state = CmsResourceState.STATE_DELETED;
        }
        File baseFolder = new File(rfsBasePath);
        File test = new File(baseFolder, filename.substring(mountPath.length()));
        if (test.exists()) {
          String rfsPath = test.getPath();
          res = rfsService.initRfsResource(filename, test.isDirectory(), projectId, dbc, rfsPath, rfsBasePath, mountPath,
              state);
          return res;
        }
      }
      throw cdae;
    }
  }

  @Override
  public byte[] readContent(CmsDbContext dbc, CmsUUID projectId,
      CmsUUID resourceId) throws CmsDataAccessException {
    debug();

    if (rfsService.isRfsResource(resourceId)) {
      return rfsService.readContent(resourceId);
    }
    else {
      return successiveDriver.readContent(dbc, projectId, resourceId);
    }
  }

  @Override
  public CmsResource createResource(CmsDbContext dbc, CmsUUID projectId,
      CmsResource resource, byte[] content) throws CmsDataAccessException {
    debug();
    RfsMappingData rmd = new RfsMappingData();
    if (isRfsMapped(dbc, projectId, resource.getRootPath(), rmd)) {
      String mountPath = rmd.getMountPath();
      String rfsBasePath = rmd.getRfsBasePath();
      return rfsService.createResource(resource, dbc, projectId, rfsBasePath, mountPath);
    }
    else {
      return successiveDriver.createResource(dbc, projectId, resource, content);
    }
  }
  
  private boolean isRfsMapped(CmsDbContext dbc, CmsUUID projectId, String filename, final RfsMappingData result) {
    // read all rfs mapping folders and find best match
    try {
      List<CmsResource> resources = successiveDriver.readResourceTree(dbc, projectId, CmsDriverManager.READ_IGNORE_PARENT, 
          45536, null, CmsDriverManager.READ_IGNORE_TIME, CmsDriverManager.READ_IGNORE_TIME, 
          -1, -1, -1, -1, 0);
    
      for (CmsResource r: resources) {
        if (filename.startsWith(r.getRootPath())) {
          if (result != null) {
            CmsProject cp = driverManager.readProject(dbc, projectId);
            CmsProperty titleProp = readPropertyObject(dbc, "Title", cp, r);
            String rfsBasePath = titleProp.getValue();
            String mountPath = r.getRootPath();
            result.setData(r, mountPath, rfsBasePath);
          }
          return true;
        }
      }
    } catch (CmsDataAccessException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return false;
  }

  
  private void debug() {

    //System.out.println(Thread.currentThread().getStackTrace()[2].getMethodName());
  }
}
