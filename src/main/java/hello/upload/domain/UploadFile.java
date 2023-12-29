package hello.upload.domain;

import lombok.Data;

@Data
public class UploadFile {
    private String uploadFileName;
    //다른 사용자가 같은 파일을 저장할 수 있기에 저장할 때는 UUID를 사용하여 저장할 예정이기에 2개의 필드를 만듦.
    private String storeFileName; //

    public UploadFile(String uploadFileName, String storeFileName) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }
}
