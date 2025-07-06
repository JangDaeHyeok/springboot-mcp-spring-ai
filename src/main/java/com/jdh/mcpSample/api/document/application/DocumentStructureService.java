package com.jdh.mcpSample.api.document.application;

import java.util.List;
import java.util.Map;

public interface DocumentStructureService {

    Map<String, List<List<String>>> generateExcelStructure(String title, String content);

    List<Map<String, String>> generatePptStructure(String title, String content);

}
