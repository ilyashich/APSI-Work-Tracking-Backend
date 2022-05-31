package com.apsiworktracking.apsiworktracking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class ApsiWorkTrackingApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(ApsiWorkTrackingApplication.class, args);
    }
    
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
      return builder.sources(ApsiWorkTrackingApplication.class);
  }

}
