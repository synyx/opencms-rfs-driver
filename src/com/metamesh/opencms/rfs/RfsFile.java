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

import org.opencms.db.CmsResourceState;
import org.opencms.file.CmsFile;
import org.opencms.file.CmsResource;
import org.opencms.util.CmsUUID;


class RfsFile extends CmsFile implements RfsResource {
  
  private static final long serialVersionUID = 1714180526727981004L;

  private String rfsPath;
  private String rfsBase;
  private String mountPath;

  public RfsFile(CmsResource resource) {
    super(resource);
  }

  public RfsFile(CmsUUID structureId, CmsUUID resourceId, String path,
      int type, int flags, CmsUUID projectId, CmsResourceState state,
      long dateCreated, CmsUUID userCreated, long dateLastModified,
      CmsUUID userLastModified, long dateReleased, long dateExpired,
      int linkCount, int length, long dateContent, int version, byte[] content,
      String rfsPath, String rfsBase, String mountPath) {
    super(structureId, resourceId, path, type, flags, projectId, state,
        dateCreated, userCreated, dateLastModified, userLastModified, dateReleased,
        dateExpired, linkCount, length, dateContent, version, content);
    this.rfsBase = rfsBase;
    this.rfsPath = rfsPath;
    this.mountPath = mountPath;
  }


  @Override
  public String getMountPath() {
    return mountPath;
  }

  @Override
  public String getRfsBase() {
    return rfsBase;
  }

  @Override
  public String getRfsPath() {
    return rfsPath;
  }
}