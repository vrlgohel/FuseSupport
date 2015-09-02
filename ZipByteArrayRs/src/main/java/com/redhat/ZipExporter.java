package com.redhat;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Created by vgohel on 9/2/15.
 */
@Path("/zip/docs")
@Produces("{application/zip}")
public class ZipExporter {
    @POST
    public byte[] exportZip(String csv){
        return csv.getBytes();
    }
}
