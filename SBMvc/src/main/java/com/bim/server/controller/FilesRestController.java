package com.bim.server.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bim.server.model.User;
import com.bim.server.util.FileUtil;

/**
 * Upload files Controller
 */
@RestController
@RequestMapping("/api/files")
public class FilesRestController {
	
	@Autowired
	private ServletContext servletContext;

    @Autowired
    private Environment env;

	@RequestMapping(value = "upload", method = RequestMethod.POST)
	@Transactional(rollbackFor = Exception.class)
	@ResponseBody
	public Object upload(@RequestParam("filePath") String filePath, @RequestParam("id") Integer id, @RequestParam("files") MultipartFile[] files) throws IOException {
		filePath = "attachment.path";
		// Prepare file path.
	    final String absolutePath = servletContext.getRealPath("/") + env.getProperty(filePath) + (id == null ? "" : (id.toString() + File.separatorChar));
	    List<String> fileNames = FileUtil.saveFilesToLocal(absolutePath, files);
	    
	    // Return result.
	    Map<String, Object> result = new HashMap<String, Object>();
	    result.put("fileNames", fileNames);
	    return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}

    @RequestMapping(value = "download", method = RequestMethod.GET)
    @ResponseBody 
    public Resource download(HttpServletRequest request, HttpServletResponse response, @RequestParam("filePath") String filePath, @RequestParam("id") Integer id, @RequestParam("fileName") String fileName) throws IOException {
	    // Prepare file path.
    	final String absolutePath = servletContext.getRealPath("/") + env.getProperty(filePath) + (id == null ? "" : (id.toString() + File.separatorChar)) + fileName;
    	// Create header.
	    String mimeType = servletContext.getMimeType(absolutePath);
	    if(mimeType == null) {
	    	mimeType = "application/octet-stream";
	    }
        response.setContentType(mimeType);
	    response.addHeader("Content-Disposition", "attachmnet; filename=" + absolutePath);
	    // Return resource.
        return new FileSystemResource(absolutePath);
    }

    @RequestMapping(value = "download-fullname", method = RequestMethod.GET)
    @ResponseBody 
    public Resource downloadByFullName(HttpServletRequest request, HttpServletResponse response, @RequestParam("fileFullName") String fileFullName) throws IOException {
	    // Prepare file path.
    	String absolutePath = servletContext.getRealPath("/") + fileFullName;
    	// Create header.
	    String mimeType = servletContext.getMimeType(absolutePath);
	    if(mimeType == null) {
	    	mimeType = "application/octet-stream";
	    }
        response.setContentType(mimeType);
	    response.addHeader("Content-Disposition", "attachmnet; filename=" + absolutePath);
	    // Return resource.
        return new FileSystemResource(absolutePath);
    }

}
