package com.miirrr.qrscan.services.engine;

import com.miirrr.qrscan.config.Config;
import com.miirrr.qrscan.entities.Position;
import com.miirrr.qrscan.entities.Shop;
import com.miirrr.qrscan.services.entities.PositionService;
import com.miirrr.qrscan.services.entities.PositionServiceImpl;
import com.miirrr.qrscan.services.entities.ShopService;
import com.miirrr.qrscan.services.entities.ShopServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
public class ReportExport implements Job {

    private static Config config;

    private static ShopService shopService;

    private static PositionService positionService;

    private static String outPath;

    public ReportExport() {
        config = Config.getConfig();
        shopService = new ShopServiceImpl();
        positionService = new PositionServiceImpl();
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        LocalDateTime dateTime = LocalDateTime.now().toLocalDate().atStartOfDay().plusDays(1);
        export(dateTime.minusWeeks(1), dateTime, null, null);
    }

    public void exportShop(Shop shop, String newInn) {
        LocalDateTime startOfWeek = LocalDateTime.now().with(ChronoField.DAY_OF_WEEK, 1).toLocalDate().atStartOfDay();
        LocalDateTime dateFrom = LocalDateTime.now().toLocalDate().atStartOfDay().minusWeeks(1);
        LocalDateTime dateTo = LocalDateTime.now().toLocalDate().atStartOfDay().plusDays(1);

        Map<String, ExportClass> positionsToExport = new HashMap<>();
        ArrayList<String> positionNames = positionService.findByDateAndShopId(dateFrom, dateTo, shop.getId())
                .stream().map(Position::getName).collect(Collectors.toCollection(ArrayList::new));
        if (!positionNames.isEmpty()) {
            ExportClass exportClass = new ExportClass();
            exportClass.setInn(shop.getInn());
            exportClass.setIpName(shop.getIpName());
            exportClass.setPositionNames(positionNames);
            positionsToExport.put(shop.getInn(), exportClass);

            String fileName = config.getOutPath() + "/" + shop.getShopCorpId() + "_" + shop.getIpName() + "_" + shop.getInn() + "_to_" + newInn + "_"
                    + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".xls";

            saveExcel(positionsToExport, fileName);
        }
    }

    public void export(LocalDateTime dateFrom, LocalDateTime dateTo, String ipInn, String path) {
        if (path == null) {
            outPath = config.getOutPath();
        } else {
            outPath = path;
        }

        Map<String, ExportClass> positionsToExport = new HashMap<>();

        if (ipInn == null) {
            for (Shop s : shopService.findAll()) {
                if (!positionsToExport.containsKey(s.getInn())) {
                    ArrayList<String> positionNames = positionService
                            .findByDateAndShopINN(dateFrom, dateTo, s.getInn())
                            .stream().map(Position::getName).collect(Collectors.toCollection(ArrayList::new));
                    if (!positionNames.isEmpty()) {
                        ExportClass exportClass = new ExportClass();
                        exportClass.setInn(s.getInn());
                        exportClass.setIpName(s.getIpName());
                        exportClass.setPositionNames(positionNames);
                        positionsToExport.put(s.getInn(), exportClass);
                    }
                }
            }
        } else {
            for (Shop s : shopService.findAll().stream().filter(s -> s.getInn().equals(ipInn)).toList()) {
                ArrayList<String> positionNames = positionService
                        .findByDateAndShopINN(dateFrom, dateTo, s.getInn())
                        .stream().map(Position::getName).collect(Collectors.toCollection(ArrayList::new));
                if (!positionNames.isEmpty()) {
                    ExportClass exportClass = new ExportClass();
                    exportClass.setInn(s.getInn());
                    exportClass.setIpName(s.getIpName());
                    exportClass.setPositionNames(positionNames);
                    positionsToExport.put(s.getInn(), exportClass);
                }
            }
        }
        if (!positionsToExport.isEmpty()) saveExcel(positionsToExport, null);
    }

    private void saveExcel(Map<String, ExportClass> exportClassMap, String fileName) {

        exportClassMap.forEach((inn, shop) -> {
                    short rowCount = 0;

                    String _fileName = fileName;
                    if (fileName == null) {
                        _fileName = outPath + "/" + shop.getIpName() + "_" + shop.getInn() + "_"
                                + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".xls";
                    }

                    HSSFWorkbook workbook = new HSSFWorkbook();
                    HSSFSheet sheet = workbook.createSheet(shop.getInn());

                    for (String positionName : shop.getPositionNames()) {
                        HSSFRow row = sheet.createRow(rowCount);
                        row.createCell(0).setCellValue(positionName);
                        rowCount++;
                    }

                    try {
                        FileOutputStream fileOut = new FileOutputStream(_fileName);
                        workbook.write(fileOut);
                        fileOut.close();
                        workbook.close();
                    } catch (IOException e) {
                        log.error(e.getLocalizedMessage());
                    }
                }
        );
    }
}
