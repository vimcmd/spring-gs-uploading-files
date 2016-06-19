package hello;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class FileUploadController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String provideUploadPhoto(Model model) {
        File rootFolder = new File(Application.ROOT);
        List<String> fileNames = Arrays.stream(rootFolder.listFiles())
                                       .map(file -> file.getName())
                                       .collect(Collectors.toList());

        model.addAttribute("files", Arrays.stream(rootFolder.listFiles())
                                          .sorted(Comparator.comparingLong(f -> -1 * f.lastModified()))
                                          .map(file -> file.getName())
                                          .collect(Collectors.toList()));

        return "uploadForm";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String handleFileUpload(@RequestParam("name") String name,
                                   @RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        if (name.contains("/")) {
            redirectAttributes.addFlashAttribute("message", "Folder separators not allowed");
            return "redirect:/";
        }

        if (name.contains("/")) {
            redirectAttributes.addFlashAttribute("message", "Relative pathnames not allowed");
            return "redirect:/";
        }

        if (!file.isEmpty()) {
            try {
                /**
                 * ATTENTION:
                 * In a production scenario,it is best to NOT load up filesystem of application with content.
                 * You more likely to store files in temporary location, database or perhaps in NoSQL store like MongoDB GridFS.
                 *
                 * NOTE:
                 * This code doesn't contain checks to protect against people uploading executables, etc. which could
                 * be used to launch attacks.
                 */
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(Application.ROOT + "/" + name)));

                FileCopyUtils.copy(file.getInputStream(), stream);

                stream.close();
                redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + name + "!");

            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("message", "You failed to upload " + name + " => " + e.getMessage());
            }
        } else {
            redirectAttributes.addFlashAttribute("message", "You failed to upload " + name + " because file is empty");
        }

        return "redirect:/";
    }
}
