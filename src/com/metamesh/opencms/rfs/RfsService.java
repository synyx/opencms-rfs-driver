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
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.opencms.db.CmsDbContext;
import org.opencms.db.CmsResourceState;
import org.opencms.file.CmsFolder;
import org.opencms.file.CmsProject;
import org.opencms.file.CmsResource;
import org.opencms.util.CmsUUID;

public class RfsService {

  private HashMap<String, CmsResourceState> resourceStatesOffline = new HashMap<String, CmsResourceState>();
  private HashMap<String, CmsResourceState> resourceStatesOnline = new HashMap<String, CmsResourceState>();

  private HashMap<CmsUUID, CmsResource> resourceCache = new HashMap<CmsUUID, CmsResource>();

  public RfsService() {
  }
  
  public List<CmsResource> rfsReadChildResources(String rfsPath, String rfsBasePath, String mountPath,
      CmsUUID projectId, CmsDbContext dbc, boolean getFolders, boolean getFiles,
      CmsResourceState resourceState) {
    if (!rfsBasePath.endsWith(File.separator)) {
      rfsBasePath = rfsBasePath.concat(File.separator);
    }
    
    Set<CmsResource> result = Collections.emptySet();
    
    File rfsFile = new File(rfsPath);
    
    if (rfsFile.exists() && rfsFile.isDirectory() && rfsFile.canRead()) {
      File[] subFilesAndFolders = rfsFile.listFiles();
      if (subFilesAndFolders != null) {
        result = new TreeSet<CmsResource>(CmsResource.COMPARE_ROOT_PATH_IGNORE_CASE_FOLDERS_FIRST);
        for (File f: subFilesAndFolders) {
          String vfsPath = mountPath + f.getPath().substring(rfsBasePath.length()).replaceAll("\\\\", "/");
          if ((f.isDirectory() && getFolders) || (!f.isDirectory() && getFiles)) {
            result.add(initRfsResource(vfsPath, f.isDirectory(), projectId, dbc,
              f.getPath(), rfsBasePath, mountPath, resourceState));
          }
        }
      }
    }
    
    return new ArrayList<CmsResource>(result);
  }
  
  private String getRfsPath(String vfsPath) {
    return "";
  }
  
  public CmsResource initRfsResource(String path,
      boolean isFolder, CmsUUID projectId, CmsDbContext dbc, String rfsPath, String rfsBase,
      String mountPath, CmsResourceState resourceState) {
    if (isFolder && path.charAt(path.length() - 1) != '/') {
      path = path.concat("/");
    }
    
    CmsResource res = null;
    CmsUUID id = CmsUUID.getConstantUUID(path);
    if (CmsProject.ONLINE_PROJECT_ID.equals(projectId)) {
      if (resourceStatesOnline.containsKey(path)) {
        resourceState = resourceStatesOnline.get(path);
      }
    }
    else if (resourceStatesOffline.containsKey(path)) {
      resourceState = resourceStatesOffline.get(path);
    }
    
    File f = new File(rfsPath);
    
    if (isFolder) {
      res = new RfsFolder(
        id,  
        id,
        path,
        45537, 0, projectId,
        resourceState,
        1, dbc.currentUser().getId(), f.lastModified(), dbc.currentUser().getId(),
        1, Long.MAX_VALUE, 1, rfsPath, rfsBase, mountPath);
    }
    else {
      res = new RfsFile(
        id,  
        id,
        path,
        45538, 0, projectId,
        resourceState,
        1, dbc.currentUser().getId(), f.lastModified(), dbc.currentUser().getId(),
        1, Long.MAX_VALUE, 1, (int)f.length(), 0, 1, new byte[]{}, rfsPath, rfsBase, mountPath);
    }
    resourceCache.put(id, res);
    return res;
  }
  
  public CmsResource createResource(CmsResource toCreate, CmsDbContext dbc,
      CmsUUID projectId, String rfsBasePath, String mountPath) {
    String filename = toCreate.getRootPath();
    File base = new File(rfsBasePath);
    File newFile = new File(base, filename.substring(mountPath.length()));
    try {
      newFile.createNewFile();
      return initRfsResource(filename, toCreate.isFolder(), projectId, dbc, newFile.getPath(), rfsBasePath, mountPath,
          CmsResourceState.STATE_NEW);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }
  
  public boolean isRfsResource(CmsResource resource) {
    
    return (resource instanceof RfsResource);
  }
  
  public boolean isRfsResource(CmsUUID uuid) {
    return resourceCache.containsKey(uuid);
  }
  public CmsResource getRfsResource(CmsUUID uuid) {
    return resourceCache.get(uuid);
  }
  public byte[] readContent(CmsUUID resourceId) {

    CmsResource res = getRfsResource(resourceId);
    if (res != null && res instanceof RfsFile) {
      try {
        File file = new File(((RfsFile)res).getRfsPath());
        FileInputStream fis = new FileInputStream(file);
        byte[] buffy = new byte[(int)file.length()];
        fis.read(buffy);
        fis.close();
        return buffy;
      }
      catch (IOException ioe) {
        ioe.printStackTrace();
      }
    }
    return new byte[0];
  }
  
  public void setResourceState(CmsResource resource, CmsProject project) {
    setResourceState(resource, project.getUuid());
  }
  
  public void setResourceState(CmsResource resource, CmsUUID projectId) {
    CmsResourceState state = resource.getState();
    if (CmsResourceState.STATE_CHANGED.equals(state)) {
      state = CmsResourceState.STATE_UNCHANGED;
    }
    if (CmsProject.ONLINE_PROJECT_ID.equals(projectId)) {
      resourceStatesOnline.put(resource.getRootPath(), state);
    }
    else {
      resourceStatesOffline.put(resource.getRootPath(), state);
    }
  }
  
  public boolean isRfsMappingFolder(CmsResource res) {
    return res.getTypeId() == 45536;
  }
  
  public void removeRfsResourceStates(CmsResource mappingFolder, CmsProject project) {
    HashMap<String, CmsResourceState> states;
    if (project.isOnlineProject()) {
      states = resourceStatesOnline;
    }
    else {
      states = resourceStatesOffline;
    }
    
    Iterator<String> it = states.keySet().iterator();
    while (it.hasNext()) {
      if (it.next().startsWith(mappingFolder.getRootPath())) {
        it.remove();
      }
    }
  }
  
  public CmsFolder readParentFolder(CmsDbContext dbc, CmsUUID projectId, CmsUUID resId) {
    CmsResource res = resourceCache.get(resId);
    String vfsParentPath = CmsResource.getParentFolder(res.getRootPath());
    RfsResource rfsRes = (RfsResource)res;
    String rfsPath = rfsRes.getRfsPath();
    File f = new File(rfsPath);
    File parent = f.getParentFile();
    return (CmsFolder)initRfsResource(vfsParentPath, true, 
        projectId, dbc, parent.getPath(), 
        rfsRes.getRfsBase(), rfsRes.getMountPath(), res.getState());
  }
}
