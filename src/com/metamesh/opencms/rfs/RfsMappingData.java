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

import org.opencms.file.CmsResource;

public class RfsMappingData {

  private CmsResource mappingFolder;
  private String mountPath;
  private String rfsBasePath;

  public RfsMappingData() {
    super();
  }
  
  public RfsMappingData(CmsResource mappingFolder, String mountPath,
      String rfsBasePath) {
    super();
    this.mappingFolder = mappingFolder;
    this.mountPath = mountPath;
    this.rfsBasePath = rfsBasePath;
  }

  public CmsResource getMappingFolder() {
    return mappingFolder;
  }

  public String getMountPath() {
    return mountPath;
  }

  public String getRfsBasePath() {
    return rfsBasePath;
  }

  public void setData(CmsResource mappingFolder, String mountPath,
      String rfsBasePath) {
    this.mappingFolder = mappingFolder;
    this.mountPath = mountPath;
    this.rfsBasePath = rfsBasePath;
  }
  
  public void setMappingFolder(CmsResource mappingFolder) {
    this.mappingFolder = mappingFolder;
  }

  public void setMountPath(String mountPath) {
    this.mountPath = mountPath;
  }

  public void setRfsBasePath(String rfsBasePath) {
    this.rfsBasePath = rfsBasePath;
  }

}
