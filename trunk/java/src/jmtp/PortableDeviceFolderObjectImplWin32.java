/*
 * Copyright 2007 Pieter De Rycke
 * 
 * This file is part of JMTP.
 * 
 * JTMP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as 
 * published by the Free Software Foundation, either version 3 of 
 * the License, or any later version.
 * 
 * JMTP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU LesserGeneral Public 
 * License along with JMTP. If not, see <http://www.gnu.org/licenses/>.
 */

package jmtp;

import java.io.File;
import java.io.IOException;

import be.derycke.pieter.com.COMException;

/**
 *
 * @author Pieter De Rycke
 */
class PortableDeviceFolderObjectImplWin32 extends PortableDeviceObjectImplWin32 implements PortableDeviceFolderObject{

    public PortableDeviceFolderObjectImplWin32(String objectID, PortableDeviceContentImplWin32 content,
            PortableDevicePropertiesImplWin32 properties) {
        
        super(objectID, content, properties);
    }
    
    public PortableDeviceObject[] getChildObjects() {
        try {
            String[] childIDs = content.listChildObjects(objectID);
            PortableDeviceObject[] objects = new PortableDeviceObject[childIDs.length];
            for(int i = 0; i < childIDs.length; i++)
            	objects[i] = WPDImplWin32.convertToPortableDeviceObject(childIDs[i], this.content, this.properties);
            
            return objects;
        }
        catch (COMException e) {
            return new PortableDeviceObject[0];
        }
    }

	public PortableDeviceAudioObject addAudioObject(File file,
			String artist, String title) throws IOException {
		
		try {
			PortableDeviceValuesImplWin32 trackValues = new PortableDeviceValuesImplWin32();
			trackValues.setStringValue(Win32WPDDefines.WPD_OBJECT_PARENT_ID, this.objectID);
			trackValues.setStringValue(Win32WPDDefines.WPD_OBJECT_ORIGINAL_FILE_NAME, file.getName());
			trackValues.setGuidValue(Win32WPDDefines.WPD_OBJECT_FORMAT, Win32WPDDefines.WPD_OBJECT_FORMAT_MP3);	//TODO nog manier vinden om type te detecteren
			trackValues.setGuidValue(Win32WPDDefines.WPD_OBJECT_CONTENT_TYPE, Win32WPDDefines.WPD_CONTENT_TYPE_AUDIO);
			trackValues.setStringValue(Win32WPDDefines.WPD_OBJECT_NAME, title);
			trackValues.setStringValue(Win32WPDDefines.WPD_MEDIA_ARTIST, artist);
			
	        return new PortableDeviceAudioObjectImplWin32(content.createObjectWithPropertiesAndData(trackValues, file),
	        		this.content, this.properties);
		}
		catch(COMException e) {
			throw new IOException(e);
		}
	}
    
}