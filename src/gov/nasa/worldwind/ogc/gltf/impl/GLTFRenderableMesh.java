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

package gov.nasa.worldwind.ogc.gltf.impl;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import java.util.ArrayList;
import java.nio.FloatBuffer;

import gov.nasa.worldwind.render.meshes.Mesh3D;
import gov.nasa.worldwind.ogc.gltf.*;
import gov.nasa.worldwind.render.DrawContext;
import gov.nasa.worldwind.render.meshes.AbstractGeometry;
import gov.nasa.worldwind.geom.Matrix;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.geom.Vec4;
import gov.nasa.worldwind.render.BasicShapeAttributes;
import gov.nasa.worldwind.render.ShapeAttributes;
import gov.nasa.worldwind.util.typescript.TypeScriptImports;

@TypeScriptImports(imports = "../../../render/meshes/AbstractGeometry,../../../geom/Matrix,../../../util/java/Buffers,../../../util/FloatBuffer,../../../geom/Vec4,../../../render/DrawContext,../GLTFUtil,./GLTFRenderer,./GLTFMeshGeometry,../GLTFMesh,../../../render/meshes/Mesh3D,../../../WorldWind,../../../shapes/ShapeAttributes")
public class GLTFRenderableMesh extends Mesh3D {


    private GLTFMesh srcMesh;
    private ArrayList<GLTFMeshGeometry> renderableGeometries;

    public GLTFRenderableMesh(GLTFMesh srcMesh, GLTFRenderer renderContext) {
        super();
        this.srcMesh = srcMesh;
        this.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);
        this.setElementType(GL.GL_TRIANGLES);
        ShapeAttributes attrs = renderContext.getAttributes();
        if (srcMesh.getMaterial() != null) {
            attrs = new BasicShapeAttributes(attrs);
            GLTFUtil.computeMaterialAttrs(attrs, srcMesh.getMaterial());
        }
        this.setAttributes(attrs);
    }

    protected void assembleRenderableGeometries(DrawContext dc) {
        this.setVertsPerShape(3);
        this.renderableGeometries = new ArrayList<>();
        Vec4[] vtxBuffer = this.srcMesh.getVertexBuffer();
        Vec4[] normalBuffer = this.srcMesh.getNormalBuffer();
        int[] bufferIndices = this.srcMesh.getBufferIndices();
        FloatBuffer vertices = Buffers.newDirectFloatBuffer(bufferIndices.length * 3);
        FloatBuffer normals = null;
        if (normalBuffer != null) {
            normals = Buffers.newDirectFloatBuffer(bufferIndices.length * 3);
        }

        for (int i = 0; i < bufferIndices.length; i++) {
            int idx = bufferIndices[i];
            Vec4 vtx = vtxBuffer[idx];
            vertices.put((float) vtx.x);
            vertices.put((float) vtx.y);
            vertices.put((float) vtx.z);
            if (normals != null) {
                Vec4 normal = normalBuffer[idx];
                normals.put((float) normal.x);
                normals.put((float) normal.y);
                normals.put((float) normal.z);
            }
        }
        this.renderableGeometries.add(new GLTFMeshGeometry(vertices, normals));
        this.setRenderableGeometries(this.renderableGeometries);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void renderOriented(DrawContext dc, Matrix transform) {
        if (this.renderableGeometries == null) {
            this.assembleRenderableGeometries(dc);
        }

        super.renderOriented(dc, transform);
    }

    @Override
    protected boolean isDoubleSided(AbstractGeometry geometry) {
        return true;
    }
}