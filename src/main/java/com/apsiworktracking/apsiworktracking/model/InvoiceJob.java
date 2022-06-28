package com.apsiworktracking.apsiworktracking.model;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InvoiceJob {

    private List<ShortJob> jobs;
    private Double sum;
}
