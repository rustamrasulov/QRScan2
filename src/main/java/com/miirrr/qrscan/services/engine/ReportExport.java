package com.miirrr.qrscan.services.engine;

import com.miirrr.qrscan.config.Config;
import com.miirrr.qrscan.entities.BaseEntity;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
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
        export(dateTime.minusWeeks(1), dateTime, null, null, true);
    }

    public void exportShop(Shop shop, String newInn) {
//        LocalDateTime startOfWeek = LocalDateTime.now().with(ChronoField.DAY_OF_WEEK, 1).toLocalDate().atStartOfDay();
        LocalDateTime dateFrom = LocalDateTime.now().toLocalDate().atStartOfDay().minusWeeks(1);
        LocalDateTime dateTo = LocalDateTime.now().toLocalDate().atStartOfDay().plusDays(1);

        Map<Long, ExportClass> positionsToExport = new HashMap<>();
        Map<Long, String> positionNames = positionService.findByDateAndShopId(dateFrom, dateTo, shop.getId())
                .stream().collect(Collectors.toMap(BaseEntity::getId, Position::getName));
        if (!positionNames.isEmpty()) {
            ExportClass exportClass = new ExportClass();
            exportClass.setInn(shop.getInn());
            exportClass.setIpName(shop.getIpName());
            exportClass.setName(shop.getName());
            exportClass.setPositionNames(positionNames);
            positionsToExport.put(shop.getId(), exportClass);

            String fileName = config.getOutPath() + "/" + shop.getShopCorpId() + "_"
                    + shop.getIpName() + "_" + shop.getInn() + "_to_" + newInn + "_"
                    + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".xls";

            if (saveExcelByINN(positionsToExport, fileName)) {
                updatePositions(positionsToExport);
            }
        }
    }

    public void exportOneShop(LocalDateTime dateFrom, LocalDateTime dateTo, Long shopId, String path) {
        if (path == null) {
            outPath = config.getOutPath();
        } else {
            outPath = path;
        }

        Shop s = shopService.findById(shopId);

        Map<String, ExportClass> positionsToExport = new HashMap<>();

        Map<Long, String> positionNames = positionService
                .findByDateAndShopId(dateFrom, dateTo, s.getId())
                .stream().collect(Collectors.toMap(BaseEntity::getId, Position::getName));
        if (!positionNames.isEmpty()) {
            ExportClass exportClass = new ExportClass();
            exportClass.setInn(s.getInn());
            exportClass.setIpName(s.getIpName());
            exportClass.setName(s.getName());
            exportClass.setPositionNames(positionNames);
            positionsToExport.put(s.getName(), exportClass);
        }

        saveExcelByShop(positionsToExport, outPath + "/" + removePunctuations(s.getName()) + "_(" + s.getInn()
                + ")_" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".xls");
    }

    public void export(LocalDateTime dateFrom, LocalDateTime dateTo, String ipInn, String path, boolean scheduled) {
        if (path == null) {
            outPath = config.getOutPath();
        } else {
            outPath = path;
        }

        Map<Long, ExportClass> positionsToExport = new HashMap<>();

        if (ipInn == null) {
            for (Shop s : shopService.findAll()) {
//                if (!positionsToExport.containsKey(s.getInn())) {
                if (!positionsToExport.containsKey(s.getId())) {
                    Map<Long, String> positionNames;
                    if (scheduled) {
                        positionNames = positionService
//                                .findByDateAndShopINN(dateFrom, dateTo, s.getInn())
                                .findByDateAndShopId(dateFrom, dateTo, s.getId())
                                .stream().filter(p -> !p.isScheduled()).collect(Collectors.toMap(BaseEntity::getId, Position::getName));
                    } else {
                        positionNames = positionService
//                                .findByDateAndShopINN(dateFrom, dateTo, s.getInn())
                                .findByDateAndShopId(dateFrom, dateTo, s.getId())
                                .stream().collect(Collectors.toMap(BaseEntity::getId, Position::getName));
                    }
                    createExportObject(positionsToExport, s, positionNames);
                }
            }
        } else {
            for (Shop s : shopService.findAll().stream().filter(s -> s.getInn().equals(ipInn)).collect(Collectors.toList())) {
                Map<Long, String> positionNames = positionService
//                                .findByDateAndShopINN(dateFrom, dateTo, s.getInn())
                        .findByDateAndShopId(dateFrom, dateTo, s.getId())
                        .stream().collect(Collectors.toMap(BaseEntity::getId, Position::getName));
                createExportObject(positionsToExport, s, positionNames);
            }
        }

        if (!positionsToExport.isEmpty()) {
            if (saveExcelByINN(positionsToExport, null) && scheduled) {
                updatePositions(positionsToExport);
            }
        }
    }

    private void createExportObject(Map<Long, ExportClass> positionsToExport, Shop s, Map<Long, String> positionNames) {
        if (!positionNames.isEmpty()) {
            ExportClass exportClass = new ExportClass();
            exportClass.setInn(s.getInn());
            exportClass.setIpName(s.getIpName());
            exportClass.setName(s.getName());
            exportClass.setPositionNames(positionNames);
            positionsToExport.put(s.getId(), exportClass);
        }
    }

    private void updatePositions(Map<Long, ExportClass> exportClassMap) {
        exportClassMap.values().forEach(e -> e.getPositionNames()
                .forEach((id, name) -> {
                            Position position = positionService.findById(id);
                            position.setScheduled(true);
                            positionService.save(position);
                        }
                )
        );
    }

    private boolean saveExcelByShop(Map<String, ExportClass> exportClassMap, String fileName) {
        AtomicBoolean saved = new AtomicBoolean(false);
        exportClassMap.forEach((inn, shop) -> {
            final short[] rowCount = {0};

            String _fileName = fileName;
            if (fileName == null) {
                _fileName = outPath + "/" + removePunctuations(shop.getName()) + "_(" + shop.getInn() + ")_"
                        + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".xls";
            }

            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet(shop.getInn());

            shop.getPositionNames().forEach((k, v) -> {
                HSSFRow row = sheet.createRow(rowCount[0]);
                row.createCell(0).setCellValue(v);
                rowCount[0]++;
            });
            try {
                FileOutputStream fileOut = new FileOutputStream(_fileName);
                workbook.write(fileOut);
                fileOut.close();
                workbook.close();
                saved.set(true);
            } catch (IOException e) {
                log.error(e.getLocalizedMessage());
            }
        });

        return saved.get();
    }

    private boolean saveExcelByINN(Map<Long, ExportClass> exportClassMap, String fileName) {
        AtomicBoolean saved = new AtomicBoolean(false);
        exportClassMap.forEach((id, shop) -> {
                    final short[] rowCount = {0};

                    String _fileName = fileName;
                    if (fileName == null) {
                        _fileName = outPath + "/"
                                + removePunctuations(shop.getIpName()) + "_" + shop.getInn() + "_"
                                + removePunctuations(shop.getName()) + "_"
                                + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".xls";
                    }

                    HSSFWorkbook workbook = new HSSFWorkbook();
                    HSSFSheet sheet = workbook.createSheet(shop.getInn());

                    shop.getPositionNames().forEach((k, v) -> {
                        HSSFRow row = sheet.createRow(rowCount[0]);
                        row.createCell(0).setCellValue(v);
                        rowCount[0]++;
                    });

                    try {
                        FileOutputStream fileOut = new FileOutputStream(_fileName);
                        workbook.write(fileOut);
                        fileOut.close();
                        workbook.close();
                        saved.set(true);
                    } catch (IOException e) {
                        log.error(e.getLocalizedMessage());
                    }
                }
        );
        return saved.get();
    }

    private String removePunctuations(String source) {
        return source.replaceAll("\\p{IsPunctuation}", "").replaceAll("\\s", "_");
    }
}
