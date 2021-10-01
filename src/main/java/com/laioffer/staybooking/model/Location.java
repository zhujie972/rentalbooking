package com.laioffer.staybooking.model;

import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;

import java.io.Serializable;
// 代表酒店的经纬度信息

@Document(indexName = "loc") // doc 是做什么的？ 为什么没有用table？ indexName对应的是个DB
public class Location implements Serializable {

    private static final long serialVersionUID = 1L;
    @Field(type = FieldType.Long)
    private Long id; // 用于搜索返回，搜索经纬度之后要返回stay的结果，用id 链接

    @GeoPointField
    private GeoPoint geoPoint; // 经纬度信息

    public Location(Long id, GeoPoint geoPoint) {
        this.id = id;
        this.geoPoint = geoPoint;
    }

    public Long getId() {
        return id;
    }

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

}
