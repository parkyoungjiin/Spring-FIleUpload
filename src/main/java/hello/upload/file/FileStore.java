package hello.upload.file;

import hello.upload.domain.UploadFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileStore {
    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    //사진 저장
    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<UploadFile> storeFileResult = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if(!multipartFile.isEmpty()){
                storeFileResult.add(storeFile(multipartFile));
            }
        }
        return storeFileResult;
    }

    // 파일 저장 (Multipartfile로 실제로 저장한 후에 -> UploadFile로 반환)
    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }
        String originalFilename = multipartFile.getOriginalFilename();
        //image.png -> UUID.png 형태로 생성하기 위한 메서드 사용.
        String storeFileName = createStoreFileName(originalFilename);
        multipartFile.transferTo (new File(getFullPath(storeFileName)));

        return new UploadFile(originalFilename, storeFileName);


    }

    //서버에 저장할 파일명 생성 메서드.
    private String createStoreFileName(String originalFilename) {
        // 확장자 (ext) 찾는 메서드 사용.
        String ext = extractExt(originalFilename);
        // UUID
        String uuid = UUID.randomUUID().toString();
        // 서버에 저장할 파일 이름  : UUID.확장자
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        //pos는 확장자를 출력하기 위해 .의 위치 int로 저장.
        int pos = originalFilename.lastIndexOf(".");
        //subString(int index) : index이후의 문자열을 출력.
        //originalFilename에서 . 이후의 확장자를 return
        return originalFilename.substring(pos + 1);
    }
}
