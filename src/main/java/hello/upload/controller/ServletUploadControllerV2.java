package hello.upload.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

@Slf4j
@Controller
@RequestMapping("/servlet/v2")
public class ServletUploadControllerV2 {
    // 파일 경로 가져오기 @Value
    @Value("${file.dir}")
    private String fileDir;
    @GetMapping("/upload")
    public String newFile() {
        return "upload-form";
    }

    @PostMapping("/upload")
    public String saveFileV1(HttpServletRequest request) throws ServletException, IOException {
        log.info("request={}", request);
        String itemName = request.getParameter("itemName");
        log.info("itemName={}", itemName);

        Collection<Part> parts = request.getParts();
        log.info("part={}", parts);
        //multipart-> 전송 데이터를 하나씩 part에 저장.
        //Part(서블릿이 지원)를 사용하여 멀티파트 형식을 편리하게 읽을 수 있다.
        for (Part part : parts) {
            //part도 헤더, 바디로 구분된다.
            log.info("==== PART ====");
            log.info("name={}", part.getName());
            //헤더들의 값을 출력한다.
            Collection<String> headerNames = part.getHeaderNames();
            for (String headerName : headerNames) {
                log.info("header {} {}", headerName, part.getName());

            }

            //편의 메서드
            //content-disposition : filename
            log.info("submittedFIlename={}", part.getSubmittedFileName());
            log.info("size={}", part.getSize());

            //데이터 읽기
            InputStream inputStream = part.getInputStream();
            String body = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            log.info("body={}", body);

            //파일에 저장하기(파일이 있는 경우만 파일 저장)
            if (StringUtils.hasText(part.getSubmittedFileName())) {
                String fullPath = fileDir + part.getSubmittedFileName();
                log.info("파일 저장 fullPath={}", fullPath);
                part.write(fullPath);

            }

        }

        return "upload-form";
    }
}
