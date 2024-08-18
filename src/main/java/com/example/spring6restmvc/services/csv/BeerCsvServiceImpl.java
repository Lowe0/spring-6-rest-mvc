package com.example.spring6restmvc.services.csv;

import com.example.spring6restmvc.model.csv.BeerCsv;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

@Service
public class BeerCsvServiceImpl implements BeerCsvService {
    @Override
    public List<BeerCsv> parseCsv(File file) {
        try {
            return new CsvToBeanBuilder<BeerCsv>(new FileReader(file))
                    .withType(BeerCsv.class)
                    .build().parse();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
