package world.lixiang.controller;


import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import world.lixiang.entity.User;
import world.lixiang.entity.UserFiles;
import world.lixiang.service.UserFilesService;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Controller
public class UserFilesController {

    @Autowired
    UserFilesService userFilesService;

    /**
     * 在线预览
     * @param id
     * @param response
     * @throws IOException
     */
    @RequestMapping("/online")
    public void online(String id , HttpServletResponse response) throws IOException {
        //获取文件信息
        UserFiles userFiles = userFilesService.findById(id);
        //根据文件路径跟文件名称写入输入流中
        String realPath = ResourceUtils.getURL("classpath:").getPath() + "/static/" + userFiles.getPath();
        //获取文件输入流
        FileInputStream fileInputStream = new FileInputStream(new File(realPath , userFiles.getNewFileName()));
        //附件下载
        response.setHeader("content-disposition","inline;fileName="+ URLEncoder.encode(userFiles.getOldFileName(),"UTF8"));
        //获取文件输出流
        ServletOutputStream outputStream  =  response.getOutputStream();

        //拷贝文件
        IOUtils.copy(fileInputStream,outputStream);
        IOUtils.closeQuietly(fileInputStream);
        IOUtils.closeQuietly(outputStream);

    }
    /**
     * 文件上传
     * @param id
     * @param response
     * @throws IOException
     */
    @RequestMapping("/download")
    public void download(String id , HttpServletResponse response) throws IOException {
        //获取文件信息
        UserFiles userFiles = userFilesService.findById(id);
        //更新下载次数
        userFiles.setDowncounts(userFiles.getDowncounts()+1);
        userFilesService.update(userFiles);
        //根据文件路径跟文件名称写入输入流中
        String realPath = ResourceUtils.getURL("classpath:").getPath() + "/static/" + userFiles.getPath();
        //获取文件输入流
        FileInputStream fileInputStream = new FileInputStream(new File(realPath , userFiles.getNewFileName()));
        //附件下载
        response.setHeader("content-disposition","attachment;fileName="+ URLEncoder.encode(userFiles.getOldFileName(),"UTF8"));
        //获取文件输出流
        ServletOutputStream outputStream  =  response.getOutputStream();

        //拷贝文件
        IOUtils.copy(fileInputStream,outputStream);
        IOUtils.closeQuietly(fileInputStream);
        IOUtils.closeQuietly(outputStream);

    }

    @RequestMapping("/showAll")
    public String showAll(HttpSession session , Model model){
        User user =  (User) session.getAttribute("user");
         model.addAttribute("files",userFilesService.findAllById(user.getId()));
        return "/showAll";
    }

    @RequestMapping("/showAllJSON")
    @ResponseBody
    public List<UserFiles> showAllJSO(HttpSession session , Model model){
        User user =  (User) session.getAttribute("user");
       List<UserFiles> userFiles =  userFilesService.findAllById(user.getId());
        return userFiles;
    }

    /**
     * 文件上传
     * @param aaa
     * @param session
     * @return
     * @throws IOException
     */
    @RequestMapping("/addFiles")
    public String AddFies(MultipartFile aaa , HttpSession session ) throws IOException {
        User user =  (User) session.getAttribute("user");
        //获取文件原始名称
        String oldFileName =  aaa.getOriginalFilename();
        //获取文件的后缀名称
        String extension = "." +  FilenameUtils.getExtension(aaa.getOriginalFilename());
        //生成新的文件名称
        String newFileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+ UUID.randomUUID().toString().replace("-","") + extension;
        //文件的大小
        Long size = aaa.getSize();
        //文件的类型
        String contentType = aaa.getContentType();

        //处理根据日期生成目录
        String realPath =  ResourceUtils.getURL("classpath:").getPath() + "/static/files";
        String dateFormat = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String dateDirPath  =  realPath + "/" + dateFormat;
        File dataDir = new File(dateDirPath);
        if(!dataDir.exists())dataDir.mkdirs();

        //处理文件上传
        aaa.transferTo(new File(dataDir,newFileName));

        //将文件信息放入数据库保存
        UserFiles userFiles = new UserFiles();
        userFiles.setOldFileName(oldFileName).setNewFileName(newFileName).setExt(extension).setSize(size.toString()).setType(contentType)
        .setPath("files/" + dateFormat).setUserId(user.getId());

        userFilesService.addUserFiles(userFiles);
        return "redirect:/showAll";
    }

    @GetMapping("/delete")
    public String delete(String id) throws FileNotFoundException {
        UserFiles userFiles =  userFilesService.findById(id);

        String realPath  =  ResourceUtils.getURL("classpath:").getPath() + "/static/" + userFiles.getPath();
        File file = new File(realPath , userFiles.getNewFileName());
        if(file.exists()){
            file.delete();
        }

        userFilesService.delete(id);

        return "redirect:/showAll";
    }
}
