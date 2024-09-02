package com.example.spring6restmvc.services.csv;

import com.example.spring6restmvc.model.csv.BeerCsv;

import java.io.File;
import java.util.List;

public interface BeerCsvService {
    List<BeerCsv> parseCsv(File file);
}
