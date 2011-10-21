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

import org.opencms.configuration.CmsConfigurationException;
import org.opencms.file.types.A_CmsResourceType;
import org.opencms.file.types.Messages;

public class RfsFileType extends A_CmsResourceType {
 


      /** The type id of this resource type. */
      private static final int RESOURCE_TYPE_ID = 45537;

      /** The name of this resource type. */
      private static final String RESOURCE_TYPE_NAME = "rfsfile";

      /** The static type id of this resource type. */
      private static int m_staticTypeId;

      /**
       * Default constructor, used to initialize member variables.<p>
       */
      public RfsFileType() {

          super();
          m_typeId = RESOURCE_TYPE_ID;
          m_typeName = RESOURCE_TYPE_NAME;
      }

      /**
       * Returns the static type id of this (default) resource type.<p>
       * 
       * @return the static type id of this (default) resource type
       */
      public static int getStaticTypeId() {

          return m_staticTypeId;
      }

      /**
       * Returns the static type name of this (default) resource type.<p>
       * 
       * @return the static type name of this (default) resource type
       */
      public static String getStaticTypeName() {

          return RESOURCE_TYPE_NAME;
      }

      /**
       * @see org.opencms.file.types.I_CmsResourceType#getLoaderId()
       */
      public int getLoaderId() {

          return RfsFileLoader.RESOURCE_LOADER_ID;
      }

      /**
       * @see org.opencms.file.types.A_CmsResourceType#initConfiguration(java.lang.String, java.lang.String, String)
       */
      public void initConfiguration(String name, String id, String className) throws CmsConfigurationException {

          if (!RESOURCE_TYPE_NAME.equals(name)) {
              // default resource type MUST have default name
              throw new CmsConfigurationException(Messages.get().container(
                  Messages.ERR_INVALID_RESTYPE_CONFIG_NAME_3,
                  this.getClass().getName(),
                  RESOURCE_TYPE_NAME,
                  name));
          }

          super.initConfiguration(RESOURCE_TYPE_NAME, id, className);
          // set static members with values from the configuration        
          m_staticTypeId = m_typeId;
      }
}
