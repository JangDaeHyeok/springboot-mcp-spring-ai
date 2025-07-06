package com.jdh.mcpSample.api.document.application.impl;

import com.jdh.mcpSample.api.document.application.DocumentGenerateService;
import com.jdh.mcpSample.api.document.application.DocumentStructureService;
import com.jdh.mcpSample.api.document.domain.entity.Document;
import com.jdh.mcpSample.api.document.domain.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Qualifier("excelGenerateServiceImpl")
public class ExcelGenerateServiceImpl implements DocumentGenerateService {

    @Value("${app.document.temp-dir}")
    private String tempDir;

    private final DocumentRepository documentRepository;

    private final DocumentStructureService documentStructureService;

    @Override
    public Document generateDocument(String title, String content) {
        Document excel = Document.processing(title);
        documentRepository.save(excel);

        try {
            // ai 서비스를 이용한 엑셀 구조 생성
            var excelStructure = documentStructureService.generateExcelStructure(title, content);

            // 엑셀 파일 생성
            String fileName = createExcelFile(title, excelStructure);

            // 완료 응답 생성
            String fileUrl = "/documents/" + excel.getId();
            String fileDownloadUrl = "/document/download/excel/" + excel.getId();

            // result
            excel = Document.completed(excel.getId(), title, fileName, fileUrl, fileDownloadUrl);
            documentRepository.save(excel);

            return excel;

        } catch (Exception e) {
            log.error("Failed to generate excel document: {}", e.getMessage(), e);

            // fail
            excel = Document.failed(excel.getId(), title, e.getMessage());
            documentRepository.save(excel);

            return excel;
        }
    }

    private String createExcelFile(String title, Map<String, List<List<String>>> sheetData) throws IllegalArgumentException {
        // 파일 이름 생성 (현재 시간 포함)
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String safeName = title.replaceAll("[^a-zA-Z0-9가-힣]", "_");
        String fileName = safeName + "_" + timestamp + ".xlsx";

        // 임시 디렉토리 확인
        Path dirPath = Paths.get(tempDir);
        if (!Files.exists(dirPath)) {
            try {
                Files.createDirectories(dirPath);
            } catch (IOException e) {
                throw new IllegalArgumentException("임시 디렉토리 생성 실패: " + e.getMessage(), e);
            }
        }

        // 엑셀 파일 생성
        try (Workbook workbook = new XSSFWorkbook()) {
            // 기본 스타일 설정
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle defaultStyle = createDefaultStyle(workbook);

            // 각 시트 생성
            for (Map.Entry<String, List<List<String>>> entry : sheetData.entrySet()) {
                String sheetName = entry.getKey();
                List<List<String>> rows = entry.getValue();

                Sheet sheet = workbook.createSheet(sheetName);

                // 데이터 입력
                for (int i = 0; i < rows.size(); i++) {
                    Row row = sheet.createRow(i);
                    List<String> cells = rows.get(i);

                    for (int j = 0; j < cells.size(); j++) {
                        Cell cell = row.createCell(j);
                        cell.setCellValue(cells.get(j));

                        // 첫 번째 행에는 헤더 스타일 적용
                        if (i == 0) {
                            cell.setCellStyle(headerStyle);
                        } else {
                            cell.setCellStyle(defaultStyle);
                        }
                    }
                }

                // 열 너비 자동 조정
                for (int i = 0; i < rows.get(0).size(); i++) {
                    sheet.autoSizeColumn(i);
                    // 최소 너비 설정
                    int currentWidth = sheet.getColumnWidth(i);
                    if (currentWidth < 3000) {
                        sheet.setColumnWidth(i, 3000);
                    } else if (currentWidth > 15000) {
                        sheet.setColumnWidth(i, 15000);
                    }
                }
            }

            // 파일 저장
            File outputFile = new File(tempDir, fileName);
            try (FileOutputStream fileOut = new FileOutputStream(outputFile)) {
                workbook.write(fileOut);
            }

            return fileName;

        } catch (IOException e) {
            throw new IllegalArgumentException("엑셀 파일 생성 실패: " + e.getMessage(), e);
        }
    }

    /**
     * 헤더 셀 스타일을 생성합니다.
     */
    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("맑은 고딕");
        font.setFontHeightInPoints((short) 11);
        font.setBold(true);
        style.setFont(font);

        return style;
    }

    private CellStyle createDefaultStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("맑은 고딕");
        font.setFontHeightInPoints((short) 10);
        style.setFont(font);

        return style;
    }

}
