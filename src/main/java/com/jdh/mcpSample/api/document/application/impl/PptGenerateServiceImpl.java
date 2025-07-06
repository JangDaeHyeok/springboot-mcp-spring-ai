package com.jdh.mcpSample.api.document.application.impl;

import com.jdh.mcpSample.api.document.application.DocumentGenerateService;
import com.jdh.mcpSample.api.document.application.DocumentStructureService;
import com.jdh.mcpSample.api.document.domain.entity.Document;
import com.jdh.mcpSample.api.document.domain.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.sl.usermodel.Placeholder;
import org.apache.poi.sl.usermodel.TextParagraph;
import org.apache.poi.xslf.usermodel.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.*;
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
@Qualifier("pptGenerateServiceImpl")
public class PptGenerateServiceImpl implements DocumentGenerateService {

    @Value("${app.document.temp-dir}")
    private String tempDir;

    private final DocumentRepository documentRepository;

    private final DocumentStructureService documentStructureService;

    @Override
    public Document generateDocument(String title, String content) {
        Document ppt = Document.processing(title);
        documentRepository.save(ppt);

        try {
            // ai를 이용한 ppt 구조 생성
            var pptStructure = documentStructureService.generatePptStructure(title, content);

            // ppt 파일 생성
            String fileName = createPowerPointFile(title, pptStructure);

            // 완료 응답 생성
            String fileUrl = "/documents/" + ppt.getId();
            String fileDownloadUrl = "/document/download/ppt/" + ppt.getId();

            // result
            ppt = Document.completed(ppt.getId(), title, fileName, fileUrl, fileDownloadUrl);
            documentRepository.save(ppt);

            return ppt;
        } catch (Exception e) {
            log.error("Failed to generate ppt document: {}", e.getMessage(), e);

            // fail
            ppt = Document.failed(ppt.getId(), title, e.getMessage());
            documentRepository.save(ppt);

            return ppt;
        }
    }

    private String createPowerPointFile(String title, List<Map<String, String>> slides) throws IllegalArgumentException {
        // 파일 이름 생성 (현재 시간 포함)
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String safeName = title.replaceAll("[^a-zA-Z0-9가-힣]", "_");
        String fileName = safeName + "_" + timestamp + ".pptx";

        // 임시 디렉토리 확인
        Path dirPath = Paths.get(tempDir);
        if (!Files.exists(dirPath)) {
            try {
                Files.createDirectories(dirPath);
            } catch (IOException e) {
                throw new IllegalArgumentException("임시 디렉토리 생성 실패: " + e.getMessage(), e);
            }
        }

        // PowerPoint 파일 생성
        try (XMLSlideShow ppt = new XMLSlideShow()) {
            // 마스터 슬라이드 레이아웃 설정
            XSLFSlideMaster defaultMaster = ppt.getSlideMasters().get(0);
            XSLFSlideLayout titleLayout = defaultMaster.getLayout(SlideLayout.TITLE);
            XSLFSlideLayout titleAndContentLayout = defaultMaster.getLayout(SlideLayout.TITLE_AND_CONTENT);

            // 첫 번째 슬라이드: 제목 슬라이드
            XSLFSlide titleSlide = ppt.createSlide(titleLayout);
            XSLFTextShape titleShape = titleSlide.getPlaceholder(0);
            titleShape.setText(title);
            titleShape.setFillColor(new Color(240, 240, 240));
            XSLFTextShape subtitleShape = titleSlide.getPlaceholder(1);
            if (subtitleShape != null) {
                subtitleShape.clearText();
                subtitleShape.setText("이 문서는 자동으로 생성되었습니다.");
                XSLFTextParagraph paragraph = subtitleShape.getTextParagraphs().get(0);
                XSLFTextRun run = paragraph.getTextRuns().get(0);
                run.setFontSize(28.);
            }

            // 스타일 설정
            XSLFTextParagraph titleParagraph = titleShape.getTextParagraphs().get(0);
            titleParagraph.setTextAlign(TextParagraph.TextAlign.CENTER);

            XSLFTextRun titleRun = titleParagraph.getTextRuns().get(0);
            titleRun.setFontSize(44.);
            titleRun.setFontFamily("맑은 고딕");
            titleRun.setBold(true);
            titleRun.setFontColor(new Color(44, 62, 80));

            // 슬라이드 생성
            for (Map<String, String> slideData : slides) {
                String slideTitle = slideData.getOrDefault("title", "");
                String slideContent = slideData.getOrDefault("content", "");
                String slideNotes = slideData.getOrDefault("notes", "");

                XSLFSlide slide = ppt.createSlide(titleAndContentLayout);

                // 제목 설정
                XSLFTextShape titlePlaceholder = slide.getPlaceholder(0);
                if (titlePlaceholder != null) {
                    titlePlaceholder.setText(slideTitle);
                    titlePlaceholder.setFillColor(new Color(240, 240, 240));

                    XSLFTextParagraph slideTitleParagraph = titlePlaceholder.getTextParagraphs().get(0);
                    XSLFTextRun slideTitleRun = slideTitleParagraph.getTextRuns().get(0);
                    slideTitleRun.setFontSize(32.);
                    slideTitleRun.setFontFamily("맑은 고딕");
                    slideTitleRun.setBold(true);
                    slideTitleRun.setFontColor(new Color(44, 62, 80));
                }

                // 내용 설정
                XSLFTextShape contentPlaceholder = slide.getPlaceholder(1);
                if (contentPlaceholder != null) {
                    contentPlaceholder.clearText();

                    // 내용 줄 단위로 분할
                    String[] lines = slideContent.split("\n");

                    for (String line : lines) {
                        XSLFTextParagraph paragraph = contentPlaceholder.addNewTextParagraph();

                        XSLFTextRun run = paragraph.addNewTextRun();
                        run.setText(line);
                        run.setFontSize(20.);
                        run.setFontFamily("맑은 고딕");
                    }
                }

                // 슬라이드 노트 추가
                if (!slideNotes.isBlank()) {
                    XSLFNotes notes = ppt.getNotesSlide(slide);

                    XSLFTextShape notesShape = null;

                    for (XSLFShape shape : notes.getShapes()) {
                        if (shape instanceof XSLFTextShape textShape &&
                                textShape.getTextType() == Placeholder.BODY) {
                            notesShape = textShape;
                            break;
                        }
                    }

                    if (notesShape != null) {
                        notesShape.clearText(); // 기본 텍스트 제거
                        notesShape.setText(slideNotes);
                    } else {
                        log.warn("슬라이드 노트 영역(Placeholder.BODY)을 찾을 수 없습니다.");
                    }
                }
            }

            // 파일 저장
            File outputFile = new File(tempDir, fileName);
            try (FileOutputStream fileOut = new FileOutputStream(outputFile)) {
                ppt.write(fileOut);
            }

            return fileName;

        } catch (IOException e) {
            throw new IllegalArgumentException("PowerPoint 파일 생성 실패: " + e.getMessage(), e);
        }
    }

}
