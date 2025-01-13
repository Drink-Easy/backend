package com.drinkeg.drinkeg.domain.wine.service;

import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import com.drinkeg.drinkeg.domain.wine.repository.WineRepository;
import com.drinkeg.drinkeg.infra.storage.StoragePathName;
import com.drinkeg.drinkeg.infra.storage.StorageService;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;
import com.opencsv.exceptions.CsvException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminWineService {
    private final WineRepository wineRepository;
    private final StorageService storageService;

    // todo : 와인 초기데이터 업로드 후 와인 업로드 및 업데이트 로직 상의 후 삭제하거나 수정
    public void uploadWineImage() throws IOException {
        List<Wine> wines = wineRepository.findAll();

        for (Wine wine : wines) {
            if (wine.getImageUrl() == null) {
                String imageName = wine.getName().toLowerCase().replace("'", "").replace(" ", "-") + ".jpg";
                File imageFile = new File(System.getenv("IMAGE_PATH")+ imageName);

                if (imageFile.exists()) {
                    MultipartFile multipartFile = new CustomMultipartFile(imageFile);
                    String imageUrl = storageService.uploadFile(multipartFile, StoragePathName.WINE);
                    wine.updateImageUrl(imageUrl);
                    wineRepository.save(wine);
                }
            }
        }
    }

    public void uploadWineImageCSV() throws IOException, CsvException {
        // CSV 파일 경로
        File csvFile = new File("CSV_INPUT_FILE_PATH");

        // 구분자 ';'로 설정한 CSVReader 사용
        CSVReader csvReader = new CSVReaderBuilder(new FileReader(csvFile))
                .withCSVParser(new com.opencsv.CSVParserBuilder().withSeparator(';').build()) // 구분자 설정
                .build();
        List<String[]> csvData = csvReader.readAll();
        csvReader.close();

        // CSV 헤더가 있을 경우 이를 처리하는 코드 (예: 첫 번째 행은 헤더일 수 있음)
        String[] header = csvData.get(0); // 첫 번째 행을 헤더로 설정
        int imgNameIndex = -1;
        int uploadImageUrlIndex = -1;
        int vivinoImageUrlIndex = -1;

        // 헤더에서 'img_name'과 'upload_image_url'의 인덱스를 찾음
        for (int i = 0; i < header.length; i++) {
            if ("img_name".equals(header[i])) {
                imgNameIndex = i;
            }
            if ("upload_image_url".equals(header[i])) {
                uploadImageUrlIndex = i;
            }
            if ("vivino_image_url".equals(header[i])) {
                vivinoImageUrlIndex = i;
            }
        }

        // 인덱스가 잘못되었으면 처리할 예외를 추가할 수 있음
        if (imgNameIndex == -1 || uploadImageUrlIndex == -1) {
            throw new IOException("CSV 파일에 'img_name' 또는 'upload_image_url' 컬럼이 없습니다.");
        }

        // 이미지 파일 경로
        String imageBasePath = "IMAGE_FOLDER_PATH";

        // 각 행을 처리하여 이미지 업로드 및 URL을 업데이트
        for (int i = 1; i < csvData.size(); i++) { // 첫 번째 행은 헤더이므로 1부터 시작
            String[] row = csvData.get(i);
            String imgName = row[imgNameIndex];
            String imagePath = imageBasePath + imgName; // 이미지 경로를 basePath와 결합
            File imageFile = new File(imagePath);

            if (imageFile.exists() && row[uploadImageUrlIndex].isEmpty() && !row[vivinoImageUrlIndex].equals("DEFAULT_IMAGE")) {
                try {
                    MultipartFile multipartFile = new CustomMultipartFile(imageFile);
                    String imageUrl = storageService.uploadFile(multipartFile, StoragePathName.WINE); // 업로드 서비스 호출
                    row[uploadImageUrlIndex] = imageUrl; // 업로드된 이미지 URL을 'upload_image_url' 열에 설정
                } catch (Exception e) {
                    // 업로드 실패 시 예외 처리
                    row[uploadImageUrlIndex] = "image upload failed";
                }
            }
        }

        // 수정된 데이터를 새로운 CSV 파일에 저장
        String outputCsvFilePath = "/CSV_OUTPUT_FILE_PATH";
        // CSVWriter 설정: 구분자를 ';'로 설정
        CSVWriter csvWriter = (CSVWriter) new CSVWriterBuilder(new FileWriter(outputCsvFilePath))
                .withSeparator(';') // 구분자 설정
                .build();
        csvWriter.writeAll(csvData);
        csvWriter.close();
    }
}
