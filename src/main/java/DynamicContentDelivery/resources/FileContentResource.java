package DynamicContentDelivery.resources;

import DynamicContentDelivery.core.FileContent;
import DynamicContentDelivery.core.FileState;
import DynamicContentDelivery.views.FileContentView;
import com.codahale.metrics.annotation.Timed;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

@Path("/")
@Produces(MediaType.TEXT_HTML)
public class FileContentResource {
    private String fileName;
    private AtomicLong counter;

    public FileContentResource(String fileName) {
        this.fileName = fileName;
        this.counter = new AtomicLong();
    }

    @GET
    @Timed
    @Consumes(MediaType.APPLICATION_JSON)
    public FileContentView viewFileContent(@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpServletRequest) throws IOException {
        String user_name = httpHeaders.getHeaderString("User-Agent") + httpServletRequest.getRemoteAddr();
        long hash_code = user_name.hashCode();
        String dirName = System.getProperty("user.dir");
        FileState fileState = new FileState(dirName + "/" + this.fileName);
        if (fileState.getGlobal_num_lines() == 0) {
            fileState.init_Queue();
        }
        long fp = fileState.getFilePointer();
        FileContent fileContent = null;
        if (FileState.contextMap.get(hash_code) == null)
            fileContent = new FileContent(this.fileName, hash_code, fileState.getContents(), fp);
        else
            fileContent = new FileContent(this.fileName, hash_code, dirName + "/" + this.fileName);
        FileContentView fileContentView = new FileContentView(fileContent);
        return fileContentView;
    }
}
