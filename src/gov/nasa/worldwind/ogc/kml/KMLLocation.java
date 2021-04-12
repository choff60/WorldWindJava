/*
 * Copyright 2006-2009, 2017, 2020 United States Government, as represented by the
 * Administrator of the National Aeronautics and Space Administration.
 * All rights reserved.
 * 
 * The NASA World Wind Java (WWJ) platform is licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * NASA World Wind Java (WWJ) also contains the following 3rd party Open Source
 * software:
 * 
 *     Jackson Parser – Licensed under Apache 2.0
 *     GDAL – Licensed under MIT
 *     JOGL – Licensed under  Berkeley Software Distribution (BSD)
 *     Gluegen – Licensed under Berkeley Software Distribution (BSD)
 * 
 * A complete listing of 3rd Party software notices and licenses included in
 * NASA World Wind Java (WWJ)  can be found in the WorldWindJava-v2.2 3rd-party
 * notices and licenses PDF found in code directory.
 */

package gov.nasa.worldwind.ogc.kml;

import gov.nasa.worldwind.geom.Position;

/**
 * Represents the KML <i>Location</i> element and provides access to its contents.
 *
 * @author tag
 * @version $Id: KMLLocation.java 1171 2013-02-11 21:45:02Z dcollins $
 */
public class KMLLocation extends KMLAbstractObject
{
    private static final String LATITUDE_KEY="latitude";
    private static final String LONGITUDE_KEY="longitude";
    private static final String ALTITUDE_KEY="altitude";
    /**
     * Construct an instance.
     *
     * @param namespaceURI the qualifying namespace URI. May be null to indicate no namespace qualification.
     */
    public KMLLocation(String namespaceURI)
    {
        super(namespaceURI);
    }

    public Double getLongitude()
    {
        return (Double) this.getField(LONGITUDE_KEY);
    }

    public Double getLatitude()
    {
        return (Double) this.getField(LATITUDE_KEY);
    }

    public Double getAltitude()
    {
        return (Double) this.getField(ALTITUDE_KEY);
    }

    /**
     * Retrieves this location as a {@link Position}. Fields that are not set are treated as zero.
     *
     * @return Position object representing this location.
     */
    public Position getPosition()
    {
        Double lat = this.getLatitude();
        Double lon = this.getLongitude();
        Double alt = this.getAltitude();

        return Position.fromDegrees(
            lat != null ? lat : 0,
            lon != null ? lon : 0,
            alt != null ? alt : 0);
    }
    
    public void setPosition(Position pos) {
        this.setField(LATITUDE_KEY, pos.latitude.degrees);
        this.setField(LONGITUDE_KEY, pos.longitude.degrees);
        this.setField(ALTITUDE_KEY, pos.elevation);
    }
}
