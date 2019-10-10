package com.small.spring.io;

import java.net.URL;

/**
 * @ClassName ResourceLoader
 * @Description TODO
 * @Author xiangke
 * @Date 2019/9/17 23:44
 * @Version 1.0
 **/
public class ResourceLoader {

    public Resource getResource(String location) {
        URL resource = this.getClass().getClassLoader().getResource(location);
        return new UrlResource(resource);
    }
}
