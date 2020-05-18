package com.google.cloud.binguo.demo;

import com.google.cloud.Timestamp;
import com.google.cloud.spanner.*;
import xyz.downgoon.snowflake.Snowflake;
import com.github.javafaker.Faker;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class App 
{
    public static void main( String[] args )
    {

        SpannerOptions options = SpannerOptions.newBuilder().build();
        Spanner spanner = options.getService();
        //Specify the instance id
        String instance = "testing";
        //Specify the database id
        String database = "db";

        Snowflake snowflake = new Snowflake(2,5);
        final int idNum = 1000;
        List<Mutation> mutations = new ArrayList<>();

        Faker faker = new Faker(new Locale("zh-CN"));

        for (int i = 0; i < idNum; i++) {
            long id = snowflake.nextId();
            mutations.add(
                    Mutation.newInsertBuilder("Orders")
                            .set("OrderId")
                            .to(id)
                            .set("UserName")
                            .to(faker.name().fullName())
                            .set("Amount")
                            .to(faker.number().randomDouble(2,1000,9999))
                            .set("OrderTime")
                            .to(Timestamp.now())
                            .build());
        }
        try {
            DatabaseId db = DatabaseId.of(options.getProjectId(), instance, database);
            DatabaseClient dbClient = spanner.getDatabaseClient(db);
            dbClient.write(mutations);
        } finally {
            spanner.close();
        }
        // [END init_client]
        System.out.println("Closed client");
    }
}