package dynamic_content_delivery.resources;

import dynamic_content_delivery.api.DeltaContent;
import dynamic_content_delivery.core.FileContent;
import dynamic_content_delivery.core.FileState;
import dynamic_content_delivery.views.FileContentView;
import com.codahale.metrics.annotation.Timed;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.Instant;

@Path("/")
@Produces(MediaType.TEXT_HTML)
public class FileContentResource {
    private String fileName;

    public FileContentResource(String fileName) {
        this.fileName = fileName;
    }


    @GET
    @Timed
    @Consumes(MediaType.APPLICATION_JSON)
    public FileContentView viewGetFileContent(@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpServletRequest) throws IOException, InterruptedException {
        long epoch = Instant.now().toEpochMilli();
        long hash_code = get_key(httpHeaders,httpServletRequest,epoch);
        FileContent fileContent = new FileContent(this.fileName, hash_code, FileState.getContents(), epoch);
        return new FileContentView(fileContent);
    }


    @Path("/reload")
    @POST
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public DeltaContent viewPostFileContent(@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpServletRequest, String data) throws ParseException, IOException, InterruptedException {
        System.out.println("Got the post request");
        long epoch = (long) NumberFormat.getInstance().parse(data);
        long hash_code = get_key(httpHeaders,httpServletRequest,epoch);
        FileContent fileContent = new FileContent(this.fileName, hash_code, epoch);
        return new DeltaContent(hash_code,fileContent.getLines());
    }

    private long get_key(HttpHeaders httpHeaders, HttpServletRequest httpServletRequest, long epoch){
        String user_name = httpHeaders.getHeaderString("User-Agent") + httpServletRequest.getRemoteAddr() + epoch;
        return  user_name.hashCode();
    }

}
